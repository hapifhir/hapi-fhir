package ca.uhn.fhir.jpa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({JpaDstu2Config.class, HapiJpaConfig.class})
public class HapiJpaDstu2Config {
}
