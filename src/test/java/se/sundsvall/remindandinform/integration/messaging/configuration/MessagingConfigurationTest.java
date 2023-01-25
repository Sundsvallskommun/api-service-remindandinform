package se.sundsvall.remindandinform.integration.messaging.configuration;

import feign.codec.ErrorDecoder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.ActiveProfiles;
import se.sundsvall.dept44.configuration.feign.FeignMultiCustomizer;
import se.sundsvall.dept44.configuration.feign.decoder.ProblemErrorDecoder;
import se.sundsvall.remindandinform.Application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.sundsvall.remindandinform.integration.messaging.configuration.MessagingConfiguration.CLIENT_REGISTRATION_ID;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("junit")
class MessagingConfigurationTest {

	@Mock
	private ClientRegistrationRepository clientRepositoryMock;

	@Mock
	private ClientRegistration clientRegistrationMock;

	@Mock
	private MessagingProperties propertiesMock;

	@Spy
	private FeignMultiCustomizer feignMultiCustomizerSpy;

	@Captor
	private ArgumentCaptor<ErrorDecoder> errorDecoderCaptor;

	@Autowired
	private MessagingProperties properties;

	@InjectMocks
	private MessagingConfiguration configuration;

	@Test
	void testFeignBuilderHelper() {

		final var connectTimeout = 123;
		final var readTimeout = 321;

		when(propertiesMock.connectTimeout()).thenReturn(connectTimeout);
		when(propertiesMock.readTimeout()).thenReturn(readTimeout);
		when(clientRepositoryMock.findByRegistrationId(CLIENT_REGISTRATION_ID)).thenReturn(clientRegistrationMock);

		// Mock static FeignMultiCustomizer to enable spy and to verify that static method is being called
		try (MockedStatic<FeignMultiCustomizer> feignMultiCustomizerMock = Mockito.mockStatic(FeignMultiCustomizer.class)) {
			feignMultiCustomizerMock.when(FeignMultiCustomizer::create).thenReturn(feignMultiCustomizerSpy);

			configuration.feignBuilderCustomizer(clientRepositoryMock, propertiesMock);

			feignMultiCustomizerMock.verify(FeignMultiCustomizer::create);
		}

		// Verifications
		verify(propertiesMock).connectTimeout();
		verify(propertiesMock).readTimeout();
		verify(clientRepositoryMock).findByRegistrationId(CLIENT_REGISTRATION_ID);
		verify(feignMultiCustomizerSpy).withErrorDecoder(errorDecoderCaptor.capture());
		verify(feignMultiCustomizerSpy).withRequestTimeoutsInSeconds(connectTimeout, readTimeout);
		verify(feignMultiCustomizerSpy).withRetryableOAuth2InterceptorForClientRegistration(clientRegistrationMock);
		verify(feignMultiCustomizerSpy).composeCustomizersToOne();

		// Assert ErrorDecoder
		Assertions.assertThat(errorDecoderCaptor.getValue())
				.isInstanceOf(ProblemErrorDecoder.class)
				.hasFieldOrPropertyWithValue("integrationName", CLIENT_REGISTRATION_ID);
	}

	@Test
	void testProperties() {
		assertThat(properties.connectTimeout()).isEqualTo(10);
		assertThat(properties.readTimeout()).isEqualTo(20);
	}
}
