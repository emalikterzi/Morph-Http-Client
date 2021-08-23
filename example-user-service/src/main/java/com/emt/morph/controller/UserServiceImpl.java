package com.emt.morph.controller;

import com.emt.morph.Feedback;
import com.emt.morph.User;
import com.emt.morph.UserApiService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserServiceImpl implements UserApiService {

    private List<User> userList = new ArrayList<>();

    @PostConstruct
    public void init() {
        userList.add(new User(1, "enes", "terzi", "e.terzi@gmail.com"));
        userList.add(new User(2, "semih", "terzi", "s.terzi@gmail.com"));
        userList.add(new User(3, "suheyb", "terzi", "s.terzi@gmail.com"));
        userList.add(new User(4, "hatice", "terzi", "h.terci@gmail.com"));
        userList.add(new User(5, "maksut", "terzi", "m.terzi@gmail.com"));
    }

    @Override
    public List<User> getAllUsers() {
        return this.userList;
    }

    @Override
    public Map<Integer, String> getAsMap() {
        return userList.stream().collect(Collectors.toMap(User::getId, User::getFirstName));
    }

    @Override
    public User updateUser(int id, User user) {
        User ref = userList.stream().filter(x -> x.getId() == id)
                .findFirst()
                .get();

        ref.setFirstName(user.getFirstName());
        return ref;
    }

    @Override
    public String handleBrowserSubmissions(Feedback feedback) {
        return "hello";
    }

    @Override
    public String handleMultiPartRequest(String name, Feedback feedback) {
        return "hello";
    }

}
