/*-
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.util;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.primitive.IdDt;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.Date;
import java.util.Objects;

/**
 * This class can be used to build a Bundle resource to be used as a FHIR transaction. Convenience methods provide
 * support for setting various bundle fields and working with bundle parts such as metadata and entry
 * (method and search).
 *
 * <p>
 * <p>
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

		if (myContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3)) {
			myMetaChild = myBundleDef.getChildByName("meta");
			myMetaDef = myMetaChild.getChildByName("meta");
		} else {
			myMetaChild = null;
			myMetaDef = null;
		}

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
	 * @param theFieldName  Name of the primitive field.
	 * @param theFieldValue Value of the field to be set.
	 */
	public BundleBuilder setBundleField(String theFieldName, String theFieldValue) {
		BaseRuntimeChildDefinition typeChild = myBundleDef.getChildByName(theFieldName);
		Validate.notNull(typeChild, "Unable to find field %s", theFieldName);

		IPrimitiveType<?> type = (IPrimitiveType<?>)
				typeChild.getChildByName(theFieldName).newInstance(typeChild.getInstanceConstructorArguments());
		type.setValueAsString(theFieldValue);
		typeChild.getMutator().setValue(myBundle, type);
		return this;
	}

	/**
	 * Sets the specified primitive field on the search entry with the value provided.
	 *
	 * @param theSearch     Search part of the entry
	 * @param theFieldName  Name of the primitive field.
	 * @param theFieldValue Value of the field to be set.
	 */
	public BundleBuilder setSearchField(IBase theSearch, String theFieldName, String theFieldValue) {
		BaseRuntimeChildDefinition typeChild = mySearchDef.getChildByName(theFieldName);
		Validate.notNull(typeChild, "Unable to find field %s", theFieldName);

		IPrimitiveType<?> type = (IPrimitiveType<?>)
				typeChild.getChildByName(theFieldName).newInstance(typeChild.getInstanceConstructorArguments());
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
	 * Adds a FHIRPatch patch bundle to the transaction
	 *
	 * @param theTarget The target resource ID to patch
	 * @param thePatch  The FHIRPath Parameters resource
	 * @since 6.3.0
	 */
	public PatchBuilder addTransactionFhirPatchEntry(IIdType theTarget, IBaseParameters thePatch) {
		Validate.notNull(theTarget, "theTarget must not be null");
		Validate.notBlank(theTarget.getResourceType(), "theTarget must contain a resource type");
		Validate.notBlank(theTarget.getIdPart(), "theTarget must contain an ID");

		IPrimitiveType<?> url = addAndPopulateTransactionBundleEntryRequest(
				thePatch,
				theTarget.getValue(),
				theTarget.toUnqualifiedVersionless().getValue(),
				"PATCH");

		return new PatchBuilder(url);
	}

	/**
	 * Adds a FHIRPatch patch bundle to the transaction. This method is intended for conditional PATCH operations. If you
	 * know the ID of the resource you wish to patch, use {@link #addTransactionFhirPatchEntry(IIdType, IBaseParameters)}
	 * instead.
	 *
	 * @param thePatch The FHIRPath Parameters resource
	 * @see #addTransactionFhirPatchEntry(IIdType, IBaseParameters)
	 * @since 6.3.0
	 */
	public PatchBuilder addTransactionFhirPatchEntry(IBaseParameters thePatch) {
		IPrimitiveType<?> url = addAndPopulateTransactionBundleEntryRequest(thePatch, null, null, "PATCH");

		return new PatchBuilder(url);
	}

	/**
	 * Adds an entry containing an update (PUT) request.
	 * Also sets the Bundle.type value to "transaction" if it is not already set.
	 *
	 * @param theResource The resource to update
	 */
	public UpdateBuilder addTransactionUpdateEntry(IBaseResource theResource) {
		Validate.notNull(theResource, "theResource must not be null");

		IIdType id = getIdTypeForUpdate(theResource);

		String requestUrl = id.toUnqualifiedVersionless().getValue();
		String fullUrl = id.getValue();
		String verb = "PUT";

		IPrimitiveType<?> url = addAndPopulateTransactionBundleEntryRequest(theResource, fullUrl, requestUrl, verb);

		return new UpdateBuilder(url);
	}

	@Nonnull
	private IPrimitiveType<?> addAndPopulateTransactionBundleEntryRequest(
			IBaseResource theResource, String theFullUrl, String theRequestUrl, String theHttpVerb) {
		setBundleField("type", "transaction");

		IBase request = addEntryAndReturnRequest(theResource, theFullUrl);

		// Bundle.entry.request.url
		IPrimitiveType<?> url =
				(IPrimitiveType<?>) myContext.getElementDefinition("uri").newInstance();
		url.setValueAsString(theRequestUrl);
		myEntryRequestUrlChild.getMutator().setValue(request, url);

		// Bundle.entry.request.method
		addRequestMethod(request, theHttpVerb);
		return url;
	}

	/**
	 * Adds an entry containing an update (UPDATE) request without the body of the resource.
	 * Also sets the Bundle.type value to "transaction" if it is not already set.
	 *
	 * @param theResource The resource to update.
	 */
	public void addTransactionUpdateIdOnlyEntry(IBaseResource theResource) {
		setBundleField("type", "transaction");

		Validate.notNull(theResource, "theResource must not be null");

		IIdType id = getIdTypeForUpdate(theResource);
		String requestUrl = id.toUnqualifiedVersionless().getValue();
		String fullUrl = id.getValue();
		String httpMethod = "PUT";

		addIdOnlyEntry(requestUrl, httpMethod, fullUrl);
	}

	/**
	 * Adds an entry containing an create (POST) request.
	 * Also sets the Bundle.type value to "transaction" if it is not already set.
	 *
	 * @param theResource The resource to create
	 */
	public CreateBuilder addTransactionCreateEntry(IBaseResource theResource) {
		return addTransactionCreateEntry(theResource, null);
	}

	/**
	 * Adds an entry containing an create (POST) request.
	 * Also sets the Bundle.type value to "transaction" if it is not already set.
	 *
	 * @param theResource The resource to create
	 * @param theFullUrl The fullUrl to attach to the entry.  If null, will default to the resource ID.
	 */
	public CreateBuilder addTransactionCreateEntry(IBaseResource theResource, @Nullable String theFullUrl) {
		setBundleField("type", "transaction");

		IBase request = addEntryAndReturnRequest(
				theResource,
				theFullUrl != null ? theFullUrl : theResource.getIdElement().getValue());

		String resourceType = myContext.getResourceType(theResource);

		// Bundle.entry.request.url
		addRequestUrl(request, resourceType);

		// Bundle.entry.request.method
		addRequestMethod(request, "POST");

		return new CreateBuilder(request);
	}

	/**
	 * Adds an entry containing a create (POST) request without the body of the resource.
	 * Also sets the Bundle.type value to "transaction" if it is not already set.
	 *
	 * @param theResource The resource to create
	 */
	public void addTransactionCreateEntryIdOnly(IBaseResource theResource) {
		setBundleField("type", "transaction");

		String requestUrl = myContext.getResourceType(theResource);
		String fullUrl = theResource.getIdElement().getValue();
		String httpMethod = "POST";

		addIdOnlyEntry(requestUrl, httpMethod, fullUrl);
	}

	private void addIdOnlyEntry(String theRequestUrl, String theHttpMethod, String theFullUrl) {
		IBase entry = addEntry();

		// Bundle.entry.request
		IBase request = myEntryRequestDef.newInstance();
		myEntryRequestChild.getMutator().setValue(entry, request);

		// Bundle.entry.request.url
		addRequestUrl(request, theRequestUrl);

		// Bundle.entry.request.method
		addRequestMethod(request, theHttpMethod);

		// Bundle.entry.fullUrl
		addFullUrl(entry, theFullUrl);
	}

	/**
	 * Adds an entry containing a delete (DELETE) request.
	 * Also sets the Bundle.type value to "transaction" if it is not already set.
	 * <p>
	 * Note that the resource is only used to extract its ID and type, and the body of the resource is not included in the entry,
	 *
	 * @param theCondition The conditional URL, e.g. "Patient?identifier=foo|bar"
	 * @since 6.8.0
	 */
	public DeleteBuilder addTransactionDeleteConditionalEntry(String theCondition) {
		Validate.notBlank(theCondition, "theCondition must not be blank");

		setBundleField("type", "transaction");
		return addDeleteEntry(theCondition);
	}

	/**
	 * Adds an entry containing a delete (DELETE) request.
	 * Also sets the Bundle.type value to "transaction" if it is not already set.
	 * <p>
	 * Note that the resource is only used to extract its ID and type, and the body of the resource is not included in the entry,
	 *
	 * @param theResource The resource to delete.
	 */
	public DeleteBuilder addTransactionDeleteEntry(IBaseResource theResource) {
		String resourceType = myContext.getResourceType(theResource);
		String idPart = theResource.getIdElement().toUnqualifiedVersionless().getIdPart();
		return addTransactionDeleteEntry(resourceType, idPart);
	}

	/**
	 * Adds an entry containing a delete (DELETE) request.
	 * Also sets the Bundle.type value to "transaction" if it is not already set.
	 * <p>
	 * Note that the resource is only used to extract its ID and type, and the body of the resource is not included in the entry,
	 *
	 * @param theResourceId The resource ID to delete.
	 * @return
	 */
	public DeleteBuilder addTransactionDeleteEntry(IIdType theResourceId) {
		String resourceType = theResourceId.getResourceType();
		String idPart = theResourceId.getIdPart();
		return addTransactionDeleteEntry(resourceType, idPart);
	}

	/**
	 * Adds an entry containing a delete (DELETE) request.
	 * Also sets the Bundle.type value to "transaction" if it is not already set.
	 *
	 * @param theResourceType The type resource to delete.
	 * @param theIdPart       the ID of the resource to delete.
	 */
	public DeleteBuilder addTransactionDeleteEntry(String theResourceType, String theIdPart) {
		setBundleField("type", "transaction");
		IdDt idDt = new IdDt(theIdPart);

		String deleteUrl = idDt.toUnqualifiedVersionless()
				.withResourceType(theResourceType)
				.getValue();

		return addDeleteEntry(deleteUrl);
	}

	/**
	 * Adds an entry containing a delete (DELETE) request.
	 * Also sets the Bundle.type value to "transaction" if it is not already set.
	 *
	 * @param theMatchUrl The match URL, e.g. <code>Patient?identifier=http://foo|123</code>
	 * @since 6.3.0
	 */
	public BaseOperationBuilder addTransactionDeleteEntryConditional(String theMatchUrl) {
		Validate.notBlank(theMatchUrl, "theMatchUrl must not be null or blank");
		return addDeleteEntry(theMatchUrl);
	}

	@Nonnull
	private DeleteBuilder addDeleteEntry(String theDeleteUrl) {
		IBase request = addEntryAndReturnRequest();

		// Bundle.entry.request.url
		addRequestUrl(request, theDeleteUrl);

		// Bundle.entry.request.method
		addRequestMethod(request, "DELETE");

		return new DeleteBuilder();
	}

	private IIdType getIdTypeForUpdate(IBaseResource theResource) {
		IIdType id = theResource.getIdElement();
		if (id.hasIdPart() && !id.hasResourceType()) {
			String resourceType = myContext.getResourceType(theResource);
			id = id.withResourceType(resourceType);
		}
		return id;
	}

	private void addFullUrl(IBase theEntry, String theFullUrl) {
		IPrimitiveType<?> fullUrl =
				(IPrimitiveType<?>) myContext.getElementDefinition("uri").newInstance();
		fullUrl.setValueAsString(theFullUrl);
		myEntryFullUrlChild.getMutator().setValue(theEntry, fullUrl);
	}

	private void addRequestUrl(IBase request, String theRequestUrl) {
		IPrimitiveType<?> url =
				(IPrimitiveType<?>) myContext.getElementDefinition("uri").newInstance();
		url.setValueAsString(theRequestUrl);
		myEntryRequestUrlChild.getMutator().setValue(request, url);
	}

	private void addRequestMethod(IBase theRequest, String theMethod) {
		IPrimitiveType<?> method = (IPrimitiveType<?>)
				myEntryRequestMethodDef.newInstance(myEntryRequestMethodChild.getInstanceConstructorArguments());
		method.setValueAsString(theMethod);
		myEntryRequestMethodChild.getMutator().setValue(theRequest, method);
	}

	/**
	 * Adds an entry for a Collection bundle type
	 */
	public void addCollectionEntry(IBaseResource theResource) {
		setType("collection");
		addEntryAndReturnRequest(theResource);
	}

	/**
	 * Adds an entry for a Document bundle type
	 */
	public void addDocumentEntry(IBaseResource theResource) {
		setType("document");
		addEntryAndReturnRequest(theResource);
	}

	/**
	 * Creates new entry and adds it to the bundle
	 *
	 * @return Returns the new entry.
	 */
	public IBase addEntry() {
		IBase entry = myEntryDef.newInstance();
		myEntryChild.getMutator().addValue(myBundle, entry);
		return entry;
	}

	/**
	 * Creates new search instance for the specified entry.
	 * Note that this method does not work for DSTU2 model classes, it will only work
	 * on DSTU3+.
	 *
	 * @param entry Entry to create search instance for
	 * @return Returns the search instance
	 */
	public IBaseBackboneElement addSearch(IBase entry) {
		Validate.isTrue(
				myContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3),
				"This method may only be called for FHIR version DSTU3 and above");

		IBase searchInstance = mySearchDef.newInstance();
		mySearchChild.getMutator().setValue(entry, searchInstance);
		return (IBaseBackboneElement) searchInstance;
	}

	private IBase addEntryAndReturnRequest(IBaseResource theResource) {
		IIdType id = theResource.getIdElement();
		if (id.hasVersionIdPart()) {
			id = id.toVersionless();
		}
		return addEntryAndReturnRequest(theResource, id.getValue());
	}

	private IBase addEntryAndReturnRequest(IBaseResource theResource, String theFullUrl) {
		Validate.notNull(theResource, "theResource must not be null");

		IBase entry = addEntry();

		// Bundle.entry.fullUrl
		addFullUrl(entry, theFullUrl);

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

	/**
	 * Convenience method which auto-casts the results of {@link #getBundle()}
	 *
	 * @since 6.3.0
	 */
	public <T extends IBaseBundle> T getBundleTyped() {
		return (T) myBundle;
	}

	/**
	 * Note that this method does not work for DSTU2 model classes, it will only work
	 * on DSTU3+.
	 */
	public BundleBuilder setMetaField(String theFieldName, IBase theFieldValue) {
		Validate.isTrue(
				myContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3),
				"This method may only be called for FHIR version DSTU3 and above");

		BaseRuntimeChildDefinition.IMutator mutator =
				myMetaDef.getChildByName(theFieldName).getMutator();
		mutator.setValue(myBundle.getMeta(), theFieldValue);
		return this;
	}

	/**
	 * Sets the specified entry field.
	 *
	 * @param theEntry          The entry instance to set values on
	 * @param theEntryChildName The child field name of the entry instance to be set
	 * @param theValue          The field value to set
	 */
	public void addToEntry(IBase theEntry, String theEntryChildName, IBase theValue) {
		addToBase(theEntry, theEntryChildName, theValue, myEntryDef);
	}

	/**
	 * Sets the specified search field.
	 *
	 * @param theSearch           The search instance to set values on
	 * @param theSearchFieldName  The child field name of the search instance to be set
	 * @param theSearchFieldValue The field value to set
	 */
	public void addToSearch(IBase theSearch, String theSearchFieldName, IBase theSearchFieldValue) {
		addToBase(theSearch, theSearchFieldName, theSearchFieldValue, mySearchDef);
	}

	private void addToBase(
			IBase theBase, String theSearchChildName, IBase theValue, BaseRuntimeElementDefinition mySearchDef) {
		BaseRuntimeChildDefinition defn = mySearchDef.getChildByName(theSearchChildName);
		Validate.notNull(defn, "Unable to get child definition %s from %s", theSearchChildName, theBase);
		defn.getMutator().addValue(theBase, theValue);
	}

	/**
	 * Creates a new primitive.
	 *
	 * @param theTypeName The element type for the primitive
	 * @param <T>         Actual type of the parameterized primitive type interface
	 * @return Returns the new empty instance of the element definition.
	 */
	public <T> IPrimitiveType<T> newPrimitive(String theTypeName) {
		BaseRuntimeElementDefinition primitiveDefinition = myContext.getElementDefinition(theTypeName);
		Validate.notNull(primitiveDefinition, "Unable to find definition for %s", theTypeName);
		return (IPrimitiveType<T>) primitiveDefinition.newInstance();
	}

	/**
	 * Creates a new primitive instance of the specified element type.
	 *
	 * @param theTypeName     Element type to create
	 * @param theInitialValue Initial value to be set on the new instance
	 * @param <T>             Actual type of the parameterized primitive type interface
	 * @return Returns the newly created instance
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

	/**
	 * Adds an identifier to <code>Bundle.identifier</code>
	 *
	 * @param theSystem The system
	 * @param theValue  The value
	 * @since 6.4.0
	 */
	public void setIdentifier(@Nullable String theSystem, @Nullable String theValue) {
		FhirTerser terser = myContext.newTerser();
		IBase identifier = terser.addElement(myBundle, "identifier");
		terser.setElement(identifier, "system", theSystem);
		terser.setElement(identifier, "value", theValue);
	}

	/**
	 * Sets the timestamp in <code>Bundle.timestamp</code>
	 *
	 * @since 6.4.0
	 */
	public void setTimestamp(@Nonnull IPrimitiveType<Date> theTimestamp) {
		FhirTerser terser = myContext.newTerser();
		terser.setElement(myBundle, "Bundle.timestamp", theTimestamp.getValueAsString());
	}

	/**
	 * Adds a profile URL to <code>Bundle.meta.profile</code>
	 *
	 * @since 7.4.0
	 */
	public void addProfile(String theProfile) {
		FhirTerser terser = myContext.newTerser();
		terser.addElement(myBundle, "Bundle.meta.profile", theProfile);
	}

	public class DeleteBuilder extends BaseOperationBuilder {

		// nothing yet

	}

	public class PatchBuilder extends BaseOperationBuilderWithConditionalUrl<PatchBuilder> {

		PatchBuilder(IPrimitiveType<?> theUrl) {
			super(theUrl);
		}
	}

	public class UpdateBuilder extends BaseOperationBuilderWithConditionalUrl<UpdateBuilder> {
		UpdateBuilder(IPrimitiveType<?> theUrl) {
			super(theUrl);
		}
	}

	public class CreateBuilder extends BaseOperationBuilder {
		private final IBase myRequest;

		CreateBuilder(IBase theRequest) {
			myRequest = theRequest;
		}

		/**
		 * Make this create a Conditional Create
		 */
		public CreateBuilder conditional(String theConditionalUrl) {
			BaseRuntimeElementDefinition<?> stringDefinition =
					Objects.requireNonNull(myContext.getElementDefinition("string"));
			IPrimitiveType<?> ifNoneExist = (IPrimitiveType<?>) stringDefinition.newInstance();
			ifNoneExist.setValueAsString(theConditionalUrl);

			myEntryRequestIfNoneExistChild.getMutator().setValue(myRequest, ifNoneExist);

			return this;
		}
	}

	public abstract class BaseOperationBuilder {

		/**
		 * Returns a reference to the BundleBuilder instance.
		 * <p>
		 * Calling this method has no effect at all, it is only
		 * provided for easy method chaning if you want to build
		 * your bundle as a single fluent call.
		 *
		 * @since 6.3.0
		 */
		public BundleBuilder andThen() {
			return BundleBuilder.this;
		}
	}

	public abstract class BaseOperationBuilderWithConditionalUrl<T extends BaseOperationBuilder>
			extends BaseOperationBuilder {

		private final IPrimitiveType<?> myUrl;

		BaseOperationBuilderWithConditionalUrl(IPrimitiveType<?> theUrl) {
			myUrl = theUrl;
		}

		/**
		 * Make this update a Conditional Update
		 */
		@SuppressWarnings("unchecked")
		public T conditional(String theConditionalUrl) {
			myUrl.setValueAsString(theConditionalUrl);
			return (T) this;
		}
	}
}
