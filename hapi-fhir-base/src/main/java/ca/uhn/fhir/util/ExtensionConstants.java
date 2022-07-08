package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
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

public class ExtensionConstants {
	
	/**
	 * Non instantiable
	 */
	private ExtensionConstants() {
		// nothing
	}
	
	public static final String PARAM_IS_REQUIRED = "http://hl7api.sourceforge.net/hapi-fhir/extensions.xml#paramIsRequired";

	public static final String QUERY_RETURN_TYPE = "http://hl7api.sourceforge.net/hapi-fhir/extensions.xml#queryReturnType";

	public static final String CONF_ADDITIONAL_PARAM = "http://hl7api.sourceforge.net/hapi-fhir/extensions.xml#additionalParam";
	
	public static final String CONF_ADDITIONAL_PARAM_NAME = "http://hl7api.sourceforge.net/hapi-fhir/extensions.xml#additionalParamName";

	public static final String CONF_ADDITIONAL_PARAM_DESCRIPTION = "http://hl7api.sourceforge.net/hapi-fhir/extensions.xml#additionalParamDescription";

	public static final String CONF_ADDITIONAL_PARAM_TYPE = "http://hl7api.sourceforge.net/hapi-fhir/extensions.xml#additionalParamType";

	public static final String CONF_ADDITIONAL_PARAM_REQUIRED = "http://hl7api.sourceforge.net/hapi-fhir/extensions.xml#additionalParamRequired";

	public static final String CONF_RESOURCE_COUNT = "http://hl7api.sourceforge.net/hapi-fhir/res/extdefs.html#resourceCount";

	public static final String QUERY_ALLOWED_INCLUDE = "http://hl7api.sourceforge.net/hapi-fhir/res/extdefs.html#allowedInclude";
	
}
