package com.emt.morph;

import com.emt.morph.annotation.processor.JerseyAnnotationProcessor;
import com.emt.morph.config.InvocationContextConfig;
import com.emt.morph.http.message.FileMessageConverter;

import java.io.File;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {

        MorphClient morphClient = new MorphClientServiceImpl(new JerseyAnnotationProcessor(), new InvocationContextConfig(true));
        UserApiService testService = morphClient.morph(UserApiService.class);

        System.setProperty("basePath", "http://localhost:8282");

        testService.processFile(new File("/Users/enes.terzi/Downloads/pozy-logo.png"));
//        List<User> userList = testService.getAllUsers();
//        Map<Integer, String> map = testService.getAsMap();
//
//        User user = userList.get(0);
//        user.setFirstName(UUID.randomUUID().toString());
//        User updatedUser = testService.updateUser(user.getId(), user);
//
//        String serverMessage = testService.handleBrowserSubmissions(new Feedback("h2", "h3"));
//        System.out.println("");
//
//        String serverMessage2 = testService.handleMultiPartRequest("hello", new Feedback("h2", "h3"));
//        System.out.println("");
    }

}
