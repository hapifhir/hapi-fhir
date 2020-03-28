package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.jpa.config.TestJpaR4Config;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {TestJpaR4Config.class})
// FIXME EMPI use this other places that need it
public abstract class BaseJpaR4Test extends BaseJpaTest {

}
