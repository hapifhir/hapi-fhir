package ca.uhn.fhir.jpa.model.pkspike.idclass;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;

@Configuration
public class IdClassKeyTypesConfig {
	@Bean
	PersistenceManagedTypes getManagedTypes() {
		return PersistenceManagedTypes.of(
			ResRootIdClassEntity.class.getName(),
			ResJoinIdClassEntity.class.getName()
		);
	}

}
