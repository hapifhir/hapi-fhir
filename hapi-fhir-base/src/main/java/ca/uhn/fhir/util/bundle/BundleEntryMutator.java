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
package ca.uhn.fhir.util.bundle;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.Date;

public class BundleEntryMutator {
	private final IBase myEntry;
	private final BaseRuntimeChildDefinition myRequestChildDef;
	private final BaseRuntimeElementCompositeDefinition<?> myRequestChildContentsDef;
	private final FhirContext myFhirContext;
	private final BaseRuntimeElementCompositeDefinition<?> myEntryDefinition;
	private final BaseRuntimeChildDefinition myMethodChildDef;

	public BundleEntryMutator(
			FhirContext theFhirContext,
			IBase theEntry,
			BaseRuntimeChildDefinition theRequestChildDef,
			BaseRuntimeElementCompositeDefinition<?> theChildContentsDef,
			BaseRuntimeElementCompositeDefinition<?> theEntryDefinition,
			BaseRuntimeChildDefinition theMethodChildDef) {
		myFhirContext = theFhirContext;
		myEntry = theEntry;
		myRequestChildDef = theRequestChildDef;
		myRequestChildContentsDef = theChildContentsDef;
		myMethodChildDef = theMethodChildDef;
		myEntryDefinition = theEntryDefinition;
	}

	void setRequestUrl(String theRequestUrl) {
		BaseRuntimeChildDefinition requestUrlChildDef = myRequestChildContentsDef.getChildByName("url");
		IPrimitiveType<?> url = ParametersUtil.createUri(myFhirContext, theRequestUrl);
		for (IBase nextRequest : myRequestChildDef.getAccessor().getValues(myEntry)) {
			requestUrlChildDef.getMutator().addValue(nextRequest, url);
		}
	}

	@SuppressWarnings("unchecked")
	public void setFullUrl(String theFullUrl) {
		IPrimitiveType<String> value = (IPrimitiveType<String>)
				myFhirContext.getElementDefinition("uri").newInstance();
		value.setValue(theFullUrl);

		BaseRuntimeChildDefinition fullUrlChild = myEntryDefinition.getChildByName("fullUrl");
		fullUrlChild.getMutator().setValue(myEntry, value);
	}

	public void setResource(IBaseResource theUpdatedResource) {
		BaseRuntimeChildDefinition resourceChild = myEntryDefinition.getChildByName("resource");
		resourceChild.getMutator().setValue(myEntry, theUpdatedResource);
	}

	public void setRequestIfNoneMatch(String theIfNoneMatch) {
		BaseRuntimeChildDefinition requestUrlChildDef = myRequestChildContentsDef.getChildByName("ifNoneMatch");
		IPrimitiveType<?> url = ParametersUtil.createString(myFhirContext, theIfNoneMatch);
		for (IBase nextRequest : myRequestChildDef.getAccessor().getValues(myEntry)) {
			requestUrlChildDef.getMutator().addValue(nextRequest, url);
		}
	}

	public void setRequestIfModifiedSince(Date theIfModifiedSince) {
		BaseRuntimeChildDefinition requestUrlChildDef = myRequestChildContentsDef.getChildByName("ifModifiedSince");
		IPrimitiveType<?> url = ParametersUtil.createInstant(myFhirContext, theIfModifiedSince);
		for (IBase nextRequest : myRequestChildDef.getAccessor().getValues(myEntry)) {
			requestUrlChildDef.getMutator().addValue(nextRequest, url);
		}
	}

	public void setRequestIfMatch(String theIfMatch) {
		BaseRuntimeChildDefinition requestUrlChildDef = myRequestChildContentsDef.getChildByName("ifMatch");
		IPrimitiveType<?> url = ParametersUtil.createString(myFhirContext, theIfMatch);
		for (IBase nextRequest : myRequestChildDef.getAccessor().getValues(myEntry)) {
			requestUrlChildDef.getMutator().addValue(nextRequest, url);
		}
	}

	public void setRequestIfNoneExist(String theIfNoneExist) {
		BaseRuntimeChildDefinition requestUrlChildDef = myRequestChildContentsDef.getChildByName("ifNoneExist");
		IPrimitiveType<?> url = ParametersUtil.createString(myFhirContext, theIfNoneExist);
		for (IBase nextRequest : myRequestChildDef.getAccessor().getValues(myEntry)) {
			requestUrlChildDef.getMutator().addValue(nextRequest, url);
		}
	}

	public void setMethod(RequestTypeEnum theMethod) {
		BaseRuntimeChildDefinition methodChildDef = myRequestChildContentsDef.getChildByName("method");
		BaseRuntimeElementDefinition<?> methodElement = methodChildDef.getChildByName("method");
		IPrimitiveType<?> newValue =
				(IPrimitiveType<?>) methodElement.newInstance(methodChildDef.getInstanceConstructorArguments());
		newValue.setValueAsString(theMethod.name());

		for (IBase nextRequest : myRequestChildDef.getAccessor().getValues(myEntry)) {
			methodChildDef.getMutator().setValue(nextRequest, newValue);
		}
	}
}
