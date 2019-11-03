package com.emtdev.morph.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorFrameworkClients {

   public static CuratorFramework createSimple(String connectionString) {
      ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
      return CuratorFrameworkFactory.newClient(connectionString, retryPolicy);
   }

}
