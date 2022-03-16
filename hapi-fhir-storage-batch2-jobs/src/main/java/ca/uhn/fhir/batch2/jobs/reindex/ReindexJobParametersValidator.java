package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.jpa.api.svc.IResourceReindexSvc;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class ReindexJobParametersValidator implements IJobParametersValidator<ReindexJobParameters> {

	@Autowired
	private IResourceReindexSvc myResourceReindexSvc;

	@Nullable
	@Override
	public List<String> validate(@Nonnull ReindexJobParameters theParameters) {
		if (theParameters.getUrl().isEmpty()) {
			if (!myResourceReindexSvc.isAllResourceTypeSupported()) {
				return Collections.singletonList("At least one type-specific search URL must be provided for " + ProviderConstants.OPERATION_REINDEX + " on this server");
			}
		}

		return Collections.emptyList();
	}

}
