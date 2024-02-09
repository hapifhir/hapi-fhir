package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceIndexedSearchParamDateTest {

	private Date date1A, date1B, date2A, date2B;
	private Timestamp timestamp1A, timestamp1B, timestamp2A, timestamp2B;

	@BeforeEach
	public void setUp() throws Exception {
		Calendar cal1 = Calendar.getInstance();
		cal1.set(1970, 01, 01, 10, 23, 33);

		Calendar cal2 = Calendar.getInstance();
		cal2.set(1990, 01, 01, 5, 11, 0);

		date1A = cal1.getTime();
		date1B = cal1.getTime();
		date2A = cal2.getTime();
		date2B = cal2.getTime();

		timestamp1A = new Timestamp(date1A.getTime());
		timestamp1B = new Timestamp(date1B.getTime());
		timestamp2A = new Timestamp(date2A.getTime());
		timestamp2B = new Timestamp(date2B.getTime());
	}

	@Test
	public void equalsIsTrueForMatchingNullDates() {
		ResourceIndexedSearchParamDate param = new ResourceIndexedSearchParamDate(new PartitionSettings(), "Patient", "SomeResource", null, null, null, null, "SomeValue");
		ResourceIndexedSearchParamDate param2 = new ResourceIndexedSearchParamDate(new PartitionSettings(), "Patient", "SomeResource", null, null, null, null, "SomeValue");

		assertThat(param.equals(param2)).isTrue();
		assertThat(param2.equals(param)).isTrue();
		assertThat(param2.hashCode()).isEqualTo(param.hashCode());
	}

	@Test
	public void equalsIsTrueForMatchingDates() {
		ResourceIndexedSearchParamDate param = new ResourceIndexedSearchParamDate(new PartitionSettings(), "Patient", "SomeResource", date1A, null, date2A, null, "SomeValue");
		ResourceIndexedSearchParamDate param2 = new ResourceIndexedSearchParamDate(new PartitionSettings(), "Patient", "SomeResource", date1B, null, date2B, null, "SomeValue");

		assertThat(param.equals(param2)).isTrue();
		assertThat(param2.equals(param)).isTrue();
		assertThat(param2.hashCode()).isEqualTo(param.hashCode());
	}

	@Test
	public void equalsIsTrueForMatchingTimeStampsThatMatch() {
		ResourceIndexedSearchParamDate param = new ResourceIndexedSearchParamDate(new PartitionSettings(), "Patient", "SomeResource", timestamp1A, null, timestamp2A, null, "SomeValue");
		ResourceIndexedSearchParamDate param2 = new ResourceIndexedSearchParamDate(new PartitionSettings(), "Patient", "SomeResource", timestamp1B, null, timestamp2B, null, "SomeValue");

		assertThat(param.equals(param2)).isTrue();
		assertThat(param2.equals(param)).isTrue();
		assertThat(param2.hashCode()).isEqualTo(param.hashCode());
	}

	// Scenario that occurs when updating a resource with a date search parameter. One date will be a java.util.Date, the
	// other will be equivalent but will be a java.sql.Timestamp. Equals should work in both directions.
	@Test
	public void equalsIsTrueForMixedTimestampsAndDates() {
		ResourceIndexedSearchParamDate param = new ResourceIndexedSearchParamDate(new PartitionSettings(), "Patient", "SomeResource", date1A, null, date2A, null, "SomeValue");
		ResourceIndexedSearchParamDate param2 = new ResourceIndexedSearchParamDate(new PartitionSettings(), "Patient", "SomeResource", timestamp1A, null, timestamp2A, null, "SomeValue");

		assertThat(param.equals(param2)).isTrue();
		assertThat(param2.equals(param)).isTrue();
		assertThat(param2.hashCode()).isEqualTo(param.hashCode());
	}

	@Test
	public void equalsIsFalseForNonMatchingDates() {
		ResourceIndexedSearchParamDate param = new ResourceIndexedSearchParamDate(new PartitionSettings(), "Patient", "SomeResource", date1A, null, date2A, null, "SomeValue");
		ResourceIndexedSearchParamDate param2 = new ResourceIndexedSearchParamDate(new PartitionSettings(), "Patient", "SomeResource", date2A, null, date1A, null, "SomeValue");

		assertThat(param.equals(param2)).isFalse();
		assertThat(param2.equals(param)).isFalse();
		assertThat(param2.hashCode()).isNotEqualTo(param.hashCode());
	}

	@Test
	public void equalsIsFalseForNonMatchingDatesNullCase() {
		ResourceIndexedSearchParamDate param = new ResourceIndexedSearchParamDate(new PartitionSettings(), "Patient", "SomeResource", date1A, null, date2A, null, "SomeValue");
		ResourceIndexedSearchParamDate param2 = new ResourceIndexedSearchParamDate(new PartitionSettings(), "Patient", "SomeResource", null, null, null, null, "SomeValue");

		assertThat(param.equals(param2)).isFalse();
		assertThat(param2.equals(param)).isFalse();
		assertThat(param2.hashCode()).isNotEqualTo(param.hashCode());
	}

	@Test
	public void equalsIsFalseForNonMatchingTimeStamps() {
		ResourceIndexedSearchParamDate param = new ResourceIndexedSearchParamDate(new PartitionSettings(), "Patient", "SomeResource", timestamp1A, null, timestamp2A, null, "SomeValue");
		ResourceIndexedSearchParamDate param2 = new ResourceIndexedSearchParamDate(new PartitionSettings(), "Patient", "SomeResource", timestamp2A, null, timestamp1A, null, "SomeValue");

		assertThat(param.equals(param2)).isFalse();
		assertThat(param2.equals(param)).isFalse();
		assertThat(param2.hashCode()).isNotEqualTo(param.hashCode());
	}

	@Test
	public void equalsIsFalseForMixedTimestampsAndDatesThatDoNotMatch() {
		ResourceIndexedSearchParamDate param = new ResourceIndexedSearchParamDate(new PartitionSettings(), "Patient", "SomeResource", date1A, null, date2A, null, "SomeValue");
		ResourceIndexedSearchParamDate param2 = new ResourceIndexedSearchParamDate(new PartitionSettings(), "Patient", "SomeResource", timestamp2A, null, timestamp1A, null, "SomeValue");

		assertThat(param.equals(param2)).isFalse();
		assertThat(param2.equals(param)).isFalse();
		assertThat(param2.hashCode()).isNotEqualTo(param.hashCode());
	}


	@Test
	public void testEquals() {
		ResourceIndexedSearchParamDate val1 = new ResourceIndexedSearchParamDate()
			.setValueHigh(new Date(100000000L))
			.setValueLow(new Date(111111111L));
		val1.setPartitionSettings(new PartitionSettings());
		val1.calculateHashes();
		ResourceIndexedSearchParamDate val2 = new ResourceIndexedSearchParamDate()
			.setValueHigh(new Date(100000000L))
			.setValueLow(new Date(111111111L));
		val2.setPartitionSettings(new PartitionSettings());
		val2.calculateHashes();
		assertThat(val1).isEqualTo(val1).isNotNull();
		assertThat(val2).isEqualTo(val1);
		assertThat("").isNotEqualTo(val1);
	}
}
