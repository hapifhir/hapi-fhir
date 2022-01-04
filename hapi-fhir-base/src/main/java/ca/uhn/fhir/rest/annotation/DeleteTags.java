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

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.primitive.IdDt;

/**
 * RESTful method annotation to be used for the FHIR <a
 * href="http://hl7.org/implement/standards/fhir/http.html#tags">Tag
 * Operations</a> which have to do with deleting tags.
 * <ul>
 * <li>
 * To delete tag(s) <b>to the given resource
 * instance</b>, this annotation should contain a {@link #type()} attribute
 * specifying the resource type, and the method should have a parameter of type
 * {@link IdDt} annotated with the {@link IdParam} annotation, as well as 
 * a parameter of type {@link TagList} which will contain the list of tags
 * to be deleted.
 * Note that for a
 * server implementation, the {@link #type()} annotation is optional if the
 * method is defined in a <a href=
 * "https://hapifhir.io/hapi-fhir/docs/server_plain/resource_providers.html"
 * >resource provider</a>, since the type is implied.</li>
 * <li>
 * To delete tag(s) on the server <b>to the given version of the
 * resource instance</b>, this annotation should contain a {@link #type()}
 * attribute specifying the resource type, and the method should have a
 * parameter of type {@link IdDt} annotated with the {@link VersionIdParam}
 * annotation, <b>and</b> a parameter of type {@link IdDt} annotated with the
 * {@link IdParam} annotation, as well as 
 * a parameter of type {@link TagList} which will contain the list of tags
 * to be deleted.
 * Note that for a server implementation, the
 * {@link #type()} annotation is optional if the method is defined in a <a href=
 * "https://hapifhir.io/hapi-fhir/docs/server_plain/resource_providers.html"
 * >resource provider</a>, since the type is implied.</li>
 * </ul>
 */
@Target(value= ElementType.METHOD)
@Retention(value=RetentionPolicy.RUNTIME)
public @interface DeleteTags {

	/**
	 * If set to a type other than the default (which is {@link IResource}
	 * , this method is expected to return a TagList containing only tags which
	 * are specific to the given resource type.
	 */
	Class<? extends IBaseResource> type() default IBaseResource.class;

	/**
	 * This method allows the return type for this method to be specified in a
	 * non-type-specific way, using the text name of the resource, e.g. "Patient".
	 *
	 * This attribute should be populate, or {@link #type()} should be, but not both.
	 *
	 * @since 5.4.0
	 */
	String typeName() default "";

}
