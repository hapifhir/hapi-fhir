package ca.uhn.fhir.jpa.util.xmlpatch;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.hl7.fhir.instance.model.api.IBaseResource;

import com.github.dnault.xmlpatch.Patcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class XmlPatchUtils {

	public static <T extends IBaseResource> T apply(FhirContext theCtx, T theResourceToUpdate, String thePatchBody) {
		
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) theResourceToUpdate.getClass();
		
		String inputResource = theCtx.newXmlParser().encodeResourceToString(theResourceToUpdate);
		
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		try {
			Patcher.patch(new ByteArrayInputStream(inputResource.getBytes(Constants.CHARSET_UTF8)), new ByteArrayInputStream(thePatchBody.getBytes(Constants.CHARSET_UTF8)), result);
		} catch (IOException e) {
			throw new InternalErrorException(e);
		}
		
		String resultString = new String(result.toByteArray(), Constants.CHARSET_UTF8);
		T retVal = theCtx.newXmlParser().parseResource(clazz, resultString);
		
		return retVal;
	}

	
}
