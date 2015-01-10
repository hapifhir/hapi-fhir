package ca.uhn.fhir.rest.annotation;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;

/**
 * RESTful method annotation to be used for the FHIR <a
 * href="http://hl7.org/implement/standards/fhir/http.html#tags">Tag
 * Operations</a> which have to do with getting tags.
 * <ul>
 * <li>
 * To return a global list of all tags on the server, this annotation should not
 * contain a {@link #type()} attribute, and the method should not have an ID or
 * Version ID parameter. On server implementations, the method must be defined
 * in a <a href=
 * "http://hl7api.sourceforge.net/hapi-fhir/doc_rest_server.html#plain_providers"
 * >plain provider</a>.</li>
 * <li>
 * To return a list of all tags on the server <b>for the given resource
 * type</b>, this annotation should contain a {@link #type()} attribute
 * specifying the resource type, and the method should not have an ID or Version
 * ID parameter. Note that for a server implementation, the {@link #type()}
 * annotation is optional if the method is defined in a <a href=
 * "http://hl7api.sourceforge.net/hapi-fhir/doc_rest_server.html#resource_providers"
 * >resource provider</a>, since the type is implied.</li>
 * <li>
 * To return a list of all tags on the server <b>for the given resource
 * instance</b>, this annotation should contain a {@link #type()} attribute
 * specifying the resource type, and the method should have a parameter of type
 * {@link IdDt} annotated with the {@link IdParam} annotation. Note that for a
 * server implementation, the {@link #type()} annotation is optional if the
 * method is defined in a <a href=
 * "http://hl7api.sourceforge.net/hapi-fhir/doc_rest_server.html#resource_providers"
 * >resource provider</a>, since the type is implied.</li>
 * <li>
 * To return a list of all tags on the server <b>for the given version of the
 * resource instance</b>, this annotation should contain a {@link #type()}
 * attribute specifying the resource type, and the method should have a
 * parameter of type {@link IdDt} annotated with the {@link VersionIdParam}
 * annotation, <b>and</b> a parameter of type {@link IdDt} annotated with the
 * {@link IdParam} annotation. Note that for a server implementation, the
 * {@link #type()} annotation is optional if the method is defined in a <a href=
 * "http://hl7api.sourceforge.net/hapi-fhir/doc_rest_server.html#resource_providers"
 * >resource provider</a>, since the type is implied.</li>
 * </ul>
 */
@Target(value= ElementType.METHOD)
@Retention(value=RetentionPolicy.RUNTIME)
public @interface GetTags {

	/**
	 * If set to a type other than the default (which is {@link IResource}
	 * , this method is expected to return a TagList containing only tags which
	 * are specific to the given resource type.
	 */
	Class<? extends IResource> type() default IResource.class;

}
