/*-
 * #%L
 * hapi-fhir-spring-boot-sample-server-jersey
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
package sample.fhir.server.plain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ca.uhn.fhir.spring.boot.autoconfigure.FhirAutoConfiguration;

@SpringBootApplication
@ImportAutoConfiguration(FhirAutoConfiguration.class)
public class SamplePlainRestfulServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SamplePlainRestfulServerApplication.class, args);
	}

}
