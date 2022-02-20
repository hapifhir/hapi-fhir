package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RuleBulkExportImplTest {

	@Mock
	private RequestDetails myRequestDetails;
	@Mock
	private IRuleApplier myRuleApplier;
	@Mock
	private Set<AuthorizationFlagsEnum> myFlags;

	@Test
	public void testDenyBulkRequestWithInvalidResourcesTypes() {
		RuleBulkExportImpl myRule = new RuleBulkExportImpl("a");
		RestOperationTypeEnum myOperation = RestOperationTypeEnum.EXTENDED_OPERATION_SERVER;
		Pointcut myPointcut = Pointcut.STORAGE_INITIATE_BULK_EXPORT;

		Set<String> myTypes = new HashSet<>();
		myTypes.add("Patient");
		myTypes.add("Practitioner");
		myRule.setResourceTypes(myTypes);

		Set<String> myWantTypes = new HashSet<>();
		myWantTypes.add("Questionnaire");

		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(myWantTypes);
		when(myRequestDetails.getAttribute(any())).thenReturn(options);

		AuthorizationInterceptor.Verdict verdict = myRule.applyRule(myOperation, myRequestDetails, null, null, null, myRuleApplier, myFlags, myPointcut);
		assertEquals(PolicyEnum.DENY, verdict.getDecision());
	}

	@Test
	public void testBulkRequestWithValidResourcesTypes() {
		RuleBulkExportImpl myRule = new RuleBulkExportImpl("a");
		RestOperationTypeEnum myOperation = RestOperationTypeEnum.EXTENDED_OPERATION_SERVER;
		Pointcut myPointcut = Pointcut.STORAGE_INITIATE_BULK_EXPORT;

		Set<String> myTypes = new HashSet<>();
		myTypes.add("Patient");
		myTypes.add("Practitioner");
		myRule.setResourceTypes(myTypes);

		Set<String> myWantTypes = new HashSet<>();
		myWantTypes.add("Patient");
		myWantTypes.add("Practitioner");

		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(myWantTypes);
		when(myRequestDetails.getAttribute(any())).thenReturn(options);

		AuthorizationInterceptor.Verdict verdict = myRule.applyRule(myOperation, myRequestDetails, null, null, null, myRuleApplier, myFlags, myPointcut);
		assertNull(verdict);
	}

}
