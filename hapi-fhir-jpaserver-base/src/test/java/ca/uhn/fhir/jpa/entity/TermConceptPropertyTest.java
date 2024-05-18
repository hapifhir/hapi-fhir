package ca.uhn.fhir.jpa.entity;

import com.google.common.base.Strings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;

public class TermConceptPropertyTest {

	private static final String ourVeryLongString = Strings.repeat("a", TermConceptProperty.MAX_LENGTH+1);

	@Test
	public void testSetValue_whenValueExceedsMAX_LENGTH_willWriteToBlobAndBin(){
		// given
		TermConceptProperty termConceptProperty = new TermConceptProperty();

		// when
		termConceptProperty.setValue(ourVeryLongString);

		// then
		assertThat(termConceptProperty.hasValueBlobForTesting(), equalTo(true));
		assertThat(termConceptProperty.hasValueBinForTesting(), equalTo(true));
	}

	@Test
	public void testHasValueBin_willDefaultToAssertingValueBin(){
		// given
		TermConceptProperty termConceptProperty = new TermConceptProperty();
		termConceptProperty.setValueBinForTesting(ourVeryLongString.getBytes());
		termConceptProperty.setValueBlobForTesting(null);

		// when/then
		assertThat(termConceptProperty.hasValueBin(), is(true));

	}

	@Test
	public void testHasValueBin_willAssertValueBlob_whenValueBinNotPresent(){
		// given
		TermConceptProperty termConceptProperty = new TermConceptProperty();
		termConceptProperty.setValueBinForTesting(null);
		termConceptProperty.setValueBlobForTesting(ourVeryLongString.getBytes());

		// when/then
		assertThat(termConceptProperty.hasValueBin(), is(true));

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
		assertThat(value, startsWith("a"));

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
		assertThat(value, startsWith("a"));
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	public void testSetValue_withSupportLegacyLob(boolean theSupportLegacyLob){
		// given
		TermConceptProperty termConceptProperty = new TermConceptProperty();

		// when
		termConceptProperty.setValue(ourVeryLongString);
		termConceptProperty.performLegacyLobSupport(theSupportLegacyLob);

		// then
		assertThat(termConceptProperty.hasValueBinForTesting(), equalTo(true));
		assertThat(termConceptProperty.hasValueBlobForTesting(), equalTo(theSupportLegacyLob));
	}

}
