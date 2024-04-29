/*
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.narrative;

import ca.uhn.fhir.narrative2.NarrativeTemplateManifest;
import org.apache.commons.lang3.Validate;

import java.util.Arrays;
import java.util.List;

public class CustomThymeleafNarrativeGenerator extends BaseThymeleafNarrativeGenerator {

	private volatile List<String> myPropertyFile;
	private volatile NarrativeTemplateManifest myManifest;

	/**
	 * Constructor. If this constructor is used you must explicitly call
	 * {@link #setManifest(NarrativeTemplateManifest)} to provide a template
	 * manifest before using the generator.
	 */
	public CustomThymeleafNarrativeGenerator() {
		super();
	}

	/**
	 * Create a new narrative generator
	 *
	 * @param theNarrativePropertyFiles The name of the property file, in one of the following formats:
	 *                                  <ul>
	 *                                  <li>file:/path/to/file/file.properties</li>
	 *                                  <li>classpath:/com/package/file.properties</li>
	 *                                  </ul>
	 */
	public CustomThymeleafNarrativeGenerator(String... theNarrativePropertyFiles) {
		this();
		setPropertyFile(theNarrativePropertyFiles);
	}

	/**
	 * Create a new narrative generator
	 *
	 * @param theNarrativePropertyFiles The name of the property file, in one of the following formats:
	 *                                  <ul>
	 *                                  <li>file:/path/to/file/file.properties</li>
	 *                                  <li>classpath:/com/package/file.properties</li>
	 *                                  </ul>
	 */
	public CustomThymeleafNarrativeGenerator(List<String> theNarrativePropertyFiles) {
		this(theNarrativePropertyFiles.toArray(new String[0]));
	}

	@Override
	public NarrativeTemplateManifest getManifest() {
		NarrativeTemplateManifest retVal = myManifest;
		if (myManifest == null) {
			Validate.isTrue(myPropertyFile != null, "Neither a property file or a manifest has been provided");
			retVal = NarrativeTemplateManifest.forManifestFileLocation(myPropertyFile);
			setManifest(retVal);
		}
		return retVal;
	}

	public void setManifest(NarrativeTemplateManifest theManifest) {
		myManifest = theManifest;
	}

	/**
	 * Set the property file to use
	 *
	 * @param thePropertyFile The name of the property file, in one of the following formats:
	 *                        <ul>
	 *                        <li>file:/path/to/file/file.properties</li>
	 *                        <li>classpath:/com/package/file.properties</li>
	 *                        </ul>
	 */
	public void setPropertyFile(String... thePropertyFile) {
		Validate.notNull(thePropertyFile, "Property file can not be null");
		myPropertyFile = Arrays.asList(thePropertyFile);
		myManifest = null;
	}
}
