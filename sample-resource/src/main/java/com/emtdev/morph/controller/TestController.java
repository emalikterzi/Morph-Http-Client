package com.emtdev.morph.controller;

import com.emtdev.morph.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class TestController {

   @Autowired
   private TestService testService;

   @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
   public ResponseEntity<List<User>> getAllUsers(HttpServletRequest httpServletRequest) {
      return ResponseEntity.ok(testService.getAllUsers());
   }


   @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
           consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
   public ResponseEntity<List<User>> getAllUserPost(MultiValueMap httpServletRequest) {

      return ResponseEntity.ok(testService.getAllUsers());
   }


   @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, value = "/getAsMap")
   public ResponseEntity<Map<Integer, String>> getAsMap() {
      return ResponseEntity.ok(testService.getAsMap());
   }


   @PutMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, value = "/user/{id}")
   public ResponseEntity<User> updateUser(@PathVariable("id") int id, @RequestBody User user) {
      return ResponseEntity.ok(testService.updateUser(id, user));
   }

}
