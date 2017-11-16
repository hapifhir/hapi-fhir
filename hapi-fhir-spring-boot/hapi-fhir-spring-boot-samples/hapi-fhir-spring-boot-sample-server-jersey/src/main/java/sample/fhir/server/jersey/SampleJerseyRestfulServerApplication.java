package sample.fhir.server.jersey;

import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SampleJerseyRestfulServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleJerseyRestfulServerApplication.class, args);
    }

    @Bean
    public LoggingInterceptor loggingInterceptor() {
        return new LoggingInterceptor();
    }
}
