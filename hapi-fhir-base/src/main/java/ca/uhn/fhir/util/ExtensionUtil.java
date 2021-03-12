package ca.uhn.fhir.util;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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


import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;

/**
 * Utility for modifying with extensions in a FHIR version-independent approach.
 */
public class ExtensionUtil {

	/**
	 * Returns an extension with the specified URL creating one if it doesn't exist.
	 *
	 * @param theBase Base resource to get extension from
	 * @param theUrl  URL for the extension
	 * @return Returns a extension with the specified URL.
	 * @throws IllegalArgumentException IllegalArgumentException is thrown in case resource doesn't support extensions
	 */
	public static IBaseExtension<?, ?> getOrCreateExtension(IBase theBase, String theUrl) {
		IBaseHasExtensions baseHasExtensions = validateExtensionSupport(theBase);
		IBaseExtension extension = getExtensionByUrl(baseHasExtensions, theUrl);
		if (extension == null) {
			extension = baseHasExtensions.addExtension();
			extension.setUrl(theUrl);
		}
		return extension;
	}

	private static IBaseHasExtensions validateExtensionSupport(IBase theBase) {
		if (!(theBase instanceof IBaseHasExtensions)) {
			throw new IllegalArgumentException(String.format("Expected instance that supports extensions, but got %s", theBase));
		}
		return (IBaseHasExtensions) theBase;
	}

	/**
	 * Checks if the specified instance has an extension with the specified URL
	 *
	 * @param theBase         The base resource to check extensions on
	 * @param theExtensionUrl URL of the extension
	 * @return Returns true if extension is exists and false otherwise
	 */
	public static boolean hasExtension(IBase theBase, String theExtensionUrl) {
		IBaseHasExtensions baseHasExtensions;
		try {
			baseHasExtensions = validateExtensionSupport(theBase);
		} catch (Exception e) {
			return false;
		}

		return getExtensionByUrl(baseHasExtensions, theExtensionUrl) != null;
	}

	/**
	 * Checks if the specified instance has an extension with the specified URL
	 *
	 * @param theBase         The base resource to check extensions on
	 * @param theExtensionUrl URL of the extension
	 * @return Returns true if extension is exists and false otherwise
	 */
	public static boolean hasExtension(IBase theBase, String theExtensionUrl, String theExtensionValue) {
		if (!hasExtension(theBase, theExtensionUrl)) {
			return false;
		}
		IBaseDatatype value = getExtensionByUrl((IBaseHasExtensions) theBase, theExtensionUrl).getValue();
		if (value == null) {
			return theExtensionValue == null;
		}
		return value.toString().equals(theExtensionValue);
	}

	private static IBaseExtension<?, ?> getExtensionByUrl(IBaseHasExtensions theBase, String theExtensionUrl) {
		return theBase.getExtension()
			.stream()
			.filter(e -> theExtensionUrl.equals(e.getUrl()))
			.findFirst()
			.orElse(null);
	}

	/**
	 * Sets value of the extension
	 *
	 * @param theExtension   The extension to set the value on
	 * @param theValue       The value to set
	 * @param theFhirContext The context containing FHIR resource definitions
	 */
	public static void setExtension(FhirContext theFhirContext, IBaseExtension theExtension, String theValue) {
		theExtension.setValue(TerserUtil.newElement(theFhirContext, "string", theValue));
	}

	/**
	 * Sets or replaces existing extension with the specified value
	 *
	 * @param theBase        The resource to update extension on
	 * @param theUrl         Extension URL
	 * @param theValue       Extension value
	 * @param theFhirContext The context containing FHIR resource definitions
	 */
	public static void setExtension(FhirContext theFhirContext, IBase theBase, String theUrl, String theValue) {
		IBaseExtension ext = getOrCreateExtension(theBase, theUrl);
		setExtension(theFhirContext, ext, theValue);
	}

}
