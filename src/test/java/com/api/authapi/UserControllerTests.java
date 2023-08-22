package com.api.authapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.json.JSONObject;

import com.api.authapi.dtos.UpdateUserDTORequest;
import com.api.authapi.model.UserModel;
import com.api.authapi.repository.UserRepository;
import com.api.authapi.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    public static String asJsonString(final Object obj) {
        try {
          return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
    }

    @Test
    public void esperoQueAltereOFistNameEOLastNameDoUsuario() throws Exception {
        this.userRepository.deleteAll();

		UserModel userModel = new UserModel(
			"foo",
			"bar",
			"foobar@gmail.com",
			"foobar"
		);

		UserModel saveUserModelResponse = this.userRepository.save(userModel);

        String jwt = this.jwtService.generateJwt(saveUserModelResponse.getUserId().toString());

        UpdateUserDTORequest updateUserDTORequest = new UpdateUserDTORequest(
            "FOO",
            "BAR"
        );

        MockHttpServletResponse mockHttpServletResponse = this.mockMvc.perform(
			put("/api/v1/user")
            .header("Authorization", "Bearer " + jwt)
			.contentType("application/json")
			.content(asJsonString(updateUserDTORequest))
		).andReturn().getResponse();

        Assertions.assertThat(mockHttpServletResponse.getStatus()).isEqualTo(200);
		
		String userId = new JSONObject(mockHttpServletResponse.getContentAsString()).getString("userId");
		
		Optional<UserModel> finUserModelResponse = this.userRepository.findById(Long.decode(userId));

		Assertions.assertThat(finUserModelResponse.isEmpty()).isEqualTo(false);
		Assertions.assertThat(finUserModelResponse.get().getFirstName()).isEqualTo("FOO");
		Assertions.assertThat(finUserModelResponse.get().getLastName()).isEqualTo("BAR");
		Assertions.assertThat(finUserModelResponse.get().getEmail()).isEqualTo("foobar@gmail.com");
		Assertions.assertThat(finUserModelResponse.get().getPassword()).isEqualTo("foobar");

		this.userRepository.deleteAll();
    }
}
