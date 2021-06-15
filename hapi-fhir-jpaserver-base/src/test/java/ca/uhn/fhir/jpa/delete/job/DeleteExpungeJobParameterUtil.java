package ca.uhn.fhir.jpa.delete.job;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import com.github.jsonldjava.shaded.com.google.common.collect.Lists;
import org.springframework.batch.core.JobParameters;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public final class DeleteExpungeJobParameterUtil {
	private DeleteExpungeJobParameterUtil() {
	}

	@Nonnull
	public static JobParameters buildJobParameters(String... theUrls) {
		List<RequestPartitionId> requestPartitionIds = new ArrayList<>();
		for (int i = 0; i < theUrls.length; ++i) {
			requestPartitionIds.add(RequestPartitionId.defaultPartition());
		}
		return DeleteExpungeJobConfig.buildJobParameters(2401, Lists.newArrayList(theUrls), requestPartitionIds);
	}
}
