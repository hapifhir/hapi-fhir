package ca.uhn.fhir.util;

/*-
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


import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utility for modifying with extensions in a FHIR version-independent approach.
 */
public class ExtensionUtil {

	/**
	 * Non instantiable
	 */
	private ExtensionUtil() {
		// nothing
	}

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
		IBaseExtension<?,?> extension = getExtensionByUrl(baseHasExtensions, theUrl);
		if (extension == null) {
			extension = baseHasExtensions.addExtension();
			extension.setUrl(theUrl);
		}
		return extension;
	}

	/**
	 * Returns an new empty extension.
	 *
	 * @param theBase Base resource to add the extension to
	 * @return Returns a new extension
	 * @throws IllegalArgumentException IllegalArgumentException is thrown in case resource doesn't support extensions
	 */
	public static IBaseExtension<?, ?> addExtension(IBase theBase) {
		return addExtension(theBase, null);
	}

	/**
	 * Returns an extension with the specified URL
	 *
	 * @param theBase Base resource to add the extension to
	 * @param theUrl  URL for the extension
	 * @return Returns a new extension with the specified URL.
	 * @throws IllegalArgumentException IllegalArgumentException is thrown in case resource doesn't support extensions
	 */
	public static IBaseExtension<?, ?> addExtension(IBase theBase, String theUrl) {
		IBaseHasExtensions baseHasExtensions = validateExtensionSupport(theBase);
		IBaseExtension<?,?> extension = baseHasExtensions.addExtension();
		if (theUrl != null) {
			extension.setUrl(theUrl);
		}
		return extension;
	}

	/**
	 * Adds an extension with the specified value
	 *
	 * @param theBase        The resource to update extension on
	 * @param theUrl         Extension URL
	 * @param theValueType   Type of the value to set in the extension
	 * @param theValue       Extension value
	 * @param theFhirContext The context containing FHIR resource definitions
	 */
	public static void addExtension(FhirContext theFhirContext, IBase theBase, String theUrl, String theValueType, Object theValue) {
		IBaseExtension<?,?> ext = addExtension(theBase, theUrl);
		setExtension(theFhirContext, ext, theValueType, theValue);
	}

	private static IBaseHasExtensions validateExtensionSupport(IBase theBase) {
		if (!(theBase instanceof IBaseHasExtensions)) {
			throw new IllegalArgumentException(Msg.code(1747) + String.format("Expected instance that supports extensions, but got %s", theBase));
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
		IBaseDatatype value = getExtensionByUrl(theBase, theExtensionUrl).getValue();
		if (value == null) {
			return theExtensionValue == null;
		}
		return value.toString().equals(theExtensionValue);
	}

	/**
	 * Gets the first extension with the specified URL
	 *
	 * @param theBase         The resource to get the extension for
	 * @param theExtensionUrl URL of the extension to get. Must be non-null
	 * @return Returns the first available extension with the specified URL, or null if such extension doesn't exist
	 */
	public static IBaseExtension<?, ?> getExtensionByUrl(IBase theBase, String theExtensionUrl) {
		Predicate<IBaseExtension<?,?>> filter;
		if (theExtensionUrl == null) {
			filter = (e -> true);
		} else {
			filter = (e -> theExtensionUrl.equals(e.getUrl()));
		}

		return getExtensionsMatchingPredicate(theBase, filter)
			.stream()
			.findFirst()
			.orElse(null);
	}

	/**
	 * Gets all extensions that match the specified filter predicate
	 *
	 * @param theBase   The resource to get the extension for
	 * @param theFilter Predicate to match the extension against
	 * @return Returns all extension with the specified URL, or an empty list if such extensions do not exist
	 */
	public static List<IBaseExtension<?, ?>> getExtensionsMatchingPredicate(IBase theBase, Predicate<? super IBaseExtension<?,?>> theFilter) {
		return validateExtensionSupport(theBase)
			.getExtension()
			.stream()
			.filter(theFilter)
			.collect(Collectors.toList());
	}

	/**
	 * Removes all extensions.
	 *
	 * @param theBase The resource to clear the extension for
	 * @return Returns all extension that were removed
	 */
	public static List<IBaseExtension<?, ?>> clearAllExtensions(IBase theBase) {
		return clearExtensionsMatchingPredicate(theBase, (e -> true));
	}

	/**
	 * Removes all extensions by URL.
	 *
	 * @param theBase The resource to clear the extension for
	 * @param theUrl  The url to clear extensions for
	 * @return Returns all extension that were removed
	 */
	public static List<IBaseExtension<?, ?>> clearExtensionsByUrl(IBase theBase, String theUrl) {
		return clearExtensionsMatchingPredicate(theBase, (e -> theUrl.equals(e.getUrl())));
	}

	/**
	 * Removes all extensions that match the specified predicate
	 *
	 * @param theBase   The base object to clear the extension for
	 * @param theFilter Defines which extensions should be cleared
	 * @return Returns all extension that were removed
	 */
	private static List<IBaseExtension<?, ?>> clearExtensionsMatchingPredicate(IBase theBase, Predicate<? super IBaseExtension<?,?>> theFilter) {
		List<IBaseExtension<?, ?>> retVal = getExtensionsMatchingPredicate(theBase, theFilter);
		validateExtensionSupport(theBase)
			.getExtension()
			.removeIf(theFilter);
		return retVal;
	}

	/**
	 * Gets all extensions with the specified URL
	 *
	 * @param theBase         The resource to get the extension for
	 * @param theExtensionUrl URL of the extension to get. Must be non-null
	 * @return Returns all extension with the specified URL, or an empty list if such extensions do not exist
	 */
	public static List<IBaseExtension<?, ?>> getExtensionsByUrl(IBaseHasExtensions theBase, String theExtensionUrl) {
		Predicate<IBaseExtension<?,?>> urlEqualityPredicate = e -> theExtensionUrl.equals(e.getUrl());
		return getExtensionsMatchingPredicate(theBase, urlEqualityPredicate);
	}

	/**
	 * Sets value of the extension as a string
	 *
	 * @param theExtension   The extension to set the value on
	 * @param theValue       The value to set
	 * @param theFhirContext The context containing FHIR resource definitions
	 */
	public static void setExtension(FhirContext theFhirContext, IBaseExtension<?,?> theExtension, String theValue) {
		setExtension(theFhirContext, theExtension, "string", theValue);
	}

	/**
	 * Sets value of the extension
	 *
	 * @param theExtension     The extension to set the value on
	 * @param theExtensionType Element type of the extension
	 * @param theValue         The value to set
	 * @param theFhirContext   The context containing FHIR resource definitions
	 */
	public static void setExtension(FhirContext theFhirContext, IBaseExtension<?,?> theExtension, String theExtensionType, Object theValue) {
		theExtension.setValue(TerserUtil.newElement(theFhirContext, theExtensionType, theValue));
	}

	/**
	 * Sets or replaces existing extension with the specified value as a string
	 *
	 * @param theBase        The resource to update extension on
	 * @param theUrl         Extension URL
	 * @param theValue       Extension value
	 * @param theFhirContext The context containing FHIR resource definitions
	 */
	public static void setExtensionAsString(FhirContext theFhirContext, IBase theBase, String theUrl, String theValue) {
		IBaseExtension<?,?> ext = getOrCreateExtension(theBase, theUrl);
		setExtension(theFhirContext, ext, theValue);
	}

	/**
	 * Sets or replaces existing extension with the specified value
	 *
	 * @param theBase        The resource to update extension on
	 * @param theUrl         Extension URL
	 * @param theValueType   Type of the value to set in the extension
	 * @param theValue       Extension value
	 * @param theFhirContext The context containing FHIR resource definitions
	 */
	public static void setExtension(FhirContext theFhirContext, IBase theBase, String theUrl, String theValueType, Object theValue) {
		IBaseExtension<?,?> ext = getOrCreateExtension(theBase, theUrl);
		setExtension(theFhirContext, ext, theValueType, theValue);
	}

	/**
	 * Compares two extensions, returns true if they have the same value and url
	 *
	 * @param theLeftExtension  : Extension to be evaluated #1
	 * @param theRightExtension : Extension to be evaluated #2
	 * @return Result of the comparison
	 */
	public static boolean equals(IBaseExtension<?,?> theLeftExtension, IBaseExtension<?,?> theRightExtension) {
		return TerserUtil.equals(theLeftExtension, theRightExtension);
	}
}
