package ca.uhn.fhir.batch2.jobs.parameters;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface IUrlListValidator {
	@Nullable
	List<String> validateUrls(@Nonnull List<String> theUrls);

	@Nullable
	List<String> validatePartitionedUrls(@Nonnull List<PartitionedUrl> thePartitionedUrls);
}
