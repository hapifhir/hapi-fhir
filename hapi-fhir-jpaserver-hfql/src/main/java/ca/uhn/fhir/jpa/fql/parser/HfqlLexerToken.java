/*-
 * #%L
 * HAPI FHIR JPA Server - HFQL Driver
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
package ca.uhn.fhir.jpa.fql.parser;

import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

class HfqlLexerToken {

	@Nonnull
	public final String myToken;

	private final int myLine;
	private final int myColumn;

	HfqlLexerToken(@Nonnull String theToken, int theLine, int theColumn) {
		myToken = theToken;
		myLine = theLine;
		myColumn = theColumn;
	}

	@Nonnull
	String getToken() {
		return myToken;
	}

	int getLine() {
		return myLine;
	}

	int getColumn() {
		return myColumn;
	}

	/**
	 * Returns the token as a normalized keyword string. Normalization
	 * returns a capitalized version of the token.
	 */
	@Nonnull
	public String asKeyword() {
		return myToken.toUpperCase(Locale.US);
	}

	@Nonnull
	public String asString() {
		return myToken;
	}

	@Nonnull
	public String describePosition() {
		return "[line=" + getLine() + ", column=" + getColumn() + "]";
	}

	public boolean isQuotedString() {
		return StringUtils.startsWith(myToken, "'") && StringUtils.endsWith(myToken, "'");
	}

	@Override
	public String toString() {
		return myToken;
	}

	public Integer asInteger() throws NumberFormatException {
		return Integer.parseInt(getToken());
	}
}
