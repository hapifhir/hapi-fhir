package ca.uhn.fhir.jpa.model.pkspike.idclass;

import ca.uhn.fhir.jpa.config.r4.FhirContextR4Config;
import ca.uhn.fhir.jpa.model.pkspike.BasicEntityTestTemplate;
import ca.uhn.fhir.jpa.model.pkspike.EntityFixture;
import ca.uhn.fhir.jpa.model.pkspike.PKSpikeDefaultJPAConfig;
import ca.uhn.fhir.jpa.model.pkspike.ValueTypeBasedParameterResolver;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Spike to assess variable binding against a db.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	IdClassKeyTypesConfig.class, PKSpikeDefaultJPAConfig.class, FhirContextR4Config.class
})
public class IdClassPkJpaBindingTest {

	public static final EntityFixture<ResRootIdClassEntity, ResJoinIdClassEntity> ourFixture = EntityFixture.build(ResRootIdClassEntity.class, ResJoinIdClassEntity.class);
	@RegisterExtension
	static final ParameterResolver ourFixtureResolver = new ValueTypeBasedParameterResolver<>(ourFixture);

	@Nested
	class Common extends BasicEntityTestTemplate<ResRootIdClassEntity, ResJoinIdClassEntity> {
		Common() {
			super(ourFixture);
		}
	}
}
