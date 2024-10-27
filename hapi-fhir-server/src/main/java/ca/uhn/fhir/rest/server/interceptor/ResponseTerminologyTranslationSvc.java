/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeDefinition;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.TranslateConceptResult;
import ca.uhn.fhir.context.support.TranslateConceptResults;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.IModelVisitor;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ResponseTerminologyTranslationSvc {
	private BaseRuntimeChildDefinition myCodingSystemChild;
	private BaseRuntimeChildDefinition myCodingCodeChild;
	private BaseRuntimeElementDefinition<IPrimitiveType<?>> myUriDefinition;
	private BaseRuntimeElementDefinition<IPrimitiveType<?>> myCodeDefinition;
	private Class<? extends IBase> myCodeableConceptType;
	private Class<? extends IBase> myCodingType;
	private BaseRuntimeChildDefinition myCodeableConceptCodingChild;
	private BaseRuntimeElementCompositeDefinition<?> myCodingDefinition;
	private RuntimePrimitiveDatatypeDefinition myStringDefinition;
	private BaseRuntimeChildDefinition myCodingDisplayChild;
	private Map<String, String> myMappingSpec;
	private final IValidationSupport myValidationSupport;
	private final FhirContext myFhirContext;

	public ResponseTerminologyTranslationSvc(@Nonnull IValidationSupport theValidationSupport) {
		myValidationSupport = theValidationSupport;
		Validate.notNull(theValidationSupport, "The validation support must not be null");

		myFhirContext = theValidationSupport.getFhirContext();
		Validate.notNull(myFhirContext, "The validation support must not return a null context");

		BaseRuntimeElementCompositeDefinition<?> codeableConceptDef = (BaseRuntimeElementCompositeDefinition<?>)
				Objects.requireNonNull(myFhirContext.getElementDefinition("CodeableConcept"));

		myCodeableConceptType = codeableConceptDef.getImplementingClass();
		myCodeableConceptCodingChild = codeableConceptDef.getChildByName("coding");

		myCodingDefinition = (BaseRuntimeElementCompositeDefinition<?>)
				Objects.requireNonNull(myFhirContext.getElementDefinition("Coding"));
		myCodingType = myCodingDefinition.getImplementingClass();
		myCodingSystemChild = myCodingDefinition.getChildByName("system");
		myCodingCodeChild = myCodingDefinition.getChildByName("code");
		myCodingDisplayChild = myCodingDefinition.getChildByName("display");

		myUriDefinition = (RuntimePrimitiveDatatypeDefinition) myFhirContext.getElementDefinition("uri");
		myCodeDefinition = (RuntimePrimitiveDatatypeDefinition) myFhirContext.getElementDefinition("code");
		myStringDefinition = (RuntimePrimitiveDatatypeDefinition) myFhirContext.getElementDefinition("string");
	}

	public void processResourcesForTerminologyTranslation(List<IBaseResource> resources) {
		FhirTerser terser = myFhirContext.newTerser();
		for (IBaseResource nextResource : resources) {
			terser.visit(nextResource, new MappingVisitor());
		}
	}

	public void addMappingSpecification(String theSourceCodeSystemUrl, String theTargetCodeSystemUrl) {
		Validate.notBlank(theSourceCodeSystemUrl, "theSourceCodeSystemUrl must not be null or blank");
		Validate.notBlank(theTargetCodeSystemUrl, "theTargetCodeSystemUrl must not be null or blank");

		getMappingSpecifications().put(theSourceCodeSystemUrl, theTargetCodeSystemUrl);
	}

	public void clearMappingSpecifications() {
		myMappingSpec.clear();
	}

	public Map<String, String> getMappingSpecifications() {
		if (myMappingSpec == null) {
			myMappingSpec = new HashMap<>();
		}
		return myMappingSpec;
	}

	private class MappingVisitor implements IModelVisitor {

		@Override
		public void acceptElement(
				IBaseResource theResource,
				IBase theElement,
				List<String> thePathToElement,
				BaseRuntimeChildDefinition theChildDefinition,
				BaseRuntimeElementDefinition<?> theDefinition) {
			if (myCodeableConceptType.isAssignableFrom(theElement.getClass())) {

				// Find all existing Codings
				Multimap<String, String> foundSystemsToCodes = ArrayListMultimap.create();
				List<IBase> nextCodeableConceptCodings =
						myCodeableConceptCodingChild.getAccessor().getValues(theElement);
				for (IBase nextCodeableConceptCoding : nextCodeableConceptCodings) {
					String system = myCodingSystemChild
							.getAccessor()
							.getFirstValueOrNull(nextCodeableConceptCoding)
							.map(t -> (IPrimitiveType<?>) t)
							.map(IPrimitiveType::getValueAsString)
							.orElse(null);
					String code = myCodingCodeChild
							.getAccessor()
							.getFirstValueOrNull(nextCodeableConceptCoding)
							.map(t -> (IPrimitiveType<?>) t)
							.map(IPrimitiveType::getValueAsString)
							.orElse(null);
					if (StringUtils.isNotBlank(system)
							&& StringUtils.isNotBlank(code)
							&& !foundSystemsToCodes.containsKey(system)) {
						foundSystemsToCodes.put(system, code);
					}
				}

				// Look for mappings
				for (String nextSourceSystem : foundSystemsToCodes.keySet()) {
					String wantTargetSystem = getMappingSpecifications().get(nextSourceSystem);
					if (wantTargetSystem != null) {
						if (!foundSystemsToCodes.containsKey(wantTargetSystem)) {

							for (String code : foundSystemsToCodes.get(nextSourceSystem)) {
								List<IBaseCoding> codings = new ArrayList<>();
								codings.add(createCodingFromPrimitives(nextSourceSystem, code, null));
								TranslateConceptResults translateConceptResults = myValidationSupport.translateConcept(
										new IValidationSupport.TranslateCodeRequest(codings, wantTargetSystem));
								if (translateConceptResults != null) {
									List<TranslateConceptResult> mappings = translateConceptResults.getResults();
									for (TranslateConceptResult nextMapping : mappings) {

										IBase newCoding = createCodingFromPrimitives(
												nextMapping.getSystem(),
												nextMapping.getCode(),
												nextMapping.getDisplay());

										// Add coding to existing CodeableConcept
										myCodeableConceptCodingChild
												.getMutator()
												.addValue(theElement, newCoding);
									}
								}
							}
						}
					}
				}
			}
		}

		private IBaseCoding createCodingFromPrimitives(String system, String code, String display) {
			assert myUriDefinition != null;
			assert myCodeDefinition != null;
			IBaseCoding newCoding = (IBaseCoding) myCodingDefinition.newInstance();
			IPrimitiveType<?> newSystem = myUriDefinition.newInstance(system);
			myCodingSystemChild.getMutator().addValue(newCoding, newSystem);
			IPrimitiveType<?> newCode = myCodeDefinition.newInstance(code);
			myCodingCodeChild.getMutator().addValue(newCoding, newCode);
			if (StringUtils.isNotBlank(display)) {
				assert myStringDefinition != null;
				IPrimitiveType<?> newDisplay = myStringDefinition.newInstance(display);
				myCodingDisplayChild.getMutator().addValue(newCoding, newDisplay);
			}
			return newCoding;
		}
	}
}
