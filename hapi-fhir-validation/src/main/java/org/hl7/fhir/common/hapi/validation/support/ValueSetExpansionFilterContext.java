package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.util.FhirVersionIndependentConcept;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.Enumerations.FilterOperator;
import org.hl7.fhir.r5.model.ValueSet;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
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
 * {@code parent} (or {@code child}) concept-property whose {@code CodeSystem.property} definition uses
 * the canonical concept-properties URI ({@value #CONCEPT_PROPERTY_PARENT_URI} /
 * {@value #CONCEPT_PROPERTY_CHILD_URI}).
 *
 * Supports: equal | is-a | descendent-of | is-not-a | regex | in | not-in | generalizes | child-of | descendent-leaf | exists
 */
public class ValueSetExpansionFilterContext {
	public static final String CONCEPT_PROPERTY_PARENT_URI = "http://hl7.org/fhir/concept-properties#parent";
	public static final String CONCEPT_PROPERTY_CHILD_URI = "http://hl7.org/fhir/concept-properties#child";

	private final Map<String, Map<String, String>> propertyIndex = new HashMap<>();

	private final Map<String, Set<String>> conceptCodeTree = new HashMap<>();
	// Codes carrying a standard concept-property usable with the 'exists' operator (see StandardExistsProperty).
	private final Map<StandardExistsProperty, Set<String>> conceptsByStandardProperty = new HashMap<>();
	private final Set<String> allCodes = new HashSet<>();
	private final Set<String> allCodesLower = new HashSet<>();
	private final Set<String> allChildCodes = new HashSet<>();
	private final Set<String> allChildCodesLower = new HashSet<>();
	private final Map<String, Set<String>> inSetsMap = new HashMap<>();
	private final Map<String, Pattern> regexCache = new HashMap<>();
	private final CodeSystem codeSystem;
	private final List<ValueSet.ConceptSetFilterComponent> filters;
	private String parentPropertyCode;
	private String childPropertyCode;
	private boolean hasIndexRun = false;

	public ValueSetExpansionFilterContext(CodeSystem codeSystem, List<ValueSet.ConceptSetFilterComponent> filters) {
		this.codeSystem = codeSystem;
		this.filters = filters;
	}

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
			boolean onChild = theFilterProperty.equals("child");
			boolean onParent = theFilterProperty.equals("parent");

			/*
			 * Hierarchical membership filters. Per the FHIR spec the 'child' and 'parent' properties are
			 * used with the 'exists' operator to select concepts that do (or don't) have children/parents
			 * — e.g. property=child op=exists value=false selects the leaf concepts.
			 */
			if (onChild || onParent) {
				if (filter.getOp() == FilterOperator.EXISTS) {
					boolean wantExists = Boolean.parseBoolean(filter.getValue());
					boolean hasRelation = onChild ? hasChildren(concept.getCode()) : hasParent(concept.getCode());
					return wantExists == hasRelation;
				}

				// The hierarchical child/parent properties only support the 'exists' operator in-memory.
				throw unsupportedFilter(filter);
			}

			/*
			 * Standard concept-properties usable with the 'exists' operator: the boolean 'inactive' /
			 * 'notSelectable' (a concept is flagged only when the value is 'true') and the date-valued
			 * 'deprecated' / 'deprecationDate' / 'retirementDate' (flagged when the property is present).
			 * That value distinction is applied at index time; here it is a uniform membership check.
			 */
			StandardExistsProperty standardProperty = StandardExistsProperty.forFilterProperty(theFilterProperty);
			if (standardProperty != null) {
				if (filter.getOp() == FilterOperator.EXISTS) {
					boolean wantExists = Boolean.parseBoolean(filter.getValue());
					boolean isFlagged = conceptsByStandardProperty
							.getOrDefault(standardProperty, Set.of())
							.contains(normalizeCode(concept.getCode()));
					return wantExists == isFlagged;
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
		return allChildCodesLower.contains(theCode.toLowerCase());
	}

	private boolean isFilterPropertyValueNotInCodeSystem(String theFilterPropertyValue) {
		// Fast O(1) existence check, respecting case sensitivity
		if (codeSystem.getCaseSensitive()) {
			return !allCodes.contains(theFilterPropertyValue);
		} else {
			return !allCodesLower.contains(theFilterPropertyValue.toLowerCase());
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
			parentPropertyCode = resolveHierarchyPropertyCode(CONCEPT_PROPERTY_PARENT_URI);
			childPropertyCode = resolveHierarchyPropertyCode(CONCEPT_PROPERTY_CHILD_URI);
			buildIndexes(codeSystem.getConcept());
			hasIndexRun = true;
		}
	}

	/**
	 * Resolve the concept-property code that carries hierarchy for the given canonical concept-properties
	 * URI, or {@code null} if none is defined. Only a property definition declaring exactly that URI is
	 * honored, so a custom property that happens to be named "parent"/"child" is ignored.
	 */
	private String resolveHierarchyPropertyCode(String theCanonicalUri) {
		for (CodeSystem.PropertyComponent property : codeSystem.getProperty()) {
			if (theCanonicalUri.equals(property.getUri()) && property.hasCode()) {
				return property.getCode();
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
			allCodesLower.add(code.toLowerCase());

			// 2) Index immediate children (nested representation)
			for (var child : def.getConcept()) {
				addParentChildEdge(code, child.getCode());
			}

			// 2b) Index hierarchy expressed via flat parent/child concept-properties
			for (var property : def.getProperty()) {
				if (!property.hasValue() || !property.getValue().isPrimitive()) {
					continue;
				}
				String relatedCode = property.getValue().primitiveValue();
				if (isBlank(relatedCode)) {
					continue;
				}
				if (parentPropertyCode != null && parentPropertyCode.equals(property.getCode())) {
					// 'code' declares 'relatedCode' as its parent → relatedCode -> code
					addParentChildEdge(relatedCode, code);
				} else if (childPropertyCode != null && childPropertyCode.equals(property.getCode())) {
					// 'code' declares 'relatedCode' as its child → code -> relatedCode
					addParentChildEdge(code, relatedCode);
				} else {
					indexStandardExistsProperty(property.getCode(), code, relatedCode);
				}
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
		allChildCodesLower.add(theChildCode.toLowerCase());
	}

	/**
	 * Record that {@code theConceptCode} carries the standard concept-property {@code thePropertyCode} (if it
	 * is one we support with {@code exists}). Boolean properties (inactive/notSelectable) only count when the
	 * value is {@code true}; date-valued properties count on presence.
	 */
	private void indexStandardExistsProperty(String thePropertyCode, String theConceptCode, String theValue) {
		StandardExistsProperty standardProperty = StandardExistsProperty.forConceptPropertyCode(thePropertyCode);
		if (standardProperty == null) {
			return;
		}
		if (standardProperty.requiresBooleanTrue() && !"true".equalsIgnoreCase(theValue)) {
			return;
		}
		conceptsByStandardProperty
				.computeIfAbsent(standardProperty, k -> new HashSet<>())
				.add(normalizeCode(theConceptCode));
	}

	private static boolean isBlank(String theValue) {
		return theValue == null || theValue.isEmpty();
	}

	private static UnsupportedFilterException unsupportedFilter(ValueSet.ConceptSetFilterComponent theFilter) {
		String op = theFilter.hasOp() ? theFilter.getOp().toCode() : "(none)";
		String property = theFilter.hasProperty() ? theFilter.getProperty() : "(none)";
		return new UnsupportedFilterException("In-memory ValueSet expansion does not support filter with property '"
				+ property + "' and operator '" + op + "'");
	}

	/**
	 * The standard FHIR concept-properties that this in-memory support can evaluate with the {@code exists}
	 * operator. Boolean properties ({@code inactive}, {@code notSelectable}) count only when their value is
	 * {@code true}; the date-valued properties ({@code deprecated}, {@code deprecationDate},
	 * {@code retirementDate}) count on presence.
	 *
	 * @see <a href="http://hl7.org/fhir/codesystem-concept-properties.html">FHIR standard concept properties</a>
	 */
	private enum StandardExistsProperty {
		INACTIVE("inactive", true),
		NOT_SELECTABLE("notSelectable", true),
		DEPRECATED("deprecated", false),
		DEPRECATION_DATE("deprecationDate", false),
		RETIREMENT_DATE("retirementDate", false);

		private final String code;
		private final boolean requiresBooleanTrue;

		StandardExistsProperty(String theCode, boolean theRequiresBooleanTrue) {
			code = theCode;
			requiresBooleanTrue = theRequiresBooleanTrue;
		}

		private boolean requiresBooleanTrue() {
			return requiresBooleanTrue;
		}

		/**
		 * Match a filter property name (already lower-cased during filtering) to a standard property, or
		 * {@code null} if it is not one of them.
		 */
		private static StandardExistsProperty forFilterProperty(String theLowerCasedProperty) {
			for (StandardExistsProperty next : values()) {
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
		private static StandardExistsProperty forConceptPropertyCode(String theCode) {
			for (StandardExistsProperty next : values()) {
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
