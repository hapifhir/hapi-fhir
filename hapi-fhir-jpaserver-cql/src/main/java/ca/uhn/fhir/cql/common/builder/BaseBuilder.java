package ca.uhn.fhir.cql.common.builder;

/*-
 * #%L
 * HAPI FHIR JPA Server - Clinical Quality Language
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

/*
    These builders are based off of work performed by Philips Healthcare.
    I simplified their work with this generic base class and added/expanded builders.

    Tip of the hat to Philips Healthcare developer nly98977
*/

public class BaseBuilder<T> {

    protected T complexProperty;

    public BaseBuilder(T complexProperty) {
        this.complexProperty = complexProperty;
    }

    public T build() {
        return complexProperty;
    }
}
