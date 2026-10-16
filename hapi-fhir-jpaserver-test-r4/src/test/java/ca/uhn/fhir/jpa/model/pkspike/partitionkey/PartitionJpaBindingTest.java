package ca.uhn.fhir.jpa.model.pkspike.partitionkey;

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
 * Try out the new @{@link org.hibernate.annotations.PartitionKey} annotation.
 * This annotation annotates columns to include in entity update/delete statements, so they can be efficient in a partitioned table.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	PartitionTypesConfig.class, PKSpikeDefaultJPAConfig.class, FhirContextR4Config.class
})
class PartitionJpaBindingTest {

	static final BasicEntityTestFixture<ResRootPartitionEntity, ResJoinPartitionEntity> ourConfig = BasicEntityTestFixture.buildNoNullPartition(ResRootPartitionEntity.class, ResJoinPartitionEntity.class);

	@RegisterExtension
	static final ParameterResolver ourResolver = ValueTypeBasedParameterResolver.build(ourConfig);

	@Nested
	class Common extends BasicEntityTestTemplate<ResRootPartitionEntity, ResJoinPartitionEntity> {
		Common() {
			super(ourConfig);
		}
	}

}
