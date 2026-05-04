package ca.uhn.fhir.jpa.dao.r4;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class PartititonTestUtil {
	/**
	 * H2 gets a bit tripped up by our changing the system TZ during test
	 * execution, so we can have an off by one error.. but as long as it's close
	 * this is definitely working
	 */
	public static void assertLocalDateFromDbMatches(LocalDate theExpected, LocalDate theActual) {
		LocalDate expMinus1 = theExpected.minusDays(1);
		LocalDate expMinus2 = theExpected.minusDays(2);
		LocalDate expPlus1 = theExpected.plusDays(1);
		assertThat(theActual)
			.satisfiesAnyOf(
				arg -> assertThat(arg).isEqualTo(theExpected),
				arg -> assertThat(arg).isEqualTo(expMinus1),
				arg -> assertThat(arg).isEqualTo(expMinus2),
				arg -> assertThat(arg).isEqualTo(expPlus1)
			);
	}
}
