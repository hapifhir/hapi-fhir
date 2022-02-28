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
 * - docs - no partition support
 * - link to docs
 * what if the sp isn't of type token?  do we check, or discard results without tokens?
 *
 */
package ca.uhn.fhir.jpa.search.autocomplete;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
