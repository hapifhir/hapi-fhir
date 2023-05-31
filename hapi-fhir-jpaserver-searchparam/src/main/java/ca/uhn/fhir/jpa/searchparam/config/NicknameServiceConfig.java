package ca.uhn.fhir.jpa.searchparam.config;

import ca.uhn.fhir.jpa.searchparam.nickname.NicknameInterceptor;
import ca.uhn.fhir.jpa.nickname.NicknameServiceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class NicknameServiceConfig {

	@Lazy
	@Bean
	public NicknameInterceptor nicknameInterceptor(NicknameServiceFactory theNicknameServiceFactory) {
		return new NicknameInterceptor(theNicknameServiceFactory);
	}

	@Bean
	public NicknameServiceFactory nicknameSvcFactory() {
		return new NicknameServiceFactory();
	}
}
