package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.interceptor.IServerOperationInterceptor;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class FhirResourceDaoR4InterceptorTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4InterceptorTest.class);
	private List<IIdType> myIds = new ArrayList<>();

	@AfterEach
	public void after() {
		myDaoConfig.setAllowMultipleDelete(new DaoConfig().isAllowMultipleDelete());
	}

	@BeforeEach
	public void before() {
	}

	@Test
	public void testJpaCreate() {
		IServerOperationInterceptor interceptor = mock(IServerOperationInterceptor.class);
		myInterceptorRegistry.registerInterceptor(interceptor);

		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		Long id = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		ArgumentCaptor<RequestDetails> detailsCapt;
		ArgumentCaptor<IBaseResource> tableCapt;

		detailsCapt = ArgumentCaptor.forClass(RequestDetails.class);
		tableCapt = ArgumentCaptor.forClass(IBaseResource.class);
		verify(interceptor, times(1)).resourceCreated(detailsCapt.capture(), tableCapt.capture());
		assertNotNull(tableCapt.getValue().getIdElement().getIdPart());
		assertEquals(id, tableCapt.getValue().getIdElement().getIdPartAsLong());

		detailsCapt = ArgumentCaptor.forClass(RequestDetails.class);
		tableCapt = ArgumentCaptor.forClass(IBaseResource.class);
		verify(interceptor, times(0)).resourceUpdated(detailsCapt.capture(), tableCapt.capture());

		/*
		 * Not do a conditional create
		 */
		p = new Patient();
		p.addName().setFamily("PATIENT1");
		Long id2 = myPatientDao.create(p, "Patient?family=PATIENT", mySrd).getId().getIdPartAsLong();
		assertEquals(id, id2);

		detailsCapt = ArgumentCaptor.forClass(RequestDetails.class);
		tableCapt = ArgumentCaptor.forClass(IBaseResource.class);
		verify(interceptor, times(1)).resourceCreated(detailsCapt.capture(), tableCapt.capture());
		verify(interceptor, times(0)).resourceUpdated(detailsCapt.capture(), tableCapt.capture());

	}

	/*
	 * *****************************************************
	 * Note that non JPA specific operations get tested in individual
	 * operation test methods too
	 * *****************************************************
	 */

	@Test
	public void testJpaDelete() {
		IServerOperationInterceptor interceptor = mock(IServerOperationInterceptor.class);
		myInterceptorRegistry.registerInterceptor(interceptor);

		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		Long id = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		myPatientDao.delete(new IdType("Patient", id), mySrd);

		ArgumentCaptor<RequestDetails> detailsCapt;
		ArgumentCaptor<IBaseResource> tableCapt;

		detailsCapt = ArgumentCaptor.forClass(RequestDetails.class);
		tableCapt = ArgumentCaptor.forClass(IBaseResource.class);
		verify(interceptor, times(1)).resourceDeleted(detailsCapt.capture(), tableCapt.capture());
		assertNotNull(tableCapt.getValue().getIdElement().getIdPart());
		assertEquals(id, tableCapt.getValue().getIdElement().getIdPartAsLong());

	}

	@Test
	public void testJpaUpdate() {
		IServerOperationInterceptor interceptor = mock(IServerOperationInterceptor.class);
		myInterceptorRegistry.registerInterceptor(interceptor);

		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		Long id = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		p = new Patient();
		p.setId(new IdType(id));
		p.addName().setFamily("PATIENT1");
		Long id2 = myPatientDao.update(p, mySrd).getId().getIdPartAsLong();
		assertEquals(id, id2);

		ArgumentCaptor<RequestDetails> detailsCapt;
		ArgumentCaptor<IBaseResource> tableCapt;

		detailsCapt = ArgumentCaptor.forClass(RequestDetails.class);
		tableCapt = ArgumentCaptor.forClass(IBaseResource.class);
		verify(interceptor, times(1)).resourceUpdated(detailsCapt.capture(), tableCapt.capture());
		assertNotNull(tableCapt.getValue().getIdElement().getIdPart());
		assertEquals(id, tableCapt.getValue().getIdElement().getIdPartAsLong());

		/*
		 * Now do a conditional update
		 */

		p = new Patient();
		p.setId(new IdType(id));
		p.addName().setFamily("PATIENT2");
		id2 = myPatientDao.update(p, "Patient?family=PATIENT1", mySrd).getId().getIdPartAsLong();
		assertEquals(id, id2);

		detailsCapt = ArgumentCaptor.forClass(RequestDetails.class);
		tableCapt = ArgumentCaptor.forClass(IBaseResource.class);
		verify(interceptor, times(1)).resourceCreated(detailsCapt.capture(), tableCapt.capture());
		verify(interceptor, times(2)).resourceUpdated(detailsCapt.capture(), tableCapt.capture());
		assertEquals(id, tableCapt.getAllValues().get(2).getIdElement().getIdPartAsLong());

		/*
		 * Now do a conditional update where none will match (so this is actually a create)
		 */

		p = new Patient();
		p.addName().setFamily("PATIENT3");
		id2 = myPatientDao.update(p, "Patient?family=ZZZ", mySrd).getId().getIdPartAsLong();
		assertNotEquals(id, id2);

		detailsCapt = ArgumentCaptor.forClass(RequestDetails.class);
		tableCapt = ArgumentCaptor.forClass(IBaseResource.class);
		verify(interceptor, times(2)).resourceUpdated(detailsCapt.capture(), tableCapt.capture());
		verify(interceptor, times(2)).resourceCreated(detailsCapt.capture(), tableCapt.capture());
		assertEquals(id2, tableCapt.getAllValues().get(3).getIdElement().getIdPartAsLong());

	}

	@Test
	public void testRequestOperationCreate() {
		IServerOperationInterceptor interceptor = mock(IServerOperationInterceptor.class);
		myInterceptorRegistry.registerInterceptor(interceptor);

		doAnswer(t -> {
			IBaseResource res = (IBaseResource) t.getArguments()[1];
			Long id = res.getIdElement().getIdPartAsLong();
			assertEquals("Patient/" + id + "/_history/1", res.getIdElement().getValue());
			return null;
		}).when(interceptor).resourceCreated(any(), any());

		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		IIdType id = myPatientDao.create(p, mySrd).getId();
		assertEquals(1L, id.getVersionIdPartAsLong().longValue());

		verify(interceptor, times(1)).resourcePreCreate(any(), any());
		verify(interceptor, times(1)).resourceCreated(any(), any());
	}

	@Test
	public void testRequestOperationDelete() {
		IServerOperationInterceptor interceptor = mock(IServerOperationInterceptor.class);
		myInterceptorRegistry.registerInterceptor(interceptor);

		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		Long id = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		doAnswer(t -> {
			IBaseResource res = (IBaseResource) t.getArguments()[1];
			Long id1 = res.getIdElement().getIdPartAsLong();
			assertEquals("Patient/" + id1 + "/_history/2", res.getIdElement().getValue());
			return null;
		}).when(interceptor).resourceDeleted(any(), any());

		IIdType newId = myPatientDao.delete(new IdType("Patient/" + id), mySrd).getId();
		assertEquals(2L, newId.getVersionIdPartAsLong().longValue());

		verify(interceptor, times(1)).resourcePreDelete(any(), any());
		verify(interceptor, times(1)).resourcePreCreate(any(), any());
		verify(interceptor, times(1)).resourceDeleted(any(), any());
		verify(interceptor, times(1)).resourceCreated(any(), any());
		verifyNoMoreInteractions(interceptor);
	}

	@Test
	public void testRequestOperationDeleteMulti() {
		IServerOperationInterceptor interceptor = mock(IServerOperationInterceptor.class);
		myInterceptorRegistry.registerInterceptor(interceptor);

		myDaoConfig.setAllowMultipleDelete(true);

		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		Long id = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		p = new Patient();
		p.addName().setFamily("PATIENT");
		Long id2 = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock theInvocation) {
				IBaseResource res = (IBaseResource) theInvocation.getArguments()[1];
				Long id = res.getIdElement().getIdPartAsLong();
				assertEquals("Patient/" + id + "/_history/2", res.getIdElement().getValue());
				return null;
			}
		}).when(interceptor).resourceDeleted(any(), any());

		DeleteMethodOutcome outcome = myPatientDao.deleteByUrl("Patient?name=PATIENT", mySrd);
		String oo = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(outcome.getOperationOutcome());
		ourLog.info(oo);
		assertThat(oo, containsString("deleted 2 resource(s)"));

		verify(interceptor, times(2)).resourceDeleted(any(), any());
		verify(interceptor, times(2)).resourceCreated(any(), any());
		verify(interceptor, times(2)).resourcePreDelete(any(), any());
		verify(interceptor, times(2)).resourcePreCreate(any(), any());
		verifyNoMoreInteractions(interceptor);
	}

	@Test
	public void testRequestOperationTransactionCreate() {
		IServerOperationInterceptor interceptor = mock(IServerOperationInterceptor.class);
		myInterceptorRegistry.registerInterceptor(interceptor);

		Patient p = new Patient();
		p.addName().setFamily("PATIENT");

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock theInvocation) {
				IBaseResource res = (IBaseResource) theInvocation.getArguments()[1];
				Long id = res.getIdElement().getIdPartAsLong();
				assertEquals("Patient/" + id + "/_history/1", res.getIdElement().getValue());
				return null;
			}
		}).when(interceptor).resourceCreated(any(), any());

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

		verify(interceptor, times(1)).resourceCreated(any(), any());
		verify(interceptor, times(1)).resourcePreCreate(any(), any());
		verifyNoMoreInteractions(interceptor);
	}

	@Test
	public void testRequestOperationTransactionDelete() {
		IServerOperationInterceptor interceptor = mock(IServerOperationInterceptor.class);
		myInterceptorRegistry.registerInterceptor(interceptor);

		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		Long id = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock theInvocation) {
				IBaseResource res = (IBaseResource) theInvocation.getArguments()[1];
				Long id = res.getIdElement().getIdPartAsLong();
				assertEquals("Patient/" + id + "/_history/2", res.getIdElement().getValue());
				return null;
			}
		}).when(interceptor).resourceDeleted(any(), any());

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

		verify(interceptor, times(1)).resourcePreDelete(any(), any());
		verify(interceptor, times(1)).resourcePreCreate(any(), any());
		verify(interceptor, times(1)).resourceDeleted(any(), any());
		verify(interceptor, times(1)).resourceCreated(any(), any());
		verifyNoMoreInteractions(interceptor);
	}

	@Test
	public void testRequestOperationTransactionDeleteMulti() {
		IServerOperationInterceptor interceptor = mock(IServerOperationInterceptor.class);
		myInterceptorRegistry.registerInterceptor(interceptor);

		myDaoConfig.setAllowMultipleDelete(true);

		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		Long id = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		p = new Patient();
		p.addName().setFamily("PATIENT");
		Long id2 = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock theInvocation) {
				IBaseResource res = (IBaseResource) theInvocation.getArguments()[1];
				Long id = res.getIdElement().getIdPartAsLong();
				assertEquals("Patient/" + id + "/_history/2", res.getIdElement().getValue());
				return null;
			}
		}).when(interceptor).resourceDeleted(any(), any());

		Bundle xactBundle = new Bundle();
		xactBundle.setType(BundleType.TRANSACTION);
		xactBundle
			.addEntry()
			.getRequest()
			.setUrl("Patient?name=PATIENT")
			.setMethod(HTTPVerb.DELETE);
		Bundle resp = mySystemDao.transaction(mySrd, xactBundle);

		String oo = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp);
		ourLog.info(oo);
		assertThat(oo, containsString("deleted 2 resource(s)"));

		verify(interceptor, times(2)).resourceDeleted(any(), any());
		verify(interceptor, times(2)).resourceCreated(any(), any());
		verify(interceptor, times(2)).resourcePreDelete(any(), any());
		verify(interceptor, times(2)).resourcePreCreate(any(), any());
		verifyNoMoreInteractions(interceptor);
	}

	@Test
	public void testRequestOperationTransactionUpdate() {
		IServerOperationInterceptor interceptor = mock(IServerOperationInterceptor.class);
		myInterceptorRegistry.registerInterceptor(interceptor);

		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		final Long id = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		p = new Patient();
		p.setId(new IdType("Patient/" + id));
		p.addName().setFamily("PATIENT2");

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock theInvocation) {
				// Old contents
				IBaseResource res = (IBaseResource) theInvocation.getArguments()[1];
				assertEquals("Patient/" + id + "/_history/1", res.getIdElement().getValue());

				// New contents
				res = (IBaseResource) theInvocation.getArguments()[2];
				assertEquals("Patient/" + id + "/_history/2", res.getIdElement().getValue());

				return null;
			}
		}).when(interceptor).resourceUpdated(any(), any(), any());

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

		verify(interceptor, times(1)).resourceUpdated(any(), any(), any());
		verify(interceptor, times(1)).resourceCreated(any(), any());
		verify(interceptor, times(1)).resourcePreCreate(any(), any());
		verify(interceptor, times(1)).resourcePreUpdate(any(), any(), any());
	}

	@Test
	public void testRequestOperationUpdate() {
		IServerOperationInterceptor interceptor = mock(IServerOperationInterceptor.class);
		myInterceptorRegistry.registerInterceptor(interceptor);

		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		final Long id = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock theInvocation) {
				IBaseResource res = (IBaseResource) theInvocation.getArguments()[1];
				assertEquals("Patient/" + id + "/_history/1", res.getIdElement().getValue());
				res = (IBaseResource) theInvocation.getArguments()[2];
				assertEquals("Patient/" + id + "/_history/2", res.getIdElement().getValue());
				return null;
			}
		}).when(interceptor).resourceUpdated(any(), any(), any());

		p = new Patient();
		p.setId(new IdType("Patient/" + id));
		p.addName().setFamily("PATIENT2");
		IIdType newId = myPatientDao.update(p, mySrd).getId();
		assertEquals(2L, newId.getVersionIdPartAsLong().longValue());

		verify(interceptor, times(1)).resourceUpdated(any(), any(), any());
		verify(interceptor, times(1)).resourceCreated(any(), any());
		verify(interceptor, times(1)).resourcePreCreate(any(), any());
		verify(interceptor, times(1)).resourcePreUpdate(any(), any(), any());
	}

	@Test
	public void testServerOperationCreate() {
		IServerOperationInterceptor interceptor = mock(IServerOperationInterceptor.class);
		myInterceptorRegistry.registerInterceptor(interceptor);

		verify(interceptor, times(0)).resourceCreated(Mockito.isNull(), any());

		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		IIdType id = myPatientDao.create(p, (RequestDetails) null).getId();
		assertEquals(1L, id.getVersionIdPartAsLong().longValue());

		verify(interceptor, times(1)).resourceCreated(Mockito.isNull(), any());
	}

	@Test
	public void testServerOperationDelete() {
		IServerOperationInterceptor interceptor = mock(IServerOperationInterceptor.class);
		myInterceptorRegistry.registerInterceptor(interceptor);

		verify(interceptor, times(0)).resourceCreated(Mockito.isNull(), any());
		verify(interceptor, times(0)).resourceDeleted(Mockito.isNull(), any());

		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		IIdType id = myPatientDao.create(p, (RequestDetails) null).getId();
		assertEquals(1L, id.getVersionIdPartAsLong().longValue());

		p.addName().setFamily("2");
		myPatientDao.delete(p.getIdElement().toUnqualifiedVersionless());

		verify(interceptor, times(1)).resourceCreated(Mockito.isNull(), any());
		verify(interceptor, times(1)).resourceDeleted(Mockito.isNull(), any());
	}

	/**
	 * Make sure that both JPA interceptors and RestfulServer interceptors can
	 * get called
	 */
	@Test
	public void testServerOperationInterceptorCanModifyOnCreateForJpaInterceptor() {

		Object interceptor = new Object() {
			@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
			public void resourcePreCreate(IBaseResource theResource) {
				((Patient) theResource).setActive(true);
			}
		};
		mySrdInterceptorService.registerInterceptor(interceptor);

		Patient p = new Patient();
		p.setActive(false);

		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = myPatientDao.read(id);
		assertEquals(true, p.getActive());

	}

	@Test
	public void testPrestorageClientAssignedIdInterceptorCanDenyClientAssignedIds() {
		Object interceptor = new Object() {
			@Hook(Pointcut.STORAGE_PRESTORAGE_CLIENT_ASSIGNED_ID)
			public void prestorageClientAssignedId(IBaseResource theResource, RequestDetails theRequest) {
				throw new ForbiddenOperationException("Client assigned id rejected.");
			}
		};
		mySrdInterceptorService.registerInterceptor(interceptor);

		{//Ensure interceptor is not invoked on create.
			Patient serverAssignedPatient = new Patient();
			try {
				myPatientDao.create(serverAssignedPatient, mySrd);
			} catch (ForbiddenOperationException e) {
				fail("Interceptor was invoked, and should not have been!");
			}
		}

		{//Ensure attempting to set a client assigned id is rejected by our interceptor.
			try {
				Patient clientAssignedPatient = new Patient();
				clientAssignedPatient.setId("Patient/custom-id");
				myPatientDao.update(clientAssignedPatient, mySrd);
				fail();
			} catch (ForbiddenOperationException e) {
				assertEquals("Client assigned id rejected.", e.getMessage());
			}
		}
	}

	/**
	 * Make sure that both JPA interceptors and RestfulServer interceptors can
	 * get called
	 */
	@Test
	public void testServerOperationInterceptorCanModifyOnCreateForServerInterceptor() {

		Object interceptor = new Object() {
			@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
			public void resourcePreCreate(IBaseResource theResource) {
				((Patient) theResource).setActive(true);
			}
		};

		myInterceptorRegistry.registerInterceptor(interceptor);

		Patient p = new Patient();
		p.setActive(false);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = myPatientDao.read(id);
		assertEquals(true, p.getActive());

	}

	@Test
	public void testServerOperationInterceptorCanModifyOnUpdate() {

		Object interceptor = new Object() {
			@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
			public void resourcePreUpdate(RequestDetails theRequest, IBaseResource theOldResource, IBaseResource theNewResource) {
				((Patient) theNewResource).setActive(true);
			}
		};
		myInterceptorRegistry.registerInterceptor(interceptor);

			Patient p = new Patient();
			p.setActive(false);
			IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();

			p = myPatientDao.read(id);
			assertEquals(false, p.getActive());

			p.setId(p.getIdElement().toUnqualifiedVersionless());
			p.addAddress().setCity("CITY");
			myPatientDao.update(p, mySrd);

			p = myPatientDao.read(id);
			assertEquals(true, p.getActive());

	}

	@Test
	public void testServerOperationPreDelete() {
		IServerOperationInterceptor interceptor = mock(IServerOperationInterceptor.class);
		myInterceptorRegistry.registerInterceptor(interceptor);

		doAnswer(new MyOneResourceAnswer()).when(interceptor).resourcePreDelete(nullable(ServletRequestDetails.class), any(Patient.class));
		doAnswer(new MyOneResourceAnswer()).when(interceptor).resourceDeleted(nullable(ServletRequestDetails.class), any(Patient.class));

		Patient p = new Patient();
		p.setActive(false);
		IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();
		String idPart = id.getIdPart();

		myPatientDao.delete(id);

		InOrder inorder = inOrder(interceptor);
		inorder.verify(interceptor, times(1)).resourcePreDelete(nullable(ServletRequestDetails.class), any(Patient.class));
		inorder.verify(interceptor, times(1)).resourceDeleted(nullable(ServletRequestDetails.class), any(Patient.class));
		// resourcePreDelete
		assertEquals(idPart, myIds.get(0).getIdPart());
		assertEquals("1", myIds.get(0).getVersionIdPart());
		// resourceDeleted
		assertEquals(idPart, myIds.get(1).getIdPart());
		assertEquals("2", myIds.get(1).getVersionIdPart());

	}

	@SuppressWarnings("deprecation")
	@Test
	public void testServerOperationUpdate() {
		IServerOperationInterceptor interceptor = mock(IServerOperationInterceptor.class);
		myInterceptorRegistry.registerInterceptor(interceptor);

		verify(interceptor, times(0)).resourceCreated(Mockito.isNull(), any());
		verify(interceptor, times(0)).resourceUpdated(Mockito.isNull(), any());
		verify(interceptor, times(0)).resourceUpdated(Mockito.isNull(), any(), any());

		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		IIdType id = myPatientDao.create(p, (RequestDetails) null).getId();
		assertEquals(1L, id.getVersionIdPartAsLong().longValue());

		p.addName().setFamily("2");
		myPatientDao.update(p);

		verify(interceptor, times(1)).resourceCreated(Mockito.isNull(), any());
		verify(interceptor, times(1)).resourceUpdated(Mockito.isNull(), any());
		verify(interceptor, times(1)).resourceUpdated(Mockito.isNull(), any(), any());
	}

	private class MyOneResourceAnswer implements Answer {
		@Override
		public Object answer(InvocationOnMock invocation) {
			IIdType id = ((IBaseResource) invocation.getArguments()[1]).getIdElement();
			myIds.add(new IdType(id.getValue()));
			return null;
		}
	}

	private class MyTwoResourceAnswer implements Answer {
		@Override
		public Object answer(InvocationOnMock invocation) {
			IIdType id = ((IBaseResource) invocation.getArguments()[1]).getIdElement();
			myIds.add(new IdType(id.getValue()));
			id = ((IBaseResource) invocation.getArguments()[2]).getIdElement();
			myIds.add(new IdType(id.getValue()));
			return null;
		}
	}

}
