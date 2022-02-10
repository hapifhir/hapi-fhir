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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import org.apache.commons.text.WordUtils;
import org.hl7.fhir.instance.model.api.IBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RuntimeChildUndeclaredExtensionDefinition extends BaseRuntimeChildDefinition {

	private static final String VALUE_REFERENCE = "valueReference";
	private static final String VALUE_RESOURCE = "valueResource";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RuntimeChildUndeclaredExtensionDefinition.class);
	private Map<String, BaseRuntimeElementDefinition<?>> myAttributeNameToDefinition;
	private Map<Class<? extends IBase>, String> myDatatypeToAttributeName;
	private Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> myDatatypeToDefinition;

	public RuntimeChildUndeclaredExtensionDefinition() {
		// nothing
	}

	private void addReferenceBinding(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions, String value) {
		BaseRuntimeElementDefinition<?> def = findResourceReferenceDefinition(theClassToElementDefinitions);

		myAttributeNameToDefinition.put(value, def);
		/*
		 * Resource reference - The correct name is 'valueReference' in DSTU2 and 'valueResource' in DSTU1
		 */
		if (!value.equals(VALUE_RESOURCE)) {
			myDatatypeToAttributeName.put(theContext.getVersion().getResourceReferenceType(), value);
			myDatatypeToDefinition.put(BaseResourceReferenceDt.class, def);
			myDatatypeToDefinition.put(theContext.getVersion().getResourceReferenceType(), def);
		}

	}

	@Override
	public IAccessor getAccessor() {
		return new IAccessor() {
			@Override
			public List<IBase> getValues(IBase theTarget) {
				ExtensionDt target = (ExtensionDt) theTarget;
				if (target.getValue() != null) {
					return Collections.singletonList(target.getValue());
				}
				return new ArrayList<>(target.getUndeclaredExtensions());
			}

		};
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildByName(String theName) {
		return myAttributeNameToDefinition.get(theName);
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildElementDefinitionByDatatype(Class<? extends IBase> theType) {
		return myDatatypeToDefinition.get(theType);
	}

	@Override
	public String getChildNameByDatatype(Class<? extends IBase> theDatatype) {
		return myDatatypeToAttributeName.get(theDatatype);
	}

	@Override
	public String getElementName() {
		return "extension";
	}

	@Override
	public int getMax() {
		return 1;
	}

	@Override
	public int getMin() {
		return 0;
	}

	@Override
	public IMutator getMutator() {
		return new IMutator() {
			@Override
			public void addValue(IBase theTarget, IBase theValue) {
				ExtensionDt target = (ExtensionDt) theTarget;
				target.setValue((IDatatype) theTarget);
			}

			@Override
			public void setValue(IBase theTarget, IBase theValue) {
				ExtensionDt target = (ExtensionDt) theTarget;
				target.setValue((IDatatype) theTarget);
			}
		};
	}

	@Override
	public Set<String> getValidChildNames() {
		return myAttributeNameToDefinition.keySet();
	}

	@Override
	public boolean isSummary() {
		return false;
	}

	@Override
	void sealAndInitialize(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		Map<String, BaseRuntimeElementDefinition<?>> datatypeAttributeNameToDefinition = new HashMap<>();
		myDatatypeToAttributeName = new HashMap<>();
		myDatatypeToDefinition = new HashMap<>();

		for (BaseRuntimeElementDefinition<?> next : theClassToElementDefinitions.values()) {
			if (next instanceof IRuntimeDatatypeDefinition) {

				myDatatypeToDefinition.put(next.getImplementingClass(), next);

				boolean isSpecialization = ((IRuntimeDatatypeDefinition) next).isSpecialization();
				if (isSpecialization) {
					ourLog.trace("Not adding specialization: {}", next.getImplementingClass());
				}


				if (!next.isStandardType()) {
					continue;
				}

				String qualifiedName = next.getImplementingClass().getName();

				/*
				 * We don't want user-defined custom datatypes ending up overriding the built in
				 * types here. It would probably be better for there to be a way for
				 * a datatype to indicate via its annotation that it's a built in
				 * type.
				 */
				if (!qualifiedName.startsWith("ca.uhn.fhir.model")) {
					if (!qualifiedName.startsWith("org.hl7.fhir")) {
						continue;
					}
				}

				String attrName = createExtensionChildName(next);
				if (isSpecialization && datatypeAttributeNameToDefinition.containsKey(attrName)) {
					continue;
				}

				if (datatypeAttributeNameToDefinition.containsKey(attrName)) {
					BaseRuntimeElementDefinition<?> existing = datatypeAttributeNameToDefinition.get(attrName);
					// We do allow built-in standard types to override extension types with the same element name,
					// e.g. how EnumerationType extends CodeType but both serialize to "code". In this case,
					// CodeType should win. If we aren't in a situation like that, there is a problem with the
					// model so we should bail.
					if (!existing.isStandardType()) {
						throw new ConfigurationException(Msg.code(1734) + "More than one child of " + getElementName() + " matches attribute name " + attrName + ". Found [" + existing.getImplementingClass().getName() + "] and [" + next.getImplementingClass().getName() + "]");
					}
				}

				datatypeAttributeNameToDefinition.put(attrName, next);
				datatypeAttributeNameToDefinition.put(attrName.toLowerCase(), next);
				myDatatypeToAttributeName.put(next.getImplementingClass(), attrName);
			}
		}

		myAttributeNameToDefinition = datatypeAttributeNameToDefinition;


		/*
		 * Resource reference - The correct name is 'valueReference' in DSTU2 and 'valueResource' in DSTU1
		 */
		addReferenceBinding(theContext, theClassToElementDefinitions, VALUE_RESOURCE);
		addReferenceBinding(theContext, theClassToElementDefinitions, VALUE_REFERENCE);
	}

	public static String createExtensionChildName(BaseRuntimeElementDefinition<?> next) {
		return "value" + WordUtils.capitalize(next.getName());
	}

}
