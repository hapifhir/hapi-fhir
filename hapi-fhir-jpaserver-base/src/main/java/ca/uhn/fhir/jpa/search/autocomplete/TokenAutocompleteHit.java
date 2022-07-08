package ca.uhn.fhir.jpa.search.autocomplete;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Nonnull;

/**
 * A single autocomplete search hit.
 */
class TokenAutocompleteHit {
	@Nonnull
	final String mySystemCode;
	final String myDisplayText;

	TokenAutocompleteHit(@Nonnull String theSystemCode, String theDisplayText) {
		Validate.notEmpty(theSystemCode);
		mySystemCode = theSystemCode;
		myDisplayText = theDisplayText;
	}

	@Nonnull
	public String getSystemCode() {
		return mySystemCode;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("mySystemCode", mySystemCode)
			.append("myDisplayText", myDisplayText)
			.toString();
	}
}
