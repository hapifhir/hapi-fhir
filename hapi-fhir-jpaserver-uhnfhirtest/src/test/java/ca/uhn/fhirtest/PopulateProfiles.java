package ca.uhn.fhirtest;

import ca.uhn.fhir.model.dstu.resource.Profile;
import ca.uhn.fhir.model.dstu.resource.Profile.ExtensionDefn;
import ca.uhn.fhir.model.dstu.valueset.DataTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ExtensionContextEnum;

public class PopulateProfiles {

	public static void main(String[] args) {
		
		Profile hapiExtensions = new Profile();
		
		ExtensionDefn ext = hapiExtensions.addExtensionDefn();
		ext.addContext("Conformance.rest.resource");
		ext.getCode().setValue("resourceCount");
		ext.getContextType().setValueAsEnum(ExtensionContextEnum.RESOURCE);
		ext.getDisplay().setValue("Resource count on server");
		ext.getDefinition().addType().setCode(DataTypeEnum.DECIMAL);
		
		
		
		
	}
	
}
