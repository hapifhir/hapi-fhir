package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchSortDstu3Test {

	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private static String ourLastMethod;
	private static SortSpec ourLastSortSpec;

	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .registerProvider(new DummyPatientResourceProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultPrettyPrint(false);

	@BeforeEach
	public void before() {
		ourLastMethod = null;
		ourLastSortSpec = null;
	}

	@Test
	public void testSearch() throws Exception {
		String responseContent = ourServer.fhirRequest("/Patient?_sort=param1,-param2,param3,-param4")
			.get()
			.assertStatus(200)
			.getBody();
		assertThat(ourLastMethod).isEqualTo("search");

		assertThat(ourLastSortSpec.getParamName()).isEqualTo("param1");
		assertThat(ourLastSortSpec.getOrder()).isEqualTo(SortOrderEnum.ASC);

		assertThat(ourLastSortSpec.getChain().getParamName()).isEqualTo("param2");
		assertThat(ourLastSortSpec.getChain().getOrder()).isEqualTo(SortOrderEnum.DESC);

		assertThat(ourLastSortSpec.getChain().getChain().getParamName()).isEqualTo("param3");
		assertThat(ourLastSortSpec.getChain().getChain().getOrder()).isEqualTo(SortOrderEnum.ASC);

		assertThat(ourLastSortSpec.getChain().getChain().getChain().getParamName()).isEqualTo("param4");
		assertThat(ourLastSortSpec.getChain().getChain().getChain().getOrder()).isEqualTo(SortOrderEnum.DESC);

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		//@formatter:off
		@SuppressWarnings("rawtypes")
		@Search()
		public List search(
				@Sort SortSpec theSortSpec
				) {
			ourLastMethod = "search";
			ourLastSortSpec = theSortSpec;
			ArrayList<Patient> retVal = new ArrayList<Patient>();
			for (int i = 1; i < 100; i++) {
				retVal.add((Patient) new Patient().addName(new HumanName().setFamily("FAMILY")).setId("" + i));
			}
			return retVal;
		}
		//@formatter:on

	}

}
