package com.example.bookflix;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class GitHubOAuth2LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientRegistrationRepository clientRegistrationRepository;

    @Test
    public void loginRedirectsToGitHubAuthorization() throws Exception {
        // Mock the client registration for GitHub
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("github")
                .clientId("Iv1.1b5dd7e22c0cf8cd")
                .clientSecret("8362896850feab096200d22d21980cd5d5472e84")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUriTemplate("{baseUrl}/login/oauth2/code/{registrationId}")
                .authorizationUri("https://github.com/login/oauth/authorize")
                .tokenUri("https://github.com/login/oauth/access_token")
                .userInfoUri("https://api.github.com/user")
                .userNameAttributeName("login")
                .clientName("GitHub")
                .build();

        when(clientRegistrationRepository.findByRegistrationId("github"))
                .thenReturn(clientRegistration);

        mockMvc.perform(MockMvcRequestBuilders.get("/login/oauth2/github"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(
                        "https://github.com/login/oauth/authorize?client_id=your-github-client-id&redirect_uri=http%3A%2F%2Flocalhost%2Flogin%2Foauth2%2Fcode%2Fgithub&response_type=code&scope=user&state="));
    }

    @Test
    public void loginSuccessRedirectsToHome() throws Exception {
        // Simulate a callback from GitHub after successful login
        mockMvc.perform(MockMvcRequestBuilders.get("/login/oauth2/code/github")
                .param("code", "test-code")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }
}
