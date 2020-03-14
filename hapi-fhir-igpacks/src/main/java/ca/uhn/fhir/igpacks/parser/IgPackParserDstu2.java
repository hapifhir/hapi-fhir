package ca.uhn.fhir.igpacks.parser;

/*-
 * #%L
 * hapi-fhir-igpacks
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Map;

public class IgPackParserDstu2 extends BaseIgPackParser<IValidationSupport> {

	public IgPackParserDstu2(FhirContext theCtx) {
		super(massage(theCtx));
	}

	@Override
	protected IValidationSupport createValidationSupport(Map<IIdType, IBaseResource> theIgResources) {
		return new IgPackValidationSupportDstu2(getCtx(), theIgResources);
	}

	@Override
	protected FhirVersionEnum provideExpectedVersion() {
		return FhirVersionEnum.DSTU2_HL7ORG;
	}

	private static FhirContext massage(FhirContext theCtx) {
		if (theCtx.getVersion().getVersion() == FhirVersionEnum.DSTU2) {
			return FhirContext.forDstu2Hl7Org();
		} else {
			return theCtx;
		}
	}

}
