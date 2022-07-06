package ca.uhn.fhir.batch2.jobs.parameters;

import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class UrlListValidator {
	private final String myOperationName;
	private final IBatch2DaoSvc myBatch2DaoSvc;

	public UrlListValidator(String theOperationName, IBatch2DaoSvc theBatch2DaoSvc) {
		myOperationName = theOperationName;
		myBatch2DaoSvc = theBatch2DaoSvc;
	}


	@Nullable
	public List<String> validate(@Nonnull List<String> theUrls) {
			if (theUrls.isEmpty()) {
				if (!myBatch2DaoSvc.isAllResourceTypeSupported()) {
					return Collections.singletonList("At least one type-specific search URL must be provided for " + myOperationName + " on this server");
				}
			}
		return Collections.emptyList();
	}

}
