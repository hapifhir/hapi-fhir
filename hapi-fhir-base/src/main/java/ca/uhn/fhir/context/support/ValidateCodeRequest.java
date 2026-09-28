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

import jakarta.annotation.Nullable;

import java.util.Objects;

/**
 * Represents the code being validated by
 * {@link IValidationSupport#validateCode(IValidationSupport.ValidationSupportContext, ConceptValidationOptions, ValidateCodeRequest)}.
 * <p>
 * The code system version is named separately, so the code system here is the bare URL rather than a
 * canonical carrying a version. An implementation which can resolve a specific version should use it; one
 * which cannot may ignore it and answer from whichever version it treats as current.
 * </p>
 *
 * @since 8.14.0
 */
// Created by Claude Opus 5
public class ValidateCodeRequest {
	private final String myCodeSystem;
	private final String myCodeSystemVersion;
	private final String myCode;
	private final String myDisplay;
	private final String myValueSetUrl;

	/**
	 * @param theCodeSystem        The code system, e.g. "<code>http://loinc.org</code>"
	 * @param theCodeSystemVersion The code system version to validate against, e.g. "<code>2.78</code>", or <code>null</code> to use whichever version is current
	 * @param theCode              The code, e.g. "<code>1234-5</code>"
	 * @param theDisplay           The display name, if it should also be validated
	 * @param theValueSetUrl       The value set to validate against, or <code>null</code> to validate against the code system alone
	 */
	public ValidateCodeRequest(
			@Nullable String theCodeSystem,
			@Nullable String theCodeSystemVersion,
			@Nullable String theCode,
			@Nullable String theDisplay,
			@Nullable String theValueSetUrl) {
		myCodeSystem = theCodeSystem;
		myCodeSystemVersion = theCodeSystemVersion;
		myCode = theCode;
		myDisplay = theDisplay;
		myValueSetUrl = theValueSetUrl;
	}

	@Nullable
	public String getCodeSystem() {
		return myCodeSystem;
	}

	@Nullable
	public String getCodeSystemVersion() {
		return myCodeSystemVersion;
	}

	@Nullable
	public String getCode() {
		return myCode;
	}

	@Nullable
	public String getDisplay() {
		return myDisplay;
	}

	@Nullable
	public String getValueSetUrl() {
		return myValueSetUrl;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;
		if (!(theO instanceof ValidateCodeRequest)) return false;
		ValidateCodeRequest that = (ValidateCodeRequest) theO;
		return Objects.equals(myCodeSystem, that.myCodeSystem)
				&& Objects.equals(myCodeSystemVersion, that.myCodeSystemVersion)
				&& Objects.equals(myCode, that.myCode)
				&& Objects.equals(myDisplay, that.myDisplay)
				&& Objects.equals(myValueSetUrl, that.myValueSetUrl);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myCodeSystem, myCodeSystemVersion, myCode, myDisplay, myValueSetUrl);
	}

	@Override
	public String toString() {
		return "ValidateCodeRequest{" + "myCodeSystem='"
				+ myCodeSystem + '\'' + ", myCodeSystemVersion='"
				+ myCodeSystemVersion + '\'' + ", myCode='"
				+ myCode + '\'' + ", myDisplay='"
				+ myDisplay + '\'' + ", myValueSetUrl='"
				+ myValueSetUrl + '\'' + '}';
	}
}
