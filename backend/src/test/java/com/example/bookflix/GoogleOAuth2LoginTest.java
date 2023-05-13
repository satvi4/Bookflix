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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class GoogleOAuth2LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientRegistrationRepository clientRegistrationRepository;

    @Test
    public void loginRedirectsToGoogleAuthorization() throws Exception {
        // Mock the client registration for Google
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("google")
                .clientId("465566953017-hop8k32gafr7n0pv2ffvsj7m96a2mtf3.apps.googleusercontent.com")
                .clientSecret("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")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUriTemplate("{baseUrl}/login/oauth2/code/{registrationId}")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName("email")
                .clientName("Google")
                .build();

        when(clientRegistrationRepository.findByRegistrationId("google"))
                .thenReturn(clientRegistration);

        mockMvc.perform(MockMvcRequestBuilders.get("/login/oauth2/google"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(
                        "https://accounts.google.com/o/oauth2/v2/auth?client_id=your-google-client-id&redirect_uri=http%3A%2F%2Flocalhost%2Flogin%2Foauth2%2Fcode%2Fgoogle&response_type=code&scope=openid+email+profile&state="));
    }

    @Test
    public void loginSuccessRedirectsToHome() throws Exception {
        // Simulate a callback from Google after successful login
        mockMvc.perform(MockMvcRequestBuilders.get("/login/oauth2/code/google")
                .param("code", "test-code")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }
}
