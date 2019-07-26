package ca.uhn.fhir.util;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.List;

public class AttachmentUtil {

	/**
	 * Fetches the base64Binary value of Attachment.data, creating it if it does not
	 * already exist.
	 */
	@SuppressWarnings("unchecked")
	public static IPrimitiveType<byte[]> getOrCreateData(FhirContext theContext, ICompositeType theAttachment) {
		return getOrCreateChild(theContext, theAttachment, "data", "base64Binary");
	}

	@SuppressWarnings("unchecked")
	public static IPrimitiveType<String> getOrCreateContentType(FhirContext theContext, ICompositeType theAttachment) {
		return getOrCreateChild(theContext, theAttachment, "contentType", "string");
	}

	public static IPrimitiveType<String> getOrCreateUrl(FhirContext theContext, ICompositeType theAttachment) {
		return getOrCreateChild(theContext, theAttachment, "url", "uri");
	}

	@SuppressWarnings("unchecked")
	private static <T> IPrimitiveType<T> getOrCreateChild(FhirContext theContext, ICompositeType theAttachment, String theChildName, String theChildDatatype) {
		BaseRuntimeChildDefinition entryChild = getChild(theContext, theAttachment, theChildName);
		List<IBase> entries = entryChild.getAccessor().getValues(theAttachment);
		return entries
			.stream()
			.map(t -> (IPrimitiveType<T>) t)
			.findFirst()
			.orElseGet(() -> {
				IPrimitiveType<String> string = newPrimitive(theContext, theChildDatatype, null);
				entryChild.getMutator().setValue(theAttachment, string);
				return (IPrimitiveType<T>) string;
			});
	}

	public static void setContentType(FhirContext theContext, ICompositeType theAttachment, String theContentType) {
		BaseRuntimeChildDefinition entryChild = getChild(theContext, theAttachment, "contentType");
		entryChild.getMutator().setValue(theAttachment, newPrimitive(theContext, "code", theContentType));
	}

	public static void setData(FhirContext theContext, ICompositeType theAttachment, byte[] theBytes) {
		BaseRuntimeChildDefinition entryChild = getChild(theContext, theAttachment, "data");
		entryChild.getMutator().setValue(theAttachment, newPrimitive(theContext, "base64Binary", theBytes));
	}

	public static void setSize(FhirContext theContext, ICompositeType theAttachment, Integer theLength) {
		BaseRuntimeChildDefinition entryChild = getChild(theContext, theAttachment, "size");
		if (theLength == null) {
			entryChild.getMutator().setValue(theAttachment, null);
		} else {
			entryChild.getMutator().setValue(theAttachment, newPrimitive(theContext, "unsignedInt", theLength));
		}
	}

	/**
	 * This is internal API- Use with caution as it may change
	 */
	@SuppressWarnings("unchecked")
	static <T> IPrimitiveType<T> newPrimitive(FhirContext theContext, String theType, T theValue) {
		IPrimitiveType<T> primitive = (IPrimitiveType<T>) theContext.getElementDefinition(theType).newInstance();
		primitive.setValue(theValue);
		return primitive;
	}

	/**
	 * This is internal API- Use with caution as it may change
	 */
	static BaseRuntimeChildDefinition getChild(FhirContext theContext, IBase theElement, String theName) {
		BaseRuntimeElementCompositeDefinition<?> def = (BaseRuntimeElementCompositeDefinition<?>) theContext.getElementDefinition(theElement.getClass());
		return def.getChildByName(theName);
	}
}
