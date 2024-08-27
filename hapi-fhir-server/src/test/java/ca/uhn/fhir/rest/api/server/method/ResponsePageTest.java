package ca.uhn.fhir.rest.api.server.method;

import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.BundleLinks;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.method.RequestedPage;
import ca.uhn.fhir.rest.server.method.ResponseBundleRequest;
import ca.uhn.fhir.rest.server.method.ResponsePage;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResponsePageTest {

	private ResponsePage.ResponsePageBuilder myBundleBuilder;

	private BundleLinks myLinks;

	private List<IBaseResource> myList;

	@Mock
	private IBundleProvider myBundleProvider;

	private ResponseBundleRequest myRequest;

	@BeforeEach
	public void before() {
		myBundleBuilder = new ResponsePage.ResponsePageBuilder();

		myLinks = new BundleLinks(
			"http://localhost", // server base
			new HashSet<>(), // includes set
			false, // pretty print
			BundleTypeEnum.SEARCHSET // links type
		);

		myList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			// does not matter what these are
			myList.add(mock(IBaseResource.class));
		}

		myRequest = createBundleRequest(0);
	}

	@ParameterizedTest
	@CsvSource({
		"false,false",
		"true,false",
		"false,true",
		"true,true"
	})
	public void bundleProviderOffsets_setsNextPreviousLinks_test(
		boolean theHasPreviousBoolean,
		boolean theHasNextBoolean
	) {
		// setup
		myBundleBuilder
			.setBundleProvider(myBundleProvider)
			.setResources(myList);
		RequestedPage requestedPage = new RequestedPage(
			0, // offset
			10 // limit
		);
		ResponsePage page = myBundleBuilder.build();

		page.setResponseBundleRequest(myRequest);
		page.setRequestedPage(requestedPage);

		// when
		if (theHasNextBoolean) {
			when(myBundleProvider.getNextPageId())
				.thenReturn("next");
		}
		if (theHasPreviousBoolean) {
			when(myBundleProvider.getPreviousPageId())
				.thenReturn("previous");
		}
		when(myBundleProvider.getCurrentPageOffset())
			.thenReturn(1);

		// test
		page.setNextPageIfNecessary(myLinks);
		page.setPreviousPageIfNecessary(myLinks);

		// verify
		verifyNextAndPreviousLinks(theHasPreviousBoolean, theHasNextBoolean);
	}

	@ParameterizedTest
	@CsvSource({
		"false,false",
		"true,false",
		"false,true",
		"true,true"
	})
	public void bundleProviderPageIds_setsNextPreviousLinks_test(
		boolean theHasPreviousBoolean,
		boolean theHasNextBoolean
	) {
		// setup
		// setup
		myBundleBuilder
			.setBundleProvider(myBundleProvider)
			.setResources(myList)
		;
		RequestedPage requestedPage = new RequestedPage(
			0, // offset
			10 // limit
		);
		ResponsePage page = myBundleBuilder.build();

		page.setResponseBundleRequest(myRequest);
		page.setRequestedPage(requestedPage);

		// when
		if (theHasNextBoolean) {
			when(myBundleProvider.getNextPageId())
				.thenReturn("next");
		}
		if (theHasPreviousBoolean) {
			when(myBundleProvider.getPreviousPageId())
				.thenReturn("previous");
		}

		// test
		page.setNextPageIfNecessary(myLinks);
		page.setPreviousPageIfNecessary(myLinks);

		// verify
		verifyNextAndPreviousLinks(theHasPreviousBoolean, theHasNextBoolean);
	}

	/**
	 * Tests for next and previous links
	 * when doing non-cached offsets.
	 *
	 * NB: In a non-cached search, having a null
	 *     myNumTotalResult is synonymous with having
	 *     a next link.
	 *     As such, we do not test for
	 *     null myNumTotalResults and expect no
	 *     next.
	 *     These test cases are omitted as a result.
	 */
	@ParameterizedTest
	@CsvSource({
		"true,false,true,true",
		"true,true,true,true",
		"false,false,false,true",
		"false,true,false,true",
		"false,false,true,true",
		"false,true,true,true",
		"true,false,true,false",
		"true,true,true,false",
		"false,false,false,false",
		"false,true,false,false",
		"false,false,true,false",
		"false,true,true,false"
	})
	public void nonCachedOffsetPaging_setsNextPreviousLinks_test(
		boolean theNumTotalResultsIsNull,
		boolean theHasPreviousBoolean,
		boolean theHasNextBoolean,
		boolean theHasTotalRequestedCountBool
	) {
		// setup
		myBundleBuilder
			.setBundleProvider(myBundleProvider)
			.setResources(myList);

		int offset = theHasPreviousBoolean ? 10 : 0;

		if (!theHasNextBoolean) {
			myBundleBuilder.setNumToReturn(10);
		}

		// when
		when(myBundleProvider.getCurrentPageOffset())
			.thenReturn(null);
		if (!theNumTotalResultsIsNull) {
			when(myBundleProvider.size())
				.thenReturn(10 + offset);
		} else {
			when(myBundleProvider.size())
				.thenReturn(null);
			if (theHasTotalRequestedCountBool) {
				myBundleBuilder.setTotalRequestedResourcesFetched(11); // 1 more than pagesize
			} else {
				myBundleBuilder.setPageSize(10);
			}
		}

		RequestedPage requestedPage = new RequestedPage(
			offset, // offset
			10 // limit
		);
		ResponsePage page = myBundleBuilder.build();

		page.setResponseBundleRequest(myRequest);
		page.setRequestedPage(requestedPage);
		page.setUseOffsetPaging(true);

		// test
		page.setNextPageIfNecessary(myLinks);
		page.setPreviousPageIfNecessary(myLinks);

		// verify
		verifyNextAndPreviousLinks(theHasPreviousBoolean, theHasNextBoolean);
	}

	@ParameterizedTest
	@CsvSource({
		"true,false,false,true",
		"true,true,false,true",
		"true,false,true,true",
		"true,true,true,true",
		"false,false,false,true",
		"false,true,false,true",
		"false,false,true,true",
		"false,true,true,true",
		"true,false,false,false",
		"true,true,false,false",
		"true,false,true,false",
		"true,true,true,false",
		"false,false,false,false",
		"false,true,false,false",
		"false,false,true,false",
		"false,true,true,false"
	})
	public void savedSearch_setsNextPreviousLinks_test(
		boolean theNumTotalResultsIsNull,
		boolean theHasPreviousBoolean,
		boolean theHasNextBoolean,
		boolean theHasTotalRequestedFetched
	) {
		// setup
		int pageSize = myList.size();
		myBundleBuilder
			.setResources(myList)
			.setSearchId("search-id")
			.setBundleProvider(myBundleProvider)
			.setPageSize(pageSize);

		int offset = 0;
		int includeResourceCount = 0;
		if (theHasPreviousBoolean) {
			offset = 10;
			myRequest = createBundleRequest(offset);
		}

		if (!theHasNextBoolean) {
			// add some includes to reach up to pagesize
			includeResourceCount = 1;
		}

		myBundleBuilder.setIncludedResourceCount(includeResourceCount);

		if (!theNumTotalResultsIsNull) {
			if (!theHasNextBoolean) {
				myBundleBuilder.setNumToReturn(pageSize + offset + includeResourceCount);
			}
		} else if (theHasTotalRequestedFetched) {
			if (theHasNextBoolean) {
				myBundleBuilder.setTotalRequestedResourcesFetched(pageSize + 1); // 1 more than page size
			} else {
				myBundleBuilder.setTotalRequestedResourcesFetched(pageSize);
			}
		}

		// when
		when(myBundleProvider.getCurrentPageOffset())
			.thenReturn(null);
		if (!theNumTotalResultsIsNull) {
			// accurate total (myNumTotalResults has a value)
			when(myBundleProvider.size())
				.thenReturn(offset + pageSize);
		} else {
			when(myBundleProvider.size())
				.thenReturn(null);
		}

		RequestedPage requestedPage = new RequestedPage(
			0, // offset
			10 // limit
		);
		ResponsePage page = myBundleBuilder.build();

		page.setResponseBundleRequest(myRequest);
		page.setRequestedPage(requestedPage);

		// test
		page.setNextPageIfNecessary(myLinks);
		page.setPreviousPageIfNecessary(myLinks);

		// verify
		verifyNextAndPreviousLinks(theHasPreviousBoolean, theHasNextBoolean);
	}

	private ResponseBundleRequest createBundleRequest(int theOffset) {
		RequestDetails details = new SystemRequestDetails();
		details.setFhirServerBase("http://serverbase.com");
		return new ResponseBundleRequest(
			null, // server
			myBundleProvider,
			details,
			theOffset, // offset
			null, // limit
			"self", // self link
			new HashSet<>(), // includes
			BundleTypeEnum.SEARCHSET,
			"search-id"
		);
	}

	private void verifyNextAndPreviousLinks(
		boolean theHasPreviousBoolean,
		boolean theHasNextBoolean
	) {
		if (theHasNextBoolean) {
			assertThat(myLinks.getNext()).as("Next link expected but not found").isNotNull();
		} else {
			assertThat(myLinks.getNext()).as("Found unexpected next link").isNull();
		}
		if (theHasPreviousBoolean) {
			assertThat(myLinks.getPrev()).as("Previous link expected but not found").isNotNull();
		} else {
			assertThat(myLinks.getPrev()).as("Found unexpected previous link").isNull();
		}
	}
}
