package sample.fhir.server.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SampleJpaRestfulServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleJpaRestfulServerApplication.class, args);
    }
}
