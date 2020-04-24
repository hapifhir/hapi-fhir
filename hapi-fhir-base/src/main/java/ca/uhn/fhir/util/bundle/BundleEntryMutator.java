package ca.uhn.fhir.util.bundle;

/*-
 * #%L
 * HAPI FHIR - Core Library
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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

public class BundleEntryMutator {
	private final IBase myEntry;
	private final BaseRuntimeChildDefinition myRequestChildDef;
	private final BaseRuntimeElementCompositeDefinition<?> myRequestChildContentsDef;

	public BundleEntryMutator(IBase theEntry, BaseRuntimeChildDefinition theRequestChildDef, BaseRuntimeElementCompositeDefinition<?> theRequestChildContentsDef) {
		myEntry = theEntry;
		myRequestChildDef = theRequestChildDef;
		myRequestChildContentsDef = theRequestChildContentsDef;
	}

	void setRequestUrl(FhirContext theFhirContext, String theRequestUrl) {
		BaseRuntimeChildDefinition requestUrlChildDef = myRequestChildContentsDef.getChildByName("url");
		IPrimitiveType<?> url = ParametersUtil.createUri(theFhirContext, theRequestUrl);
		for (IBase nextRequest : myRequestChildDef.getAccessor().getValues(myEntry)) {
			requestUrlChildDef.getMutator().addValue(nextRequest, url);
		}
	}
}
