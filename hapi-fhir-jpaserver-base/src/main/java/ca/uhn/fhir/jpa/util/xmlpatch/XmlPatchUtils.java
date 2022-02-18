package ca.uhn.fhir.jpa.util.xmlpatch;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.github.dnault.xmlpatch.Patcher;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/*
 * #%L
 * HAPI FHIR JPA Server
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

public class XmlPatchUtils {

	public static <T extends IBaseResource> T apply(FhirContext theCtx, T theResourceToUpdate, String thePatchBody) {
		
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) theResourceToUpdate.getClass();
		
		String inputResource = theCtx.newXmlParser().encodeResourceToString(theResourceToUpdate);
		
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		try {
			Patcher.patch(new ByteArrayInputStream(inputResource.getBytes(Constants.CHARSET_UTF8)), new ByteArrayInputStream(thePatchBody.getBytes(Constants.CHARSET_UTF8)), result);
		} catch (IOException e) {
			throw new InternalErrorException(Msg.code(817) + e);
		}
		
		String resultString = new String(result.toByteArray(), Constants.CHARSET_UTF8);
		T retVal = theCtx.newXmlParser().parseResource(clazz, resultString);
		
		return retVal;
	}

	
}
