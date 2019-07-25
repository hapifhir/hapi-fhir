package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.dao.*;
import ca.uhn.fhir.jpa.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.TagTypeEnum;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.model.valueset.BundleEntryTransactionMethodEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.exceptions.*;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.hamcrest.core.StringContains;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.r4.model.OperationOutcome.IssueType;
import org.hl7.fhir.r4.model.Quantity.QuantityComparator;
import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@SuppressWarnings({ "unchecked", "deprecation" })
public class FhirResourceDaoCreatePlaceholdersR4Test extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoCreatePlaceholdersR4Test.class);

	@After
	public final void afterResetDao() {
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(new DaoConfig().isAutoCreatePlaceholderReferenceTargets());
	}

	@Test
	public void testCreateWithBadReferenceFails() {

		Observation o = new Observation();
		o.setStatus(ObservationStatus.FINAL);
		o.getSubject().setReference("Patient/FOO");
		try {
			myObservationDao.create(o, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Resource Patient/FOO not found, specified in path: Observation.subject", e.getMessage());
		}
	}

	@Test
	public void testCreateWithBadReferenceIsPermitted() {
		assertFalse(myDaoConfig.isAutoCreatePlaceholderReferenceTargets());
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);

		Observation o = new Observation();
		o.setStatus(ObservationStatus.FINAL);
		o.getSubject().setReference("Patient/FOO");
		myObservationDao.create(o, mySrd);
	}

	@Test
	public void testCreateWithMultiplePlaceholders() {
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);

		Task task = new Task();
		task.addNote().setText("A note");
		task.addPartOf().setReference("Task/AAA");
		task.addPartOf().setReference("Task/AAA");
		task.addPartOf().setReference("Task/AAA");
		IIdType id = myTaskDao.create(task).getId().toUnqualifiedVersionless();

		task = myTaskDao.read(id);
		assertEquals(3, task.getPartOf().size());
		assertEquals("Task/AAA", task.getPartOf().get(0).getReference());
		assertEquals("Task/AAA", task.getPartOf().get(1).getReference());
		assertEquals("Task/AAA", task.getPartOf().get(2).getReference());

		SearchParameterMap params = new SearchParameterMap();
		params.add(Task.SP_PART_OF, new ReferenceParam("Task/AAA"));
		List<String> found = toUnqualifiedVersionlessIdValues(myTaskDao.search(params));
		assertThat(found, contains(id.getValue()));


	}

	@Test
	public void testUpdateWithBadReferenceFails() {

		Observation o = new Observation();
		o.setStatus(ObservationStatus.FINAL);
		IIdType id = myObservationDao.create(o, mySrd).getId();

		o = new Observation();
		o.setId(id);
		o.setStatus(ObservationStatus.FINAL);
		o.getSubject().setReference("Patient/FOO");
		try {
			myObservationDao.update(o, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Resource Patient/FOO not found, specified in path: Observation.subject", e.getMessage());
		}
	}

	@Test
	public void testUpdateWithBadReferenceIsPermitted() {
		assertFalse(myDaoConfig.isAutoCreatePlaceholderReferenceTargets());
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);

		Observation o = new Observation();
		o.setStatus(ObservationStatus.FINAL);
		IIdType id = myObservationDao.create(o, mySrd).getId();

		o = new Observation();
		o.setId(id);
		o.setStatus(ObservationStatus.FINAL);
		o.getSubject().setReference("Patient/FOO");
		myObservationDao.update(o, mySrd);
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


}
