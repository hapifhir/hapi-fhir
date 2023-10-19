/*-
 * #%L
 * HAPI FHIR - Command Line Client - API
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class LocalFileValidationSupport extends PrePopulatedValidationSupport {

	public LocalFileValidationSupport(FhirContext ctx) {
		super(ctx);
	}

	@Override
	public FhirContext getFhirContext() {
		return myCtx;
	}

	public void loadFile(String theFileName) throws IOException {
		String contents = IOUtils.toString(new InputStreamReader(new FileInputStream(theFileName), "UTF-8"));
		IBaseResource resource = myCtx.newJsonParser().parseResource(contents);
		this.addResource(resource);
	}
}
