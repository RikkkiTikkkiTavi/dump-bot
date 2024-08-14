package org.example.service;

import org.example.entity.AppUser;

public interface AppUserService {
    String registerUser(AppUser appUser);

    String setEmail(AppUser appUser, String email);
}
