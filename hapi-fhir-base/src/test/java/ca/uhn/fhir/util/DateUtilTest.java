package ca.uhn.fhir.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

public class DateUtilTest {

	@Test
	public void testCompletedDate() {
		
		Pair<String, String> result = DateUtils.getCompletedDate(null);
		assertThat(result.getLeft()).isNull();
		assertThat(result.getRight()).isNull();	
		
		result = DateUtils.getCompletedDate("2020");
		assertThat(result.getLeft()).isEqualTo("2020-01-01");
		assertThat(result.getRight()).isEqualTo("2020-12-31");
		
		result = DateUtils.getCompletedDate("202001a");
		assertThat(result.getLeft()).isEqualTo("202001a");
		assertThat(result.getRight()).isEqualTo("202001a");
		
		result = DateUtils.getCompletedDate("202001");
		assertThat(result.getLeft()).isEqualTo("202001");
		assertThat(result.getRight()).isEqualTo("202001");
		
		result = DateUtils.getCompletedDate("2020-01");
		assertThat(result.getLeft()).isEqualTo("2020-01-01");
		assertThat(result.getRight()).isEqualTo("2020-01-31");
		
		result = DateUtils.getCompletedDate("2020-02");
		assertThat(result.getLeft()).isEqualTo("2020-02-01");
		assertThat(result.getRight()).isEqualTo("2020-02-29");
		
		result = DateUtils.getCompletedDate("2021-02");
		assertThat(result.getLeft()).isEqualTo("2021-02-01");
		assertThat(result.getRight()).isEqualTo("2021-02-28");
		
		result = DateUtils.getCompletedDate("2020-04");
		assertThat(result.getLeft()).isEqualTo("2020-04-01");
		assertThat(result.getRight()).isEqualTo("2020-04-30");
		
		result = DateUtils.getCompletedDate("2020-05-16");
		assertThat(result.getLeft()).isEqualTo("2020-05-16");
		assertThat(result.getRight()).isEqualTo("2020-05-16");
	}
}
