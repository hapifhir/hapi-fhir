package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.jpa.nickname.NicknameSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.rules.matcher.models.IMdmFieldMatcher;
import ca.uhn.fhir.mdm.rules.matcher.models.MatchTypeEnum;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.Nonnull;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class StringMatcherR4Test extends BaseMatcherR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(StringMatcherR4Test.class);
	public static final String LEFT_NAME = "namadega";
	public static final String RIGHT_NAME = "namaedga";

	private IMatcherFactory myIMatcherFactory;

	private IMdmSettings myMdmSettings;

	@BeforeEach
	public void before() {
		super.before();

		myMdmSettings = mock(IMdmSettings.class);

		myIMatcherFactory = new MdmMatcherFactory(
			ourFhirContext,
			myMdmSettings,
			new NicknameSvc()
		);
	}

	private @Nonnull IMdmFieldMatcher getFieldMatcher(MatchTypeEnum theMatchTypeEnum) {
		return myIMatcherFactory.getFieldMatcherForMatchType(theMatchTypeEnum);
	}

	@Test
	public void testNamadega() {
		String left = LEFT_NAME;
		String right = RIGHT_NAME;
		assertThat(match(MatchTypeEnum.COLOGNE, left, right)).isTrue();
		assertThat(match(MatchTypeEnum.DOUBLE_METAPHONE, left, right)).isTrue();
		assertThat(match(MatchTypeEnum.MATCH_RATING_APPROACH, left, right)).isTrue();
		assertThat(match(MatchTypeEnum.METAPHONE, left, right)).isTrue();
		assertThat(match(MatchTypeEnum.SOUNDEX, left, right)).isTrue();

		assertThat(match(MatchTypeEnum.CAVERPHONE1, left, right)).isFalse();
		assertThat(match(MatchTypeEnum.CAVERPHONE2, left, right)).isFalse();
		assertThat(match(MatchTypeEnum.NYSIIS, left, right)).isFalse();
		assertThat(match(MatchTypeEnum.REFINED_SOUNDEX, left, right)).isFalse();
		assertThat(match(MatchTypeEnum.STRING, left, right)).isFalse();
		assertThat(match(MatchTypeEnum.SUBSTRING, left, right)).isFalse();
	}

	@Test
	public void testNumeric() {
		assertThat(match(MatchTypeEnum.NUMERIC, "4169671111", "(416) 967-1111")).isTrue();
		assertThat(match(MatchTypeEnum.NUMERIC, "5169671111", "(416) 967-1111")).isFalse();
		assertThat(match(MatchTypeEnum.NUMERIC, "4169671111", "(416) 967-1111x123")).isFalse();
	}

	@Test
	public void testMetaphone() {
		assertThat(match(MatchTypeEnum.METAPHONE, "Durie", "dury")).isTrue();
		assertThat(match(MatchTypeEnum.METAPHONE, "Balo", "ballo")).isTrue();
		assertThat(match(MatchTypeEnum.METAPHONE, "Hans Peter", "Hanspeter")).isTrue();
		assertThat(match(MatchTypeEnum.METAPHONE, "Lawson", "Law son")).isTrue();

		assertThat(match(MatchTypeEnum.METAPHONE, "Allsop", "Allsob")).isFalse();
		assertThat(match(MatchTypeEnum.METAPHONE, "Gevne", "Geve")).isFalse();
		assertThat(match(MatchTypeEnum.METAPHONE, "Bruce", "Bruch")).isFalse();
		assertThat(match(MatchTypeEnum.METAPHONE, "Smith", "Schmidt")).isFalse();
		assertThat(match(MatchTypeEnum.METAPHONE, "Jyothi", "Jyoti")).isFalse();
	}

	@Test
	public void testDoubleMetaphone() {
		assertThat(match(MatchTypeEnum.DOUBLE_METAPHONE, "Durie", "dury")).isTrue();
		assertThat(match(MatchTypeEnum.DOUBLE_METAPHONE, "Balo", "ballo")).isTrue();
		assertThat(match(MatchTypeEnum.DOUBLE_METAPHONE, "Hans Peter", "Hanspeter")).isTrue();
		assertThat(match(MatchTypeEnum.DOUBLE_METAPHONE, "Lawson", "Law son")).isTrue();
		assertThat(match(MatchTypeEnum.DOUBLE_METAPHONE, "Allsop", "Allsob")).isTrue();

		assertThat(match(MatchTypeEnum.DOUBLE_METAPHONE, "Gevne", "Geve")).isFalse();
		assertThat(match(MatchTypeEnum.DOUBLE_METAPHONE, "Bruce", "Bruch")).isFalse();
		assertThat(match(MatchTypeEnum.DOUBLE_METAPHONE, "Smith", "Schmidt")).isFalse();
		assertThat(match(MatchTypeEnum.DOUBLE_METAPHONE, "Jyothi", "Jyoti")).isFalse();
	}

	@Test
	public void testNormalizeCase() {
		assertThat(match(MatchTypeEnum.STRING, "joe", "JoE")).isTrue();
		assertThat(match(MatchTypeEnum.STRING, "MCTAVISH", "McTavish")).isTrue();

		assertThat(match(MatchTypeEnum.STRING, "joey", "joe")).isFalse();
		assertThat(match(MatchTypeEnum.STRING, "joe", "joey")).isFalse();
	}

	@Test
	public void testExactString() {
		myMdmMatcherJson.setExact(true);

		assertThat(getFieldMatcher(MatchTypeEnum.STRING).matches(new StringType("Jilly"), new StringType("Jilly"), myMdmMatcherJson)).isTrue();

		assertThat(getFieldMatcher(MatchTypeEnum.STRING).matches(new StringType("MCTAVISH"), new StringType("McTavish"), myMdmMatcherJson)).isFalse();
		assertThat(getFieldMatcher(MatchTypeEnum.STRING).matches(new StringType("Durie"), new StringType("dury"), myMdmMatcherJson)).isFalse();
	}

	@Test
	public void testExactBoolean() {
		myMdmMatcherJson.setExact(true);

		assertThat(getFieldMatcher(MatchTypeEnum.STRING).matches(new BooleanType(true), new BooleanType(true), myMdmMatcherJson)).isTrue();

		assertThat(getFieldMatcher(MatchTypeEnum.STRING).matches(new BooleanType(true), new BooleanType(false), myMdmMatcherJson)).isFalse();
		assertThat(getFieldMatcher(MatchTypeEnum.STRING).matches(new BooleanType(false), new BooleanType(true), myMdmMatcherJson)).isFalse();
	}

	@Test
	public void testExactDateString() {
		myMdmMatcherJson.setExact(true);

		assertThat(getFieldMatcher(MatchTypeEnum.STRING).matches(new DateType("1965-08-09"), new DateType("1965-08-09"), myMdmMatcherJson)).isTrue();

		assertThat(getFieldMatcher(MatchTypeEnum.STRING).matches(new DateType("1965-08-09"), new DateType("1965-09-08"), myMdmMatcherJson)).isFalse();
	}


	@Test
	public void testExactGender() {
		Enumeration<Enumerations.AdministrativeGender> male = new Enumeration<Enumerations.AdministrativeGender>(new Enumerations.AdministrativeGenderEnumFactory());
		male.setValue(Enumerations.AdministrativeGender.MALE);

		Enumeration<Enumerations.AdministrativeGender> female = new Enumeration<Enumerations.AdministrativeGender>(new Enumerations.AdministrativeGenderEnumFactory());
		female.setValue(Enumerations.AdministrativeGender.FEMALE);

		myMdmMatcherJson.setExact(true);

		assertThat(getFieldMatcher(MatchTypeEnum.STRING).matches(male, male, myMdmMatcherJson)).isTrue();

		assertThat(getFieldMatcher(MatchTypeEnum.STRING).matches(male, female, myMdmMatcherJson)).isFalse();
	}

	@Test
	public void testSoundex() {
		assertThat(match(MatchTypeEnum.SOUNDEX, "Gail", "Gale")).isTrue();
		assertThat(match(MatchTypeEnum.SOUNDEX, "John", "Jon")).isTrue();
		assertThat(match(MatchTypeEnum.SOUNDEX, "Thom", "Tom")).isTrue();

		assertThat(match(MatchTypeEnum.SOUNDEX, "Fred", "Frank")).isFalse();
		assertThat(match(MatchTypeEnum.SOUNDEX, "Thomas", "Tom")).isFalse();
	}


	@Test
	public void testCaverphone1() {
		assertThat(match(MatchTypeEnum.CAVERPHONE1, "Gail", "Gael")).isTrue();
		assertThat(match(MatchTypeEnum.CAVERPHONE1, "John", "Jon")).isTrue();

		assertThat(match(MatchTypeEnum.CAVERPHONE1, "Gail", "Gale")).isFalse();
		assertThat(match(MatchTypeEnum.CAVERPHONE1, "Fred", "Frank")).isFalse();
		assertThat(match(MatchTypeEnum.CAVERPHONE1, "Thomas", "Tom")).isFalse();
	}

	@Test
	public void testCaverphone2() {
		assertThat(match(MatchTypeEnum.CAVERPHONE2, "Gail", "Gael")).isTrue();
		assertThat(match(MatchTypeEnum.CAVERPHONE2, "John", "Jon")).isTrue();
		assertThat(match(MatchTypeEnum.CAVERPHONE2, "Gail", "Gale")).isTrue();

		assertThat(match(MatchTypeEnum.CAVERPHONE2, "Fred", "Frank")).isFalse();
		assertThat(match(MatchTypeEnum.CAVERPHONE2, "Thomas", "Tom")).isFalse();
	}

	@Test
	public void testNormalizeSubstring() {
		assertThat(match(MatchTypeEnum.SUBSTRING, "BILLY", "Bill")).isTrue();
		assertThat(match(MatchTypeEnum.SUBSTRING, "Bill", "Billy")).isTrue();
		assertThat(match(MatchTypeEnum.SUBSTRING, "FRED", "Frederik")).isTrue();

		assertThat(match(MatchTypeEnum.SUBSTRING, "Fred", "Friederik")).isFalse();
	}

	private boolean match(MatchTypeEnum theMatcher, String theLeft, String theRight) {
		return getFieldMatcher(theMatcher)
			.matches(new StringType(theLeft), new StringType(theRight), myMdmMatcherJson);
	}
}
