package ca.uhn.fhir.context;

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

import java.util.Map;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseXhtml;

public class RuntimePrimitiveDatatypeXhtmlHl7OrgDefinition  extends BaseRuntimeElementDefinition<IBaseXhtml> {

	public RuntimePrimitiveDatatypeXhtmlHl7OrgDefinition(String theName, Class<? extends IBaseXhtml> theImplementingClass, boolean theStandardType) {
		super(theName, theImplementingClass, theStandardType);
	}

	@Override
	public ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum getChildType() {
		return ChildTypeEnum.PRIMITIVE_XHTML_HL7ORG;
	}

	@Override
	void sealAndInitialize(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		// nothing
	}

}
