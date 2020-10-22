package ca.uhn.fhir.cql;

import ca.uhn.fhir.cql.config.CqlConfig;
import ca.uhn.fhir.cql.config.TestCqlConfigR4;
import ca.uhn.fhir.jpa.subscription.match.config.SubscriptionProcessorConfig;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CqlConfig.class, TestCqlConfigR4.class, SubscriptionProcessorConfig.class})
abstract public class BaseCqlR4Test extends BaseJpaR4Test {
}
