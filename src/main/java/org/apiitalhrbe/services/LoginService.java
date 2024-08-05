package org.apiitalhrbe.services;

import org.apiitalhrbe.dtos.login.Credential;
import org.apiitalhrbe.dtos.login.LoginResponseDTO;
import org.apiitalhrbe.entities.sql.UserEntity;
import org.apiitalhrbe.repositories.sql.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    public LoginResponseDTO getByCredential(Credential credential) {
        Optional<UserEntity> userEntity = userRepository.findUserEntitiesLogin(credential.getIdentity(), credential.getIdentity(), credential.getPassword());
        return userEntity.map(entity -> new LoginResponseDTO(entity.getUsername(), entity.getRole())).orElse(null);
    }
}
