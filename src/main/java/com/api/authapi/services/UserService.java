package com.api.authapi.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.api.authapi.dtos.SignInDTORequest;
import com.api.authapi.dtos.SignInDTOResponse;
import com.api.authapi.dtos.SignUpDTORequest;
import com.api.authapi.dtos.SignUpDTOResponse;
import com.api.authapi.dtos.UpdateUserDTORequest;
import com.api.authapi.dtos.UpdateUserDTOResponse;
import com.api.authapi.model.UserModel;
import com.api.authapi.repository.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;
    private JwtService jwtService;

    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public SignInDTOResponse signin(SignInDTORequest signInDTORequest) throws Exception {
        Optional<UserModel> userModel = this.userRepository.findByEmail(signInDTORequest.getEmail());

        if (userModel.isEmpty()) {
            throw new Exception("Email or password invalid");
        }

        String password = userModel.get().getPassword();

        if (!password.equals(signInDTORequest.getPassword())) {
            throw new Exception("Email or password invalid");
        }

        String jwt = this.jwtService.generateJwt(userModel.get().getUserId().toString());

        return new SignInDTOResponse(jwt);
    }

    public SignUpDTOResponse signup(SignUpDTORequest signUpDTORequest) {
        UserModel userModel = new UserModel(
            signUpDTORequest.getFirstName(),
            signUpDTORequest.getLastName(),
            signUpDTORequest.getEmail(),
            signUpDTORequest.getPassword()
        );

        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        return new SignUpDTOResponse(saveUserModelResponse.getUserId().toString());
    }

    public UpdateUserDTOResponse update(UpdateUserDTORequest updateUserDTORequest, String userId) {
        UserModel findUserModelResponse = this.userRepository.findById(Long.decode(userId)).get();

        UserModel userModel = new UserModel(
            Long.decode(userId),
            updateUserDTORequest.getFirstName(),
            updateUserDTORequest.getLastName(),
            findUserModelResponse.getEmail(),
            findUserModelResponse.getPassword()
        );

        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        return new UpdateUserDTOResponse(saveUserModelResponse.getUserId().toString());
    }
}
