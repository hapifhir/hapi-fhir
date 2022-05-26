package ca.uhn.fhir.rest.server.interceptor;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeDefinition;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.TranslateConceptResult;
import ca.uhn.fhir.context.support.TranslateConceptResults;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.IModelVisitor;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
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

import static ca.uhn.fhir.rest.server.interceptor.InterceptorOrders.RESPONSE_TERMINOLOGY_TRANSLATION_INTERCEPTOR;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This interceptor leverages ConceptMap resources stored in the repository to automatically map
 * terminology from one CodeSystem to another at runtime, in resources that are being
 * returned by the server.
 * <p>
 * Mappings are applied only if they are explicitly configured in the interceptor via
 * the {@link #addMappingSpecification(String, String)} method.
 * </p>
 *
 * @since 5.4.0
 */
public class ResponseTerminologyTranslationInterceptor extends BaseResponseTerminologyInterceptor {

	private final BaseRuntimeChildDefinition myCodingSystemChild;
	private final BaseRuntimeChildDefinition myCodingCodeChild;
	private final BaseRuntimeElementDefinition<IPrimitiveType<?>> myUriDefinition;
	private final BaseRuntimeElementDefinition<IPrimitiveType<?>> myCodeDefinition;
	private final Class<? extends IBase> myCodeableConceptType;
	private final Class<? extends IBase> myCodingType;
	private final BaseRuntimeChildDefinition myCodeableConceptCodingChild;
	private final BaseRuntimeElementCompositeDefinition<?> myCodingDefinitition;
	private final RuntimePrimitiveDatatypeDefinition myStringDefinition;
	private final BaseRuntimeChildDefinition myCodingDisplayChild;
	private Map<String, String> myMappingSpecifications = new HashMap<>();

	/**
	 * Constructor
	 *
	 * @param theValidationSupport The validation support module
	 */
	public ResponseTerminologyTranslationInterceptor(IValidationSupport theValidationSupport) {
		super(theValidationSupport);

		BaseRuntimeElementCompositeDefinition<?> codeableConceptDef = (BaseRuntimeElementCompositeDefinition<?>) Objects.requireNonNull(myContext.getElementDefinition("CodeableConcept"));
		myCodeableConceptType = codeableConceptDef.getImplementingClass();
		myCodeableConceptCodingChild = codeableConceptDef.getChildByName("coding");

		myCodingDefinitition = (BaseRuntimeElementCompositeDefinition<?>) Objects.requireNonNull(myContext.getElementDefinition("Coding"));
		myCodingType = myCodingDefinitition.getImplementingClass();
		myCodingSystemChild = myCodingDefinitition.getChildByName("system");
		myCodingCodeChild = myCodingDefinitition.getChildByName("code");
		myCodingDisplayChild = myCodingDefinitition.getChildByName("display");

		myUriDefinition = (RuntimePrimitiveDatatypeDefinition) myContext.getElementDefinition("uri");
		myCodeDefinition = (RuntimePrimitiveDatatypeDefinition) myContext.getElementDefinition("code");
		myStringDefinition = (RuntimePrimitiveDatatypeDefinition) myContext.getElementDefinition("string");
	}

	/**
	 * Adds a mapping specification using only a source and target CodeSystem URL. Any mappings specified using
	 * this URL
	 *
	 * @param theSourceCodeSystemUrl The source CodeSystem URL
	 * @param theTargetCodeSystemUrl The target CodeSystem URL
	 */
	public void addMappingSpecification(String theSourceCodeSystemUrl, String theTargetCodeSystemUrl) {
		Validate.notBlank(theSourceCodeSystemUrl, "theSourceCodeSystemUrl must not be null or blank");
		Validate.notBlank(theTargetCodeSystemUrl, "theTargetCodeSystemUrl must not be null or blank");

		myMappingSpecifications.put(theSourceCodeSystemUrl, theTargetCodeSystemUrl);
	}

	/**
	 * Clear all mapping specifications
	 */
	public void clearMappingSpecifications() {
		myMappingSpecifications.clear();
	}


	@Hook(value = Pointcut.SERVER_OUTGOING_RESPONSE, order = RESPONSE_TERMINOLOGY_TRANSLATION_INTERCEPTOR)
	public void handleResource(RequestDetails theRequestDetails, IBaseResource theResource) {
		List<IBaseResource> resources = toListForProcessing(theRequestDetails, theResource);

		FhirTerser terser = myContext.newTerser();
		for (IBaseResource nextResource : resources) {
			terser.visit(nextResource, new MappingVisitor());
		}

	}


	private class MappingVisitor implements IModelVisitor {

		@Override
		public void acceptElement(IBaseResource theResource, IBase theElement, List<String> thePathToElement, BaseRuntimeChildDefinition theChildDefinition, BaseRuntimeElementDefinition<?> theDefinition) {
			if (myCodeableConceptType.isAssignableFrom(theElement.getClass())) {

				// Find all existing Codings
				Multimap<String, String> foundSystemsToCodes = ArrayListMultimap.create();
				List<IBase> nextCodeableConceptCodings = myCodeableConceptCodingChild.getAccessor().getValues(theElement);
				for (IBase nextCodeableConceptCoding : nextCodeableConceptCodings) {
					String system = myCodingSystemChild.getAccessor().getFirstValueOrNull(nextCodeableConceptCoding).map(t -> (IPrimitiveType<?>) t).map(t -> t.getValueAsString()).orElse(null);
					String code = myCodingCodeChild.getAccessor().getFirstValueOrNull(nextCodeableConceptCoding).map(t -> (IPrimitiveType<?>) t).map(t -> t.getValueAsString()).orElse(null);
					if (isNotBlank(system) && isNotBlank(code) && !foundSystemsToCodes.containsKey(system)) {
						foundSystemsToCodes.put(system, code);
					}
				}

				// Look for mappings
				for (String nextSourceSystem : foundSystemsToCodes.keySet()) {
					String wantTargetSystem = myMappingSpecifications.get(nextSourceSystem);
					if (wantTargetSystem != null) {
						if (!foundSystemsToCodes.containsKey(wantTargetSystem)) {

							for (String code : foundSystemsToCodes.get(nextSourceSystem)) {
								List<IBaseCoding> codings = new ArrayList<IBaseCoding>();
								codings.add(createCodingFromPrimitives(nextSourceSystem, code, null));
								TranslateConceptResults translateConceptResults = myValidationSupport.translateConcept(new IValidationSupport.TranslateCodeRequest(codings, wantTargetSystem));
								if (translateConceptResults != null) {
									List<TranslateConceptResult> mappings = translateConceptResults.getResults();
									for (TranslateConceptResult nextMapping : mappings) {

										IBase newCoding = createCodingFromPrimitives(
											nextMapping.getSystem(),
											nextMapping.getCode(),
											nextMapping.getDisplay());

										// Add coding to existing CodeableConcept
										myCodeableConceptCodingChild.getMutator().addValue(theElement, newCoding);

									}
								}
							}
						}
					}
				}

			}

		}

		private IBaseCoding createCodingFromPrimitives(String system, String code, String display) {
			IBaseCoding newCoding = (IBaseCoding) myCodingDefinitition.newInstance();
			IPrimitiveType<?> newSystem = myUriDefinition.newInstance(system);
			myCodingSystemChild.getMutator().addValue(newCoding, newSystem);
			IPrimitiveType<?> newCode = myCodeDefinition.newInstance(code);
			myCodingCodeChild.getMutator().addValue(newCoding, newCode);
			if (isNotBlank(display)) {
				IPrimitiveType<?> newDisplay = myStringDefinition.newInstance(display);
				myCodingDisplayChild.getMutator().addValue(newCoding, newDisplay);
			}
			return newCoding;
		}

	}
}
