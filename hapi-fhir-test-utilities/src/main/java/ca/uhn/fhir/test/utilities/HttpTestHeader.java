/*-
 * #%L
 * HAPI FHIR Test Utilities
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
package ca.uhn.fhir.test.utilities;

/**
 * One HTTP header, independent of any HTTP client library and of the direction it travels in.
 * Headers a test sets on an {@link HttpTestRequest} and headers it reads back off an
 * {@link HttpTestResponse} are the same shape, so they share one type rather than the request half
 * of the API borrowing a type from the response half.
 */
// Created by claude-opus-5
public record HttpTestHeader(String name, String value) {}
