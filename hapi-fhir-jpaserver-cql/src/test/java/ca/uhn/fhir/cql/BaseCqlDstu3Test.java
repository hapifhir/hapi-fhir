package ca.uhn.fhir.cql;

import ca.uhn.fhir.cql.config.CqlConfig;
import ca.uhn.fhir.cql.config.TestCqlConfig;
import ca.uhn.fhir.jpa.subscription.match.config.SubscriptionProcessorConfig;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CqlConfig.class, TestCqlConfig.class, SubscriptionProcessorConfig.class})
public class BaseCqlDstu3Test extends BaseJpaDstu3Test {
}
