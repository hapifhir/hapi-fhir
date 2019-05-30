package ca.uhn.fhir.context;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

import java.util.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;

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

public class RuntimeSearchParam {
	private final IIdType myId;
	private final Set<String> myBase;
	private final List<RuntimeSearchParam> myCompositeOf;
	private final String myDescription;
	private final String myName;
	private final RestSearchParameterTypeEnum myParamType;
	private final String myPath;
	private final Set<String> myTargets;

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("base", myBase)
			.append("name", myName)
			.append("path", myPath)
			.append("id", myId)
			.append("uri", myUri)
			.toString();
	}

	private final Set<String> myProvidesMembershipInCompartments;
	private final RuntimeSearchParamStatusEnum myStatus;
	private final String myUri;

	public IIdType getId() {
		return myId;
	}

	public String getUri() {
		return myUri;
	}

	public RuntimeSearchParam(IIdType theId, String theUri, String theName, String theDescription, String thePath, RestSearchParameterTypeEnum theParamType, List<RuntimeSearchParam> theCompositeOf,
									  Set<String> theProvidesMembershipInCompartments, Set<String> theTargets, RuntimeSearchParamStatusEnum theStatus) {
		this(theId, theUri, theName, theDescription, thePath, theParamType, theCompositeOf, theProvidesMembershipInCompartments, theTargets, theStatus, null);
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (theO == null || getClass() != theO.getClass()) return false;

		RuntimeSearchParam that = (RuntimeSearchParam) theO;

		return new EqualsBuilder()
			.append(getId(), that.getId())
			.append(getName(), that.getName())
			.append(getPath(), that.getPath())
			.append(getUri(), that.getUri())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(getId())
			.append(getName())
			.append(getPath())
			.append(getUri())
			.toHashCode();
	}

	public RuntimeSearchParam(IIdType theId, String theUri, String theName, String theDescription, String thePath, RestSearchParameterTypeEnum theParamType, List<RuntimeSearchParam> theCompositeOf,
									  Set<String> theProvidesMembershipInCompartments, Set<String> theTargets, RuntimeSearchParamStatusEnum theStatus, Collection<String> theBase) {
		super();

		myId = theId;
		myUri = theUri;
		myName = theName;
		myDescription = theDescription;
		myPath = thePath;
		myParamType = theParamType;
		myCompositeOf = theCompositeOf;
		myStatus = theStatus;
		if (theProvidesMembershipInCompartments != null && !theProvidesMembershipInCompartments.isEmpty()) {
			myProvidesMembershipInCompartments = Collections.unmodifiableSet(theProvidesMembershipInCompartments);
		} else {
			myProvidesMembershipInCompartments = null;
		}
		if (theTargets != null && theTargets.isEmpty() == false) {
			myTargets = Collections.unmodifiableSet(theTargets);
		} else {
			myTargets = null;
		}

		if (theBase == null || theBase.isEmpty()) {
			HashSet<String> base = new HashSet<>();
			if (isNotBlank(thePath)) {
				int indexOf = thePath.indexOf('.');
				if (indexOf != -1) {
					base.add(trim(thePath.substring(0, indexOf)));
				}
			}
			myBase = Collections.unmodifiableSet(base);
		} else {
			myBase = Collections.unmodifiableSet(new HashSet<>(theBase));
		}
	}

	public Set<String> getBase() {
		return myBase;
	}

	public Set<String> getTargets() {
		return myTargets;
	}

	public RuntimeSearchParamStatusEnum getStatus() {
		return myStatus;
	}

	public RuntimeSearchParam(String theName, String theDescription, String thePath, RestSearchParameterTypeEnum theParamType, Set<String> theProvidesMembershipInCompartments, Set<String> theTargets, RuntimeSearchParamStatusEnum theStatus) {
		this(null, null, theName, theDescription, thePath, theParamType, null, theProvidesMembershipInCompartments, theTargets, theStatus);
	}

	public List<RuntimeSearchParam> getCompositeOf() {
		return myCompositeOf;
	}

	public String getDescription() {
		return myDescription;
	}

	public String getName() {
		return myName;
	}

	public RestSearchParameterTypeEnum getParamType() {
		return myParamType;
	}

	public String getPath() {
		return myPath;
	}

	public List<String> getPathsSplit() {
		String path = getPath();
		if (path.indexOf('|') == -1) {
			return Collections.singletonList(path);
		}

		List<String> retVal = new ArrayList<String>();
		StringTokenizer tok = new StringTokenizer(path, "|");
		while (tok.hasMoreElements()) {
			String nextPath = tok.nextToken().trim();
			retVal.add(nextPath.trim());
		}
		return retVal;
	}

	/**
	 * Can return null
	 */
	public Set<String> getProvidesMembershipInCompartments() {
		return myProvidesMembershipInCompartments;
	}

	public enum RuntimeSearchParamStatusEnum {
		ACTIVE,
		DRAFT,
		RETIRED,
		UNKNOWN
	}
	
}
