package ca.uhn.fhir.dao;

import javax.servlet.http.HttpServletRequest;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UnsignedIntDt;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.IBundleProvider;

public interface IFhirResourceDaoEncounter<T extends IBaseResource> extends IFhirResourceDao<T> {

	IBundleProvider encounterInstanceEverything(HttpServletRequest theServletRequest, IdDt theId, UnsignedIntDt theCount, DateRangeParam theLastUpdate, SortSpec theSort);

	IBundleProvider encounterTypeEverything(HttpServletRequest theServletRequest, UnsignedIntDt theCount, DateRangeParam theLastUpdated, SortSpec theSortSpec);

}
