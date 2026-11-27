package ca.uhn.fhir.jpa.model.pkspike.embeddedid;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;

@Configuration
public class EmbeddedIdTypesConfig {
	@Bean
	PersistenceManagedTypes getManagedTypes() {
		return PersistenceManagedTypes.of(
			ResRootEmbeddedIdEntity.class.getName(),
			ResJoinEmbeddedIdEntity.class.getName()
		);
	}

}
