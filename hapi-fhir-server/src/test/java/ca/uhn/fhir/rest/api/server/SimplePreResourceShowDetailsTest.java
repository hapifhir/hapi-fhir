package ca.uhn.fhir.rest.api.server;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SimplePreResourceShowDetailsTest {

	@Mock
	private IBaseResource myResource1;
	@Mock
	private IBaseResource myResource2;

	@Test(expected = IllegalArgumentException.class)
	public void testSetResource_TooLow() {
		SimplePreResourceShowDetails details = new SimplePreResourceShowDetails(myResource1);
		details.setResource(-1, myResource2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetResource_TooHigh() {
		SimplePreResourceShowDetails details = new SimplePreResourceShowDetails(myResource1);
		details.setResource(2, myResource2);
	}

	@Test
	public void testSetResource() {
		SimplePreResourceShowDetails details = new SimplePreResourceShowDetails(myResource1);
		details.setResource(0, myResource2);
		assertSame(myResource2, details.iterator().next());
	}
}
