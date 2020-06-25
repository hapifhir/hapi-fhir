package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.config.PhoneticEncoderEnum;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
// FIXME KHS test
public class PhoneticEncoderSvc {
	@Autowired
	ModelConfig myModelConfig;
	@Autowired
	ISearchParamRegistry mySearchParamRegistry;

	public String encode(RuntimeSearchParam theSearchParam, String theString) {
		String retval = theString;

		if (myModelConfig.hasStringEncoder() && Patient.SP_PHONETIC.equals(theSearchParam.getName())) {
			retval = myModelConfig.getStringEncoder().encode(theString);
		} else {
			retval = encodeWithPhoneticSearchParam(theString, retval, theSearchParam);
		}
		return retval;
	}

	public String encode(String theResourceName, String theParamName, String theString) {
		String retval = theString;

		if (myModelConfig.hasStringEncoder() && Patient.SP_PHONETIC.equals(theParamName)) {
			retval = myModelConfig.getStringEncoder().encode(theString);
		} else {
			// FIXME KHS pass this down so we don't have to look it up again?
			RuntimeSearchParam searchParam = mySearchParamRegistry.getActiveSearchParam(theResourceName, theParamName);
			retval = encodeWithPhoneticSearchParam(theString, retval, searchParam);
		}
		return retval;
	}

	private String encodeWithPhoneticSearchParam(String theString, String theRetval, RuntimeSearchParam theSearchParam) {
		if (theSearchParam != null) {
			Optional<PhoneticEncoderEnum> oEncoder = theSearchParam.getExtensions(JpaConstants.EXT_SEARCHPARAM_PHONETIC_ENCODER).stream()
				.map(IBaseExtension::getValue)
				.map(val -> (IPrimitiveType<?>) val)
				.map(IPrimitiveType::getValueAsString)
				.map(PhoneticEncoderEnum::valueOf)
				.findFirst();
			if (oEncoder.isPresent()) {
				theRetval = oEncoder.get().getPhoneticEncoder().encode(theString);
			}
		}
		return theRetval;
	}
}
