package ca.uhn.fhir.jpa.util.xmlpatch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.hl7.fhir.instance.model.api.IBaseResource;

import com.github.dnault.xmlpatch.Patcher;
import com.phloc.commons.io.streams.StringInputStream;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class XmlPatchUtils {

	public static <T extends IBaseResource> T apply(FhirContext theCtx, T theResourceToUpdate, String thePatchBody) {
		
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) theResourceToUpdate.getClass();
		
		String inputResource = theCtx.newXmlParser().encodeResourceToString(theResourceToUpdate);
		
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		try {
			Patcher.patch(new StringInputStream(inputResource, StandardCharsets.UTF_8), new StringInputStream(thePatchBody, StandardCharsets.UTF_8), result);
		} catch (IOException e) {
			throw new InternalErrorException(e);
		}
		
		String resultString = new String(result.toByteArray(), StandardCharsets.UTF_8);
		T retVal = theCtx.newXmlParser().parseResource(clazz, resultString);
		
		return retVal;
	}

	
}
