package ca.uhn.fhir.batch2.api;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface IJobDefinitionRegistry {
	Optional<IBatchJobDefinition> getLatestJobDefinition(@Nonnull String theJobDefinitionId);

}
