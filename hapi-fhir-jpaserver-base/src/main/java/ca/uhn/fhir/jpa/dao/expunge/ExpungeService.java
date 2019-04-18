package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.util.ExpungeOptions;
import ca.uhn.fhir.jpa.util.ExpungeOutcome;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

@Service
public abstract class ExpungeService {
	private static final Logger ourLog = LoggerFactory.getLogger(ExpungeService.class);

	@Autowired
	private DaoConfig myConfig;
	@Autowired
	private ExpungeEverythingService myExpungeEverythingService;

	@Lookup
	protected abstract ExpungeRun getExpungeRun(String theResourceName, Long theResourceId, Long theVersion, ExpungeOptions theExpungeOptions);

	public ExpungeOutcome expunge(String theResourceName, Long theResourceId, Long theVersion, ExpungeOptions theExpungeOptions) {
		ourLog.info("Expunge: ResourceName[{}] Id[{}] Version[{}] Options[{}]", theResourceName, theResourceId, theVersion, theExpungeOptions);

		if (!myConfig.isExpungeEnabled()) {
			throw new MethodNotAllowedException("$expunge is not enabled on this server");
		}

		if (theExpungeOptions.getLimit() < 1) {
			throw new InvalidRequestException("Expunge limit may not be less than 1.  Received expunge limit " + theExpungeOptions.getLimit() + ".");
		}

		if (theResourceName == null && theResourceId == null && theVersion == null) {
			if (theExpungeOptions.isExpungeEverything()) {
				myExpungeEverythingService.expungeEverything();
			}
		}

		ExpungeRun expungeRun = getExpungeRun(theResourceName, theResourceId, theVersion, theExpungeOptions);
		return expungeRun.call();
	}
}
