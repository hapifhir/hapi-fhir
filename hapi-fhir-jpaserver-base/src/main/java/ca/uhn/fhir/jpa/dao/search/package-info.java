/**
 * <h1>Extended fhir indexing for Hibernate Search using Lucene/Elasticsearch.
 * <p>
 * By default, Lucene indexing only provides support for _text, and _content search parameters using
 * {@link ca.uhn.fhir.jpa.model.entity.ResourceTable#myNarrativeText} and
 * {@link ca.uhn.fhir.jpa.model.entity.ResourceTable#myContentText}.
 * This package extends this search to support token, string, and reference parameters via {@link ca.uhn.fhir.jpa.model.entity.ResourceTable#myLuceneIndexData}.
 * When active, the extracted search parameters which are written to the HFJ_SPIDX_* tables are also written to the Lucene index document.
 * For now, we use the existing JPA index entities to populate the {@link ca.uhn.fhir.jpa.model.search.ExtendedLuceneIndexData}
 * in {@link ca.uhn.fhir.jpa.dao.search.ExtendedLuceneIndexExtractor#extract(org.hl7.fhir.instance.model.api.IBaseResource, ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams)} ()}
 *
 * <h2>Implementation</h2>
 * Both {@link ca.uhn.fhir.jpa.search.builder.SearchBuilder} and {@link ca.uhn.fhir.jpa.dao.LegacySearchBuilder} delegate the
 * search to {@link ca.uhn.fhir.jpa.dao.FulltextSearchSvcImpl#doSearch} when active.
 * The fulltext search runs first and interprets any search parameters it understands, returning a pid list.
 * This pid list is used as a narrowing where clause against the remaining unprocessed search parameters in a jdbc query.
 * The actual queries for the different search types (e.g. token, string, modifiers, etc.) are
 * generated in {@link ca.uhn.fhir.jpa.dao.search.ExtendedLuceneSearchBuilder}.
 * <p>
 *    Full resource bodies can be stored in the Hibernate Search index.
 *    The {@link ca.uhn.fhir.jpa.dao.search.ExtendedLuceneResourceProjection} is used to extract these.
 *    This is currently restricted to LastN, and misses tag changes from $meta-add and $meta-delete since those don't
 *    update Hibernate Search.
 * </p>
 *
 * <h2>Operation</h2>
 * During startup, Hibernate Search uses {@link ca.uhn.fhir.jpa.model.search.SearchParamTextPropertyBinder} to generate a schema.
 *
 * @see ca.uhn.fhir.jpa.model.search.ExtendedLuceneIndexData
 * @see ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter
 * @see ca.uhn.fhir.jpa.dao.search.ExtendedLuceneSearchBuilder
 * @see ca.uhn.fhir.jpa.model.search.SearchParamTextPropertyBinder
 *
 * Activated by {@link ca.uhn.fhir.jpa.api.config.DaoConfig#setAdvancedLuceneIndexing(boolean)}.
 */
package ca.uhn.fhir.jpa.dao.search;

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
