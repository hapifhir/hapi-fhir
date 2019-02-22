package ca.uhn.fhir.narrative2;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fluentpath.IFluentPath;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.INarrative;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.*;

public abstract class BaseNarrativeGenerator implements INarrativeGenerator {

	private INarrativeTemplateManifest myManifest;
	private FhirContext myFhirContext;

	public INarrativeTemplateManifest getManifest() {
		return myManifest;
	}

	public void setManifest(INarrativeTemplateManifest theManifest) {
		myManifest = theManifest;
	}

	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	public void setFhirContext(FhirContext theContext) {
		myFhirContext = theContext;
	}

	@Override
	public void generateNarrative(IBaseResource theResource) {
		String resourceName = myFhirContext.getResourceDefinition(theResource).getName();
		Optional<INarrativeTemplate> templateOpt = myManifest.getTemplateByResourceName(getStyle(), resourceName);
		templateOpt.ifPresent(t -> applyTemplate(t, theResource));
	}

	private void applyTemplate(INarrativeTemplate theTemplate, IBaseResource theResource) {
		if (templateDoesntApplyToResourceProfiles(theTemplate, theResource)) {
			return;
		}

		String resourceName = myFhirContext.getResourceDefinition(theResource).getName();
		String contextPath = defaultIfEmpty(theTemplate.getContextPath(), resourceName);

		IFluentPath fhirPath = myFhirContext.newFluentPath();
		List<IBase> targets = fhirPath.evaluate(theResource, contextPath, IBase.class);
		for (IBase nextTargetContext : targets) {

			BaseRuntimeElementCompositeDefinition<?> targetElementDef = (BaseRuntimeElementCompositeDefinition<?>) getFhirContext().getElementDefinition(nextTargetContext.getClass());
			BaseRuntimeChildDefinition targetTextChild = targetElementDef.getChildByName("text");
			List<IBase> existing = targetTextChild.getAccessor().getValues(nextTargetContext);
			INarrative nextTargetNarrative;
			if (existing.isEmpty()) {
				nextTargetNarrative = (INarrative) getFhirContext().getElementDefinition("narrative").newInstance();
				targetTextChild.getMutator().addValue(nextTargetContext, nextTargetNarrative);
			} else {
				nextTargetNarrative = (INarrative) existing.get(0);
			}

			String narrative = applyTemplate(theTemplate, nextTargetContext);

			if (isNotBlank(narrative)) {
				try {
					nextTargetNarrative.setDivAsString(narrative);
					nextTargetNarrative.setStatusAsString("generated");
				} catch (Exception e) {
					throw new InternalErrorException(e);
				}
			}

		}
	}

	protected abstract String applyTemplate(INarrativeTemplate theTemplate, IBase theTargetContext);

	private boolean templateDoesntApplyToResourceProfiles(INarrativeTemplate theTemplate, IBaseResource theResource) {
		boolean retVal = false;
		if (theTemplate.getAppliesToProfiles() != null && !theTemplate.getAppliesToProfiles().isEmpty()) {
			Set<String> resourceProfiles = theResource
				.getMeta()
				.getProfile()
				.stream()
				.map(t -> t.getValueAsString())
				.collect(Collectors.toSet());
			retVal = true;
			for (String next : theTemplate.getAppliesToProfiles()) {
				if (resourceProfiles.contains(next)) {
					retVal = false;
					break;
				}
			}
		}
		return retVal;
	}

	protected abstract TemplateTypeEnum getStyle();

}
