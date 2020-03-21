package ca.uhn.fhir.model.dstu;

/*
 * #%L
 * HAPI FHIR Structures - DSTU1 (FHIR v0.80)
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.join;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeDeclaredChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeChildChoiceDefinition;
import ca.uhn.fhir.context.RuntimeChildCompositeDatatypeDefinition;
import ca.uhn.fhir.context.RuntimeChildContainedResources;
import ca.uhn.fhir.context.RuntimeChildDeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimeChildExtension;
import ca.uhn.fhir.context.RuntimeChildPrimitiveDatatypeDefinition;
import ca.uhn.fhir.context.RuntimeChildResourceBlockDefinition;
import ca.uhn.fhir.context.RuntimeChildResourceDefinition;
import ca.uhn.fhir.context.RuntimeChildUndeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimeCompositeDatatypeDefinition;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeDefinition;
import ca.uhn.fhir.context.RuntimeResourceBlockDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.support.IContextValidationSupport;
import ca.uhn.fhir.fhirpath.IFluentPath;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseContainedDt;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.ContainedDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Profile;
import ca.uhn.fhir.model.dstu.resource.Profile.ExtensionDefn;
import ca.uhn.fhir.model.dstu.resource.Profile.Structure;
import ca.uhn.fhir.model.dstu.resource.Profile.StructureElement;
import ca.uhn.fhir.model.dstu.resource.Profile.StructureElementDefinitionType;
import ca.uhn.fhir.model.dstu.valueset.DataTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SlicingRulesEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.Dstu1BundleFactory;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.provider.ServerConformanceProvider;
import ca.uhn.fhir.rest.server.provider.ServerProfileProvider;

public class FhirDstu1 implements IFhirVersion {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirDstu1.class);
	// private Map<RuntimeChildDeclaredExtensionDefinition, String> myExtensionDefToCode = new
	// HashMap<RuntimeChildDeclaredExtensionDefinition, String>();
	private String myId;

	@Override
	public ServerConformanceProvider createServerConformanceProvider(RestfulServer theServer) {
		return new ServerConformanceProvider(theServer);
	}

	@Override
	public IResourceProvider createServerProfilesProvider(RestfulServer theRestfulServer) {
		return new ServerProfileProvider(theRestfulServer);
	}

	private void fillBasics(StructureElement theElement, BaseRuntimeElementDefinition<?> def, LinkedList<String> path, BaseRuntimeDeclaredChildDefinition theChild) {
		if (path.isEmpty()) {
			path.add(def.getName());
			theElement.setName(def.getName());
		} else {
			path.add(WordUtils.uncapitalize(theChild.getElementName()));
			theElement.setName(theChild.getElementName());
		}
		theElement.setPath(StringUtils.join(path, '.'));
	}

	private void fillExtensions(Structure theStruct, LinkedList<String> path, List<RuntimeChildDeclaredExtensionDefinition> extList, String elementName, boolean theIsModifier) {
		if (extList.size() > 0) {
			StructureElement extSlice = theStruct.addElement();
			extSlice.setName(elementName);
			extSlice.setPath(join(path, '.') + '.' + elementName);
			extSlice.getSlicing().getDiscriminator().setValue("url");
			extSlice.getSlicing().setOrdered(false);
			extSlice.getSlicing().setRules(SlicingRulesEnum.OPEN);
			extSlice.getDefinition().addType().setCode(DataTypeEnum.EXTENSION);

			for (RuntimeChildDeclaredExtensionDefinition nextExt : extList) {
				StructureElement nextProfileExt = theStruct.addElement();
				nextProfileExt.getDefinition().setIsModifier(theIsModifier);
				nextProfileExt.setName(extSlice.getName());
				nextProfileExt.setPath(extSlice.getPath());
				fillMinAndMaxAndDefinitions(nextExt, nextProfileExt);
				StructureElementDefinitionType type = nextProfileExt.getDefinition().addType();
				type.setCode(DataTypeEnum.EXTENSION);
				if (nextExt.isDefinedLocally()) {
					type.setProfile(nextExt.getExtensionUrl().substring(nextExt.getExtensionUrl().indexOf('#')));
				} else {
					type.setProfile(nextExt.getExtensionUrl());
				}
			}
		} else {
			StructureElement extSlice = theStruct.addElement();
			extSlice.setName(elementName);
			extSlice.setPath(join(path, '.') + '.' + elementName);
			extSlice.getDefinition().setIsModifier(theIsModifier);
			extSlice.getDefinition().addType().setCode(DataTypeEnum.EXTENSION);
			extSlice.getDefinition().setMin(0);
			extSlice.getDefinition().setMax("*");
		}
	}

	private void fillMinAndMaxAndDefinitions(BaseRuntimeDeclaredChildDefinition child, StructureElement elem) {
		elem.getDefinition().setMin(child.getMin());
		if (child.getMax() == Child.MAX_UNLIMITED) {
			elem.getDefinition().setMax("*");
		} else {
			elem.getDefinition().setMax(Integer.toString(child.getMax()));
		}

		if (isNotBlank(child.getShortDefinition())) {
			elem.getDefinition().getShort().setValue(child.getShortDefinition());
		}
		if (isNotBlank(child.getFormalDefinition())) {
			elem.getDefinition().getFormal().setValue(child.getFormalDefinition());
		}
	}

	private void fillName(StructureElement elem, BaseRuntimeElementDefinition<?> nextDef, String theServerBase) {
		assert nextDef != null;

		StructureElementDefinitionType type = elem.getDefinition().addType();
		String name = nextDef.getName();
		DataTypeEnum fromCodeString = DataTypeEnum.VALUESET_BINDER.fromCodeString(name);
		if (fromCodeString == null) {
			throw new ConfigurationException("Unknown type: " + name);
		}
		type.setCode(fromCodeString);
	}

	private void fillProfile(Structure theStruct, StructureElement theElement, BaseRuntimeElementDefinition<?> def, LinkedList<String> path, BaseRuntimeDeclaredChildDefinition theChild, String theServerBase) {

		fillBasics(theElement, def, path, theChild);

		String expectedPath = StringUtils.join(path, '.');

		ourLog.debug("Filling profile for: {} - Path: {}", expectedPath);
		String name = def.getName();
		if (!expectedPath.equals(name)) {
			path.pollLast();
			theElement.getDefinition().getNameReference().setValue(def.getName());
			return;
		}

		fillExtensions(theStruct, path, def.getExtensionsNonModifier(), "extension", false);
		fillExtensions(theStruct, path, def.getExtensionsModifier(), "modifierExtension", true);

		if (def.getChildType() == ChildTypeEnum.RESOURCE) {
			StructureElement narrative = theStruct.addElement();
			narrative.setName("text");
			narrative.setPath(join(path, '.') + ".text");
			narrative.getDefinition().addType().setCode(DataTypeEnum.NARRATIVE);
			narrative.getDefinition().setIsModifier(false);
			narrative.getDefinition().setMin(0);
			narrative.getDefinition().setMax("1");

			StructureElement contained = theStruct.addElement();
			contained.setName("contained");
			contained.setPath(join(path, '.') + ".contained");
			contained.getDefinition().addType().getCode().setValue("Resource");
			contained.getDefinition().setIsModifier(false);
			contained.getDefinition().setMin(0);
			contained.getDefinition().setMax("1");
		}

		if (def instanceof BaseRuntimeElementCompositeDefinition) {
			BaseRuntimeElementCompositeDefinition<?> cdef = ((BaseRuntimeElementCompositeDefinition<?>) def);
			for (BaseRuntimeChildDefinition nextChild : cdef.getChildren()) {
				if (nextChild instanceof RuntimeChildUndeclaredExtensionDefinition) {
					continue;
				}
				if (nextChild instanceof RuntimeChildExtension) {
					continue;
				} 

				BaseRuntimeDeclaredChildDefinition child = (BaseRuntimeDeclaredChildDefinition) nextChild;
				StructureElement elem = theStruct.addElement();
				fillMinAndMaxAndDefinitions(child, elem);

				if (child instanceof RuntimeChildResourceBlockDefinition) {
					RuntimeResourceBlockDefinition nextDef = (RuntimeResourceBlockDefinition) child.getSingleChildOrThrow();
					fillProfile(theStruct, elem, nextDef, path, child, theServerBase);
				} else if (child instanceof RuntimeChildContainedResources) {
					// ignore
				} else if (child instanceof RuntimeChildDeclaredExtensionDefinition) {
					throw new IllegalStateException("Unexpected child type: " + child.getClass().getCanonicalName());
				} else if (child instanceof RuntimeChildCompositeDatatypeDefinition || child instanceof RuntimeChildPrimitiveDatatypeDefinition || child instanceof RuntimeChildChoiceDefinition || child instanceof RuntimeChildResourceDefinition) {
					Iterator<String> childNamesIter = child.getValidChildNames().iterator();
					String nextName = childNamesIter.next();
					BaseRuntimeElementDefinition<?> nextDef = child.getChildByName(nextName);
					fillBasics(elem, nextDef, path, child);
					fillName(elem, nextDef, theServerBase);
					while (childNamesIter.hasNext()) {
						nextDef = child.getChildByName(childNamesIter.next());
						fillName(elem, nextDef, theServerBase);
					}
					path.pollLast();
				} else {
					throw new IllegalStateException("Unexpected child type: " + child.getClass().getCanonicalName());
				}

			}
		} else {
			throw new IllegalStateException("Unexpected child type: " + def.getClass().getCanonicalName());
		}

		path.pollLast();
	}

	@Override
	public IResource generateProfile(RuntimeResourceDefinition theRuntimeResourceDefinition, String theServerBase) {
		Profile retVal = new Profile();

		RuntimeResourceDefinition def = theRuntimeResourceDefinition;

		myId = def.getId();
		if (StringUtils.isBlank(myId)) {
			myId = theRuntimeResourceDefinition.getName().toLowerCase();
		}

		retVal.setId(new IdDt(myId));

		// Scan for extensions
		scanForExtensions(retVal, def, new HashMap<RuntimeChildDeclaredExtensionDefinition, String>());
		Collections.sort(retVal.getExtensionDefn(), new Comparator<ExtensionDefn>() {
			@Override
			public int compare(ExtensionDefn theO1, ExtensionDefn theO2) {
				return theO1.getCode().compareTo(theO2.getCode());
			}
		});

		// Scan for children
		retVal.setName(def.getName());
		Structure struct = retVal.addStructure();
		LinkedList<String> path = new LinkedList<String>();

		StructureElement element = struct.addElement();
		element.getDefinition().setMin(1);
		element.getDefinition().setMax("1");
		fillProfile(struct, element, def, path, null, theServerBase);

		retVal.getStructure().get(0).getElement().get(0).getDefinition().addType().getCode().setValue("Resource");

		return retVal;
	}

	@Override
	public Class<? extends BaseContainedDt> getContainedType() {
		return ContainedDt.class;
	}

	@Override
	public InputStream getFhirVersionPropertiesFile() {
		InputStream str = FhirDstu1.class.getResourceAsStream("/ca/uhn/fhir/model/dstu/fhirversion.properties");
		if (str == null) {
			str = FhirDstu1.class.getResourceAsStream("ca/uhn/fhir/model/dstu/fhirversion.properties");
		}
		if (str == null) {
			throw new ConfigurationException("Can not find model property file on classpath: " + "/ca/uhn/fhir/model/dstu/model.properties");
		}
		return str;
	}

	@Override
	public IPrimitiveType<Date> getLastUpdated(IBaseResource theResource) {
		return ResourceMetadataKeyEnum.UPDATED.get((IResource) theResource);
	}

	@Override
	public String getPathToSchemaDefinitions() {
		return "/ca/uhn/fhir/model/dstu/schema";
	}

	@Override
	public Class<? extends BaseResourceReferenceDt> getResourceReferenceType() {
		return ResourceReferenceDt.class;
	}

	@Override
	public FhirVersionEnum getVersion() {
		return FhirVersionEnum.DSTU1;
	}

	@Override
	public IVersionSpecificBundleFactory newBundleFactory(FhirContext theContext) {
		return new Dstu1BundleFactory(theContext);
	}

	@Override
	public BaseCodingDt newCodingDt() {
		return new CodingDt();
	}

	private Map<RuntimeChildDeclaredExtensionDefinition, String> scanForExtensions(Profile theProfile, BaseRuntimeElementDefinition<?> def, Map<RuntimeChildDeclaredExtensionDefinition, String> theExtensionDefToCode) {
		BaseRuntimeElementCompositeDefinition<?> cdef = ((BaseRuntimeElementCompositeDefinition<?>) def);

		for (RuntimeChildDeclaredExtensionDefinition nextChild : cdef.getExtensions()) {
			if (theExtensionDefToCode.containsKey(nextChild)) {
				continue;
			}

			if (nextChild.isDefinedLocally() == false) {
				continue;
			}

			ExtensionDefn defn = theProfile.addExtensionDefn();
			String code = null;
			if (nextChild.getExtensionUrl().contains("#") && !nextChild.getExtensionUrl().endsWith("#")) {
				code = nextChild.getExtensionUrl().substring(nextChild.getExtensionUrl().indexOf('#') + 1);
			} else {
				throw new ConfigurationException("Locally defined extension has no '#[code]' part in extension URL: " + nextChild.getExtensionUrl());
			}

			defn.setCode(code);
			if (theExtensionDefToCode.values().contains(code)) {
				throw new IllegalStateException("Duplicate extension code: " + code);
			}
			theExtensionDefToCode.put(nextChild, code);

			if (nextChild.getChildType() != null && IPrimitiveDatatype.class.isAssignableFrom(nextChild.getChildType())) {
				RuntimePrimitiveDatatypeDefinition pdef = (RuntimePrimitiveDatatypeDefinition) nextChild.getSingleChildOrThrow();
				defn.getDefinition().addType().setCode(DataTypeEnum.VALUESET_BINDER.fromCodeString(pdef.getName()));
			} else if (nextChild.getChildType() != null && ICompositeDatatype.class.isAssignableFrom(nextChild.getChildType())) {
				RuntimeCompositeDatatypeDefinition pdef = (RuntimeCompositeDatatypeDefinition) nextChild.getSingleChildOrThrow();
				defn.getDefinition().addType().setCode(DataTypeEnum.VALUESET_BINDER.fromCodeString(pdef.getName()));
			} else {
				BaseRuntimeElementDefinition<?> singleChildOrThrow = nextChild.getSingleChildOrThrow();
				if (singleChildOrThrow instanceof RuntimeResourceBlockDefinition) {
					RuntimeResourceBlockDefinition pdef = (RuntimeResourceBlockDefinition) singleChildOrThrow;
					scanForExtensions(theProfile, pdef, theExtensionDefToCode);
	
					for (RuntimeChildDeclaredExtensionDefinition nextChildExt : pdef.getExtensions()) {
						StructureElementDefinitionType type = defn.getDefinition().addType();
						type.setCode(DataTypeEnum.EXTENSION);
						type.setProfile("#" + theExtensionDefToCode.get(nextChildExt));
					}
				}
			}
		}

		return theExtensionDefToCode;
	}

	@Override
	public IIdType newIdType() {
		return new IdDt();
	}

	@Override
	public IContextValidationSupport<?, ?, ?, ?, ?, ?> createValidationSupport() {
		throw new UnsupportedOperationException("Validation support is not supported in DSTU1 contexts");
	}

	@Override
	public IFluentPath createFluentPathExecutor(FhirContext theFhirContext) {
		throw new UnsupportedOperationException("FluentPath is not supported in DSTU1 contexts");
	}

}
