package com.zookeeper.service;

import com.emtdev.morph.api.MorphLoadBalancer;
import com.emtdev.morph.bandit.loadbalancer.BanditLoadBalancer;
import com.zookeeper.model.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Path("zookeeper://test_service")
@MorphLoadBalancer(BanditLoadBalancer.class)
public interface ZookeeperService {

   @GET
   @Path("/api")
   @Consumes("application/json")
   List<User> getAllUsers();

}
