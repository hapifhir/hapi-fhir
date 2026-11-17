package ca.uhn.fhir.jpa.provider.r4b;

import org.hl7.fhir.r4b.model.Bundle;
import org.hl7.fhir.r4b.model.Patient;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <code>_lastUpdated</code> with AND/OR combinations over REST (hapi-fhir#8857)
 */
public class ResourceProviderR4BLastUpdatedAndOrTest extends BaseResourceProviderR4BTest {

	private static String format(Date theDate) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		f.setTimeZone(TimeZone.getTimeZone("UTC"));
		return URLEncoder.encode(f.format(theDate), StandardCharsets.UTF_8);
	}

	private List<String> search(String theQuery) {
		Bundle bundle = myClient.search().byUrl("Patient?" + theQuery + "&_count=50").returnBundle(Bundle.class).execute();
		List<String> retVal = new ArrayList<>();
		bundle.getEntry().forEach(e -> retVal.add(e.getResource().getIdElement().toUnqualifiedVersionless().getValue()));
		return retVal;
	}

	@Test
	public void testSearchLastUpdated_AndOr() throws InterruptedException {
		List<String> ids = new ArrayList<>();
		List<String> t = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Patient p = new Patient();
			p.addName().setFamily("P" + i);
			String id = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless().getValue();
			ids.add(id);
			Patient read = myClient.read().resource(Patient.class).withUrl(id).execute();
			t.add(format(read.getMeta().getLastUpdated()));
			if (i > 0) {
				assertThat(t.get(i)).isNotEqualTo(t.get(i - 1));
			}
			Thread.sleep(5);
		}

		// OR: before T2 or after T4
		assertThat(search("_lastUpdated=lt" + t.get(1) + ",gt" + t.get(3)))
			.containsExactlyInAnyOrder(ids.get(0), ids.get(4));
		// OR with "ne"
		assertThat(search("_lastUpdated=ne" + t.get(0) + ",eq" + t.get(1)))
			.containsExactlyInAnyOrder(ids.get(1), ids.get(2), ids.get(3), ids.get(4));
		// AND, more than two
		assertThat(search("_lastUpdated=gt" + t.get(0) + "&_lastUpdated=gt" + t.get(1) + "&_lastUpdated=lt" + t.get(4)))
			.containsExactlyInAnyOrder(ids.get(2), ids.get(3));
		// A range, as before
		assertThat(search("_lastUpdated=ge" + t.get(1) + "&_lastUpdated=le" + t.get(3)))
			.containsExactlyInAnyOrder(ids.get(1), ids.get(2), ids.get(3));
		assertThat(search("_lastUpdated=ne" + t.get(2)))
			.containsExactlyInAnyOrder(ids.get(0), ids.get(1), ids.get(3), ids.get(4));
	}
}
