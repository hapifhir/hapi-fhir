package ca.uhn.fhir.cql;

import ca.uhn.fhir.cql.config.CqlDstu3Config;
import ca.uhn.fhir.cql.config.TestCqlConfig;
import ca.uhn.fhir.jpa.subscription.match.config.SubscriptionProcessorConfig;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestCqlConfig.class, SubscriptionProcessorConfig.class, CqlDstu3Config.class})
public class BaseCqlDstu3Test extends BaseJpaDstu3Test {
}
