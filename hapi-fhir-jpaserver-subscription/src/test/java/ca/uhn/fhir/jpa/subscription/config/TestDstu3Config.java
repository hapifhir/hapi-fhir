package ca.uhn.fhir.jpa.subscription.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

// FIXME KHS remove?
@Configuration
@Import(TestSubscriptionConfig.class)
public class TestDstu3Config extends BaseSubscriptionDstu3Config {

}
