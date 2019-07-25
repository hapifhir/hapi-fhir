package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.DeleteMethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.IServerOperationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ServerOperationInterceptorAdapter;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class FhirResourceDaoR4InterceptorTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4InterceptorTest.class);
	private IServerOperationInterceptor myJpaInterceptor;
	private ServerOperationInterceptorAdapter myJpaInterceptorAdapter = new ServerOperationInterceptorAdapter();
	private IServerOperationInterceptor myServerOperationInterceptor;
	private List<IIdType> myIds = new ArrayList<>();

	@After
	public void after() {
		myDaoConfig.getInterceptors().remove(myJpaInterceptor);
		myDaoConfig.getInterceptors().remove(myJpaInterceptorAdapter);
		myDaoConfig.setAllowMultipleDelete(new DaoConfig().isAllowMultipleDelete());
	}

	@Before
	public void before() {
		myJpaInterceptor = mock(IServerOperationInterceptor.class);
		myIds.clear();

		myServerOperationInterceptor = mock(IServerOperationInterceptor.class, new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock theInvocation) {
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

		ArgumentCaptor<RequestDetails> detailsCapt;
		ArgumentCaptor<IBaseResource> tableCapt;

		detailsCapt = ArgumentCaptor.forClass(RequestDetails.class);
		tableCapt = ArgumentCaptor.forClass(IBaseResource.class);
		verify(myJpaInterceptor, times(1)).resourceCreated(detailsCapt.capture(), tableCapt.capture());
		assertNotNull(tableCapt.getValue().getIdElement().getIdPart());
		assertEquals(id, tableCapt.getValue().getIdElement().getIdPartAsLong());

		detailsCapt = ArgumentCaptor.forClass(RequestDetails.class);
		tableCapt = ArgumentCaptor.forClass(IBaseResource.class);
		verify(myJpaInterceptor, times(0)).resourceUpdated(detailsCapt.capture(), tableCapt.capture());

		/*
		 * Not do a conditional create
		 */
		p = new Patient();
		p.addName().setFamily("PATIENT1");
		Long id2 = myPatientDao.create(p, "Patient?family=PATIENT", mySrd).getId().getIdPartAsLong();
		assertEquals(id, id2);

		detailsCapt = ArgumentCaptor.forClass(RequestDetails.class);
		tableCapt = ArgumentCaptor.forClass(IBaseResource.class);
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

		ArgumentCaptor<RequestDetails> detailsCapt;
		ArgumentCaptor<IBaseResource> tableCapt;

		detailsCapt = ArgumentCaptor.forClass(RequestDetails.class);
		tableCapt = ArgumentCaptor.forClass(IBaseResource.class);
		verify(myJpaInterceptor, times(1)).resourceDeleted(detailsCapt.capture(), tableCapt.capture());
		assertNotNull(tableCapt.getValue().getIdElement().getIdPart());
		assertEquals(id, tableCapt.getValue().getIdElement().getIdPartAsLong());

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

		ArgumentCaptor<RequestDetails> detailsCapt;
		ArgumentCaptor<IBaseResource> tableCapt;

		detailsCapt = ArgumentCaptor.forClass(RequestDetails.class);
		tableCapt = ArgumentCaptor.forClass(IBaseResource.class);
		verify(myJpaInterceptor, times(1)).resourceUpdated(detailsCapt.capture(), tableCapt.capture());
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
		verify(myJpaInterceptor, times(1)).resourceCreated(detailsCapt.capture(), tableCapt.capture());
		verify(myJpaInterceptor, times(2)).resourceUpdated(detailsCapt.capture(), tableCapt.capture());
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
		verify(myJpaInterceptor, times(2)).resourceUpdated(detailsCapt.capture(), tableCapt.capture());
		verify(myJpaInterceptor, times(2)).resourceCreated(detailsCapt.capture(), tableCapt.capture());
		assertEquals(id2, tableCapt.getAllValues().get(3).getIdElement().getIdPartAsLong());

	}

	@Test
	public void testRequestOperationCreate() {
		IServerOperationInterceptor interceptor = mock(IServerOperationInterceptor.class);
		myServerInterceptorList.add(interceptor);

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock theInvocation) {
				IBaseResource res = (IBaseResource) theInvocation.getArguments()[0];
				Long id = res.getIdElement().getIdPartAsLong();
				assertEquals("Patient/" + id + "/_history/1", res.getIdElement().getValue());
				return null;
			}
		}).when(myRequestOperationCallback).resourceCreated(any(IBaseResource.class));

		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		IIdType id = myPatientDao.create(p, mySrd).getId();
		assertEquals(1L, id.getVersionIdPartAsLong().longValue());

		verify(myRequestOperationCallback, times(1)).resourcePreCreate(any(IBaseResource.class));
		verify(myRequestOperationCallback, times(1)).resourceCreated(any(IBaseResource.class));
		verifyNoMoreInteractions(myRequestOperationCallback);
	}

	@Test
	public void testRequestOperationDelete() {
		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		Long id = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock theInvocation) {
				IBaseResource res = (IBaseResource) theInvocation.getArguments()[0];
				Long id = res.getIdElement().getIdPartAsLong();
				assertEquals("Patient/" + id + "/_history/2", res.getIdElement().getValue());
				return null;
			}
		}).when(myRequestOperationCallback).resourceDeleted(any(IBaseResource.class));

		IIdType newId = myPatientDao.delete(new IdType("Patient/" + id), mySrd).getId();
		assertEquals(2L, newId.getVersionIdPartAsLong().longValue());

		verify(myRequestOperationCallback, times(1)).resourcePreDelete(any(IBaseResource.class));
		verify(myRequestOperationCallback, times(1)).resourcePreCreate(any(IBaseResource.class));
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
			public Void answer(InvocationOnMock theInvocation) {
				IBaseResource res = (IBaseResource) theInvocation.getArguments()[0];
				Long id = res.getIdElement().getIdPartAsLong();
				assertEquals("Patient/" + id + "/_history/2", res.getIdElement().getValue());
				return null;
			}
		}).when(myRequestOperationCallback).resourceDeleted(any(IBaseResource.class));

		DeleteMethodOutcome outcome = myPatientDao.deleteByUrl("Patient?name=PATIENT", mySrd);
		String oo = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(outcome.getOperationOutcome());
		ourLog.info(oo);
		assertThat(oo, containsString("deleted 2 resource(s)"));

		verify(myRequestOperationCallback, times(2)).resourceDeleted(any(IBaseResource.class));
		verify(myRequestOperationCallback, times(2)).resourceCreated(any(IBaseResource.class));
		verify(myRequestOperationCallback, times(2)).resourcePreDelete(any(IBaseResource.class));
		verify(myRequestOperationCallback, times(2)).resourcePreCreate(any(IBaseResource.class));
		verifyNoMoreInteractions(myRequestOperationCallback);
	}

	@Test
	public void testRequestOperationTransactionCreate() {
		Patient p = new Patient();
		p.addName().setFamily("PATIENT");

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock theInvocation) {
				IBaseResource res = (IBaseResource) theInvocation.getArguments()[0];
				Long id = res.getIdElement().getIdPartAsLong();
				assertEquals("Patient/" + id + "/_history/1", res.getIdElement().getValue());
				return null;
			}
		}).when(myRequestOperationCallback).resourceCreated(any(IBaseResource.class));

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
		verify(myRequestOperationCallback, times(1)).resourcePreCreate(any(IBaseResource.class));
		verifyNoMoreInteractions(myRequestOperationCallback);
	}

	@Test
	public void testRequestOperationTransactionDelete() {
		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		Long id = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock theInvocation) {
				IBaseResource res = (IBaseResource) theInvocation.getArguments()[0];
				Long id = res.getIdElement().getIdPartAsLong();
				assertEquals("Patient/" + id + "/_history/2", res.getIdElement().getValue());
				return null;
			}
		}).when(myRequestOperationCallback).resourceDeleted(any(IBaseResource.class));

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

		verify(myRequestOperationCallback, times(1)).resourcePreDelete(any(IBaseResource.class));
		verify(myRequestOperationCallback, times(1)).resourcePreCreate(any(IBaseResource.class));
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
			public Void answer(InvocationOnMock theInvocation) {
				IBaseResource res = (IBaseResource) theInvocation.getArguments()[0];
				Long id = res.getIdElement().getIdPartAsLong();
				assertEquals("Patient/" + id + "/_history/2", res.getIdElement().getValue());
				return null;
			}
		}).when(myRequestOperationCallback).resourceDeleted(any(IBaseResource.class));

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
		verify(myRequestOperationCallback, times(2)).resourcePreDelete(any(IBaseResource.class));
		verify(myRequestOperationCallback, times(2)).resourcePreCreate(any(IBaseResource.class));
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
			public Void answer(InvocationOnMock theInvocation) {
				IBaseResource res = (IBaseResource) theInvocation.getArguments()[1];
				assertEquals("Patient/" + id + "/_history/2", res.getIdElement().getValue());
				return null;
			}
		}).when(myRequestOperationCallback).resourceUpdated(any(IBaseResource.class), any(IBaseResource.class));

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
		verify(myRequestOperationCallback, times(1)).resourceUpdated(any(IBaseResource.class), any(IBaseResource.class));
		verify(myRequestOperationCallback, times(1)).resourceCreated(any(IBaseResource.class));
		verify(myRequestOperationCallback, times(1)).resourcePreCreate(any(IBaseResource.class));
		verify(myRequestOperationCallback, times(1)).resourcePreUpdate(any(IBaseResource.class), any(IBaseResource.class));
		verifyNoMoreInteractions(myRequestOperationCallback);
	}

	@Test
	public void testRequestOperationUpdate() {
		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		final Long id = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock theInvocation) {
				IBaseResource res = (IBaseResource) theInvocation.getArguments()[0];
				assertEquals("Patient/" + id + "/_history/1", res.getIdElement().getValue());
				res = (IBaseResource) theInvocation.getArguments()[1];
				assertEquals("Patient/" + id + "/_history/2", res.getIdElement().getValue());
				return null;
			}
		}).when(myRequestOperationCallback).resourceUpdated(any(IBaseResource.class), any(IBaseResource.class));

		p = new Patient();
		p.setId(new IdType("Patient/" + id));
		p.addName().setFamily("PATIENT2");
		IIdType newId = myPatientDao.update(p, mySrd).getId();
		assertEquals(2L, newId.getVersionIdPartAsLong().longValue());

		verify(myRequestOperationCallback, times(1)).resourceUpdated(any(IBaseResource.class));
		verify(myRequestOperationCallback, times(1)).resourceUpdated(any(IBaseResource.class), any(IBaseResource.class));
		verify(myRequestOperationCallback, times(1)).resourceCreated(any(IBaseResource.class));
		verify(myRequestOperationCallback, times(1)).resourcePreCreate(any(IBaseResource.class));
		verify(myRequestOperationCallback, times(1)).resourcePreUpdate(any(IBaseResource.class), any(IBaseResource.class));
		verifyNoMoreInteractions(myRequestOperationCallback);
	}

	@Test
	public void testServerOperationCreate() {
		verify(myServerOperationInterceptor, times(0)).resourceCreated(Mockito.isNull(RequestDetails.class), any(IBaseResource.class));

		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		IIdType id = myPatientDao.create(p, (RequestDetails) null).getId();
		assertEquals(1L, id.getVersionIdPartAsLong().longValue());

		verify(myServerOperationInterceptor, times(1)).resourceCreated(Mockito.isNull(RequestDetails.class), any(IBaseResource.class));
	}

	@Test
	public void testServerOperationDelete() {
		verify(myServerOperationInterceptor, times(0)).resourceCreated(Mockito.isNull(RequestDetails.class), any(IBaseResource.class));
		verify(myServerOperationInterceptor, times(0)).resourceDeleted(Mockito.isNull(RequestDetails.class), any(IBaseResource.class));

		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		IIdType id = myPatientDao.create(p, (RequestDetails) null).getId();
		assertEquals(1L, id.getVersionIdPartAsLong().longValue());

		p.addName().setFamily("2");
		myPatientDao.delete(p.getIdElement().toUnqualifiedVersionless());

		verify(myServerOperationInterceptor, times(1)).resourceCreated(Mockito.isNull(RequestDetails.class), any(IBaseResource.class));
		verify(myServerOperationInterceptor, times(1)).resourceDeleted(Mockito.isNull(RequestDetails.class), any(IBaseResource.class));
	}

	@Test
	public void testServerOperationInterceptorCanModifyOnCreate() {

		ServerOperationInterceptorAdapter interceptor = new ServerOperationInterceptorAdapter() {
			@Override
			public void resourcePreCreate(RequestDetails theRequest, IBaseResource theResource) {
				((Patient) theResource).setActive(true);
			}
		};
		myDaoConfig.getInterceptors().add(interceptor);
		try {

			doAnswer(new MyOneResourceAnswer()).when(myJpaInterceptor).resourcePreCreate(any(RequestDetails.class), any(IBaseResource.class));
			doAnswer(new MyOneResourceAnswer()).when(myJpaInterceptor).resourceCreated(any(RequestDetails.class), any(IBaseResource.class));

			Patient p = new Patient();
			p.setActive(false);
			IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

			InOrder inorder = inOrder(myJpaInterceptor, myRequestOperationCallback);
			inorder.verify(myRequestOperationCallback, times(1)).resourcePreCreate(any(IBaseResource.class));
			inorder.verify(myJpaInterceptor, times(1)).resourcePreCreate(any(RequestDetails.class), any(IBaseResource.class));
			inorder.verify(myRequestOperationCallback, times(1)).resourceCreated(any(IBaseResource.class));
			inorder.verify(myJpaInterceptor, times(1)).resourceCreated(any(RequestDetails.class), any(IBaseResource.class));

			assertNull(myIds.get(0).getIdPart());
			assertNull(myIds.get(0).getVersionIdPart());
			assertNotNull(myIds.get(1).getIdPart());
			assertEquals("1", myIds.get(1).getVersionIdPart());

			p = myPatientDao.read(id);
			assertEquals(true, p.getActive());

		} finally {
			myDaoConfig.getInterceptors().remove(interceptor);
		}
	}

	@Test
	public void testServerOperationInterceptorCanModifyOnUpdate() {

		ServerOperationInterceptorAdapter interceptor = new ServerOperationInterceptorAdapter() {
			@Override
			public void resourcePreUpdate(RequestDetails theRequest, IBaseResource theOldResource, IBaseResource theNewResource) {
				((Patient) theNewResource).setActive(true);
			}
		};
		myDaoConfig.getInterceptors().add(interceptor);
		try {

			doAnswer(new MyTwoResourceAnswer()).when(myJpaInterceptor).resourcePreUpdate(any(RequestDetails.class), any(IBaseResource.class), any(IBaseResource.class));
			doAnswer(new MyTwoResourceAnswer()).when(myJpaInterceptor).resourceUpdated(any(RequestDetails.class), any(IBaseResource.class), any(IBaseResource.class));

			Patient p = new Patient();
			p.setActive(false);
			IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();
			String idPart = id.getIdPart();

			p = myPatientDao.read(id);
			assertEquals(false, p.getActive());

			p.setId(p.getIdElement().toUnqualifiedVersionless());
			p.addAddress().setCity("CITY");
			myPatientDao.update(p, mySrd);

			InOrder inorder = inOrder(myJpaInterceptor, myRequestOperationCallback);
			inorder.verify(myRequestOperationCallback, times(1)).resourcePreUpdate(any(IBaseResource.class), any(IBaseResource.class));
			inorder.verify(myJpaInterceptor, times(1)).resourcePreUpdate(any(RequestDetails.class), any(IBaseResource.class), any(IBaseResource.class));
			inorder.verify(myRequestOperationCallback, times(1)).resourceUpdated(any(IBaseResource.class), any(IBaseResource.class));
			inorder.verify(myJpaInterceptor, times(1)).resourceUpdated(any(RequestDetails.class), any(IBaseResource.class), any(IBaseResource.class));

			// resourcePreUpdate
			assertEquals(idPart, myIds.get(0).getIdPart());
			assertEquals("1", myIds.get(0).getVersionIdPart());
			assertEquals(idPart, myIds.get(1).getIdPart());
			assertEquals(null, myIds.get(1).getVersionIdPart());
			// resourceUpdated
			assertEquals(idPart, myIds.get(2).getIdPart());
			assertEquals("1", myIds.get(2).getVersionIdPart());
			assertEquals(idPart, myIds.get(3).getIdPart());
			assertEquals("2", myIds.get(3).getVersionIdPart());

			p = myPatientDao.read(id);
			assertEquals(true, p.getActive());

		} finally {
			myDaoConfig.getInterceptors().remove(interceptor);
		}
	}

	@Test
	public void testServerOperationPreDelete() {

		doAnswer(new MyOneResourceAnswer()).when(myJpaInterceptor).resourcePreDelete(nullable(ServletRequestDetails.class), any(Patient.class));
		doAnswer(new MyOneResourceAnswer()).when(myJpaInterceptor).resourceDeleted(nullable(ServletRequestDetails.class), any(Patient.class));

		Patient p = new Patient();
		p.setActive(false);
		IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();
		String idPart = id.getIdPart();

		myPatientDao.delete(id);

		InOrder inorder = inOrder(myJpaInterceptor);
		inorder.verify(myJpaInterceptor, times(1)).resourcePreDelete(nullable(ServletRequestDetails.class), any(Patient.class));
		inorder.verify(myJpaInterceptor, times(1)).resourceDeleted(nullable(ServletRequestDetails.class), any(Patient.class));
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
		verify(myServerOperationInterceptor, times(0)).resourceCreated(Mockito.isNull(RequestDetails.class), any(IBaseResource.class));
		verify(myServerOperationInterceptor, times(0)).resourceUpdated(Mockito.isNull(RequestDetails.class), any(IBaseResource.class));
		verify(myServerOperationInterceptor, times(0)).resourceUpdated(Mockito.isNull(RequestDetails.class), any(IBaseResource.class), any(IBaseResource.class));

		Patient p = new Patient();
		p.addName().setFamily("PATIENT");
		IIdType id = myPatientDao.create(p, (RequestDetails) null).getId();
		assertEquals(1L, id.getVersionIdPartAsLong().longValue());

		p.addName().setFamily("2");
		myPatientDao.update(p);

		verify(myServerOperationInterceptor, times(1)).resourceCreated(Mockito.isNull(RequestDetails.class), any(IBaseResource.class));
		verify(myServerOperationInterceptor, times(1)).resourceUpdated(Mockito.isNull(RequestDetails.class), any(IBaseResource.class));
		verify(myServerOperationInterceptor, times(1)).resourceUpdated(Mockito.isNull(RequestDetails.class), any(IBaseResource.class), any(IBaseResource.class));
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
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
