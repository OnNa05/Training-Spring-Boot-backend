package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.exception.BaseException;
import com.example.backend.exception.UserException;
import com.example.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Optional<User> findById(String id) {
        return repository.findById(id);
    }

    public User Update(User user) {
        return repository.save(user);
    }

    public User updateName(String id, String name) throws BaseException {
        Optional<User> opt = repository.findById(id);
        if (opt.isEmpty()) {
            throw UserException.notFound();
        }

        User user = opt.get();
        user.setName(name);

        return repository.save(user);
    }

    public void delete(String id){
        repository.deleteById(id);
    }

    public boolean matchPass(String rawPass, String encodedPass) {
        return passwordEncoder.matches(rawPass, encodedPass);
    }

    public User create(String email, String pass, String name) throws UserException {
        //validate
        if (Objects.isNull(email)) {
            throw UserException.createEmailNull();
        }

        if (Objects.isNull(pass)) {
            throw UserException.createPassNull();
        }

        if (Objects.isNull(name)) {
            throw UserException.createNameNull();
        }

        // verify
        if (repository.existsByEmail(email)) {
            throw UserException.createEmailDuplicated();
        }

        User entity = new User();
        entity.setEmail(email);
        entity.setPass(passwordEncoder.encode(pass));
        entity.setName(name);

        return repository.save(entity);
    }


}
