package ca.uhn.fhir.jpa.model.pkspike.idclass;

import ca.uhn.fhir.jpa.config.HapiFhirLocalContainerEntityManagerFactoryBean;
import ca.uhn.fhir.jpa.config.r4.FhirContextR4Config;
import ca.uhn.fhir.jpa.model.pkspike.BasicEntityTestTemplate;
import ca.uhn.fhir.jpa.model.pkspike.BasicEntityTestFixture;
import ca.uhn.fhir.jpa.model.pkspike.PKSpikeDefaultJPAConfig;
import ca.uhn.fhir.jpa.model.pkspike.ValueTypeBasedParameterResolver;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.function.Consumer;

/**
 * Override the JPA annotations with an orm.xml file to add PARTITION_ID to the root PK, and the join expressions.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	IdClassPkCustomXmlJpaBindingTest.Config.class, IdClassKeyTypesConfig.class, PKSpikeDefaultJPAConfig.class, FhirContextR4Config.class
})
public class IdClassPkCustomXmlJpaBindingTest {
	private static final Logger ourLog = LoggerFactory.getLogger(IdClassPkCustomXmlJpaBindingTest.class);

	@Configuration
	static class Config {
		@Bean
		Consumer<HapiFhirLocalContainerEntityManagerFactoryBean> entityManagerFactoryCustomizer() {
			return em->{
				ourLog.info("Injecting custom persistence.xml");
				em.setMappingResources("/ca/uhn/fhir/jpa/model/pkspike/idclass/ormComposite.xml");
			};
		}
	}

	static final BasicEntityTestFixture<ResRootIdClassEntity, ResJoinIdClassEntity> ourFixture = BasicEntityTestFixture.buildNoNullPartition(ResRootIdClassEntity.class, ResJoinIdClassEntity.class);
	@RegisterExtension
	static final ParameterResolver ourFixtureResolver = ValueTypeBasedParameterResolver.build(ourFixture);

	@Nested
	class Common extends BasicEntityTestTemplate<ResRootIdClassEntity, ResJoinIdClassEntity> {
		Common() {
			super(ourFixture);
		}
	}
}
