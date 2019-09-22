package com.emt.morph.zookeeper.resolver;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

import com.emt.morph.ImmutableRemoteAddressGroup;
import com.emt.morph.Listener;
import com.emt.morph.NameResolver;
import com.emt.morph.exception.MorphException;


public abstract class AbstractZookeeperResolver implements NameResolver, TreeCacheListener
{

    private final CuratorFramework client;
    private final String serviceZookeeperPath;

    private final CountDownLatch cacheReadyLatch = new CountDownLatch(1);
    private final ConcurrentHashMap<String, Listener> authorityMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<ImmutableRemoteAddressGroup>> addressGroup = new ConcurrentHashMap<>();

    private TreeCache treeCache;

    public AbstractZookeeperResolver(CuratorFramework client, String serviceZookeeperPath)
    {
        this.client = client;
        this.serviceZookeeperPath = serviceZookeeperPath;
        this.init();
    }

    private void init()
    {
        treeCache = new TreeCache(client, serviceZookeeperPath);
        treeCache.getListenable().addListener(this);
    }

    protected abstract List<ImmutableRemoteAddressGroup> parseChildData(byte[] childData) throws MorphException;

    @Override
    public String getScheme()
    {
        return "zookeeper";
    }

    @Override
    public int priority()
    {
        return 1;
    }

    @Override
    public void start(String authority, Listener listener)
    {
        if (authorityMap.containsKey(authority))
            throw new RuntimeException("should be never happen");

        authorityMap.put(authority, listener);

        //for already exist data
        if (addressGroup.containsKey(authority))
            refreshListenerAsync(listener, addressGroup.get(authority));
    }

    private void refreshListenerAsync(Listener listener, List<ImmutableRemoteAddressGroup> remoteAddressGroups)
    {
        Thread thread = new Thread(() -> listener.refresh(remoteAddressGroups));
        thread.start();
    }

    private void updatePathInfo()
    {
        Map<String, ChildData> currentChildren = treeCache.getCurrentChildren(serviceZookeeperPath);

        currentChildren.forEach((serviceName, childData) -> {

            final List<ImmutableRemoteAddressGroup> remoteAddressGroups = parseChildData(childData.getData());
            final Listener listener = authorityMap.get(serviceName);

            //for safety resasons
            addressGroup.put(serviceName, remoteAddressGroups);

            if (Objects.nonNull(listener))
            {
                refreshListenerAsync(listener, remoteAddressGroups);
            }
        });
    }

    @Override
    public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent event) throws Exception
    {
        if (event == null || event.getType() == null)
        {
            // oops
            return;
        }

        switch (event.getType())
        {
            case INITIALIZED:
                updatePathInfo();
                break;

            case NODE_ADDED:
            case NODE_REMOVED:
            case NODE_UPDATED:
                if (cacheReadyLatch.getCount() == 0)
                {
                    updatePathInfo();
                }
                break;
            default:
                // ignore
                break;
        }
    }
}
