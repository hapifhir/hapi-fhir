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
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.IModelVisitor;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.List;
import java.util.Objects;

import static ca.uhn.fhir.rest.server.interceptor.InterceptorOrders.RESPONSE_TERMINOLOGY_DISPLAY_POPULATION_INTERCEPTOR;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This interceptor looks for coded data (
 *
 * @since 5.4.0
 */
public class ResponseTerminologyDisplayPopulationInterceptor extends BaseResponseTerminologyInterceptor {

	private final BaseRuntimeChildDefinition myCodingSystemChild;
	private final BaseRuntimeChildDefinition myCodingCodeChild;
	private final Class<? extends IBase> myCodingType;
	private final BaseRuntimeElementCompositeDefinition<?> myCodingDefinitition;
	private final BaseRuntimeChildDefinition myCodingDisplayChild;
	private final RuntimePrimitiveDatatypeDefinition myStringDefinition;

	/**
	 * Constructor
	 *
	 * @param theValidationSupport The validation support module
	 */
	public ResponseTerminologyDisplayPopulationInterceptor(IValidationSupport theValidationSupport) {
		super(theValidationSupport);

		myCodingDefinitition = (BaseRuntimeElementCompositeDefinition<?>) Objects.requireNonNull(myContext.getElementDefinition("Coding"));
		myCodingType = myCodingDefinitition.getImplementingClass();
		myCodingSystemChild = myCodingDefinitition.getChildByName("system");
		myCodingCodeChild = myCodingDefinitition.getChildByName("code");
		myCodingDisplayChild = myCodingDefinitition.getChildByName("display");

		myStringDefinition = (RuntimePrimitiveDatatypeDefinition) myContext.getElementDefinition("string");
	}

	@Hook(value = Pointcut.SERVER_OUTGOING_RESPONSE, order = RESPONSE_TERMINOLOGY_DISPLAY_POPULATION_INTERCEPTOR)
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
			if (myCodingType.isAssignableFrom(theElement.getClass())) {
				String system = myCodingSystemChild.getAccessor().getFirstValueOrNull(theElement).map(t -> (IPrimitiveType<?>) t).map(t -> t.getValueAsString()).orElse(null);
				String code = myCodingCodeChild.getAccessor().getFirstValueOrNull(theElement).map(t -> (IPrimitiveType<?>) t).map(t -> t.getValueAsString()).orElse(null);
				if (isBlank(system) || isBlank(code)) {
					return;
				}

				String display = myCodingDisplayChild.getAccessor().getFirstValueOrNull(theElement).map(t -> (IPrimitiveType<?>) t).map(t -> t.getValueAsString()).orElse(null);
				if (isNotBlank(display)) {
					return;
				}

				ValidationSupportContext validationSupportContext = new ValidationSupportContext(myValidationSupport);
				if (myValidationSupport.isCodeSystemSupported(validationSupportContext, system)) {

					IValidationSupport.LookupCodeResult lookupCodeResult = myValidationSupport.lookupCode(validationSupportContext, system, code);
					if (lookupCodeResult != null && lookupCodeResult.isFound()) {
						String newDisplay = lookupCodeResult.getCodeDisplay();
						IPrimitiveType<?> newString = myStringDefinition.newInstance(newDisplay);
						myCodingDisplayChild.getMutator().addValue(theElement, newString);
					}

				}
			}

		}

	}

}
