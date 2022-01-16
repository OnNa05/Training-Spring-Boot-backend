package com.example.backend.business;

import com.example.backend.entity.User;
import com.example.backend.exception.BaseException;
import com.example.backend.exception.FileException;
import com.example.backend.exception.UserException;
import com.example.backend.mapper.UserMapper;
import com.example.backend.model.MLoginRequest;
import com.example.backend.model.MRegisterRequest;
import com.example.backend.model.MRegisterResponse;
import com.example.backend.service.TokenService;
import com.example.backend.service.UserService;
import com.example.backend.util.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserBusiness {

    private final UserService userService;

    private final UserMapper userMapper;

    private final TokenService tokenService;

    public UserBusiness(UserService userService, UserMapper userMapper, TokenService tokenService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.tokenService = tokenService;
    }

    public String login(MLoginRequest request) throws BaseException {
        // validate request

        // validate database
        Optional<User> obt = userService.findByEmail(request.getEmail());
        if (obt.isEmpty()) {
            throw UserException.loginFailEmailNotFound();
        }

        User user = obt.get();
        if (!userService.matchPass(request.getPass(), user.getPass())) {
            throw UserException.loginFailPassIncorrect();
        }

        return tokenService.tokenize(user);

    }

    public String refreshToken() throws BaseException {
        Optional<String> optional = SecurityUtil.getCurrentUSerId();
        if (optional.isEmpty()) {
            throw UserException.unAuthentication();
        }

        String userId = optional.get();

        Optional<User> opt = userService.findById(userId);
        if (opt.isEmpty()) {
            throw UserException.notFound();
        }

        User user = opt.get();
        return tokenService.tokenize(user);
    }


    public MRegisterResponse register(MRegisterRequest request) throws BaseException {
        User user = userService.create(request.getEmail(), request.getPass(), request.getName());

        return userMapper.toRegisterResponse(user);

    }

    public String uploadProfilePicture(MultipartFile file) throws BaseException {
        // validate file
        if (file == null) {
            // throw err
            throw FileException.fileNull();
        }

        // validate size
        if (file.getSize() > 1048576 * 2) {
            // throw err
            throw FileException.fileMaxSize();
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            // throw err
            throw FileException.fileUnSupported();
        }

        List<String> supportTypes = Arrays.asList("image/jpeg", "image/png");
        if (!supportTypes.contains(contentType)) {
            // throw err (unsupported)
            throw FileException.fileUnSupported();
        }

        // TODO: upload file File Storage (AWS S3, etc...)
        try {
            byte[] bytes = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
