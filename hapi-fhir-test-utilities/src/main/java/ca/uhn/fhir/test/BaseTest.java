package ca.uhn.fhir.test;

/*-
 * #%L
 * HAPI FHIR Test Utilities
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
import ca.uhn.fhir.util.ClasspathUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.IOException;

public class BaseTest {

	static {
		ToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
	}

	protected String loadResource(String theClasspath) throws IOException {
		return ClasspathUtil.loadResource(theClasspath);
	}

	protected String loadCompressedResource(String theClasspath) throws IOException {
		return ClasspathUtil.loadCompressedResource(theClasspath);
	}

	protected <T extends IBaseResource> T loadResource(FhirContext theCtx, Class<T> theType, String theClasspath) throws IOException {
		return ClasspathUtil.loadResource(theCtx, theType, theClasspath);
	}
}
