package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorR4;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FulltextSearchSvcImplTest {
	@InjectMocks
	FulltextSearchSvcImpl mySvc;

	@Mock
	private ISearchParamExtractor searchParamExtractor;

	@Test
	void parseSearchParamTextStuff() {
		//Given
		String SP_NAME = "code";
		String SP_TEXT_VALUE = "Hello I am some text";
		CodeableConcept codeableConcept = new CodeableConcept().setText(SP_TEXT_VALUE);
		when(searchParamExtractor.extractValues(any(), any())).thenReturn(Collections.singletonList(codeableConcept));

		Observation obs = new Observation();
		obs.setCode(codeableConcept);

		//When
		Map<String, String> stringStringMap = mySvc.extractLuceneIndexData(FhirContext.forR4Cached(), obs).getMap();

		//Then
		assertThat(stringStringMap.keySet(), hasItem(SP_NAME));
		assertThat(stringStringMap.get(SP_NAME), is(equalTo(SP_TEXT_VALUE)));
	}
}
