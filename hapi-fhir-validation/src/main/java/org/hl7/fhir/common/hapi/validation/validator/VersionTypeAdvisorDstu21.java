package org.hl7.fhir.common.hapi.validation.validator;

import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_14_50;

public class VersionTypeAdvisorDstu21 extends BaseAdvisor_14_50 {
	@Override
	public boolean failFastOnNullOrUnknownEntry() {
		return false;
	}
}
