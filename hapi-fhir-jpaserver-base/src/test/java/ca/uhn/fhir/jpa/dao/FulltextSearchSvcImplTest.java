package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

class FulltextSearchSvcImplTest {

	@Test
	void parseSearchParamTextStuff() {
		//Given
		String SP_NAME = "code";
		String SP_TEXT_VALUE = "Hello I am some text";
		Observation obs = new Observation();
		obs.setCode(new CodeableConcept().setText(SP_TEXT_VALUE));

		//When
		Map<String, String> stringStringMap = FulltextSearchSvcImpl.parseSearchParamTextStuff(FhirContext.forR4Cached(), obs);

		//Then
		assertThat(stringStringMap.keySet(), hasItem(SP_NAME));
		assertThat(stringStringMap.get(SP_NAME), is(equalTo(SP_TEXT_VALUE)));
	}
}
