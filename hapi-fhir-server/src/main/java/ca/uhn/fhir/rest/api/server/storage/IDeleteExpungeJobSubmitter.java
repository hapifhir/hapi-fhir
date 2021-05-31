package ca.uhn.fhir.rest.api.server.storage;

import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.batch.core.JobParametersInvalidException;

import java.util.List;

public interface IDeleteExpungeJobSubmitter {
	IBaseParameters submitJob(List<IPrimitiveType<String>> theUrlsToExpungeDelete) throws JobParametersInvalidException;
}
