package com.zookeeper;

import com.emt.morph.MorphClientServiceBuilder;
import com.emt.morph.factory.NameResolverFactory;
import com.emt.morph.zookeeper.CuratorFrameworkClients;
import com.emt.morph.zookeeper.resolver.DefaultNameResolverFactory;
import com.emt.morph.zookeeper.resolver.DefaultZookeeperResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zookeeper.service.ZookeeperService;
import org.apache.curator.framework.CuratorFramework;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ZookeeperTest {

   private CuratorFramework curatorFramework;

   @BeforeEach
   public void init() {
      this.curatorFramework = CuratorFrameworkClients.createSimple("localhost:2181");
      this.curatorFramework.start();
   }

   @AfterEach
   public void destroy() {
      this.curatorFramework.close();
   }


   @Test
   public void nodeTest() {
      System.out.println(1e7);
      System.out.println(1e2);
      System.out.println(1e3);
      String mainPath = "/zookeeper/quota";
      NameResolverFactory nameResolverFactory
              = new DefaultNameResolverFactory(this.curatorFramework, mainPath, new ObjectMapper());


      ZookeeperService zookeeperService =
              MorphClientServiceBuilder
                      .newBuilder()
                      .setNameResolverFactory(nameResolverFactory)
                      .addNameResolver(DefaultZookeeperResolver.class)
                      .build().morph(ZookeeperService.class);

      System.out.println(zookeeperService.getAllUsers());
      System.out.println(zookeeperService.getAllUsers());
      System.out.println(zookeeperService.getAllUsers());
      System.out.println(zookeeperService.getAllUsers());
   }
}
