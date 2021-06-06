package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class RuleBulkExportImpl extends BaseRule {
	private String myGroupId;
	private BulkDataExportOptions.ExportStyle myWantExportStyle;
	private Collection<String> myResourceTypes;

	RuleBulkExportImpl(String theRuleName) {
		super(theRuleName);
	}

	@Override
	public AuthorizationInterceptor.Verdict applyRule(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource, IRuleApplier theRuleApplier, Set<AuthorizationFlagsEnum> theFlags, Pointcut thePointcut) {
		if (thePointcut != Pointcut.STORAGE_INITIATE_BULK_EXPORT) {
			return null;
		}

		if (theRequestDetails == null) {
			return null;
		}

		BulkDataExportOptions options = (BulkDataExportOptions) theRequestDetails.getAttribute(AuthorizationInterceptor.REQUEST_ATTRIBUTE_BULK_DATA_EXPORT_OPTIONS);

		if (options.getExportStyle() != myWantExportStyle) {
			return null;
		}

		if (myResourceTypes != null && !myResourceTypes.isEmpty()) {
			for (String next : myResourceTypes) {
				if (!myResourceTypes.contains(next)) {
					return null;
				}
			}
		}

		if (myWantExportStyle == BulkDataExportOptions.ExportStyle.SYSTEM) {
			return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
		}

		if (isNotBlank(myGroupId) && options.getGroupId() != null) {
			String expectedGroupId = new IdDt(myGroupId).toUnqualifiedVersionless().getValue();
			String actualGroupId = options.getGroupId().toUnqualifiedVersionless().getValue();
			if (Objects.equals(expectedGroupId, actualGroupId)) {
				return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
			}
		}

		return null;
	}

	public void setAppliesToGroupExportOnGroup(String theGroupId) {
		myWantExportStyle = BulkDataExportOptions.ExportStyle.GROUP;
		myGroupId = theGroupId;
	}

	public void setAppliesToPatientExportOnGroup(String theGroupId) {
		myWantExportStyle = BulkDataExportOptions.ExportStyle.PATIENT;
		myGroupId = theGroupId;
	}

	public void setAppliesToSystem() {
		myWantExportStyle = BulkDataExportOptions.ExportStyle.SYSTEM;
	}

	public void setResourceTypes(Collection<String> theResourceTypes) {
		myResourceTypes = theResourceTypes;
	}

}
