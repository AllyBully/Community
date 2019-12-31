package com.kmsoft.community.service;

import com.kmsoft.community.model.User;

import java.util.List;

public interface UserService {

    int addUser(User user);

    List<User> FindByToken(String token);

    List<User> FindByAccountId(String accountId);

    int updateUser(User user);
}
