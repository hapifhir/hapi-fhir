package ca.uhn.fhir.rest.gclient;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.SearchStyleEnum;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.param.DateRangeParam;
import org.hl7.fhir.instance.model.api.IBaseBundle;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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

public interface IQuery<Y> extends IBaseQuery<IQuery<Y>>, IClientExecutable<IQuery<Y>, Y> {

	/**
	 * {@inheritDoc}
	 */
	// This is here as an overridden method to allow mocking clients with Mockito to work
	@Override
	IQuery<Y> and(ICriterion<?> theCriterion);

	/**
	 * Specifies the <code>_count</code> parameter, which indicates to the server how many resources should be returned
	 * on a single page.
	 *
	 * @since 1.4
	 */
	IQuery<Y> count(int theCount);

	/**
	 * Specifies the <code>_offset</code> parameter, which indicates to the server the offset of the query. Use
	 * with {@link #count(int)}.
	 *
	 * This parameter is not part of the FHIR standard, all servers might not implement it.
	 *
	 * @since 5.2
	 */
	IQuery<Y> offset(int theOffset);

	/**
	 * Add an "_include" specification or an "_include:recurse" specification. If you are using
	 * a constant from one of the built-in structures you can select whether you want recursive
	 * behaviour by using the following syntax:
	 * <ul>
	 * <li><b>Recurse:</b> <code>.include(Patient.INCLUDE_ORGANIZATION.asRecursive())</code>
	 * <li><b>No Recurse:</b> <code>.include(Patient.INCLUDE_ORGANIZATION.asNonRecursive())</code>
	 * </ul>
	 */
	IQuery<Y> include(Include theInclude);

	/**
	 * Add a "_lastUpdated" specification
	 *
	 * @since HAPI FHIR 1.1 - Note that option was added to FHIR itself in DSTU2
	 */
	IQuery<Y> lastUpdated(DateRangeParam theLastUpdated);

	/**
	 * Specifies the <code>_count</code> parameter, which indicates to the server how many resources should be returned
	 * on a single page.
	 *
	 * @deprecated This parameter is badly named, since FHIR calls this parameter "_count" and not "_limit". Use {@link #count(int)} instead (it also sets the _count parameter)
	 * @see #count(int)
	 */
	@Deprecated
	IQuery<Y> limitTo(int theLimitTo);

	/**
	 * Request that the client return the specified bundle type, e.g. <code>org.hl7.fhir.dstu2.model.Bundle.class</code>
	 * or <code>ca.uhn.fhir.model.dstu2.resource.Bundle.class</code>
	 */
	<B extends IBaseBundle> IQuery<B> returnBundle(Class<B> theClass);

	/**
	 * Request that the server modify the response using the <code>_total</code> param
	 *
	 * THIS IS AN EXPERIMENTAL FEATURE - Use with caution, as it may be
	 * removed or modified in a future version.
	 */
	IQuery<Y> totalMode(SearchTotalModeEnum theTotalMode);

	/**
	 * Add a "_revinclude" specification
	 *
	 * @since HAPI FHIR 1.0 - Note that option was added to FHIR itself in DSTU2
	 */
	IQuery<Y> revInclude(Include theIncludeTarget);

	/**
	 * Adds a sort criteria
	 *
	 * @see #sort(SortSpec) for an alternate way of speciyfing sorts
	 */
	ISort<Y> sort();

	/**
	 * Adds a sort using a {@link SortSpec} object
	 *
	 * @see #sort() for an alternate way of speciyfing sorts
	 */
	IQuery<Y> sort(SortSpec theSortSpec);

	/**
	 * Forces the query to perform the search using the given method (allowable methods are described in the
	 * <a href="http://www.hl7.org/fhir/search.html">FHIR Search Specification</a>)
	 * <p>
	 * This can be used to force the use of an HTTP POST instead of an HTTP GET
	 * </p>
	 *
	 * @see SearchStyleEnum
	 * @since 0.6
	 */
	IQuery<Y> usingStyle(SearchStyleEnum theStyle);

	/**
	 * {@inheritDoc}
	 */
	// This is here as an overridden method to allow mocking clients with Mockito to work
	@Override
	IQuery<Y> where(ICriterion<?> theCriterion);

	/**
	 * Matches any of the profiles given as argument. This would result in an OR search for resources matching one or more profiles.
	 * To do an AND search, make multiple calls to {@link #withProfile(String)}.
	 *
	 * @param theProfileUris The URIs of a given profile to search for resources which match.
	 */
	IQuery<Y> withAnyProfile(Collection<String> theProfileUris);

	IQuery<Y> withIdAndCompartment(String theResourceId, String theCompartmentName);

	/**
	 * Match only resources where the resource has the given profile declaration. This parameter corresponds to
	 * the <code>_profile</code> URL parameter.
	 *
	 * @param theProfileUri The URI of a given profile to search for resources which match
	 */
	IQuery<Y> withProfile(String theProfileUri);

	/**
	 * Match only resources where the resource has the given security tag. This parameter corresponds to
	 * the <code>_security</code> URL parameter.
	 *
	 * @param theSystem The tag code system, or <code>null</code> to match any code system (this may not be supported on all servers)
	 * @param theCode   The tag code. Must not be <code>null</code> or empty.
	 */
	IQuery<Y> withSecurity(String theSystem, String theCode);

	/**
	 * Match only resources where the resource has the given tag. This parameter corresponds to
	 * the <code>_tag</code> URL parameter.
	 *
	 * @param theSystem The tag code system, or <code>null</code> to match any code system (this may not be supported on all servers)
	 * @param theCode   The tag code. Must not be <code>null</code> or empty.
	 */
	IQuery<Y> withTag(String theSystem, String theCode);

//	Y execute();

}
