package ca.uhn.fhir.jpa.dao.dstu3;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.model.Appointment;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryRequestComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryResponseComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.EpisodeOfCare;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.provider.SystemProviderDstu2Test;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.util.TestUtil;

public class FhirSystemDaoDstu3Test extends BaseJpaDstu3SystemTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirSystemDaoDstu3Test.class);

	@After
	public void after() {
		myDaoConfig.setAllowInlineMatchUrlReferences(false);
		myDaoConfig.setAllowMultipleDelete(new DaoConfig().isAllowMultipleDelete());
	}

	@Before
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
	}

	@SuppressWarnings("unchecked")
	private <T extends org.hl7.fhir.dstu3.model.Resource> T find(Bundle theBundle, Class<T> theType, int theIndex) {
		int count = 0;
		for (BundleEntryComponent nextEntry : theBundle.getEntry()) {
			if (nextEntry.getResource() != null && theType.isAssignableFrom(nextEntry.getResource().getClass())) {
				if (count == theIndex) {
					T t = (T) nextEntry.getResource();
					return t;
				}
				count++;
			}
		}
		fail();
		return null;
	}

	@Test
	public void testCircularCreateAndDelete() {
		Encounter enc = new Encounter();
		enc.setId(IdType.newRandomUuid());

		Condition cond = new Condition();
		cond.setId(IdType.newRandomUuid());

		EpisodeOfCare ep = new EpisodeOfCare();
		ep.setId(IdType.newRandomUuid());

		enc.getEpisodeOfCareFirstRep().setReference(ep.getId());
		cond.getContext().setReference(enc.getId());
		ep.getDiagnosisFirstRep().getCondition().setReference(cond.getId());

		Bundle inputBundle = new Bundle();
		inputBundle.setType(Bundle.BundleType.TRANSACTION);
		inputBundle
				.addEntry()
				.setResource(ep)
				.setFullUrl(ep.getId())
				.getRequest().setMethod(HTTPVerb.POST);
		inputBundle
				.addEntry()
				.setResource(cond)
				.setFullUrl(cond.getId())
				.getRequest().setMethod(HTTPVerb.POST);
		inputBundle
				.addEntry()
				.setResource(enc)
				.setFullUrl(enc.getId())
				.getRequest().setMethod(HTTPVerb.POST);

		Bundle resp = mySystemDao.transaction(mySrd, inputBundle);
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		IdType epId = new IdType(resp.getEntry().get(0).getResponse().getLocation());
		IdType condId = new IdType(resp.getEntry().get(1).getResponse().getLocation());
		IdType encId = new IdType(resp.getEntry().get(2).getResponse().getLocation());

		// Make sure a single one can't be deleted
		try {
			myEncounterDao.delete(encId);
			fail();
		} catch (ResourceVersionConflictException e) {
			// good
		}

		/*
		 * Now delete all 3 by transaction
		 */
		inputBundle = new Bundle();
		inputBundle.setType(Bundle.BundleType.TRANSACTION);
		inputBundle
				.addEntry()
				.getRequest().setMethod(HTTPVerb.DELETE)
				.setUrl(epId.toUnqualifiedVersionless().getValue());
		inputBundle
				.addEntry()
				.getRequest().setMethod(HTTPVerb.DELETE)
				.setUrl(encId.toUnqualifiedVersionless().getValue());
		inputBundle
				.addEntry()
				.getRequest().setMethod(HTTPVerb.DELETE)
				.setUrl(condId.toUnqualifiedVersionless().getValue());

		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(inputBundle));
		resp = mySystemDao.transaction(mySrd, inputBundle);
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		// They should now be deleted
		try {
			myEncounterDao.read(encId.toUnqualifiedVersionless());
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

	}

	/**
	 * See #410
	 */
	@Test
	public void testContainedArePreservedForBug410() throws IOException {
		String input = IOUtils.toString(getClass().getResourceAsStream("/bug-410-bundle.xml"), StandardCharsets.UTF_8);
		Bundle bundle = myFhirCtx.newXmlParser().parseResource(Bundle.class, input);

		Bundle output = mySystemDao.transaction(mySrd, bundle);
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));

		IdType id = new IdType(output.getEntry().get(1).getResponse().getLocation());
		MedicationRequest mo = myMedicationRequestDao.read(id);
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(mo));
	}

	@Test
	public void testDeleteWithHas() {
		Observation obs1 = new Observation();
		obs1.setStatus(ObservationStatus.FINAL);
		IIdType obs1id = myObservationDao.create(obs1).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.setStatus(ObservationStatus.FINAL);
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
		b.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl("Observation?_has:DiagnosticReport:result:identifier=foo|IDENTIFIER");
		b.addEntry().setResource(rpt).getRequest().setMethod(HTTPVerb.PUT).setUrl("DiagnosticReport?identifier=foo|IDENTIFIER");
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
	public void testMultipleUpdatesWithNoChangesDoesNotResultInAnUpdateForTransaction() {
		Bundle bundle;
		
		// First time
		Patient p = new Patient();
		p.setActive(true);
		bundle = new Bundle();
		bundle.setType(BundleType.TRANSACTION);
		bundle
			.addEntry()
			.setResource(p)
			.setFullUrl("Patient/A")
			.getRequest()
				.setMethod(HTTPVerb.PUT)
				.setUrl("Patient/A");
		Bundle resp = mySystemDao.transaction(mySrd, bundle);
		assertThat(resp.getEntry().get(0).getResponse().getLocation(), endsWith("Patient/A/_history/1"));
		
		// Second time should not result in an update
		p = new Patient();
		p.setActive(true);
		bundle = new Bundle();
		bundle.setType(BundleType.TRANSACTION);
		bundle
			.addEntry()
			.setResource(p)
			.setFullUrl("Patient/A")
			.getRequest()
				.setMethod(HTTPVerb.PUT)
				.setUrl("Patient/A");
		resp = mySystemDao.transaction(mySrd, bundle);
		assertThat(resp.getEntry().get(0).getResponse().getLocation(), endsWith("Patient/A/_history/1"));

		// And third time should not result in an update
		p = new Patient();
		p.setActive(true);
		bundle = new Bundle();
		bundle.setType(BundleType.TRANSACTION);
		bundle
			.addEntry()
			.setResource(p)
			.setFullUrl("Patient/A")
			.getRequest()
				.setMethod(HTTPVerb.PUT)
				.setUrl("Patient/A");
		resp = mySystemDao.transaction(mySrd, bundle);
		assertThat(resp.getEntry().get(0).getResponse().getLocation(), endsWith("Patient/A/_history/1"));

		myPatientDao.read(new IdType("Patient/A"));
		myPatientDao.read(new IdType("Patient/A/_history/1"));
		try {
			myPatientDao.read(new IdType("Patient/A/_history/2"));
			fail();
		} catch (ResourceNotFoundException e) {
			//good
		}
		try {
			myPatientDao.read(new IdType("Patient/A/_history/3"));
			fail();
		} catch (ResourceNotFoundException e) {
			//good
		}
	}

	@Test
	public void testReindexing() {
		Patient p = new Patient();
		p.addName().setFamily("family");
		final IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		ValueSet vs = new ValueSet();
		vs.setUrl("http://foo");
		myValueSetDao.create(vs, mySrd);

		ResourceTable entity = new TransactionTemplate(myTxManager).execute(new TransactionCallback<ResourceTable>() {
			@Override
			public ResourceTable doInTransaction(TransactionStatus theStatus) {
				return myEntityManager.find(ResourceTable.class, id.getIdPartAsLong());
			}
		});
		assertEquals(Long.valueOf(1), entity.getIndexStatus());

		mySystemDao.markAllResourcesForReindexing();

		entity = new TransactionTemplate(myTxManager).execute(new TransactionCallback<ResourceTable>() {
			@Override
			public ResourceTable doInTransaction(TransactionStatus theStatus) {
				return myEntityManager.find(ResourceTable.class, id.getIdPartAsLong());
			}
		});
		assertEquals(null, entity.getIndexStatus());

		mySystemDao.performReindexingPass(null);

		entity = new TransactionTemplate(myTxManager).execute(new TransactionCallback<ResourceTable>() {
			@Override
			public ResourceTable doInTransaction(TransactionStatus theStatus) {
				return myEntityManager.find(ResourceTable.class, id.getIdPartAsLong());
			}
		});
		assertEquals(Long.valueOf(1), entity.getIndexStatus());

		// Just make sure this doesn't cause a choke
		mySystemDao.performReindexingPass(100000);

		// Try making the resource unparseable

		TransactionTemplate template = new TransactionTemplate(myTxManager);
		template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		template.execute(new TransactionCallback<ResourceTable>() {
			@Override
			public ResourceTable doInTransaction(TransactionStatus theStatus) {
				ResourceTable table = myEntityManager.find(ResourceTable.class, id.getIdPartAsLong());
				table.setEncoding(ResourceEncodingEnum.JSON);
				table.setIndexStatus(null);
				try {
					table.setResource("{\"resourceType\":\"FOO\"}".getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					throw new Error(e);
				}
				myEntityManager.merge(table);
				return null;
			}
		});

		mySystemDao.performReindexingPass(null);

		entity = new TransactionTemplate(myTxManager).execute(new TransactionCallback<ResourceTable>() {
			@Override
			public ResourceTable doInTransaction(TransactionStatus theStatus) {
				return myEntityManager.find(ResourceTable.class, id.getIdPartAsLong());
			}
		});
		assertEquals(Long.valueOf(2), entity.getIndexStatus());

	}

	@Test
	public void testSystemMetaOperation() {

		Meta meta = mySystemDao.metaGetOperation(mySrd);
		List<Coding> published = meta.getTag();
		assertEquals(0, published.size());

		String methodName = "testSystemMetaOperation";
		IIdType id1;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue(methodName);
			patient.addName().setFamily("Tester").addGiven("Joe");

			patient.getMeta().addTag(null, "Dog", "Puppies");
			patient.getMeta().getSecurity().add(new Coding().setSystem("seclabel:sys:1").setCode("seclabel:code:1").setDisplay("seclabel:dis:1"));
			patient.getMeta().getProfile().add(new IdType("http://profile/1"));

			id1 = myPatientDao.create(patient, mySrd).getId();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue(methodName);
			patient.addName().setFamily("Tester").addGiven("Joe");

			patient.getMeta().addTag("http://foo", "Cat", "Kittens");
			patient.getMeta().getSecurity().add(new Coding().setSystem("seclabel:sys:2").setCode("seclabel:code:2").setDisplay("seclabel:dis:2"));
			patient.getMeta().getProfile().add(new IdType("http://profile/2"));

			myPatientDao.create(patient, mySrd);
		}

		meta = mySystemDao.metaGetOperation(mySrd);
		published = meta.getTag();
		assertEquals(2, published.size());
		assertEquals(null, published.get(0).getSystem());
		assertEquals("Dog", published.get(0).getCode());
		assertEquals("Puppies", published.get(0).getDisplay());
		assertEquals("http://foo", published.get(1).getSystem());
		assertEquals("Cat", published.get(1).getCode());
		assertEquals("Kittens", published.get(1).getDisplay());
		List<Coding> secLabels = meta.getSecurity();
		assertEquals(2, secLabels.size());
		assertEquals("seclabel:sys:1", secLabels.get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:1", secLabels.get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:1", secLabels.get(0).getDisplayElement().getValue());
		assertEquals("seclabel:sys:2", secLabels.get(1).getSystemElement().getValue());
		assertEquals("seclabel:code:2", secLabels.get(1).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", secLabels.get(1).getDisplayElement().getValue());
		List<UriType> profiles = meta.getProfile();
		assertEquals(2, profiles.size());
		assertEquals("http://profile/1", profiles.get(0).getValue());
		assertEquals("http://profile/2", profiles.get(1).getValue());

		myPatientDao.removeTag(id1, TagTypeEnum.TAG, null, "Dog", mySrd);
		myPatientDao.removeTag(id1, TagTypeEnum.SECURITY_LABEL, "seclabel:sys:1", "seclabel:code:1", mySrd);
		myPatientDao.removeTag(id1, TagTypeEnum.PROFILE, BaseHapiFhirDao.NS_JPA_PROFILE, "http://profile/1", mySrd);

		meta = mySystemDao.metaGetOperation(mySrd);
		published = meta.getTag();
		assertEquals(1, published.size());
		assertEquals("http://foo", published.get(0).getSystem());
		assertEquals("Cat", published.get(0).getCode());
		assertEquals("Kittens", published.get(0).getDisplay());
		secLabels = meta.getSecurity();
		assertEquals(1, secLabels.size());
		assertEquals("seclabel:sys:2", secLabels.get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:2", secLabels.get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", secLabels.get(0).getDisplayElement().getValue());
		profiles = meta.getProfile();
		assertEquals(1, profiles.size());
		assertEquals("http://profile/2", profiles.get(0).getValue());

	}

	@Test
	public void testTransaction1() throws IOException {
		String inputBundleString = loadClasspath("/david-bundle-error.json");
		Bundle bundle = myFhirCtx.newJsonParser().parseResource(Bundle.class, inputBundleString);
		Bundle resp = mySystemDao.transaction(mySrd, bundle);

		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		assertEquals("201 Created", resp.getEntry().get(0).getResponse().getStatus());
	}

	@Test
	public void testTransactionBatchWithFailingRead() {
		String methodName = "testTransactionBatchWithFailingRead";
		Bundle request = new Bundle();
		request.setType(BundleType.BATCH);

		Patient p = new Patient();
		p.addName().setFamily(methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST);

		request.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl("Patient/THIS_ID_DOESNT_EXIST");

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(3, resp.getEntry().size());
		assertEquals(BundleType.BATCHRESPONSE, resp.getTypeElement().getValue());

		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));
		BundleEntryResponseComponent respEntry;

		// Bundle.entry[0] is operation outcome
		assertEquals(OperationOutcome.class, resp.getEntry().get(0).getResource().getClass());
		assertEquals(IssueSeverity.INFORMATION, ((OperationOutcome) resp.getEntry().get(0).getResource()).getIssue().get(0).getSeverityElement().getValue());
		assertThat(((OperationOutcome) resp.getEntry().get(0).getResource()).getIssue().get(0).getDiagnostics(), startsWith("Batch completed in "));

		// Bundle.entry[1] is create response
		assertEquals("201 Created", resp.getEntry().get(1).getResponse().getStatus());
		assertThat(resp.getEntry().get(1).getResponse().getLocation(), startsWith("Patient/"));

		// Bundle.entry[2] is failed read response
		assertEquals(OperationOutcome.class, resp.getEntry().get(2).getResource().getClass());
		assertEquals(IssueSeverity.ERROR, ((OperationOutcome) resp.getEntry().get(2).getResource()).getIssue().get(0).getSeverityElement().getValue());
		assertEquals("Resource Patient/THIS_ID_DOESNT_EXIST is not known", ((OperationOutcome) resp.getEntry().get(2).getResource()).getIssue().get(0).getDiagnostics());
		assertEquals("404 Not Found", resp.getEntry().get(2).getResponse().getStatus());

		// Check POST
		respEntry = resp.getEntry().get(1).getResponse();
		assertEquals("201 Created", respEntry.getStatus());
		IdType createdId = new IdType(respEntry.getLocation());
		assertEquals("Patient", createdId.getResourceType());
		myPatientDao.read(createdId, mySrd); // shouldn't fail

		// Check GET
		respEntry = resp.getEntry().get(2).getResponse();
		assertThat(respEntry.getStatus(), startsWith("404"));

	}

	@Test
	public void testTransactionCreateInlineMatchUrlWithNoMatches() {
		String methodName = "testTransactionCreateInlineMatchUrlWithNoMatches";
		Bundle request = new Bundle();

		myDaoConfig.setAllowInlineMatchUrlReferences(true);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient?identifier=urn%3Asystem%7C" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("Invalid match URL \"Patient?identifier=urn%3Asystem%7CtestTransactionCreateInlineMatchUrlWithNoMatches\" - No resources match this search", e.getMessage());
		}
	}

	@Test
	public void testTransactionCreateInlineMatchUrlWithOneMatch() {
		String methodName = "testTransactionCreateInlineMatchUrlWithOneMatch";
		Bundle request = new Bundle();

		myDaoConfig.setAllowInlineMatchUrlReferences(true);

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType id = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient?identifier=urn%3Asystem%7C" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(1, resp.getEntry().size());

		BundleEntryComponent respEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());
		assertThat(respEntry.getResponse().getLocation(), containsString("Observation/"));
		assertThat(respEntry.getResponse().getLocation(), endsWith("/_history/1"));
		assertEquals("1", respEntry.getResponse().getEtag());

		o = myObservationDao.read(new IdType(respEntry.getResponse().getLocationElement()), mySrd);
		assertEquals(id.toVersionless().getValue(), o.getSubject().getReference());
		assertEquals("1", o.getIdElement().getVersionIdPart());

	}

	@Test
	public void testTransactionCreateInlineMatchUrlWithOneMatch2() {
		String methodName = "testTransactionCreateInlineMatchUrlWithOneMatch2";
		Bundle request = new Bundle();

		myDaoConfig.setAllowInlineMatchUrlReferences(true);

		Patient p = new Patient();
		p.addName().addGiven("Heute");
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType id = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/?given=heute");
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(1, resp.getEntry().size());

		BundleEntryComponent respEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());
		assertThat(respEntry.getResponse().getLocation(), containsString("Observation/"));
		assertThat(respEntry.getResponse().getLocation(), endsWith("/_history/1"));
		assertEquals("1", respEntry.getResponse().getEtag());

		o = myObservationDao.read(new IdType(respEntry.getResponse().getLocationElement()), mySrd);
		assertEquals(id.toVersionless().getValue(), o.getSubject().getReference());
		assertEquals("1", o.getIdElement().getVersionIdPart());

	}

	@Test
	public void testTransactionCreateInlineMatchUrlWithOneMatchLastUpdated() {
		Bundle request = new Bundle();
		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST).setIfNoneExist("Observation?_lastUpdated=gt2011-01-01");
		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(1, resp.getEntry().size());

		BundleEntryComponent respEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());
		assertThat(respEntry.getResponse().getLocation(), containsString("Observation/"));
		assertThat(respEntry.getResponse().getLocation(), endsWith("/_history/1"));
		assertEquals("1", respEntry.getResponse().getEtag());

		/*
		 * Second time should not update
		 */

		request = new Bundle();
		o = new Observation();
		o.getCode().setText("Some Observation");
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST).setIfNoneExist("Observation?_lastUpdated=gt2011-01-01");
		resp = mySystemDao.transaction(mySrd, request);
		assertEquals(1, resp.getEntry().size());

		respEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_200_OK + " OK", respEntry.getResponse().getStatus());
		assertThat(respEntry.getResponse().getLocation(), containsString("Observation/"));
		assertThat(respEntry.getResponse().getLocation(), endsWith("/_history/1"));
		assertEquals("1", respEntry.getResponse().getEtag());

	}

	@Test
	public void testTransactionCreateInlineMatchUrlWithTwoMatches() {
		String methodName = "testTransactionCreateInlineMatchUrlWithTwoMatches";
		Bundle request = new Bundle();

		myDaoConfig.setAllowInlineMatchUrlReferences(true);

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		myPatientDao.create(p, mySrd).getId();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		myPatientDao.create(p, mySrd).getId();

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient?identifier=urn%3Asystem%7C" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals("Invalid match URL \"Patient?identifier=urn%3Asystem%7CtestTransactionCreateInlineMatchUrlWithTwoMatches\" - Multiple resources match this search", e.getMessage());
		}
	}

	@Test
	public void testTransactionCreateMatchUrlWithOneMatch() {
		String methodName = "testTransactionCreateMatchUrlWithOneMatch";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType id = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.setId("Patient/" + methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(2, resp.getEntry().size());

		BundleEntryComponent respEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_200_OK + " OK", respEntry.getResponse().getStatus());
		assertThat(respEntry.getResponse().getLocation(), endsWith("Patient/" + id.getIdPart() + "/_history/1"));
		assertEquals("1", respEntry.getResponse().getEtag());

		respEntry = resp.getEntry().get(1);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());
		assertThat(respEntry.getResponse().getLocation(), containsString("Observation/"));
		assertThat(respEntry.getResponse().getLocation(), endsWith("/_history/1"));
		assertEquals("1", respEntry.getResponse().getEtag());

		o = myObservationDao.read(new IdType(respEntry.getResponse().getLocationElement()), mySrd);
		assertEquals(id.toVersionless().getValue(), o.getSubject().getReference());
		assertEquals("1", o.getIdElement().getVersionIdPart());

	}

	@Test
	public void testTransactionCreateMatchUrlWithTwoMatch() {
		String methodName = "testTransactionCreateMatchUrlWithTwoMatch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		Bundle request = new Bundle();
		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.setId("Patient/" + methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (PreconditionFailedException e) {
			assertThat(e.getMessage(), containsString("with match URL \"Patient"));
		}
	}

	@Test
	public void testTransactionCreateMatchUrlWithZeroMatch() {
		String methodName = "testTransactionCreateMatchUrlWithZeroMatch";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.setId("Patient/" + methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(BundleType.TRANSACTIONRESPONSE, resp.getTypeElement().getValue());
		assertEquals(2, resp.getEntry().size());

		BundleEntryComponent respEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());
		String patientId = respEntry.getResponse().getLocation();
		assertThat(patientId, not(endsWith("Patient/" + methodName + "/_history/1")));
		assertThat(patientId, (endsWith("/_history/1")));
		assertThat(patientId, (containsString("Patient/")));
		assertEquals("1", respEntry.getResponse().getEtag());

		respEntry = resp.getEntry().get(1);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());
		assertThat(respEntry.getResponse().getLocation(), containsString("Observation/"));
		assertThat(respEntry.getResponse().getLocation(), endsWith("/_history/1"));
		assertEquals("1", respEntry.getResponse().getEtag());

		o = myObservationDao.read(new IdType(respEntry.getResponse().getLocationElement()), mySrd);
		assertEquals(new IdType(patientId).toUnqualifiedVersionless().getValue(), o.getSubject().getReference());
	}

	@Test
	public void testTransactionCreateNoMatchUrl() {
		String methodName = "testTransactionCreateNoMatchUrl";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(1, resp.getEntry().size());

		BundleEntryComponent respEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());
		String patientId = respEntry.getResponse().getLocation();
		assertThat(patientId, not(containsString("test")));

		/*
		 * Interceptor should have been called once for the transaction, and once for the embedded operation
		 */
		ArgumentCaptor<ActionRequestDetails> detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		verify(myInterceptor).incomingRequestPreHandled(eq(RestOperationTypeEnum.TRANSACTION), detailsCapt.capture());
		ActionRequestDetails details = detailsCapt.getValue();
		assertEquals("Bundle", details.getResourceType());
		assertEquals(Bundle.class, details.getResource().getClass());

		detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		verify(myInterceptor).incomingRequestPreHandled(eq(RestOperationTypeEnum.CREATE), detailsCapt.capture());
		details = detailsCapt.getValue();
		assertNotNull(details.getId());
		assertEquals("Patient", details.getResourceType());
		assertEquals(Patient.class, details.getResource().getClass());

	}

	@Test
	public void testTransactionCreateWithDuplicateMatchUrl01() {
		String methodName = "testTransactionCreateWithDuplicateMatchUrl01";
		Bundle request = new Bundle();

		Patient p;
		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(e.getMessage(),
					"Unable to process Transaction - Request would cause multiple resources to match URL: \"Patient?identifier=urn%3Asystem%7CtestTransactionCreateWithDuplicateMatchUrl01\". Does transaction request contain duplicates?");
		}
	}

	@Test
	public void testTransactionCreateWithDuplicateMatchUrl02() {
		String methodName = "testTransactionCreateWithDuplicateMatchUrl02";
		Bundle request = new Bundle();

		Patient p;
		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(e.getMessage(),
					"Unable to process Transaction - Request would cause multiple resources to match URL: \"Patient?identifier=urn%3Asystem%7CtestTransactionCreateWithDuplicateMatchUrl02\". Does transaction request contain duplicates?");
		}
	}

	@Test
	public void testTransactionCreateWithInvalidMatchUrl() {
		String methodName = "testTransactionCreateWithInvalidMatchUrl";
		Bundle request = new Bundle();

		Patient p;
		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		BundleEntryRequestComponent entry = request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST);

		try {
			entry.setIfNoneExist("Patient?identifier   identifier" + methodName);
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Failed to parse match URL[Patient?identifier   identifiertestTransactionCreateWithInvalidMatchUrl] - URL is invalid (must not contain spaces)", e.getMessage());
		}

		try {
			entry.setIfNoneExist("Patient?identifier=");
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Invalid match URL[Patient?identifier=] - URL has no search parameters", e.getMessage());
		}

		try {
			entry.setIfNoneExist("Patient?foo=bar");
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Failed to parse match URL[Patient?foo=bar] - Resource type Patient does not have a parameter with name: foo", e.getMessage());
		}
	}

	@Test
	public void testTransactionCreateWithInvalidReferenceNumeric() {
		String methodName = "testTransactionCreateWithInvalidReferenceNumeric";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.getManagingOrganization().setReference("Organization/9999999999999999");
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Resource Organization/9999999999999999 not found, specified in path: Patient.managingOrganization"));
		}
	}

	@Test
	public void testTransactionCreateWithInvalidReferenceTextual() {
		String methodName = "testTransactionCreateWithInvalidReferenceTextual";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.getManagingOrganization().setReference("Organization/" + methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Resource Organization/" + methodName + " not found, specified in path: Patient.managingOrganization"));
		}
	}

	@Test
	public void testTransactionCreateWithPutUsingAbsoluteUrl() {
		String methodName = "testTransactionCreateWithPutUsingAbsoluteUrl";
		Bundle request = new Bundle();
		request.setType(BundleType.TRANSACTION);

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.PUT).setUrl("http://localhost/server/base/Patient/" + methodName);

		mySystemDao.transaction(mySrd, request);

		myPatientDao.read(new IdType("Patient/" + methodName), mySrd);
	}

	@Test
	public void testTransactionCreateWithPutUsingUrl() {
		String methodName = "testTransactionCreateWithPutUsingUrl";
		Bundle request = new Bundle();
		request.setType(BundleType.TRANSACTION);

		Observation o = new Observation();
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.PUT).setUrl("Observation/a" + methodName);

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.PUT).setUrl("Patient/" + methodName);

		mySystemDao.transaction(mySrd, request);

		myObservationDao.read(new IdType("Observation/a" + methodName), mySrd);
		myPatientDao.read(new IdType("Patient/" + methodName), mySrd);
	}

	@Test
	public void testTransactionCreateWithPutUsingUrl2() throws Exception {
		String req = IOUtils.toString(FhirSystemDaoDstu3Test.class.getResourceAsStream("/bundle-dstu3.xml"), StandardCharsets.UTF_8);
		Bundle request = myFhirCtx.newXmlParser().parseResource(Bundle.class, req);
		mySystemDao.transaction(mySrd, request);
	}

	@Test
	public void testTransactionDeleteByResourceId() {
		String methodName = "testTransactionDeleteByResourceId";

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id1 = myPatientDao.create(p1, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id1);

		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("urn:system").setValue(methodName);
		p2.setId("Patient/" + methodName);
		IIdType id2 = myPatientDao.update(p2, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id2);

		Bundle request = new Bundle();

		request.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl("Patient/" + id1.getIdPart());
		request.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl("Patient/" + id2.getIdPart());

		myPatientDao.read(id1.toVersionless(), mySrd);
		myPatientDao.read(id2.toVersionless(), mySrd);

		Bundle resp = mySystemDao.transaction(mySrd, request);

		assertEquals(2, resp.getEntry().size());
		assertEquals("204 No Content", resp.getEntry().get(0).getResponse().getStatus());
		assertEquals("204 No Content", resp.getEntry().get(1).getResponse().getStatus());

		try {
			myPatientDao.read(id1.toVersionless(), mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		try {
			myPatientDao.read(id2.toVersionless(), mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

	}

	/**
	 * See #253 Test that the order of deletes is version independent
	 */
	@Test
	public void testTransactionDeleteIsOrderIndependantTargetFirst() {
		String methodName = "testTransactionDeleteIsOrderIndependantTargetFirst";

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType pid = myPatientDao.create(p1, mySrd).getId().toUnqualifiedVersionless();
		ourLog.info("Created patient, got it: {}", pid);

		Observation o1 = new Observation();
		o1.getSubject().setReferenceElement(pid);
		IIdType oid1 = myObservationDao.create(o1, mySrd).getId().toUnqualifiedVersionless();

		Observation o2 = new Observation();
		o2.addIdentifier().setValue(methodName);
		o2.getSubject().setReferenceElement(pid);
		IIdType oid2 = myObservationDao.create(o2, mySrd).getId().toUnqualifiedVersionless();

		myPatientDao.read(pid, mySrd);
		myObservationDao.read(oid1, mySrd);

		// The target is Patient, so try with it first in the bundle
		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl(pid.getValue());
		request.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl(oid1.getValue());
		request.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl("Observation?identifier=" + methodName);
		Bundle resp = mySystemDao.transaction(mySrd, request);

		assertEquals(3, resp.getEntry().size());
		assertEquals("204 No Content", resp.getEntry().get(0).getResponse().getStatus());
		assertEquals("204 No Content", resp.getEntry().get(1).getResponse().getStatus());
		assertEquals("204 No Content", resp.getEntry().get(2).getResponse().getStatus());

		try {
			myPatientDao.read(pid, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		try {
			myObservationDao.read(oid1, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		try {
			myObservationDao.read(oid2, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

	}

	/**
	 * See #253 Test that the order of deletes is version independent
	 */
	@Test
	public void testTransactionDeleteIsOrderIndependantTargetLast() {
		String methodName = "testTransactionDeleteIsOrderIndependantTargetFirst";

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType pid = myPatientDao.create(p1, mySrd).getId().toUnqualifiedVersionless();
		ourLog.info("Created patient, got it: {}", pid);

		Observation o1 = new Observation();
		o1.getSubject().setReferenceElement(pid);
		IIdType oid1 = myObservationDao.create(o1, mySrd).getId().toUnqualifiedVersionless();

		Observation o2 = new Observation();
		o2.addIdentifier().setValue(methodName);
		o2.getSubject().setReferenceElement(pid);
		IIdType oid2 = myObservationDao.create(o2, mySrd).getId().toUnqualifiedVersionless();

		myPatientDao.read(pid, mySrd);
		myObservationDao.read(oid1, mySrd);

		// The target is Patient, so try with it last in the bundle
		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl(oid1.getValue());
		request.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl("Observation?identifier=" + methodName);
		request.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl(pid.getValue());
		Bundle resp = mySystemDao.transaction(mySrd, request);

		assertEquals(3, resp.getEntry().size());
		assertEquals("204 No Content", resp.getEntry().get(0).getResponse().getStatus());
		assertEquals("204 No Content", resp.getEntry().get(1).getResponse().getStatus());
		assertEquals("204 No Content", resp.getEntry().get(2).getResponse().getStatus());

		try {
			myPatientDao.read(pid, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		try {
			myObservationDao.read(oid1, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		try {
			myObservationDao.read(oid2, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

	}

	@Test
	public void testTransactionDeleteMatchUrlWithOneMatch() {
		String methodName = "testTransactionDeleteMatchUrlWithOneMatch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(1, resp.getEntry().size());

		BundleEntryComponent nextEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_204_NO_CONTENT + " No Content", nextEntry.getResponse().getStatus());

		try {
			myPatientDao.read(id.toVersionless(), mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// ok
		}

		try {
			myPatientDao.read(new IdType("Patient/" + methodName), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			// ok
		}

		IBundleProvider history = myPatientDao.history(id, null, null, mySrd);
		assertEquals(2, history.size().intValue());

		assertNotNull(ResourceMetadataKeyEnum.DELETED_AT.get((IAnyResource) history.getResources(0, 1).get(0)));
		assertNotNull(ResourceMetadataKeyEnum.DELETED_AT.get((IAnyResource) history.getResources(0, 1).get(0)).getValue());
		assertNull(ResourceMetadataKeyEnum.DELETED_AT.get((IAnyResource) history.getResources(1, 2).get(0)));

	}

	@Test
	public void testTransactionDeleteMatchUrlWithTwoMatch() {
		myDaoConfig.setAllowMultipleDelete(false);

		String methodName = "testTransactionDeleteMatchUrlWithTwoMatch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.setId("Patient/" + methodName);

		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (PreconditionFailedException e) {
			assertThat(e.getMessage(), containsString("resource with match URL \"Patient?"));
		}
	}

	@Test
	public void testTransactionDeleteMatchUrlWithZeroMatch() {
		String methodName = "testTransactionDeleteMatchUrlWithZeroMatch";

		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		// try {
		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(1, resp.getEntry().size());
		assertEquals("204 No Content", resp.getEntry().get(0).getResponse().getStatus());

		// fail();
		// } catch (ResourceNotFoundException e) {
		// assertThat(e.getMessage(), containsString("resource matching URL \"Patient?"));
		// }
	}

	@Test
	public void testTransactionDeleteNoMatchUrl() {
		String methodName = "testTransactionDeleteNoMatchUrl";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType id = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		Bundle res = mySystemDao.transaction(mySrd, request);
		assertEquals(1, res.getEntry().size());

		assertEquals(Constants.STATUS_HTTP_204_NO_CONTENT + " No Content", res.getEntry().get(0).getResponse().getStatus());

		try {
			myPatientDao.read(id.toVersionless(), mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// ok
		}
	}

	@Test
	public void testTransactionDoesNotAllowDanglingTemporaryIds() throws Exception {
		String input = IOUtils.toString(getClass().getResourceAsStream("/cdr-bundle.json"), StandardCharsets.UTF_8);
		Bundle bundle = myFhirCtx.newJsonParser().parseResource(Bundle.class, input);

		BundleEntryComponent entry = bundle.addEntry();
		Patient p = new Patient();
		p.getManagingOrganization().setReference("urn:uuid:30ce60cf-f7cb-4196-961f-cadafa8b7ff5");
		entry.setResource(p);
		entry.getRequest().setMethod(HTTPVerb.POST);
		entry.getRequest().setUrl("Patient");

		try {
			mySystemDao.transaction(mySrd, bundle);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Unable to satisfy placeholder ID: urn:uuid:30ce60cf-f7cb-4196-961f-cadafa8b7ff5", e.getMessage());
		}
	}

	@Test
	public void testTransactionDoesNotLeavePlaceholderIds() throws Exception {
		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				String input;
				try {
					input = IOUtils.toString(getClass().getResourceAsStream("/cdr-bundle.json"), StandardCharsets.UTF_8);
				} catch (IOException e) {
					fail(e.toString());
					return;
				}
				Bundle bundle = myFhirCtx.newJsonParser().parseResource(Bundle.class, input);
				mySystemDao.transaction(mySrd, bundle);
			}
		});

		IBundleProvider history = mySystemDao.history(null, null, null);
		Bundle list = toBundle(history);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(list));

		assertEquals(6, list.getEntry().size());

		Patient p = find(list, Patient.class, 0);
		assertTrue(p.getIdElement().isIdPartValidLong());
		assertTrue(p.getGeneralPractitionerFirstRep().getReferenceElement().isIdPartValidLong());
	}

	@Test
	public void testTransactionDoubleConditionalCreateOnlyCreatesOne() {
		Bundle inputBundle = new Bundle();
		inputBundle.setType(Bundle.BundleType.TRANSACTION);

		Encounter enc1 = new Encounter();
		enc1.addIdentifier().setSystem("urn:foo").setValue("12345");
		inputBundle
				.addEntry()
				.setResource(enc1)
				.getRequest()
				.setMethod(HTTPVerb.POST)
				.setIfNoneExist("Encounter?identifier=urn:foo|12345");
		Encounter enc2 = new Encounter();
		enc2.addIdentifier().setSystem("urn:foo").setValue("12345");
		inputBundle
				.addEntry()
				.setResource(enc2)
				.getRequest()
				.setMethod(HTTPVerb.POST)
				.setIfNoneExist("Encounter?identifier=urn:foo|12345");

		try {
			mySystemDao.transaction(mySrd, inputBundle);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Unable to process Transaction - Request would cause multiple resources to match URL: \"Encounter?identifier=urn:foo|12345\". Does transaction request contain duplicates?", e.getMessage());
		}
	}

	@Test
	public void testTransactionDoubleConditionalUpdateOnlyCreatesOne() {
		Bundle inputBundle = new Bundle();
		inputBundle.setType(Bundle.BundleType.TRANSACTION);

		Encounter enc1 = new Encounter();
		enc1.addIdentifier().setSystem("urn:foo").setValue("12345");
		inputBundle
				.addEntry()
				.setResource(enc1)
				.getRequest()
				.setMethod(HTTPVerb.PUT)
				.setUrl("Encounter?identifier=urn:foo|12345");
		Encounter enc2 = new Encounter();
		enc2.addIdentifier().setSystem("urn:foo").setValue("12345");
		inputBundle
				.addEntry()
				.setResource(enc2)
				.getRequest()
				.setMethod(HTTPVerb.PUT)
				.setUrl("Encounter?identifier=urn:foo|12345");

		try {
			mySystemDao.transaction(mySrd, inputBundle);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Unable to process Transaction - Request would cause multiple resources to match URL: \"Encounter?identifier=urn:foo|12345\". Does transaction request contain duplicates?", e.getMessage());
		}
		
	}

	@Test(expected = InvalidRequestException.class)
	public void testTransactionFailsWithDuplicateIds() {
		Bundle request = new Bundle();

		Patient patient1 = new Patient();
		patient1.setId(new IdType("Patient/testTransactionFailsWithDusplicateIds"));
		patient1.addIdentifier().setSystem("urn:system").setValue("testPersistWithSimpleLinkP01");
		request.addEntry().setResource(patient1).getRequest().setMethod(HTTPVerb.POST);

		Patient patient2 = new Patient();
		patient2.setId(new IdType("Patient/testTransactionFailsWithDusplicateIds"));
		patient2.addIdentifier().setSystem("urn:system").setValue("testPersistWithSimpleLinkP02");
		request.addEntry().setResource(patient2).getRequest().setMethod(HTTPVerb.POST);

		mySystemDao.transaction(mySrd, request);
	}

	@Test
	public void testTransactionFromBundle() throws Exception {

		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/transaction_link_patient_eve.xml");
		String bundleStr = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		Bundle bundle = myFhirCtx.newXmlParser().parseResource(Bundle.class, bundleStr);

		Bundle resp = mySystemDao.transaction(mySrd, bundle);

		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));

		assertThat(resp.getEntry().get(0).getResponse().getLocation(), startsWith("Patient/a555-44-4444/_history/"));
		assertThat(resp.getEntry().get(1).getResponse().getLocation(), startsWith("Patient/temp6789/_history/"));
		assertThat(resp.getEntry().get(2).getResponse().getLocation(), startsWith("Organization/GHH/_history/"));

		Patient p = myPatientDao.read(new IdType("Patient/a555-44-4444/_history/1"), mySrd);
		assertEquals("Patient/temp6789", p.getLink().get(0).getOther().getReference());
	}

	@Test
	public void testTransactionFromBundle2() throws Exception {
		String input = IOUtils.toString(getClass().getResourceAsStream("/transaction-bundle.xml"), StandardCharsets.UTF_8);
		Bundle bundle = myFhirCtx.newXmlParser().parseResource(Bundle.class, input);
		Bundle response = mySystemDao.transaction(mySrd, bundle);

		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(response));
		assertEquals("201 Created", response.getEntry().get(0).getResponse().getStatus());
		assertThat(response.getEntry().get(0).getResponse().getLocation(), matchesPattern("Practitioner/[0-9]+/_history/1"));

		/*
		 * Now a second time
		 */

		bundle = myFhirCtx.newXmlParser().parseResource(Bundle.class, input);
		response = mySystemDao.transaction(mySrd, bundle);

		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(response));
		assertEquals("200 OK", response.getEntry().get(0).getResponse().getStatus());
		assertThat(response.getEntry().get(0).getResponse().getLocation(), matchesPattern("Practitioner/[0-9]+/_history/1"));

	}

	@Test
	public void testTransactionFromBundle6() throws Exception {
		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/simone_bundle3.xml");
		String bundle = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		Bundle output = mySystemDao.transaction(mySrd, myFhirCtx.newXmlParser().parseResource(Bundle.class, bundle));
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));
	}

	@Test
	public void testTransactionFromBundleJosh() throws Exception {

		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/josh-bundle.json");
		String bundleStr = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		Bundle bundle = myFhirCtx.newJsonParser().parseResource(Bundle.class, bundleStr);

		Bundle resp = mySystemDao.transaction(mySrd, bundle);

		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));

		assertEquals("201 Created", resp.getEntry().get(0).getResponse().getStatus());
		assertEquals("201 Created", resp.getEntry().get(1).getResponse().getStatus());
	}

	@Test
	public void testTransactionOrdering() {
		String methodName = "testTransactionOrdering";

		//@formatter:off
		/*
		 * Transaction Order, per the spec:
		 * 
		 * Process any DELETE interactions
		 * Process any POST interactions
		 * Process any PUT interactions
		 * Process any GET interactions
		 * 
		 * This test creates a transaction bundle that includes 
		 * these four operations in the reverse order and verifies
		 * that they are invoked correctly.
		 */
		//@formatter:off
		
		int pass = 0;
		IdType patientPlaceholderId = IdType.newRandomUuid();
		
		Bundle req = testTransactionOrderingCreateBundle(methodName, pass, patientPlaceholderId);
		Bundle resp = mySystemDao.transaction(mySrd, req);
		testTransactionOrderingValidateResponse(pass, resp);
		
		pass = 1;
		patientPlaceholderId = IdType.newRandomUuid();
		
		req = testTransactionOrderingCreateBundle(methodName, pass, patientPlaceholderId);
		resp = mySystemDao.transaction(mySrd, req);
		testTransactionOrderingValidateResponse(pass, resp);

	}

	private Bundle testTransactionOrderingCreateBundle(String methodName, int pass, IdType patientPlaceholderId) {
		Bundle req = new Bundle();
		req.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl("Patient?identifier=" + methodName);
		
		Observation obs = new Observation();
		obs.getSubject().setReferenceElement(patientPlaceholderId);
		obs.addIdentifier().setValue(methodName);
		obs.getCode().setText(methodName + pass);
		req.addEntry().setResource(obs).getRequest().setMethod(HTTPVerb.PUT).setUrl("Observation?identifier=" + methodName);
		
		Patient pat = new Patient();
		pat.addIdentifier().setValue(methodName);
		pat.addName().setFamily(methodName + pass);
		req.addEntry().setResource(pat).setFullUrl(patientPlaceholderId.getValue()).getRequest().setMethod(HTTPVerb.POST).setUrl("Patient");
		
		req.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl("Patient?identifier=" + methodName);
		return req;
	}

	private void testTransactionOrderingValidateResponse(int pass, Bundle resp) {
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));
		assertEquals(4, resp.getEntry().size());
		assertEquals("200 OK", resp.getEntry().get(0).getResponse().getStatus());
		if (pass == 0) {
			assertEquals("201 Created", resp.getEntry().get(1).getResponse().getStatus());
			assertThat(resp.getEntry().get(1).getResponse().getLocation(), startsWith("Observation/"));
			assertThat(resp.getEntry().get(1).getResponse().getLocation(), endsWith("_history/1"));
		} else {
			assertEquals("200 OK", resp.getEntry().get(1).getResponse().getStatus());
			assertThat(resp.getEntry().get(1).getResponse().getLocation(), startsWith("Observation/"));
			assertThat(resp.getEntry().get(1).getResponse().getLocation(), endsWith("_history/2"));
		}
		assertEquals("201 Created", resp.getEntry().get(2).getResponse().getStatus());
		assertThat(resp.getEntry().get(2).getResponse().getLocation(), startsWith("Patient/"));
		if (pass == 0) {
			assertEquals("204 No Content", resp.getEntry().get(3).getResponse().getStatus());
		} else {
			assertEquals("204 No Content", resp.getEntry().get(3).getResponse().getStatus());
		}
		
		
		Bundle respGetBundle = (Bundle) resp.getEntry().get(0).getResource();
		assertEquals(1, respGetBundle.getEntry().size());
		assertEquals("testTransactionOrdering" + pass, ((Patient)respGetBundle.getEntry().get(0).getResource()).getName().get(0).getFamily());
		assertThat(respGetBundle.getLink("self").getUrl(), endsWith("/Patient?identifier=testTransactionOrdering"));
	}

	@Test
	public void testTransactionOruBundle() throws IOException {
		myDaoConfig.setAllowMultipleDelete(true);

		String input = IOUtils.toString(getClass().getResourceAsStream("/oruBundle.json"), StandardCharsets.UTF_8);

		Bundle inputBundle;
		Bundle outputBundle;
		inputBundle = myFhirCtx.newJsonParser().parseResource(Bundle.class, input);
		outputBundle = mySystemDao.transaction(mySrd, inputBundle);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outputBundle));

		inputBundle = myFhirCtx.newJsonParser().parseResource(Bundle.class, input);
		outputBundle = mySystemDao.transaction(mySrd, inputBundle);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outputBundle));

		IBundleProvider allPatients = myPatientDao.search(new SearchParameterMap());
		assertEquals(1, allPatients.size().intValue());
	}

	@Test
	public void testTransactionReadAndSearch() {
		String methodName = "testTransactionReadAndSearch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType idv1 = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Created patient, got id: {}", idv1);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Family Name");
		p.setId("Patient/" + methodName);
		IIdType idv2 = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Updated patient, got id: {}", idv2);

		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl(idv1.toUnqualifiedVersionless().getValue());
		request.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl(idv1.toUnqualified().getValue());
		request.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		Bundle resp = mySystemDao.transaction(mySrd, request);

		assertEquals(3, resp.getEntry().size());

		BundleEntryComponent nextEntry;

		nextEntry = resp.getEntry().get(0);
		assertEquals(Patient.class, nextEntry.getResource().getClass());
		assertEquals(idv2.toUnqualified(), nextEntry.getResource().getIdElement().toUnqualified());

		nextEntry = resp.getEntry().get(1);
		assertEquals(Patient.class, nextEntry.getResource().getClass());
		assertEquals(idv1.toUnqualified(), nextEntry.getResource().getIdElement().toUnqualified());

		nextEntry = resp.getEntry().get(2);
		assertEquals(Bundle.class, nextEntry.getResource().getClass());
		Bundle respBundle = (Bundle) nextEntry.getResource();
		assertEquals(1, respBundle.getTotal());

		/*
		 * Interceptor should have been called once for the transaction, and once for the embedded operation
		 */
		ArgumentCaptor<ActionRequestDetails> detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		verify(myInterceptor, times(1)).incomingRequestPreHandled(eq(RestOperationTypeEnum.TRANSACTION), detailsCapt.capture());
		ActionRequestDetails details = detailsCapt.getValue();
		assertEquals("Bundle", details.getResourceType());
		assertEquals(Bundle.class, details.getResource().getClass());

		detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		verify(myInterceptor, times(1)).incomingRequestPreHandled(eq(RestOperationTypeEnum.READ), detailsCapt.capture());
		details = detailsCapt.getValue();
		assertEquals(idv1.toUnqualifiedVersionless().getValue(), details.getId().getValue());
		assertEquals("Patient", details.getResourceType());

		detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		verify(myInterceptor, times(1)).incomingRequestPreHandled(eq(RestOperationTypeEnum.VREAD), detailsCapt.capture());
		details = detailsCapt.getValue();
		assertEquals(idv1.toUnqualified().getValue(), details.getId().getValue());
		assertEquals("Patient", details.getResourceType());

		detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		verify(myInterceptor, times(1)).incomingRequestPreHandled(eq(RestOperationTypeEnum.SEARCH_TYPE), detailsCapt.capture());
		details = detailsCapt.getValue();
		assertEquals("Patient", details.getResourceType());

		
	}

	@Test
	public void testTransactionReadWithIfNoneMatch() {
		String methodName = "testTransactionReadWithIfNoneMatch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType idv1 = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Created patient, got id: {}", idv1);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Family Name");
		p.setId("Patient/" + methodName);
		IIdType idv2 = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Updated patient, got id: {}", idv2);

		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl(idv1.toUnqualifiedVersionless().getValue());
		request.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl(idv1.toUnqualifiedVersionless().getValue()).setIfNoneMatch("W/\"" + idv1.getVersionIdPart() + "\"");
		request.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl(idv1.toUnqualifiedVersionless().getValue()).setIfNoneMatch("W/\"" + idv2.getVersionIdPart() + "\"");

		Bundle resp = mySystemDao.transaction(mySrd, request);

		assertEquals(3, resp.getEntry().size());

		BundleEntryComponent nextEntry;

		nextEntry = resp.getEntry().get(0);
		assertNotNull(nextEntry.getResource());
		assertEquals(Patient.class, nextEntry.getResource().getClass());
		assertEquals(idv2.toUnqualified(), nextEntry.getResource().getIdElement().toUnqualified());
		assertEquals("200 OK", nextEntry.getResponse().getStatus());

		nextEntry = resp.getEntry().get(1);
		assertNotNull(nextEntry.getResource());
		assertEquals(Patient.class, nextEntry.getResource().getClass());
		assertEquals(idv2.toUnqualified(), nextEntry.getResource().getIdElement().toUnqualified());
		assertEquals("200 OK", nextEntry.getResponse().getStatus());

		nextEntry = resp.getEntry().get(2);
		assertEquals("304 Not Modified", nextEntry.getResponse().getStatus());
		assertNull(nextEntry.getResource());
	}

	@Test
	public void testTransactionSearchWithCount() {
		String methodName = "testTransactionSearchWithCount";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType idv1 = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Created patient, got id: {}", idv1);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Family Name");
		p.setId("Patient/" + methodName);
		IIdType idv2 = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Updated patient, got id: {}", idv2);

		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl("Patient?" + Constants.PARAM_COUNT + "=1");
		Bundle resp = mySystemDao.transaction(mySrd, request);

		assertEquals(1, resp.getEntry().size());

		BundleEntryComponent nextEntry = resp.getEntry().get(0);
		assertEquals(Bundle.class, nextEntry.getResource().getClass());
		Bundle respBundle = (Bundle) nextEntry.getResource();
		assertThat(respBundle.getTotal(), greaterThan(0));

		// Invalid _count

		request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl("Patient?" + Constants.PARAM_COUNT + "=GKJGKJG");
		try {
			mySystemDao.transaction(mySrd, request);
		} catch (InvalidRequestException e) {
			assertEquals(e.getMessage(), ("Invalid _count value: GKJGKJG"));
		}

		// Empty _count

		request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl("Patient?" + Constants.PARAM_COUNT + "=");
		respBundle = mySystemDao.transaction(mySrd, request);
		assertThat(respBundle.getEntry().size(), greaterThan(0));
	}

	@Test
	public void testTransactionSingleEmptyResource() {

		Bundle request = new Bundle();
		request.setType(BundleType.SEARCHSET);
		Patient p = new Patient();
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Unable to process transaction where incoming Bundle.type = searchset", e.getMessage());
		}

	}

	@Test
	public void testTransactionUpdateMatchUrlWithOneMatch() {
		String methodName = "testTransactionUpdateMatchUrlWithOneMatch";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.setId("Patient/" + methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.PUT).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(2, resp.getEntry().size());

		BundleEntryComponent nextEntry = resp.getEntry().get(0);
		assertEquals("200 OK", nextEntry.getResponse().getStatus());
		assertThat(nextEntry.getResponse().getLocation(), not(containsString("test")));
		assertEquals(id.toVersionless(), p.getIdElement().toVersionless());
		assertNotEquals(id, p.getId());
		assertThat(p.getId().toString(), endsWith("/_history/2"));

		nextEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_200_OK + " OK", nextEntry.getResponse().getStatus());
		assertThat(nextEntry.getResponse().getLocation(), not(emptyString()));

		nextEntry = resp.getEntry().get(1);
		o = myObservationDao.read(new IdType(nextEntry.getResponse().getLocation()), mySrd);
		assertEquals(id.toVersionless().getValue(), o.getSubject().getReference());

	}

	@Test
	public void testTransactionUpdateMatchUrlWithTwoMatch() {
		String methodName = "testTransactionUpdateMatchUrlWithTwoMatch";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.setId("Patient/" + methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.PUT).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (PreconditionFailedException e) {
			assertThat(e.getMessage(), containsString("with match URL \"Patient"));
		}
	}

	@Test
	public void testTransactionUpdateMatchUrlWithZeroMatch() {
		String methodName = "testTransactionUpdateMatchUrlWithZeroMatch";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addName().setFamily("Hello");
		IIdType id = myPatientDao.create(p, mySrd).getId();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.setId(methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.PUT).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(2, resp.getEntry().size());

		BundleEntryComponent nextEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", nextEntry.getResponse().getStatus());
		IdType patientId = new IdType(nextEntry.getResponse().getLocation());

		assertThat(nextEntry.getResponse().getLocation(), not(containsString("test")));
		assertNotEquals(id.toVersionless(), patientId.toVersionless());

		assertThat(patientId.getValue(), endsWith("/_history/1"));

		nextEntry = resp.getEntry().get(1);
		o = myObservationDao.read(new IdType(nextEntry.getResponse().getLocation()), mySrd);
		assertEquals(patientId.toVersionless().getValue(), o.getSubject().getReference());

	}

	@Test
	public void testTransactionUpdateNoMatchUrl() {
		String methodName = "testTransactionUpdateNoMatchUrl";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType id = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.setId("Patient/" + methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.PUT).setUrl("Patient/" + id.getIdPart());

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(2, resp.getEntry().size());

		BundleEntryComponent nextEntry = resp.getEntry().get(0);
		assertEquals("200 OK", nextEntry.getResponse().getStatus());

		assertThat(nextEntry.getResponse().getLocation(), (containsString("test")));
		assertEquals(id.toVersionless(), new IdType(nextEntry.getResponse().getLocation()).toVersionless());
		assertNotEquals(id, new IdType(nextEntry.getResponse().getLocation()));
		assertThat(nextEntry.getResponse().getLocation(), endsWith("/_history/2"));

		nextEntry = resp.getEntry().get(1);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", nextEntry.getResponse().getStatus());

		o = myObservationDao.read(new IdType(resp.getEntry().get(1).getResponse().getLocation()), mySrd);
		assertEquals(id.toVersionless().getValue(), o.getSubject().getReference());

	}

	/**
	 * Format changed, source isn't valid
	 */
	@Test
	@Ignore
	public void testTransactionWithBundledValidationSourceAndTarget() throws Exception {

		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/questionnaire-sdc-profile-example-ussg-fht.xml");
		String bundleStr = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		Bundle bundle = myFhirCtx.newXmlParser().parseResource(Bundle.class, bundleStr);

		Bundle resp = mySystemDao.transaction(mySrd, bundle);

		String encoded = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp);
		ourLog.info(encoded);

		encoded = myFhirCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(resp);
		//@formatter:off
		assertThat(encoded, containsString("\"response\":{" + 
				"\"status\":\"201 Created\"," + 
				"\"location\":\"Questionnaire/54127-6/_history/1\",")); 
		//@formatter:on

		/*
		 * Upload again to update
		 */

		resp = mySystemDao.transaction(mySrd, bundle);

		encoded = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp);
		ourLog.info(encoded);

		encoded = myFhirCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(resp);
		//@formatter:off
		assertThat(encoded, containsString("\"response\":{" + 
				"\"status\":\"200 OK\"," + 
				"\"location\":\"Questionnaire/54127-6/_history/2\",")); 
		//@formatter:on

	}

	@Test
	public void testTransactionWithInlineMatchUrl() throws Exception {
		myDaoConfig.setAllowInlineMatchUrlReferences(true);

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://www.ghh.org/identifiers").setValue("condreftestpatid1");
		myPatientDao.create(patient, mySrd);

		String input = IOUtils.toString(getClass().getResourceAsStream("/simone-conditional-url.xml"), StandardCharsets.UTF_8);
		Bundle bundle = myFhirCtx.newXmlParser().parseResource(Bundle.class, input);

		Bundle response = mySystemDao.transaction(mySrd, bundle);
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(response));

	}

	@Test
	public void testTransactionWithInlineMatchUrlMultipleMatches() throws Exception {
		myDaoConfig.setAllowInlineMatchUrlReferences(true);

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://www.ghh.org/identifiers").setValue("condreftestpatid1");
		myPatientDao.create(patient, mySrd);

		patient = new Patient();
		patient.addIdentifier().setSystem("http://www.ghh.org/identifiers").setValue("condreftestpatid1");
		myPatientDao.create(patient, mySrd);

		String input = IOUtils.toString(getClass().getResourceAsStream("/simone-conditional-url.xml"), StandardCharsets.UTF_8);
		Bundle bundle = myFhirCtx.newXmlParser().parseResource(Bundle.class, input);

		try {
			mySystemDao.transaction(mySrd, bundle);
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals("Invalid match URL \"Patient?identifier=http://www.ghh.org/identifiers|condreftestpatid1\" - Multiple resources match this search", e.getMessage());
		}

	}

	@Test
	public void testTransactionWithInlineMatchUrlNoMatches() throws Exception {
		myDaoConfig.setAllowInlineMatchUrlReferences(true);

		String input = IOUtils.toString(getClass().getResourceAsStream("/simone-conditional-url.xml"), StandardCharsets.UTF_8);
		Bundle bundle = myFhirCtx.newXmlParser().parseResource(Bundle.class, input);

		try {
			mySystemDao.transaction(mySrd, bundle);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("Invalid match URL \"Patient?identifier=http://www.ghh.org/identifiers|condreftestpatid1\" - No resources match this search", e.getMessage());
		}

	}

	@Test
	public void testTransactionWIthInvalidPlaceholder() throws Exception {
		Bundle res = new Bundle();
		res.setType(BundleType.TRANSACTION);

		Observation o1 = new Observation();
		o1.setId("cid:observation1");
		o1.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds02");
		res.addEntry().setResource(o1).getRequest().setMethod(HTTPVerb.POST).setUrl("Observation");

		try {
			mySystemDao.transaction(mySrd, res);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Invalid placeholder ID found: cid:observation1 - Must be of the form 'urn:uuid:[uuid]' or 'urn:oid:[oid]'", e.getMessage());
		}
	}

	@Test
	public void testTransactionWithInvalidType() {
		Bundle request = new Bundle();
		request.setType(BundleType.SEARCHSET);
		Patient p = new Patient();
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Unable to process transaction where incoming Bundle.type = searchset", e.getMessage());
		}

	}

	@Test
	public void testTransactionWithMultiBundle() throws IOException {
		String inputBundleString = loadClasspath("/batch-error.xml");
		Bundle bundle = myFhirCtx.newXmlParser().parseResource(Bundle.class, inputBundleString);
		Bundle resp = mySystemDao.transaction(mySrd, bundle);

		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		assertEquals("201 Created", resp.getEntry().get(0).getResponse().getStatus());
	}

	@Test
	public void testTransactionWithNullReference() {
		Patient p = new Patient();
		p.addName().setFamily("family");
		final IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		Bundle inputBundle = new Bundle();

		//@formatter:off
		Patient app0 = new Patient();
		app0.addName().setFamily("NEW PATIENT");
		String placeholderId0 = IdDt.newRandomUuid().getValue();
		inputBundle
			.addEntry()
				.setResource(app0)
				.setFullUrl(placeholderId0)
				.getRequest()
					.setMethod(HTTPVerb.POST)
					.setUrl("Patient");
		//@formatter:on

		//@formatter:off
		Appointment app1 = new Appointment();
		app1.addParticipant().getActor().setReference(id.getValue());
		inputBundle
			.addEntry()
				.setResource(app1)
				.getRequest()
					.setMethod(HTTPVerb.POST)
					.setUrl("Appointment");
		//@formatter:on

		//@formatter:off
		Appointment app2 = new Appointment();
		app2.addParticipant().getActor().setDisplay("NO REF");
		app2.addParticipant().getActor().setDisplay("YES REF").setReference(placeholderId0);
		inputBundle
			.addEntry()
				.setResource(app2)
				.getRequest()
					.setMethod(HTTPVerb.POST)
					.setUrl("Appointment");
		//@formatter:on

		Bundle outputBundle = mySystemDao.transaction(mySrd, inputBundle);
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(outputBundle));

		assertEquals(3, outputBundle.getEntry().size());
		IdDt id0 = new IdDt(outputBundle.getEntry().get(0).getResponse().getLocation());
		IdDt id2 = new IdDt(outputBundle.getEntry().get(2).getResponse().getLocation());

		app2 = myAppointmentDao.read(id2, mySrd);
		assertEquals("NO REF", app2.getParticipant().get(0).getActor().getDisplay());
		assertEquals(null, app2.getParticipant().get(0).getActor().getReference());
		assertEquals("YES REF", app2.getParticipant().get(1).getActor().getDisplay());
		assertEquals(id0.toUnqualifiedVersionless().getValue(), app2.getParticipant().get(1).getActor().getReference());
	}

	/**
	 * Per a message on the mailing list
	 */
	@Test
	public void testTransactionWithPostDoesntUpdate() throws Exception {

		// First bundle (name is Joshua)

		String input = IOUtils.toString(getClass().getResource("/dstu3-post1.xml"), StandardCharsets.UTF_8);
		Bundle request = myFhirCtx.newXmlParser().parseResource(Bundle.class, input);
		Bundle response = mySystemDao.transaction(mySrd, request);
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(response));

		assertEquals(1, response.getEntry().size());
		assertEquals("201 Created", response.getEntry().get(0).getResponse().getStatus());
		assertEquals("1", response.getEntry().get(0).getResponse().getEtag());
		String id = response.getEntry().get(0).getResponse().getLocation();

		// Now the second (name is Adam, shouldn't get used)

		input = IOUtils.toString(getClass().getResource("/dstu3-post2.xml"), StandardCharsets.UTF_8);
		request = myFhirCtx.newXmlParser().parseResource(Bundle.class, input);
		response = mySystemDao.transaction(mySrd, request);
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(response));

		assertEquals(1, response.getEntry().size());
		assertEquals("200 OK", response.getEntry().get(0).getResponse().getStatus());
		assertEquals("1", response.getEntry().get(0).getResponse().getEtag());
		String id2 = response.getEntry().get(0).getResponse().getLocation();
		assertEquals(id, id2);

		Patient patient = myPatientDao.read(new IdType(id), mySrd);
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient));
		assertEquals("Joshua", patient.getNameFirstRep().getGivenAsSingleString());
	}

	//
	//
	// /**
	// * Issue #55
	// */
	// @Test
	// public void testTransactionWithCidIds() throws Exception {
	// Bundle request = new Bundle();
	//
	// Patient p1 = new Patient();
	// p1.setId("cid:patient1");
	// p1.addIdentifier().setSystem("system").setValue("testTransactionWithCidIds01");
	// res.add(p1);
	//
	// Observation o1 = new Observation();
	// o1.setId("cid:observation1");
	// o1.getIdentifier().setSystem("system").setValue("testTransactionWithCidIds02");
	// o1.setSubject(new Reference("Patient/cid:patient1"));
	// res.add(o1);
	//
	// Observation o2 = new Observation();
	// o2.setId("cid:observation2");
	// o2.getIdentifier().setSystem("system").setValue("testTransactionWithCidIds03");
	// o2.setSubject(new Reference("Patient/cid:patient1"));
	// res.add(o2);
	//
	// ourSystemDao.transaction(res);
	//
	// assertTrue(p1.getId().getValue(), p1.getId().getIdPart().matches("^[0-9]+$"));
	// assertTrue(o1.getId().getValue(), o1.getId().getIdPart().matches("^[0-9]+$"));
	// assertTrue(o2.getId().getValue(), o2.getId().getIdPart().matches("^[0-9]+$"));
	//
	// assertThat(o1.getSubject().getReference().getValue(), endsWith("Patient/" + p1.getId().getIdPart()));
	// assertThat(o2.getSubject().getReference().getValue(), endsWith("Patient/" + p1.getId().getIdPart()));
	//
	// }
	//
	// @Test
	// public void testTransactionWithDelete() throws Exception {
	// Bundle request = new Bundle();
	//
	// /*
	// * Create 3
	// */
	//
	// List<IResource> res;
	// res = new ArrayList<IResource>();
	//
	// Patient p1 = new Patient();
	// p1.addIdentifier().setSystem("urn:system").setValue("testTransactionWithDelete");
	// res.add(p1);
	//
	// Patient p2 = new Patient();
	// p2.addIdentifier().setSystem("urn:system").setValue("testTransactionWithDelete");
	// res.add(p2);
	//
	// Patient p3 = new Patient();
	// p3.addIdentifier().setSystem("urn:system").setValue("testTransactionWithDelete");
	// res.add(p3);
	//
	// ourSystemDao.transaction(res);
	//
	// /*
	// * Verify
	// */
	//
	// IBundleProvider results = ourPatientDao.search(Patient.SP_IDENTIFIER, new TokenParam("urn:system",
	// "testTransactionWithDelete"));
	// assertEquals(3, results.size());
	//
	// /*
	// * Now delete 2
	// */
	//
	// request = new Bundle();
	// res = new ArrayList<IResource>();
	// List<IResource> existing = results.getResources(0, 3);
	//
	// p1 = new Patient();
	// p1.setId(existing.get(0).getId());
	// ResourceMetadataKeyEnum.DELETED_AT.put(p1, InstantDt.withCurrentTime());
	// res.add(p1);
	//
	// p2 = new Patient();
	// p2.setId(existing.get(1).getId());
	// ResourceMetadataKeyEnum.DELETED_AT.put(p2, InstantDt.withCurrentTime());
	// res.add(p2);
	//
	// ourSystemDao.transaction(res);
	//
	// /*
	// * Verify
	// */
	//
	// IBundleProvider results2 = ourPatientDao.search(Patient.SP_IDENTIFIER, new TokenParam("urn:system",
	// "testTransactionWithDelete"));
	// assertEquals(1, results2.size());
	// List<IResource> existing2 = results2.getResources(0, 1);
	// assertEquals(existing2.get(0).getId(), existing.get(2).getId());
	//
	// }

	@Test
	public void testTransactionWithReferenceToCreateIfNoneExist() {
		Bundle bundle = new Bundle();
		bundle.setType(BundleType.TRANSACTION);

		Medication med = new Medication();
		IdType medId = IdType.newRandomUuid();
		med.setId(medId);
		med.getCode().addCoding().setSystem("billscodes").setCode("theCode");
		bundle.addEntry().setResource(med).setFullUrl(medId.getValue()).getRequest().setMethod(HTTPVerb.POST).setIfNoneExist("Medication?code=billscodes|theCode");

		MedicationRequest mo = new MedicationRequest();
		mo.setMedication(new Reference(medId));
		bundle.addEntry().setResource(mo).setFullUrl(mo.getIdElement().getValue()).getRequest().setMethod(HTTPVerb.POST);

		ourLog.info("Request:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		Bundle outcome = mySystemDao.transaction(mySrd, bundle);
		ourLog.info("Response:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		IdType medId1 = new IdType(outcome.getEntry().get(0).getResponse().getLocation());
		IdType medOrderId1 = new IdType(outcome.getEntry().get(1).getResponse().getLocation());

		/*
		 * Again!
		 */

		bundle = new Bundle();
		bundle.setType(BundleType.TRANSACTION);

		med = new Medication();
		medId = IdType.newRandomUuid();
		med.getCode().addCoding().setSystem("billscodes").setCode("theCode");
		bundle.addEntry().setResource(med).setFullUrl(medId.getValue()).getRequest().setMethod(HTTPVerb.POST).setIfNoneExist("Medication?code=billscodes|theCode");

		mo = new MedicationRequest();
		mo.setMedication(new Reference(medId));
		bundle.addEntry().setResource(mo).setFullUrl(mo.getIdElement().getValue()).getRequest().setMethod(HTTPVerb.POST);

		outcome = mySystemDao.transaction(mySrd, bundle);

		IdType medId2 = new IdType(outcome.getEntry().get(0).getResponse().getLocation());
		IdType medOrderId2 = new IdType(outcome.getEntry().get(1).getResponse().getLocation());

		assertTrue(medId1.isIdPartValidLong());
		assertTrue(medId2.isIdPartValidLong());
		assertTrue(medOrderId1.isIdPartValidLong());
		assertTrue(medOrderId2.isIdPartValidLong());

		assertEquals(medId1, medId2);
		assertNotEquals(medOrderId1, medOrderId2);
	}
	
	
	@Test
	public void testTransactionWithRelativeOidIds() throws Exception {
		Bundle res = new Bundle();
		res.setType(BundleType.TRANSACTION);

		Patient p1 = new Patient();
		p1.setId("urn:oid:0.1.2.3");
		p1.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds01");
		res.addEntry().setResource(p1).getRequest().setMethod(HTTPVerb.POST).setUrl("Patient");

		Observation o1 = new Observation();
		o1.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds02");
		o1.setSubject(new Reference("urn:oid:0.1.2.3"));
		res.addEntry().setResource(o1).getRequest().setMethod(HTTPVerb.POST).setUrl("Observation");

		Observation o2 = new Observation();
		o2.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds03");
		o2.setSubject(new Reference("urn:oid:0.1.2.3"));
		res.addEntry().setResource(o2).getRequest().setMethod(HTTPVerb.POST).setUrl("Observation");

		Bundle resp = mySystemDao.transaction(mySrd, res);

		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		assertEquals(BundleType.TRANSACTIONRESPONSE, resp.getTypeElement().getValue());
		assertEquals(3, resp.getEntry().size());

		assertTrue(resp.getEntry().get(0).getResponse().getLocation(), new IdType(resp.getEntry().get(0).getResponse().getLocation()).getIdPart().matches("^[0-9]+$"));
		assertTrue(resp.getEntry().get(1).getResponse().getLocation(), new IdType(resp.getEntry().get(1).getResponse().getLocation()).getIdPart().matches("^[0-9]+$"));
		assertTrue(resp.getEntry().get(2).getResponse().getLocation(), new IdType(resp.getEntry().get(2).getResponse().getLocation()).getIdPart().matches("^[0-9]+$"));

		o1 = myObservationDao.read(new IdType(resp.getEntry().get(1).getResponse().getLocation()), mySrd);
		o2 = myObservationDao.read(new IdType(resp.getEntry().get(2).getResponse().getLocation()), mySrd);
		assertThat(o1.getSubject().getReferenceElement().getValue(), endsWith("Patient/" + p1.getIdElement().getIdPart()));
		assertThat(o2.getSubject().getReferenceElement().getValue(), endsWith("Patient/" + p1.getIdElement().getIdPart()));

	}
	

	/**
	 * This is not the correct way to do it, but we'll allow it to be lenient
	 */
	@Test
	public void testTransactionWithRelativeOidIdsQualified() throws Exception {
		Bundle res = new Bundle();
		res.setType(BundleType.TRANSACTION);

		Patient p1 = new Patient();
		p1.setId("urn:oid:0.1.2.3");
		p1.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds01");
		res.addEntry().setResource(p1).getRequest().setMethod(HTTPVerb.POST).setUrl("Patient");

		Observation o1 = new Observation();
		o1.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds02");
		o1.setSubject(new Reference("Patient/urn:oid:0.1.2.3"));
		res.addEntry().setResource(o1).getRequest().setMethod(HTTPVerb.POST).setUrl("Observation");

		Observation o2 = new Observation();
		o2.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds03");
		o2.setSubject(new Reference("Patient/urn:oid:0.1.2.3"));
		res.addEntry().setResource(o2).getRequest().setMethod(HTTPVerb.POST).setUrl("Observation");

		Bundle resp = mySystemDao.transaction(mySrd, res);

		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		assertEquals(BundleType.TRANSACTIONRESPONSE, resp.getTypeElement().getValue());
		assertEquals(3, resp.getEntry().size());

		assertTrue(resp.getEntry().get(0).getResponse().getLocation(), new IdType(resp.getEntry().get(0).getResponse().getLocation()).getIdPart().matches("^[0-9]+$"));
		assertTrue(resp.getEntry().get(1).getResponse().getLocation(), new IdType(resp.getEntry().get(1).getResponse().getLocation()).getIdPart().matches("^[0-9]+$"));
		assertTrue(resp.getEntry().get(2).getResponse().getLocation(), new IdType(resp.getEntry().get(2).getResponse().getLocation()).getIdPart().matches("^[0-9]+$"));

		o1 = myObservationDao.read(new IdType(resp.getEntry().get(1).getResponse().getLocation()), mySrd);
		o2 = myObservationDao.read(new IdType(resp.getEntry().get(2).getResponse().getLocation()), mySrd);
		assertThat(o1.getSubject().getReferenceElement().getValue(), endsWith("Patient/" + p1.getIdElement().getIdPart()));
		assertThat(o2.getSubject().getReferenceElement().getValue(), endsWith("Patient/" + p1.getIdElement().getIdPart()));

	}

	/**
	 * See #467
	 */
	@Test
	public void testTransactionWithSelfReferentialLink() {
		/*
		 * Link to each other
		 */
		Bundle request = new Bundle();

		Organization o1 = new Organization();
		o1.setId(IdType.newRandomUuid());
		o1.setName("ORG1");
		request.addEntry().setResource(o1).getRequest().setMethod(HTTPVerb.POST);

		Organization o2 = new Organization();
		o2.setName("ORG2");
		o2.setId(IdType.newRandomUuid());
		request.addEntry().setResource(o2).getRequest().setMethod(HTTPVerb.POST);

		o1.getPartOf().setReference(o2.getId());
		o2.getPartOf().setReference(o1.getId());

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(BundleType.TRANSACTIONRESPONSE, resp.getTypeElement().getValue());
		assertEquals(2, resp.getEntry().size());

		IdType id1 = new IdType(resp.getEntry().get(0).getResponse().getLocation());
		IdType id2 = new IdType(resp.getEntry().get(1).getResponse().getLocation());

		ourLog.info("ID1: {}", id1);

		SearchParameterMap map = new SearchParameterMap();
		map.add(Organization.SP_PARTOF, new ReferenceParam(id1.toUnqualifiedVersionless().getValue()));
		IBundleProvider res = myOrganizationDao.search(map);
		assertEquals(1, res.size().intValue());
		assertEquals(id2.toUnqualifiedVersionless().getValue(), res.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless().getValue());

		map = new SearchParameterMap();
		map.add(Organization.SP_PARTOF, new ReferenceParam(id2.toUnqualifiedVersionless().getValue()));
		res = myOrganizationDao.search(map);
		assertEquals(1, res.size().intValue());
		assertEquals(id1.toUnqualifiedVersionless().getValue(), res.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless().getValue());

		/*
		 * Link to self
		 */
		request = new Bundle();

		o1 = new Organization();
		o1.setId(id1);
		o1.setName("ORG1");
		request.addEntry().setResource(o1).getRequest().setMethod(HTTPVerb.PUT).setUrl(id1.toUnqualifiedVersionless().getValue());

		o2 = new Organization();
		o2.setName("ORG2");
		o2.setId(id2);
		request.addEntry().setResource(o2).getRequest().setMethod(HTTPVerb.PUT).setUrl(id2.toUnqualifiedVersionless().getValue());

		o1.getPartOf().setReference(o1.getId());
		o2.getPartOf().setReference(o2.getId());

		resp = mySystemDao.transaction(mySrd, request);
		assertEquals(BundleType.TRANSACTIONRESPONSE, resp.getTypeElement().getValue());
		assertEquals(2, resp.getEntry().size());

		id1 = new IdType(resp.getEntry().get(0).getResponse().getLocation());
		id2 = new IdType(resp.getEntry().get(1).getResponse().getLocation());

		ourLog.info("ID1: {}", id1);

		map = new SearchParameterMap();
		map.add(Organization.SP_PARTOF, new ReferenceParam(id1.toUnqualifiedVersionless().getValue()));
		res = myOrganizationDao.search(map);
		assertEquals(1, res.size().intValue());
		assertEquals(id1.toUnqualifiedVersionless().getValue(), res.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless().getValue());

		map = new SearchParameterMap();
		map.add(Organization.SP_PARTOF, new ReferenceParam(id2.toUnqualifiedVersionless().getValue()));
		res = myOrganizationDao.search(map);
		assertEquals(1, res.size().intValue());
		assertEquals(id2.toUnqualifiedVersionless().getValue(), res.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless().getValue());

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
