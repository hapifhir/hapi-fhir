package ca.uhn.fhir.cql;

import ca.uhn.fhir.cql.config.CqlR4Config;
import ca.uhn.fhir.cql.config.TestCqlConfig;
import ca.uhn.fhir.jpa.subscription.match.config.SubscriptionProcessorConfig;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CqlR4Config.class, TestCqlConfig.class, SubscriptionProcessorConfig.class})
public class BaseCqlR4Test extends BaseJpaR4Test {
}
