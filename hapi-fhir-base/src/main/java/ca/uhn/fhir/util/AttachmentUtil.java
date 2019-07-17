package ca.uhn.fhir.util;

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
		BaseRuntimeChildDefinition entryChild = getChild(theContext, theAttachment, "data");
		List<IBase> entries = entryChild.getAccessor().getValues(theAttachment);
		return entries
			.stream()
			.map(t -> (IPrimitiveType<byte[]>) t)
			.findFirst()
			.orElseGet(() -> {
				IPrimitiveType<byte[]> binary = newPrimitive(theContext, "base64Binary", null);
				entryChild.getMutator().setValue(theAttachment, binary);
				return binary;
			});
	}

	@SuppressWarnings("unchecked")
	public static IPrimitiveType<String> getOrCreateContentType(FhirContext theContext, ICompositeType theAttachment) {
		BaseRuntimeChildDefinition entryChild = getChild(theContext, theAttachment, "contentType");
		List<IBase> entries = entryChild.getAccessor().getValues(theAttachment);
		return entries
			.stream()
			.map(t -> (IPrimitiveType<String>) t)
			.findFirst()
			.orElseGet(() -> {
				IPrimitiveType<String> string = newPrimitive(theContext, "string", null);
				entryChild.getMutator().setValue(theAttachment, string);
				return string;
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

	@SuppressWarnings("unchecked")
	private static <T> IPrimitiveType<T> newPrimitive(FhirContext theContext, String theType, T theValue) {
		IPrimitiveType<T> primitive = (IPrimitiveType<T>) theContext.getElementDefinition(theType).newInstance();
		primitive.setValue(theValue);
		return primitive;
	}

	private static BaseRuntimeChildDefinition getChild(FhirContext theContext, ICompositeType theAttachment, String theName) {
		BaseRuntimeElementCompositeDefinition<?> def = (BaseRuntimeElementCompositeDefinition<?>) theContext.getElementDefinition(theAttachment.getClass());
		return def.getChildByName(theName);
	}
}
