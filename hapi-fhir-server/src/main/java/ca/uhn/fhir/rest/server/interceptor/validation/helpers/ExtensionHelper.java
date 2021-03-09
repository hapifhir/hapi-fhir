package ca.uhn.fhir.rest.server.interceptor.validation.helpers;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;

public class ExtensionHelper {

	/**
	 * Returns an extension with the specified URL creating one if it doesn't exist.
	 *
	 * @param theBase Base resource to get extension from
	 * @param theUrl  URL for the extension
	 * @return Returns a extension with the specified URL.
	 * @throws IllegalArgumentException IllegalArgumentException is thrown in case resource doesn't support extensions
	 */
	public IBaseExtension<?, ?> getOrCreateExtension(IBase theBase, String theUrl) {
		IBaseHasExtensions baseHasExtensions = validateExtensionSupport(theBase);
		IBaseExtension extension = getExtension(baseHasExtensions, theUrl);
		if (extension == null) {
			extension = baseHasExtensions.addExtension();
			extension.setUrl(theUrl);
		}
		return extension;
	}

	private IBaseHasExtensions validateExtensionSupport(IBase theBase) {
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
	public boolean hasExtension(IBase theBase, String theExtensionUrl) {
		IBaseHasExtensions baseHasExtensions;
		try {
			baseHasExtensions = validateExtensionSupport(theBase);
		} catch (Exception e) {
			return false;
		}

		return getExtension(baseHasExtensions, theExtensionUrl) != null;
	}

	/**
	 * Checks if the specified instance has an extension with the specified URL
	 *
	 * @param theBase         The base resource to check extensions on
	 * @param theExtensionUrl URL of the extension
	 * @return Returns true if extension is exists and false otherwise
	 */
	public boolean hasExtension(IBase theBase, String theExtensionUrl, String theExtensionValue) {
		if (!hasExtension(theBase, theExtensionUrl)) {
			return false;
		}
		IBaseDatatype value = getExtension((IBaseHasExtensions) theBase, theExtensionUrl).getValue();
		if (value == null) {
			return theExtensionValue == null;
		}
		return value.toString().equals(theExtensionValue);
	}

	private IBaseExtension<?, ?> getExtension(IBaseHasExtensions theBase, String theExtensionUrl) {
		return theBase.getExtension()
			.stream()
			.filter(e -> theExtensionUrl.equals(e.getUrl()))
			.findFirst()
			.orElse(null);
	}

	public void setValue(IBaseExtension theExtension, String theValue, FhirContext theFhirContext) {
		theExtension.setValue(newString(theValue, theFhirContext));
	}

	public void setValue(IBase theBase, String theUrl, String theValue, FhirContext theFhirContext) {
		IBaseExtension ext = getOrCreateExtension(theBase, theUrl);
		setValue(ext, theValue, theFhirContext);
	}

	public IBaseDatatype newString(String theValue, FhirContext theFhirContext) {
		BaseRuntimeElementDefinition<?> def = theFhirContext.getElementDefinition("string");
		return (IBaseDatatype) def.newInstance(theValue);
	}

}
