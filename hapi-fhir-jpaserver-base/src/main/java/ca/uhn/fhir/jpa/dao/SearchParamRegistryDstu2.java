package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.search.JpaRuntimeSearchParam;
import ca.uhn.fhir.jpa.util.JpaConstants;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu2.resource.SearchParameter;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.util.DatatypeUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class SearchParamRegistryDstu2 extends BaseSearchParamRegistry<SearchParameter> {

	@Autowired
	private IFhirResourceDao<SearchParameter> mySpDao;

	@Override
	public IFhirResourceDao<SearchParameter> getSearchParameterDao() {
		return mySpDao;
	}

	@Override
	protected JpaRuntimeSearchParam toRuntimeSp(SearchParameter theNextSp) {
		String name = theNextSp.getCode();
		String description = theNextSp.getDescription();
		String path = theNextSp.getXpath();
		RestSearchParameterTypeEnum paramType = null;
		RuntimeSearchParam.RuntimeSearchParamStatusEnum status = null;
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

		List<ExtensionDt> uniqueExts = theNextSp.getUndeclaredExtensionsByUrl(JpaConstants.EXT_SP_UNIQUE);
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

}
