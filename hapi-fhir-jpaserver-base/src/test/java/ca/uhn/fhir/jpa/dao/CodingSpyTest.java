package ca.uhn.fhir.jpa.dao;

import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Coding;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import jakarta.annotation.Nonnull;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CodingSpyTest {

	/**
	 * Ensure we can read the default null value of userSelected on Coding
	 */

	@ParameterizedTest
	@MethodSource("getCases")
	void canReadValueUserSelected(IBaseCoding theObject, Boolean theValue)  {
		IBaseCoding value = theObject.setSystem("http://example.com").setCode("value");
		if (theValue != null) {
			theObject.setUserSelected(theValue);
		}

		Boolean result = new CodingSpy().getBooleanObject(theObject);

		assertThat(result).isEqualTo(theValue);
	}

	@Test
	void canReadNullUserSelected()  {
		Coding value = new Coding().setSystem("http://example.com").setCode("value");

		Boolean result = new CodingSpy().getBooleanObject(value);

		assertThat(result).isNull();
	}

	static List<Arguments> getCases() {
		var classes = List.of(
			org.hl7.fhir.r4.model.Coding.class,
			org.hl7.fhir.r5.model.Coding.class,
			ca.uhn.fhir.model.api.Tag.class
		);
		var values = Lists.newArrayList(true, false, null);
		return classes.stream()
			.flatMap(k-> values.stream()
				.map(v-> Arguments.of(getNewInstance(k), v)))
			.toList();
	}

	@Nonnull
	private static Object getNewInstance(Class<? extends IBaseCoding> k) {
		try {
			return k.getDeclaredConstructor().newInstance();
		} catch (Exception theE) {
			throw new RuntimeException(theE);
		}
	}


	@Test
	void booleanNulls() {
	    // given
		BooleanType b = new BooleanType();

	    // when
		var s = b.asStringValue();

		assertThat(s).isNullOrEmpty();
	}


}
