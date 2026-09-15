package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.util.FhirVersionIndependentConcept;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.Enumerations.CodeSystemContentMode;
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
 * <p>Custom (non-standard) concept properties are also evaluated, but only when the CodeSystem declares
 * {@code content=complete} and only with the {@code =}, {@code in}, {@code not-in} and {@code regex}
 * operators. Under any other content mode they remain unsupported.
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

	/**
	 * The operators a custom concept-property filter can be evaluated with. The others are hierarchical and
	 * resolve against concept codes, so they do not apply to an arbitrary property value.
	 */
	private static final Set<FilterOperator> CUSTOM_PROPERTY_OPERATORS =
			EnumSet.of(FilterOperator.EQUAL, FilterOperator.IN, FilterOperator.NOTIN, FilterOperator.REGEX);

	/** Filter properties that select on the concept itself rather than on a concept-property value. */
	private static final Set<String> CODE_AND_DISPLAY_FILTER_PROPERTIES = Set.of("concept", "code", "display");

	// The CodeSystem.concept's own 'code' and 'display' values: "code" or "display" -> concept code -> value.
	private final Map<String, Map<String, String>> codeSystemCodeAndDisplayIndex = new HashMap<>();

	// CodeSystem.concept.property.value for NON-standard (custom) properties. A Set is used since concept.property
	// cardinality is 0..*; a filter matches when ANY value satisfies it.
	private final Map<String, Map<String, Set<String>>> codeSystemCustomPropertyIndex = new HashMap<>();

	// Concepts carrying a non-primitive CodeSystem.concept.property.value (e.g. a Coding) for a custom
	// property: property code -> concept codes. Such a value cannot be compared against the filter's string
	// value, so the concept is recorded here to avoid treating it as having no value - that would report a
	// confident "not a member" about data never examined.
	private final Map<String, Set<String>> codeSystemCustomPropertiesWithUnusableValue = new HashMap<>();

	// The non-standard property codes CodeSystem.property[] declares.
	// FHIR documents CodeSystem.concept.property.code as a reference to CodeSystem.property.code, but a
	// CodeSystem that omits the declaration is still valid. A CodeSystem.concept may therefore carry a value
	// for a property that was never declared, and that value is read and compared like any other.
	//
	// This set is consulted only when a CodeSystem.concept has NO value for the property being filtered on,
	// where the declaration is the only thing separating two different answers:
	//   declared   -> the CodeSystem knows the property and this CodeSystem.concept simply has no value for
	//                 it, so the concept is not a member (a determined negative)
	//   undeclared -> the CodeSystem does not know the property at all, so membership can be neither
	//                 established nor refuted (undetermined)
	private final Set<String> codeSystemDeclaredCustomProperties = new HashSet<>();

	// The non-standard property codes referenced by the ValueSet filters this context was constructed with,
	// that is the ValueSet.compose.include.filter (or compose.exclude.filter) entries being applied.
	//
	// Indexing walks every CodeSystem.concept.property and keeps only those whose code appears in this set,
	// storing them in codeSystemCustomPropertyIndex; the rest are skipped to avoid indexing properties that
	// can never be accessed.
	//
	// Mirrors valueSetStandardPropertiesUsedInFilters, but stores codes verbatim: custom property codes are
	// case-sensitive
	// and must not be lowercased the way the standard-property dispatch does.
	private final Set<String> valueSetCustomPropertiesUsedInFilters = new HashSet<>();

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
	private final Set<StandardConceptProperty> valueSetStandardPropertiesUsedInFilters =
			EnumSet.noneOf(StandardConceptProperty.class);
	// Standard properties for which the "matched by code name (not declared)" info log was already emitted.
	private final Set<StandardConceptProperty> loggedNameMatch = EnumSet.noneOf(StandardConceptProperty.class);
	private final Set<String> allCodes = new HashSet<>();
	private final Set<String> allCodesLower = new HashSet<>();
	private final Set<String> allChildCodes = new HashSet<>();
	private final Set<String> allChildCodesLower = new HashSet<>();
	private final Map<String, Set<String>> inSetsMap = new HashMap<>();
	private final Map<String, Pattern> regexCache = new HashMap<>();
	private final CodeSystem myCodeSystem;
	private final List<ValueSet.ConceptSetFilterComponent> myFilters;
	private boolean hasIndexRun = false;

	public ValueSetExpansionFilterContext(CodeSystem codeSystem, List<ValueSet.ConceptSetFilterComponent> filters) {
		myCodeSystem = codeSystem;
		myFilters = filters;
	}

	/**
	 * @return {@code true} if the concept is filtered OUT (fails at least one filter), {@code false} if it
	 *     passes all filters.
	 * @throws UnsupportedFilterException if a filter uses a property/operator combination the in-memory
	 *     expansion cannot evaluate. Callers should surface this as an expansion error or delegate to another
	 *     terminology service (this exception is unchecked to keep the public API backwards-compatible).
	 * @throws UndeterminedFilterException if a custom concept-property filter could not be evaluated for this
	 *     concept, leaving membership neither established nor refuted. Callers must keep this apart from a
	 *     determined negative (see the exception's javadoc).
	 */
	public boolean isFiltered(FhirVersionIndependentConcept concept) {
		if (myFilters == null || myFilters.isEmpty()) {
			return false;
		}

		// buildChildrenMap() once in ctor or lazily here
		for (ValueSet.ConceptSetFilterComponent filter : myFilters) {
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
	 * @throws UndeterminedFilterException if membership could not be determined (see {@link #isFiltered}).
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
			 * Anything else is a filter on a custom (non-standard) concept property. Those are valid per the
			 * FHIR spec, and can be evaluated here when the CodeSystem declares content=complete; under any
			 * other content mode they remain unsupported.
			 *
			 * @see <a href="https://build.fhir.org/codesystem.html#properties">
			 *      FHIR CodeSystem Concept Properties (4.8.11)</a>
			 * @see <a href="https://build.fhir.org/codesystem.html#defined-props">
			 *      FHIR CodeSystem Defined Concept Properties (4.8.12)</a>
			 */
			if (!onCode && !onDisplay) {
				return passesCustomPropertyFilter(filter, concept);
			}

			String theFilterValue = filter.getValue();
			String theConceptCode = concept.getCode();
			String theConceptPropertyValue = onCode
					? concept.getCode()
					: codeSystemCodeAndDisplayIndex
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
		return myCodeSystem.getCaseSensitive() ? theCode : theCode.toLowerCase(Locale.ROOT);
	}

	private boolean isEqualsWithOptionalCaseSensitive(String a, String b) {
		return myCodeSystem.getCaseSensitive()
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
		if (myCodeSystem.getCaseSensitive()) {
			return allChildCodes.contains(theCode);
		}
		return allChildCodesLower.contains(theCode.toLowerCase(Locale.ROOT));
	}

	private boolean isFilterPropertyValueNotInCodeSystem(String theFilterPropertyValue) {
		// Fast O(1) existence check, respecting case sensitivity
		if (myCodeSystem.getCaseSensitive()) {
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
					expr, key -> Pattern.compile(key, myCodeSystem.getCaseSensitive() ? 0 : Pattern.CASE_INSENSITIVE));
			return p.matcher(text).matches();
		} catch (PatternSyntaxException e) {
			// Invalid regex → treat as “no match”
			return false;
		}
	}

	private void buildIndexes() {
		if (!hasIndexRun) {
			classifyProperties();
			buildIndexes(myCodeSystem.getConcept());
			hasIndexRun = true;
		}
	}

	/**
	 * Inspect which CodeSystem.concept.property codes the ValueSet filters reference, and how
	 * CodeSystem.property[] declares them. Runs once, ahead of the CodeSystem.concept indexes, because both of
	 * those answers decide what those indexes need to hold.
	 *
	 * <p>For the <em>standard</em> CodeSystem.concept.property codes: one declared in CodeSystem.property[]
	 * with its canonical URI is honored; one declared with a <em>different</em> URI has an unknown meaning and
	 * is recorded as conflicting (a ValueSet filter using it will fail rather than be misinterpreted); an
	 * undeclared property is matched by its reserved code name, with an informational log emitted when it is
	 * first matched, if a ValueSet filter uses it.
	 *
	 * <p>For <em>custom</em> (non-standard) CodeSystem.concept.property codes: the codes the ValueSet filters
	 * reference are recorded verbatim, because they are case-sensitive and they bound which CodeSystem.concept.property values are
	 * worth indexing. Whether CodeSystem.property[] declares each of them is recorded separately - not to
	 * decide whether a value may be read, but to tell a CodeSystem.concept that genuinely has no value (a
	 * determined negative) from a property the CodeSystem does not know at all (undetermined).
	 */
	private void classifyProperties() {
		if (myFilters != null) {
			for (ValueSet.ConceptSetFilterComponent filter : myFilters) {
				if (filter.hasProperty()) {
					String filterProperty = filter.getProperty();
					StandardConceptProperty property = StandardConceptProperty.forFilterProperty(filterProperty.toLowerCase(Locale.ROOT));
					if (property != null) {
						valueSetStandardPropertiesUsedInFilters.add(property);
					} else if (!CODE_AND_DISPLAY_FILTER_PROPERTIES.contains(filterProperty)) {
						// Keep the code verbatim: custom property codes are case-sensitive.
						valueSetCustomPropertiesUsedInFilters.add(filterProperty);
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
		for (CodeSystem.PropertyComponent declaration : myCodeSystem.getProperty()) {
			if (valueSetCustomPropertiesUsedInFilters.contains(declaration.getCode())) {
				codeSystemDeclaredCustomProperties.add(declaration.getCode());
			}
		}
	}

	private CodeSystem.PropertyComponent findPropertyDeclarationByCode(String theCode) {
		for (CodeSystem.PropertyComponent property : myCodeSystem.getProperty()) {
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
				if (!property.hasValue()) {
					continue;
				}
				// Custom properties are indexed before the skips below: for them, "skipped" and "absent" are
				// different answers, and conflating the two fabricates a determined negative.
				if (isCustomPropertyOfInterest(property.getCode())) {
					indexCustomProperty(property, code);
					continue;
				}
				if (!property.getValue().isPrimitive()) {
					continue;
				}
				String value = property.getValue().primitiveValue();
				if (isBlank(value)) {
					continue;
				}
				indexStandardProperty(property.getCode(), code, value);
			}

			// 3) Index the "code" property
			codeSystemCodeAndDisplayIndex
					.computeIfAbsent("code", k -> new HashMap<>())
					.put(code, code);

			// 4) Index the "display" property
			codeSystemCodeAndDisplayIndex
					.computeIfAbsent("display", k -> new HashMap<>())
					.put(code, display);

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
	 * Whether this CodeSystem asserts that every concept it defines is present in the resource. That
	 * assertion is what makes evaluating a custom concept-property filter in memory both possible (every
	 * concept is here) and sound (a missing value is meaningful rather than merely unknown), so it gates all
	 * custom-property handling. Every other content mode behaves exactly as it did before.
	 */
	private boolean isContentComplete() {
		return myCodeSystem.getContent() == CodeSystemContentMode.COMPLETE;
	}

	private boolean isCustomPropertyOfInterest(String thePropertyCode) {
		return isContentComplete() && valueSetCustomPropertiesUsedInFilters.contains(thePropertyCode);
	}

	/**
	 * Index one custom concept-property value for later evaluation by {@link #passesCustomPropertyFilter}. A
	 * value that is not a primitive cannot be compared against the filter's string value, so the concept is
	 * recorded as carrying an unusable value rather than no value at all.
	 */
	private void indexCustomProperty(CodeSystem.ConceptPropertyComponent theProperty, String theConceptCode) {
		String value =
				theProperty.getValue().isPrimitive() ? theProperty.getValue().primitiveValue() : null;
		if (value == null) {
			codeSystemCustomPropertiesWithUnusableValue
					.computeIfAbsent(theProperty.getCode(), k -> new HashSet<>())
					.add(theConceptCode);
			return;
		}
		// A blank value is still a value: it simply will not equal the filter's, which is a determination we
		// can make rather than one we must decline.
		codeSystemCustomPropertyIndex
				.computeIfAbsent(theProperty.getCode(), k -> new HashMap<>())
				.computeIfAbsent(theConceptCode, k -> new HashSet<>())
				.add(value);
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
		if (valueSetStandardPropertiesUsedInFilters.contains(property)
				&& !canonicalUriDeclared.contains(property)
				&& loggedNameMatch.add(property)) {
			ourLog.info(
					"Concept property '{}' in CodeSystem '{}' is not declared with its canonical URI '{}'; matching by property code name.",
					property.code(),
					myCodeSystem.getUrl(),
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

	/**
	 * Evaluate a filter on a custom (non-standard) concept property. Only meaningful when the CodeSystem
	 * declares {@code content=complete} (see {@link #isContentComplete()}); otherwise the filter is still
	 * unsupported. Three outcomes are possible: the concept carries a value and the filter is evaluated
	 * against it; the concept carries no value but the CodeSystem declares the property, which under
	 * {@code complete} means the concept genuinely has none and so is not a member; or the property is
	 * unknown to the CodeSystem entirely, leaving membership undetermined.
	 *
	 * @throws UnsupportedFilterException if the content mode or operator puts this filter out of reach
	 * @throws UndeterminedFilterException if membership can be neither established nor refuted
	 */
	private boolean passesCustomPropertyFilter(
			ValueSet.ConceptSetFilterComponent theFilter, FhirVersionIndependentConcept theConcept) {
		if (!isContentComplete()) {
			throw unsupportedFilter(theFilter);
		}
		if (!CUSTOM_PROPERTY_OPERATORS.contains(theFilter.getOp())) {
			throw unsupportedFilter(theFilter);
		}

		String property = theFilter.getProperty();
		String conceptCode = theConcept.getCode();

		if (codeSystemCustomPropertiesWithUnusableValue
				.getOrDefault(property, Set.of())
				.contains(conceptCode)) {
			throw undeterminedFilter(
					theFilter, "the concept's value for it cannot be compared against the filter value");
		}

		Set<String> values = codeSystemCustomPropertyIndex
				.getOrDefault(property, Collections.emptyMap())
				.getOrDefault(conceptCode, Set.of());

		if (values.isEmpty()) {
			if (codeSystemDeclaredCustomProperties.contains(property)) {
				// 'complete' makes the absence meaningful: the concept has no value, so it is not a member.
				// This holds for every operator, including the negative ones - a concept with nothing to
				// compare is not admitted by a filter it was never measured against.
				return false;
			}
			throw undeterminedFilter(
					theFilter, "the CodeSystem neither declares it nor gives the concept a value for it");
		}

		return values.stream().anyMatch(value -> matchesCustomPropertyValue(theFilter, value));
	}

	private boolean matchesCustomPropertyValue(ValueSet.ConceptSetFilterComponent theFilter, String theValue) {
		String filterValue = theFilter.getValue();
		switch (theFilter.getOp()) {
			case EQUAL:
				return isEqualsWithOptionalCaseSensitive(filterValue, theValue);
			case IN:
				return csvFilterListContains(filterValue, theValue);
			case NOTIN:
				return !csvFilterListContains(filterValue, theValue);
			case REGEX:
				return matchesRegex(filterValue, theValue);
			default:
				throw unsupportedFilter(theFilter);
		}
	}

	/**
	 * Signal that a custom-property filter could not be evaluated for this concept, as opposed to evaluating
	 * to "not a member". See {@link UndeterminedFilterException} for why the two must stay apart.
	 */
	private static UndeterminedFilterException undeterminedFilter(
			ValueSet.ConceptSetFilterComponent theFilter, String theReason) {
		String property = theFilter.hasProperty() ? theFilter.getProperty() : "(none)";
		return new UndeterminedFilterException(Msg.code(3048)
				+ "In-memory ValueSet expansion cannot determine membership for the filter on property '"
				+ property + "': " + theReason);
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

	/**
	 * Thrown when a ValueSet filter references a concept property that cannot be resolved for the concept
	 * under evaluation, so membership can be neither established nor refuted (an <em>undetermined</em>
	 * result, as opposed to a determined negative). This is distinct from {@link UnsupportedFilterException}:
	 * the property/operator combination is one the in-memory expansion could evaluate, but the data needed to
	 * do so is absent. Callers should surface this as a {@code not-found} issue (recalculated by binding
	 * strength) rather than a fatal {@code vs-invalid}, so an undetermined check is never silently dropped.
	 */
	public static class UndeterminedFilterException extends RuntimeException {
		// Deliberately NOT a subclass of UnsupportedFilterException: callers catch that type and map it to a
		// fatal 'vs-invalid', which would swallow this signal and silently restore the behaviour this class
		// exists to prevent.
		private static final long serialVersionUID = 1L;

		public UndeterminedFilterException(String theMessage) {
			super(theMessage);
		}
	}
}
