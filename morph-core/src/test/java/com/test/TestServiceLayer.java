package com.test;

import com.emt.morph.MorphClient;
import com.emt.morph.MorphClientServiceBuilder;
import com.emt.morph.converter.DefaultJsonMessageConverter;
import com.emt.morph.impl.DefaultSystemEnvironmentPropertyResolver;
import com.model.regres.User;
import com.service.RegresService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

public class TestServiceLayer {

   private RegresService regresService;

   @BeforeEach
   @SuppressWarnings("unchecked")
   void before() {
      System.setProperty("baseUrl", "http://localhost:8282/api");

      MorphClient morphClient =
              MorphClientServiceBuilder.newBuilder()
                      .addMessageConverter(new DefaultJsonMessageConverter())
                      .setPathPropertyResolver(new DefaultSystemEnvironmentPropertyResolver())
                      .build();


      this.regresService = morphClient.morph(RegresService.class);
   }

   @Test
   public void test() {
      List<User> userList = this.regresService.getAllUsers();

      Assertions.assertTrue(Objects.nonNull(userList));
      Assertions.assertFalse(userList.isEmpty());

      User user = userList.get(0);
      user.setFirstName("newUserName");

      User newUser = regresService.updateUser(user.getId(), user);
      regresService.updateUser(user.getId(), user);
      regresService.updateUser(user.getId(), user);
      regresService.updateUser(user.getId(), user);
      regresService.updateUser(user.getId(), user);

      Assertions.assertEquals(newUser.getFirstName(), user.getFirstName());

   }

}
