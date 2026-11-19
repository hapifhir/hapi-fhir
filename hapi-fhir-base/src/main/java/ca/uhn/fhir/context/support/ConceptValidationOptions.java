/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.context.support;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

public class ConceptValidationOptions {

	private boolean myValidateDisplay;
	private boolean myInferSystem;
	private boolean myValueSetUrlUnresolved;

	public static ConceptValidationOptions copy(ConceptValidationOptions theOriginal) {
		if (theOriginal == null) {
			return null;
		}

		ConceptValidationOptions copy = new ConceptValidationOptions();
		copy.setValidateDisplay(theOriginal.isValidateDisplay());
		copy.setInferSystem(theOriginal.isInferSystem());
		copy.setValueSetUrlUnresolved(theOriginal.isValueSetUrlUnresolved());
		return copy;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;
		if (!(theO instanceof ConceptValidationOptions)) return false;
		ConceptValidationOptions that = (ConceptValidationOptions) theO;
		return myValidateDisplay == that.myValidateDisplay
				&& myInferSystem == that.myInferSystem
				&& myValueSetUrlUnresolved == that.myValueSetUrlUnresolved;
	}

	@Override
	public int hashCode() {
		return Objects.hash(myValidateDisplay, myInferSystem, myValueSetUrlUnresolved);
	}

	public boolean isInferSystem() {
		return myInferSystem;
	}

	public ConceptValidationOptions setInferSystem(boolean theInferSystem) {
		myInferSystem = theInferSystem;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("myValidateDisplay", myValidateDisplay)
				.append("inferSystem", myInferSystem)
				.append("valueSetUrlUnresolved", myValueSetUrlUnresolved)
				.append("hashCode", hashCode())
				.toString();
	}

	public boolean isValidateDisplay() {
		return myValidateDisplay;
	}

	public ConceptValidationOptions setValidateDisplay(boolean theValidateDisplay) {
		myValidateDisplay = theValidateDisplay;
		return this;
	}

	/**
	 * @return true if no validation support module holds a ValueSet with the url of the ValueSet given to
	 *    {@link IValidationSupport#validateCodeInValueSet}, such as a ValueSet contained in a profile whose url is
	 *    stored nowhere. A module asked with this set checks the code against the ValueSet given, and does not look
	 *    its url up.
	 * @since 9.2.0
	 */
	public boolean isValueSetUrlUnresolved() {
		return myValueSetUrlUnresolved;
	}

	/**
	 * Set by {@code ValidationSupportChain} when it asks its modules about a ValueSet whose url none of them holds.
	 * Callers do not normally set this.
	 *
	 * @param theValueSetUrlUnresolved true if no validation support module holds the url of the ValueSet given
	 * @return this
	 * @see #isValueSetUrlUnresolved()
	 * @since 9.2.0
	 */
	public ConceptValidationOptions setValueSetUrlUnresolved(boolean theValueSetUrlUnresolved) {
		myValueSetUrlUnresolved = theValueSetUrlUnresolved;
		return this;
	}
}
