package ca.uhn.fhir.util;

/*
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

import java.util.StringTokenizer;

public class UrlPathTokenizer {

	private final StringTokenizer myTok;

	public UrlPathTokenizer(String theRequestPath) {
		myTok = new StringTokenizer(theRequestPath, "/");
	}

	public boolean hasMoreTokens() {
		return myTok.hasMoreTokens();
	}

	/**
	 * Returns the next portion. Any URL-encoding is undone, but we will
	 * HTML encode the &lt; and &quot; marks since they are both
	 * not useful un URL paths in FHIR and potentially represent injection
	 * attacks.
	 *
	 * @see UrlUtil#sanitizeUrlPart(String)
	 * @see UrlUtil#unescape(String)
	 */
	public String nextTokenUnescapedAndSanitized() {
		return UrlUtil.sanitizeUrlPart(UrlUtil.unescape(myTok.nextToken()));
	}

}
