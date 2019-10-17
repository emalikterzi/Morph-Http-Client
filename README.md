# Morph-Client

Dynamic Rest Client Via Interfaces

## Introduction



## Sample

Sample Interface

``` java

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
