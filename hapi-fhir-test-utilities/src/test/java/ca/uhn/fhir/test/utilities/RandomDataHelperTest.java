package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

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
		assertThat(object.myInt, not(equalTo(0)));
		assertThat(object.myBoxedLong, notNullValue());
		assertThat(object.myDate, notNullValue());
		assertThat(object.myUUID, notNullValue());
		assertThat(object.myEnum, notNullValue());
	}

}
