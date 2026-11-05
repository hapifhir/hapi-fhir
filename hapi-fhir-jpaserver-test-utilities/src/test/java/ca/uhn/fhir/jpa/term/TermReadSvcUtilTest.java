package ca.uhn.fhir.jpa.term;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TermReadSvcUtilTest {

	// Created by Claude Opus 5.5
	@ParameterizedTest
	@CsvSource(textBlock = """
		# url                                   , expected value set id
		http://boinc.org                        ,
		http://boing.org                        ,
		http://loinc.org                        ,
		http://loinc.org/vs-something-else.ar   ,
		http://loinc.org/vs-no-slash-after-vs   ,
		http://loinc.org/vs/                    ,
		http://loinc.org/vs                     , loinc-all
		http://loinc.org/vs/radiology-playbook  , radiology-playbook
		http://loinc.org/vs/some-vs-id          , some-vs-id
		""")
	void getValueSetId_url_returnsExpectedId(String theUrl, String theExpectedId) {
		assertThat(TermReadSvcUtil.getValueSetId(theUrl)).isEqualTo(Optional.ofNullable(theExpectedId));
	}

	// Created by Claude Opus 5.5
	@ParameterizedTest
	@CsvSource(textBlock = """
		# url                                          , expected
		http://loin-c.org                              , false
		http://boing.org                               , false
		http://boing.org|v2.68                         , false
		http://loinc.org|v2.68                         , false
		http://loinc.org%7Cv2.68                       , false
		http://loinc.org/vs/radiology-playbook|v2.68   , false
		http://loinc.org/vs/radiology-playbook%7Cv2.68 , false
		http://loinc.org                               , true
		http://LOINC.org                               , true
		http://anything-plus-loinc.org                 , true
		http://loinc.org|                              , true
		http://loinc.org/vs                            , true
		http://loinc.org/vs/radiology-playbook         , true
		http://loinc.org/vs/radiology-playbook|        , true
		""")
	void isLoincUnversioned_url_returnsExpected(String theUrl, boolean theExpected) {
		assertThat(TermReadSvcUtil.isLoincUnversioned(theUrl)).isEqualTo(theExpected);
	}

}
