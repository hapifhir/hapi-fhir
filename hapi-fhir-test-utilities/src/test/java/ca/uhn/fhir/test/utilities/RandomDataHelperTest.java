package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

		assertThat(object.myString).isNotBlank();
		assertThat(object.myInt).isNotEqualTo(0);
		assertNotNull(object.myBoxedLong);
		assertNotNull(object.myDate);
		assertNotNull(object.myUUID);
		assertNotNull(object.myEnum);
	}

}
