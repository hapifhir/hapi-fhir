package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.subscription.config.BaseSubscriptionDstu3Config;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BaseSubscriptionDstu3Config.class})
public class BaseSubscriptionDstu3Test extends BaseSubscriptionTest {
}
