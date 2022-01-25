package ca.uhn.fhir.context;

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

import ca.uhn.fhir.parser.IParser;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This object supplies default configuration to all {@link IParser parser} instances
 * created by a given {@link FhirContext}. It is accessed using {@link FhirContext#getParserOptions()}
 * and {@link FhirContext#setParserOptions(ParserOptions)}.
 * <p>
 * It is fine to share a ParserOptions instances across multiple context instances.
 * </p>
 */
public class ParserOptions {

	private boolean myStripVersionsFromReferences = true;
	private Set<String> myDontStripVersionsFromReferencesAtPaths = Collections.emptySet();
	private boolean myOverrideResourceIdWithBundleEntryFullUrl = true;
	private boolean myAutoContainReferenceTargetsWithNoId = true;

	/**
	 * If set to {@literal true} (which is the default), contained resources may be specified by
	 * populating the target (contained) resource directly in {@link org.hl7.fhir.instance.model.api.IBaseReference#setReference(String)}
	 * and the parser will automatically locate it and insert it into <code>Resource.contained</code> when
	 * serializing. This is convenient, but also imposes a performance cost when serializing large numbers
	 * of resources, so this can be disabled if it is not needed.
	 * <p>
	 * If disabled, only resources that are directly placed in <code>Resource.contained</code> will be
	 * serialized.
	 * </p>
	 *
	 * @since 5.7.0
	 */
	public boolean isAutoContainReferenceTargetsWithNoId() {
		return myAutoContainReferenceTargetsWithNoId;
	}

	/**
	 * If set to {@literal true} (which is the default), contained resources may be specified by
	 * populating the target (contained) resource directly in {@link org.hl7.fhir.instance.model.api.IBaseReference#setReference(String)}
	 * and the parser will automatically locate it and insert it into <code>Resource.contained</code> when
	 * serializing. This is convenient, but also imposes a performance cost when serializing large numbers
	 * of resources, so this can be disabled if it is not needed.
	 * <p>
	 * If disabled, only resources that are directly placed in <code>Resource.contained</code> will be
	 * serialized.
	 * </p>
	 *
	 * @since 5.7.0
	 */
	public void setAutoContainReferenceTargetsWithNoId(boolean theAllowAutoContainedReferences) {
		myAutoContainReferenceTargetsWithNoId = theAllowAutoContainedReferences;
	}

	/**
	 * If set to <code>true<code> (which is the default), resource references containing a version
	 * will have the version removed when the resource is encoded. This is generally good behaviour because
	 * in most situations, references from one resource to another should be to the resource by ID, not
	 * by ID and version. In some cases though, it may be desirable to preserve the version in resource
	 * links. In that case, this value should be set to <code>false</code>.
	 *
	 * @return Returns the parser instance's configuration setting for stripping versions from resource references when
	 * encoding. Default is <code>true</code>.
	 */
	public boolean isStripVersionsFromReferences() {
		return myStripVersionsFromReferences;
	}

	/**
	 * If set to <code>true<code> (which is the default), resource references containing a version
	 * will have the version removed when the resource is encoded. This is generally good behaviour because
	 * in most situations, references from one resource to another should be to the resource by ID, not
	 * by ID and version. In some cases though, it may be desirable to preserve the version in resource
	 * links. In that case, this value should be set to <code>false</code>.
	 * <p>
	 * This method provides the ability to globally disable reference encoding. If finer-grained
	 * control is needed, use {@link #setDontStripVersionsFromReferencesAtPaths(String...)}
	 * </p>
	 *
	 * @param theStripVersionsFromReferences Set this to <code>false<code> to prevent the parser from removing
	 *                                       resource versions from references.
	 * @return Returns a reference to <code>this</code> parser so that method calls can be chained together
	 * @see #setDontStripVersionsFromReferencesAtPaths(String...)
	 */
	public ParserOptions setStripVersionsFromReferences(boolean theStripVersionsFromReferences) {
		myStripVersionsFromReferences = theStripVersionsFromReferences;
		return this;
	}

	/**
	 * Returns the value supplied to {@link IParser#setDontStripVersionsFromReferencesAtPaths(String...)}
	 *
	 * @see #setDontStripVersionsFromReferencesAtPaths(String...)
	 * @see #setStripVersionsFromReferences(boolean)
	 */
	public Set<String> getDontStripVersionsFromReferencesAtPaths() {
		return myDontStripVersionsFromReferencesAtPaths;
	}

	/**
	 * If supplied value(s), any resource references at the specified paths will have their
	 * resource versions encoded instead of being automatically stripped during the encoding
	 * process. This setting has no effect on the parsing process.
	 * <p>
	 * This method provides a finer-grained level of control than {@link #setStripVersionsFromReferences(boolean)}
	 * and any paths specified by this method will be encoded even if {@link #setStripVersionsFromReferences(boolean)}
	 * has been set to <code>true</code> (which is the default)
	 * </p>
	 *
	 * @param thePaths A collection of paths for which the resource versions will not be removed automatically
	 *                 when serializing, e.g. "Patient.managingOrganization" or "AuditEvent.object.reference". Note that
	 *                 only resource name and field names with dots separating is allowed here (no repetition
	 *                 indicators, FluentPath expressions, etc.)
	 * @return Returns a reference to <code>this</code> parser so that method calls can be chained together
	 * @see #setStripVersionsFromReferences(boolean)
	 */
	public ParserOptions setDontStripVersionsFromReferencesAtPaths(String... thePaths) {
		if (thePaths == null) {
			setDontStripVersionsFromReferencesAtPaths((List<String>) null);
		} else {
			setDontStripVersionsFromReferencesAtPaths(Arrays.asList(thePaths));
		}
		return this;
	}

	/**
	 * If supplied value(s), any resource references at the specified paths will have their
	 * resource versions encoded instead of being automatically stripped during the encoding
	 * process. This setting has no effect on the parsing process.
	 * <p>
	 * This method provides a finer-grained level of control than {@link #setStripVersionsFromReferences(boolean)}
	 * and any paths specified by this method will be encoded even if {@link #setStripVersionsFromReferences(boolean)}
	 * has been set to <code>true</code> (which is the default)
	 * </p>
	 *
	 * @param thePaths A collection of paths for which the resource versions will not be removed automatically
	 *                 when serializing, e.g. "Patient.managingOrganization" or "AuditEvent.object.reference". Note that
	 *                 only resource name and field names with dots separating is allowed here (no repetition
	 *                 indicators, FluentPath expressions, etc.)
	 * @return Returns a reference to <code>this</code> parser so that method calls can be chained together
	 * @see #setStripVersionsFromReferences(boolean)
	 */
	@SuppressWarnings("unchecked")
	public ParserOptions setDontStripVersionsFromReferencesAtPaths(Collection<String> thePaths) {
		if (thePaths == null) {
			myDontStripVersionsFromReferencesAtPaths = Collections.emptySet();
		} else if (thePaths instanceof HashSet) {
			myDontStripVersionsFromReferencesAtPaths = (Set<String>) ((HashSet<String>) thePaths).clone();
		} else {
			myDontStripVersionsFromReferencesAtPaths = new HashSet<>(thePaths);
		}
		return this;
	}

	/**
	 * If set to <code>true</code> (which is the default), the Bundle.entry.fullUrl will override the Bundle.entry.resource's
	 * resource id if the fullUrl is defined. This behavior happens when parsing the source data into a Bundle object. Set this
	 * to <code>false</code> if this is not the desired behavior (e.g. the client code wishes to perform additional
	 * validation checks between the fullUrl and the resource id).
	 *
	 * @return Returns the parser instance's configuration setting for overriding resource ids with Bundle.entry.fullUrl when
	 * parsing the source data into a Bundle object. Default is <code>true</code>.
	 */
	public boolean isOverrideResourceIdWithBundleEntryFullUrl() {
		return myOverrideResourceIdWithBundleEntryFullUrl;
	}

	/**
	 * If set to <code>true</code> (which is the default), the Bundle.entry.fullUrl will override the Bundle.entry.resource's
	 * resource id if the fullUrl is defined. This behavior happens when parsing the source data into a Bundle object. Set this
	 * to <code>false</code> if this is not the desired behavior (e.g. the client code wishes to perform additional
	 * validation checks between the fullUrl and the resource id).
	 *
	 * @param theOverrideResourceIdWithBundleEntryFullUrl Set this to <code>false</code> to prevent the parser from overriding resource ids with the
	 *                                                    Bundle.entry.fullUrl
	 * @return Returns a reference to <code>this</code> parser so that method calls can be chained together
	 */
	public ParserOptions setOverrideResourceIdWithBundleEntryFullUrl(boolean theOverrideResourceIdWithBundleEntryFullUrl) {
		myOverrideResourceIdWithBundleEntryFullUrl = theOverrideResourceIdWithBundleEntryFullUrl;
		return this;
	}

}
