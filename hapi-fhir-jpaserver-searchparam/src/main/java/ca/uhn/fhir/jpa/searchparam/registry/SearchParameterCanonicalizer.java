/*-
 * #%L
 * HAPI FHIR JPA - Search Parameters
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
package ca.uhn.fhir.jpa.searchparam.registry;

import ca.uhn.fhir.context.ComboSearchParamType;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.phonetic.IPhoneticEncoder;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.DatatypeUtil;
import ca.uhn.fhir.util.ExtensionUtil;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.PhoneticEncoderUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.SearchParameter;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.startsWith;

@Service
public class SearchParameterCanonicalizer {
	private static final Logger ourLog = LoggerFactory.getLogger(SearchParameterCanonicalizer.class);

	private final FhirContext myFhirContext;
	private final FhirTerser myTerser;

	@Autowired
	public SearchParameterCanonicalizer(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
		myTerser = myFhirContext.newTerser();
	}

	private static Collection<String> toStrings(Collection<? extends IPrimitiveType<String>> theBase) {
		HashSet<String> retVal = new HashSet<>();
		for (IPrimitiveType<String> next : theBase) {
			if (isNotBlank(next.getValueAsString())) {
				retVal.add(next.getValueAsString());
			}
		}
		return retVal;
	}

	public RuntimeSearchParam canonicalizeSearchParameter(IBaseResource theSearchParameter) {
		RuntimeSearchParam retVal;
		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU2:
				retVal = canonicalizeSearchParameterDstu2(
						(ca.uhn.fhir.model.dstu2.resource.SearchParameter) theSearchParameter);
				break;
			case DSTU3:
				retVal =
						canonicalizeSearchParameterDstu3((org.hl7.fhir.dstu3.model.SearchParameter) theSearchParameter);
				break;
			case R4:
			case R4B:
			case R5:
				retVal = canonicalizeSearchParameterR4Plus(theSearchParameter);
				break;
			case DSTU2_HL7ORG:
			case DSTU2_1:
				// Non-supported - these won't happen so just fall through
			default:
				throw new InternalErrorException(
						Msg.code(510) + "SearchParameter canonicalization not supported for FHIR version"
								+ myFhirContext.getVersion().getVersion());
		}

		if (retVal != null) {
			extractExtensions(theSearchParameter, retVal);
		}

		return retVal;
	}

	private RuntimeSearchParam canonicalizeSearchParameterDstu2(
			ca.uhn.fhir.model.dstu2.resource.SearchParameter theNextSp) {
		String name = theNextSp.getCode();
		String description = theNextSp.getDescription();
		String path = theNextSp.getXpath();

		Collection<String> baseResource = toStrings(Collections.singletonList(theNextSp.getBaseElement()));
		List<String> baseCustomResources = extractDstu2CustomResourcesFromExtensions(
				theNextSp, HapiExtensions.EXTENSION_SEARCHPARAM_CUSTOM_BASE_RESOURCE);

		if (!baseCustomResources.isEmpty()) {
			baseResource = Collections.singleton(baseCustomResources.get(0));
		}

		RestSearchParameterTypeEnum paramType = null;
		RuntimeSearchParam.RuntimeSearchParamStatusEnum status = null;
		if (theNextSp.getTypeElement().getValueAsEnum() != null) {
			switch (theNextSp.getTypeElement().getValueAsEnum()) {
				case COMPOSITE:
					paramType = RestSearchParameterTypeEnum.COMPOSITE;
					break;
				case DATE_DATETIME:
					paramType = RestSearchParameterTypeEnum.DATE;
					break;
				case NUMBER:
					paramType = RestSearchParameterTypeEnum.NUMBER;
					break;
				case QUANTITY:
					paramType = RestSearchParameterTypeEnum.QUANTITY;
					break;
				case REFERENCE:
					paramType = RestSearchParameterTypeEnum.REFERENCE;
					break;
				case STRING:
					paramType = RestSearchParameterTypeEnum.STRING;
					break;
				case TOKEN:
					paramType = RestSearchParameterTypeEnum.TOKEN;
					break;
				case URI:
					paramType = RestSearchParameterTypeEnum.URI;
					break;
			}
		}
		if (theNextSp.getStatus() != null) {
			switch (theNextSp.getStatusElement().getValueAsEnum()) {
				case ACTIVE:
					status = RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE;
					break;
				case DRAFT:
					status = RuntimeSearchParam.RuntimeSearchParamStatusEnum.DRAFT;
					break;
				case RETIRED:
					status = RuntimeSearchParam.RuntimeSearchParamStatusEnum.RETIRED;
					break;
			}
		}

		Set<String> targetResources = DatatypeUtil.toStringSet(theNextSp.getTarget());
		List<String> targetCustomResources = extractDstu2CustomResourcesFromExtensions(
				theNextSp, HapiExtensions.EXTENSION_SEARCHPARAM_CUSTOM_TARGET_RESOURCE);

		maybeAddCustomResourcesToResources(targetResources, targetCustomResources);

		if (isBlank(name) || isBlank(path)) {
			if (paramType != RestSearchParameterTypeEnum.COMPOSITE) {
				return null;
			}
		}

		IIdType id = theNextSp.getIdElement();
		String uri = "";
		ComboSearchParamType unique = null;

		List<ExtensionDt> uniqueExts = theNextSp.getUndeclaredExtensionsByUrl(HapiExtensions.EXT_SP_UNIQUE);
		if (uniqueExts.size() > 0) {
			IPrimitiveType<?> uniqueExtsValuePrimitive = uniqueExts.get(0).getValueAsPrimitive();
			if (uniqueExtsValuePrimitive != null) {
				if ("true".equalsIgnoreCase(uniqueExtsValuePrimitive.getValueAsString())) {
					unique = ComboSearchParamType.UNIQUE;
				} else if ("false".equalsIgnoreCase(uniqueExtsValuePrimitive.getValueAsString())) {
					unique = ComboSearchParamType.NON_UNIQUE;
				}
			}
		}

		List<RuntimeSearchParam.Component> components = Collections.emptyList();
		return new RuntimeSearchParam(
				id,
				uri,
				name,
				description,
				path,
				paramType,
				Collections.emptySet(),
				targetResources,
				status,
				unique,
				components,
				baseResource);
	}

	private RuntimeSearchParam canonicalizeSearchParameterDstu3(org.hl7.fhir.dstu3.model.SearchParameter theNextSp) {
		String name = theNextSp.getCode();
		String description = theNextSp.getDescription();
		String path = theNextSp.getExpression();

		List<String> baseResources = new ArrayList<>(toStrings(theNextSp.getBase()));
		List<String> baseCustomResources = extractDstu3CustomResourcesFromExtensions(
				theNextSp, HapiExtensions.EXTENSION_SEARCHPARAM_CUSTOM_BASE_RESOURCE);

		maybeAddCustomResourcesToResources(baseResources, baseCustomResources);

		RestSearchParameterTypeEnum paramType = null;
		RuntimeSearchParam.RuntimeSearchParamStatusEnum status = null;
		if (theNextSp.getType() != null) {
			switch (theNextSp.getType()) {
				case COMPOSITE:
					paramType = RestSearchParameterTypeEnum.COMPOSITE;
					break;
				case DATE:
					paramType = RestSearchParameterTypeEnum.DATE;
					break;
				case NUMBER:
					paramType = RestSearchParameterTypeEnum.NUMBER;
					break;
				case QUANTITY:
					paramType = RestSearchParameterTypeEnum.QUANTITY;
					break;
				case REFERENCE:
					paramType = RestSearchParameterTypeEnum.REFERENCE;
					break;
				case STRING:
					paramType = RestSearchParameterTypeEnum.STRING;
					break;
				case TOKEN:
					paramType = RestSearchParameterTypeEnum.TOKEN;
					break;
				case URI:
					paramType = RestSearchParameterTypeEnum.URI;
					break;
				case NULL:
					break;
			}
		}
		if (theNextSp.getStatus() != null) {
			switch (theNextSp.getStatus()) {
				case ACTIVE:
					status = RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE;
					break;
				case DRAFT:
					status = RuntimeSearchParam.RuntimeSearchParamStatusEnum.DRAFT;
					break;
				case RETIRED:
					status = RuntimeSearchParam.RuntimeSearchParamStatusEnum.RETIRED;
					break;
				case UNKNOWN:
					status = RuntimeSearchParam.RuntimeSearchParamStatusEnum.UNKNOWN;
					break;
				case NULL:
					break;
			}
		}

		Set<String> targetResources = DatatypeUtil.toStringSet(theNextSp.getTarget());
		List<String> targetCustomResources = extractDstu3CustomResourcesFromExtensions(
				theNextSp, HapiExtensions.EXTENSION_SEARCHPARAM_CUSTOM_TARGET_RESOURCE);

		maybeAddCustomResourcesToResources(targetResources, targetCustomResources);

		if (isBlank(name) || isBlank(path) || paramType == null) {
			if (paramType != RestSearchParameterTypeEnum.COMPOSITE) {
				return null;
			}
		}

		IIdType id = theNextSp.getIdElement();
		String uri = "";
		ComboSearchParamType unique = null;

		List<Extension> uniqueExts = theNextSp.getExtensionsByUrl(HapiExtensions.EXT_SP_UNIQUE);
		if (uniqueExts.size() > 0) {
			IPrimitiveType<?> uniqueExtsValuePrimitive = uniqueExts.get(0).getValueAsPrimitive();
			if (uniqueExtsValuePrimitive != null) {
				if ("true".equalsIgnoreCase(uniqueExtsValuePrimitive.getValueAsString())) {
					unique = ComboSearchParamType.UNIQUE;
				} else if ("false".equalsIgnoreCase(uniqueExtsValuePrimitive.getValueAsString())) {
					unique = ComboSearchParamType.NON_UNIQUE;
				}
			}
		}

		List<RuntimeSearchParam.Component> components = new ArrayList<>();
		for (SearchParameter.SearchParameterComponentComponent next : theNextSp.getComponent()) {
			components.add(new RuntimeSearchParam.Component(
					next.getExpression(),
					next.getDefinition()
							.getReferenceElement()
							.toUnqualifiedVersionless()
							.getValue()));
		}

		return new RuntimeSearchParam(
				id,
				uri,
				name,
				description,
				path,
				paramType,
				Collections.emptySet(),
				targetResources,
				status,
				unique,
				components,
				baseResources);
	}

	private RuntimeSearchParam canonicalizeSearchParameterR4Plus(IBaseResource theNextSp) {

		String name = myTerser.getSinglePrimitiveValueOrNull(theNextSp, "code");
		String description = myTerser.getSinglePrimitiveValueOrNull(theNextSp, "description");
		String path = myTerser.getSinglePrimitiveValueOrNull(theNextSp, "expression");

		Set<String> baseResources = extractR4PlusResources("base", theNextSp);
		List<String> baseCustomResources = extractR4PlusCustomResourcesFromExtensions(
				theNextSp, HapiExtensions.EXTENSION_SEARCHPARAM_CUSTOM_BASE_RESOURCE);

		maybeAddCustomResourcesToResources(baseResources, baseCustomResources);

		RestSearchParameterTypeEnum paramType = null;
		RuntimeSearchParam.RuntimeSearchParamStatusEnum status = null;
		switch (myTerser.getSinglePrimitiveValue(theNextSp, "type").orElse("")) {
			case "composite":
				paramType = RestSearchParameterTypeEnum.COMPOSITE;
				break;
			case "date":
				paramType = RestSearchParameterTypeEnum.DATE;
				break;
			case "number":
				paramType = RestSearchParameterTypeEnum.NUMBER;
				break;
			case "quantity":
				paramType = RestSearchParameterTypeEnum.QUANTITY;
				break;
			case "reference":
				paramType = RestSearchParameterTypeEnum.REFERENCE;
				break;
			case "string":
				paramType = RestSearchParameterTypeEnum.STRING;
				break;
			case "token":
				paramType = RestSearchParameterTypeEnum.TOKEN;
				break;
			case "uri":
				paramType = RestSearchParameterTypeEnum.URI;
				break;
			case "special":
				paramType = RestSearchParameterTypeEnum.SPECIAL;
				break;
		}
		switch (myTerser.getSinglePrimitiveValue(theNextSp, "status").orElse("")) {
			case "active":
				status = RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE;
				break;
			case "draft":
				status = RuntimeSearchParam.RuntimeSearchParamStatusEnum.DRAFT;
				break;
			case "retired":
				status = RuntimeSearchParam.RuntimeSearchParamStatusEnum.RETIRED;
				break;
			case "unknown":
				status = RuntimeSearchParam.RuntimeSearchParamStatusEnum.UNKNOWN;
				break;
		}

		Set<String> targetResources = extractR4PlusResources("target", theNextSp);
		List<String> targetCustomResources = extractR4PlusCustomResourcesFromExtensions(
				theNextSp, HapiExtensions.EXTENSION_SEARCHPARAM_CUSTOM_TARGET_RESOURCE);

		maybeAddCustomResourcesToResources(targetResources, targetCustomResources);

		if (isBlank(name) || isBlank(path) || paramType == null) {
			if ("_text".equals(name) || "_content".equals(name)) {
				// ok
			} else if (paramType != RestSearchParameterTypeEnum.COMPOSITE) {
				return null;
			}
		}

		IIdType id = theNextSp.getIdElement();
		String uri = myTerser.getSinglePrimitiveValueOrNull(theNextSp, "url");
		ComboSearchParamType unique = null;

		String value = ((IBaseHasExtensions) theNextSp)
				.getExtension().stream()
						.filter(e -> HapiExtensions.EXT_SP_UNIQUE.equals(e.getUrl()))
						.filter(t -> t.getValue() instanceof IPrimitiveType)
						.map(t -> (IPrimitiveType<?>) t.getValue())
						.map(t -> t.getValueAsString())
						.findFirst()
						.orElse("");
		if ("true".equalsIgnoreCase(value)) {
			unique = ComboSearchParamType.UNIQUE;
		} else if ("false".equalsIgnoreCase(value)) {
			unique = ComboSearchParamType.NON_UNIQUE;
		}

		List<RuntimeSearchParam.Component> components = new ArrayList<>();
		for (IBase next : myTerser.getValues(theNextSp, "component")) {
			String expression = myTerser.getSinglePrimitiveValueOrNull(next, "expression");
			String definition = myTerser.getSinglePrimitiveValueOrNull(next, "definition");
			if (startsWith(definition, "/SearchParameter/")) {
				definition = definition.substring(1);
			}

			components.add(new RuntimeSearchParam.Component(expression, definition));
		}

		return new RuntimeSearchParam(
				id,
				uri,
				name,
				description,
				path,
				paramType,
				Collections.emptySet(),
				targetResources,
				status,
				unique,
				components,
				baseResources);
	}

	private Set<String> extractR4PlusResources(String thePath, IBaseResource theNextSp) {
		return myTerser.getValues(theNextSp, thePath, IPrimitiveType.class).stream()
				.map(IPrimitiveType::getValueAsString)
				.collect(Collectors.toSet());
	}

	/**
	 * Extracts any extensions from the resource and populates an extension field in the
	 */
	protected void extractExtensions(IBaseResource theSearchParamResource, RuntimeSearchParam theRuntimeSearchParam) {
		if (theSearchParamResource instanceof IBaseHasExtensions) {
			List<? extends IBaseExtension<? extends IBaseExtension, ?>> extensions =
					(List<? extends IBaseExtension<? extends IBaseExtension, ?>>)
							((IBaseHasExtensions) theSearchParamResource).getExtension();
			for (IBaseExtension<? extends IBaseExtension, ?> next : extensions) {
				String nextUrl = next.getUrl();
				if (isNotBlank(nextUrl)) {
					theRuntimeSearchParam.addExtension(nextUrl, next);
					if (HapiExtensions.EXT_SEARCHPARAM_PHONETIC_ENCODER.equals(nextUrl)) {
						setEncoder(theRuntimeSearchParam, next.getValue());
					} else if (HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN.equals(nextUrl)) {
						addUpliftRefchain(theRuntimeSearchParam, next);
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void addUpliftRefchain(
			RuntimeSearchParam theRuntimeSearchParam, IBaseExtension<? extends IBaseExtension, ?> theExtension) {
		String code = ExtensionUtil.extractChildPrimitiveExtensionValue(
				theExtension, HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN_PARAM_CODE);
		String elementName = ExtensionUtil.extractChildPrimitiveExtensionValue(
				theExtension, HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN_ELEMENT_NAME);
		if (isNotBlank(code)) {
			theRuntimeSearchParam.addUpliftRefchain(code, elementName);
		}
	}

	private void setEncoder(RuntimeSearchParam theRuntimeSearchParam, IBaseDatatype theValue) {
		if (theValue instanceof IPrimitiveType) {
			String stringValue = ((IPrimitiveType<?>) theValue).getValueAsString();

			// every string creates a completely new encoder wrapper.
			// this is fine, because the runtime search parameters are constructed at startup
			// for every saved value
			IPhoneticEncoder encoder = PhoneticEncoderUtil.getEncoder(stringValue);
			if (encoder != null) {
				theRuntimeSearchParam.setPhoneticEncoder(encoder);
			} else {
				ourLog.error("Invalid PhoneticEncoderEnum value '" + stringValue + "'");
			}
		}
	}

	private List<String> extractDstu2CustomResourcesFromExtensions(
			ca.uhn.fhir.model.dstu2.resource.SearchParameter theSearchParameter, String theExtensionUrl) {

		List<ExtensionDt> customSpExtensionDt = theSearchParameter.getUndeclaredExtensionsByUrl(theExtensionUrl);

		return customSpExtensionDt.stream()
				.map(theExtensionDt -> theExtensionDt.getValueAsPrimitive().getValueAsString())
				.filter(StringUtils::isNotBlank)
				.collect(Collectors.toList());
	}

	private List<String> extractDstu3CustomResourcesFromExtensions(
			org.hl7.fhir.dstu3.model.SearchParameter theSearchParameter, String theExtensionUrl) {

		List<Extension> customSpExtensions = theSearchParameter.getExtensionsByUrl(theExtensionUrl);

		return customSpExtensions.stream()
				.map(theExtension -> theExtension.getValueAsPrimitive().getValueAsString())
				.filter(StringUtils::isNotBlank)
				.collect(Collectors.toList());
	}

	private List<String> extractR4PlusCustomResourcesFromExtensions(
			IBaseResource theSearchParameter, String theExtensionUrl) {

		List<String> retVal = new ArrayList<>();

		if (theSearchParameter instanceof IBaseHasExtensions) {
			((IBaseHasExtensions) theSearchParameter)
					.getExtension().stream()
							.filter(t -> theExtensionUrl.equals(t.getUrl()))
							.filter(t -> t.getValue() instanceof IPrimitiveType)
							.map(t -> ((IPrimitiveType<?>) t.getValue()))
							.map(IPrimitiveType::getValueAsString)
							.filter(StringUtils::isNotBlank)
							.forEach(retVal::add);
		}

		return retVal;
	}

	private <T extends Collection<String>> void maybeAddCustomResourcesToResources(
			T theResources, List<String> theCustomResources) {
		// SearchParameter base and target components require strict binding to ResourceType for dstu[2|3], R4, R4B
		// and to Version Independent Resource Types for R5.
		//
		// To handle custom resources, we set a placeholder of type 'Resource' in the base or target component and
		// define
		// the custom resource by adding a corresponding extension with url
		// HapiExtensions.EXTENSION_SEARCHPARAM_CUSTOM_BASE_RESOURCE
		// or HapiExtensions.EXTENSION_SEARCHPARAM_CUSTOM_TARGET_RESOURCE with the name of the custom resource.
		//
		// To provide a base/target list that contains both the resources and customResources, we need to remove the
		// placeholders
		// from the theResources and add theCustomResources.

		if (!theCustomResources.isEmpty()) {
			theResources.removeAll(Collections.singleton("Resource"));
			theResources.addAll(theCustomResources);
		}
	}
}
