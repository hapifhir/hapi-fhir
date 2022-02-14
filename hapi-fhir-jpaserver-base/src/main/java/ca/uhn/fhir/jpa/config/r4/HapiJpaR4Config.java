package ca.uhn.fhir.jpa.config.r4;

import ca.uhn.fhir.jpa.config.HapiJpaConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({JpaR4Config.class, HapiJpaConfig.class})
public class HapiJpaR4Config {
}
