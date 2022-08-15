package ca.uhn.fhir.util;

/*-
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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.primitive.IdDt;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.Objects;

/**
 * This class can be used to build a Bundle resource to be used as a FHIR transaction. Convenience methods provide
 * support for setting various bundle fields and working with bundle parts such as metadata and entry
 * (method and search).
 *
 * <p>
 *
 * This is not yet complete, and doesn't support all FHIR features. <b>USE WITH CAUTION</b> as the API
 * may change.
 *
 * @since 5.1.0
 */
public class BundleBuilder {

	private final FhirContext myContext;
	private final IBaseBundle myBundle;
	private final RuntimeResourceDefinition myBundleDef;
	private final BaseRuntimeChildDefinition myEntryChild;
	private final BaseRuntimeChildDefinition myMetaChild;
	private final BaseRuntimeChildDefinition mySearchChild;
	private final BaseRuntimeElementDefinition<?> myEntryDef;
	private final BaseRuntimeElementDefinition<?> myMetaDef;
	private final BaseRuntimeElementDefinition mySearchDef;
	private final BaseRuntimeChildDefinition myEntryResourceChild;
	private final BaseRuntimeChildDefinition myEntryFullUrlChild;
	private final BaseRuntimeChildDefinition myEntryRequestChild;
	private final BaseRuntimeElementDefinition<?> myEntryRequestDef;
	private final BaseRuntimeChildDefinition myEntryRequestUrlChild;
	private final BaseRuntimeChildDefinition myEntryRequestMethodChild;
	private final BaseRuntimeElementDefinition<?> myEntryRequestMethodDef;
	private final BaseRuntimeChildDefinition myEntryRequestIfNoneExistChild;

	/**
	 * Constructor
	 */
	public BundleBuilder(FhirContext theContext) {
		myContext = theContext;

		myBundleDef = myContext.getResourceDefinition("Bundle");
		myBundle = (IBaseBundle) myBundleDef.newInstance();

		myEntryChild = myBundleDef.getChildByName("entry");
		myEntryDef = myEntryChild.getChildByName("entry");

		mySearchChild = myEntryDef.getChildByName("search");
		mySearchDef = mySearchChild.getChildByName("search");

		myMetaChild = myBundleDef.getChildByName("meta");
		myMetaDef = myMetaChild.getChildByName("meta");

		myEntryResourceChild = myEntryDef.getChildByName("resource");
		myEntryFullUrlChild = myEntryDef.getChildByName("fullUrl");

		myEntryRequestChild = myEntryDef.getChildByName("request");
		myEntryRequestDef = myEntryRequestChild.getChildByName("request");

		myEntryRequestUrlChild = myEntryRequestDef.getChildByName("url");

		myEntryRequestMethodChild = myEntryRequestDef.getChildByName("method");
		myEntryRequestMethodDef = myEntryRequestMethodChild.getChildByName("method");

		myEntryRequestIfNoneExistChild = myEntryRequestDef.getChildByName("ifNoneExist");
	}

	/**
	 * Sets the specified primitive field on the bundle with the value provided.
	 *
	 * @param theFieldName
	 * 		Name of the primitive field.
	 * @param theFieldValue
	 * 		Value of the field to be set.
	 */
	public BundleBuilder setBundleField(String theFieldName, String theFieldValue) {
		BaseRuntimeChildDefinition typeChild = myBundleDef.getChildByName(theFieldName);
		Validate.notNull(typeChild, "Unable to find field %s", theFieldName);

		IPrimitiveType<?> type = (IPrimitiveType<?>) typeChild.getChildByName(theFieldName).newInstance(typeChild.getInstanceConstructorArguments());
		type.setValueAsString(theFieldValue);
		typeChild.getMutator().setValue(myBundle, type);
		return this;
	}

	/**
	 * Sets the specified primitive field on the search entry with the value provided.
	 *
	 * @param theSearch
	 * 		Search part of the entry
	 * @param theFieldName
	 * 		Name of the primitive field.
	 * @param theFieldValue
	 * 		Value of the field to be set.
	 */
	public BundleBuilder setSearchField(IBase theSearch, String theFieldName, String theFieldValue) {
		BaseRuntimeChildDefinition typeChild = mySearchDef.getChildByName(theFieldName);
		Validate.notNull(typeChild, "Unable to find field %s", theFieldName);

		IPrimitiveType<?> type = (IPrimitiveType<?>) typeChild.getChildByName(theFieldName).newInstance(typeChild.getInstanceConstructorArguments());
		type.setValueAsString(theFieldValue);
		typeChild.getMutator().setValue(theSearch, type);
		return this;
	}

	public BundleBuilder setSearchField(IBase theSearch, String theFieldName, IPrimitiveType<?> theFieldValue) {
		BaseRuntimeChildDefinition typeChild = mySearchDef.getChildByName(theFieldName);
		Validate.notNull(typeChild, "Unable to find field %s", theFieldName);

		typeChild.getMutator().setValue(theSearch, theFieldValue);
		return this;
	}

	/**
	 * Adds an entry containing an update (PUT) request.
	 * Also sets the Bundle.type value to "transaction" if it is not already set.
	 *
	 * @param theResource The resource to update
	 */
	public UpdateBuilder addTransactionUpdateEntry(IBaseResource theResource) {
		setBundleField("type", "transaction");

		IBase request = addEntryAndReturnRequest(theResource);

		// Bundle.entry.request.url
		IPrimitiveType<?> url = (IPrimitiveType<?>) myContext.getElementDefinition("uri").newInstance();
		String resourceType = myContext.getResourceType(theResource);
		url.setValueAsString(theResource.getIdElement().toUnqualifiedVersionless().withResourceType(resourceType).getValue());
		myEntryRequestUrlChild.getMutator().setValue(request, url);

		// Bundle.entry.request.url
		IPrimitiveType<?> method = (IPrimitiveType<?>) myEntryRequestMethodDef.newInstance(myEntryRequestMethodChild.getInstanceConstructorArguments());
		method.setValueAsString("PUT");
		myEntryRequestMethodChild.getMutator().setValue(request, method);

		return new UpdateBuilder(url);
	}

	/**
	 * Adds an entry containing an create (POST) request.
	 * Also sets the Bundle.type value to "transaction" if it is not already set.
	 *
	 * @param theResource The resource to create
	 */
	public CreateBuilder addTransactionCreateEntry(IBaseResource theResource) {
		setBundleField("type", "transaction");

		IBase request = addEntryAndReturnRequest(theResource);

		String resourceType = myContext.getResourceType(theResource);

		// Bundle.entry.request.url
		IPrimitiveType<?> url = (IPrimitiveType<?>) myContext.getElementDefinition("uri").newInstance();
		url.setValueAsString(resourceType);
		myEntryRequestUrlChild.getMutator().setValue(request, url);

		// Bundle.entry.request.url
		IPrimitiveType<?> method = (IPrimitiveType<?>) myEntryRequestMethodDef.newInstance(myEntryRequestMethodChild.getInstanceConstructorArguments());
		method.setValueAsString("POST");
		myEntryRequestMethodChild.getMutator().setValue(request, method);

		return new CreateBuilder(request);
	}

	/**
	 * Adds an entry containing a delete (DELETE) request.
	 * Also sets the Bundle.type value to "transaction" if it is not already set.
	 *
	 * Note that the resource is only used to extract its ID and type, and the body of the resource is not included in the entry,
	 *
	 * @param theResource The resource to delete.
	 */
	public void addTransactionDeleteEntry(IBaseResource theResource) {
		String resourceType = myContext.getResourceType(theResource);
		String idPart = theResource.getIdElement().toUnqualifiedVersionless().getIdPart();
		addTransactionDeleteEntry(resourceType, idPart);
	}

	/**
	 * Adds an entry containing a delete (DELETE) request.
	 * Also sets the Bundle.type value to "transaction" if it is not already set.
	 *
	 * @param theResourceType The type resource to delete.
	 * @param theIdPart  the ID of the resource to delete.
	 */
	public void addTransactionDeleteEntry(String theResourceType, String theIdPart) {
		setBundleField("type", "transaction");
		IBase request = addEntryAndReturnRequest();
		IdDt idDt = new IdDt(theIdPart);
		
		// Bundle.entry.request.url
		IPrimitiveType<?> url = (IPrimitiveType<?>) myContext.getElementDefinition("uri").newInstance();
		url.setValueAsString(idDt.toUnqualifiedVersionless().withResourceType(theResourceType).getValue());
		myEntryRequestUrlChild.getMutator().setValue(request, url);

		// Bundle.entry.request.method
		IPrimitiveType<?> method = (IPrimitiveType<?>) myEntryRequestMethodDef.newInstance(myEntryRequestMethodChild.getInstanceConstructorArguments());
		method.setValueAsString("DELETE");
		myEntryRequestMethodChild.getMutator().setValue(request, method);
	}



	/**
	 * Adds an entry for a Collection bundle type
	 */
	public void addCollectionEntry(IBaseResource theResource) {
		setType("collection");
		addEntryAndReturnRequest(theResource);
	}

	/**
	 * Creates new entry and adds it to the bundle
	 *
	 * @return
	 * 		Returns the new entry.
	 */
	public IBase addEntry() {
		IBase entry = myEntryDef.newInstance();
		myEntryChild.getMutator().addValue(myBundle, entry);
		return entry;
	}

	/**
	 * Creates new search instance for the specified entry
	 *
	 * @param entry Entry to create search instance for
	 * @return
	 * 		Returns the search instance
	 */
	public IBaseBackboneElement addSearch(IBase entry) {
		IBase searchInstance = mySearchDef.newInstance();
		mySearchChild.getMutator().setValue(entry, searchInstance);
		return (IBaseBackboneElement) searchInstance;
	}

	/**
	 *
	 * @param theResource
	 * @return
	 */
	public IBase addEntryAndReturnRequest(IBaseResource theResource) {
		Validate.notNull(theResource, "theResource must not be null");

		IBase entry = addEntry();

		// Bundle.entry.fullUrl
		IPrimitiveType<?> fullUrl = (IPrimitiveType<?>) myContext.getElementDefinition("uri").newInstance();
		fullUrl.setValueAsString(theResource.getIdElement().getValue());
		myEntryFullUrlChild.getMutator().setValue(entry, fullUrl);

		// Bundle.entry.resource
		myEntryResourceChild.getMutator().setValue(entry, theResource);

		// Bundle.entry.request
		IBase request = myEntryRequestDef.newInstance();
		myEntryRequestChild.getMutator().setValue(entry, request);
		return request;
	}

	public IBase addEntryAndReturnRequest() {
		IBase entry = addEntry();

		// Bundle.entry.request
		IBase request = myEntryRequestDef.newInstance();
		myEntryRequestChild.getMutator().setValue(entry, request);
		return request;

	}


	public IBaseBundle getBundle() {
		return myBundle;
	}

	public BundleBuilder setMetaField(String theFieldName, IBase theFieldValue) {
		BaseRuntimeChildDefinition.IMutator mutator = myMetaDef.getChildByName(theFieldName).getMutator();
		mutator.setValue(myBundle.getMeta(), theFieldValue);
		return this;
	}

	/**
	 * Sets the specified entry field.
	 *
	 * @param theEntry
	 * 		The entry instance to set values on
	 * @param theEntryChildName
	 * 		The child field name of the entry instance to be set
	 * @param theValue
	 * 		The field value to set
	 */
	public void addToEntry(IBase theEntry, String theEntryChildName, IBase theValue) {
		addToBase(theEntry, theEntryChildName, theValue, myEntryDef);
	}

	/**
	 * Sets the specified search field.
	 *
	 * @param theSearch
	 * 		The search instance to set values on
	 * @param theSearchFieldName
	 * 		The child field name of the search instance to be set
	 * @param theSearchFieldValue
	 * 		The field value to set
	 */
	public void addToSearch(IBase theSearch, String theSearchFieldName, IBase theSearchFieldValue) {
		addToBase(theSearch, theSearchFieldName, theSearchFieldValue, mySearchDef);
	}

	private void addToBase(IBase theBase, String theSearchChildName, IBase theValue, BaseRuntimeElementDefinition mySearchDef) {
		BaseRuntimeChildDefinition defn = mySearchDef.getChildByName(theSearchChildName);
		Validate.notNull(defn, "Unable to get child definition %s from %s", theSearchChildName, theBase);
		defn.getMutator().addValue(theBase, theValue);
	}

	/**
	 * Creates a new primitive.
	 *
	 * @param theTypeName
	 * 		The element type for the primitive
	 * @param <T>
	 *    	Actual type of the parameterized primitive type interface
	 * @return
	 * 		Returns the new empty instance of the element definition.
	 */
	public <T> IPrimitiveType<T> newPrimitive(String theTypeName) {
		BaseRuntimeElementDefinition primitiveDefinition = myContext.getElementDefinition(theTypeName);
		Validate.notNull(primitiveDefinition, "Unable to find definition for %s", theTypeName);
		return (IPrimitiveType<T>) primitiveDefinition.newInstance();
	}

	/**
	 * Creates a new primitive instance of the specified element type.
	 *
	 * @param theTypeName
	 * 		Element type to create
	 * @param theInitialValue
	 * 		Initial value to be set on the new instance
	 * @param <T>
	 *    	Actual type of the parameterized primitive type interface
	 * @return
	 * 		Returns the newly created instance
	 */
	public <T> IPrimitiveType<T> newPrimitive(String theTypeName, T theInitialValue) {
		IPrimitiveType<T> retVal = newPrimitive(theTypeName);
		retVal.setValue(theInitialValue);
		return retVal;
	}

	/**
	 * Sets a value for <code>Bundle.type</code>. That this is a coded field so {@literal theType}
	 * must be an actual valid value for this field or a {@link ca.uhn.fhir.parser.DataFormatException}
	 * will be thrown.
	 */
	public void setType(String theType) {
		setBundleField("type", theType);
	}

	public static class UpdateBuilder {

		private final IPrimitiveType<?> myUrl;

		public UpdateBuilder(IPrimitiveType<?> theUrl) {
			myUrl = theUrl;
		}

		/**
		 * Make this update a Conditional Update
		 */
		public void conditional(String theConditionalUrl) {
			myUrl.setValueAsString(theConditionalUrl);
		}
	}

	public class CreateBuilder {
		private final IBase myRequest;

		public CreateBuilder(IBase theRequest) {
			myRequest = theRequest;
		}

		/**
		 * Make this create a Conditional Create
		 */
		public void conditional(String theConditionalUrl) {
			BaseRuntimeElementDefinition<?> stringDefinition = Objects.requireNonNull(myContext.getElementDefinition("string"));
			IPrimitiveType<?> ifNoneExist = (IPrimitiveType<?>) stringDefinition.newInstance();
			ifNoneExist.setValueAsString(theConditionalUrl);

			myEntryRequestIfNoneExistChild.getMutator().setValue(myRequest, ifNoneExist);
		}

	}
}
