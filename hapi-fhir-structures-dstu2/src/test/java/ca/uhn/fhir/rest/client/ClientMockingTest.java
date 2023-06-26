package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.ICriterion;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientMockingTest {

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}


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
