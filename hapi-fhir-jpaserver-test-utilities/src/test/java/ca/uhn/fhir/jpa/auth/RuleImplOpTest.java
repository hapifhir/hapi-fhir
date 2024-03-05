package ca.uhn.fhir.jpa.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.IRuleApplier;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class RuleImplOpTest {
	private static final String OPERATION = "operation";
	private static final String TYPE = "type";
	private static final String PATH = "path";
	private static final String VALUE = "value";
	private static final String REPLACE = "replace";
	private static final String PATIENT_BIRTH_DATE = "Patient.birthDate";
	private static final Parameters PARAMETERS = buildParameters();
	private static final String DOCUMENT = "document";
	private static final String ERROR_TEMPLATE = "HAPI-0339: Can not handle nested Parameters with %s operation";
	private static final String ERROR_UPDATE = String.format(ERROR_TEMPLATE, "UPDATE");
	private static final String ERROR_CREATE = String.format(ERROR_TEMPLATE, "CREATE");

	private static final String REQUEST_RULELIST = AuthorizationInterceptor.class.getName() + "_1_RULELIST";
	private final Patient myPatient = buildPatient();

	private final List<IAuthRule> myRules = new RuleBuilder()
		.allow()
		.transaction()
		.withAnyOperation()
		.andApplyNormalRules()
		.andThen()
		.allow()
		.write()
		.allResources()
		.withAnyId()
		.build();

	private final IAuthRule myRule = myRules.get(0);
	private final FhirContext myFhirContext = FhirContext.forR4Cached();
	private final IBaseBundle myInnerBundle = buildInnerBundler(myFhirContext);

	private final RequestDetails mySystemRequestDetails = buildSystemRequestDetails(myFhirContext, myRules);
	private final IRuleApplier myRuleApplier = new AuthorizationInterceptor();

	@Test
	void testTransactionBundleUpdateWithParameters() {
		final BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);
		bundleBuilder.addTransactionUpdateEntry(PARAMETERS);

		try {
			applyRule(bundleBuilder.getBundle());
			fail("Expected an InvalidRequestException");
		} catch (InvalidRequestException exception) {
			assertEquals(ERROR_UPDATE, exception.getMessage());
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
			assertEquals(ERROR_CREATE, exception.getMessage());
		}
	}

	@Test
	void testTransactionBundlePatchWithParameters() {
		final BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);
		bundleBuilder.addTransactionFhirPatchEntry(myPatient.getIdElement(), PARAMETERS);

		final AuthorizationInterceptor.Verdict verdict = applyRule(bundleBuilder.getBundle());

		assertThat(verdict.getDecision(), equalTo(PolicyEnum.ALLOW));
	}

	private AuthorizationInterceptor.Verdict applyRule(IBaseBundle theBundle) {
		return myRule.applyRule(RestOperationTypeEnum.TRANSACTION, mySystemRequestDetails, theBundle, myPatient.getIdElement(), myPatient, myRuleApplier, new HashSet<>(), null);
	}

	private static Parameters buildParameters() {
		final Parameters patch = new Parameters();

		final Parameters.ParametersParameterComponent op = patch.addParameter().setName(OPERATION);
		op.addPart().setName(TYPE).setValue(new CodeType(REPLACE));
		op.addPart().setName(PATH).setValue(new CodeType(PATIENT_BIRTH_DATE));
		op.addPart().setName(VALUE).setValue(new StringType("1912-04-14"));

		return patch;
	}

	private static RequestDetails buildSystemRequestDetails(FhirContext theFhirContext, List<IAuthRule> theRules) {
        final SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
		systemRequestDetails.setFhirContext(theFhirContext);
		systemRequestDetails.getUserData().put(REQUEST_RULELIST, theRules);

		return systemRequestDetails;
	}

	private static Patient buildPatient() {
		final Patient patient = new Patient();
		patient.setId(new IdType("Patient", "1"));
		return patient;
	}

	private static IBaseBundle buildInnerBundler(FhirContext theFhirContext) {
		final BundleBuilder innerBundleBuilder = new BundleBuilder(theFhirContext);
		innerBundleBuilder.setType(DOCUMENT);
		return innerBundleBuilder.getBundle();
	}
}
