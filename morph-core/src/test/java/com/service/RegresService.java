package com.service;


import com.model.regres.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.Map;


@Path("${baseUrl}")
public interface RegresService {

   @GET
   List<User> getAllUsers();


   @GET
   @Path("/getAsMap")
   Map<Integer, String> getAsMap();

   @PUT
   @Path("/user/{id}")
   @Consumes("application/json")
   @Produces("application/json")
   User updateUser(@PathParam("id") int id, User user);

   @PUT
   @Path("/user/{id}")
   @Consumes("application/json")
   @Produces("application/json")
   String updateUserAsStr(@PathParam("id") int id, User user);


}
