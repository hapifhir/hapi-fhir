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
package ca.uhn.fhir.jpa.packages;

import org.hl7.fhir.utilities.npm.NpmPackage;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class NpmPackageUtils {

	private NpmPackageUtils() {
		/* This utility class should not be instantiated */
	}

	/**
	 * Msg structure when an npm package has successfully been added to the cache
	 */
	public static final String SUCCESSFULLY_INSTALLED_MSG_TEMPLATE = "Successfully added package %s#%s to registry";

	public static boolean isSuccessfulMsg(String theMsg) {
		return isNotBlank(theMsg) && theMsg.contains("Successfully added package");
	}

	/**
	 * Retrieve the collection of processing messages from the user data of a package
	 * @param thePackage the NPM package being processed
	 * @return the messages
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getProcessingMessages(NpmPackage thePackage) {
		return (List<String>)
				thePackage.getUserData().computeIfAbsent("JpPackageCache_ProcessingMessages", t -> new ArrayList<>());
	}

	/**
	 * Add a message to the collection of processing messages in the user data of a package.
	 * The new message will be appended at the end of the list.
	 * @param thePackage the NPM package being processed
	 * @param theMessage the message
	 */
	static void addProcessingMessage(NpmPackage thePackage, String theMessage) {
		getProcessingMessages(thePackage).add(theMessage);
	}

	/**
	 * Add a message the collection of processing messages in the user data of a package.
	 * The new message will be inserted at the beginning of the list.
	 * @param thePackage the NPM package being processed
	 * @param theMessage the message
	 */
	static void addFirstProcessingMessage(NpmPackage thePackage, String theMessage) {
		getProcessingMessages(thePackage).add(0, theMessage);
	}
}
