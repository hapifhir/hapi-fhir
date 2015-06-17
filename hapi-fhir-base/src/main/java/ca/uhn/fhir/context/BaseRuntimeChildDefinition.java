package ca.uhn.fhir.context;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBase;

public abstract class BaseRuntimeChildDefinition {

	public abstract IAccessor getAccessor();

	@Override
	public String toString() {
		return getClass().getSimpleName()+"[" + getElementName() + "]";
	}

	public abstract BaseRuntimeElementDefinition<?> getChildByName(String theName);

	public abstract BaseRuntimeElementDefinition<?> getChildElementDefinitionByDatatype(Class<? extends IBase> theType);

	public abstract String getChildNameByDatatype(Class<? extends IBase> theDatatype);

	public abstract IMutator getMutator();

	public abstract Set<String> getValidChildNames();

	abstract void sealAndInitialize(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions);

	public interface IAccessor {
		List<IBase> getValues(Object theTarget);
	}

	public abstract String getElementName();
	
	public abstract int getMax();
	
	public abstract int getMin();
	
	public interface IMutator {
		void setValue(Object theTarget, IBase theValue);

		void addValue(Object theTarget, IBase theValue);
	}

	public String getExtensionUrl() {
		return null;
	}

	public Object getInstanceConstructorArguments() {
		return null;
	}

//	public String getExtensionUrl() {
//		return null;
//	}
}
