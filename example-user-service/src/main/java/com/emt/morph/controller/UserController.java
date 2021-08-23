package com.emt.morph.controller;

import com.emt.morph.Feedback;
import com.emt.morph.User;
import com.emt.morph.UserApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class UserController {

    @Autowired
    private UserApiService testService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<User>> getAllUsers() {
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

    @PostMapping(path = "/feedback", produces = MediaType.TEXT_PLAIN_VALUE, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String handleBrowserSubmissions(Feedback feedback) throws Exception {
        // Save feedback data
        return "hello";
    }

    @PostMapping(path = "/feedback/multipart", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String handleMultiPartRequest(@RequestPart("name") String name, @RequestPart("feedback") Feedback feedback) throws Exception {
        // Save feedback data
        return "hello";
    }

}
