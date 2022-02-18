package ca.uhn.fhir.rest.client.apache;

/*-
 * #%L
 * HAPI FHIR - Client Framework
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.nio.charset.UnsupportedCharsetException;

/**
 * Apache HttpClient request content entity where the body is a FHIR resource, that will
 * be encoded as JSON by default
 */
public class ResourceEntity extends StringEntity {

	public ResourceEntity(FhirContext theContext, IBaseResource theResource) throws UnsupportedCharsetException {
		super(theContext.newJsonParser().encodeResourceToString(theResource), ContentType.parse(Constants.CT_FHIR_JSON_NEW));
	}
}
