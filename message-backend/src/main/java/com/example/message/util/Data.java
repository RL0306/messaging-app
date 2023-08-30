package com.example.message.util;

import com.example.message.model.Group;
import com.example.message.model.User;

import java.util.Arrays;
import java.util.List;


public class Data {
    public static final List<User> users = Arrays.asList(
            new User(1L, "test1"),
            new User(2L, "test2"),
            new User(3L, "test3"),
            new User(4L, "test4"),
            new User(5L, "test5"),
            new User(6L, "test6"),
            new User(7L, "test7"),
            new User(8L, "test8")
    );

    public static List<Group> groups = Arrays.asList(
            new Group(9L, Arrays.asList(users.get(0), users.get(1), users.get(2))),
            new Group(10L, Arrays.asList(users.get(0), users.get(3), users.get(4)))
    );

}
