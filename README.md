# Morph-Client

Dynamic Rest Client Via Interfaces

## Introduction

## Sample

Sample Interface

``` java

@Path("${baseUrl}")
public interface RegresService {

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

}
```

Sample Client

``` Java

  System.setProperty("baseUrl", "http://localhost:8282/api");
 
  MorphClient morphClient =
               MorphClientServiceBuilder.newBuilder()
                       .addMessageConverter(new DefaultJsonMessageConverter())
                       .setPathPropertyResolver(new DefaultSystemEnvironmentPropertyResolver())
                       .build();

  this.regresService = morphClient.morph(RegresService.class);

```
