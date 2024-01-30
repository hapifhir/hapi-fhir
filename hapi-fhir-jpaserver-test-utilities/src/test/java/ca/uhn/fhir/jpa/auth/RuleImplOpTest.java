package ca.uhn.fhir.jpa.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.IRuleApplier;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@MockitoSettings
public class RuleImplOpTest {
	private static final String OPERATION = "operation";
	private static final String TYPE = "type";
	private static final String PATH = "path";
	private static final String VALUE = "value";
	private static final String REPLACE = "replace";
	private static final String PATIENT_BIRTH_DATE = "Patient.birthDate";
	private static final Parameters PARAMETERS = buildParameters();
	private static final String DOCUMENT = "document";
	private static final String ERROR_TEMPLATE = "HAPI-0339: Can not handle transaction with nested resource of type %s";
	private static final String ERROR_PARAMETERS = String.format(ERROR_TEMPLATE, "Parameters");
	private static final String ERROR_BUNDLE = String.format(ERROR_TEMPLATE, "Bundle");

	private IBaseBundle myInnerBundle;
	private IAuthRule myRule;
	private final FhirContext myFhirContext = FhirContext.forR4Cached();

	@Mock
	private RequestDetails myRequestDetails;
	private final IRuleApplier myRuleApplier = new AuthorizationInterceptor();

	@BeforeEach
	void beforeEach() {
		when(myRequestDetails.getFhirContext()).thenReturn(myFhirContext);

		final BundleBuilder innerBundleBuilder = new BundleBuilder(myFhirContext);
		innerBundleBuilder.setType(DOCUMENT);
		myInnerBundle = innerBundleBuilder.getBundle();

		myRule = new RuleBuilder()
			.allow()
			.transaction()
			.withAnyOperation()
			.andApplyNormalRules()
			.build()
			.get(0);
	}

	@Test
	void testTransactionBundleUpdateWithParameters() {
		final BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);
		bundleBuilder.addTransactionUpdateEntry(PARAMETERS);

		try {
			applyRule(bundleBuilder.getBundle());
			fail("Expected an InvalidRequestException");
		} catch (InvalidRequestException exception) {
			assertEquals(ERROR_PARAMETERS, exception.getMessage());
		}
	}

	@Test
	void testTransactionBundleWithNestedBundle() {
		final BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);
		bundleBuilder.addTransactionCreateEntry(myInnerBundle);

		try {
			applyRule(bundleBuilder.getBundle());
			fail("Expected an InvalidRequestException");
		} catch (InvalidRequestException exception) {
			assertEquals(ERROR_BUNDLE, exception.getMessage());
		}
	}

	@Test
	void testTransactionBundlePatchWithParameters() {
		final BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);
		bundleBuilder.addTransactionFhirPatchEntry(PARAMETERS);

		final AuthorizationInterceptor.Verdict verdict = applyRule(bundleBuilder.getBundle());

		// The important thing is that no InvalidRequestException is thrown
		assertThat(verdict.getDecision(), equalTo(PolicyEnum.DENY));
	}

	private AuthorizationInterceptor.Verdict applyRule(IBaseBundle theBundle) {
		return myRule.applyRule(RestOperationTypeEnum.TRANSACTION, myRequestDetails, theBundle, null, myInnerBundle, myRuleApplier, new HashSet<>(), null);
	}

	private static Parameters buildParameters() {
		final Parameters patch = new Parameters();

		final Parameters.ParametersParameterComponent op = patch.addParameter().setName(OPERATION);
		op.addPart().setName(TYPE).setValue(new CodeType(REPLACE));
		op.addPart().setName(PATH).setValue(new CodeType(PATIENT_BIRTH_DATE));
		op.addPart().setName(VALUE).setValue(new StringType("1912-04-14"));

		return patch;
	}
}
