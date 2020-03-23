package ca.uhn.fhir.jpa.empi;

import ca.uhn.fhir.empi.jpalink.config.EmpiConfig;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.empi.config.EmpiTestConfig;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {EmpiConfig.class, EmpiTestConfig.class})
abstract public class BaseEmpiR4Test extends BaseJpaR4Test {
}
