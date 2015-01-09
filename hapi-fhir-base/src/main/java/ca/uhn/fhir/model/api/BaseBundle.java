package ca.uhn.fhir.model.api;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.ElementUtil;

public class BaseBundle extends BaseElement implements IElement {

	private StringDt myAuthorName;
	private StringDt myAuthorUri;
	private IdDt myId;

	public StringDt getAuthorName() {
		if (myAuthorName == null) {
			myAuthorName = new StringDt();
		}
		return myAuthorName;
	}

	public StringDt getAuthorUri() {
		if (myAuthorUri == null) {
			myAuthorUri = new StringDt();
		}
		return myAuthorUri;
	}

	public IdDt getId() {
		if (myId==null) {
			myId=new IdDt();
		}
		return myId;
	}

	@Override
	public boolean isEmpty() {
		return ElementUtil.isEmpty(myAuthorName, myAuthorUri);
	}

	public void setId(IdDt theId) {
		myId = theId;
	}

}
