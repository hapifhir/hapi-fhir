/**
 * Search for distinct Coding elements by the display text, or by partial codes for a given SearchParameter.
 * <p>
 * Main entrypoint - {@link ca.uhn.fhir.jpa.search.autocomplete.TokenAutocompleteSearch#search}
 *
 * This work depends on on the Hibernate Search infrastructure in {@link ca.uhn.fhir.jpa.dao.search}.
 *
 * NIH sponsored this work to provide an interactive-autocomplete when browsing codes in a research dataset.
 *
 * wipmb https://gitlab.com/simpatico.ai/cdr/-/issues/2452
 */
package ca.uhn.fhir.jpa.search.autocomplete;
