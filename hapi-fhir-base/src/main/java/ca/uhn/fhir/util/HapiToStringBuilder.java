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
package ca.uhn.fhir.util;

import jakarta.annotation.Nullable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * Added functionality to {@link ToStringBuilder}
 *
 * @since 8.12.0
 */
public class HapiToStringBuilder extends ToStringBuilder {

	public HapiToStringBuilder(Object theObject, ToStringStyle theStyle) {
		super(theObject, theStyle);
	}

	/**
	 * Appends a date with FHIR serialization (ISO-8601, e.g. <code>2022-01-01T00:00:00.000Z</code>).
	 * Appends the string <code>null</code> if the value is <code>null</code>.
	 */
	public HapiToStringBuilder append(String theFieldName, @Nullable Date theValue) {
		if (theValue != null) {
			append(theFieldName, DateUtils.convertDateToIso8601String(theValue));
		} else {
			super.append(theFieldName, (String) null);
		}
		return this;
	}

	/**
	 * Performs an {@link #append(String, int)} if {@literal theValue != 0}
	 */
	public HapiToStringBuilder appendIfNonZero(String theFieldName, int theValue) {
		if (theValue != 0) {
			append(theFieldName, theValue);
		}
		return this;
	}
}
