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
 * Use an IdClass even though the PK is only a single column.
 * This allows us to extend the PK next door in the {@link IdClassPkCustomXmlJpaBindingTest}.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	IdClassPkJpaBindingTest.Config.class, IdClassKeyTypesConfig.class, PKSpikeDefaultJPAConfig.class, FhirContextR4Config.class
})
public class IdClassPkJpaBindingTest {
	private static final Logger ourLog = LoggerFactory.getLogger(IdClassPkJpaBindingTest.class);

	@Configuration
	static class Config {
		@Bean
		Consumer<HapiFhirLocalContainerEntityManagerFactoryBean> entityManagerFactoryCustomizer() {
			return em->{
				ourLog.info("Injecting custom persistence.xml");
				em.setMappingResources("/ca/uhn/fhir/jpa/model/pkspike/idclass/ormLong.xml");
			};
		}
	}

	public static final BasicEntityTestFixture<ResRootIdClassEntity, ResJoinIdClassEntity> ourFixture = BasicEntityTestFixture.build(ResRootIdClassEntity.class, ResJoinIdClassEntity.class);
	@RegisterExtension
	static final ParameterResolver ourFixtureResolver = ValueTypeBasedParameterResolver.build(ourFixture);

	@Nested
	class Common extends BasicEntityTestTemplate<ResRootIdClassEntity, ResJoinIdClassEntity> {
		Common() {
			super(ourFixture);
		}
	}
}
