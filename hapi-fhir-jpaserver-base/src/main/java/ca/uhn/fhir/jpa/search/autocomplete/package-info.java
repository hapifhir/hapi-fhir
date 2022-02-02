/**
 * Search for distinct Coding elements by the display text, or by partial codes for a given SearchParameter.
 * <p>
 * Main entrypoint - {@link ca.uhn.fhir.jpa.search.autocomplete.TokenAutocompleteSearch#search}
 *
 * This work depends on on the Hibernate Search infrastructure in {@link ca.uhn.fhir.jpa.dao.search}.
 *
 * NIH sponsored this work to provide an interactive-autocomplete when browsing codes in a research dataset.
 *
 * https://gitlab.com/simpatico.ai/cdr/-/issues/2452
 * wipmb TODO-LIST
 * wipmb - docs - no partition support
 * wipmb - link to docs
 * wipmb what if the sp isn't of type token?  do we check, or discard results without tokens?
 *
 */
package ca.uhn.fhir.jpa.search.autocomplete;
