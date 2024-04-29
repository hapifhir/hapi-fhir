package ca.uhn.fhir.rest.api.server;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
public class SimplePreResourceShowDetailsTest {

	@Mock
	private IBaseResource myResource1;
	@Mock
	private IBaseResource myResource2;

	@Test
	public void testSetResource_TooLow() {
		try {
			SimplePreResourceShowDetails details = new SimplePreResourceShowDetails(myResource1);
			details.setResource(-1, myResource2);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid index -1 - theIndex must not be < 0", e.getMessage());
		}
	}

	@Test
	public void testSetResource_TooHigh() {
		try {
			SimplePreResourceShowDetails details = new SimplePreResourceShowDetails(myResource1);
			details.setResource(2, myResource2);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid index {} - theIndex must be < 2", e.getMessage());
		}
	}

	@Test
	public void testSetResource() {
		SimplePreResourceShowDetails details = new SimplePreResourceShowDetails(myResource1);
		details.setResource(0, myResource2);
		assertSame(myResource2, details.iterator().next());
	}


	@Test
	public void testGetResources() {
		SimplePreResourceShowDetails details = new SimplePreResourceShowDetails(List.of(myResource1, myResource2));
		assertThat(details.getAllResources(), contains(myResource1, myResource2));

		details.setResource(0, null);

		assertThat(details.getAllResources(), contains(myResource2));
	}

}
