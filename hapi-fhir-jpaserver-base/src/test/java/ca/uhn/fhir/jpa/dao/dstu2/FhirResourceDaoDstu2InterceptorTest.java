package ca.uhn.fhir.jpa.dao.dstu2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.interceptor.IJpaServerInterceptor;
import ca.uhn.fhir.jpa.interceptor.JpaServerInterceptorAdapter;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;

public class FhirResourceDaoDstu2InterceptorTest extends BaseJpaDstu2Test {

	private IJpaServerInterceptor myJpaInterceptor;
	private JpaServerInterceptorAdapter myJpaInterceptorAdapter = new JpaServerInterceptorAdapter();

	@After
	public void after() {
		myDaoConfig.getInterceptors().remove(myJpaInterceptor);
		myDaoConfig.getInterceptors().remove(myJpaInterceptorAdapter);
	}
	
	@Before
	public void before() {
		myJpaInterceptor = mock(IJpaServerInterceptor.class);
		myDaoConfig.getInterceptors().add(myJpaInterceptor);
		myDaoConfig.getInterceptors().add(myJpaInterceptorAdapter);
	}
	
	/*
	 * *****************************************************
	 * Note that non JPA specific operations get tested in individual
	 * operation test methods too
	 * *****************************************************
	 */
	
	@Test
	public void testJpaCreate() {
		Patient p = new Patient();
		p.addName().addFamily("PATIENT");
		Long id = myPatientDao.create(p, new ServletRequestDetails()).getId().getIdPartAsLong();
		
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
		p.addName().addFamily("PATIENT1");
		Long id2 = myPatientDao.create(p, "Patient?family=PATIENT", new ServletRequestDetails()).getId().getIdPartAsLong();
		assertEquals(id, id2);

		detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		tableCapt = ArgumentCaptor.forClass(ResourceTable.class);
		verify(myJpaInterceptor, times(1)).resourceCreated(detailsCapt.capture(), tableCapt.capture());
		verify(myJpaInterceptor, times(0)).resourceUpdated(detailsCapt.capture(), tableCapt.capture());

	}

	@Test
	public void testJpaDelete() {
		Patient p = new Patient();
		p.addName().addFamily("PATIENT");
		Long id = myPatientDao.create(p, new ServletRequestDetails()).getId().getIdPartAsLong();

		myPatientDao.delete(new IdDt("Patient", id), new ServletRequestDetails());
		
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
		p.addName().addFamily("PATIENT");
		Long id = myPatientDao.create(p, new ServletRequestDetails()).getId().getIdPartAsLong();

		p = new Patient();
		p.setId(new IdDt(id));
		p.addName().addFamily("PATIENT1");
		Long id2 = myPatientDao.update(p, new ServletRequestDetails()).getId().getIdPartAsLong();
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
		p.setId(new IdDt(id));
		p.addName().addFamily("PATIENT2");
		id2 = myPatientDao.update(p, "Patient?family=PATIENT1", new ServletRequestDetails()).getId().getIdPartAsLong();
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
		p.addName().addFamily("PATIENT3");
		id2 = myPatientDao.update(p, "Patient?family=ZZZ", new ServletRequestDetails()).getId().getIdPartAsLong();
		assertNotEquals(id, id2);

		detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		tableCapt = ArgumentCaptor.forClass(ResourceTable.class);
		verify(myJpaInterceptor, times(2)).resourceUpdated(detailsCapt.capture(), tableCapt.capture());
		verify(myJpaInterceptor, times(2)).resourceCreated(detailsCapt.capture(), tableCapt.capture());
		assertEquals(id2, tableCapt.getAllValues().get(3).getId());

		
	}

}
