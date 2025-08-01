/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.fhirpath;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.List;

public interface IFhirPathEvaluationContext {

	/**
	 * The is an adapter for org.hl7.fhir.utilities.fhirpath.FHIRPathConstantEvaluationMode. At present, it matches
	 * org.hl7.fhir.core 1-to-1.
	 * </br>
	 * Comments are provided here for convenience, but org.hl7.fhir.utilities.fhirpath.FHIRPathConstantEvaluationMode
	 * should be considered the source of truth.
	 *
	 */
	public enum ConstantEvaluationMode {
		EXPLICIT, // the FHIRPathEngine has encountered an explicit reference to a constant e.g. %{token} that it does
		// not recognise internally
		NOVALUE, // the FHIRPathEngine was invoked with no focus provided
		IMPLICIT_BEFORE, // The FHIRPath engine is about to evaluate a named property reference, but the Host
		// Application is being offered an opportunity to provide it's own value first
		IMPLICIT_AFTER // The FHIRPath engine has evaluated a property and found nothing, and perhaps the Host
		// Application wants to offer a value (constant fall through). This only happens if
		// checkWithHostServicesBeforeHand is true on the FHIRPath engine
	}

	/**
	 * Evaluates the <code>resolve()</code> function and returns the target
	 * of the resolution.
	 *
	 * @param theReference The reference
	 * @param theContext The entity containing the reference. Note that this will be <code>null</code> for FHIR versions R4 and below.
	 */
	default IBase resolveReference(@Nonnull IIdType theReference, @Nullable IBase theContext) {
		return null;
	}

	/**
	 *
	 * @param appContext
	 * @param name The name of the constant(s) to be resolved
	 * @param mode
	 * @return
	 */
	default List<IBase> resolveConstant(Object appContext, String name, ConstantEvaluationMode mode) {
		return null;
	}
}
