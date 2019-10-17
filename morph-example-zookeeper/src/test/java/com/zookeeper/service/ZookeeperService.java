package com.zookeeper.service;

import com.emt.morph.api.MorphLoadBalancer;
import com.emt.morph.zookeeper.CustomLoadBalancer;
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
   @MorphLoadBalancer(CustomLoadBalancer.class)
   List<User> getAllUsers();

}
