package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.ExtensionUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;

import java.util.List;

public class ExtensionMatcher implements IMdmFieldMatcher{
	@Override
	public boolean matches(FhirContext theFhirContext, IBase theLeftBase, IBase theRightBase, boolean theExact, String theIdentifierSystem) {
		if (!(theLeftBase instanceof IBaseHasExtensions && theRightBase instanceof IBaseHasExtensions)){
			return false;
		}
		List<? extends IBaseExtension<?, ?>> leftExtension = ((IBaseHasExtensions) theLeftBase).getExtension();
		List<? extends IBaseExtension<?, ?>> rightExtension = ((IBaseHasExtensions) theRightBase).getExtension();

		boolean match = false;

		if (theIdentifierSystem != null) {
			leftExtension.removeIf(iBaseExtension -> !iBaseExtension.getUrl().equals(theIdentifierSystem));
			rightExtension.removeIf(iBaseExtension -> !iBaseExtension.getUrl().equals(theIdentifierSystem));
		}

		for (IBaseExtension leftExtensionValue : leftExtension) {
			for (IBaseExtension rightExtensionValue : rightExtension) {
				match |= ExtensionUtil.equals(leftExtensionValue, rightExtensionValue);
			}
		}
		return match;
	}
}
