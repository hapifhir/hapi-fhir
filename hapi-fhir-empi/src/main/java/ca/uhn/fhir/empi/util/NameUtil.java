package ca.uhn.fhir.empi.util;

import ca.uhn.fhir.context.FhirContext;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.PrimitiveType;

import java.util.List;
import java.util.stream.Collectors;

public class NameUtil {
	public static List<String> extractGivenNames(FhirContext theFhirContext, IBase theBase) {
		switch(theFhirContext.getVersion().getVersion()) {
			case R4:
				HumanName humanName = (HumanName)theBase;
				return humanName.getGiven().stream().map(PrimitiveType::getValueAsString).filter(s -> !StringUtils.isEmpty(s)).collect(Collectors.toList());
			default:
				// FIXME EMPI moar versions
				throw new UnsupportedOperationException("Version not supported: " + theFhirContext.getVersion().getVersion());

		}
	}

	public static String extractFamilyName(FhirContext theFhirContext, IBase theBase) {
		switch(theFhirContext.getVersion().getVersion()) {
			case R4:
				HumanName humanName = (HumanName)theBase;
				return humanName.getFamily();
			default:
				// FIXME EMPI moar versions
				throw new UnsupportedOperationException("Version not supported: " + theFhirContext.getVersion().getVersion());

		}
	}
}
