package ca.uhn.fhir.jpa.config.r5;

import ca.uhn.fhir.jpa.config.HapiJpaConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({JpaR5Config.class, HapiJpaConfig.class})
public class HapiJpaR5Config {
}
