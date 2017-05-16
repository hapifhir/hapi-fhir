package ca.uhn.fhir.jpa.dao.dstu3;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.interceptor.IJpaServerInterceptor;
import ca.uhn.fhir.jpa.interceptor.JpaServerInterceptorAdapter;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.rest.server.interceptor.IServerOperationInterceptor;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.TestUtil;

public class FhirResourceDaoDstu3InterceptorTest extends BaseJpaDstu3Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu3InterceptorTest.class);
	private IJpaServerInterceptor myJpaInterceptor;
	private JpaServerInterceptorAdapter myJpaInterceptorAdapter = new JpaServerInterceptorAdapter();
	private IServerOperationInterceptor myServerOperationInterceptor;

	@After
	public void after() {
		myDaoConfig.getInterceptors().remove(myJpaInterceptor);
		myDaoConfig.getInterceptors().remove(myJpaInterceptorAdapter);
		myDaoConfig.setAllowMultipleDelete(new DaoConfig().isAllowMultipleDelete());
	}

	@Before
	public void before() {
		myJpaInterceptor = mock(IJpaServerInterceptor.class);
		
		myServerOperationInterceptor = mock(IServerOperationInterceptor.class, new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock theInvocation) throws Throwable {
				if (theInvocation.getMethod().getReturnType().equals(boolean.class)) {
					return true;
				}
				return null;
			}
		});
		
		myDaoConfig.getInterceptors().add(myJpaInterceptor);
		myDaoConfig.getInterceptors().add(myJpaInterceptorAdapter);
		myDaoConfig.getInterceptors().add(myServerOperationInterceptor);
	}

	@Test
	public void testJpaCreate() {
		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		Long id = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		ArgumentCaptor<ActionRequestDetails> detailsCapt;
		ArgumentCaptor<ResourceTable> tableCapt;

		detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		tableCapt = ArgumentCaptor.forClass(ResourceTable.class);
		verify(myJpaInterceptor, times(1)).resourceCreated(detailsCapt.capture(), tableCapt.capture());
		assertNotNull(tableCapt.getValue().getId());
		assertEquals(id, tableCapt.getValue().getId());

		detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		tableCapt = ArgumentCaptor.forClass(ResourceTable.class);
		verify(myJpaInterceptor, times(0)).resourceUpdated(detailsCapt.capture(), tableCapt.capture());

		/*
		 * Not do a conditional create
		 */
		p = new Patient();
		p.addName().setFamily("PATIENT1");
		Long id2 = myPatientDao.create(p, "Patient?family=PATIENT", mySrd).getId().getIdPartAsLong();
		assertEquals(id, id2);

		detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		tableCapt = ArgumentCaptor.forClass(ResourceTable.class);
		verify(myJpaInterceptor, times(1)).resourceCreated(detailsCapt.capture(), tableCapt.capture());
		verify(myJpaInterceptor, times(0)).resourceUpdated(detailsCapt.capture(), tableCapt.capture());

	}

	/*
	 * *****************************************************
	 * Note that non JPA specific operations get tested in individual
	 * operation test methods too
	 * *****************************************************
	 */

	@Test
	public void testJpaDelete() {
		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		Long id = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		myPatientDao.delete(new IdType("Patient", id), mySrd);

		ArgumentCaptor<ActionRequestDetails> detailsCapt;
		ArgumentCaptor<ResourceTable> tableCapt;

		detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		tableCapt = ArgumentCaptor.forClass(ResourceTable.class);
		verify(myJpaInterceptor, times(1)).resourceDeleted(detailsCapt.capture(), tableCapt.capture());
		assertNotNull(tableCapt.getValue().getId());
		assertEquals(id, tableCapt.getValue().getId());

	}

	@Test
	public void testJpaUpdate() {
		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		Long id = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		p = new Patient();
		p.setId(new IdType(id));
		p.addName().setFamily("PATIENT1");
		Long id2 = myPatientDao.update(p, mySrd).getId().getIdPartAsLong();
		assertEquals(id, id2);

		ArgumentCaptor<ActionRequestDetails> detailsCapt;
		ArgumentCaptor<ResourceTable> tableCapt;

		detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		tableCapt = ArgumentCaptor.forClass(ResourceTable.class);
		verify(myJpaInterceptor, times(1)).resourceUpdated(detailsCapt.capture(), tableCapt.capture());
		assertNotNull(tableCapt.getValue().getId());
		assertEquals(id, tableCapt.getValue().getId());

		/*
		 * Now do a conditional update
		 */

		p = new Patient();
		p.setId(new IdType(id));
		p.addName().setFamily("PATIENT2");
		id2 = myPatientDao.update(p, "Patient?family=PATIENT1", mySrd).getId().getIdPartAsLong();
		assertEquals(id, id2);

		detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		tableCapt = ArgumentCaptor.forClass(ResourceTable.class);
		verify(myJpaInterceptor, times(1)).resourceCreated(detailsCapt.capture(), tableCapt.capture());
		verify(myJpaInterceptor, times(2)).resourceUpdated(detailsCapt.capture(), tableCapt.capture());
		assertEquals(id, tableCapt.getAllValues().get(2).getId());

		/*
		 * Now do a conditional update where none will match (so this is actually a create)
		 */

		p = new Patient();
		p.addName().setFamily("PATIENT3");
		id2 = myPatientDao.update(p, "Patient?family=ZZZ", mySrd).getId().getIdPartAsLong();
		assertNotEquals(id, id2);

		detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		tableCapt = ArgumentCaptor.forClass(ResourceTable.class);
		verify(myJpaInterceptor, times(2)).resourceUpdated(detailsCapt.capture(), tableCapt.capture());
		verify(myJpaInterceptor, times(2)).resourceCreated(detailsCapt.capture(), tableCapt.capture());
		assertEquals(id2, tableCapt.getAllValues().get(3).getId());

	}

	@Test
	public void testRequestOperationCreate() {
		IServerOperationInterceptor interceptor = mock(IServerOperationInterceptor.class);
		myServerInterceptorList.add(interceptor);

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock theInvocation) throws Throwable {
				IBaseResource res = (IBaseResource) theInvocation.getArguments()[0];
				Long id = res.getIdElement().getIdPartAsLong();
				assertEquals("Patient/" + id + "/_history/1", res.getIdElement().getValue());
				return null;
			}}).when(myRequestOperationCallback).resourceCreated(any(IBaseResource.class));

		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		IIdType id = myPatientDao.create(p, mySrd).getId();
		assertEquals(1L, id.getVersionIdPartAsLong().longValue());
		
		verify(myRequestOperationCallback, times(1)).resourceCreated(any(IBaseResource.class));
		verifyNoMoreInteractions(myRequestOperationCallback);
	}

	@Test
	public void testServerOperationCreate() {
		verify(myServerOperationInterceptor, times(0)).resourceCreated(Mockito.isNull(RequestDetails.class), any(IBaseResource.class));

		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		IIdType id = myPatientDao.create(p, (RequestDetails)null).getId();
		assertEquals(1L, id.getVersionIdPartAsLong().longValue());
		
		verify(myServerOperationInterceptor, times(1)).resourceCreated(Mockito.isNull(RequestDetails.class), any(IBaseResource.class));
	}

	@Test
	public void testServerOperationUpdate() {
		verify(myServerOperationInterceptor, times(0)).resourceCreated(Mockito.isNull(RequestDetails.class), any(IBaseResource.class));
		verify(myServerOperationInterceptor, times(0)).resourceUpdated(Mockito.isNull(RequestDetails.class), any(IBaseResource.class));

		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		IIdType id = myPatientDao.create(p, (RequestDetails)null).getId();
		assertEquals(1L, id.getVersionIdPartAsLong().longValue());
		
		p.addName().setFamily("2");
		myPatientDao.update(p);
		
		verify(myServerOperationInterceptor, times(1)).resourceCreated(Mockito.isNull(RequestDetails.class), any(IBaseResource.class));
		verify(myServerOperationInterceptor, times(1)).resourceUpdated(Mockito.isNull(RequestDetails.class), any(IBaseResource.class));
	}

	@Test
	public void testServerOperationDelete() {
		verify(myServerOperationInterceptor, times(0)).resourceCreated(Mockito.isNull(RequestDetails.class), any(IBaseResource.class));
		verify(myServerOperationInterceptor, times(0)).resourceDeleted(Mockito.isNull(RequestDetails.class), any(IBaseResource.class));

		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		IIdType id = myPatientDao.create(p, (RequestDetails)null).getId();
		assertEquals(1L, id.getVersionIdPartAsLong().longValue());
		
		p.addName().setFamily("2");
		myPatientDao.delete(p.getIdElement().toUnqualifiedVersionless());
		
		verify(myServerOperationInterceptor, times(1)).resourceCreated(Mockito.isNull(RequestDetails.class), any(IBaseResource.class));
		verify(myServerOperationInterceptor, times(1)).resourceDeleted(Mockito.isNull(RequestDetails.class), any(IBaseResource.class));
	}

	@Test
	public void testRequestOperationDelete() {
		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		Long id = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock theInvocation) throws Throwable {
				IBaseResource res = (IBaseResource) theInvocation.getArguments()[0];
				Long id = res.getIdElement().getIdPartAsLong();
				assertEquals("Patient/" + id + "/_history/2", res.getIdElement().getValue());
				return null;
			}}).when(myRequestOperationCallback).resourceDeleted(any(IBaseResource.class));

		IIdType newId = myPatientDao.delete(new IdType("Patient/" + id), mySrd).getId();
		assertEquals(2L, newId.getVersionIdPartAsLong().longValue());

		verify(myRequestOperationCallback, times(1)).resourceDeleted(any(IBaseResource.class));
		verify(myRequestOperationCallback, times(1)).resourceCreated(any(IBaseResource.class));
		verifyNoMoreInteractions(myRequestOperationCallback);
	}

	@Test
	public void testRequestOperationDeleteMulti() {
		myDaoConfig.setAllowMultipleDelete(true);
		
		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		Long id = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		p = new Patient();
		p.addName().setFamily("PATIENT");
		Long id2 = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock theInvocation) throws Throwable {
				IBaseResource res = (IBaseResource) theInvocation.getArguments()[0];
				Long id = res.getIdElement().getIdPartAsLong();
				assertEquals("Patient/" + id + "/_history/2", res.getIdElement().getValue());
				return null;
			}}).when(myRequestOperationCallback).resourceDeleted(any(IBaseResource.class));

		DeleteMethodOutcome outcome = myPatientDao.deleteByUrl("Patient?name=PATIENT", mySrd);
		String oo = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(outcome.getOperationOutcome());
		ourLog.info(oo);
		assertThat(oo, containsString("deleted 2 resource(s)"));
		
		verify(myRequestOperationCallback, times(2)).resourceDeleted(any(IBaseResource.class));
		verify(myRequestOperationCallback, times(2)).resourceCreated(any(IBaseResource.class));
		verifyNoMoreInteractions(myRequestOperationCallback);
	}

	@Test
	public void testRequestOperationTransactionCreate() {
		Patient p = new Patient();
		p.addName().setFamily("PATIENT");

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock theInvocation) throws Throwable {
				IBaseResource res = (IBaseResource) theInvocation.getArguments()[0];
				Long id = res.getIdElement().getIdPartAsLong();
				assertEquals("Patient/" + id + "/_history/1", res.getIdElement().getValue());
				return null;
			}}).when(myRequestOperationCallback).resourceCreated(any(IBaseResource.class));

		Bundle xactBundle = new Bundle();
		xactBundle.setType(BundleType.TRANSACTION);
		xactBundle
				.addEntry()
				.setResource(p)
				.getRequest()
				.setUrl("Patient")
				.setMethod(HTTPVerb.POST);
		Bundle resp = mySystemDao.transaction(mySrd, xactBundle);

		IdType newId = new IdType(resp.getEntry().get(0).getResponse().getLocation());
		assertEquals(1L, newId.getVersionIdPartAsLong().longValue());
		
		verify(myRequestOperationCallback, times(1)).resourceCreated(any(IBaseResource.class));
		verifyNoMoreInteractions(myRequestOperationCallback);
	}

	@Test
	public void testRequestOperationTransactionDelete() {
		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		Long id = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock theInvocation) throws Throwable {
				IBaseResource res = (IBaseResource) theInvocation.getArguments()[0];
				Long id = res.getIdElement().getIdPartAsLong();
				assertEquals("Patient/" + id + "/_history/2", res.getIdElement().getValue());
				return null;
			}}).when(myRequestOperationCallback).resourceDeleted(any(IBaseResource.class));

		Bundle xactBundle = new Bundle();
		xactBundle.setType(BundleType.TRANSACTION);
		xactBundle
				.addEntry()
				.getRequest()
				.setUrl("Patient/" + id)
				.setMethod(HTTPVerb.DELETE);
		Bundle resp = mySystemDao.transaction(mySrd, xactBundle);

		IdType newId = new IdType(resp.getEntry().get(0).getResponse().getLocation());
		assertEquals(2L, newId.getVersionIdPartAsLong().longValue());

		verify(myRequestOperationCallback, times(1)).resourceDeleted(any(IBaseResource.class));
		verify(myRequestOperationCallback, times(1)).resourceCreated(any(IBaseResource.class));
		verifyNoMoreInteractions(myRequestOperationCallback);
	}

	@Test
	public void testRequestOperationTransactionDeleteMulti() {
		myDaoConfig.setAllowMultipleDelete(true);
		
		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		Long id = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		p = new Patient();
		p.addName().setFamily("PATIENT");
		Long id2 = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock theInvocation) throws Throwable {
				IBaseResource res = (IBaseResource) theInvocation.getArguments()[0];
				Long id = res.getIdElement().getIdPartAsLong();
				assertEquals("Patient/" + id + "/_history/2", res.getIdElement().getValue());
				return null;
			}}).when(myRequestOperationCallback).resourceDeleted(any(IBaseResource.class));

		Bundle xactBundle = new Bundle();
		xactBundle.setType(BundleType.TRANSACTION);
		xactBundle
				.addEntry()
				.getRequest()
				.setUrl("Patient?name=PATIENT")
				.setMethod(HTTPVerb.DELETE);
		Bundle resp = mySystemDao.transaction(mySrd, xactBundle);

		String oo = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp);
		ourLog.info(oo);
		assertThat(oo, containsString("deleted 2 resource(s)"));
		
		verify(myRequestOperationCallback, times(2)).resourceDeleted(any(IBaseResource.class));
		verify(myRequestOperationCallback, times(2)).resourceCreated(any(IBaseResource.class));
		verifyNoMoreInteractions(myRequestOperationCallback);
	}

	@Test
	public void testRequestOperationTransactionUpdate() {
		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		final Long id = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		p = new Patient();
		p.setId(new IdType("Patient/" + id));
		p.addName().setFamily("PATIENT2");

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock theInvocation) throws Throwable {
				IBaseResource res = (IBaseResource) theInvocation.getArguments()[0];
				assertEquals("Patient/" + id + "/_history/2", res.getIdElement().getValue());
				return null;
			}}).when(myRequestOperationCallback).resourceUpdated(any(IBaseResource.class));

		Bundle xactBundle = new Bundle();
		xactBundle.setType(BundleType.TRANSACTION);
		xactBundle
				.addEntry()
				.setResource(p)
				.getRequest()
				.setUrl("Patient/" + id)
				.setMethod(HTTPVerb.PUT);
		Bundle resp = mySystemDao.transaction(mySrd, xactBundle);

		IdType newId = new IdType(resp.getEntry().get(0).getResponse().getLocation());
		assertEquals(2L, newId.getVersionIdPartAsLong().longValue());

		verify(myRequestOperationCallback, times(1)).resourceUpdated(any(IBaseResource.class));
		verify(myRequestOperationCallback, times(1)).resourceCreated(any(IBaseResource.class));
		verifyNoMoreInteractions(myRequestOperationCallback);
	}

	@Test
	public void testRequestOperationUpdate() {
		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		final Long id = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock theInvocation) throws Throwable {
				IBaseResource res = (IBaseResource) theInvocation.getArguments()[0];
				assertEquals("Patient/" + id + "/_history/2", res.getIdElement().getValue());
				return null;
			}}).when(myRequestOperationCallback).resourceUpdated(any(IBaseResource.class));

		p = new Patient();
		p.setId(new IdType("Patient/" + id));
		p.addName().setFamily("PATIENT2");
		IIdType newId = myPatientDao.update(p, mySrd).getId();
		assertEquals(2L, newId.getVersionIdPartAsLong().longValue());

		verify(myRequestOperationCallback, times(1)).resourceUpdated(any(IBaseResource.class));
		verify(myRequestOperationCallback, times(1)).resourceCreated(any(IBaseResource.class));
		verifyNoMoreInteractions(myRequestOperationCallback);
	}
	
	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
