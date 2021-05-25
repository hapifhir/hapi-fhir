package ca.uhn.fhir.jpa.delete;

import ca.uhn.fhir.rest.api.server.storage.IDeleteExpungeJobSubmitter;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.List;

public class DeleteExpungeJobSubmitterImpl implements IDeleteExpungeJobSubmitter {
	@Override
	public IBaseParameters submitJob(List<IPrimitiveType<String>> theUrlsToExpungeDelete) {
		return null;
	}
}
