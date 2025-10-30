package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.util.FhirVersionIndependentConcept;
import org.hl7.fhir.r5.model.CodeSystem;
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
 * Will only work on 'code', 'concept' and 'display' property types.
 *
 * Supports: equal | is-a | descendent-of | is-not-a | regex | in | not-in | generalizes | child-of | descendent-leaf | exists
 */
public class ValueSetExpansionFilterContext {
	private final Map<String, Map<String, String>> propertyIndex = new HashMap<>();

	private final Map<String, Set<String>> conceptCodeTree = new HashMap<>();
	private final Set<String> allCodes = new HashSet<>();
	private final Set<String> allCodesLower = new HashSet<>();
	private final Map<String, Set<String>> inSetsMap = new HashMap<>();
	private final Map<String, Pattern> regexCache = new HashMap<>();
	private final CodeSystem codeSystem;
	private final List<ValueSet.ConceptSetFilterComponent> filters;
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

			/**
			 * Only code/display supported in the in-memory validation support, so reject any custom concept properties,
			 * even though that should technically be supported by the FHIR spec.
			 *
			 * @see <a href="https://build.fhir.org/codesystem.html#properties">
			 *      FHIR CodeSystem Concept Properties (4.8.11)</a>
			 * @see <a href="https://build.fhir.org/codesystem.html#defined-props">
			 *      FHIR CodeSystem Defined Concept Properties (4.8.12)</a>
			 */
			if (!onCode && !onDisplay) {
				return false;
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
					Set<String> directKids = conceptCodeTree.getOrDefault(theFilterValue, Collections.emptySet());

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
					Set<String> kids = conceptCodeTree.getOrDefault(theConceptCode, Collections.emptySet());
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
		Deque<String> stack = new ArrayDeque<>(conceptCodeTree.getOrDefault(theParentCode, Set.of()));
		while (!stack.isEmpty()) {
			String theChildCode = stack.pop();
			if (isEqualsWithOptionalCaseSensitive(theChildCode, theCandidatePropertyValue)) {
				return true;
			}
			stack.addAll(conceptCodeTree.getOrDefault(theChildCode, Set.of()));
		}

		return false;
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
			buildIndexes(codeSystem.getConcept());
			hasIndexRun = true;
		}
	}

	private void buildIndexes(List<CodeSystem.ConceptDefinitionComponent> defs) {
		for (var def : defs) {
			String code = def.getCode();
			String display = def.getDisplay();

			// 1) Index existence
			allCodes.add(code);
			allCodesLower.add(code.toLowerCase());

			// 2) Index immediate children
			for (var child : def.getConcept()) {
				conceptCodeTree.computeIfAbsent(code, k -> new HashSet<>()).add(child.getCode());
			}

			// 3) Index the "code" property
			propertyIndex.computeIfAbsent("code", k -> new HashMap<>()).put(code, code);

			// 4) Index the "display" property
			propertyIndex.computeIfAbsent("display", k -> new HashMap<>()).put(code, display);

			// 5) Recurse
			buildIndexes(def.getConcept());
		}
	}
}
