package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.hl7.fhir.r4.model.IdType;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RuleBulkExportImplTest {
	private RestOperationTypeEnum myOperation = RestOperationTypeEnum.EXTENDED_OPERATION_SERVER;
	private Pointcut myPointcut = Pointcut.STORAGE_INITIATE_BULK_EXPORT;
	@Mock
	private RequestDetails myRequestDetails;
	@Mock
	private IRuleApplier myRuleApplier;
	@Mock
	private Set<AuthorizationFlagsEnum> myFlags;

	@Test
	public void testDenyBulkRequestWithInvalidGroupId() {
		RuleBulkExportImpl myRule = new RuleBulkExportImpl("a");
		myRule.setAppliesToGroupExportOnGroup("invalid group");

		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);
		options.setGroupId(new IdType("Group/123"));

		when(myRequestDetails.getAttribute(any())).thenReturn(options);

		AuthorizationInterceptor.Verdict verdict = myRule.applyRule(myOperation, myRequestDetails, null, null, null, myRuleApplier, myFlags, myPointcut);
		assertEquals(PolicyEnum.DENY, verdict.getDecision());
	}

}
