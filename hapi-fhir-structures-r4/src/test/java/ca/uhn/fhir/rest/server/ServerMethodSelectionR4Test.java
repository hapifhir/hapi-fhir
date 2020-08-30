package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class ServerMethodSelectionR4Test extends BaseR4ServerTest {


	/**
	 * Server method with no _include
	 * Client request with _include
	 * <p>
	 * See #1421
	 */
	@Test
	public void testRejectIncludeIfNotProvided() throws Exception {

		class MyProvider extends MyBaseProvider {
			@Search
			public List<IBaseResource> search(@OptionalParam(name = "name") StringType theName) {
				return Lists.newArrayList(new Patient().setActive(true).setId("Patient/123"));
			}
		}
		MyProvider provider = new MyProvider();

		startServer(provider);

		try {
			myClient
				.search()
				.forResource(Patient.class)
				.where(Patient.NAME.matches().value("foo"))
				.include(Patient.INCLUDE_ORGANIZATION)
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("this server does not know how to handle GET operation[Patient] with parameters [[_include, name]]"));
		}
	}

	/**
	 * Server method with no _include
	 * Client request with _include
	 * <p>
	 * See #1421
	 */
	@Test
	public void testAllowIncludeIfProvided() throws Exception {

		class MyProvider extends MyBaseProvider {
			@Search
			public List<IBaseResource> search(@OptionalParam(name = "name") StringType theName, @IncludeParam Set<Include> theIncludes) {
				return Lists.newArrayList(new Patient().setActive(true).setId("Patient/123"));
			}
		}
		MyProvider provider = new MyProvider();

		startServer(provider);

		Bundle results = myClient
			.search()
			.forResource(Patient.class)
			.where(Patient.NAME.matches().value("foo"))
			.include(Patient.INCLUDE_ORGANIZATION)
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(1, results.getEntry().size());
	}

	/**
	 * Server method with no _revinclude
	 * Client request with _revinclude
	 * <p>
	 * See #1421
	 */
	@Test
	public void testRejectRevIncludeIfNotProvided() throws Exception {

		class MyProvider extends MyBaseProvider {
			@Search
			public List<IBaseResource> search(@OptionalParam(name = "name") StringType theName) {
				return Lists.newArrayList(new Patient().setActive(true).setId("Patient/123"));
			}
		}
		MyProvider provider = new MyProvider();

		startServer(provider);

		try {
			myClient
				.search()
				.forResource(Patient.class)
				.where(Patient.NAME.matches().value("foo"))
				.revInclude(Patient.INCLUDE_ORGANIZATION)
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("this server does not know how to handle GET operation[Patient] with parameters [[_revinclude, name]]"));
		}
	}

	/**
	 * Server method with no _revInclude
	 * Client request with _revInclude
	 * <p>
	 * See #1421
	 */
	@Test
	public void testAllowRevIncludeIfProvided() throws Exception {

		class MyProvider extends MyBaseProvider {
			@Search
			public List<IBaseResource> search(@OptionalParam(name = "name") StringType theName, @IncludeParam(reverse = true) Set<Include> theRevIncludes) {
				return Lists.newArrayList(new Patient().setActive(true).setId("Patient/123"));
			}
		}
		MyProvider provider = new MyProvider();

		startServer(provider);

		Bundle results = myClient
			.search()
			.forResource(Patient.class)
			.where(Patient.NAME.matches().value("foo"))
			.revInclude(Patient.INCLUDE_ORGANIZATION)
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(1, results.getEntry().size());
	}



	public static class MyBaseProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

	}

}
