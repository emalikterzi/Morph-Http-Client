package com.zookeeper;

import java.util.Collections;

import org.apache.curator.framework.CuratorFramework;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.emt.morph.MorphClientServiceBuilder;
import com.emt.morph.NameResolver;
import com.emt.morph.zookeeper.CuratorFrameworkClients;
import com.emt.morph.zookeeper.resolver.DefaultZookeeperResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zookeeper.service.ZookeeperService;

public class ZookeeperTest
{

    private CuratorFramework curatorFramework;

    @BeforeEach
    public void init()
    {
        this.curatorFramework = CuratorFrameworkClients.createSimple("localhost:2181");
        this.curatorFramework.start();
    }

    @AfterEach
    public void destroy()
    {
        this.curatorFramework.close();
    }


    @Test
    public void nodeTest()
    {
        String mainPath = "/zookeeper/quota";
        NameResolver nameResolver = new DefaultZookeeperResolver(curatorFramework, mainPath, new ObjectMapper());

        ZookeeperService zookeeperService =
                MorphClientServiceBuilder
                        .newBuilder()
                        .debug()
                        .logInvocationTime()
                        .setNameResolvers(Collections.singletonList(nameResolver))
                        .build().morph(ZookeeperService.class);

        zookeeperService.getAllUsers();

    }
}
