package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.IParser;
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

public class MassIngestionR4Test extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(MassIngestionR4Test.class);
	private IParser myParser;

	@BeforeEach
	void beforeEach() {
		myParser = myFhirContext.newJsonParser();
		myStorageSettings.setMassIngestionMode(true);
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
	}

	@AfterEach
	public void afterEach() {
		myStorageSettings.setMassIngestionMode(new JpaStorageSettings().isMassIngestionMode());
	}

	@Test
	void testUpdateWithClientAssignedId_canFindBySearch() {
		// given a bundle creating a patient with a client assigned ID
		Patient pUpdated = new Patient();
		pUpdated.setId("client-id");
		pUpdated.setActive(true);
		Bundle b = new BundleBuilder(myFhirContext).addTransactionUpdateEntry(pUpdated).andThen().getBundleTyped();
		String bString = myParser.encodeResourceToString(b);

		var txResult = mySystemDao.transaction(new SystemRequestDetails(), myParser.parseResource(Bundle.class, bString));
		var txResult2 = mySystemDao.transaction(new SystemRequestDetails(), myParser.parseResource(Bundle.class, bString));

	    // when
		var resources = myTestDaoSearch.searchForResources("Patient?active=true");

	    // then
		new TransactionTemplate(myTxManager).executeWithoutResult(t -> {
			JpaPid pid = myIdHelperService.getPidOrThrowException(RequestPartitionId.allPartitions(), new IdDt("Patient/client-id"));
			var resourceTable = myResourceTableDao.findById(pid);
			assertThat(resourceTable.get().getVersion()).isEqualTo(2);

			var versions = myResourceHistoryTableDao.findAllVersionsForResourceIdInOrder(pid.toFk());
			assertThat(versions).hasSize(2);
			ourLog.info("{}", versions);
		});

		assertThat(resources).isNotEmpty()
			.first()
			.describedAs("search should find resource body.")
			.isNotNull();


	}

}
