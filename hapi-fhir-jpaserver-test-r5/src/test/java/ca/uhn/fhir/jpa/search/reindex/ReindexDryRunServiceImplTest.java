package ca.uhn.fhir.jpa.search.reindex;

import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class ReindexDryRunServiceImplTest extends BaseJpaR5Test {

	@Autowired
	private IReindexDryRunService mySvc;

	@Test
	public void reindexDryRun() {
		IIdType id = createPatient(withIdentifier("http://identifiers", "123"), withFamily("Smith"));

		Parameters outcome = (Parameters) mySvc.reindexDryRun(new SystemRequestDetails(), id);
		ourLog.info("Output:{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

	}

}
