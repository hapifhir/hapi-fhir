/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.jpa.patch.ParsedFhirPath;

public class FhirPathUtils {
	/**
	 * Turns an invalid FhirPath into a valid one
	 * -
	 * Do not use this for user input; but it can be used for internally parsed paths
	 */
	public static String cleansePath(String thePath) {
		String path = thePath;

		// remove trailing .
		while (path.endsWith(".")) {
			path = path.substring(0, path.length() - 1);
		}

		// remove preceding .
		while (path.startsWith(".")) {
			path = path.substring(1);
		}

		// balance brackets
		int openBrace = path.indexOf("(");
		String remainder = path;
		int endingIndex = openBrace == -1 ? path.length() : 0;
		while (openBrace != -1) {
			int closing = RandomTextUtils.findMatchingClosingBrace(openBrace, remainder);
			endingIndex += closing + 1; // +1 because substring ending is exclusive
			remainder = remainder.substring(closing + 1);
			openBrace = remainder.indexOf("(");
		}
		path = path.substring(0, endingIndex);

		return path;
	}

	/**
	 * Determines if the node is a subsetting node
	 * as described by http://hl7.org/fhirpath/N1/#subsetting
	 */
	public static boolean isSubsettingNode(ParsedFhirPath.FhirPathNode theNode) {
		if (theNode.getListIndex() >= 0) {
			return true;
		}
		if (theNode.isFunction()) {
			String funName = theNode.getValue();
			switch (funName) {
				case "first", "last", "single", "tail", "skip", "take", "exclude", "intersect" -> {
					return true;
				}
			}
		}
		return false;
	}
}
