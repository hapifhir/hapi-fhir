/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
/**
 * This package contains the "bulk modification" job framework. Jobs may extend this if
 * they make changes to a collection of resources. There are two kinds of jobs that
 * can be created:
 * <ul>
 *     <li><b>Bulk Modify Jobs</b>: These jobs modify the current version of a resource, creating
 *     a new version if any changes are actually made.</li>
 *     <li><b>Bulk Rewrite Jobs</b>: These jobs modify all versions of a resource, rewriting history
 *     without creating a new version.</li>
 * </ul>
 *
 * TODO:
 * - Ability to save the output as bundles instead of writing directly to the resource tables
 * - Make retry behaviour in BaseBulkModifyResourcesStep configurable
 * - Derive "bulk consumption" framework and use it for subscription triggering, RTE submit, etc
 */
package ca.uhn.fhir.batch2.jobs.bulkmodify;
