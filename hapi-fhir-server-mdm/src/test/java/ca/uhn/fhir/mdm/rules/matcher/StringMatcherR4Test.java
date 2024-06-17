package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.jpa.nickname.NicknameSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.rules.matcher.models.IMdmFieldMatcher;
import ca.uhn.fhir.mdm.rules.matcher.models.MatchTypeEnum;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
		assertTrue(match(MatchTypeEnum.COLOGNE, left, right));
		assertTrue(match(MatchTypeEnum.DOUBLE_METAPHONE, left, right));
		assertTrue(match(MatchTypeEnum.MATCH_RATING_APPROACH, left, right));
		assertTrue(match(MatchTypeEnum.METAPHONE, left, right));
		assertTrue(match(MatchTypeEnum.SOUNDEX, left, right));

		assertFalse(match(MatchTypeEnum.CAVERPHONE1, left, right));
		assertFalse(match(MatchTypeEnum.CAVERPHONE2, left, right));
		assertFalse(match(MatchTypeEnum.NYSIIS, left, right));
		assertFalse(match(MatchTypeEnum.REFINED_SOUNDEX, left, right));
		assertFalse(match(MatchTypeEnum.STRING, left, right));
		assertFalse(match(MatchTypeEnum.SUBSTRING, left, right));
	}

	@Test
	public void testNumeric() {
		assertTrue(match(MatchTypeEnum.NUMERIC, "4169671111", "(416) 967-1111"));
		assertFalse(match(MatchTypeEnum.NUMERIC, "5169671111", "(416) 967-1111"));
		assertFalse(match(MatchTypeEnum.NUMERIC, "4169671111", "(416) 967-1111x123"));
	}

	@Test
	public void testMetaphone() {
		assertTrue(match(MatchTypeEnum.METAPHONE, "Durie", "dury"));
		assertTrue(match(MatchTypeEnum.METAPHONE, "Balo", "ballo"));
		assertTrue(match(MatchTypeEnum.METAPHONE, "Hans Peter", "Hanspeter"));
		assertTrue(match(MatchTypeEnum.METAPHONE, "Lawson", "Law son"));

		assertFalse(match(MatchTypeEnum.METAPHONE, "Allsop", "Allsob"));
		assertFalse(match(MatchTypeEnum.METAPHONE, "Gevne", "Geve"));
		assertFalse(match(MatchTypeEnum.METAPHONE, "Bruce", "Bruch"));
		assertFalse(match(MatchTypeEnum.METAPHONE, "Smith", "Schmidt"));
		assertFalse(match(MatchTypeEnum.METAPHONE, "Jyothi", "Jyoti"));
	}

	@Test
	public void testDoubleMetaphone() {
		assertTrue(match(MatchTypeEnum.DOUBLE_METAPHONE, "Durie", "dury"));
		assertTrue(match(MatchTypeEnum.DOUBLE_METAPHONE, "Balo", "ballo"));
		assertTrue(match(MatchTypeEnum.DOUBLE_METAPHONE, "Hans Peter", "Hanspeter"));
		assertTrue(match(MatchTypeEnum.DOUBLE_METAPHONE, "Lawson", "Law son"));
		assertTrue(match(MatchTypeEnum.DOUBLE_METAPHONE, "Allsop", "Allsob"));

		assertFalse(match(MatchTypeEnum.DOUBLE_METAPHONE, "Gevne", "Geve"));
		assertFalse(match(MatchTypeEnum.DOUBLE_METAPHONE, "Bruce", "Bruch"));
		assertFalse(match(MatchTypeEnum.DOUBLE_METAPHONE, "Smith", "Schmidt"));
		assertFalse(match(MatchTypeEnum.DOUBLE_METAPHONE, "Jyothi", "Jyoti"));
	}

	@Test
	public void testNormalizeCase() {
		assertTrue(match(MatchTypeEnum.STRING, "joe", "JoE"));
		assertTrue(match(MatchTypeEnum.STRING, "MCTAVISH", "McTavish"));

		assertFalse(match(MatchTypeEnum.STRING, "joey", "joe"));
		assertFalse(match(MatchTypeEnum.STRING, "joe", "joey"));
	}

	@Test
	public void testExactString() {
		myMdmMatcherJson.setExact(true);

		assertTrue(getFieldMatcher(MatchTypeEnum.STRING).matches(new StringType("Jilly"), new StringType("Jilly"), myMdmMatcherJson));

		assertFalse(getFieldMatcher(MatchTypeEnum.STRING).matches(new StringType("MCTAVISH"), new StringType("McTavish"), myMdmMatcherJson));
		assertFalse(getFieldMatcher(MatchTypeEnum.STRING).matches(new StringType("Durie"), new StringType("dury"), myMdmMatcherJson));
	}

	@Test
	public void testExactBoolean() {
		myMdmMatcherJson.setExact(true);

		assertTrue(getFieldMatcher(MatchTypeEnum.STRING).matches(new BooleanType(true), new BooleanType(true), myMdmMatcherJson));

		assertFalse(getFieldMatcher(MatchTypeEnum.STRING).matches(new BooleanType(true), new BooleanType(false), myMdmMatcherJson));
		assertFalse(getFieldMatcher(MatchTypeEnum.STRING).matches(new BooleanType(false), new BooleanType(true), myMdmMatcherJson));
	}

	@Test
	public void testExactDateString() {
		myMdmMatcherJson.setExact(true);

		assertTrue(getFieldMatcher(MatchTypeEnum.STRING).matches(new DateType("1965-08-09"), new DateType("1965-08-09"), myMdmMatcherJson));

		assertFalse(getFieldMatcher(MatchTypeEnum.STRING).matches(new DateType("1965-08-09"), new DateType("1965-09-08"), myMdmMatcherJson));
	}


	@Test
	public void testExactGender() {
		Enumeration<Enumerations.AdministrativeGender> male = new Enumeration<Enumerations.AdministrativeGender>(new Enumerations.AdministrativeGenderEnumFactory());
		male.setValue(Enumerations.AdministrativeGender.MALE);

		Enumeration<Enumerations.AdministrativeGender> female = new Enumeration<Enumerations.AdministrativeGender>(new Enumerations.AdministrativeGenderEnumFactory());
		female.setValue(Enumerations.AdministrativeGender.FEMALE);

		myMdmMatcherJson.setExact(true);

		assertTrue(getFieldMatcher(MatchTypeEnum.STRING).matches(male, male, myMdmMatcherJson));

		assertFalse(getFieldMatcher(MatchTypeEnum.STRING).matches(male, female, myMdmMatcherJson));
	}

	@Test
	public void testSoundex() {
		assertTrue(match(MatchTypeEnum.SOUNDEX, "Gail", "Gale"));
		assertTrue(match(MatchTypeEnum.SOUNDEX, "John", "Jon"));
		assertTrue(match(MatchTypeEnum.SOUNDEX, "Thom", "Tom"));

		assertFalse(match(MatchTypeEnum.SOUNDEX, "Fred", "Frank"));
		assertFalse(match(MatchTypeEnum.SOUNDEX, "Thomas", "Tom"));
	}


	@Test
	public void testCaverphone1() {
		assertTrue(match(MatchTypeEnum.CAVERPHONE1, "Gail", "Gael"));
		assertTrue(match(MatchTypeEnum.CAVERPHONE1, "John", "Jon"));

		assertFalse(match(MatchTypeEnum.CAVERPHONE1, "Gail", "Gale"));
		assertFalse(match(MatchTypeEnum.CAVERPHONE1, "Fred", "Frank"));
		assertFalse(match(MatchTypeEnum.CAVERPHONE1, "Thomas", "Tom"));
	}

	@Test
	public void testCaverphone2() {
		assertTrue(match(MatchTypeEnum.CAVERPHONE2, "Gail", "Gael"));
		assertTrue(match(MatchTypeEnum.CAVERPHONE2, "John", "Jon"));
		assertTrue(match(MatchTypeEnum.CAVERPHONE2, "Gail", "Gale"));

		assertFalse(match(MatchTypeEnum.CAVERPHONE2, "Fred", "Frank"));
		assertFalse(match(MatchTypeEnum.CAVERPHONE2, "Thomas", "Tom"));
	}

	@Test
	public void testNormalizeSubstring() {
		assertTrue(match(MatchTypeEnum.SUBSTRING, "BILLY", "Bill"));
		assertTrue(match(MatchTypeEnum.SUBSTRING, "Bill", "Billy"));
		assertTrue(match(MatchTypeEnum.SUBSTRING, "FRED", "Frederik"));

		assertFalse(match(MatchTypeEnum.SUBSTRING, "Fred", "Friederik"));
	}

	private boolean match(MatchTypeEnum theMatcher, String theLeft, String theRight) {
		return getFieldMatcher(theMatcher)
			.matches(new StringType(theLeft), new StringType(theRight), myMdmMatcherJson);
	}
}
