package com.emt.morph.zookeeper.resolver;

import com.emt.morph.NameResolver;
import com.emt.morph.factory.NameResolverFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.curator.framework.CuratorFramework;

public class DefaultNameResolverFactory implements NameResolverFactory {

   private final CuratorFramework client;
   private final String serviceZookeeperPath;
   private final ObjectMapper objectMapper;

   public DefaultNameResolverFactory(CuratorFramework client, String serviceZookeeperPath, ObjectMapper objectMapper) {
      this.client = client;
      this.serviceZookeeperPath = serviceZookeeperPath;
      this.objectMapper = objectMapper;
   }

   @Override
   public NameResolver createNameResolver(Class<? extends NameResolver> nameResolverClass) {
      return new DefaultZookeeperResolver(this.client, this.serviceZookeeperPath, this.objectMapper);
   }
}
