package ca.uhn.fhir.jpa.batch.job;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.batch.reader.ReverseCronologicalBatchResourcePidReader;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import com.github.jsonldjava.shaded.com.google.common.collect.Lists;
import org.springframework.batch.core.JobParameters;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public final class MultiUrlJobParameterUtil {
	private MultiUrlJobParameterUtil() {
	}

	@Nonnull
	public static JobParameters buildJobParameters(String... theUrls) {
		List<RequestPartitionId> requestPartitionIds = new ArrayList<>();
		for (int i = 0; i < theUrls.length; ++i) {
			requestPartitionIds.add(RequestPartitionId.defaultPartition());
		}
		return ReverseCronologicalBatchResourcePidReader.buildJobParameters(ProviderConstants.OPERATION_REINDEX, 2401, Lists.newArrayList(theUrls), requestPartitionIds);
	}
}
