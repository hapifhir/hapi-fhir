package ca.uhn.fhir.model.base.composite;

import java.util.List;

import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.util.CoverageIgnore;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

public abstract class BaseContainedDt implements IDatatype {

	private static final long serialVersionUID = 1L;

	public abstract List<? extends IResource> getContainedResources();

	/**
	 * NOT SUPPORTED - Throws {@link UnsupportedOperationException}
	 */
	@Override
	@CoverageIgnore
	public List<String> getFormatCommentsPost() {
		throw new UnsupportedOperationException();
	}

	/**
	 * NOT SUPPORTED - Throws {@link UnsupportedOperationException}
	 */
	@Override
	@CoverageIgnore
	public List<String> getFormatCommentsPre() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns false
	 */
	@Override
	@CoverageIgnore
	public boolean hasFormatComment() {
		return false;
	}

}
