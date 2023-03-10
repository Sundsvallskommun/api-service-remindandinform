package se.sundsvall.remindandinform.integration.messaging.configuration;

import org.springframework.cloud.openfeign.FeignBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import se.sundsvall.dept44.configuration.feign.FeignConfiguration;
import se.sundsvall.dept44.configuration.feign.FeignMultiCustomizer;
import se.sundsvall.dept44.configuration.feign.decoder.ProblemErrorDecoder;

@Configuration
@Import(FeignConfiguration.class)
public class MessagingConfiguration {

    public static final String CLIENT_REGISTRATION_ID = "messaging";

    @Bean
    FeignBuilderCustomizer feignBuilderCustomizer(ClientRegistrationRepository clientRepository, MessagingProperties messagingProperties) {
        return FeignMultiCustomizer.create()
                .withErrorDecoder(new ProblemErrorDecoder(CLIENT_REGISTRATION_ID))
                .withRequestTimeoutsInSeconds(messagingProperties.connectTimeout(), messagingProperties.readTimeout())
                .withRetryableOAuth2InterceptorForClientRegistration(clientRepository.findByRegistrationId(CLIENT_REGISTRATION_ID))
                .composeCustomizersToOne();
    }
}
