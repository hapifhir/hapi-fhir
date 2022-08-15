package ca.uhn.fhir.model.api.annotation;

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
 * Field modifier to be placed on a child field (a field also annotated with the {@link Child} annotation) which
 * indicates that this field is an extension.
 */
@Target(value = { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Extension {

	/**
	 * This parameter affects how the extension is treated when the element definition containing this resource is
	 * exported to a profile.
	 * 
	 * <p>
	 * If set to <b><code>true</code></b>, the resource is taken to be a local resource and its definition is exported
	 * along with the reference. Use this option for extension defintions that you have added locally (i.e. within your
	 * own organization)
	 * </p>
	 * 
	 * <p>
	 * If set to <b><code>false</code></b>, the resource is taken to be a remote resource and its definition is
	 * <b>not</b> exported to the profile. Use this option for extensions that are defined by other organizations (i.e.
	 * by regional authorities or jurisdictional governments)
	 * </p>
	 */
	boolean definedLocally() default true;

	/**
	 * Returns <code>true</code> if this extension is a <a
	 * href="http://www.hl7.org/implement/standards/fhir/extensibility.html#modifierExtension">modifier extension</a>
	 */
	boolean isModifier() default false;

	/**
	 * The URL associated with this extension
	 */
	String url();

}
