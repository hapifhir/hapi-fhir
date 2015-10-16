package ca.uhn.fhir.rest.client;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;

import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.rest.gclient.ICriterion;
import ca.uhn.fhir.rest.gclient.IQuery;

public class ClientMockingTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testMockingDeepStubs() {

		IGenericClient client = mock(IGenericClient.class, new ReturnsDeepStubs());

		// System.out.println(stub.getClass());
		// System.out.println(stub.getClass());

		Bundle retVal = new Bundle();

		//@formatter:off
		Object when = client
			 .search()
			 .forResource(eq(Condition.class))
			 .where(any(ICriterion.class))
			 .returnBundle((Class<IBaseBundle>)any())
			 .execute();
		when((Object)when)
			 .thenReturn(retVal);
		//@formatter:off
		
		Bundle actual = client.search().forResource(Condition.class).where(Condition.ASSERTER.hasId("123")).returnBundle(Bundle.class).execute();
		assertSame(retVal, actual);
		
	}
	
	

}
