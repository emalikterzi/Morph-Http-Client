package com.test;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.emt.morph.MorphClient;
import com.emt.morph.MorphClientServiceBuilder;
import com.emt.morph.converter.DefaultJsonMessageConverter;
import com.emt.morph.impl.DefaultHttpClientProvider;
import com.emt.morph.impl.DefaultSystemEnvironmentPropertyResolver;
import com.model.regres.User;
import com.service.RegresService;

public class TestServiceLayer {

   private RegresService regresService;

   @BeforeEach
   void before() {
      System.setProperty("baseUrl", "http://localhost:8282/api");

      MorphClient morphClient = MorphClientServiceBuilder.newBuilder()
              .setHttpClientProvider(new DefaultHttpClientProvider())
              .setPathPropertyResolver(new DefaultSystemEnvironmentPropertyResolver())
              .setMessageConverters(Collections.singletonList(new DefaultJsonMessageConverter()))
              .logInvocationTime()
              .debug().build();
      this.regresService = morphClient.morph(RegresService.class);
   }

   @Test
   public void test()
   {
      List<User> userList = this.regresService.getAllUsers();

      Assertions.assertTrue(Objects.nonNull(userList));
      Assertions.assertTrue(!userList.isEmpty());

      User user = userList.get(0);
      user.setFirstName("editted");

      User newUser = regresService.updateUser(user.getId(), user);
      regresService.updateUser(user.getId(), user);
      regresService.updateUser(user.getId(), user);
      regresService.updateUser(user.getId(), user);
      regresService.updateUser(user.getId(), user);

      Assertions.assertTrue(newUser.getFirstName().equals(user.getFirstName()));

   }

}
