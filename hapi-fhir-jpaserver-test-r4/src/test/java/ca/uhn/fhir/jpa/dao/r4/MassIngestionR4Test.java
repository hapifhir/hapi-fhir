package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MassIngestionR4Test extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(MassIngestionR4Test.class);
	@BeforeEach
	void beforeEach() {
		myStorageSettings.setMassIngestionMode(true);
	}

	@AfterEach
	public void afterEach() {
		myStorageSettings.setMassIngestionMode(new JpaStorageSettings().isMassIngestionMode());
	}

	@Test
	void testUpdateWithClientAssignedId_canFindBySearch() {
	    // given
		Patient p = new Patient();
		p.setId("client-id");
		// first update is create.  V1
		myPatientDao.update(p, mySrd);
		assertEquals("1", p.getMeta().getVersionId());

		// Second update should update. Create V2
		Patient pUpdated = new Patient();
		pUpdated.setId("client-id");
		pUpdated.setActive(true);
		Bundle b = new BundleBuilder(myFhirContext).addTransactionUpdateEntry(pUpdated).andThen().getBundleTyped();
		var txResult = mySystemDao.transaction(new SystemRequestDetails(), b);
		//myPatientDao.update(pUpdated, new SystemRequestDetails());
		assertEquals("2", pUpdated.getMeta().getVersionId());

		new TransactionTemplate(myTxManager).executeWithoutResult(t -> {
			JpaPid pid = myIdHelperService.getPidOrNull(null, pUpdated);
			var versions = myResourceHistoryTableDao.findAllVersionsForResourceIdInOrder(pid.toFk());
			assertThat(versions).hasSize(2);
			ourLog.info("{}", versions);
		});

	    // when
		var resources = myTestDaoSearch.searchForResources("Patient?active=true");

	    // then
		assertThat(resources).isNotEmpty()
			.first()
			.describedAs("search should find resource body.")
			.isNotNull();
	}

}
