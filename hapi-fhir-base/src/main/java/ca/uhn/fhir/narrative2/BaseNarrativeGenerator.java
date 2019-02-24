package ca.uhn.fhir.narrative2;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fluentpath.IFluentPath;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.INarrative;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseNarrativeGenerator implements INarrativeGenerator {

	private INarrativeTemplateManifest myManifest;
	private FhirContext myFhirContext;

	public BaseNarrativeGenerator() {
		super();
	}

	public INarrativeTemplateManifest getManifest() {
		return myManifest;
	}

	public void setManifest(INarrativeTemplateManifest theManifest) {
		myManifest = theManifest;
	}

	public FhirContext getFhirContext() {
		Validate.notNull(myFhirContext, "The FhirContext is not set on this Narrative Generator. Please call setFhirContext(FhirContext) to set this property before using the narrative generator.");
		return myFhirContext;
	}

	public void setFhirContext(FhirContext theFhirContext) {
		Validate.notNull(theFhirContext, "theFhirContext must not be null");
		myFhirContext = theFhirContext;
	}

	@Override
	public boolean generateNarrative(IBaseResource theResource) {
		Optional<INarrativeTemplate> templateOpt = getTemplateForElement(theResource);
		if (templateOpt.isPresent()) {
			return applyTemplate(templateOpt.get(), theResource);
		} else {
			return false;
		}
	}

	private Optional<INarrativeTemplate> getTemplateForElement(IBase theElement) {
		return myManifest.getTemplateByElement(getStyle(), theElement);
	}

	private boolean applyTemplate(INarrativeTemplate theTemplate, IBaseResource theResource) {
		if (templateDoesntApplyToResourceProfiles(theTemplate, theResource)) {
			return false;
		}

		boolean retVal = false;
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
			narrative = cleanWhitespace(narrative);

			if (isNotBlank(narrative)) {
				try {
					nextTargetNarrative.setDivAsString(narrative);
					nextTargetNarrative.setStatusAsString("generated");
					retVal = true;
				} catch (Exception e) {
					throw new InternalErrorException(e);
				}
			}

		}
		return retVal;
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

	public static String cleanWhitespace(String theResult) {
		StringBuilder b = new StringBuilder();
		boolean inWhitespace = false;
		boolean betweenTags = false;
		boolean lastNonWhitespaceCharWasTagEnd = false;
		boolean inPre = false;
		for (int i = 0; i < theResult.length(); i++) {
			char nextChar = theResult.charAt(i);
			if (inPre) {
				b.append(nextChar);
				continue;
			} else if (nextChar == '>') {
				b.append(nextChar);
				betweenTags = true;
				lastNonWhitespaceCharWasTagEnd = true;
				continue;
			} else if (nextChar == '\n' || nextChar == '\r') {
				// if (inWhitespace) {
				// b.append(' ');
				// inWhitespace = false;
				// }
				continue;
			}

			if (betweenTags) {
				if (Character.isWhitespace(nextChar)) {
					inWhitespace = true;
				} else if (nextChar == '<') {
					if (inWhitespace && !lastNonWhitespaceCharWasTagEnd) {
						b.append(' ');
					}
					inWhitespace = false;
					b.append(nextChar);
					inWhitespace = false;
					betweenTags = false;
					lastNonWhitespaceCharWasTagEnd = false;
					if (i + 3 < theResult.length()) {
						char char1 = Character.toLowerCase(theResult.charAt(i + 1));
						char char2 = Character.toLowerCase(theResult.charAt(i + 2));
						char char3 = Character.toLowerCase(theResult.charAt(i + 3));
						char char4 = Character.toLowerCase((i + 4 < theResult.length()) ? theResult.charAt(i + 4) : ' ');
						if (char1 == 'p' && char2 == 'r' && char3 == 'e') {
							inPre = true;
						} else if (char1 == '/' && char2 == 'p' && char3 == 'r' && char4 == 'e') {
							inPre = false;
						}
					}
				} else {
					lastNonWhitespaceCharWasTagEnd = false;
					if (inWhitespace) {
						b.append(' ');
						inWhitespace = false;
					}
					b.append(nextChar);
				}
			} else {
				b.append(nextChar);
			}
		}
		return b.toString();
	}
}
