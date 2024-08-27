/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package org.hl7.fhir.instance.model.api;

import java.util.List;

/**
 * @param <T> The actual concrete type of the extension
 * @param <D> Note that this type param is not used anywhere - It is kept only to avoid making a breaking change
 */
// public interface IBaseExtension<T extends IBaseExtension<T, D>, D> extends ICompositeType {
public interface IBaseExtension<T, D> extends ICompositeType {

	List<T> getExtension();

	String getUrl();

	IBaseDatatype getValue();

	T setUrl(String theUrl);

	T setValue(IBaseDatatype theValue);
}
