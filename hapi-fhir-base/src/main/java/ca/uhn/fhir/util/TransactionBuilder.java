package ca.uhn.fhir.util;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.thymeleaf.util.Validate;

/**
 * This class can be used to build a Bundle resource to be used as a FHIR transaction.
 * <p>
 * This is not yet complete, and doesn't support all FHIR features. <b>USE WITH CAUTION</b> as the API
 * may change.
 *
 * @since 5.1.0
 */
public class TransactionBuilder {

	private final FhirContext myContext;
	private final IBaseBundle myBundle;
	private final RuntimeResourceDefinition myBundleDef;
	private final BaseRuntimeChildDefinition myEntryChild;
	private final BaseRuntimeElementDefinition<?> myEntryDef;
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
	public TransactionBuilder(FhirContext theContext) {
		myContext = theContext;

		myBundleDef = myContext.getResourceDefinition("Bundle");
		myBundle = (IBaseBundle) myBundleDef.newInstance();

		BaseRuntimeChildDefinition typeChild = myBundleDef.getChildByName("type");
		IPrimitiveType<?> type = (IPrimitiveType<?>) typeChild.getChildByName("type").newInstance(typeChild.getInstanceConstructorArguments());
		type.setValueAsString("transaction");
		typeChild.getMutator().setValue(myBundle, type);

		myEntryChild = myBundleDef.getChildByName("entry");
		myEntryDef = myEntryChild.getChildByName("entry");

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
	 * Adds an entry containing an update (PUT) request
	 *
	 * @param theResource The resource to update
	 */
	public UpdateBuilder addUpdateEntry(IBaseResource theResource) {
		IBase request = addEntryAndReturnRequest(theResource);

		// Bundle.entry.request.url
		IPrimitiveType<?> url = (IPrimitiveType<?>) myContext.getElementDefinition("uri").newInstance();
		url.setValueAsString(theResource.getIdElement().toUnqualifiedVersionless().getValue());
		myEntryRequestUrlChild.getMutator().setValue(request, url);

		// Bundle.entry.request.url
		IPrimitiveType<?> method = (IPrimitiveType<?>) myEntryRequestMethodDef.newInstance(myEntryRequestMethodChild.getInstanceConstructorArguments());
		method.setValueAsString("PUT");
		myEntryRequestMethodChild.getMutator().setValue(request, method);

		return new UpdateBuilder(url);
	}

	/**
	 * Adds an entry containing an create (POST) request
	 *
	 * @param theResource The resource to create
	 */
	public CreateBuilder addCreateEntry(IBaseResource theResource) {
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

	public IBase addEntryAndReturnRequest(IBaseResource theResource) {
		Validate.notNull(theResource, "theResource must not be null");

		IBase entry = myEntryDef.newInstance();
		myEntryChild.getMutator().addValue(myBundle, entry);

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


	public IBaseBundle getBundle() {
		return myBundle;
	}

	public class UpdateBuilder {

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
			IPrimitiveType<?> ifNoneExist = (IPrimitiveType<?>) myContext.getElementDefinition("string").newInstance();
			ifNoneExist.setValueAsString(theConditionalUrl);

			myEntryRequestIfNoneExistChild.getMutator().setValue(myRequest, ifNoneExist);
		}

	}
}
