package ca.uhn.fhir.narrative2;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.INarrative;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseNarrativeGenerator implements INarrativeGenerator {

	private INarrativeTemplateManifest myManifest;

	public INarrativeTemplateManifest getManifest() {
		return myManifest;
	}

	public void setManifest(INarrativeTemplateManifest theManifest) {
		myManifest = theManifest;
	}

	@Override
	public boolean populateResourceNarrative(FhirContext theFhirContext, IBaseResource theResource) {
		List<INarrativeTemplate> templateOpt = getTemplateForElement(theFhirContext, theResource);
		if (templateOpt.size() > 0) {
			applyTemplate(theFhirContext, templateOpt.get(0), theResource);
			return true;
		}

		return false;
	}

	private List<INarrativeTemplate> getTemplateForElement(FhirContext theFhirContext, IBase theElement) {
		return myManifest.getTemplateByElement(theFhirContext, getStyle(), theElement);
	}

	private boolean applyTemplate(FhirContext theFhirContext, INarrativeTemplate theTemplate, IBaseResource theResource) {
		if (templateDoesntApplyToResource(theTemplate, theResource)) {
			return false;
		}

		boolean retVal = false;
		String resourceName = theFhirContext.getResourceType(theResource);
		String contextPath = defaultIfEmpty(theTemplate.getContextPath(), resourceName);

		// Narrative templates define a path within the resource that they apply to. Here, we're
		// finding anywhere in the resource that gets a narrative
		List<IBase> targets = findElementsInResourceRequiringNarratives(theFhirContext, theResource, contextPath);
		for (IBase nextTargetContext : targets) {

			// Extract [element].text of type Narrative
			INarrative nextTargetNarrative = getOrCreateNarrativeChildElement(theFhirContext, nextTargetContext);

			// Create the actual narrative text
			String narrative = applyTemplate(theFhirContext, theTemplate, nextTargetContext);
			narrative = cleanWhitespace(narrative);

			if (isNotBlank(narrative)) {
				try {
					nextTargetNarrative.setDivAsString(narrative);
					nextTargetNarrative.setStatusAsString("generated");
					retVal = true;
				} catch (Exception e) {
					throw new InternalErrorException(Msg.code(1865) + e);
				}
			}

		}
		return retVal;
	}

	private INarrative getOrCreateNarrativeChildElement(FhirContext theFhirContext, IBase nextTargetContext) {
		BaseRuntimeElementCompositeDefinition<?> targetElementDef = (BaseRuntimeElementCompositeDefinition<?>) theFhirContext.getElementDefinition(nextTargetContext.getClass());
		BaseRuntimeChildDefinition targetTextChild = targetElementDef.getChildByName("text");
		List<IBase> existing = targetTextChild.getAccessor().getValues(nextTargetContext);
		INarrative nextTargetNarrative;
		if (existing.isEmpty()) {
			nextTargetNarrative = (INarrative) theFhirContext.getElementDefinition("narrative").newInstance();
			targetTextChild.getMutator().addValue(nextTargetContext, nextTargetNarrative);
		} else {
			nextTargetNarrative = (INarrative) existing.get(0);
		}
		return nextTargetNarrative;
	}

	private List<IBase> findElementsInResourceRequiringNarratives(FhirContext theFhirContext, IBaseResource theResource, String theContextPath) {
		if (theFhirContext.getVersion().getVersion().isOlderThan(FhirVersionEnum.DSTU3)) {
			return Collections.singletonList(theResource);
		}
		IFhirPath fhirPath = theFhirContext.newFluentPath();
		return fhirPath.evaluate(theResource, theContextPath, IBase.class);
	}

	protected abstract String applyTemplate(FhirContext theFhirContext, INarrativeTemplate theTemplate, IBase theTargetContext);

	private boolean templateDoesntApplyToResource(INarrativeTemplate theTemplate, IBaseResource theResource) {
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

	protected abstract EnumSet<TemplateTypeEnum> getStyle();

	/**
	 * Trims the superfluous whitespace out of an HTML block
	 */
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
				continue;
			}

			if (betweenTags) {
				if (Character.isWhitespace(nextChar)) {
					inWhitespace = true;
				} else if (nextChar == '<') {
					if (inWhitespace && !lastNonWhitespaceCharWasTagEnd) {
						b.append(' ');
					}
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
