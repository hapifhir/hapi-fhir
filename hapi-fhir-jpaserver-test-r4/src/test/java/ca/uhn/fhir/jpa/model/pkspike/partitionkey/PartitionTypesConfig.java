package ca.uhn.fhir.jpa.model.pkspike.partitionkey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;

@Configuration
public class PartitionTypesConfig {
	@Bean
	PersistenceManagedTypes getManagedTypes() {
		return PersistenceManagedTypes.of(
			ResRootPartitionEntity.class.getName(),
			ResJoinPartitionEntity.class.getName()
		);
	}

}
