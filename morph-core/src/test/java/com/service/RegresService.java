package com.service;


import com.model.regres.FormParamPayload;
import com.model.regres.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.Map;


@Path("${baseUrl}")
public interface RegresService {

   @GET
   List<User> getAllUsers(@HeaderParam("test-header") @DefaultValue("default-header-value") String testHeader);

   @POST
   @Consumes("application/x-www-form-urlencoded")
   List<User> getAllUsers(@FormParam("param1") String formParam, @FormParam("param2") String formParam2,
                          FormParamPayload formParamPayload);

   @GET
   @Path("/getAsMap")
   Map<Integer, String> getAsMap();

   @PUT
   @Path("/user/{id}")
   @Consumes("application/json")
   User updateUser(@PathParam("id") int id, User user);

   @PUT
   @Path("/user/{id}")
   @Consumes("application/json")
   String updateUserAsStr(@PathParam("id") int id, User user);


}
