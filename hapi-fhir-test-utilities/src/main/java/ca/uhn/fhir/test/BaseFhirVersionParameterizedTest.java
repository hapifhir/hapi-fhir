/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.test.utilities.BaseRestServerHelper;
import ca.uhn.fhir.test.utilities.RestServerDstu3Helper;
import ca.uhn.fhir.test.utilities.RestServerR4Helper;
import ca.uhn.fhir.test.utilities.TlsAuthenticationTestHelper;
import ca.uhn.fhir.tls.TlsAuthentication;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public abstract class BaseFhirVersionParameterizedTest {

	@RegisterExtension
	public final RestServerR4Helper myRestServerR4Helper = RestServerR4Helper.newInitialized();
	@RegisterExtension
	public final RestServerDstu3Helper myRestServerDstu3Helper = RestServerDstu3Helper.newInitialized();
	@RegisterExtension
	public TlsAuthenticationTestHelper myTlsAuthenticationTestHelper = new TlsAuthenticationTestHelper();

	protected final FhirContext myR4FhirContext = FhirContext.forR4();
	protected final FhirContext myDstu3FhirContext = FhirContext.forDstu3();

	protected static Stream<Arguments> baseParamsProvider(){
		return Stream.of(
			Arguments.arguments(FhirVersionEnum.R4),
			Arguments.arguments(FhirVersionEnum.DSTU3)
		);
	}

	protected FhirVersionParams getFhirVersionParams(FhirVersionEnum theFhirVersion){
		switch(theFhirVersion){
			case R4:
				return new FhirVersionParams(myRestServerR4Helper, myR4FhirContext);
			case DSTU3:
				return new FhirVersionParams(myRestServerDstu3Helper, myDstu3FhirContext);
			default:
				throw new RuntimeException(Msg.code(2114)+"Unknown FHIR Version param provided: " + theFhirVersion);
		}
	}

	protected TlsAuthentication getTlsAuthentication(){
		return myTlsAuthenticationTestHelper.getTlsAuthentication();
	}

	protected class FhirVersionParams {
		private final BaseRestServerHelper myBaseRestServerHelper;
		private final FhirContext myFhirContext;
		private final FhirVersionEnum myFhirVersion;

		public FhirVersionParams(BaseRestServerHelper theBaseRestServerHelper, FhirContext theFhirContext) {
			myBaseRestServerHelper = theBaseRestServerHelper;
			myFhirContext = theFhirContext;
			myFhirVersion = theFhirContext.getVersion().getVersion();
		}

		public FhirContext getFhirContext() {
			return myFhirContext;
		}

		public FhirVersionEnum getFhirVersion() {
			return myFhirVersion;
		}

		public String getBase(){
			return myBaseRestServerHelper.getBase();
		}

		public String getSecureBase(){
			return myBaseRestServerHelper.getSecureBase();
		}

		public String getPatientEndpoint(){
			return getBase()+"/Patient";
		}

		public String getSecuredPatientEndpoint(){
			return getSecureBase()+"/Patient";
		}

		public IBaseResource parseResource(String json){
			return myFhirContext.newJsonParser().parseResource(json);
		}
	}

}
