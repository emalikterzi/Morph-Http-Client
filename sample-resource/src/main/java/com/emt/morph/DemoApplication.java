package com.emt.morph;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {


   public static void main(String[] args) {
      SpringApplication.run(DemoApplication.class, args);
   }

   @Bean
   public ObjectMapper objectMapper() {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
      return objectMapper;
   }

}
