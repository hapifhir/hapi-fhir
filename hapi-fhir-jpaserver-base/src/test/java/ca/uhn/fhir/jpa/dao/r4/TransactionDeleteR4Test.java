package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class TransactionDeleteR4Test extends BaseJpaR4SystemTest {

	@AfterEach
	public void after() {
		myDaoConfig.setEnforceReferentialIntegrityOnDelete(true);
	}


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

		Bundle b = new Bundle();
		b.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl(rptId.getValue());
		b.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl(obs2id.getValue());

		try {
			// transaction should succeed because the DiagnosticReport which references obs2 is also deleted
			mySystemDao.transaction(mySrd, b);
		} catch (ResourceVersionConflictException e) {
			fail();
		}
	}


	@Test
	public void testDeleteWithHas_SourceModifiedToNoLongerIncludeReference() {

		Observation obs1 = new Observation();
		obs1.setStatus(Observation.ObservationStatus.FINAL);
		IIdType obs1id = myObservationDao.create(obs1).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.setStatus(Observation.ObservationStatus.FINAL);
		IIdType obs2id = myObservationDao.create(obs2).getId().toUnqualifiedVersionless();

		DiagnosticReport rpt = new DiagnosticReport();
		rpt.addIdentifier().setSystem("foo").setValue("IDENTIFIER");
		rpt.addResult(new Reference(obs2id));
		IIdType rptId = myDiagnosticReportDao.create(rpt).getId().toUnqualifiedVersionless();

		myObservationDao.read(obs1id);
		myObservationDao.read(obs2id);

		rpt = new DiagnosticReport();
		rpt.addIdentifier().setSystem("foo").setValue("IDENTIFIER");

		Bundle b = new Bundle();
		b.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl("Observation?_has:DiagnosticReport:result:identifier=foo|IDENTIFIER");
		b.addEntry().setResource(rpt).getRequest().setMethod(Bundle.HTTPVerb.PUT).setUrl("DiagnosticReport?identifier=foo|IDENTIFIER");
		mySystemDao.transaction(mySrd, b);

		myObservationDao.read(obs1id);
		try {
			myObservationDao.read(obs2id);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		rpt = myDiagnosticReportDao.read(rptId);
		assertThat(rpt.getResult(), empty());
	}

	@Test
	public void testDeleteWithId_SourceModifiedToNoLongerIncludeReference() {

		Observation obs1 = new Observation();
		obs1.setStatus(Observation.ObservationStatus.FINAL);
		IIdType obs1id = myObservationDao.create(obs1).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.setStatus(Observation.ObservationStatus.FINAL);
		IIdType obs2id = myObservationDao.create(obs2).getId().toUnqualifiedVersionless();

		DiagnosticReport rpt = new DiagnosticReport();
		rpt.addResult(new Reference(obs1id));
		IIdType rptId = myDiagnosticReportDao.create(rpt).getId().toUnqualifiedVersionless();

		myObservationDao.read(obs1id);
		myObservationDao.read(obs2id);

		rpt = new DiagnosticReport();
		rpt.addResult(new Reference(obs2id));

		Bundle b = new Bundle();
		b.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl(obs1id.getValue());
		b.addEntry().setResource(rpt).getRequest().setMethod(Bundle.HTTPVerb.PUT).setUrl(rptId.getValue());
		mySystemDao.transaction(mySrd, b);

		myObservationDao.read(obs2id);
		myDiagnosticReportDao.read(rptId);
		try {
			myObservationDao.read(obs1id);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

	}


	@Test
	public void testDeleteWithHas_SourceModifiedToStillIncludeReference() {

		Observation obs1 = new Observation();
		obs1.setStatus(Observation.ObservationStatus.FINAL);
		IIdType obs1id = myObservationDao.create(obs1).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.setStatus(Observation.ObservationStatus.FINAL);
		IIdType obs2id = myObservationDao.create(obs2).getId().toUnqualifiedVersionless();

		DiagnosticReport rpt = new DiagnosticReport();
		rpt.addIdentifier().setSystem("foo").setValue("IDENTIFIER");
		rpt.addResult(new Reference(obs2id));
		IIdType rptId = myDiagnosticReportDao.create(rpt).getId().toUnqualifiedVersionless();

		myObservationDao.read(obs1id);
		myObservationDao.read(obs2id);

		rpt = new DiagnosticReport();
		rpt.addIdentifier().setSystem("foo").setValue("IDENTIFIER");
		rpt.addResult(new Reference(obs2id));

		Bundle b = new Bundle();
		b.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl("Observation?_has:DiagnosticReport:result:identifier=foo|IDENTIFIER");
		b.addEntry().setResource(rpt).getRequest().setMethod(Bundle.HTTPVerb.PUT).setUrl("DiagnosticReport?identifier=foo|IDENTIFIER");
		try {
			mySystemDao.transaction(mySrd, b);
			fail();
		} catch (ResourceVersionConflictException e ) {
			assertThat(e.getMessage(), matchesPattern(Msg.code(550) + Msg.code(515) + "Unable to delete Observation/[0-9]+ because at least one resource has a reference to this resource. First reference found was resource DiagnosticReport/[0-9]+ in path DiagnosticReport.result"));
		}

		myObservationDao.read(obs1id);
		myObservationDao.read(obs2id);
		myDiagnosticReportDao.read(rptId);
	}
}
