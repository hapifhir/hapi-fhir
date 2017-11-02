package ca.uhn.fhir.util;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseReference;

import java.util.List;

public class BinaryUtil {

	private BinaryUtil() {
		// non instantiable
	}

	public static IBaseReference getSecurityContext(FhirContext theCtx, IBaseBinary theBinary) {
		RuntimeResourceDefinition def = theCtx.getResourceDefinition("Binary");
		BaseRuntimeChildDefinition child = def.getChildByName("securityContext");

		List<IBase> values = child.getAccessor().getValues(theBinary);
		IBaseReference retVal = null;
		if (values.size() > 0) {
			retVal = (IBaseReference) values.get(0);
		}
		return retVal;
	}

	public static IBaseBinary newBinary(FhirContext theCtx) {
		return (IBaseBinary) theCtx.getResourceDefinition("Binary").newInstance();
	}

	public static void setSecurityContext(FhirContext theCtx, IBaseBinary theBinary, String theSecurityContext) {
		RuntimeResourceDefinition def = theCtx.getResourceDefinition("Binary");
		BaseRuntimeChildDefinition child = def.getChildByName("securityContext");

		BaseRuntimeElementDefinition<?> referenceDef = theCtx.getElementDefinition("reference");
		IBaseReference reference = (IBaseReference) referenceDef.newInstance();
		child.getMutator().addValue(theBinary, reference);

		reference.setReference(theSecurityContext);
	}

}
