package ca.uhn.fhir.rest.gclient;

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

public interface ISort<T> {

	/**
	 * Sort ascending
	 */
	IQuery<T> ascending(IParam theParam);

	/**
	 * Sort ascending
	 *
	 * @param theParam The param name, e.g. "address"
	 */
	IQuery<T> ascending(String theParam);

	/**
	 * Sort by the default order. Note that as of STU3, there is no longer
	 * a concept of default order, only ascending and descending. This method
	 * technically implies "ascending" but it makes more sense to use
	 * {@link #ascending(IParam)}
	 */
	IQuery<T> defaultOrder(IParam theParam);

	/**
	 * Sort by the default order. Note that as of STU3, there is no longer
	 * a concept of default order, only ascending and descending. This method
	 * technically implies "ascending" but it makes more sense to use
	 * {@link #ascending(IParam)}
	 */
	IQuery<T> defaultOrder(String theParam);

	/**
	 * Sort descending
	 *
	 * @param theParam A query param - Could be a constant such as <code>Patient.ADDRESS</code> or a custom
	 *                 param such as <code>new StringClientParam("foo")</code>
	 */
	IQuery<T> descending(IParam theParam);

	/**
	 * Sort ascending
	 *
	 * @param theParam The param name, e.g. "address"
	 */
	IQuery<T> descending(String theParam);

}
