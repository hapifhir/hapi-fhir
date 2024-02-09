package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.jpa.nickname.INicknameSvc;
import ca.uhn.fhir.jpa.nickname.NicknameSvc;
import ca.uhn.fhir.mdm.rules.json.MdmMatcherJson;
import ca.uhn.fhir.mdm.rules.matcher.fieldmatchers.NicknameMatcher;
import ca.uhn.fhir.mdm.rules.matcher.models.IMdmFieldMatcher;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NicknameMatcherTest {
	IMdmFieldMatcher matcher;

	INicknameSvc myNicknameSvc = new NicknameSvc();

	@BeforeEach
	public void begin() {
		matcher = new NicknameMatcher(myNicknameSvc);
	}

	@Test
	public void testMatches() {
		assertThat(match("Ken", "ken")).isTrue();
		assertThat(match("ken", "Ken")).isTrue();
		assertThat(match("Ken", "Ken")).isTrue();
		assertThat(match("Kenneth", "Ken")).isTrue();
		assertThat(match("Kenneth", "Kenny")).isTrue();
		assertThat(match("Ken", "Kenneth")).isTrue();
		assertThat(match("Kenny", "Kenneth")).isTrue();
		assertThat(match("Jim", "Jimmy")).isTrue();
		assertThat(match("Jimmy", "Jim")).isTrue();
		assertThat(match("Jim", "James")).isTrue();
		assertThat(match("Jimmy", "James")).isTrue();
		assertThat(match("James", "Jimmy")).isTrue();
		assertThat(match("James", "Jim")).isTrue();

		assertThat(match("Ken", "Bob")).isFalse();
		// These aren't nickname matches.  If you want matches like these use a phonetic matcher
		assertThat(match("Allen", "Allan")).isFalse();
	}

	private boolean match(String theFirst, String theSecond) {
		MdmMatcherJson json = new MdmMatcherJson();
		json.setExact(true);
		return matcher.matches(new StringType(theFirst), new StringType(theSecond), json);
	}
}
