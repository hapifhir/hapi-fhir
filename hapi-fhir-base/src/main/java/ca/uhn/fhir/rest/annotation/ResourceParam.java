package ca.uhn.fhir.rest.annotation;

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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes a parameter for a REST method which will contain the resource actually
 * being created/updated/etc in an operation which contains a resource in the HTTP request.
 * <p>
 * For example, in a {@link Create} operation the method parameter annotated with this
 * annotation will contain the actual resource being created.
 * </p>
 * <p>
 * Parameters with this annotation should typically be of the type of resource being
 * operated on (see below for an exception when raw data is required). For example, in a
 * IResourceProvider for Patient resources, the parameter annotated with this
 * annotation should be of type Patient.
 * </p>
 * <p>
 * Note that in servers it is also acceptable to have parameters with this annotation
 * which are of type {@link String} or of type <code>byte[]</code>. Parameters of
 * these types will contain the raw/unparsed HTTP request body. It is fine to
 * have multiple parameters with this annotation, so you can have one parameter
 * which accepts the parsed resource, and another which accepts the raw request.
 * </p>
 * <p>
 * Also note that this parameter may be null if a client does not supply a body.
 * </p>
 */
@Target(value = ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceParam {

}
