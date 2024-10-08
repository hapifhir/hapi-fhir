package ca.uhn.fhir.jpa.model.pkspike.primitive;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;

@Configuration
public class SimpleTypesConfig {
	@Bean
	PersistenceManagedTypes getManagedTypes() {
		return PersistenceManagedTypes.of(
			ResRootEntity.class.getName(),
			ResJoinEntity.class.getName()
		);
	}

}
