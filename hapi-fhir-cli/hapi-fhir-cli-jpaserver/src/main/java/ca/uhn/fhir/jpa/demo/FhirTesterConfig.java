package ca.uhn.fhir.jpa.demo;

/*-
 * #%L
 * HAPI FHIR - Command Line Client - Server WAR
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.to.FhirTesterMvcConfig;
import ca.uhn.fhir.to.TesterConfig;

//@formatter:off
/**
 * This spring config file configures the web testing module. It serves two
 * purposes:
 * 1. It imports FhirTesterMvcConfig, which is the spring config for the
 *    tester itself
 * 2. It tells the tester which server(s) to talk to, via the testerConfig()
 *    method below
 */
@Configuration
@Import(FhirTesterMvcConfig.class)
public class FhirTesterConfig {

	/**
	 * This bean tells the testing webpage which servers it should configure itself
	 * to communicate with. In this example we configure it to talk to the local
	 * server, as well as one public server. If you are creating a project to 
	 * deploy somewhere else, you might choose to only put your own server's 
	 * address here.
	 * 
	 * Note the use of the ${serverBase} variable below. This will be replaced with
	 * the base URL as reported by the server itself. Often for a simple Tomcat
	 * (or other container) installation, this will end up being something
	 * like "http://localhost:8080/hapi-fhir-jpaserver-example". If you are
	 * deploying your server to a place with a fully qualified domain name, 
	 * you might want to use that instead of using the variable.
	 */
	@Bean
	public TesterConfig testerConfig() {
		TesterConfig retVal = new TesterConfig();
		retVal
			.addServer()
				.withId("home")
				.withFhirVersion(ContextHolder.getCtx().getVersion().getVersion())
				.withBaseUrl("${serverBase}" + ContextHolder.getPath())
				.withName("Local Tester");
		
		retVal
			.addServer()
				.withId("hapi")
				.withFhirVersion(FhirVersionEnum.DSTU2)
				.withBaseUrl("http://fhirtest.uhn.ca/baseDstu2")
				.withName("Public HAPI Test Server");
		return retVal;
	}
	
}
//@formatter:on
