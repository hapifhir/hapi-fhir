package ca.uhn.fhir.jpa.model.pkspike.partitionkey;

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
	PartitionTypesConfig.class, PKSpikeDefaultJPAConfig.class, FhirContextR4Config.class
})
class PartitionJpaBindingTest {

	static final EntityFixture<ResRootPartitionEntity, ResJoinPartitionEntity> ourConfig = EntityFixture.buildNoNullPartition(ResRootPartitionEntity.class, ResJoinPartitionEntity.class);

	@RegisterExtension
	static final ParameterResolver ourResolver = new ValueTypeBasedParameterResolver(ourConfig);

	@Nested
	class Common extends BasicEntityTestTemplate {
		Common() {
			super(ourConfig);
		}
	}

}
