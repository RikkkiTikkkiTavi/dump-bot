package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dao.AppUserDAO;
import org.example.entity.AppUser;
import org.example.service.UserActivationService;
import org.example.utils.CryptoTool;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserActivationServiceImpl implements UserActivationService {
    private final AppUserDAO appUserDAO;
    private final CryptoTool cryptoTool;

    @Override
    public boolean activation(String cryptoUserId) {
        Long userId = cryptoTool.idOf(cryptoUserId);
        Optional<AppUser> optional = appUserDAO.findById(userId);
        if (optional.isPresent()) {
            AppUser appUser = optional.get();
            appUser.setIsActive(true);
            appUserDAO.save(appUser);
            return true;
        }
        return false;
    }
}
