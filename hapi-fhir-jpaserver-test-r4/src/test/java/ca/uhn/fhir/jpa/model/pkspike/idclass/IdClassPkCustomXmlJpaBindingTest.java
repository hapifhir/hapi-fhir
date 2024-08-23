package ca.uhn.fhir.jpa.model.pkspike.idclass;

import ca.uhn.fhir.jpa.config.HapiFhirLocalContainerEntityManagerFactoryBean;
import ca.uhn.fhir.jpa.config.r4.FhirContextR4Config;
import ca.uhn.fhir.jpa.model.pkspike.BasicEntityTestTemplate;
import ca.uhn.fhir.jpa.model.pkspike.EntityFixture;
import ca.uhn.fhir.jpa.model.pkspike.PKSpikeDefaultJPAConfig;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.function.Consumer;

/**
 * Spike to assess variable binding against a db.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	IdClassPkCustomXmlJpaBindingTest.Config.class, IdClassKeyTypesConfig.class, PKSpikeDefaultJPAConfig.class, FhirContextR4Config.class
})
public class IdClassPkCustomXmlJpaBindingTest {
	@Configuration
	static class Config {
		@Bean
		Consumer<HapiFhirLocalContainerEntityManagerFactoryBean> entityManagerFactoryCustomizer() {
			return em->{
				em.setPersistenceXmlLocation("classpath:/ca/uhn/fhir/jpa/model/pkspike/idclass/persistenceOverride.xml");
			};
		}

	}

	@Nested
	class Common extends BasicEntityTestTemplate<ResRootIdClassEntity, ResJoinIdClassEntity> {
		Common() {
			super(EntityFixture.build(ResRootIdClassEntity.class, ResJoinIdClassEntity.class));
		}
	}
}
