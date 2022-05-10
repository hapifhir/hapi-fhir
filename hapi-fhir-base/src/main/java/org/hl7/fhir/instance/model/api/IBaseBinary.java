package org.hl7.fhir.instance.model.api;

/*
 * #%L
 * HAPI FHIR - Core Library
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


public interface IBaseBinary extends IBaseResource {

	byte[] getContent();

	IPrimitiveType<byte[]> getContentElement();

	String getContentAsBase64();

	String getContentType();

	IBaseBinary setContent(byte[] theContent);

	IBaseBinary setContentAsBase64(String theContent);

	IBaseBinary setContentType(String theContentType);

	default boolean hasData() { return getContent() != null && getContent().length > 0; };

}
