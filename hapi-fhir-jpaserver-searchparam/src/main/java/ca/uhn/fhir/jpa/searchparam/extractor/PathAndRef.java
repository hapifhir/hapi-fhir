/*
 * #%L
 * HAPI FHIR JPA - Search Parameters
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
package ca.uhn.fhir.jpa.searchparam.extractor;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class PathAndRef {

	private final String myPath;
	private final IBaseReference myRef;
	private final IBaseResource myResource;
	private final String mySearchParamName;
	private final boolean myCanonical;

	/**
	 * Constructor for a reference
	 */
	public PathAndRef(String theSearchParamName, String thePath, IBaseReference theRef, boolean theCanonical) {
		super();
		mySearchParamName = theSearchParamName;
		myPath = thePath;
		myRef = theRef;
		myCanonical = theCanonical;
		myResource = null;
	}

	/**
	 * Constructor for a resource (this is expected to be rare, only really covering
	 * cases like the path Bundle.entry.resource)
	 */
	public PathAndRef(String theSearchParamName, String thePath, IBaseResource theResource) {
		super();
		mySearchParamName = theSearchParamName;
		myPath = thePath;
		myRef = null;
		myCanonical = false;
		myResource = theResource;
	}

	/**
	 * Note that this will generally be null, it is only used for cases like
	 * indexing {@literal Bundle.entry.resource}. If this is populated, {@link #getRef()}
	 * will be null and vice versa.
	 *
	 * @since 6.6.0
	 */
	public IBaseResource getResource() {
		return myResource;
	}

	public boolean isCanonical() {
		return myCanonical;
	}

	public String getSearchParamName() {
		return mySearchParamName;
	}

	public String getPath() {
		return myPath;
	}

	/**
	 * If this is populated, {@link #getResource()} will be null, and vice versa.
	 */
	public IBaseReference getRef() {
		return myRef;
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("paramName", mySearchParamName);
		if (myRef != null && myRef.getReferenceElement() != null) {
			b.append("ref", myRef.getReferenceElement().getValue());
		}
		b.append("path", myPath);
		b.append("resource", myResource);
		b.append("canonical", myCanonical);
		return b.toString();
	}
}
