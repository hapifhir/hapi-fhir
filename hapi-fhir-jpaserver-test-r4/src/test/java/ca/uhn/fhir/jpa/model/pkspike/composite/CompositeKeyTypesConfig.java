package ca.uhn.fhir.jpa.model.pkspike.composite;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;

@Configuration
public class CompositeKeyTypesConfig {
	@Bean
	PersistenceManagedTypes getManagedTypes() {
		return PersistenceManagedTypes.of(
			ResRootCompositeEntity.class.getName(),
			ResJoinCompositeEntity.class.getName()
		);
	}

}
