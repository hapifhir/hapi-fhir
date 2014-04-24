package ca.uhn.fhir.model.api;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import org.apache.commons.lang3.StringUtils;

public class BundleCategory extends BaseElement implements IElement {

	private String myLabel;
	private String myScheme;
	private String myTerm;

	public String getLabel() {
		return myLabel;
	}

	public String getScheme() {
		return myScheme;
	}

	public String getTerm() {
		return myTerm;
	}

	public void setLabel(String theLabel) {
		myLabel = theLabel;
	}

	public void setScheme(String theScheme) {
		myScheme = theScheme;
	}

	public void setTerm(String theTerm) {
		myTerm = theTerm;
	}

	@Override
	public boolean isEmpty() {
		return StringUtils.isBlank(myLabel) && StringUtils.isBlank(myScheme) && StringUtils.isBlank(myTerm);
	}

}
