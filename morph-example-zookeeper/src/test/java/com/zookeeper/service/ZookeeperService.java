package com.zookeeper.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.zookeeper.model.User;

@Path("zookeeper://test_service")
public interface ZookeeperService
{

    @GET
    @Path("/api")
    @Consumes("application/json")
    List<User> getAllUsers();

}
