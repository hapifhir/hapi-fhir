/*-
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
package ca.uhn.fhir.repository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import com.google.common.annotations.Beta;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * This API is under-going active development, so it should be considered beta-level.
 * </p>
 *
 * <p>
 * This interface is a Java rendition of the FHIR REST API. All FHIR operations are defined at the
 * HTTP level, which is convenient from the specification point-of-view since FHIR is built on top
 * of web standards. This does mean that a few HTTP specific considerations, such as transmitting
 * side-band information through the HTTP headers, bleeds into this API.
 * </p>
 *
 * <p>
 * One particularly odd case are FHIR Bundle links. The specification describes these as opaque to
 * the end-user, so a given FHIR repository implementation must be able to resolve those directly.
 * See {@link Repository#link(Class, String)}
 * </p>
 *
 * <p>
 * This interface also chooses to ignore return headers for most cases, preferring to return the
 * Java objects directly. In cases where this is not possible, or the additional headers are crucial
 * information, HAPI's {@link MethodOutcome} is used.
 * </p>
 *
 * <p>
 * Implementations of this interface should prefer to throw the exceptions derived from
 * {@link ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException}
 *
 * All operations may throw {@link AuthenticationException}, {@link ForbiddenOperationException}, or
 * {@link InternalErrorException} in addition to operation-specific exceptions.
 * </p>
 *
 * <p>
 * If a given operation is not supported, implementations should throw an
 * {@link NotImplementedOperationException}. The capabilities operation, if supported, should return
 * the set of supported interactions. If capabilities is not supported, the components in this
 * repository will try to invoke operations with "sensible" defaults. For example, by using the
 * standard FHIR search parameters. Discussion is on-going to determine what a "sensible" minimal
 * level of support for interactions should be.
 * </p>
 *
 * @see <a href="https://www.hl7.org/fhir/http.html">FHIR REST API</a>
 */
@Beta
public interface Repository {

	// CRUD starts here

	/**
	 * Reads a resource from the repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/http.html#read">FHIR read</a>
	 * @see <a href="https://www.hl7.org/fhir/http.html#vread">FHIR vRead</a>
	 *
	 * @param <T> a Resource type
	 * @param <I> an Id type
	 * @param resourceType the class of the Resource type to read
	 * @param id the id of the Resource to read
	 * @return the Resource
	 */
	default <T extends IBaseResource, I extends IIdType> T read(Class<T> resourceType, I id) {
		return this.read(resourceType, id, Collections.emptyMap());
	}

	/**
	 * Reads a Resource from the repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/http.html#read">FHIR read</a>
	 * @see <a href="https://www.hl7.org/fhir/http.html#vread">FHIR vRead</a>
	 *
	 * @param <T> a Resource type
	 * @param <I> an Id type
	 * @param resourceType the class of the Resource type to read
	 * @param id the id of the Resource to read
	 * @param headers headers for this request, typically key-value pairs of HTTP headers
	 * @return the Resource
	 */
	<T extends IBaseResource, I extends IIdType> T read(Class<T> resourceType, I id, Map<String, String> headers);

	/**
	 * Creates a Resource in the repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/http.html#create">FHIR create</a>
	 *
	 * @param <T> a Resource type
	 * @param resource the Resource to create
	 * @return a MethodOutcome with the id of the created Resource
	 */
	default <T extends IBaseResource> MethodOutcome create(T resource) {
		return this.create(resource, Collections.emptyMap());
	}

	/**
	 * Creates a Resource in the repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/http.html#create">FHIR create</a>
	 *
	 * @param <T> a Resource type
	 * @param resource the Resource to create
	 * @param headers headers for this request, typically key-value pairs of HTTP headers
	 * @return a MethodOutcome with the id of the created Resource
	 */
	<T extends IBaseResource> MethodOutcome create(T resource, Map<String, String> headers);

	/**
	 * Patches a Resource in the repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/http.html#patch">FHIR patch</a>
	 *
	 * @param <I> an Id type
	 * @param <P> a Parameters type
	 * @param id the id of the Resource to patch
	 * @param patchParameters parameters describing the patches to apply
	 * @return a MethodOutcome with the id of the patched resource
	 */
	default <I extends IIdType, P extends IBaseParameters> MethodOutcome patch(I id, P patchParameters) {
		return this.patch(id, patchParameters, Collections.emptyMap());
	}

	/**
	 * Patches a Resource in the repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/http.html#patch">FHIR patch</a>
	 *
	 * @param <I> an Id type
	 * @param <P> a Parameters type
	 * @param id the id of the Resource to patch
	 * @param patchParameters parameters describing the patches to apply
	 * @param headers headers for this request, typically key-value pairs of HTTP headers
	 * @return a MethodOutcome with the id of the patched resource
	 */
	default <I extends IIdType, P extends IBaseParameters> MethodOutcome patch(
			I id, P patchParameters, Map<String, String> headers) {
		return throwNotImplementedOperationException("patch is not supported by this repository");
	}

	/**
	 * Updates a Resource in the repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/http.html#update">FHIR update</a>
	 *
	 * @param <T> a Resource type
	 * @param resource the Resource to update
	 * @return a MethodOutcome with the id of the updated Resource
	 */
	default <T extends IBaseResource> MethodOutcome update(T resource) {
		return this.update(resource, Collections.emptyMap());
	}

	/**
	 * Updates a Resource in the repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/http.html#update">FHIR update</a>
	 *
	 * @param <T> a Resource type
	 * @param resource the Resource to update
	 * @param headers headers for this request, typically key-value pairs of HTTP headers
	 * @return a MethodOutcome with the id of the updated Resource
	 */
	<T extends IBaseResource> MethodOutcome update(T resource, Map<String, String> headers);

	/**
	 * Deletes a Resource in the repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/http.html#delete">FHIR delete</a>
	 *
	 * @param <T> a Resource type
	 * @param <I> an Id type
	 * @param resourceType the class of the Resource type to delete
	 * @param id the id of the Resource to delete
	 * @return a MethodOutcome with the id of the deleted resource
	 */
	default <T extends IBaseResource, I extends IIdType> MethodOutcome delete(Class<T> resourceType, I id) {
		return this.delete(resourceType, id, Collections.emptyMap());
	}

	/**
	 * Deletes a Resource in the repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/http.html#delete">FHIR delete</a>
	 *
	 * @param <T> a Resource type
	 * @param <I> an Id type
	 * @param resourceType the class of the Resource type to delete
	 * @param id the id of the Resource to delete
	 * @param headers headers for this request, typically key-value pairs of HTTP headers
	 * @return a MethodOutcome with the id of the deleted resource
	 */
	<T extends IBaseResource, I extends IIdType> MethodOutcome delete(
			Class<T> resourceType, I id, Map<String, String> headers);

	// Querying starts here

	/**
	 * Searches this repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/http.html#search">FHIR search</a>
	 *
	 * @param <B> a Bundle type
	 * @param <T> a Resource type
	 * @param bundleType the class of the Bundle type to return
	 * @param resourceType the class of the Resource type to search
	 * @param searchParameters the searchParameters for this search
	 * @return a Bundle with the results of the search
	 */
	default <B extends IBaseBundle, T extends IBaseResource> B search(
			Class<B> bundleType, Class<T> resourceType, Multimap<String, List<IQueryParameterType>> searchParameters) {
		return this.search(bundleType, resourceType, searchParameters, Collections.emptyMap());
	}

	/**
	 * Searches this repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/http.html#search">FHIR search</a>
	 *
	 * @param <B> a Bundle type
	 * @param <T> a Resource type
	 * @param bundleType the class of the Bundle type to return
	 * @param resourceType the class of the Resource type to search
	 * @param searchParameters the searchParameters for this search
	 * @return a Bundle with the results of the search
	 */
	default <B extends IBaseBundle, T extends IBaseResource> B search(
			Class<B> bundleType, Class<T> resourceType, Map<String, List<IQueryParameterType>> searchParameters) {
		return this.search(bundleType, resourceType, searchParameters, Collections.emptyMap());
	}

	/**
	 * Searches this repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/http.html#search">FHIR search</a>
	 *
	 * @param <B> a Bundle type
	 * @param <T> a Resource type
	 * @param bundleType the class of the Bundle type to return
	 * @param resourceType the class of the Resource type to search
	 * @param searchParameters the searchParameters for this search
	 * @param headers headers for this request, typically key-value pairs of HTTP headers
	 * @return a Bundle with the results of the search
	 */
	<B extends IBaseBundle, T extends IBaseResource> B search(
			Class<B> bundleType,
			Class<T> resourceType,
			Multimap<String, List<IQueryParameterType>> searchParameters,
			Map<String, String> headers);

	/**
	 * Searches this repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/http.html#search">FHIR search</a>
	 *
	 * @param <B> a Bundle type
	 * @param <T> a Resource type
	 * @param bundleType the class of the Bundle type to return
	 * @param resourceType the class of the Resource type to search
	 * @param searchParameters the searchParameters for this search
	 * @param headers headers for this request, typically key-value pairs of HTTP headers
	 * @return a Bundle with the results of the search
	 */
	default <B extends IBaseBundle, T extends IBaseResource> B search(
			Class<B> bundleType,
			Class<T> resourceType,
			Map<String, List<IQueryParameterType>> searchParameters,
			Map<String, String> headers) {
		ArrayListMultimap<String, List<IQueryParameterType>> multimap = ArrayListMultimap.create();
		searchParameters.forEach(multimap::put);
		return this.search(bundleType, resourceType, multimap, headers);
	}

	// Paging starts here

	/**
	 * Reads a Bundle from a link on this repository
	 *
	 * This is typically used for paging during searches
	 *
	 * @see <a href="https://www.hl7.org/fhir/bundle-definitions.html#Bundle.link">FHIR Bundle
	 *      link</a>
	 *
	 * @param <B> a Bundle type
	 * @param url the url of the Bundle to load
	 * @return a Bundle
	 */
	default <B extends IBaseBundle> B link(Class<B> bundleType, String url) {
		return this.link(bundleType, url, Collections.emptyMap());
	}

	/**
	 * Reads a Bundle from a link on this repository
	 *
	 * This is typically used for paging during searches
	 *
	 * @see <a href="https://www.hl7.org/fhir/bundle-definitions.html#Bundle.link">FHIR Bundle
	 *      link</a>
	 *
	 * @param <B> a Bundle type
	 * @param url the url of the Bundle to load
	 * @param headers headers for this request, typically key-value pairs of HTTP headers
	 * @return a Bundle
	 */
	default <B extends IBaseBundle> B link(Class<B> bundleType, String url, Map<String, String> headers) {
		return throwNotImplementedOperationException("link is not supported by this repository");
	}

	// Metadata starts here

	/**
	 * Returns the CapabilityStatement/Conformance metadata for this repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/http.html#capabilities">FHIR capabilities</a>
	 *
	 * @param <C> a CapabilityStatement/Conformance type
	 * @param resourceType the class of the CapabilityStatement/Conformance to return
	 * @return a CapabilityStatement/Conformance with the repository's metadata
	 */
	default <C extends IBaseConformance> C capabilities(Class<C> resourceType) {
		return this.capabilities(resourceType, Collections.emptyMap());
	}

	/**
	 * Returns the CapabilityStatement/Conformance metadata for this repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/http.html#capabilities">FHIR capabilities</a>
	 *
	 * @param <C> a CapabilityStatement/Conformance type
	 * @param resourceType the class of the CapabilityStatement/Conformance to return
	 * @param headers headers for this request, typically key-value pairs of HTTP headers
	 * @return a CapabilityStatement/Conformance with the repository's metadata
	 */
	default <C extends IBaseConformance> C capabilities(Class<C> resourceType, Map<String, String> headers) {
		return throwNotImplementedOperationException("capabilities is not supported by this repository");
	}

	// Transactions starts here

	/**
	 * Performs a transaction or batch on this repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/http.html#transaction">FHIR transaction</a>
	 *
	 * @param <B> a Bundle type
	 * @param transaction a Bundle with the transaction/batch
	 * @return a Bundle with the results of the transaction/batch
	 */
	default <B extends IBaseBundle> B transaction(B transaction) {
		return this.transaction(transaction, Collections.emptyMap());
	}

	/**
	 * Performs a transaction or batch on this repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/http.html#transaction">FHIR transaction</a>
	 *
	 * @param <B> a Bundle type
	 * @param transaction a Bundle with the transaction/batch
	 * @param headers headers for this request, typically key-value pairs of HTTP headers
	 * @return a Bundle with the results of the transaction/batch
	 */
	default <B extends IBaseBundle> B transaction(B transaction, Map<String, String> headers) {
		return throwNotImplementedOperationException("transaction is not supported by this repository");
	}

	// Operations starts here

	/**
	 * Invokes a server-level operation on this repository that returns a Resource
	 *
	 * @see <a href="https://www.hl7.org/fhir/operations.html">FHIR operations</a>
	 *
	 * @param <R> a Resource type to return
	 * @param <P> a Parameters type for operation parameters
	 * @param name the name of the operation to invoke
	 * @param parameters the operation parameters
	 * @param returnType the class of the Resource the operation returns
	 * @return the results of the operation
	 */
	default <R extends IBaseResource, P extends IBaseParameters> R invoke(
			String name, P parameters, Class<R> returnType) {
		return this.invoke(name, parameters, returnType, Collections.emptyMap());
	}

	/**
	 * Invokes a server-level operation on this repository that returns a Resource
	 *
	 * @see <a href="https://www.hl7.org/fhir/operations.html">FHIR operations</a>
	 *
	 * @param <R> a Resource type to return
	 * @param <P> a Parameters type for operation parameters
	 * @param name the name of the operation to invoke
	 * @param parameters the operation parameters
	 * @param returnType the class of the Resource the operation returns
	 * @param headers headers for this request, typically key-value pairs of HTTP headers
	 * @return the results of the operation
	 */
	default <R extends IBaseResource, P extends IBaseParameters> R invoke(
			String name, P parameters, Class<R> returnType, Map<String, String> headers) {
		return throwNotImplementedOperationException("server-level invoke is not supported by this repository");
	}

	/**
	 * Invokes a server-level operation on this repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/operations.html">FHIR operations</a>
	 *
	 * @param <P> a Parameters type for operation parameters
	 * @param name the name of the operation to invoke
	 * @param parameters the operation parameters
	 * @return a MethodOutcome with a status code
	 */
	default <P extends IBaseParameters> MethodOutcome invoke(String name, P parameters) {
		return this.invoke(name, parameters, Collections.emptyMap());
	}

	/**
	 * Invokes a server-level operation on this repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/operations.html">FHIR operations</a>
	 *
	 * @param <P> a Parameters type for operation parameters
	 * @param name the name of the operation to invoke
	 * @param parameters the operation parameters
	 * @param headers headers for this request, typically key-value pairs of HTTP headers
	 * @return a MethodOutcome with a status code
	 */
	default <P extends IBaseParameters> MethodOutcome invoke(String name, P parameters, Map<String, String> headers) {
		return throwNotImplementedOperationException("server-level invoke is not supported by this repository");
	}

	/**
	 * Invokes a type-level operation on this repository that returns a Resource
	 *
	 * @see <a href="https://www.hl7.org/fhir/operations.html">FHIR operations</a>
	 *
	 * @param <R> a Resource type to return
	 * @param <P> a Parameters type for operation parameters
	 * @param <T> a Resource type to do the invocation for
	 * @param resourceType the class of the Resource to do the invocation for
	 * @param name the name of the operation to invoke
	 * @param parameters the operation parameters
	 * @param returnType the class of the Resource the operation returns
	 * @return the results of the operation
	 */
	default <R extends IBaseResource, P extends IBaseParameters, T extends IBaseResource> R invoke(
			Class<T> resourceType, String name, P parameters, Class<R> returnType) {
		return this.invoke(resourceType, name, parameters, returnType, Collections.emptyMap());
	}

	/**
	 * Invokes a type-level operation on this repository that returns a Resource
	 *
	 * @see <a href="https://www.hl7.org/fhir/operations.html">FHIR operations</a>
	 *
	 * @param <R> a Resource type to return
	 * @param <P> a Parameters type for operation parameters
	 * @param <T> a Resource type to do the invocation for
	 * @param resourceType the class of the Resource to do the invocation for
	 * @param name the name of the operation to invoke
	 * @param parameters the operation parameters
	 * @param headers headers for this request, typically key-value pairs of HTTP headers
	 * @param returnType the class of the Resource the operation returns
	 * @return the results of the operation
	 */
	<R extends IBaseResource, P extends IBaseParameters, T extends IBaseResource> R invoke(
			Class<T> resourceType, String name, P parameters, Class<R> returnType, Map<String, String> headers);

	/**
	 * Invokes a type-level operation on this repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/operations.html">FHIR operations</a>
	 *
	 * @param <P> a Parameters type for operation parameters
	 * @param <T> a Resource type to do the invocation for
	 * @param resourceType the class of the Resource to do the invocation for
	 * @param name the name of the operation to invoke
	 * @param parameters the operation parameters
	 * @return a MethodOutcome with a status code
	 */
	default <P extends IBaseParameters, T extends IBaseResource> MethodOutcome invoke(
			Class<T> resourceType, String name, P parameters) {
		return this.invoke(resourceType, name, parameters, Collections.emptyMap());
	}

	/**
	 * Invokes a type-level operation on this repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/operations.html">FHIR operations</a>
	 *
	 * @param <P> a Parameters type for operation parameters
	 * @param <T> a Resource type to do the invocation for
	 * @param resourceType the class of the Resource to do the invocation for
	 * @param name the name of the operation to invoke
	 * @param parameters the operation parameters
	 * @param headers headers for this request, typically key-value pairs of HTTP headers
	 * @return a MethodOutcome with a status code
	 */
	default <P extends IBaseParameters, T extends IBaseResource> MethodOutcome invoke(
			Class<T> resourceType, String name, P parameters, Map<String, String> headers) {
		return throwNotImplementedOperationException("type-level invoke is not supported by this repository");
	}

	/**
	 * Invokes an instance-level operation on this repository that returns a Resource
	 *
	 * @see <a href="https://www.hl7.org/fhir/operations.html">FHIR operations</a>
	 *
	 * @param <R> a Resource type to return
	 * @param <P> a Parameters type for operation parameters
	 * @param <I> an Id type
	 * @param id the id of the Resource to do the invocation on
	 * @param name the name of the operation to invoke
	 * @param parameters the operation parameters
	 * @param returnType the class of the Resource the operation returns
	 * @return the results of the operation
	 */
	default <R extends IBaseResource, P extends IBaseParameters, I extends IIdType> R invoke(
			I id, String name, P parameters, Class<R> returnType) {
		return this.invoke(id, name, parameters, returnType, Collections.emptyMap());
	}

	/**
	 * Invokes an instance-level operation on this repository that returns a Resource
	 *
	 * @see <a href="https://www.hl7.org/fhir/operations.html">FHIR operations</a>
	 *
	 * @param <R> a Resource type to return
	 * @param <P> a Parameters type for operation parameters
	 * @param <I> an Id type
	 * @param id the id of the Resource to do the invocation on
	 * @param name the name of the operation to invoke
	 * @param parameters the operation parameters
	 * @param returnType the class of the Resource the operation returns
	 * @param headers headers for this request, typically key-value pairs of HTTP headers
	 * @return the results of the operation
	 */
	<R extends IBaseResource, P extends IBaseParameters, I extends IIdType> R invoke(
			I id, String name, P parameters, Class<R> returnType, Map<String, String> headers);

	/**
	 * Invokes an instance-level operation on this repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/operations.html">FHIR operations</a>
	 *
	 * @param <P> a Parameters type for operation parameters
	 * @param <I> an Id type
	 * @param id the id of the Resource to do the invocation on
	 * @param name the name of the operation to invoke
	 * @param parameters the operation parameters
	 * @return a MethodOutcome with a status code
	 */
	default <P extends IBaseParameters, I extends IIdType> MethodOutcome invoke(I id, String name, P parameters) {
		return this.invoke(id, name, parameters, Collections.emptyMap());
	}

	/**
	 * Invokes an instance-level operation on this repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/operations.html">FHIR operations</a>
	 *
	 * @param <P> a Parameters type for operation parameters
	 * @param <I> an Id type
	 * @param id the id of the Resource to do the invocation on
	 * @param name the name of the operation to invoke
	 * @param parameters the operation parameters
	 * @param headers headers for this request, typically key-value pairs of HTTP headers
	 * @return a MethodOutcome with a status code
	 */
	default <P extends IBaseParameters, I extends IIdType> MethodOutcome invoke(
			I id, String name, P parameters, Map<String, String> headers) {
		return throwNotImplementedOperationException("instance-level invoke is not supported by this repository");
	}

	// History starts here

	/**
	 * Returns a Bundle with server-level history for this repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/http.html#history">FHIR history</a>
	 *
	 * @param <B> a Bundle type to return
	 * @param <P> a Parameters type for input parameters
	 * @param parameters the parameters for this history interaction
	 * @param returnType the class of the Bundle type to return
	 * @return a Bundle with the server history
	 */
	default <B extends IBaseBundle, P extends IBaseParameters> B history(P parameters, Class<B> returnType) {
		return this.history(parameters, returnType, Collections.emptyMap());
	}

	/**
	 * Returns a Bundle with server-level history for this repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/http.html#history">FHIR history</a>
	 *
	 * @param <B> a Bundle type to return
	 * @param <P> a Parameters type for input parameters
	 * @param parameters the parameters for this history interaction
	 * @param returnType the class of the Bundle type to return
	 * @param headers headers for this request, typically key-value pairs of HTTP headers
	 * @return a Bundle with the server history
	 */
	default <B extends IBaseBundle, P extends IBaseParameters> B history(
			P parameters, Class<B> returnType, Map<String, String> headers) {
		return throwNotImplementedOperationException("server-level history is not supported by this repository");
	}

	/**
	 * Returns a Bundle with type-level history for this repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/http.html#history">FHIR history</a>
	 *
	 * @param <B> a Bundle type to return
	 * @param <P> a Parameters type for input parameters
	 * @param <T> a Resource type to produce history for
	 * @param resourceType the class of the Resource type to produce history for
	 * @param parameters the parameters for this history interaction
	 * @param returnType the class of the Bundle type to return
	 * @return a Bundle with the type history
	 */
	default <B extends IBaseBundle, P extends IBaseParameters, T extends IBaseResource> B history(
			Class<T> resourceType, P parameters, Class<B> returnType) {
		return this.history(resourceType, parameters, returnType, Collections.emptyMap());
	}

	/**
	 * Returns a Bundle with type-level history for this repository
	 *
	 * @see <a href="https://www.hl7.org/fhir/http.html#history">FHIR history</a>
	 *
	 * @param <B> a Bundle type to return
	 * @param <P> a Parameters type for input parameters
	 * @param <T> a Resource type to produce history for
	 * @param resourceType the class of the Resource type to produce history for
	 * @param parameters the parameters for this history interaction
	 * @param returnType the class of the Bundle type to return
	 * @param headers headers for this request, typically key-value pairs of HTTP headers
	 * @return a Bundle with the type history
	 */
	default <B extends IBaseBundle, P extends IBaseParameters, T extends IBaseResource> B history(
			Class<T> resourceType, P parameters, Class<B> returnType, Map<String, String> headers) {
		return throwNotImplementedOperationException("type-level history is not supported by this repository");
	}

	/**
	 * Returns a Bundle with instance-level history
	 *
	 * @see <a href="https://www.hl7.org/fhir/http.html#history">FHIR history</a>
	 *
	 * @param <B> a Bundle type to return
	 * @param <P> a Parameters type for input parameters
	 * @param <I> an Id type for the Resource to produce history for
	 * @param id the id of the Resource type to produce history for
	 * @param parameters the parameters for this history interaction
	 * @param returnType the class of the Bundle type to return
	 * @return a Bundle with the instance history
	 */
	default <B extends IBaseBundle, P extends IBaseParameters, I extends IIdType> B history(
			I id, P parameters, Class<B> returnType) {
		return this.history(id, parameters, returnType, Collections.emptyMap());
	}

	/**
	 * Returns a Bundle with instance-level history
	 *
	 * @see <a href="https://www.hl7.org/fhir/http.html#history">FHIR history</a>
	 *
	 * @param <B> a Bundle type to return
	 * @param <P> a Parameters type for input parameters
	 * @param <I> an Id type for the Resource to produce history for
	 * @param id the id of the Resource type to produce history for
	 * @param parameters the parameters for this history interaction
	 * @param returnType the class of the Bundle type to return
	 * @param headers headers for this request, typically key-value pairs of HTTP headers
	 * @return a Bundle with the instance history
	 */
	default <B extends IBaseBundle, P extends IBaseParameters, I extends IIdType> B history(
			I id, P parameters, Class<B> returnType, Map<String, String> headers) {
		return throwNotImplementedOperationException("instance-level history is not supported by this repository");
	}

	/**
	 * Returns the {@link FhirContext} used by the repository
	 *
	 * Practically, implementing FHIR functionality with the HAPI toolset requires a FhirContext. In
	 * particular for things like version independent code. Ideally, a user could which FHIR version a
	 * repository was configured for using things like the CapabilityStatement. In practice, that's
	 * not widely implemented (yet) and it's expensive to create a new context with every call. We
	 * will probably revisit this in the future.
	 *
	 * @return a FhirContext
	 */
	FhirContext fhirContext();

	private static <T> T throwNotImplementedOperationException(String theMessage) {
		throw new NotImplementedOperationException(Msg.code(2542) + theMessage);
	}
}
