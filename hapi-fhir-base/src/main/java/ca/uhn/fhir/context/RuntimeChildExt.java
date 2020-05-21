package ca.uhn.fhir.context;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RuntimeChildExt extends BaseRuntimeChildDefinition {

	private Map<String, BaseRuntimeElementDefinition<?>> myNameToChild;
	private Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> myDatatypeToChild;
	private Map<Class<? extends IBase>, String> myDatatypeToChildName;

	@Override
	public IAccessor getAccessor() {
		return new IAccessor() {
			@SuppressWarnings({"unchecked", "rawtypes"})
			@Override
			public List<IBase> getValues(IBase theTarget) {
				List extension = ((IBaseHasExtensions) theTarget).getExtension();
				return Collections.unmodifiableList(extension);
			}
		};
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildByName(String theName) {
		return myNameToChild.get(theName);
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildElementDefinitionByDatatype(Class<? extends IBase> theType) {
		return myDatatypeToChild.get(theType);
	}

	@Override
	public String getChildNameByDatatype(Class<? extends IBase> theDatatype) {
		return myDatatypeToChildName.get(theDatatype);
	}

	@Override
	public String getElementName() {
		return "extension";
	}

	@Override
	public int getMax() {
		return -1;
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
				List extensions = ((IBaseHasExtensions) theTarget).getExtension();
				IBaseExtension<?, ?> value = (IBaseExtension<?, ?>) theValue;
				extensions.add(value);
			}

			@Override
			public void setValue(IBase theTarget, IBase theValue) {
				List extensions = ((IBaseHasExtensions) theTarget).getExtension();
				extensions.clear();
				if (theValue != null) {
					IBaseExtension<?, ?> value = (IBaseExtension<?, ?>) theValue;
					extensions.add(value);
				}
			}
		};
	}

	@Override
	public Set<String> getValidChildNames() {
		return Sets.newHashSet("extension");
	}

	@Override
	public boolean isSummary() {
		return false;
	}

	@Override
	void sealAndInitialize(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		myNameToChild = new HashMap<>();
		myDatatypeToChild = new HashMap<>();
		myDatatypeToChildName = new HashMap<>();

		for (BaseRuntimeElementDefinition<?> next : theClassToElementDefinitions.values()) {
			if (next.getName().equals("Extension")) {
				myNameToChild.put("extension", next);
				myDatatypeToChild.put(next.getImplementingClass(), next);
				myDatatypeToChildName.put(next.getImplementingClass(), "extension");
			}
		}

		Validate.isTrue(!myNameToChild.isEmpty());
	}
}
