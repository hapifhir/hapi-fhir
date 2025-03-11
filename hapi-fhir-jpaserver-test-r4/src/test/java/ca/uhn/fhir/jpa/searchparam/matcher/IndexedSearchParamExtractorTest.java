package ca.uhn.fhir.jpa.searchparam.matcher;

import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

import static org.assertj.core.api.Assertions.assertThat;

public class IndexedSearchParamExtractorTest extends BaseJpaR4Test {
	@Autowired
	PlatformTransactionManager myPlatformTransactionManager;

	// SUT: System Under Test
	@Autowired
	IndexedSearchParamExtractor mySrv;

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myPlatformTransactionManager;
	}

	@Test
	public void extractIndexedSearchParams_twoReferences_twoLinks() {
		Encounter encounter = new Encounter();
		Appointment appt1 = new Appointment();
		appt1.setId("Appointment/appt1");
		encounter.addAppointment(new Reference(appt1));
		Appointment appt2 = new Appointment();
		appt2.setId("Appointment/appt2");
		encounter.addAppointment(new Reference(appt2));

		ResourceIndexedSearchParams result = mySrv.extractIndexedSearchParams(encounter, mySrd);
		// red-green before the fix, the size was 1
		assertThat(result.myLinks).hasSize(2);
	}
}
