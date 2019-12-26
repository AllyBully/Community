package com.kmsoft.community.service;

import com.kmsoft.community.model.User;

public interface UserService {

    int addUser(User user);

    User FindByToken(String token);
}
