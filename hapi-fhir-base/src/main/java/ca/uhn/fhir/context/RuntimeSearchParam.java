/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.context;

import ca.uhn.fhir.context.phonetic.IPhoneticEncoder;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class RuntimeSearchParam {
	private final IIdType myId;
	private final Set<String> myBase;
	private final String myDescription;
	private final String myName;
	private final RestSearchParameterTypeEnum myParamType;
	private final String myPath;
	private final Set<String> myTargets;
	private final Set<String> myProvidesMembershipInCompartments;
	private final RuntimeSearchParamStatusEnum myStatus;
	private final String myUri;
	private final Map<String, List<IBaseExtension<?, ?>>> myExtensions = new HashMap<>();
	private final Map<String, String> myUpliftRefchains = new HashMap<>();
	private final ComboSearchParamType myComboSearchParamType;
	private final List<Component> myComponents;
	private final IIdType myIdUnqualifiedVersionless;
	private IPhoneticEncoder myPhoneticEncoder;
	private boolean myEnabledForSearching = true;

	/**
	 * Constructor
	 */
	public RuntimeSearchParam(
			IIdType theId,
			String theUri,
			String theName,
			String theDescription,
			String thePath,
			RestSearchParameterTypeEnum theParamType,
			Set<String> theProvidesMembershipInCompartments,
			Set<String> theTargets,
			RuntimeSearchParamStatusEnum theStatus,
			Collection<String> theBase) {
		this(
				theId,
				theUri,
				theName,
				theDescription,
				thePath,
				theParamType,
				theProvidesMembershipInCompartments,
				theTargets,
				theStatus,
				null,
				Collections.emptyList(),
				theBase);
	}

	/**
	 * Copy constructor
	 */
	public RuntimeSearchParam(RuntimeSearchParam theSp) {
		this(
				theSp.getId(),
				theSp.getUri(),
				theSp.getName(),
				theSp.getDescription(),
				theSp.getPath(),
				theSp.getParamType(),
				theSp.getProvidesMembershipInCompartments(),
				theSp.getTargets(),
				theSp.getStatus(),
				theSp.getComboSearchParamType(),
				theSp.getComponents(),
				theSp.getBase());
	}

	/**
	 * Constructor
	 */
	public RuntimeSearchParam(
			IIdType theId,
			String theUri,
			String theName,
			String theDescription,
			String thePath,
			RestSearchParameterTypeEnum theParamType,
			Set<String> theProvidesMembershipInCompartments,
			Set<String> theTargets,
			RuntimeSearchParamStatusEnum theStatus,
			ComboSearchParamType theComboSearchParamType,
			List<Component> theComponents,
			Collection<String> theBase) {
		super();

		myId = theId;
		myIdUnqualifiedVersionless = theId != null ? theId.toUnqualifiedVersionless() : null;
		myUri = theUri;
		myName = theName;
		myDescription = theDescription;
		myPath = thePath;
		myParamType = theParamType;
		myStatus = theStatus;
		if (theProvidesMembershipInCompartments != null && !theProvidesMembershipInCompartments.isEmpty()) {
			myProvidesMembershipInCompartments = Collections.unmodifiableSet(theProvidesMembershipInCompartments);
		} else {
			myProvidesMembershipInCompartments = null;
		}
		if (theTargets != null && theTargets.isEmpty() == false) {
			myTargets = Collections.unmodifiableSet(theTargets);
		} else {
			myTargets = Collections.emptySet();
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
		myComboSearchParamType = theComboSearchParamType;
		if (theComponents != null) {
			myComponents = Collections.unmodifiableList(theComponents);
		} else {
			myComponents = Collections.emptyList();
		}
	}

	/**
	 * Is this search parameter actually enabled for being used in searches (as opposed to only being used for
	 * generating indexes, which might be desired while the search parameter is still being indexed). This
	 * setting defaults to {@literal true} if it isn't set otherwise.
	 */
	public boolean isEnabledForSearching() {
		return myEnabledForSearching;
	}

	/**
	 * Is this search parameter actually enabled for being used in searches (as opposed to only being used for
	 * generating indexes, which might be desired while the search parameter is still being indexed). This
	 * setting defaults to {@literal true} if it isn't set otherwise.
	 */
	public void setEnabledForSearching(boolean theEnabledForSearching) {
		myEnabledForSearching = theEnabledForSearching;
	}

	public List<Component> getComponents() {
		return myComponents;
	}

	/**
	 * Returns <code>null</code> if this is not a combo search param type
	 */
	@Nullable
	public ComboSearchParamType getComboSearchParamType() {
		return myComboSearchParamType;
	}

	/**
	 * Retrieve user data - This can be used to store any application-specific data
	 */
	@Nonnull
	public List<IBaseExtension<?, ?>> getExtensions(String theKey) {
		List<IBaseExtension<?, ?>> retVal = myExtensions.get(theKey);
		if (retVal != null) {
			retVal = Collections.unmodifiableList(retVal);
		} else {
			retVal = Collections.emptyList();
		}
		return retVal;
	}

	/**
	 * Sets user data - This can be used to store any application-specific data
	 */
	public RuntimeSearchParam addExtension(String theKey, IBaseExtension<?, ?> theValue) {
		List<IBaseExtension<?, ?>> valuesList = myExtensions.computeIfAbsent(theKey, k -> new ArrayList<>());
		valuesList.add(theValue);
		return this;
	}

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

	public IIdType getId() {
		return myId;
	}

	public IIdType getIdUnqualifiedVersionless() {
		return myIdUnqualifiedVersionless;
	}

	public String getUri() {
		return myUri;
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

	public Set<String> getBase() {
		return myBase;
	}

	@Nonnull
	public Set<String> getTargets() {
		return myTargets;
	}

	public boolean hasTargets() {
		return !myTargets.isEmpty();
	}

	public RuntimeSearchParamStatusEnum getStatus() {
		return myStatus;
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
		return getPathsSplitForResourceType(null);
	}

	/**
	 * Can return null
	 */
	public Set<String> getProvidesMembershipInCompartments() {
		return myProvidesMembershipInCompartments;
	}

	public RuntimeSearchParam setPhoneticEncoder(IPhoneticEncoder thePhoneticEncoder) {
		myPhoneticEncoder = thePhoneticEncoder;
		return this;
	}

	public String encode(String theString) {
		if (myPhoneticEncoder == null || theString == null) {
			return theString;
		}
		return myPhoneticEncoder.encode(theString);
	}

	public List<String> getPathsSplitForResourceType(@Nullable String theResourceName) {
		String path = getPath();
		if (path.indexOf('|') == -1) {
			if (theResourceName != null && !pathMatchesResourceType(theResourceName, path)) {
				return Collections.emptyList();
			}
			return Collections.singletonList(path);
		}

		List<String> retVal = new ArrayList<>();
		StringTokenizer tok = new StringTokenizer(path, "|");
		while (tok.hasMoreElements()) {
			String nextPath = tok.nextToken().trim();
			if (theResourceName != null && !pathMatchesResourceType(theResourceName, nextPath)) {
				continue;
			}
			retVal.add(nextPath.trim());
		}
		return retVal;
	}

	public void addUpliftRefchain(@Nonnull String theCode, @Nonnull String theElementName) {
		myUpliftRefchains.put(theCode, theElementName);
	}

	/**
	 * Does this search parameter have an uplift refchain definition for the given code?
	 * See the HAPI FHIR documentation for a description of how uplift refchains work.
	 *
	 * @since 6.6.0
	 */
	public boolean hasUpliftRefchain(String theCode) {
		return myUpliftRefchains.containsKey(theCode);
	}

	/**
	 * Returns a set of all codes associated with uplift refchains for this search parameter.
	 * See the HAPI FHIR documentation for a description of how uplift refchains work.
	 *
	 * @since 6.6.0
	 */
	public Set<String> getUpliftRefchainCodes() {
		return Collections.unmodifiableSet(myUpliftRefchains.keySet());
	}

	/**
	 * Does this search parameter have any uplift refchain definitions?
	 * See the HAPI FHIR documentation for a description of how uplift refchains work.
	 *
	 * @since 6.6.0
	 */
	public boolean hasUpliftRefchains() {
		return !myUpliftRefchains.isEmpty();
	}

	/**
	 * This method tests whether a given FHIRPath expression <i>could</i>
	 * possibly apply to the given resource type.
	 *
	 * @param theResourceName
	 * @param thePath
	 * @return
	 */
	static boolean pathMatchesResourceType(String theResourceName, String thePath) {
		for (int i = 0; i < thePath.length() - 1; i++) {
			char nextChar = thePath.charAt(i);
			if (Character.isLowerCase(nextChar)) {
				return true;
			}
			if (Character.isLetter(nextChar)) {
				if (fhirPathExpressionStartsWith(theResourceName, thePath, i)) {
					return true;
				}
				if (fhirPathExpressionStartsWith("Resource", thePath, i)) {
					return true;
				}
				if (fhirPathExpressionStartsWith("DomainResource", thePath, i)) {
					return true;
				}
				return false;
			}
		}

		return false;
	}

	private static boolean fhirPathExpressionStartsWith(String theResourceName, String thePath, int theStartingIndex) {
		if (thePath.startsWith(theResourceName, theStartingIndex) && thePath.length() > theResourceName.length()) {
			for (int i = theResourceName.length() + theStartingIndex; i < thePath.length(); i++) {
				char nextChar = thePath.charAt(i);
				if (nextChar == '.') {
					return true;
				} else if (nextChar != ' ') {
					return false;
				}
			}
		}
		return false;
	}

	public enum RuntimeSearchParamStatusEnum {
		ACTIVE,
		DRAFT,
		RETIRED,
		UNKNOWN
	}

	public static class Component {
		private final String myExpression;
		private final String myReference;

		/**
		 * Constructor
		 */
		public Component(String theExpression, String theReference) {
			myExpression = theExpression;
			myReference = theReference;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
					.append("expression", myExpression)
					.append("reference", myReference)
					.toString();
		}

		public String getExpression() {
			return myExpression;
		}

		public String getReference() {
			return myReference;
		}
	}
}
