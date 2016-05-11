package ca.uhn.fhir.context;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.lang.reflect.Field;

import org.hl7.fhir.instance.model.api.IBase;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;

public class RuntimeChildPrimitiveDatatypeDefinition extends BaseRuntimeChildDatatypeDefinition {
//	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RuntimeChildPrimitiveDatatypeDefinition.class);
//	private IMutator myReferenceMutator;
	
	public RuntimeChildPrimitiveDatatypeDefinition(Field theField, String theElementName, Description theDescriptionAnnotation, Child theChildAnnotation,  Class<? extends IBase> theDatatype) {
		super(theField, theElementName, theChildAnnotation, theDescriptionAnnotation, theDatatype);
	}

//	@Override
//	void sealAndInitialize(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
//		super.sealAndInitialize(theContext, theClassToElementDefinitions);
//		
//		if (theContext.getVersion().getVersion().equals(FhirVersionEnum.DSTU2_HL7ORG)) {
//			if (IReference.class.isAssignableFrom(getDatatype())) {
//				String fieldName = getField().getName() + "Target";
//				try {
//					Field targetField = getField().getDeclaringClass().getField(fieldName);
//					if (List.class.isAssignableFrom(targetField.getType())) {
//						myReferenceMutator = new FieldListMutator();
//					} else if (IBaseResource.class.isAssignableFrom(targetField.getType())) {
//						myReferenceMutator = new FieldPlainMutator();
//					}
//				} catch (Exception e) {
//					ourLog.debug("Unable to find target field named {}", fieldName);
//				}
//			}
//		} else {
//			if (BaseResourceReferenceDt.class.isAssignableFrom(getDatatype())) {
//				myReferenceMutator = new IMutator() {
//					@Override
//					public void addValue(Object theTarget, IBase theValue) {
//						BaseResourceReferenceDt dt = (BaseResourceReferenceDt)theTarget;
//						dt.setResource((IBaseResource) theValue);
//					}};
//			}
//		}
//		
//	}
//
//	public IMutator getReferenceMutator() {
//		return myReferenceMutator;
//	}


}
