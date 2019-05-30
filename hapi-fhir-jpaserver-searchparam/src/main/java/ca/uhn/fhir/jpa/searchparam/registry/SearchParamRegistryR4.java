package ca.uhn.fhir.jpa.searchparam.registry;

/*
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.RuntimeSearchParam.RuntimeSearchParamStatusEnum;
import ca.uhn.fhir.jpa.searchparam.JpaRuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.SearchParamConstants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.util.DatatypeUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.SearchParameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class SearchParamRegistryR4 extends BaseSearchParamRegistry<SearchParameter> {

	@Override
	protected RuntimeSearchParam toRuntimeSp(SearchParameter theNextSp) {
		String name = theNextSp.getCode();
		String description = theNextSp.getDescription();
		String path = theNextSp.getExpression();
		RestSearchParameterTypeEnum paramType = null;
		RuntimeSearchParamStatusEnum status = null;
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
					status = RuntimeSearchParamStatusEnum.ACTIVE;
					break;
				case DRAFT:
					status = RuntimeSearchParamStatusEnum.DRAFT;
					break;
				case RETIRED:
					status = RuntimeSearchParamStatusEnum.RETIRED;
					break;
				case UNKNOWN:
					status = RuntimeSearchParamStatusEnum.UNKNOWN;
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

		List<Extension> uniqueExts = theNextSp.getExtensionsByUrl(SearchParamConstants.EXT_SP_UNIQUE);
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
}
