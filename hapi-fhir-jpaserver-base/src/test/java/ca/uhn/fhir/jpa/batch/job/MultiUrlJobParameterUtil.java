package ca.uhn.fhir.jpa.batch.job;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.batch.job.model.PartitionedUrl;
import ca.uhn.fhir.jpa.batch.job.model.RequestListJson;
import ca.uhn.fhir.jpa.batch.reader.ReverseCronologicalBatchResourcePidReader;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.springframework.batch.core.JobParameters;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public final class MultiUrlJobParameterUtil {
	private MultiUrlJobParameterUtil() {
	}

	@Nonnull
	public static JobParameters buildJobParameters(String... theUrls) {
		List<PartitionedUrl> partitionedUrls = new ArrayList<>();
		for (String url : theUrls) {
			partitionedUrls.add(new PartitionedUrl(url, RequestPartitionId.defaultPartition()));
		}

		RequestListJson requestListJson = new RequestListJson();
		requestListJson.setPartitionedUrls(partitionedUrls);
		return ReverseCronologicalBatchResourcePidReader.buildJobParameters(ProviderConstants.OPERATION_REINDEX, 2401, requestListJson);
	}
}
