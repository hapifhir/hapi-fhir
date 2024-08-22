package ca.uhn.fhir.jpa.model.pkspike.idclass;

import ca.uhn.fhir.jpa.config.r4.FhirContextR4Config;
import ca.uhn.fhir.jpa.model.pkspike.BasicEntityTestTemplate;
import ca.uhn.fhir.jpa.model.pkspike.EntityFixture;
import ca.uhn.fhir.jpa.model.pkspike.PKSpikeDefaultJPAConfig;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Spike to assess variable binding against a db.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	IdClassKeyTypesConfig.class, PKSpikeDefaultJPAConfig.class, FhirContextR4Config.class
})
public class IdClassPkJpaBindingTest {

	@Nested
	class Common extends BasicEntityTestTemplate<ResRootIdClassEntity, ResJoinIdClassEntity> {
		Common() {
			super(EntityFixture.build(ResRootIdClassEntity.class, ResJoinIdClassEntity.class));
		}
	}
}
