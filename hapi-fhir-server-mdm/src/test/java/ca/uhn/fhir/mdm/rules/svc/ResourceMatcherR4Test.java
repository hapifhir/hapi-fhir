package ca.uhn.fhir.mdm.rules.svc;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson;
import ca.uhn.fhir.mdm.rules.json.MdmMatcherJson;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.mdm.rules.matcher.fieldmatchers.PhoneticEncoderMatcher;
import ca.uhn.fhir.mdm.rules.matcher.models.MatchTypeEnum;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class ResourceMatcherR4Test extends BaseMdmRulesR4Test {
	private static final String PATIENT_PHONE = "phone";
	private static final String MATCH_FIELDS = PATIENT_GIVEN + "," + PATIENT_FAMILY + "," + PATIENT_PHONE;
	public static final String PHONE_NUMBER = "123 456789";
	private Patient myLeft;
	private Patient myRight;
	@Mock
	private Appender<ILoggingEvent> myAppender;
	@Captor
	ArgumentCaptor<ILoggingEvent> myLoggingEvent;

	@Override
	@BeforeEach
	public void before() {
		super.before();

		when(mySearchParamRetriever.getActiveSearchParam("Patient", "birthdate")).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam("Patient", "identifier")).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam("Practitioner", "identifier")).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam("Medication", "identifier")).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam("Patient", "active")).thenReturn(mock(RuntimeSearchParam.class));

		{
			myLeft = new Patient();
			HumanName name = myLeft.addName();
			name.addGiven("zulaiha");
			name.setFamily("namadega");
			myLeft.addTelecom().setValue(PHONE_NUMBER);
			myLeft.setId("Patient/1");
		}
		{
			myRight = new Patient();
			HumanName name = myRight.addName();
			name.addGiven("zulaiha");
			name.setFamily("namaedga");
			myRight.addTelecom().setValue(PHONE_NUMBER);
			myRight.setId("Patient/2");
		}
	}

	@Test
	public void testMetaphoneMatchResult() {
		MdmResourceMatcherSvc matcherSvc = buildMatcher(buildNamePhoneRules(MatchTypeEnum.METAPHONE));

		MdmMatchOutcome result = matcherSvc.match(myLeft, myRight);
		assertMatchResult(MdmMatchResultEnum.MATCH, 7L, 3.0, false, false, result);
	}

	@Test
	public void testMetaphoneOnName() {
		Logger logger = (Logger) LoggerFactory.getLogger(PhoneticEncoderMatcher.class);
		logger.addAppender(myAppender);

		// Given: MDM rules that match by Patient.name using some phonetic algorithm
		MdmFieldMatchJson lastNameMatchField = new MdmFieldMatchJson()
			.setName(PATIENT_FAMILY)
			.setResourceType("Patient")
			.setResourcePath("name")
			.setMatcher(new MdmMatcherJson().setAlgorithm(MatchTypeEnum.METAPHONE));

		MdmRulesJson retval = new MdmRulesJson();
		retval.setVersion("test version");
		retval.addMatchField(lastNameMatchField);
		retval.setMdmTypes(List.of("Patient"));
		retval.putMatchResult(PATIENT_FAMILY, MdmMatchResultEnum.MATCH);

		// When
		MdmResourceMatcherSvc matcherSvc = buildMatcher(retval);
		MdmMatchOutcome result = matcherSvc.match(myLeft, myRight);

		// Then: expect a logged message notifying that we are unable to phonetically match by HumanName
		verify(myAppender, times(1)).doAppend(myLoggingEvent.capture());
		ILoggingEvent event = myLoggingEvent.getValue();
		assertEquals(Level.WARN, event.getLevel());
		assertEquals("Unable to evaluate match between HumanName and HumanName because they are not an instance of PrimitiveType.", event.getFormattedMessage());

		logger.detachAppender(myAppender);
		verifyNoMoreInteractions(myAppender);
		System.clearProperty("unit_test");

		assertMatchResult(MdmMatchResultEnum.NO_MATCH, 0L, 0.0, false, false, result);
	}

	@Test
	public void testStringMatchResult() {
		MdmResourceMatcherSvc matcherSvc = buildMatcher(buildNamePhoneRules(MatchTypeEnum.STRING));

		MdmMatchOutcome result = matcherSvc.match(myLeft, myRight);
		assertMatchResult(MdmMatchResultEnum.NO_MATCH, 5L, 2.0, false, false, result);
	}

	protected MdmRulesJson buildNamePhoneRules(MatchTypeEnum theMatcherEnum) {
		MdmFieldMatchJson lastNameMatchField = new MdmFieldMatchJson()
			.setName(PATIENT_FAMILY)
			.setResourceType("Patient")
			.setResourcePath("name.family")
			.setMatcher(new MdmMatcherJson().setAlgorithm(theMatcherEnum));

		MdmFieldMatchJson firstNameMatchField = new MdmFieldMatchJson()
			.setName(PATIENT_GIVEN)
			.setResourceType("Patient")
			.setResourcePath("name.given")
			.setMatcher(new MdmMatcherJson().setAlgorithm(theMatcherEnum));

		MdmFieldMatchJson phoneField = new MdmFieldMatchJson()
			.setName(PATIENT_PHONE)
			.setResourceType("Patient")
			.setResourcePath("telecom.value")
			.setMatcher(new MdmMatcherJson().setAlgorithm(MatchTypeEnum.STRING));

		MdmRulesJson retval = new MdmRulesJson();
		retval.setVersion("test version");
		retval.addMatchField(firstNameMatchField);
		retval.addMatchField(lastNameMatchField);
		retval.setMdmTypes(Arrays.asList("Patient", "Practitioner", "Medication"));
		retval.addMatchField(phoneField);
		retval.putMatchResult(MATCH_FIELDS, MdmMatchResultEnum.MATCH);
		return retval;
	}
}
