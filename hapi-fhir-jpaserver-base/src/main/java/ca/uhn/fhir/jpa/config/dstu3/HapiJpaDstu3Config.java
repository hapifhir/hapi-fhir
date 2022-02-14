package ca.uhn.fhir.jpa.config.dstu3;

import ca.uhn.fhir.jpa.config.HapiJpaConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

// WIP KHS remove these unneeded classes?
@Configuration
@Import({JpaDstu3Config.class, HapiJpaConfig.class})
public class HapiJpaDstu3Config {
}
