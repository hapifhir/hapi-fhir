/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
 * Implements the Adapter pattern to allow external classes to extend/adapt existing classes.
 * Useful for extending interfaces that are closed to modification, or restricted for classpath reasons.
 * <p>
 *     For clients, the main entry point is {@link ca.uhn.fhir.util.adapters.AdapterUtils#adapt(java.lang.Object, java.lang.Class)}
 *     which will attempt to cast to the target type, or build an adapter of the target type.
 * </p>
 * <p>
 *     For implementors, you can support adaptation via two mechanisms:
 *     <ul>
 *         <li>by implementing {@link ca.uhn.fhir.util.adapters.IAdaptable} directly on a class to provide supported adapters,
 *         <li>or when the class is closed to direct modification, you can implement
 *         an instance of {@link ca.uhn.fhir.util.adapters.IAdapterFactory} and register
 *         it with the public {@link ca.uhn.fhir.util.adapters.AdapterManager#INSTANCE}.</li>
 *     </ul>
 *     The AdapterUtils.adapt() supports both of these.
 * </p>
 * Inspired by the Eclipse runtime.
 */
package ca.uhn.fhir.util.adapters;
