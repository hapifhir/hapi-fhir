package ca.uhn.fhir.jpa.entity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.google.common.base.Strings;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TermConceptPropertyTest {

	private static final String ourVeryLongString = Strings.repeat("a", TermConceptProperty.MAX_LENGTH+1);

	@Test
	public void testSetValue_whenValueExceedsMAX_LENGTH_willWriteToBlobAndBin(){
		// given
		TermConceptProperty termConceptProperty = new TermConceptProperty();

		// when
		termConceptProperty.setValue(ourVeryLongString);

		// then
		assertNotNull(termConceptProperty.getValueBlobForTesting());
		assertNotNull(termConceptProperty.getValueBinForTesting());
	}

	@Test
	public void testHasValueBin_willDefaultToAssertingValueBin(){
		// given
		TermConceptProperty termConceptProperty = new TermConceptProperty();
		termConceptProperty.setValueBinForTesting(ourVeryLongString.getBytes());
		termConceptProperty.setValueBlobForTesting(null);

		// when/then
		assertTrue(termConceptProperty.hasValueBin());

	}

	@Test
	public void testHasValueBin_willAssertValueBlob_whenValueBinNotPresent(){
		// given
		TermConceptProperty termConceptProperty = new TermConceptProperty();
		termConceptProperty.setValueBinForTesting(null);
		termConceptProperty.setValueBlobForTesting(ourVeryLongString.getBytes());

		// when/then
		assertTrue(termConceptProperty.hasValueBin());

	}

	@Test
	public void testGetValue_whenValueExceedsMAX_LENGTH_willGetValueBinByDefault(){
		// given
		TermConceptProperty termConceptProperty = new TermConceptProperty();
		termConceptProperty.setValueBinForTesting(ourVeryLongString.getBytes());
		termConceptProperty.setValueBlobForTesting(null);

		// when
		String value = termConceptProperty.getValue();

		// then
		assertThat(value).startsWith("a");

	}

	@Test
	public void testGetValue_whenOnlyValueBlobIsSet_willGetValueValueBlob(){
		// given
		TermConceptProperty termConceptProperty = new TermConceptProperty();
		termConceptProperty.setValueBinForTesting(null);
		termConceptProperty.setValueBlobForTesting(ourVeryLongString.getBytes());

		// when
		String value = termConceptProperty.getValue();

		// then
		assertThat(value).startsWith("a");
	}

}
