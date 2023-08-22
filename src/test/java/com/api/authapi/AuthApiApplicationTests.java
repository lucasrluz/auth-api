package com.api.authapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;

import com.api.authapi.dtos.SignInDTORequest;
import com.api.authapi.dtos.SignUpDTORequest;
import com.api.authapi.dtos.UpdateUserDTORequest;
import com.api.authapi.model.UserModel;
import com.api.authapi.repository.UserRepository;
import com.api.authapi.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class AuthApiApplicationTests {
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

	// POST /signup

	@Test
	public void esperoQueCadastreUmNovoUsuarioNoSistema() throws Exception {
		this.userRepository.deleteAll();

		SignUpDTORequest signUpDTORequest = new SignUpDTORequest(
			"foo",
			"bar",
			"foobar@gmail.com",
			"foobar"	
		);

		MockHttpServletResponse mockHttpServletResponse = this.mockMvc.perform(
			post("/api/v1/auth/signup")
			.contentType("application/json")
			.content(asJsonString(signUpDTORequest))
		).andReturn().getResponse();

		Assertions.assertThat(mockHttpServletResponse.getStatus()).isEqualTo(201);
		
		String userId = new JSONObject(mockHttpServletResponse.getContentAsString()).getString("userId");
		
		Optional<UserModel> userModel = this.userRepository.findById(Long.decode(userId));

		Assertions.assertThat(userModel.isEmpty()).isEqualTo(false);
		Assertions.assertThat(userModel.get().getFirstName()).isEqualTo("foo");
		Assertions.assertThat(userModel.get().getLastName()).isEqualTo("bar");
		Assertions.assertThat(userModel.get().getEmail()).isEqualTo("foobar@gmail.com");
		Assertions.assertThat(userModel.get().getPassword()).isEqualTo("foobar");

		this.userRepository.deleteAll();
	}

	// POST /signin

	@Test
	public void esperoQueFacaOSingIn() throws Exception {
		this.userRepository.deleteAll();

		UserModel userModel = new UserModel(
			"foo",
			"bar",
			"foobar@gmail.com",
			"foobar"
		);

		UserModel saveUserModelResponse = this.userRepository.save(userModel);

		SignInDTORequest signInDTORequest = new SignInDTORequest(
			"foobar@gmail.com",
			"foobar"
		);

		MockHttpServletResponse mockHttpServletResponse = this.mockMvc.perform(
			post("/api/v1/auth/signin")
			.contentType("application/json")
			.content(asJsonString(signInDTORequest))
		).andReturn().getResponse();

		Assertions.assertThat(mockHttpServletResponse.getStatus()).isEqualTo(201);
		
		String jwt = new JSONObject(mockHttpServletResponse.getContentAsString()).getString("jwt");

		String userId = this.jwtService.validateJwt(jwt);
		
		Assertions.assertThat(userId).isEqualTo(saveUserModelResponse.getUserId().toString());

		this.userRepository.deleteAll();
	}

	@Test
	public void esperoQueRetorneUmErroDeEmailOuSenhaInvalidDadoUmEmailInvalido() throws Exception {
		this.userRepository.deleteAll();

		SignInDTORequest signInDTORequest = new SignInDTORequest(
			"foobar@gmail.com",
			"foobar"
		);

		MockHttpServletResponse mockHttpServletResponse = this.mockMvc.perform(
			post("/api/v1/auth/signin")
			.contentType("application/json")
			.content(asJsonString(signInDTORequest))
		).andReturn().getResponse();

		Assertions.assertThat(mockHttpServletResponse.getStatus()).isEqualTo(400);
		Assertions.assertThat(mockHttpServletResponse.getContentAsString()).isEqualTo("Email or password invalid");

		this.userRepository.deleteAll();
	}

	@Test
	public void esperoQueRetorneUmErroDeEmailOuSenhaInvalidDadoUmaSenhaInvalida() throws Exception {
		this.userRepository.deleteAll();

		UserModel userModel = new UserModel(
			"foo",
			"bar",
			"foobar@gmail.com",
			"foobar"
		);

		this.userRepository.save(userModel);

		SignInDTORequest signInDTORequest = new SignInDTORequest(
			"foobar@gmail.com",
			"123"
		);

		MockHttpServletResponse mockHttpServletResponse = this.mockMvc.perform(
			post("/api/v1/auth/signin")
			.contentType("application/json")
			.content(asJsonString(signInDTORequest))
		).andReturn().getResponse();

		Assertions.assertThat(mockHttpServletResponse.getStatus()).isEqualTo(400);
		Assertions.assertThat(mockHttpServletResponse.getContentAsString()).isEqualTo("Email or password invalid");

		this.userRepository.deleteAll();
	}

	// SecurityFilter

	@Test
    public void esperoQueRetorneUmErroDeAcessoNegadoDadoQueNaoFoiEnviadoOJWT() throws Exception {
        this.userRepository.deleteAll();

		UserModel userModel = new UserModel(
			"foo",
			"bar",
			"foobar@gmail.com",
			"foobar"
		);

		this.userRepository.save(userModel);

        UpdateUserDTORequest updateUserDTORequest = new UpdateUserDTORequest(
            "FOO",
            "BAR"
        );

        MockHttpServletResponse mockHttpServletResponse = this.mockMvc.perform(
			put("/api/v1/user")
			.contentType("application/json")
			.content(asJsonString(updateUserDTORequest))
		).andReturn().getResponse();

        Assertions.assertThat(mockHttpServletResponse.getStatus()).isEqualTo(403);
		Assertions.assertThat(mockHttpServletResponse.getErrorMessage()).isEqualTo("Access Denied");

		this.userRepository.deleteAll();
    }

	@Test
    public void esperoQueRetorneUmErroDeAcessoNegadoDadoQueUsuarioNaoExiste() throws Exception {
        this.userRepository.deleteAll();

		UserModel userModel = new UserModel(
			"foo",
			"bar",
			"foobar@gmail.com",
			"foobar"
		);

		UserModel saveUserModelResponse = this.userRepository.save(userModel);

        String jwt = this.jwtService.generateJwt(saveUserModelResponse.getUserId().toString());

		this.userRepository.deleteAll();

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

		Assertions.assertThat(mockHttpServletResponse.getStatus()).isEqualTo(403);
		Assertions.assertThat(mockHttpServletResponse.getErrorMessage()).isEqualTo("Access Denied");

		this.userRepository.deleteAll();
    }
}
