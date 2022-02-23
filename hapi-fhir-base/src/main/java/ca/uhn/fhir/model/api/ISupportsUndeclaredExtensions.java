package ca.uhn.fhir.model.api;

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

import org.hl7.fhir.instance.model.api.IBaseDatatype;

import java.util.List;

public interface ISupportsUndeclaredExtensions extends IElement {
	
	/**
	 * Returns a list containing all undeclared non-modifier extensions. The returned list
	 * is mutable, so it may be modified (e.g. to add or remove an extension).
	 */
	List<ExtensionDt> getUndeclaredExtensions();

	/**
	 * Returns an <b>immutable</b> list containing all undeclared extensions (modifier and non-modifier) by extension URL
	 * 
	 * @see #getUndeclaredExtensions() To return a mutable list which may be used to remove extensions
	 */
	List<ExtensionDt> getUndeclaredExtensionsByUrl(String theUrl);

	/**
	 * Returns an <b>immutable</b> list containing all extensions (modifier and non-modifier).
	 * 
	 * @see #getUndeclaredExtensions() To return a mutable list which may be used to remove undeclared non-modifier extensions
	 * @see #getUndeclaredModifierExtensions() To return a mutable list which may be used to remove undeclared modifier extensions
	 */
	List<ExtensionDt> getAllUndeclaredExtensions();

	/**
	 * Returns a list containing all undeclared modifier extensions. The returned list
	 * is mutable, so it may be modified (e.g. to add or remove an extension).
	 */
	List<ExtensionDt> getUndeclaredModifierExtensions();
	
	/**
	 * Adds an extension to this object. This extension should have the
	 * following properties set:
	 * <ul>
	 * <li>{@link ExtensionDt#setModifier(boolean) Is Modifier}</li>
	 * <li>{@link ExtensionDt#setUrl(String) URL}</li>
	 * <li>And one of:
	 * <ul>
	 * <li>{@link ExtensionDt#setValue(IBaseDatatype) A datatype value}</li>
	 * <li>{@link #addUndeclaredExtension(ExtensionDt) Further sub-extensions}</li>
	 * </ul>
	 * </ul> 
	 * 
	 * @param theExtension The extension to add. Can not be null.
	 */
	void addUndeclaredExtension(ExtensionDt theExtension);
	
	/**
	 * Adds an extension to this object
	 * 
	 * @see #getUndeclaredExtensions() To return a mutable list which may be used to remove extensions
	 */
	ExtensionDt addUndeclaredExtension(boolean theIsModifier, String theUrl, IBaseDatatype theValue);

	/**
	 * Adds an extension to this object. This method is intended for use when
	 * an extension is being added which will contain child extensions, as opposed to
	 * a datatype.
	 * 
	 * @see #getUndeclaredExtensions() To return a mutable list which may be used to remove extensions
	 */
	ExtensionDt addUndeclaredExtension(boolean theIsModifier, String theUrl);

}
