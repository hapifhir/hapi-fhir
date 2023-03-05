package ca.uhn.fhir.cr.common;

/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.BundleLinks;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.opencds.cqf.fhir.api.Repository;

import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.rest.server.RestfulServerUtils.*;

@SuppressWarnings("unchecked")
/**
 * This class leverages DaoRegistry from Hapi-fhir to implement CRUD FHIR API operations constrained to provide only the operations necessary for the cql-evaluator modules to function.
 **/
public class HapiFhirRepository implements Repository {
	protected final FhirContext myContext;
	protected final DaoRegistry myDaoRegistry;
	protected final RequestDetails myRequestDetails;

	public HapiFhirRepository(FhirContext theContext, DaoRegistry theDaoRegistry) {
		this(theContext, theDaoRegistry, null);
	}

	public HapiFhirRepository(FhirContext theContext, DaoRegistry theDaoRegistry, RequestDetails theRequestDetails) {
		this.myContext = theContext;
		this.myDaoRegistry = theDaoRegistry;
		this.myRequestDetails = theRequestDetails;
	}

	@Override
	public <T extends IBaseResource, I extends IIdType> T read(Class<T> theResourceClass, I theId, Map<String, String> theHeaders) {
		return (T) this.myDaoRegistry.getResourceDao(theResourceClass.getSimpleName()).read(theId, myRequestDetails);
	}

	@Override
	public <T extends IBaseResource> MethodOutcome create(T theResource, Map<String, String> theHeaders) {
		return this.myDaoRegistry.getResourceDao(theResource.fhirType()).create(theResource, myRequestDetails);
	}

	@Override
	public <I extends IIdType, P extends IBaseParameters> MethodOutcome patch(I theId, P theParameters, Map<String, String> theHeaders) {
		return this.myDaoRegistry.getResourceDao(theId.getResourceType()).patch(theId, null, null, null, theParameters, myRequestDetails);
	}

	@Override
	public <T extends IBaseResource> MethodOutcome update(T theResource, Map<String, String> theHeaders) {
		return this.myDaoRegistry.getResourceDao(theResource.fhirType()).update(theResource, myRequestDetails);
	}

	@Override
	public <T extends IBaseResource, I extends IIdType> MethodOutcome delete(Class<T> theResourceClass, I theId, Map<String, String> theHeaders) {
		return this.myDaoRegistry.getResourceDao(theResourceClass.getSimpleName()).delete(theId, myRequestDetails);
	}

	@Override
	public <B extends IBaseBundle, T extends IBaseResource> B search(Class<B> theBundleClass, Class<T> theResourceClass, Map<String, List<IQueryParameterType>> theQueryStrings, Map<String, String> theHeaders) {
		var response = this.myDaoRegistry.getResourceDao(theResourceClass)
			.search(SearchParameterMap.newSynchronous(), myRequestDetails);

		var links = new BundleLinks(myRequestDetails.getServerBaseForRequest(), null, prettyPrintResponse(myRequestDetails.getServer(), myRequestDetails), BundleTypeEnum.SEARCHSET);
		var linkSelf = RestfulServerUtils.createLinkSelf(myRequestDetails.getFhirServerBase(), myRequestDetails);
		links.setSelf(linkSelf);

		var bundleFactory = myContext.newBundleFactory();
		bundleFactory.addRootPropertiesToBundle(response.getUuid(), links, response.size(), response.getPublished());
		bundleFactory.addResourcesToBundle(response.getAllResources(), BundleTypeEnum.SEARCHSET, myRequestDetails.getFhirServerBase(), null, null);

		var result = bundleFactory.getResourceBundle();

		return (B) result;
	}

	@Override
	public <B extends IBaseBundle> B link(Class<B> theClass, String s, Map<String, String> theHeaders) {
		return null;
	}

	@Override
	public <C extends IBaseConformance> C capabilities(Class<C> theClass, Map<String, String> theHeaders) {
		return null;
	}

	@Override
	public <B extends IBaseBundle> B transaction(B theBundle, Map<String, String> theHeaders) {
		return null;
	}

	@Override
	public <R extends IBaseResource, P extends IBaseParameters> R invoke(String s, P theParameters, Class<R> theResourceClass, Map<String, String> theHeaders) {
		return null;
	}

	@Override
	public <P extends IBaseParameters> MethodOutcome invoke(String s, P theParameters, Map<String, String> theHeaders) {
		return null;
	}

	@Override
	public <R extends IBaseResource, P extends IBaseParameters, T extends IBaseResource> R invoke(Class<T> aClass, String s, P theParameters, Class<R> theResourceClass, Map<String, String> theHeaders) {
		return null;
	}

	@Override
	public <P extends IBaseParameters, T extends IBaseResource> MethodOutcome invoke(Class<T> aClass, String s, P theParameters, Map<String, String> theHeaders) {
		return null;
	}

	@Override
	public <R extends IBaseResource, P extends IBaseParameters, I extends IIdType> R invoke(I theId, String s, P theParameters, Class<R> theResourceClass, Map<String, String> theHeaders) {
		return null;
	}

	@Override
	public <P extends IBaseParameters, I extends IIdType> MethodOutcome invoke(I theId, String s, P theParameters, Map<String, String> theHeaders) {
		return null;
	}

	@Override
	public <B extends IBaseBundle, P extends IBaseParameters> B history(P theParameters, Class<B> theBundleClass, Map<String, String> theHeaders) {
		return null;
	}

	@Override
	public <B extends IBaseBundle, P extends IBaseParameters, T extends IBaseResource> B history(Class<T> theResourceClass, P theParameters, Class<B> theBundleClass, Map<String, String> theHeaders) {
		return null;
	}

	@Override
	public <B extends IBaseBundle, P extends IBaseParameters, I extends IIdType> B history(I theId, P theParameters, Class<B> theBundleClass, Map<String, String> theHeaders) {
		return null;
	}
}
