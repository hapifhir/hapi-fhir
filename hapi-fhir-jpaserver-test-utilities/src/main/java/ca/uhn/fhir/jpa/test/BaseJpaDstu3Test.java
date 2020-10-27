package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.jpa.config.TestJpaDstu3Config;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {TestJpaDstu3Config.class})
public abstract class BaseJpaDstu3Test extends BaseJpaTest {
}
