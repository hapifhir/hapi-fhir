package ca.uhn.fhir.tinder.ts;

// Created by Claude Opus 4.8

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildExtension;
import ca.uhn.fhir.context.RuntimeChildPrimitiveBoundCodeDatatypeDefinition;
import ca.uhn.fhir.context.RuntimeChildPrimitiveEnumerationDatatypeDefinition;
import ca.uhn.fhir.context.RuntimeChildResourceBlockDefinition;
import ca.uhn.fhir.context.RuntimeChildUndeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Walks HAPI's runtime model ({@link RuntimeResourceDefinition} and friends) for a given
 * {@link FhirContext} and produces a version-agnostic {@link TsModel} describing the equivalent
 * TypeScript interfaces and enumerations.
 *
 * <p>
 * Because all traversal goes through the runtime model rather than version-specific spreadsheets, the
 * same extractor works for DSTU2 through R5.
 * </p>
 */
public class TypescriptModelExtractor {

	private static final Logger ourLog = LoggerFactory.getLogger(TypescriptModelExtractor.class);

	private static final Set<String> NUMERIC_PRIMITIVES =
			Set.of("integer", "decimal", "unsignedint", "positiveint", "integer64");

	private final FhirContext myContext;

	/**
	 * Maps each composite implementing class to the TypeScript interface name it was assigned. Keyed by
	 * class rather than name so that recursive backbone elements (e.g. {@code Questionnaire.item.item})
	 * resolve to a single interface instead of recursing forever.
	 */
	private final Map<Class<?>, String> myInterfaceNamesByClass = new HashMap<>();

	public TypescriptModelExtractor(FhirContext theContext) {
		myContext = theContext;
	}

	/**
	 * Extract every resource and complex datatype known to the configured {@link FhirContext}.
	 */
	public TsModel extract() {
		TsModel model = new TsModel(myContext.getVersion().getVersion().name());

		List<String> resourceNames = new ArrayList<>(myContext.getResourceTypes());
		resourceNames.sort(String.CASE_INSENSITIVE_ORDER);
		for (String nextResourceName : resourceNames) {
			RuntimeResourceDefinition def = myContext.getResourceDefinition(nextResourceName);
			processComposite(def, def.getName(), true, model);
		}

		// Complex datatypes are largely reached transitively, but enumerate them explicitly so that
		// unreferenced datatypes are still emitted.
		for (BaseRuntimeElementDefinition<?> nextDef : myContext.getElementDefinitions()) {
			if (nextDef.getChildType() == ChildTypeEnum.COMPOSITE_DATATYPE
					&& nextDef instanceof BaseRuntimeElementCompositeDefinition) {
				processComposite((BaseRuntimeElementCompositeDefinition<?>) nextDef, nextDef.getName(), false, model);
			}
		}

		return model;
	}

	/**
	 * Builds the interface for a composite definition if it has not already been built, and returns the
	 * canonical interface name to use when referencing it.
	 *
	 * @param theProposedName the name to assign if this is the first time the definition is seen
	 * @return the interface name actually assigned to this definition's implementing class
	 */
	private String processComposite(
			BaseRuntimeElementCompositeDefinition<?> theDefinition,
			String theProposedName,
			boolean theIsResource,
			TsModel theModel) {
		Class<?> implClass = theDefinition.getImplementingClass();
		String existing = myInterfaceNamesByClass.get(implClass);
		if (existing != null) {
			return existing;
		}

		// Register the name before recursing so that self-referential structures terminate.
		myInterfaceNamesByClass.put(implClass, theProposedName);

		TsInterface iface = new TsInterface(theProposedName);
		theModel.addInterface(iface);

		if (theIsResource) {
			iface.addProperty(new TsProperty("resourceType", "string", TsTypeKind.PRIMITIVE, false, false));
		}

		for (BaseRuntimeChildDefinition nextChild : theDefinition.getChildren()) {
			handleChild(nextChild, theProposedName, iface, theModel);
		}

		return theProposedName;
	}

	private void handleChild(
			BaseRuntimeChildDefinition theChild, String theOwnerName, TsInterface theInterface, TsModel theModel) {

		boolean array = theChild.isMultipleCardinality();
		boolean optional = theChild.getMin() == 0;

		// Extensions are open-ended, so they are modelled via the Extension datatype rather than by
		// recursing (their internal child naming does not round-trip through getChildByName).
		if (theChild instanceof RuntimeChildExtension
				|| theChild instanceof RuntimeChildUndeclaredExtensionDefinition) {
			theInterface.addProperty(
					new TsProperty(theChild.getElementName(), "Extension", TsTypeKind.INTERFACE, array, optional));
			return;
		}

		// Backbone (nested) elements become their own interface named <Owner><Element>.
		if (theChild instanceof RuntimeChildResourceBlockDefinition) {
			String elementName = theChild.getElementName();
			String blockName = theOwnerName + StringUtils.capitalize(stripChoiceSuffix(elementName));
			BaseRuntimeElementDefinition<?> blockDef = theChild.getChildByName(elementName);
			if (blockDef instanceof BaseRuntimeElementCompositeDefinition) {
				String resolvedName = processComposite(
						(BaseRuntimeElementCompositeDefinition<?>) blockDef, blockName, false, theModel);
				theInterface.addProperty(
						new TsProperty(elementName, resolvedName, TsTypeKind.INTERFACE, array, optional));
			}
			return;
		}

		// Bound codes (primitive) become a string-literal union alias. Composite bound types (e.g. a
		// bound CodeableConcept) are intentionally left to the generic path so they keep their structure.
		Class<? extends Enum<?>> boundEnumType = getBoundEnumType(theChild);
		if (boundEnumType != null) {
			String enumName = registerEnum(boundEnumType, theModel);
			if (enumName != null) {
				theInterface.addProperty(
						new TsProperty(theChild.getElementName(), enumName, TsTypeKind.ENUM, array, optional));
			} else {
				theInterface.addProperty(
						new TsProperty(theChild.getElementName(), "string", TsTypeKind.PRIMITIVE, array, optional));
			}
			return;
		}

		// Everything else (primitives, composite datatypes and choice[x] elements) is resolved through
		// the set of valid child names. A choice element exposes more than one name.
		Set<String> validNames = new TreeSet<>(theChild.getValidChildNames());
		boolean isChoice = validNames.size() > 1;
		for (String nextName : validNames) {
			BaseRuntimeElementDefinition<?> elementDef = theChild.getChildByName(nextName);
			if (elementDef == null) {
				continue;
			}
			ResolvedType resolved = resolveType(elementDef, theModel);
			if (resolved == null) {
				continue;
			}
			theInterface.addProperty(
					new TsProperty(nextName, resolved.typeName, resolved.kind, array, optional || isChoice));
		}
	}

	private ResolvedType resolveType(BaseRuntimeElementDefinition<?> theDefinition, TsModel theModel) {
		switch (theDefinition.getChildType()) {
			case PRIMITIVE_DATATYPE:
			case ID_DATATYPE:
			case PRIMITIVE_XHTML:
			case PRIMITIVE_XHTML_HL7ORG:
				return new ResolvedType(mapPrimitive(theDefinition.getName()), TsTypeKind.PRIMITIVE);
			case COMPOSITE_DATATYPE:
				if (theDefinition instanceof BaseRuntimeElementCompositeDefinition) {
					String resolvedName = processComposite(
							(BaseRuntimeElementCompositeDefinition<?>) theDefinition,
							theDefinition.getName(),
							false,
							theModel);
					return new ResolvedType(resolvedName, TsTypeKind.INTERFACE);
				}
				return new ResolvedType(theDefinition.getName(), TsTypeKind.INTERFACE);
			case RESOURCE:
				return new ResolvedType("any", TsTypeKind.PRIMITIVE);
			default:
				// Contained resources, extensions, etc. are not modelled as concrete properties here.
				return null;
		}
	}

	private static String mapPrimitive(String theFhirTypeName) {
		String name = theFhirTypeName.toLowerCase();
		if (NUMERIC_PRIMITIVES.contains(name)) {
			return "number";
		}
		if (name.equals("boolean")) {
			return "boolean";
		}
		return "string";
	}

	/**
	 * Returns the bound Java enum type for a primitive bound-code child (either the HL7.org-style
	 * enumeration child or the DSTU2-style bound-code child), or {@code null} if the child is not a
	 * primitive bound code.
	 */
	private static Class<? extends Enum<?>> getBoundEnumType(BaseRuntimeChildDefinition theChild) {
		if (theChild instanceof RuntimeChildPrimitiveEnumerationDatatypeDefinition) {
			return ((RuntimeChildPrimitiveEnumerationDatatypeDefinition) theChild).getBoundEnumType();
		}
		if (theChild instanceof RuntimeChildPrimitiveBoundCodeDatatypeDefinition) {
			return ((RuntimeChildPrimitiveBoundCodeDatatypeDefinition) theChild).getBoundEnumType();
		}
		return null;
	}

	/**
	 * Builds (or reuses) a {@link TsEnum} for a bound-code element by reflecting the code values out of
	 * the bound Java enum. Returns the enum's TypeScript name, or {@code null} if the codes could not be
	 * determined (in which case the caller falls back to a plain string).
	 */
	private String registerEnum(Class<? extends Enum<?>> theEnumType, TsModel theModel) {
		Class<? extends Enum<?>> enumType = theEnumType;
		if (enumType == null) {
			return null;
		}

		String enumName = StringUtils.removeEnd(enumType.getSimpleName(), "Enum");
		if (theModel.hasEnum(enumName)) {
			return enumName;
		}

		List<String> codes = extractCodes(enumType);
		if (codes.isEmpty()) {
			return null;
		}

		theModel.addEnum(new TsEnum(enumName, codes));
		return enumName;
	}

	private static List<String> extractCodes(Class<? extends Enum<?>> theEnumType) {
		List<String> codes = new ArrayList<>();
		Method codeMethod = findCodeMethod(theEnumType);
		if (codeMethod == null) {
			return codes;
		}
		for (Object constant : theEnumType.getEnumConstants()) {
			try {
				Object value = codeMethod.invoke(constant);
				if (value instanceof String) {
					String code = (String) value;
					if (StringUtils.isNotBlank(code) && !code.equals("?") && !codes.contains(code)) {
						codes.add(code);
					}
				}
			} catch (Exception e) {
				ourLog.debug("Failed to read code from {}: {}", theEnumType.getName(), e.toString());
			}
		}
		return codes;
	}

	private static Method findCodeMethod(Class<?> theEnumType) {
		for (String candidate : new String[] {"toCode", "getCode"}) {
			try {
				return theEnumType.getMethod(candidate);
			} catch (NoSuchMethodException e) {
				// try the next candidate
			}
		}
		return null;
	}

	private static String stripChoiceSuffix(String theElementName) {
		return StringUtils.removeEnd(theElementName, "[x]");
	}

	private static final class ResolvedType {
		private final String typeName;
		private final TsTypeKind kind;

		private ResolvedType(String theTypeName, TsTypeKind theKind) {
			typeName = theTypeName;
			kind = theKind;
		}
	}
}
