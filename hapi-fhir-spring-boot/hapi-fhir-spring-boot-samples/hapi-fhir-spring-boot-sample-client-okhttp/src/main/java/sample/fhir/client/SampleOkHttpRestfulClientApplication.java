package sample.fhir.client;

/*-
 * #%L
 * hapi-fhir-spring-boot-sample-client-okhttp
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

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import org.hl7.fhir.dstu3.model.CapabilityStatement;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SampleOkHttpRestfulClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleOkHttpRestfulClientApplication.class, args);
    }

    @Bean
    public LoggingInterceptor loggingInterceptor() {
        return new LoggingInterceptor(true);
    }

    @Bean
    public CommandLineRunner runner(final IGenericClient fhirClient) {
        return new CommandLineRunner() {

            @Override
            public void run(String... args) throws Exception {
                fhirClient.capabilities()
                        .ofType(CapabilityStatement.class)
                        .execute();
            }
        };
    }

}
