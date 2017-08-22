package sample.fhir.client;

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
