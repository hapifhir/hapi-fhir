/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.util;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class MetaUtil {
	private static final Logger ourLog = LoggerFactory.getLogger(MetaUtil.class);

	private MetaUtil() {
		// non-instantiable
	}

	public static String cleanProvenanceSourceUriOrEmpty(String theProvenanceSourceUri) {
		String sanitizedProvenance = defaultString(theProvenanceSourceUri);
		return StringUtils.substringBefore(sanitizedProvenance, "#");
	}

	public static String getSource(FhirContext theContext, IBaseMetaType theMeta) {
		if (theContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.R4)) {
			return getSourceR4Plus(theContext, theMeta);
		} else if (theContext.getVersion().getVersion().equals(FhirVersionEnum.DSTU3)) {
			return getSourceDstu3((IBaseHasExtensions) theMeta);
		} else {
			throw new UnsupportedOperationException(
					Msg.code(1782) + MetaUtil.class.getSimpleName() + ".getSource() not supported on FHIR Version "
							+ theContext.getVersion().getVersion());
		}
	}

	private static String getSourceDstu3(IBaseHasExtensions theMeta) {
		IBaseHasExtensions metaWithExtensions = theMeta;
		List<? extends IBaseExtension<?, ?>> extensions = metaWithExtensions.getExtension();
		for (IBaseExtension extension : extensions) {
			if (HapiExtensions.EXT_META_SOURCE.equals(extension.getUrl())) {
				IPrimitiveType<String> value = (IPrimitiveType<String>) extension.getValue();
				return value.getValueAsString();
			}
		}
		return null;
	}

	private static String getSourceR4Plus(FhirContext theFhirContext, IBaseMetaType theMeta) {
		BaseRuntimeElementCompositeDefinition<?> elementDef =
				(BaseRuntimeElementCompositeDefinition<?>) theFhirContext.getElementDefinition(theMeta.getClass());
		BaseRuntimeChildDefinition sourceChild = elementDef.getChildByName("source");
		if (sourceChild == null) {
			return null;
		}
		List<IBase> sourceValues = sourceChild.getAccessor().getValues(theMeta);
		String retVal = null;
		if (sourceValues.size() > 0) {
			retVal = ((IPrimitiveType<?>) sourceValues.get(0)).getValueAsString();
		}
		return retVal;
	}

	public static <R extends IBaseResource> void populateResourceSource(
			FhirContext theFhirContext, String theProvenanceSourceUri, String theProvenanceRequestId, R theRetVal) {
		String sourceString = cleanProvenanceSourceUriOrEmpty(theProvenanceSourceUri);
		if (isNotBlank(theProvenanceRequestId)) {
			sourceString = sourceString + "#" + theProvenanceRequestId;
		}

		if (isNotBlank(sourceString)) {
			setSource(theFhirContext, theRetVal, sourceString);
		}
	}

	/**
	 * Sets the value for <code>Resource.meta.source</code> for R4+ resources, and places the value in
	 * an extension on <code>Resource.meta</code>
	 * with the URL <code>http://hapifhir.io/fhir/StructureDefinition/resource-meta-source</code> for DSTU3.
	 *
	 * @param theContext  The FhirContext object
	 * @param theResource The resource to modify
	 * @param theValue    The source URI
	 * @see <a href="http://hl7.org/fhir/resource-definitions.html#Resource.meta">Meta.source</a>
	 */
	@SuppressWarnings("unchecked")
	public static void setSource(FhirContext theContext, IBaseResource theResource, String theValue) {
		if (theContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.R4)) {
			MetaUtil.setSource(theContext, theResource.getMeta(), theValue);
		} else if (theContext.getVersion().getVersion().equals(FhirVersionEnum.DSTU3)) {
			IBaseExtension<?, ?> sourceExtension = ((IBaseHasExtensions) theResource.getMeta()).addExtension();
			sourceExtension.setUrl(HapiExtensions.EXT_META_SOURCE);
			IPrimitiveType<String> value = (IPrimitiveType<String>)
					theContext.getElementDefinition("uri").newInstance();
			value.setValue(theValue);
			sourceExtension.setValue(value);
		} else {
			ourLog.debug(MetaUtil.class.getSimpleName() + ".setSource() not supported on FHIR Version "
					+ theContext.getVersion().getVersion());
		}
	}

	public static void setSource(FhirContext theContext, IBaseMetaType theMeta, String theValue) {
		BaseRuntimeElementCompositeDefinition<?> elementDef =
				(BaseRuntimeElementCompositeDefinition<?>) theContext.getElementDefinition(theMeta.getClass());
		BaseRuntimeChildDefinition sourceChild = elementDef.getChildByName("source");
		List<IBase> sourceValues = sourceChild.getAccessor().getValues(theMeta);
		IPrimitiveType<?> sourceElement;
		if (sourceValues.size() > 0) {
			sourceElement = ((IPrimitiveType<?>) sourceValues.get(0));
		} else {
			sourceElement =
					(IPrimitiveType<?>) theContext.getElementDefinition("uri").newInstance();
			sourceChild.getMutator().setValue(theMeta, sourceElement);
		}
		sourceElement.setValueAsString(theValue);
	}

	public static Set<String> getAutoVersionReferencesAtPath(IBaseMetaType theMeta, String theResourceType) {
		return ExtensionUtil.getExtensionPrimitiveValues(
						theMeta, HapiExtensions.EXTENSION_AUTO_VERSION_REFERENCES_AT_PATH)
				.stream()
				.map(path -> String.format("%s.%s", theResourceType, path))
				.collect(Collectors.toSet());
	}
}
