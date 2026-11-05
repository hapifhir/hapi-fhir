/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.api.server.bulk;

import java.util.ArrayList;
import java.util.List;

public class ConvertedFiles {

	private List<ConvertedFile> myFiles;

	/**
	 * Retrieves the current list of converted files
	 * (never null).
	 * We might want to consider making this a stream instead of a list
	 * if customers decide to convert our list of resources into an
	 * even larger list of "contents".
	 */
	public List<ConvertedFile> getFiles() {
		if (myFiles == null) {
			myFiles = new ArrayList<>();
		}
		return myFiles;
	}

	/**
	 * Adds a converted file to the collection
	 */
	public ConvertedFiles addFile(ConvertedFile theFile) {
		getFiles().add(theFile);
		return this;
	}
}
