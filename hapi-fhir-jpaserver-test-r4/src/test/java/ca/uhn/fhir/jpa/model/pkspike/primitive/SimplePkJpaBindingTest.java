package ca.uhn.fhir.jpa.model.pkspike.primitive;

import ca.uhn.fhir.jpa.config.r4.FhirContextR4Config;
import ca.uhn.fhir.jpa.model.pkspike.BasicEntityTestTemplate;
import ca.uhn.fhir.jpa.model.pkspike.BasicEntityTestFixture;
import ca.uhn.fhir.jpa.model.pkspike.PKSpikeDefaultJPAConfig;
import ca.uhn.fhir.jpa.model.pkspike.ValueTypeBasedParameterResolver;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * The simple scenario - single column keys and joins.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	SimpleTypesConfig.class, PKSpikeDefaultJPAConfig.class, FhirContextR4Config.class
})
public class SimplePkJpaBindingTest extends BasicEntityTestTemplate<ResRootEntity, ResJoinEntity> {

	public static final BasicEntityTestFixture<ResRootEntity, ResJoinEntity> ourFixture = BasicEntityTestFixture.build(ResRootEntity.class, ResJoinEntity.class);
	@RegisterExtension
	static final ParameterResolver ourFixtureResolver = ValueTypeBasedParameterResolver.build(ourFixture);

	public SimplePkJpaBindingTest() {
		super(ourFixture);
	}

}
