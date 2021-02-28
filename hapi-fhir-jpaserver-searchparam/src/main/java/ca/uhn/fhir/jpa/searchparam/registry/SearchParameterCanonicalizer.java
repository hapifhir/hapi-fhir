package ca.uhn.fhir.jpa.searchparam.registry;

/*-
 * #%L
 * HAPI FHIR Search Parameters
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
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.phonetic.PhoneticEncoderEnum;
import ca.uhn.fhir.jpa.searchparam.JpaRuntimeSearchParam;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.DatatypeUtil;
import ca.uhn.fhir.util.HapiExtensions;
import org.apache.commons.lang3.EnumUtils;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.SearchParameter;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class SearchParameterCanonicalizer {
	private static final Logger ourLog = LoggerFactory.getLogger(SearchParameterCanonicalizer.class);

	private final FhirContext myFhirContext;

	@Autowired
	public SearchParameterCanonicalizer(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	protected RuntimeSearchParam canonicalizeSearchParameter(IBaseResource theSearchParameter) {
		JpaRuntimeSearchParam retVal;
		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU2:
				retVal = canonicalizeSearchParameterDstu2((ca.uhn.fhir.model.dstu2.resource.SearchParameter) theSearchParameter);
				break;
			case DSTU3:
				retVal = canonicalizeSearchParameterDstu3((org.hl7.fhir.dstu3.model.SearchParameter) theSearchParameter);
				break;
			case R4:
				retVal = canonicalizeSearchParameterR4((org.hl7.fhir.r4.model.SearchParameter) theSearchParameter);
				break;
			case R5:
				retVal = canonicalizeSearchParameterR5((org.hl7.fhir.r5.model.SearchParameter) theSearchParameter);
				break;
			case DSTU2_HL7ORG:
			case DSTU2_1:
				// Non-supported - these won't happen so just fall through
			default:
				throw new InternalErrorException("SearchParameter canonicalization not supported for FHIR version" + myFhirContext.getVersion().getVersion());
		}
		extractExtensions(theSearchParameter, retVal);
		return retVal;
	}

	private JpaRuntimeSearchParam canonicalizeSearchParameterDstu2(ca.uhn.fhir.model.dstu2.resource.SearchParameter theNextSp) {
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
		boolean unique = false;

		List<ExtensionDt> uniqueExts = theNextSp.getUndeclaredExtensionsByUrl(HapiExtensions.EXT_SP_UNIQUE);
		if (uniqueExts.size() > 0) {
			IPrimitiveType<?> uniqueExtsValuePrimitive = uniqueExts.get(0).getValueAsPrimitive();
			if (uniqueExtsValuePrimitive != null) {
				if ("true".equalsIgnoreCase(uniqueExtsValuePrimitive.getValueAsString())) {
					unique = true;
				}
			}
		}

		List<JpaRuntimeSearchParam.Component> components = Collections.emptyList();
		Collection<? extends IPrimitiveType<String>> base = Collections.singletonList(theNextSp.getBaseElement());
		return new JpaRuntimeSearchParam(id, uri, name, description, path, paramType, providesMembershipInCompartments, targets, status, unique, components, base);
	}

	private JpaRuntimeSearchParam canonicalizeSearchParameterDstu3(org.hl7.fhir.dstu3.model.SearchParameter theNextSp) {
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
		boolean unique = false;

		List<Extension> uniqueExts = theNextSp.getExtensionsByUrl(HapiExtensions.EXT_SP_UNIQUE);
		if (uniqueExts.size() > 0) {
			IPrimitiveType<?> uniqueExtsValuePrimitive = uniqueExts.get(0).getValueAsPrimitive();
			if (uniqueExtsValuePrimitive != null) {
				if ("true".equalsIgnoreCase(uniqueExtsValuePrimitive.getValueAsString())) {
					unique = true;
				}
			}
		}

		List<JpaRuntimeSearchParam.Component> components = new ArrayList<>();
		for (SearchParameter.SearchParameterComponentComponent next : theNextSp.getComponent()) {
			components.add(new JpaRuntimeSearchParam.Component(next.getExpression(), next.getDefinition()));
		}

		return new JpaRuntimeSearchParam(id, uri, name, description, path, paramType, providesMembershipInCompartments, targets, status, unique, components, theNextSp.getBase());
	}

	private JpaRuntimeSearchParam canonicalizeSearchParameterR4(org.hl7.fhir.r4.model.SearchParameter theNextSp) {
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
			case SPECIAL:
				paramType = RestSearchParameterTypeEnum.SPECIAL;
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
		boolean unique = false;

		List<org.hl7.fhir.r4.model.Extension> uniqueExts = theNextSp.getExtensionsByUrl(HapiExtensions.EXT_SP_UNIQUE);
		if (uniqueExts.size() > 0) {
			IPrimitiveType<?> uniqueExtsValuePrimitive = uniqueExts.get(0).getValueAsPrimitive();
			if (uniqueExtsValuePrimitive != null) {
				if ("true".equalsIgnoreCase(uniqueExtsValuePrimitive.getValueAsString())) {
					unique = true;
				}
			}
		}

		List<JpaRuntimeSearchParam.Component> components = new ArrayList<>();
		for (org.hl7.fhir.r4.model.SearchParameter.SearchParameterComponentComponent next : theNextSp.getComponent()) {
			components.add(new JpaRuntimeSearchParam.Component(next.getExpression(), new Reference(next.getDefinition())));
		}

		return new JpaRuntimeSearchParam(id, uri, name, description, path, paramType, providesMembershipInCompartments, targets, status, unique, components, theNextSp.getBase());
	}

	private JpaRuntimeSearchParam canonicalizeSearchParameterR5(org.hl7.fhir.r5.model.SearchParameter theNextSp) {
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
			case SPECIAL:
				paramType = RestSearchParameterTypeEnum.SPECIAL;
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
		boolean unique = false;

		List<org.hl7.fhir.r5.model.Extension> uniqueExts = theNextSp.getExtensionsByUrl(HapiExtensions.EXT_SP_UNIQUE);
		if (uniqueExts.size() > 0) {
			IPrimitiveType<?> uniqueExtsValuePrimitive = uniqueExts.get(0).getValueAsPrimitive();
			if (uniqueExtsValuePrimitive != null) {
				if ("true".equalsIgnoreCase(uniqueExtsValuePrimitive.getValueAsString())) {
					unique = true;
				}
			}
		}

		List<JpaRuntimeSearchParam.Component> components = new ArrayList<>();
		for (org.hl7.fhir.r5.model.SearchParameter.SearchParameterComponentComponent next : theNextSp.getComponent()) {
			components.add(new JpaRuntimeSearchParam.Component(next.getExpression(), new org.hl7.fhir.r5.model.Reference(next.getDefinition())));
		}

		return new JpaRuntimeSearchParam(id, uri, name, description, path, paramType, providesMembershipInCompartments, targets, status, unique, components, theNextSp.getBase());
	}


	/**
	 * Extracts any extensions from the resource and populates an extension field in the
	 */
	protected void extractExtensions(IBaseResource theSearchParamResource, JpaRuntimeSearchParam theRuntimeSearchParam) {
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

	private void setEncoder(JpaRuntimeSearchParam theRuntimeSearchParam, IBaseDatatype theValue) {
		if (theValue instanceof IPrimitiveType) {
			String stringValue = ((IPrimitiveType<?>) theValue).getValueAsString();
			PhoneticEncoderEnum encoderEnum = EnumUtils.getEnum(PhoneticEncoderEnum.class, stringValue);
			if (encoderEnum != null) {
				theRuntimeSearchParam.setPhoneticEncoder(encoderEnum.getPhoneticEncoder());
			} else {
				ourLog.error("Invalid PhoneticEncoderEnum value '" + stringValue + "'");
			}
		}
	}
}
