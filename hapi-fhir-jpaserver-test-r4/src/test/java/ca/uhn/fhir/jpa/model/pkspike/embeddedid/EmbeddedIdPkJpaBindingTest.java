package ca.uhn.fhir.jpa.model.pkspike.embeddedid;

import ca.uhn.fhir.jpa.config.r4.FhirContextR4Config;
import ca.uhn.fhir.jpa.model.pkspike.BasicEntityTestTemplate;
import ca.uhn.fhir.jpa.model.pkspike.BasicEntityTestFixture;
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
	EmbeddedIdTypesConfig.class, PKSpikeDefaultJPAConfig.class, FhirContextR4Config.class
})
public class EmbeddedIdPkJpaBindingTest {

	static final BasicEntityTestFixture<ResRootEmbeddedIdEntity, ResJoinEmbeddedIdEntity> ourFixture = BasicEntityTestFixture.buildNoNullPartition(ResRootEmbeddedIdEntity.class, ResJoinEmbeddedIdEntity.class);

	@RegisterExtension
	static final ParameterResolver ourResolver = ValueTypeBasedParameterResolver.build(ourFixture);


	@Nested
	class Common extends BasicEntityTestTemplate<ResRootEmbeddedIdEntity, ResJoinEmbeddedIdEntity> {
		Common() {
			super(ourFixture);
		}
	}

}
