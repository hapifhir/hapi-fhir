package ca.uhn.fhir.jpa.searchparam.config;

import ca.uhn.fhir.jpa.nickname.NicknameServiceFactory;
import ca.uhn.fhir.jpa.searchparam.nickname.NicknameInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class NicknameServiceConfig {

	@Bean
	public NicknameServiceFactory nicknameSvc() {
		return new NicknameServiceFactory();
	}

	@Lazy
	@Bean
	public NicknameInterceptor nicknameInterceptor(NicknameServiceFactory theNicknameSvcFactory) {
		return new NicknameInterceptor(theNicknameSvcFactory);
	}
}
