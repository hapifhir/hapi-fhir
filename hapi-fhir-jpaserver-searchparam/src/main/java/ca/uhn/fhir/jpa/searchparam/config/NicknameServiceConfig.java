package ca.uhn.fhir.jpa.searchparam.config;

import ca.uhn.fhir.jpa.nickname.NicknameSvc;
import ca.uhn.fhir.jpa.searchparam.nickname.NicknameInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class NicknameServiceConfig {

	@Lazy
	@Bean
	public NicknameInterceptor nicknameInterceptor(NicknameSvc theNicknameSvc) {
		return new NicknameInterceptor(theNicknameSvc);
	}

	@Bean
	public NicknameSvc nicknameSvc() {
		return new NicknameSvc();
	}
}
