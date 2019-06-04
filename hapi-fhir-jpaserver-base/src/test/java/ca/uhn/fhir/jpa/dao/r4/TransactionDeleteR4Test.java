package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.config.TestR4Config;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestR4Config.class})
public class TransactionDeleteR4Test extends BaseJpaR4SystemTest {

	@Test
	public void testDeleteInTransactionShouldFailWhenReferencesExist() {
		final Observation obs1 = new Observation();
		obs1.setStatus(Observation.ObservationStatus.FINAL);
		IIdType obs1id = myObservationDao.create(obs1).getId().toUnqualifiedVersionless();

		final Observation obs2 = new Observation();
		obs2.setStatus(Observation.ObservationStatus.FINAL);
		org.hl7.fhir.instance.model.api.IIdType obs2id = myObservationDao.create(obs2).getId().toUnqualifiedVersionless();

		final DiagnosticReport rpt = new DiagnosticReport();
		rpt.addResult(new Reference(obs2id));
		IIdType rptId = myDiagnosticReportDao.create(rpt).getId().toUnqualifiedVersionless();

		myObservationDao.read(obs1id);
		myObservationDao.read(obs2id);
		myDiagnosticReportDao.read(rptId);

		Bundle b = new Bundle();
		b.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl(obs2id.getValue());

		try {
			mySystemDao.transaction(mySrd, b);
			fail();
		} catch (ResourceVersionConflictException e) {
			// good, transaction should not succeed because DiagnosticReport has a reference to the obs2
		}
	}

	@Test
	public void testDeleteInTransactionShouldSucceedWhenReferencesAreAlsoRemoved() {
		final Observation obs1 = new Observation();
		obs1.setStatus(Observation.ObservationStatus.FINAL);
		IIdType obs1id = myObservationDao.create(obs1).getId().toUnqualifiedVersionless();

		final Observation obs2 = new Observation();
		obs2.setStatus(Observation.ObservationStatus.FINAL);
		IIdType obs2id = myObservationDao.create(obs2).getId().toUnqualifiedVersionless();

		final DiagnosticReport rpt = new DiagnosticReport();
		rpt.addResult(new Reference(obs2id));
		IIdType rptId = myDiagnosticReportDao.create(rpt).getId().toUnqualifiedVersionless();

		myObservationDao.read(obs1id);
		myObservationDao.read(obs2id);
		myDiagnosticReportDao.read(rptId);

		/*
		 * We're deleting the Observation, which the DiagnosticReport refers to, so it
		 * normally should cause a constraint error... BUT we're also removing the reference
		 * so it actually is ok. Magical.
		 */

		DiagnosticReport rpt2 = new DiagnosticReport();
		rpt2.setId(rptId);
		rpt2.setStatus(DiagnosticReport.DiagnosticReportStatus.FINAL);

		Bundle b = new Bundle();
		b.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl(obs2id.getValue());
		b.addEntry().setResource(rpt2).getRequest().setMethod(Bundle.HTTPVerb.PUT).setUrl(rptId.getValue());

		try {
			// transaction should succeed because the DiagnosticReport which references obs2 is also deleted
			mySystemDao.transaction(mySrd, b);
		} catch (ResourceVersionConflictException e) {
			fail();
		}
	}
}
