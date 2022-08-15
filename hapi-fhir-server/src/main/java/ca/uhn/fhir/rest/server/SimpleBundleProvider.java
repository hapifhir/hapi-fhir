package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR - Server Framework
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

import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SimpleBundleProvider implements IBundleProvider {

	private final List<? extends IBaseResource> myList;
	private final String myUuid;
	private Integer myPreferredPageSize;
	private Integer mySize;
	private IPrimitiveType<Date> myPublished = InstantDt.withCurrentTime();
	private Integer myCurrentPageOffset;
	private Integer myCurrentPageSize;

	/**
	 * Constructor
	 */
	public SimpleBundleProvider(IBaseResource theResource) {
		this(Collections.singletonList(theResource));
	}

	/**
	 * Create an empty bundle
	 */
	public SimpleBundleProvider() {
		this(Collections.emptyList());
	}

	/**
	 * Constructor
	 */
	public SimpleBundleProvider(List<? extends IBaseResource> theList) {
		this(theList, null);
	}

	public SimpleBundleProvider(List<? extends IBaseResource> theList, String theUuid) {
		myList = theList;
		myUuid = theUuid;
		setSize(theList.size());
	}

	/**
	 * Constructor that provides only a size but no actual data (useful for _count = 0)
	 */
	public SimpleBundleProvider(int theSize) {
		myList = Collections.emptyList();
		myUuid = null;
		setSize(theSize);
	}

	/**
	 * @since 5.5.0
	 */
	@Override
	public Integer getCurrentPageOffset() {
		return myCurrentPageOffset;
	}

	/**
	 * @since 5.5.0
	 */
	public void setCurrentPageOffset(Integer theCurrentPageOffset) {
		myCurrentPageOffset = theCurrentPageOffset;
	}

	/**
	 * @since 5.5.0
	 */
	@Override
	public Integer getCurrentPageSize() {
		return myCurrentPageSize;
	}

	/**
	 * @since 5.5.0
	 */
	public void setCurrentPageSize(Integer theCurrentPageSize) {
		myCurrentPageSize = theCurrentPageSize;
	}

	/**
	 * Returns the results stored in this provider
	 */
	protected List<? extends IBaseResource> getList() {
		return myList;
	}

	@Override
	public IPrimitiveType<Date> getPublished() {
		return myPublished;
	}

	/**
	 * By default this class uses the object creation date/time (for this object)
	 * to determine {@link #getPublished() the published date} but this
	 * method may be used to specify an alternate date/time
	 */
	public void setPublished(IPrimitiveType<Date> thePublished) {
		myPublished = thePublished;
	}

	@Nonnull
	@Override
	public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
		return (List<IBaseResource>) myList.subList(theFromIndex, Math.min(theToIndex, myList.size()));
	}

	@Override
	public String getUuid() {
		return myUuid;
	}

	/**
	 * Defaults to null
	 */
	@Override
	public Integer preferredPageSize() {
		return myPreferredPageSize;
	}

	/**
	 * Sets the preferred page size to be returned by {@link #preferredPageSize()}.
	 * Default is <code>null</code>.
	 */
	public void setPreferredPageSize(Integer thePreferredPageSize) {
		myPreferredPageSize = thePreferredPageSize;
	}

	/**
	 * Sets the total number of results, if this provider
	 * corresponds to a single page within a larger search result
	 */
	public SimpleBundleProvider setSize(Integer theSize) {
		mySize = theSize;
		return this;
	}

	@Override
	public Integer size() {
		return mySize;
	}

}
