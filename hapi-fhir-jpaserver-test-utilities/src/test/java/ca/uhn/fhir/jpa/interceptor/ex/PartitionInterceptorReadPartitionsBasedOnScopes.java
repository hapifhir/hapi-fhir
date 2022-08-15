package ca.uhn.fhir.jpa.interceptor.ex;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ValidateUtil;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;

// This class is replicated in PartitionExamples.java -- Keep it up to date there too!!
@Interceptor
public class PartitionInterceptorReadPartitionsBasedOnScopes {

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
	public RequestPartitionId readPartition(ServletRequestDetails theRequest) {

		HttpServletRequest servletRequest = theRequest.getServletRequest();
		Set<String> approvedScopes = (Set<String>) servletRequest.getAttribute("ca.cdr.servletattribute.session.oidc.approved_scopes");

		String partition = approvedScopes
			.stream()
			.filter(t->t.startsWith("partition-"))
			.map(t->t.substring("partition-".length()))
			.findFirst()
			.orElseThrow(()->new InvalidRequestException("No partition scopes found in request"));
		return RequestPartitionId.fromPartitionName(partition);

	}

}
