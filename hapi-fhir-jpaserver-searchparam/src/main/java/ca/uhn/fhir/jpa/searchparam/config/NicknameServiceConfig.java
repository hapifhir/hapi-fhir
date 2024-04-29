/*-
 * #%L
 * HAPI FHIR JPA - Search Parameters
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.searchparam.config;

import ca.uhn.fhir.jpa.nickname.INicknameSvc;
import ca.uhn.fhir.jpa.nickname.NicknameSvc;
import ca.uhn.fhir.jpa.searchparam.nickname.NicknameInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class NicknameServiceConfig {

	@Bean
	public INicknameSvc nicknameSvc() {
		return new NicknameSvc();
	}

	@Lazy
	@Bean
	public NicknameInterceptor nicknameInterceptor(INicknameSvc theNicknameSvc) {
		return new NicknameInterceptor(theNicknameSvc);
	}
}
