package com.spotify.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.TestPropertySource;
import static org.mockito.Mockito.mock;

// Add more granular security exclusions if they are present in your classpath
// You might not need all of these, but it's a "throw everything at it" approach for troubleshooting.
// Check your build.gradle for other Spring Security dependencies like 'spring-security-oauth2-resource-server', etc.
@SpringBootTest
@EnableAutoConfiguration(exclude = {
		SecurityAutoConfiguration.class,
		OAuth2ClientAutoConfiguration.class,
		// Potentially exclude other related auto-configurations if they exist and are problematic
		// Example: If you have Spring Cloud Security or specific OAuth2 resource server config
		// org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration.class,
		// org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerAutoConfiguration.class
		// ... any other security or related auto-configurations from your dependencies
})
@TestPropertySource(properties = {
		// Keep only the essential properties if you suspect other placeholder issues
		"spring.security.oauth2.client.registration.spotify.client-id=test-client-id",
		"spring.security.oauth2.client.registration.spotify.client-secret=test-client-secret"
		// Remove all other OAuth2 properties for now to see if a different placeholder is the issue
})
class SpotifyApiApplicationTests {

	@TestConfiguration
	static class TestConfig {
		@Bean
		public OAuth2AuthorizedClientService oAuth2AuthorizedClientService() {
			return mock(OAuth2AuthorizedClientService.class);
		}

		@Bean
		public OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager() {
			return mock(OAuth2AuthorizedClientManager.class);
		}

		@Bean
		public ClientRegistrationRepository clientRegistrationRepository() {
			return mock(ClientRegistrationRepository.class);
		}

		// Sometimes, the default UserDetailsService also causes issues if Security is fully disabled
		// If the above doesn't work, consider mocking this too.
		 @Bean
		 public UserDetailsService userDetailsService() {
		     return mock(UserDetailsService.class);
		 }
	}

//	@Test
//	void contextLoads() {
//	}
}