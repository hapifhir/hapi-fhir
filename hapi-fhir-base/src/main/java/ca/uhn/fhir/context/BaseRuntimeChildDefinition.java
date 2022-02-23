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
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseReference;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

public abstract class BaseRuntimeChildDefinition {

	private BaseRuntimeChildDefinition myReplacedParentDefinition;

	public abstract IAccessor getAccessor();

	public abstract BaseRuntimeElementDefinition<?> getChildByName(String theName);

	public abstract BaseRuntimeElementDefinition<?> getChildElementDefinitionByDatatype(Class<? extends IBase> theType);

	public abstract String getChildNameByDatatype(Class<? extends IBase> theDatatype);

	public abstract String getElementName();

	public String getExtensionUrl() {
		return null;
	}

	public Object getInstanceConstructorArguments() {
		return null;
	}

	public abstract int getMax();

	public abstract int getMin();

	public abstract IMutator getMutator();

	public abstract Set<String> getValidChildNames();

	public abstract boolean isSummary();

	abstract void sealAndInitialize(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions);

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getElementName() + "]";
	}

	public BaseRuntimeChildDefinition getReplacedParentDefinition() {
		return myReplacedParentDefinition;
	}

	public void setReplacedParentDefinition(BaseRuntimeChildDefinition myReplacedParentDefinition) {
		this.myReplacedParentDefinition = myReplacedParentDefinition;
	}

	public interface IAccessor {
		List<IBase> getValues(IBase theTarget);

		default <T extends IBase> Optional<T> getFirstValueOrNull(IBase theTarget) {
			return (Optional<T>) getValues(theTarget).stream().findFirst();
		}
	}

	public interface IMutator {
		void addValue(IBase theTarget, IBase theValue);

		void setValue(IBase theTarget, IBase theValue);
	}

	BaseRuntimeElementDefinition<?> findResourceReferenceDefinition(Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		for (Entry<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> next : theClassToElementDefinitions.entrySet()) {
			if (IBaseReference.class.isAssignableFrom(next.getKey())) {
				return next.getValue();
			}
		}
		
		// Shouldn't happen
		throw new IllegalStateException(Msg.code(1692) + "Unable to find reference type");
	}

	// public String getExtensionUrl() {
	// return null;
	// }
}
