package com.emtdev.morph.zookeeper.resolver;


import com.emtdev.morph.ImmutableRemoteAddressGroup;
import com.emtdev.morph.Listener;
import com.emtdev.morph.NameResolver;
import com.emtdev.morph.exception.MorphException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;


public class DefaultZookeeperResolver implements NameResolver, TreeCacheListener {

   private final CuratorFramework client;
   private final String serviceZookeeperPath;
   private final ObjectMapper objectMapper;


   private final CountDownLatch cacheReadyLatch = new CountDownLatch(1);
   private final ConcurrentHashMap<String, Listener> authorityMap = new ConcurrentHashMap<>();
   private final ConcurrentHashMap<String, List<ImmutableRemoteAddressGroup>> addressGroup = new ConcurrentHashMap<>();

   private TreeCache treeCache;

   public DefaultZookeeperResolver(CuratorFramework client, String serviceZookeeperPath, ObjectMapper objectMapper) {
      this.client = client;
      this.serviceZookeeperPath = serviceZookeeperPath;
      this.objectMapper = objectMapper;
      this.init();
   }


   private List<ImmutableRemoteAddressGroup> parseChildData(byte[] childData) throws RuntimeException {
      CollectionLikeType collectionLikeType = objectMapper.getTypeFactory()
              .constructCollectionLikeType(List.class, RemoteAddressGroup.class);
      try {
         List<RemoteAddressGroup> remoteAddressGroups = objectMapper.readValue(childData, collectionLikeType);
         return remoteAddressGroups.stream()
                 .map(RemoteAddressGroup::toImmutableRemoteAddressGroup).collect(Collectors.toList());
      } catch (IOException e) {
         throw new MorphException(e);
      }
   }

   private void init() {
      treeCache = new TreeCache(client, serviceZookeeperPath);
      treeCache.getListenable().addListener(this);
      try {
         treeCache.start();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   @Override
   public final String getScheme() {
      return "zookeeper";
   }

   @Override
   public final int priority() {
      return 1;
   }

   @Override
   public final void start(String authority, Listener listener) {
      if (authorityMap.containsKey(authority))
         throw new RuntimeException("should be never happen");

      authorityMap.put(authority, listener);

      //for already exist data
      if (addressGroup.containsKey(authority))
         refreshListenerAsync(listener, addressGroup.get(authority));
   }

   private void refreshListenerAsync(Listener listener, List<ImmutableRemoteAddressGroup> remoteAddressGroups) {
      Thread thread = new Thread(() -> listener.refresh(remoteAddressGroups));
      thread.start();
   }

   private void updatePathInfo() {
      Map<String, ChildData> currentChildren = treeCache.getCurrentChildren(serviceZookeeperPath);

      currentChildren.forEach((serviceName, childData) -> {

         final List<ImmutableRemoteAddressGroup> remoteAddressGroups = parseChildData(childData.getData());
         final Listener listener = authorityMap.get(serviceName);

         //for safety resasons
         addressGroup.put(serviceName, remoteAddressGroups);

         if (Objects.nonNull(listener)) {
            refreshListenerAsync(listener, remoteAddressGroups);
         }
      });
   }

   @Override
   public final void childEvent(CuratorFramework curatorFramework, TreeCacheEvent event) throws Exception {
      if (event == null || event.getType() == null) {
         // oops
         return;
      }

      switch (event.getType()) {
         case INITIALIZED:
            cacheReadyLatch.countDown();
            updatePathInfo();
            break;

         case NODE_ADDED:
         case NODE_REMOVED:
         case NODE_UPDATED:
            if (cacheReadyLatch.getCount() == 0) {
               updatePathInfo();
            }
            break;
         default:
            // ignore
            break;
      }
   }
}
