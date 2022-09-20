package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.method.PageMethodBinding;

import javax.annotation.Nonnull;

public class ExceptionService {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchCoordinatorSvcImpl.class);

	private final FhirContext myContext;

    public ExceptionService(FhirContext theContext) {
			myContext = theContext;
    }

    @Nonnull
    public ResourceGoneException newUnknownSearchException(String theUuid) {
        ourLog.trace("Client requested unknown paging ID[{}]", theUuid);
        String msg = myContext.getLocalizer().getMessage(PageMethodBinding.class, "unknownSearchId", theUuid);
        return new ResourceGoneException(msg);
    }
}
