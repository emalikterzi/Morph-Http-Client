package com.emt.morph;

import com.emt.morph.annotation.api.MultiPart;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.File;
import java.util.List;
import java.util.Map;

@Path("${basePath}/api")
public interface UserApiService {

    @GET
    @Consumes("application/json")
    List<User> getAllUsers();

    @GET
    @Path("/getAsMap")
    @Consumes("application/json")
    Map<Integer, String> getAsMap();

    @PUT
    @Path("/user/{id}")
    @Consumes("application/json")
    User updateUser(@PathParam("id") int id, User user);

    @POST
    @Path("/feedback")
    @Consumes("application/x-www-form-urlencoded")
    String handleBrowserSubmissions(Feedback feedback);

    @POST
    @Path("/feedback/multipart")
    @Consumes("multipart/form-data")
    @Produces("text/plain")
    String handleMultiPartRequest(@MultiPart(name = "name", mimeType = "text/plain") String name,
                                  @MultiPart(name = "feedback", mimeType = "application/json") Feedback feedback);


    @POST
    @Path("/feedback/multipart")
    @Produces("text/plain")
    String processFile(File file);

}
