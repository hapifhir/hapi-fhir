package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.util.ExpungeOptions;
import ca.uhn.fhir.jpa.util.ExpungeOutcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

@Service
public abstract class ExpungeService {
	private static final Logger ourLog = LoggerFactory.getLogger(ExpungeService.class);

	@Lookup
	protected abstract ExpungeRun getExpungeRun(String theResourceName, Long theResourceId, Long theVersion, ExpungeOptions theExpungeOptions);

	public ExpungeOutcome expunge(String theResourceName, Long theResourceId, Long theVersion, ExpungeOptions theExpungeOptions) {
		ExpungeRun expungeRun = getExpungeRun(theResourceName, theResourceId, theVersion, theExpungeOptions);
		return expungeRun.call();
	}
}
