/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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
package ca.uhn.fhir.jpa.provider.r4;

/**
 * @deprecated This class stood as a near-duplicate of
 *    {@link ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test}, which is now the only
 *    implementation; extend that one instead. This subclass carries no behaviour of its own and
 *    exists so that code already extending this name keeps compiling for one release.
 */
@Deprecated(forRemoval = true)
// Created by claude-opus-5
public abstract class BaseResourceProviderR4Test extends ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test {}
