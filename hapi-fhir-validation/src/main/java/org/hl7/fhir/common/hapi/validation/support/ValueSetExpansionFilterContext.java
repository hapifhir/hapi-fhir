package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.util.FhirVersionIndependentConcept;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.Enumerations.FilterOperator;
import org.hl7.fhir.r5.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Class to apply ValueSet filters during in-memory expansion.
 * Works on 'code', 'concept' and 'display' property types, plus the hierarchical 'child' and 'parent'
 * properties, the standard boolean 'inactive' and 'notSelectable' properties, and the standard date-valued
 * 'deprecated', 'deprecationDate' and 'retirementDate' properties (each with the {@code exists} operator).
 *
 * <p>The concept hierarchy used by the structural operators is built both from nested
 * {@code CodeSystem.concept} arrays and from a FLAT representation where each concept carries a
 * {@code parent} (or {@code child}) concept-property. These standard properties are matched by their
 * reserved code name; a property the CodeSystem declares with a non-canonical URI is treated as having a
 * different meaning and is not used.
 *
 * Supports: equal | is-a | descendent-of | is-not-a | regex | in | not-in | generalizes | child-of | descendent-leaf | exists
 */
public class ValueSetExpansionFilterContext {
	/** Base of the canonical FHIR concept-properties system; the individual property URIs append the code. */
	private static final String CONCEPT_PROPERTIES_SYSTEM = "http://hl7.org/fhir/concept-properties#";

	private static final Logger ourLog = LoggerFactory.getLogger(ValueSetExpansionFilterContext.class);

	private final Map<String, Map<String, String>> propertyIndex = new HashMap<>();

	private final Map<String, Set<String>> conceptCodeTree = new HashMap<>();
	// Concepts carrying a boolean/date standard property, for membership 'exists' checks (see kinds below).
	private final Map<StandardConceptProperty, Set<String>> conceptsByStandardProperty =
			new EnumMap<>(StandardConceptProperty.class);
	// Standard properties the CodeSystem declares with the canonical concept-properties URI (honored as-is).
	private final Set<StandardConceptProperty> canonicalUriDeclared = EnumSet.noneOf(StandardConceptProperty.class);
	// A standard property is "conflicting" when the CodeSystem declares it (by its reserved code) but with a
	// 'uri' that differs from the canonical concept-properties URI — it then means something non-standard, so
	// we cannot evaluate it. Maps such a property to that declared URI. A missing declaration, or one without
	// a uri, is NOT conflicting: the property is matched by its reserved code name instead.
	private final Map<StandardConceptProperty, String> conflictingPropertyUris =
			new EnumMap<>(StandardConceptProperty.class);
	// Standard properties used by one of the filters (drives the "matched by code name" info log).
	private final Set<StandardConceptProperty> propertiesUsedInFilters = EnumSet.noneOf(StandardConceptProperty.class);
	// Standard properties for which the "matched by code name (not declared)" info log was already emitted.
	private final Set<StandardConceptProperty> loggedNameMatch = EnumSet.noneOf(StandardConceptProperty.class);
	private final Set<String> allCodes = new HashSet<>();
	private final Set<String> allCodesLower = new HashSet<>();
	private final Set<String> allChildCodes = new HashSet<>();
	private final Set<String> allChildCodesLower = new HashSet<>();
	private final Map<String, Set<String>> inSetsMap = new HashMap<>();
	private final Map<String, Pattern> regexCache = new HashMap<>();
	private final CodeSystem codeSystem;
	private final List<ValueSet.ConceptSetFilterComponent> filters;
	private boolean hasIndexRun = false;

	public ValueSetExpansionFilterContext(CodeSystem codeSystem, List<ValueSet.ConceptSetFilterComponent> filters) {
		this.codeSystem = codeSystem;
		this.filters = filters;
	}

	/**
	 * @return {@code true} if the concept is filtered OUT (fails at least one filter), {@code false} if it
	 *     passes all filters.
	 * @throws UnsupportedFilterException if a filter uses a property/operator combination the in-memory
	 *     expansion cannot evaluate. Callers should surface this as an expansion error or delegate to another
	 *     terminology service (this exception is unchecked to keep the public API backwards-compatible).
	 */
	public boolean isFiltered(FhirVersionIndependentConcept concept) {
		if (filters == null || filters.isEmpty()) {
			return false;
		}

		// buildChildrenMap() once in ctor or lazily here
		for (ValueSet.ConceptSetFilterComponent filter : filters) {
			if (!passesFilter(filter, concept)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @return {@code true} if the concept passes the given filter, {@code false} otherwise.
	 * @throws UnsupportedFilterException if the filter uses a property/operator combination the in-memory
	 *     expansion cannot evaluate (see {@link #isFiltered}).
	 */
	public boolean passesFilter(ValueSet.ConceptSetFilterComponent filter, FhirVersionIndependentConcept concept) {
		if (filter.hasOp()) {
			// Lazy load the index, if there are any filters to process.
			buildIndexes();

			// The 'property' element is required by the FHIR spec, but we default to "concept" (the code)
			// when it's missing, for backwards-compatibility with legacy HAPI clients.
			String theFilterProperty =
					filter.hasProperty() ? filter.getProperty().toLowerCase(Locale.ROOT) : "concept";
			boolean onCode = theFilterProperty.equals("concept") || theFilterProperty.equals("code");
			boolean onDisplay = theFilterProperty.equals("display");

			/*
			 * Standard FHIR concept-properties, all evaluated with the 'exists' operator:
			 *  - 'child' / 'parent' (hierarchical: does the concept have children / a parent),
			 *  - the boolean 'inactive' / 'notSelectable' (flagged only when the value is 'true'),
			 *  - the date-valued 'deprecated' / 'deprecationDate' / 'retirementDate' (flagged when present).
			 * They are identified by their reserved code name; if the CodeSystem declares one with a
			 * non-canonical URI its meaning is unknown and the filter fails.
			 */
			StandardConceptProperty standardProperty = StandardConceptProperty.forFilterProperty(theFilterProperty);
			if (standardProperty != null) {
				String conflictingUri = conflictingPropertyUris.get(standardProperty);
				if (conflictingUri != null) {
					throw new UnsupportedFilterException(Msg.code(3005)
							+ "In-memory ValueSet expansion cannot evaluate filter on property '"
							+ filter.getProperty() + "': the CodeSystem declares it with URI '" + conflictingUri
							+ "' rather than the standard '" + standardProperty.canonicalUri()
							+ "', so its meaning is unknown");
				}
				if (filter.getOp() == FilterOperator.EXISTS) {
					boolean wantExists = parseRequiredBoolean(filter);
					return wantExists == conceptHasStandardProperty(standardProperty, concept.getCode());
				}

				// These standard properties only support the 'exists' operator in-memory.
				throw unsupportedFilter(filter);
			}

			/*
			 * Only code/display (and the standard properties handled above) are supported by the in-memory
			 * validation support. Custom concept properties are valid per the FHIR spec but cannot be
			 * evaluated here, so signal that this filter is unsupported rather than silently producing an
			 * (incorrect) empty expansion — the caller can then surface an error or delegate to another
			 * terminology service in the chain.
			 *
			 * @see <a href="https://build.fhir.org/codesystem.html#properties">
			 *      FHIR CodeSystem Concept Properties (4.8.11)</a>
			 * @see <a href="https://build.fhir.org/codesystem.html#defined-props">
			 *      FHIR CodeSystem Defined Concept Properties (4.8.12)</a>
			 */
			if (!onCode && !onDisplay) {
				throw unsupportedFilter(filter);
			}

			String theFilterValue = filter.getValue();
			String theConceptCode = concept.getCode();
			String theConceptPropertyValue = onCode
					? concept.getCode()
					: propertyIndex
							.getOrDefault("display", Collections.emptyMap())
							.get(theConceptCode);

			switch (filter.getOp()) {
				case EQUAL:
					// if we’re filtering on display but there is none, it’s not a match
					if (theConceptPropertyValue == null) {
						return false;
					}

					return isEqualsWithOptionalCaseSensitive(theFilterValue, theConceptPropertyValue);
				case ISA:
					// 1) structural filter guards
					if (failsStructuralFilterGuard(theFilterValue, onCode)) {
						return false;
					}

					// 2) accept the code itself
					if (isEqualsWithOptionalCaseSensitive(theFilterValue, theConceptCode)) {
						return true;
					}

					// 3) accept any true descendant
					return isDescendantOf(theFilterValue, theConceptCode);
				case DESCENDENTOF:
					// 1) structural filter guards
					if (failsStructuralFilterGuard(theFilterValue, onCode)) {
						return false;
					}

					// 2) accept only any true descendant
					return isDescendantOf(theFilterValue, theConceptCode);
				case ISNOTA:
					// 1) structural filter guards
					if (failsStructuralFilterGuard(theFilterValue, onCode)) {
						return false;
					}

					// 2) Exclude the filter value itself
					if (isEqualsWithOptionalCaseSensitive(theFilterValue, theConceptCode)) {
						return false;
					}

					// 3) Exclude any true descendant
					if (isDescendantOf(theFilterValue, theConceptCode)) {
						return false;
					}

					// 5) Everything else passes
					return true;
				case REGEX:
					// 1) If there's no target text (e.g. display missing), we can’t match
					if (theConceptPropertyValue == null) {
						return false;
					}

					// 2) Delegate to our cached helper (which handles invalid patterns)
					return matchesRegex(theFilterValue, theConceptPropertyValue);
				case IN:
					// 1) If there's no target text (e.g. display missing), we can’t match
					if (theConceptPropertyValue == null) {
						return false;
					}

					// 2) Match
					return csvFilterListContains(theFilterValue, theConceptPropertyValue);
				case NOTIN:
					// If there is no property value, then it’s trivially “not in” any list → pass
					if (theConceptPropertyValue == null) {
						return true;
					}

					// 2) Match
					return !csvFilterListContains(theFilterValue, theConceptPropertyValue);
				case GENERALIZES: {
					// 1) structural filter guards
					if (failsStructuralFilterGuard(theFilterValue, onCode)) {
						return false;
					}

					// 2) Include X itself
					if (isEqualsWithOptionalCaseSensitive(theFilterValue, theConceptCode)) {
						return true;
					}

					// 3) Include any true ancestor of X:
					//    i.e. those codes C for which X is in C's subtree.
					if (isDescendantOf(theConceptCode, theFilterValue)) {
						return true;
					}

					// 5) Everything else is outside the ancestor chain → filtered out
					return false;
				}
				case CHILDOF: {
					// 1) structural filter guards
					if (failsStructuralFilterGuard(theFilterValue, onCode)) {
						return false;
					}

					// 2) Look up the direct children of X
					Set<String> directKids = getChildren(theFilterValue);

					// 3) Accept only if our candidate code matches one of those children
					return directKids.stream()
							.anyMatch(childCode -> isEqualsWithOptionalCaseSensitive(childCode, theConceptCode));
				}
				case DESCENDENTLEAF: {
					// 1) structural filter guards
					if (failsStructuralFilterGuard(theFilterValue, onCode)) {
						return false;
					}

					// 2) It must be a true descendant (not X itself)
					if (!isDescendantOf(theFilterValue, theConceptCode)) {
						return false;
					}

					// 3) It must have no children of its own → is a leaf
					Set<String> kids = getChildren(theConceptCode);
					return kids.isEmpty();
				}
				case EXISTS: {
					// filter.getValue() will be "true" or "false"
					boolean wantExists = Boolean.parseBoolean(theFilterValue);

					if (onCode) {
						// Every concept always has a code, so:
						//  exists=true  ⇒ include all (pass filter)
						//  exists=false ⇒ include none (fail filter)

						// Also check whether the *code* is actually defined in the CodeSystem
						boolean hasCode = !isFilterPropertyValueNotInCodeSystem(theConceptCode);

						return wantExists == hasCode;
					}

					// Otherwise we’re on display
					// theConceptPropertyValue was set to concept.getDisplay() above
					boolean hasDisplay = theConceptPropertyValue != null;
					return wantExists == hasDisplay;
				}
			}
		}

		return false;
	}

	/**
	 * Whether the given concept satisfies the {@code exists} check for a standard property: for the
	 * hierarchical {@code child}/{@code parent} this means it has children / a parent; for the
	 * boolean/date properties it means the concept was indexed as carrying that property.
	 */
	private boolean conceptHasStandardProperty(StandardConceptProperty theProperty, String theConceptCode) {
		switch (theProperty.kind()) {
			case PARENT:
				return hasParent(theConceptCode);
			case CHILD:
				return hasChildren(theConceptCode);
			default:
				return conceptsByStandardProperty
						.getOrDefault(theProperty, Set.of())
						.contains(normalizeCode(theConceptCode));
		}
	}

	/**
	 * Return false if we should even _try_ a structural filter on this property + value:
	 *   1) Must be on the code (not display)
	 *   2) The filter value must actually exist in the CodeSystem
	 */
	private boolean failsStructuralFilterGuard(String theFilterValue, boolean onCode) {
		return !onCode || isFilterPropertyValueNotInCodeSystem(theFilterValue);
	}

	private boolean isDescendantOf(String theParentCode, String theCandidatePropertyValue) {
		Deque<String> stack = new ArrayDeque<>(getChildren(theParentCode));
		// Guard against cycles (possible when the hierarchy is expressed via flat parent/child properties),
		// otherwise a cycle would loop forever for a candidate that is not part of the subtree.
		// Seed the visited-set with the parent so that a cycle back to it (A→B→A) does not make the parent
		// its own descendant.
		Set<String> visited = new HashSet<>();
		visited.add(normalizeCode(theParentCode));
		while (!stack.isEmpty()) {
			String theChildCode = stack.pop();
			if (!visited.add(normalizeCode(theChildCode))) {
				continue;
			}
			if (isEqualsWithOptionalCaseSensitive(theChildCode, theCandidatePropertyValue)) {
				return true;
			}
			stack.addAll(getChildren(theChildCode));
		}

		return false;
	}

	/**
	 * Return the direct children of the given code, resolving the lookup case-insensitively when the
	 * CodeSystem is not case-sensitive (so a filter value like "p" resolves the subtree stored under "P").
	 */
	private Set<String> getChildren(String theCode) {
		return conceptCodeTree.getOrDefault(normalizeCode(theCode), Set.of());
	}

	/**
	 * Normalize a code for use as a hierarchy map key / visited-set entry, honoring case sensitivity.
	 */
	private String normalizeCode(String theCode) {
		return codeSystem.getCaseSensitive() ? theCode : theCode.toLowerCase(Locale.ROOT);
	}

	private boolean isEqualsWithOptionalCaseSensitive(String a, String b) {
		return codeSystem.getCaseSensitive()
				? a.equals(b) // case-sensitive
				: a.equalsIgnoreCase(b); // case-insensitive
	}

	/**
	 * Return true if 'code' appears in the comma-separated list 'csv'
	 */
	private boolean csvFilterListContains(String theCsvFilter, String theCandidatePropertyValue) {
		// lazily parse & cache the comma-list
		Set<String> values = inSetsMap.computeIfAbsent(
				theCsvFilter, filter -> new HashSet<>(Arrays.asList(filter.split("\\s*,\\s*"))));

		// Now just test membership, respecting case‐sensitivity
		return values.stream().anyMatch(part -> isEqualsWithOptionalCaseSensitive(part, theCandidatePropertyValue));
	}

	private boolean hasChildren(String theCode) {
		return !getChildren(theCode).isEmpty();
	}

	private boolean hasParent(String theCode) {
		if (codeSystem.getCaseSensitive()) {
			return allChildCodes.contains(theCode);
		}
		return allChildCodesLower.contains(theCode.toLowerCase(Locale.ROOT));
	}

	private boolean isFilterPropertyValueNotInCodeSystem(String theFilterPropertyValue) {
		// Fast O(1) existence check, respecting case sensitivity
		if (codeSystem.getCaseSensitive()) {
			return !allCodes.contains(theFilterPropertyValue);
		} else {
			return !allCodesLower.contains(theFilterPropertyValue.toLowerCase(Locale.ROOT));
		}
	}

	/**
	 * Match `text` against the regex `expr`, respecting caseSensitivity.
	 * Returns false if the pattern is invalid.
	 */
	private boolean matchesRegex(String expr, String text) {
		try {
			Pattern p = regexCache.computeIfAbsent(
					expr, key -> Pattern.compile(key, codeSystem.getCaseSensitive() ? 0 : Pattern.CASE_INSENSITIVE));
			return p.matcher(text).matches();
		} catch (PatternSyntaxException e) {
			// Invalid regex → treat as “no match”
			return false;
		}
	}

	private void buildIndexes() {
		if (!hasIndexRun) {
			classifyStandardProperties();
			buildIndexes(codeSystem.getConcept());
			hasIndexRun = true;
		}
	}

	/**
	 * Inspect how the CodeSystem declares the standard concept-properties, and which the filters use. A
	 * property declared with its canonical URI is honored; one declared with a <em>different</em> URI has an
	 * unknown meaning and is recorded as conflicting (a filter using it will fail rather than be
	 * misinterpreted); an undeclared property is matched by its reserved code name (with an informational log
	 * emitted when it is first matched, if a filter uses it).
	 */
	private void classifyStandardProperties() {
		if (filters != null) {
			for (ValueSet.ConceptSetFilterComponent filter : filters) {
				if (filter.hasProperty()) {
					StandardConceptProperty property = StandardConceptProperty.forFilterProperty(
							filter.getProperty().toLowerCase(Locale.ROOT));
					if (property != null) {
						propertiesUsedInFilters.add(property);
					}
				}
			}
		}
		for (StandardConceptProperty property : StandardConceptProperty.values()) {
			CodeSystem.PropertyComponent declaration = findPropertyDeclarationByCode(property.code());
			if (declaration == null || !declaration.hasUri()) {
				continue; // undeclared / no URI → matched by reserved code name (logged when first matched)
			}
			if (property.canonicalUri().equals(declaration.getUri())) {
				canonicalUriDeclared.add(property);
			} else {
				conflictingPropertyUris.put(property, declaration.getUri());
			}
		}
	}

	private CodeSystem.PropertyComponent findPropertyDeclarationByCode(String theCode) {
		for (CodeSystem.PropertyComponent property : codeSystem.getProperty()) {
			if (theCode.equals(property.getCode())) {
				return property;
			}
		}
		return null;
	}

	private void buildIndexes(List<CodeSystem.ConceptDefinitionComponent> defs) {
		for (var def : defs) {
			String code = def.getCode();
			String display = def.getDisplay();

			// 1) Index existence
			allCodes.add(code);
			allCodesLower.add(code.toLowerCase(Locale.ROOT));

			// 2) Index immediate children (nested representation)
			for (var child : def.getConcept()) {
				addParentChildEdge(code, child.getCode());
			}

			// 2b) Index the standard concept-properties expressed on this concept (flat hierarchy via
			// parent/child, plus the boolean/date exists-properties).
			for (var property : def.getProperty()) {
				if (!property.hasValue() || !property.getValue().isPrimitive()) {
					continue;
				}
				String value = property.getValue().primitiveValue();
				if (isBlank(value)) {
					continue;
				}
				indexStandardProperty(property.getCode(), code, value);
			}

			// 3) Index the "code" property
			propertyIndex.computeIfAbsent("code", k -> new HashMap<>()).put(code, code);

			// 4) Index the "display" property
			propertyIndex.computeIfAbsent("display", k -> new HashMap<>()).put(code, display);

			// 5) Recurse
			buildIndexes(def.getConcept());
		}
	}

	private void addParentChildEdge(String theParentCode, String theChildCode) {
		// Key the tree by the normalized parent code so that case-insensitive systems resolve the subtree
		// even when a filter value differs in case from the stored code. Child values keep their original
		// casing because membership comparisons go through isEqualsWithOptionalCaseSensitive().
		conceptCodeTree
				.computeIfAbsent(normalizeCode(theParentCode), k -> new HashSet<>())
				.add(theChildCode);
		allChildCodes.add(theChildCode);
		allChildCodesLower.add(theChildCode.toLowerCase(Locale.ROOT));
	}

	/**
	 * Index the given concept property if it is one of the standard properties we support. The parent/child
	 * properties add a hierarchy edge; the boolean {@code inactive}/{@code notSelectable} count the concept
	 * only when the value is {@code true}; the date-valued properties count it on presence. A property the
	 * CodeSystem declared with a non-canonical URI is skipped (its meaning is unknown).
	 */
	private void indexStandardProperty(String thePropertyCode, String theConceptCode, String theValue) {
		StandardConceptProperty property = StandardConceptProperty.forConceptPropertyCode(thePropertyCode);
		if (property == null || conflictingPropertyUris.containsKey(property)) {
			return;
		}
		if (propertiesUsedInFilters.contains(property)
				&& !canonicalUriDeclared.contains(property)
				&& loggedNameMatch.add(property)) {
			ourLog.info(
					"Concept property '{}' in CodeSystem '{}' is not declared with its canonical URI '{}'; matching by property code name.",
					property.code(),
					codeSystem.getUrl(),
					property.canonicalUri());
		}
		switch (property.kind()) {
			case PARENT:
				// 'theConceptCode' declares 'theValue' as its parent → theValue -> theConceptCode
				addParentChildEdge(theValue, theConceptCode);
				break;
			case CHILD:
				// 'theConceptCode' declares 'theValue' as its child → theConceptCode -> theValue
				addParentChildEdge(theConceptCode, theValue);
				break;
			case BOOLEAN_TRUE:
				if ("true".equalsIgnoreCase(theValue)) {
					addStandardPropertyMembership(property, theConceptCode);
				}
				break;
			case PRESENCE:
				addStandardPropertyMembership(property, theConceptCode);
				break;
		}
	}

	private void addStandardPropertyMembership(StandardConceptProperty theProperty, String theConceptCode) {
		conceptsByStandardProperty
				.computeIfAbsent(theProperty, k -> new HashSet<>())
				.add(normalizeCode(theConceptCode));
	}

	private static boolean isBlank(String theValue) {
		return theValue == null || theValue.isEmpty();
	}

	/**
	 * Parses the value of an {@code exists} filter as a strict boolean literal. The value is semantically
	 * required and must be {@code true} or {@code false}; anything else (blank, "0", "no", …) is rejected
	 * rather than silently coerced to {@code false}, which would invert the filter (for example selecting
	 * the leaves/roots for a {@code child}/{@code parent} filter).
	 *
	 * @throws UnsupportedFilterException if the value is not a strict boolean literal
	 */
	private static boolean parseRequiredBoolean(ValueSet.ConceptSetFilterComponent theFilter) {
		String value = theFilter.hasValue() ? theFilter.getValue().trim() : null;
		if ("true".equalsIgnoreCase(value)) {
			return true;
		}
		if ("false".equalsIgnoreCase(value)) {
			return false;
		}
		throw new UnsupportedFilterException(Msg.code(3006)
				+ "In-memory ValueSet expansion filter on property '" + theFilter.getProperty()
				+ "' with operator 'exists' requires a boolean value ('true' or 'false') but was '"
				+ theFilter.getValue() + "'");
	}

	private static UnsupportedFilterException unsupportedFilter(ValueSet.ConceptSetFilterComponent theFilter) {
		String op = theFilter.hasOp() ? theFilter.getOp().toCode() : "(none)";
		String property = theFilter.hasProperty() ? theFilter.getProperty() : "(none)";
		return new UnsupportedFilterException(Msg.code(3004)
				+ "In-memory ValueSet expansion does not support filter with property '" + property
				+ "' and operator '" + op + "'");
	}

	/**
	 * The standard FHIR concept-properties this in-memory support can evaluate with the {@code exists}
	 * operator, identified by their reserved code name. The {@link Kind} determines how a match is
	 * interpreted: {@code parent}/{@code child} contribute a hierarchy edge; {@code inactive}/
	 * {@code notSelectable} count a concept only when their value is {@code true}; the date-valued
	 * {@code deprecated}/{@code deprecationDate}/{@code retirementDate} count a concept on presence.
	 *
	 * @see <a href="http://hl7.org/fhir/codesystem-concept-properties.html">FHIR standard concept properties</a>
	 */
	private enum StandardConceptProperty {
		PARENT("parent", Kind.PARENT),
		CHILD("child", Kind.CHILD),
		INACTIVE("inactive", Kind.BOOLEAN_TRUE),
		NOT_SELECTABLE("notSelectable", Kind.BOOLEAN_TRUE),
		DEPRECATED("deprecated", Kind.PRESENCE),
		DEPRECATION_DATE("deprecationDate", Kind.PRESENCE),
		RETIREMENT_DATE("retirementDate", Kind.PRESENCE);

		/** How a matched property is interpreted during indexing and {@code exists} evaluation. */
		private enum Kind {
			PARENT, // hierarchical: the property value is the concept's parent code
			CHILD, // hierarchical: the property value is the concept's child code
			BOOLEAN_TRUE, // boolean flag: the concept is flagged only when the value is 'true'
			PRESENCE // date-valued: the concept is flagged when the property is present
		}

		private final String code;
		private final Kind kind;

		StandardConceptProperty(String theCode, Kind theKind) {
			code = theCode;
			kind = theKind;
		}

		private String code() {
			return code;
		}

		private Kind kind() {
			return kind;
		}

		/**
		 * The canonical concept-properties URI that identifies this standard property (e.g.
		 * {@code http://hl7.org/fhir/concept-properties#inactive}).
		 */
		private String canonicalUri() {
			return CONCEPT_PROPERTIES_SYSTEM + code;
		}

		/**
		 * Match a filter property name (already lower-cased during filtering) to a standard property, or
		 * {@code null} if it is not one of them.
		 */
		private static StandardConceptProperty forFilterProperty(String theLowerCasedProperty) {
			for (StandardConceptProperty next : values()) {
				if (next.code.toLowerCase(Locale.ROOT).equals(theLowerCasedProperty)) {
					return next;
				}
			}
			return null;
		}

		/**
		 * Match an exact CodeSystem concept-property code to a standard property, or {@code null} if it is
		 * not one of them.
		 */
		private static StandardConceptProperty forConceptPropertyCode(String theCode) {
			for (StandardConceptProperty next : values()) {
				if (next.code.equals(theCode)) {
					return next;
				}
			}
			return null;
		}
	}

	/**
	 * Thrown when a ValueSet filter uses a property/operator combination that the in-memory expansion
	 * cannot evaluate. Callers should surface this as an expansion error (or delegate to another
	 * terminology service) rather than returning a silently incomplete/empty expansion.
	 */
	public static class UnsupportedFilterException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public UnsupportedFilterException(String theMessage) {
			super(theMessage);
		}
	}
}
