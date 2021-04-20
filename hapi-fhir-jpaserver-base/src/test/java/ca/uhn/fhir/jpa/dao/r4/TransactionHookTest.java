package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.rest.api.server.storage.DeferredInterceptorBroadcasts;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.test.concurrency.PointcutLatch;
import com.google.common.collect.ListMultimap;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.ExplanationOfBenefit;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hl7.fhir.r4.model.Bundle.HTTPVerb.DELETE;
import static org.hl7.fhir.r4.model.Bundle.HTTPVerb.GET;
import static org.hl7.fhir.r4.model.Bundle.HTTPVerb.POST;
import static org.hl7.fhir.r4.model.Bundle.HTTPVerb.PUT;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class TransactionHookTest extends BaseJpaR4SystemTest {

	@AfterEach
	public void after() {
		myDaoConfig.setEnforceReferentialIntegrityOnDelete(true);
	}

	PointcutLatch myPointcutLatch = new PointcutLatch(Pointcut.STORAGE_TRANSACTION_PROCESSED);
	@Autowired
	private IInterceptorService myInterceptorService;


	@BeforeEach
	public void beforeEach() {
		myInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_TRANSACTION_PROCESSED,  myPointcutLatch);
		myInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED,  myPointcutLatch);
		myInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED,  myPointcutLatch);
		myInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED,  myPointcutLatch);
	}

	@Test
	public void testTopologicalTransactionSortForCreates() {

		Bundle b = new Bundle();
		Bundle.BundleEntryComponent bundleEntryComponent = b.addEntry();
		final Observation obs1 = new Observation();
		obs1.setStatus(Observation.ObservationStatus.FINAL);
		obs1.setSubject(new Reference("Patient/P1"));
		obs1.setValue(new Quantity(4));
		obs1.setId("Observation/O1");
		bundleEntryComponent.setResource(obs1);
		bundleEntryComponent.getRequest().setMethod(POST).setUrl("Observation");

		bundleEntryComponent = b.addEntry();
		final Observation obs2 = new Observation();
		obs2.setStatus(Observation.ObservationStatus.FINAL);
		obs2.setValue(new Quantity(4));
		obs2.setId("Observation/O2");
		bundleEntryComponent.setResource(obs2);
		bundleEntryComponent.getRequest().setMethod(POST).setUrl("Observation");

		Bundle.BundleEntryComponent patientComponent = b.addEntry();
		Patient pat1 = new Patient();
		pat1.setId("Patient/P1");
		pat1.setManagingOrganization(new Reference("Organization/Org1"));
		patientComponent.setResource(pat1);
		patientComponent.getRequest().setMethod(POST).setUrl("Patient");

		Bundle.BundleEntryComponent organizationComponent = b.addEntry();
		Organization org1 = new Organization();
		org1.setId("Organization/Org1");
		organizationComponent.setResource(org1);
		organizationComponent.getRequest().setMethod(POST).setUrl("Patient");

		BundleUtil.sortEntriesIntoProcessingOrder(myFhirCtx, b);

		assertThat(b.getEntry(), hasSize(4));

		List<Bundle.BundleEntryComponent> entry = b.getEntry();
		int observationIndex = getIndexOfEntryWithId("Observation/O1", b);
		int patientIndex = getIndexOfEntryWithId("Patient/P1", b);
		int organizationIndex = getIndexOfEntryWithId("Organization/Org1", b);

		assertTrue(organizationIndex < patientIndex);
		assertTrue(patientIndex < observationIndex);
	}

	@Test
	public void testTransactionSorterFailsOnCyclicReference() {
		Bundle b = new Bundle();
		Bundle.BundleEntryComponent bundleEntryComponent = b.addEntry();
		final Observation obs1 = new Observation();
		obs1.setStatus(Observation.ObservationStatus.FINAL);
		obs1.setSubject(new Reference("Patient/P1"));
		obs1.setValue(new Quantity(4));
		obs1.setId("Observation/O1");
		obs1.setHasMember(Collections.singletonList(new Reference("Observation/O2")));
		bundleEntryComponent.setResource(obs1);
		bundleEntryComponent.getRequest().setMethod(POST).setUrl("Observation");

		bundleEntryComponent = b.addEntry();
		final Observation obs2 = new Observation();
		obs2.setStatus(Observation.ObservationStatus.FINAL);
		obs2.setValue(new Quantity(4));
		obs2.setId("Observation/O2");
		obs2.setHasMember(Collections.singletonList(new Reference("Observation/O1")));
		bundleEntryComponent.setResource(obs2);
		bundleEntryComponent.getRequest().setMethod(POST).setUrl("Observation");
		try {
			BundleUtil.sortEntriesIntoProcessingOrder(myFhirCtx, b);
			fail();
		} catch (IllegalStateException e ) {

		}
	}

	@Test
	public void testTransactionSortingReturnsOperationsInCorrectOrder() {

		Bundle b = new Bundle();

		//UPDATE patient
		Bundle.BundleEntryComponent patientUpdateComponent= b.addEntry();
		final Patient p2 = new Patient();
		p2.setId("Patient/P2");
		p2.getNameFirstRep().setFamily("Test!");
		patientUpdateComponent.setResource(p2);
		patientUpdateComponent.getRequest().setMethod(PUT).setUrl("Patient/P2");

		//CREATE observation
		Bundle.BundleEntryComponent bundleEntryComponent = b.addEntry();
		final Observation obs1 = new Observation();
		obs1.setStatus(Observation.ObservationStatus.FINAL);
		obs1.setSubject(new Reference("Patient/P1"));
		obs1.setValue(new Quantity(4));
		obs1.setId("Observation/O1");
		bundleEntryComponent.setResource(obs1);
		bundleEntryComponent.getRequest().setMethod(POST).setUrl("Observation");

		//DELETE medication
		Bundle.BundleEntryComponent medicationComponent= b.addEntry();
		final Medication med1 = new Medication();
		med1.setId("Medication/M1");
		medicationComponent.setResource(med1);
		medicationComponent.getRequest().setMethod(DELETE).setUrl("Medication");

		//GET medication
		Bundle.BundleEntryComponent searchComponent = b.addEntry();
		searchComponent.getRequest().setMethod(GET).setUrl("Medication?code=123");

		//CREATE patient
		Bundle.BundleEntryComponent patientComponent = b.addEntry();
		Patient pat1 = new Patient();
		pat1.setId("Patient/P1");
		pat1.setManagingOrganization(new Reference("Organization/Org1"));
		patientComponent.setResource(pat1);
		patientComponent.getRequest().setMethod(POST).setUrl("Patient");

		//CREATE organization
		Bundle.BundleEntryComponent organizationComponent = b.addEntry();
		Organization org1 = new Organization();
		org1.setId("Organization/Org1");
		organizationComponent.setResource(org1);
		organizationComponent.getRequest().setMethod(POST).setUrl("Organization");

		//DELETE ExplanationOfBenefit
		Bundle.BundleEntryComponent explanationOfBenefitComponent= b.addEntry();
		final ExplanationOfBenefit eob1 = new ExplanationOfBenefit();
		eob1.setId("ExplanationOfBenefit/E1");
		explanationOfBenefitComponent.setResource(eob1);
		explanationOfBenefitComponent.getRequest().setMethod(DELETE).setUrl("ExplanationOfBenefit");

		BundleUtil.sortEntriesIntoProcessingOrder(myFhirCtx, b);

		assertThat(b.getEntry(), hasSize(7));

		List<Bundle.BundleEntryComponent> entry = b.getEntry();

		// DELETEs first
		assertThat(entry.get(0).getRequest().getMethod(), is(equalTo(DELETE)));
		assertThat(entry.get(1).getRequest().getMethod(), is(equalTo(DELETE)));
		// Then POSTs
		assertThat(entry.get(2).getRequest().getMethod(), is(equalTo(POST)));
		assertThat(entry.get(3).getRequest().getMethod(), is(equalTo(POST)));
		assertThat(entry.get(4).getRequest().getMethod(), is(equalTo(POST)));
		// Then PUTs
		assertThat(entry.get(5).getRequest().getMethod(), is(equalTo(PUT)));
		// Then GETs
		assertThat(entry.get(6).getRequest().getMethod(), is(equalTo(GET)));
	}

	@Test
	public void testTransactionSorterReturnsDeletesInCorrectProcessingOrder() {
		Bundle b = new Bundle();
		Bundle.BundleEntryComponent bundleEntryComponent = b.addEntry();
		final Observation obs1 = new Observation();
		obs1.setStatus(Observation.ObservationStatus.FINAL);
		obs1.setSubject(new Reference("Patient/P1"));
		obs1.setValue(new Quantity(4));
		obs1.setId("Observation/O1");
		bundleEntryComponent.setResource(obs1);
		bundleEntryComponent.getRequest().setMethod(DELETE).setUrl("Observation");

		bundleEntryComponent = b.addEntry();
		final Observation obs2 = new Observation();
		obs2.setStatus(Observation.ObservationStatus.FINAL);
		obs2.setValue(new Quantity(4));
		obs2.setId("Observation/O2");
		bundleEntryComponent.setResource(obs2);
		bundleEntryComponent.getRequest().setMethod(DELETE).setUrl("Observation");

		Bundle.BundleEntryComponent patientComponent = b.addEntry();
		Patient pat1 = new Patient();
		pat1.setId("Patient/P1");
		pat1.setManagingOrganization(new Reference("Organization/Org1"));
		patientComponent.setResource(pat1);
		patientComponent.getRequest().setMethod(DELETE).setUrl("Patient");

		Bundle.BundleEntryComponent organizationComponent = b.addEntry();
		Organization org1 = new Organization();
		org1.setId("Organization/Org1");
		organizationComponent.setResource(org1);
		organizationComponent.getRequest().setMethod(DELETE).setUrl("Organization");

		BundleUtil.sortEntriesIntoProcessingOrder(myFhirCtx, b);

		assertThat(b.getEntry(), hasSize(4));

		int observationIndex = getIndexOfEntryWithId("Observation/O1", b);
		int patientIndex = getIndexOfEntryWithId("Patient/P1", b);
		int organizationIndex = getIndexOfEntryWithId("Organization/Org1", b);

		assertTrue(patientIndex < organizationIndex);
		assertTrue(observationIndex < patientIndex);
	}

	private int getIndexOfEntryWithId(String theResourceId, Bundle theBundle) {
		List<Bundle.BundleEntryComponent> entries = theBundle.getEntry();
		for (int i = 0; i < entries.size(); i++) {
			String id = entries.get(i).getResource().getIdElement().toUnqualifiedVersionless().toString();
			if (id.equals(theResourceId)) {
				return i;
			}
		}
		fail("Didn't find resource with ID " + theResourceId);
		return -1;
	}

	@Test
	public void testHookShouldContainParamsForAllCreateUpdateDeleteInvocations() throws InterruptedException {

		String urnReference = "urn:uuid:3bc44de3-069d-442d-829b-f3ef68cae371";

		final Observation obsToDelete = new Observation();
		obsToDelete.setStatus(Observation.ObservationStatus.FINAL);
		DaoMethodOutcome daoMethodOutcome = myObservationDao.create(obsToDelete);

		Patient pat1 = new Patient();

		final Observation obs1 = new Observation();
		obs1.setStatus(Observation.ObservationStatus.FINAL);
		obs1.setSubject(new Reference(urnReference));

		Bundle b = new Bundle();
		Bundle.BundleEntryComponent bundleEntryComponent = b.addEntry();

		bundleEntryComponent.setResource(obs1);
		bundleEntryComponent.getRequest().setMethod(POST).setUrl("Observation");

		Bundle.BundleEntryComponent patientComponent = b.addEntry();
		patientComponent.setFullUrl(urnReference);
		patientComponent.setResource(pat1);
		patientComponent.getRequest().setMethod(POST).setUrl("Patient");


		//Delete an observation
		b.addEntry().getRequest().setMethod(DELETE).setUrl(daoMethodOutcome.getId().toUnqualifiedVersionless().getValue());


		myPointcutLatch.setExpectedCount(4);
		mySystemDao.transaction(mySrd, b);
		List<HookParams> hookParams = myPointcutLatch.awaitExpected();

		DeferredInterceptorBroadcasts broadcastsParam = hookParams.get(3).get(DeferredInterceptorBroadcasts.class);
		ListMultimap<Pointcut, HookParams> deferredInterceptorBroadcasts = broadcastsParam.getDeferredInterceptorBroadcasts();
		assertThat(deferredInterceptorBroadcasts.entries(), hasSize(3));

		List<HookParams> createPointcutInvocations = deferredInterceptorBroadcasts.get(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED);
		assertThat(createPointcutInvocations, hasSize(2));

		IBaseResource firstCreatedResource = createPointcutInvocations.get(0).get(IBaseResource.class);
		boolean wasDeferred = createPointcutInvocations.get(0).get(Boolean.class);
		assertTrue(firstCreatedResource instanceof Observation);
		assertTrue(wasDeferred);

		IBaseResource secondCreatedResource = createPointcutInvocations.get(1).get(IBaseResource.class);
		wasDeferred = createPointcutInvocations.get(1).get(Boolean.class);
		assertTrue(secondCreatedResource instanceof Patient);
		assertTrue(wasDeferred);

		assertThat(deferredInterceptorBroadcasts.get(Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED), hasSize(1));
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
		b.addEntry().getRequest().setMethod(DELETE).setUrl(rptId.getValue());
		b.addEntry().getRequest().setMethod(DELETE).setUrl(obs2id.getValue());

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
		b.addEntry().getRequest().setMethod(DELETE).setUrl("Observation?_has:DiagnosticReport:result:identifier=foo|IDENTIFIER");
		b.addEntry().setResource(rpt).getRequest().setMethod(PUT).setUrl("DiagnosticReport?identifier=foo|IDENTIFIER");
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
		b.addEntry().getRequest().setMethod(DELETE).setUrl(obs1id.getValue());
		b.addEntry().setResource(rpt).getRequest().setMethod(PUT).setUrl(rptId.getValue());
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
		b.addEntry().getRequest().setMethod(DELETE).setUrl("Observation?_has:DiagnosticReport:result:identifier=foo|IDENTIFIER");
		b.addEntry().setResource(rpt).getRequest().setMethod(PUT).setUrl("DiagnosticReport?identifier=foo|IDENTIFIER");
		try {
			mySystemDao.transaction(mySrd, b);
			fail();
		} catch (ResourceVersionConflictException e ) {
			assertThat(e.getMessage(), matchesPattern("Unable to delete Observation/[0-9]+ because at least one resource has a reference to this resource. First reference found was resource DiagnosticReport/[0-9]+ in path DiagnosticReport.result"));
		}

		myObservationDao.read(obs1id);
		myObservationDao.read(obs2id);
		myDiagnosticReportDao.read(rptId);
	}
}
