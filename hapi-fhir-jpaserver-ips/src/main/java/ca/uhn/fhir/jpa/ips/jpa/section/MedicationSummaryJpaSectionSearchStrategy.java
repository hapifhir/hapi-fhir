package ca.uhn.fhir.jpa.ips.jpa.section;

import ca.uhn.fhir.jpa.ips.api.IpsSectionContext;
import ca.uhn.fhir.jpa.ips.jpa.JpaSectionSearchStrategy;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.MedicationDispense;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.ResourceType;

public class MedicationSummaryJpaSectionSearchStrategy extends JpaSectionSearchStrategy {

	@Override
	public void massageResourceSearch(IpsSectionContext theIpsSectionContext, SearchParameterMap theSearchParameterMap) {
		String resourceType = theIpsSectionContext.getResourceType();
		if (ResourceType.MedicationStatement.name().equals(resourceType)) {

			theSearchParameterMap.addInclude(MedicationStatement.INCLUDE_MEDICATION);
			theSearchParameterMap.add(
				MedicationStatement.SP_STATUS,
				new TokenOrListParam()
					.addOr(new TokenParam(
						MedicationStatement.MedicationStatementStatus.ACTIVE.getSystem(),
						MedicationStatement.MedicationStatementStatus.ACTIVE.toCode()))
					.addOr(new TokenParam(
						MedicationStatement.MedicationStatementStatus.INTENDED.getSystem(),
						MedicationStatement.MedicationStatementStatus.INTENDED.toCode()))
					.addOr(new TokenParam(
						MedicationStatement.MedicationStatementStatus.UNKNOWN.getSystem(),
						MedicationStatement.MedicationStatementStatus.UNKNOWN.toCode()))
					.addOr(new TokenParam(
						MedicationStatement.MedicationStatementStatus.ONHOLD.getSystem(),
						MedicationStatement.MedicationStatementStatus.ONHOLD.toCode())));

		} else if (ResourceType.MedicationRequest.name().equals(resourceType)) {

			theSearchParameterMap.addInclude(MedicationRequest.INCLUDE_MEDICATION);
			theSearchParameterMap.add(
				MedicationRequest.SP_STATUS,
				new TokenOrListParam()
					.addOr(new TokenParam(
						MedicationRequest.MedicationRequestStatus.ACTIVE.getSystem(),
						MedicationRequest.MedicationRequestStatus.ACTIVE.toCode()))
					.addOr(new TokenParam(
						MedicationRequest.MedicationRequestStatus.UNKNOWN.getSystem(),
						MedicationRequest.MedicationRequestStatus.UNKNOWN.toCode()))
					.addOr(new TokenParam(
						MedicationRequest.MedicationRequestStatus.ONHOLD.getSystem(),
						MedicationRequest.MedicationRequestStatus.ONHOLD.toCode())));

		} else if (ResourceType.MedicationAdministration.name().equals(resourceType)) {

			theSearchParameterMap.addInclude(MedicationAdministration.INCLUDE_MEDICATION);
			theSearchParameterMap.add(
				MedicationAdministration.SP_STATUS,
				new TokenOrListParam()
					.addOr(new TokenParam(
						MedicationAdministration.MedicationAdministrationStatus.INPROGRESS
							.getSystem(),
						MedicationAdministration.MedicationAdministrationStatus.INPROGRESS
							.toCode()))
					.addOr(new TokenParam(
						MedicationAdministration.MedicationAdministrationStatus.UNKNOWN.getSystem(),
						MedicationAdministration.MedicationAdministrationStatus.UNKNOWN.toCode()))
					.addOr(new TokenParam(
						MedicationAdministration.MedicationAdministrationStatus.ONHOLD.getSystem(),
						MedicationAdministration.MedicationAdministrationStatus.ONHOLD.toCode())));

		} else if (ResourceType.MedicationDispense.name().equals(resourceType)) {

			theSearchParameterMap.addInclude(MedicationDispense.INCLUDE_MEDICATION);
			theSearchParameterMap.add(
				MedicationDispense.SP_STATUS,
				new TokenOrListParam()
					.addOr(new TokenParam(
						MedicationDispense.MedicationDispenseStatus.INPROGRESS.getSystem(),
						MedicationDispense.MedicationDispenseStatus.INPROGRESS.toCode()))
					.addOr(new TokenParam(
						MedicationDispense.MedicationDispenseStatus.UNKNOWN.getSystem(),
						MedicationDispense.MedicationDispenseStatus.UNKNOWN.toCode()))
					.addOr(new TokenParam(
						MedicationDispense.MedicationDispenseStatus.ONHOLD.getSystem(),
						MedicationDispense.MedicationDispenseStatus.ONHOLD.toCode())));

		}

	}
}
