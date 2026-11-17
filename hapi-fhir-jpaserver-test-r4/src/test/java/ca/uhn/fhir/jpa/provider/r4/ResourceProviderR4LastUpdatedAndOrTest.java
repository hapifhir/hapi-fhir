package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.api.Constants;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.function.LongPredicate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <code>_lastUpdated</code> with AND (repetition) and OR (comma) combinations over REST (hapi-fhir#8857).
 * Expected results come from the FHIR R4 prefix rules applied to the <code>meta.lastUpdated</code> of each
 * resource, searched with millisecond precision values.
 */
public class ResourceProviderR4LastUpdatedAndOrTest extends BaseResourceProviderR4Test {

	private static final String[] PREFIXES = {"eq", "ne", "gt", "lt", "ge", "le", "sa", "eb", "ap"};
	private final HttpClient myHttpClient = HttpClient.newHttpClient();
	private final List<String> myPatientIds = new ArrayList<>();
	private final List<String> myObservationIds = new ArrayList<>();
	private final long[] myPatientTimes = new long[5];
	private final long[] myObservationTimes = new long[5];

	@Autowired
	private IBatch2JobInstanceRepository myJobInstanceRepository;

	@BeforeEach
	public void createResources() throws InterruptedException {
		for (int i = 0; i < 5; i++) {
			Patient patient = new Patient();
			patient.addName().setFamily("P" + (i + 1));
			String patientId = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getValue();
			myPatientIds.add(patientId);
			myPatientTimes[i] = myPatientDao.read(patient.getIdElement().toUnqualifiedVersionless(), mySrd).getMeta().getLastUpdated().getTime();
			Thread.sleep(5);

			Observation observation = new Observation();
			observation.setStatus(Observation.ObservationStatus.FINAL);
			observation.setSubject(new Reference(patientId));
			String observationId = myObservationDao.create(observation, mySrd).getId().toUnqualifiedVersionless().getValue();
			myObservationIds.add(observationId);
			myObservationTimes[i] = myObservationDao.read(observation.getIdElement().toUnqualifiedVersionless(), mySrd).getMeta().getLastUpdated().getTime();
			Thread.sleep(5);
		}
		for (int i = 1; i < 5; i++) {
			assertThat(myObservationTimes[i]).isGreaterThan(myObservationTimes[i - 1]);
			assertThat(myPatientTimes[i]).isGreaterThan(myPatientTimes[i - 1]);
		}
	}

	private static String format(long theMillis, String theZone) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		f.setTimeZone(TimeZone.getTimeZone(theZone));
		return f.format(new Date(theMillis));
	}

	private static String encode(String theValue) {
		return URLEncoder.encode(theValue, StandardCharsets.UTF_8);
	}

	/**
	 * FHIR R4 prefix rules for an instant against a search value with millisecond precision
	 */
	private static LongPredicate spec(String thePrefix, long theValue) {
		return switch (thePrefix) {
			case "eq" -> t -> t == theValue;
			case "ne" -> t -> t != theValue;
			case "gt", "sa" -> t -> t > theValue;
			case "lt", "eb" -> t -> t < theValue;
			case "ge" -> t -> t >= theValue;
			case "le" -> t -> t <= theValue;
			default -> null;
		};
	}

	/**
	 * @param theAndOr AND groups of OR alternatives, each "[prefix][index of T]" (e.g. "ne2")
	 * @return the expected IDs, or <code>null</code> if the expression uses "ap"
	 */
	private static Set<String> expected(long[] theTimes, List<String> theIds, String[][] theAndOr) {
		Set<String> retVal = new TreeSet<>();
		for (int i = 0; i < theTimes.length; i++) {
			boolean all = true;
			for (String[] nextOr : theAndOr) {
				boolean any = false;
				for (String next : nextOr) {
					LongPredicate predicate = spec(next.substring(0, 2), theTimes[Integer.parseInt(next.substring(2)) - 1]);
					if (predicate == null) {
						return null;
					}
					any |= predicate.test(theTimes[i]);
				}
				all &= any;
			}
			if (all) {
				retVal.add(theIds.get(i));
			}
		}
		return retVal;
	}

	private static String toQuery(String theParamName, long[] theTimes, String[][] theAndOr) {
		List<String> parts = new ArrayList<>();
		for (String[] nextOr : theAndOr) {
			List<String> values = new ArrayList<>();
			for (String next : nextOr) {
				values.add(next.substring(0, 2) + encode(format(theTimes[Integer.parseInt(next.substring(2)) - 1], "UTC")));
			}
			parts.add(theParamName + "=" + String.join(",", values));
		}
		return String.join("&", parts);
	}

	private record Result(int status, Set<String> ids, String body) {
	}

	private Result get(String theRelativeUrl) throws Exception {
		HttpRequest request = HttpRequest.newBuilder(URI.create(myServerBase + "/" + theRelativeUrl))
			.header(Constants.HEADER_CACHE_CONTROL, Constants.CACHE_CONTROL_NO_CACHE)
			.header(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW)
			.GET()
			.build();
		HttpResponse<String> response = myHttpClient.send(request, HttpResponse.BodyHandlers.ofString());
		Set<String> ids = new TreeSet<>();
		if (response.statusCode() == 200) {
			Bundle bundle = myFhirContext.newJsonParser().parseResource(Bundle.class, response.body());
			bundle.getEntry().forEach(e -> ids.add(e.getResource().getIdElement().toUnqualifiedVersionless().getValue()));
		}
		return new Result(response.statusCode(), ids, response.body());
	}

	private String diagnostics(Result theResult) {
		return myFhirContext.newJsonParser().parseResource(OperationOutcome.class, theResult.body()).getIssueFirstRep().getDiagnostics();
	}

	private void assertSearch(String theResourceType, long[] theTimes, List<String> theIds, String[][] theAndOr) throws Exception {
		String query = toQuery(Constants.PARAM_LASTUPDATED, theTimes, theAndOr);
		Result result = get(theResourceType + "?" + query + "&_count=50");
		Set<String> expected = expected(theTimes, theIds, theAndOr);
		if (expected == null) {
			assertThat(result.status()).as(query).isEqualTo(400);
			assertThat(diagnostics(result)).as(query).startsWith("HAPI-1925: ");
		} else {
			assertThat(result.status()).as(query + "\n" + result.body()).isEqualTo(200);
			assertThat(result.ids()).as(query).isEqualTo(expected);
		}
	}

	/**
	 * A single lower bound after a single upper bound (e.g. <code>_lastUpdated=lt[T2]&amp;_lastUpdated=gt[T4]</code>)
	 * is still a plain range, which is rejected with HAPI-1932 when the search is stored, as before.
	 * See {@link #testSearch_InvertedRange_ReturnsEmptyBundle()}.
	 */
	private static boolean isInvertedRange(String[][] theAndOr) {
		if (theAndOr.length != 2 || theAndOr[0].length != 1 || theAndOr[1].length != 1) {
			return false;
		}
		String upper = theAndOr[0][0];
		String lower = theAndOr[1][0];
		return List.of("lt", "le", "eb").contains(upper.substring(0, 2))
			&& List.of("gt", "ge", "sa").contains(lower.substring(0, 2))
			&& Integer.parseInt(lower.substring(2)) > Integer.parseInt(upper.substring(2));
	}

	@Test
	public void testSearch_AndMatrix_EveryPrefixPair() throws Exception {
		for (String first : PREFIXES) {
			for (String second : PREFIXES) {
				String[][] expression = {{first + "2"}, {second + "4"}};
				if (isInvertedRange(expression)) {
					continue;
				}
				assertSearch("Observation", myObservationTimes, myObservationIds, expression);
			}
		}
	}

	/**
	 * Not changed by hapi-fhir#8857: a plain range whose lower bound is after its upper bound is accepted by
	 * the parser, gives an empty result in a synchronous search, but fails with HAPI-1932 when the search
	 * is stored (Search#getLastUpdated). To be reported separately.
	 */
	@Test
	@Disabled("Existing behaviour, not changed by hapi-fhir#8857: to be reported separately")
	public void testSearch_InvertedRange_ReturnsEmptyBundle() throws Exception {
		for (String upper : List.of("lt", "le", "eb")) {
			for (String lower : List.of("gt", "ge", "sa")) {
				assertSearch("Observation", myObservationTimes, myObservationIds, new String[][]{{upper + "2"}, {lower + "4"}});
			}
		}
	}

	@Test
	public void testSearch_OrMatrix_EveryPrefixPair() throws Exception {
		for (String first : PREFIXES) {
			for (String second : PREFIXES) {
				assertSearch("Observation", myObservationTimes, myObservationIds, new String[][]{{first + "2", second + "4"}});
			}
		}
	}

	@Test
	public void testSearch_AndOrMixed() throws Exception {
		String[][][] expressions = {
			{{"ne1", "eq2"}},
			{{"eq2", "ne1"}},
			{{"ne2", "ne4"}},
			{{"lt2", "gt3"}, {"lt4", "gt5"}},
			{{"ne2", "eq3"}, {"ge2"}},
			{{"eq1", "eq2", "eq3"}, {"eq3", "eq4", "eq5"}},
			{{"gt1"}, {"gt2"}, {"lt5"}},
			{{"ge2"}, {"le4"}, {"ne3"}},
			{{"ne1"}, {"ne3"}, {"ne5"}},
			{{"sa1", "eb1"}, {"ne5"}},
			// Empty intersections
			{{"lt2", "lt1"}, {"gt4"}},
			{{"eq1"}, {"eq2"}},
		};
		for (String[][] next : expressions) {
			assertSearch("Observation", myObservationTimes, myObservationIds, next);
		}
	}

	@Test
	public void testSearch_EmptyIntersection_ReturnsEmptyBundle() throws Exception {
		String t2 = encode(format(myObservationTimes[1], "UTC"));
		String t4 = encode(format(myObservationTimes[3], "UTC"));
		Result result = get("Observation?_lastUpdated=lt" + t2 + "&_lastUpdated=gt" + t4 + "&_lastUpdated=ne" + t4);
		assertThat(result.status()).isEqualTo(200);
		assertThat(result.ids()).isEmpty();

		result = get("Observation?_lastUpdated=lt" + t2 + "," + "eq" + t2 + "&_lastUpdated=gt" + t4);
		assertThat(result.status()).isEqualTo(200);
		assertThat(result.ids()).isEmpty();
	}

	@Test
	public void testSearch_Has_OrKeepsEveryPrefix() throws Exception {
		// Each patient Pn has one observation On
		String[][][] expressions = {
			{{"ne1", "eq2"}},
			{{"ne2", "ne4"}},
			{{"lt2", "gt4"}},
			{{"eq3", "ne3"}},
		};
		for (String[][] next : expressions) {
			String query = toQuery("_has:Observation:subject:_lastUpdated", myObservationTimes, next);
			Result result = get("Patient?" + query);
			assertThat(result.status()).as(query + "\n" + result.body()).isEqualTo(200);
			assertThat(result.ids()).as(query).isEqualTo(expected(myObservationTimes, myPatientIds, next));
		}

		// Repetition: each _has is matched on its own (a patient with an observation matching the first one,
		// and an observation matching the second one)
		String query = toQuery("_has:Observation:subject:_lastUpdated", myObservationTimes, new String[][]{{"gt1"}, {"lt5"}});
		Result result = get("Patient?" + query);
		assertThat(result.status()).isEqualTo(200);
		assertThat(result.ids()).isEqualTo(expected(myObservationTimes, myPatientIds, new String[][]{{"gt1"}, {"lt5"}}));
	}

	@Test
	public void testSearch_Paging() throws Exception {
		String[][] expression = {{"ne3", "eq3"}, {"ne1"}};
		Set<String> expected = expected(myObservationTimes, myObservationIds, expression);
		assertThat(expected).hasSize(4);

		Bundle page = myClient
			.search()
			.byUrl("Observation?" + toQuery(Constants.PARAM_LASTUPDATED, myObservationTimes, expression) + "&_count=1")
			.returnBundle(Bundle.class)
			.execute();
		List<String> ids = new ArrayList<>();
		int pages = 0;
		while (true) {
			pages++;
			page.getEntry().forEach(e -> ids.add(e.getResource().getIdElement().toUnqualifiedVersionless().getValue()));
			if (page.getLink(Bundle.LINK_NEXT) == null) {
				break;
			}
			page = myClient.loadPage().next(page).execute();
		}
		assertThat(pages).isGreaterThanOrEqualTo(4);
		assertThat(ids).hasSize(4);
		assertThat(new HashSet<>(ids)).isEqualTo(expected);
	}

	@Test
	public void testSearch_IncludeAndRevInclude() throws Exception {
		String[][] expression = {{"lt2", "gt4"}};

		Result result = get("Observation?" + toQuery(Constants.PARAM_LASTUPDATED, myObservationTimes, expression) + "&_include=Observation:subject");
		assertThat(result.status()).isEqualTo(200);
		Set<String> expected = new TreeSet<>(expected(myObservationTimes, myObservationIds, expression));
		expected.add(myPatientIds.get(0));
		expected.add(myPatientIds.get(4));
		assertThat(result.ids()).isEqualTo(expected);

		result = get("Patient?" + toQuery(Constants.PARAM_LASTUPDATED, myPatientTimes, expression) + "&_revinclude=Observation:subject");
		assertThat(result.status()).isEqualTo(200);
		expected = new TreeSet<>(expected(myPatientTimes, myPatientIds, expression));
		expected.add(myObservationIds.get(0));
		expected.add(myObservationIds.get(4));
		assertThat(result.ids()).isEqualTo(expected);
	}

	@Test
	public void testSearch_TimezoneOffsets() throws Exception {
		String t1Minus4 = format(myObservationTimes[0], "GMT-04:00");
		String t3Plus3 = format(myObservationTimes[2], "GMT+03:00");
		assertThat(t1Minus4).endsWith("-04:00");
		assertThat(t3Plus3).endsWith("+03:00");
		Set<String> expected = Set.of(myObservationIds.get(0), myObservationIds.get(2));

		// Escaped
		Result result = get("Observation?_lastUpdated=eq" + encode(t3Plus3) + ",eq" + encode(t1Minus4));
		assertThat(result.status()).as(result.body()).isEqualTo(200);
		assertThat(result.ids()).isEqualTo(expected);

		// Not escaped: the '+' is decoded as a space
		result = get("Observation?_lastUpdated=eq" + t3Plus3 + ",eq" + t1Minus4);
		assertThat(result.status()).as(result.body()).isEqualTo(200);
		assertThat(result.ids()).isEqualTo(expected);

		// Same thing for a single value (a plain range), as before
		result = get("Observation?_lastUpdated=eq" + t3Plus3);
		assertThat(result.status()).as(result.body()).isEqualTo(200);
		assertThat(result.ids()).containsExactly(myObservationIds.get(2));
		result = get("Observation?_lastUpdated=ne" + t3Plus3 + "&_lastUpdated=ne" + t1Minus4);
		assertThat(result.status()).as(result.body()).isEqualTo(200);
		assertThat(result.ids()).containsExactlyInAnyOrder(myObservationIds.get(1), myObservationIds.get(3), myObservationIds.get(4));
	}

	@Test
	public void testBulkExport_TypeFilterWithAndOr_RejectedBeforeTheJobIsCreated() throws Exception {
		String t2 = format(myPatientTimes[1], "UTC");
		String t4 = format(myPatientTimes[3], "UTC");
		long jobsBefore = myJobInstanceRepository.count();

		String typeFilter = "Patient?_lastUpdated=lt" + t2 + ",gt" + t4;
		HttpRequest request = HttpRequest.newBuilder(URI.create(myServerBase + "/$export?_type=Patient&_typeFilter=" + encode(typeFilter)))
			.header(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.header(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW)
			.GET()
			.build();
		HttpResponse<String> response = myHttpClient.send(request, HttpResponse.BodyHandlers.ofString());
		assertThat(response.statusCode()).as(response.body()).isEqualTo(400);
		assertThat(response.body()).contains(Constants.PARAM_LASTUPDATED);
		assertThat(myJobInstanceRepository.count()).isEqualTo(jobsBefore);

		// A range is accepted, as before
		typeFilter = "Patient?_lastUpdated=ge" + t2 + "&_lastUpdated=le" + t4;
		request = HttpRequest.newBuilder(URI.create(myServerBase + "/$export?_type=Patient&_typeFilter=" + encode(typeFilter)))
			.header(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.header(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW)
			.GET()
			.build();
		response = myHttpClient.send(request, HttpResponse.BodyHandlers.ofString());
		assertThat(response.statusCode()).as(response.body()).isEqualTo(202);
		assertThat(myJobInstanceRepository.count()).isEqualTo(jobsBefore + 1);
	}

	private int startExport(String theTypeFilter) throws Exception {
		HttpRequest request = HttpRequest.newBuilder(URI.create(myServerBase + "/$export?_type=Patient,Observation&_typeFilter=" + encode(theTypeFilter)))
			.header(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.header(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW)
			.GET()
			.build();
		return myHttpClient.send(request, HttpResponse.BodyHandlers.ofString()).statusCode();
	}

	@Test
	public void testBulkExport_TypeFilterWithEncodedParameterName_RejectedBeforeTheJobIsCreated() throws Exception {
		long jobsBefore = myJobInstanceRepository.count();
		assertThat(startExport("Patient?%5FlastUpdated=lt2020,gt2021")).isEqualTo(400);
		assertThat(startExport("Patient?_lastUpdated=gt2020&%5FlastUpdated=gt2021")).isEqualTo(400);
		assertThat(myJobInstanceRepository.count()).isEqualTo(jobsBefore);
	}

	@Test
	public void testBulkExport_TypeFilterWithLastUpdatedOnlyInHasOrChain_NotRejected() throws Exception {
		long jobsBefore = myJobInstanceRepository.count();
		assertThat(startExport("Patient?_has:Observation:subject:_lastUpdated=lt2020,gt2021")).isEqualTo(202);
		assertThat(startExport("Observation?subject._lastUpdated=lt2020,gt2021")).isEqualTo(202);
		assertThat(myJobInstanceRepository.count()).isEqualTo(jobsBefore + 2);
	}
}
