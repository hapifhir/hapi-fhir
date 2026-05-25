/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.patch.ParsedFhirPath;
import ca.uhn.fhir.parser.DataFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FhirPathUtils {

	private static final Logger ourLog = LoggerFactory.getLogger(FhirPathUtils.class);
	/**
	 * Turns an invalid FhirPath into a valid one
	 * -
	 * Do not use this for user input; but it can be used for internally parsed paths
	 */
	public static String cleansePath(String thePath) {
		String path = thePath;

		if (path.startsWith("()")) {
			path = path.substring(2);
		}

		// remove trailing .
		while (path.endsWith(".")) {
			path = path.substring(0, path.length() - 1);
		}

		// remove preceding .
		while (path.startsWith(".")) {
			path = path.substring(1);
		}

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

	/**
	 * Determines whether the given FhirPath expression points to a single-valued element
	 * (cardinality 0..1 or 1..1).
	 *
	 * @param theFhirContext the FhirContext used to look up resource and element definitions
	 * @param thePath the dot-separated FhirPath expression (e.g. {@code "Coverage.beneficiary"})
	 * @return {@code true} if every segment of the FhirPath expression has a maximum cardinality of one;
	 *         {@code false} if any segment is multi-valued (0..*) or unknown
	 */
	public static boolean isPathSingleValued(FhirContext theFhirContext, String thePath) {
		String cleanPath = cleansePath(thePath);
		String[] parts = cleanPath.split("\\.");
		if (parts.length <= 1) {
			return false;
		}
		BaseRuntimeElementCompositeDefinition<?> currentDef;
		try {
			currentDef = theFhirContext.getResourceDefinition(parts[0]);
		} catch (DataFormatException e) {
			return false;
		}
		for (int i = 1; i < parts.length; i++) {
			BaseRuntimeChildDefinition child = currentDef.getChildByName(parts[i]);
			if (child == null) {
				// Choice types are declared as "value[x]" but appear in search-param paths as "value".
				child = currentDef.getChildByName(parts[i] + "[x]");
			}
			if (child == null || child.isMultipleCardinality()) {
				return false;
			}
			if (i < parts.length - 1) {
				if (parts[i + 1].contains("(")) {
					ourLog.debug("Path {} is single-valued with function call.", thePath);
					return true;
				}
				BaseRuntimeElementDefinition<?> elem = child.getChildByName(child.getElementName());
				if (!(elem instanceof BaseRuntimeElementCompositeDefinition)) {
					return false;
				}
				currentDef = (BaseRuntimeElementCompositeDefinition<?>) elem;
			}
		}
		return true;
	}
}
