package com.zookeeper.service;

import com.zookeeper.model.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Path("zookeeper://test_service")
public interface ZookeeperService {

   @GET
   @Path("/api")
   @Consumes("application/json")
   List<User> getAllUsers();

}
