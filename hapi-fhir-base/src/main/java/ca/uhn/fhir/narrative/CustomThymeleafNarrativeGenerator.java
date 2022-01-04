package ca.uhn.fhir.narrative;

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

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.Validate;

public class CustomThymeleafNarrativeGenerator extends BaseThymeleafNarrativeGenerator {

	private List<String> myPropertyFile;

	/**
	 * Create a new narrative generator
	 * 
	 * @param thePropertyFile
	 *            The name of the property file, in one of the following formats:
	 *            <ul>
	 *            <li>file:/path/to/file/file.properties</li>
	 *            <li>classpath:/com/package/file.properties</li>
	 *            </ul>
	 */
	public CustomThymeleafNarrativeGenerator(String... thePropertyFile) {
		super();
		setPropertyFile(thePropertyFile);
	}

	/**
	 * Set the property file to use
	 * 
	 * @param thePropertyFile
	 *            The name of the property file, in one of the following formats:
	 *            <ul>
	 *            <li>file:/path/to/file/file.properties</li>
	 *            <li>classpath:/com/package/file.properties</li>
	 *            </ul>
	 */
	public void setPropertyFile(String... thePropertyFile) {
		Validate.notNull(thePropertyFile, "Property file can not be null");
		myPropertyFile = Arrays.asList(thePropertyFile);
	}

	@Override
	public List<String> getPropertyFile() {
		return myPropertyFile;
	}

}
