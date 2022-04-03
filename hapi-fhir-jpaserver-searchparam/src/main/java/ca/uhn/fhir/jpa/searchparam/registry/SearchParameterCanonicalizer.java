package ca.uhn.fhir.jpa.searchparam.registry;

/*-
 * #%L
 * HAPI FHIR Search Parameters
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.ComboSearchParamType;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.phonetic.IPhoneticEncoder;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.DatatypeUtil;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.PhoneticEncoderUtil;
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

	@Autowired
	public SearchParameterCanonicalizer(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	public RuntimeSearchParam canonicalizeSearchParameter(IBaseResource theSearchParameter) {
		RuntimeSearchParam retVal;
		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU2:
				retVal = canonicalizeSearchParameterDstu2((ca.uhn.fhir.model.dstu2.resource.SearchParameter) theSearchParameter);
				break;
			case DSTU3:
				retVal = canonicalizeSearchParameterDstu3((org.hl7.fhir.dstu3.model.SearchParameter) theSearchParameter);
				break;
			case R4:
			case R5:
				retVal = canonicalizeSearchParameterR4Plus(theSearchParameter);
				break;
			case DSTU2_HL7ORG:
			case DSTU2_1:
				// Non-supported - these won't happen so just fall through
			default:
				throw new InternalErrorException(Msg.code(510) + "SearchParameter canonicalization not supported for FHIR version" + myFhirContext.getVersion().getVersion());
		}

		if (retVal != null) {
			extractExtensions(theSearchParameter, retVal);
		}

		return retVal;
	}

	private RuntimeSearchParam canonicalizeSearchParameterDstu2(ca.uhn.fhir.model.dstu2.resource.SearchParameter theNextSp) {
		String name = theNextSp.getCode();
		String description = theNextSp.getDescription();
		String path = theNextSp.getXpath();
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
		Set<String> providesMembershipInCompartments = Collections.emptySet();
		Set<String> targets = DatatypeUtil.toStringSet(theNextSp.getTarget());

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
		Collection<? extends IPrimitiveType<String>> base = Collections.singletonList(theNextSp.getBaseElement());
		return new RuntimeSearchParam(id, uri, name, description, path, paramType, providesMembershipInCompartments, targets, status, unique, components, toStrings(base));
	}

	private RuntimeSearchParam canonicalizeSearchParameterDstu3(org.hl7.fhir.dstu3.model.SearchParameter theNextSp) {
		String name = theNextSp.getCode();
		String description = theNextSp.getDescription();
		String path = theNextSp.getExpression();
		RestSearchParameterTypeEnum paramType = null;
		RuntimeSearchParam.RuntimeSearchParamStatusEnum status = null;
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
		Set<String> providesMembershipInCompartments = Collections.emptySet();
		Set<String> targets = DatatypeUtil.toStringSet(theNextSp.getTarget());

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
			components.add(new RuntimeSearchParam.Component(next.getExpression(), next.getDefinition().getReferenceElement().toUnqualifiedVersionless().getValue()));
		}

		return new RuntimeSearchParam(id, uri, name, description, path, paramType, providesMembershipInCompartments, targets, status, unique, components, toStrings(theNextSp.getBase()));
	}

	private RuntimeSearchParam canonicalizeSearchParameterR4Plus(IBaseResource theNextSp) {
		FhirTerser terser = myFhirContext.newTerser();
		String name = terser.getSinglePrimitiveValueOrNull(theNextSp, "code");
		String description = terser.getSinglePrimitiveValueOrNull(theNextSp, "description");
		String path = terser.getSinglePrimitiveValueOrNull(theNextSp, "expression");
		List<String> base = terser.getValues(theNextSp, "base", IPrimitiveType.class).stream().map(t -> t.getValueAsString()).collect(Collectors.toList());

		RestSearchParameterTypeEnum paramType = null;
		RuntimeSearchParam.RuntimeSearchParamStatusEnum status = null;
		switch (terser.getSinglePrimitiveValue(theNextSp, "type").orElse("")) {
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
		switch (terser.getSinglePrimitiveValue(theNextSp, "status").orElse("")) {
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
		Set<String> providesMembershipInCompartments = Collections.emptySet();
		Set<String> targets = terser.getValues(theNextSp, "target", IPrimitiveType.class).stream().map(t -> t.getValueAsString()).collect(Collectors.toSet());

		if (isBlank(name) || isBlank(path) || paramType == null) {
			if ("_text".equals(name) || "_content".equals(name)) {
				// ok
			} else if (paramType != RestSearchParameterTypeEnum.COMPOSITE) {
				return null;
			}
		}

		IIdType id = theNextSp.getIdElement();
		String uri = terser.getSinglePrimitiveValueOrNull(theNextSp, "url");
		ComboSearchParamType unique = null;

		String value = ((IBaseHasExtensions) theNextSp).getExtension()
			.stream()
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
		for (IBase next : terser.getValues(theNextSp, "component")) {
			String expression = terser.getSinglePrimitiveValueOrNull(next, "expression");
			String definition = terser.getSinglePrimitiveValueOrNull(next, "definition");
			if (startsWith(definition, "/SearchParameter/")) {
				definition = definition.substring(1);
			}

			components.add(new RuntimeSearchParam.Component(expression, definition));
		}

		return new RuntimeSearchParam(id, uri, name, description, path, paramType, providesMembershipInCompartments, targets, status, unique, components, base);
	}


	/**
	 * Extracts any extensions from the resource and populates an extension field in the
	 */
	protected void extractExtensions(IBaseResource theSearchParamResource, RuntimeSearchParam theRuntimeSearchParam) {
		if (theSearchParamResource instanceof IBaseHasExtensions) {
			List<? extends IBaseExtension<?, ?>> extensions = ((IBaseHasExtensions) theSearchParamResource).getExtension();
			for (IBaseExtension<?, ?> next : extensions) {
				String nextUrl = next.getUrl();
				if (isNotBlank(nextUrl)) {
					theRuntimeSearchParam.addExtension(nextUrl, next);
					if (HapiExtensions.EXT_SEARCHPARAM_PHONETIC_ENCODER.equals(nextUrl)) {
						setEncoder(theRuntimeSearchParam, next.getValue());
					}
				}
			}
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
			}
			else {
				ourLog.error("Invalid PhoneticEncoderEnum value '" + stringValue + "'");
			}
		}
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


}
