package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.not;

class RandomDataHelperTest {

	static class TestClass {
		String myString;
		int myInt;
		Long myBoxedLong;
		Date myDate;
		UUID myUUID;
		TemporalPrecisionEnum myEnum;
	}

	@Test
	void fillFieldsRandomly() {
		TestClass object = new TestClass();

		RandomDataHelper.fillFieldsRandomly(object);

		assertThat(object.myString, not(blankOrNullString()));
		assertThat(object.myInt).isNotEqualTo(0);
		assertThat(object.myBoxedLong).isNotNull();
		assertThat(object.myDate).isNotNull();
		assertThat(object.myUUID).isNotNull();
		assertThat(object.myEnum).isNotNull();
	}

}
