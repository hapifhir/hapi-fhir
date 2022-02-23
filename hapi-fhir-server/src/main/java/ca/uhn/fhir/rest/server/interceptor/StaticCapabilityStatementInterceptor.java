package ca.uhn.fhir.rest.server.interceptor;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.ClasspathUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseConformance;

/**
 * This interceptor replaces the auto-generated CapabilityStatement that is generated
 * by the HAPI FHIR Server with a static hard-coded resource.
 */
@Interceptor
public class StaticCapabilityStatementInterceptor {

	private String myCapabilityStatementResource;
	private volatile IBaseConformance myCapabilityStatement;

	/**
	 * Sets the CapabilityStatement to use
	 *
	 * @see #setCapabilityStatementResource(String) #setCapabilityStatementResource(String) is an alternate way to supply the CapabilityStatement
	 */
	public void setCapabilityStatement(IBaseConformance theCapabilityStatement) {
		myCapabilityStatement = theCapabilityStatement;
	}

	/**
	 * Sets the classpath location of the CapabilityStatement to use. If this method is used to supply
	 * the CapabiltyStatement, then the given classpath resource will be read and parsed as a FHIR
	 * CapabilityStatement.
	 *
	 * @see #setCapabilityStatement(IBaseConformance) #setCapabilityStatement(IBaseConformance) is an alternate way to supply the CapabilityStatement
	 */
	public void setCapabilityStatementResource(String theClasspath) {
		myCapabilityStatementResource = theClasspath;
		myCapabilityStatement = null;
	}

	@Hook(Pointcut.SERVER_CAPABILITY_STATEMENT_GENERATED)
	public IBaseConformance hook(RequestDetails theRequestDetails) {
		IBaseConformance retVal = myCapabilityStatement;

		if (retVal == null) {
			Validate.notBlank(myCapabilityStatementResource, "No CapabilityStatement defined");
			String output = ClasspathUtil.loadResource(myCapabilityStatementResource);

			FhirContext ctx = theRequestDetails.getFhirContext();
			EncodingEnum encoding = EncodingEnum.detectEncodingNoDefault(output);
			Validate.notNull(encoding, "Could not determine FHIR encoding for resource: %s", myCapabilityStatementResource);

			retVal = (IBaseConformance) encoding
				.newParser(ctx)
				.parseResource(output);
			myCapabilityStatement = retVal;
		}

		return retVal;
	}

}
