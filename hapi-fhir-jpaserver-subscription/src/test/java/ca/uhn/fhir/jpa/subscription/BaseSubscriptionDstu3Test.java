package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.subscription.config.TestSubscriptionDstu3Config;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {TestSubscriptionDstu3Config.class})
public class BaseSubscriptionDstu3Test extends BaseSubscriptionTest {
}
