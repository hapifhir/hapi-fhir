package ca.uhn.fhir.batch2.jobs.parameters;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IUrlListValidator {
    @Nullable
    List<String> validateUrls(@Nonnull List<String> theUrls);

    @Nullable
    List<String> validatePartitionedUrls(@Nonnull List<PartitionedUrl> thePartitionedUrls);
}
