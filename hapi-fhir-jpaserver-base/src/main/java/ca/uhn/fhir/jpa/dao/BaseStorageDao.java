package ca.uhn.fhir.jpa.dao;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.server.*;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.param.QualifierDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.method.SearchMethodBinding;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.InstantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.*;

import static ca.uhn.fhir.jpa.dao.BaseHapiFhirDao.OO_SEVERITY_ERROR;
import static ca.uhn.fhir.jpa.dao.BaseHapiFhirDao.OO_SEVERITY_INFO;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseStorageDao {
	@Autowired
	protected ISearchParamRegistry mySearchParamRegistry;

	/**
	 * May be overridden by subclasses to validate resources prior to storage
	 *
	 * @param theResource The resource that is about to be stored
	 */
	protected void preProcessResourceForStorage(IBaseResource theResource) {
		String type = getContext().getResourceDefinition(theResource).getName();
		if (getResourceName() != null && !getResourceName().equals(type)) {
			throw new InvalidRequestException(getContext().getLocalizer().getMessageSanitized(BaseHapiFhirResourceDao.class, "incorrectResourceType", type, getResourceName()));
		}

		if (theResource.getIdElement().hasIdPart()) {
			if (!theResource.getIdElement().isIdPartValid()) {
				throw new InvalidRequestException(getContext().getLocalizer().getMessageSanitized(BaseHapiFhirResourceDao.class, "failedToCreateWithInvalidId", theResource.getIdElement().getIdPart()));
			}
		}

		/*
		 * Replace absolute references with relative ones if configured to do so
		 */
		if (getConfig().getTreatBaseUrlsAsLocal().isEmpty() == false) {
			FhirTerser t = getContext().newTerser();
			List<ResourceReferenceInfo> refs = t.getAllResourceReferences(theResource);
			for (ResourceReferenceInfo nextRef : refs) {
				IIdType refId = nextRef.getResourceReference().getReferenceElement();
				if (refId != null && refId.hasBaseUrl()) {
					if (getConfig().getTreatBaseUrlsAsLocal().contains(refId.getBaseUrl())) {
						IIdType newRefId = refId.toUnqualified();
						nextRef.getResourceReference().setReference(newRefId.getValue());
					}
				}
			}
		}

		if ("Bundle".equals(type)) {
			Set<String> allowedBundleTypes = getConfig().getBundleTypesAllowedForStorage();
			String bundleType = BundleUtil.getBundleType(getContext(), (IBaseBundle) theResource);
			bundleType = defaultString(bundleType);
			if (!allowedBundleTypes.contains(bundleType)) {
				String message = "Unable to store a Bundle resource on this server with a Bundle.type value of: " + (isNotBlank(bundleType) ? bundleType : "(missing)");
				throw new UnprocessableEntityException(message);
			}
		}

	}

	protected DaoMethodOutcome toMethodOutcome(RequestDetails theRequest, @Nonnull final IBasePersistedResource theEntity, @Nonnull IBaseResource theResource) {
		DaoMethodOutcome outcome = new DaoMethodOutcome();

		IIdType id = null;
		if (theResource.getIdElement().getValue() != null) {
			id = theResource.getIdElement();
		}
		if (id == null) {
			id = theEntity.getIdDt();
			if (getContext().getVersion().getVersion().isRi()) {
				id = getContext().getVersion().newIdType().setValue(id.getValue());
			}
		}

		outcome.setId(id);
		if (theEntity.isDeleted() == false) {
			outcome.setResource(theResource);
		}
		outcome.setEntity(theEntity);

		// Interceptor broadcast: STORAGE_PREACCESS_RESOURCES
		if (outcome.getResource() != null) {
			SimplePreResourceAccessDetails accessDetails = new SimplePreResourceAccessDetails(outcome.getResource());
			HookParams params = new HookParams()
				.add(IPreResourceAccessDetails.class, accessDetails)
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest);
			JpaInterceptorBroadcaster.doCallHooks(getInterceptorBroadcaster(), theRequest, Pointcut.STORAGE_PREACCESS_RESOURCES, params);
			if (accessDetails.isDontReturnResourceAtIndex(0)) {
				outcome.setResource(null);
			}
		}

		// Interceptor broadcast: STORAGE_PRESHOW_RESOURCES
		// Note that this will only fire if someone actually goes to use the
		// resource in a response (it's their responsibility to call
		// outcome.fireResourceViewCallback())
		outcome.registerResourceViewCallback(() -> {
			if (outcome.getResource() != null) {
				SimplePreResourceShowDetails showDetails = new SimplePreResourceShowDetails(outcome.getResource());
				HookParams params = new HookParams()
					.add(IPreResourceShowDetails.class, showDetails)
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest);
				JpaInterceptorBroadcaster.doCallHooks(getInterceptorBroadcaster(), theRequest, Pointcut.STORAGE_PRESHOW_RESOURCES, params);
				outcome.setResource(showDetails.getResource(0));
			}
		});

		return outcome;
	}

	protected void doCallHooks(RequestDetails theRequestDetails, Pointcut thePointcut, HookParams theParams) {
		JpaInterceptorBroadcaster.doCallHooks(getInterceptorBroadcaster(), theRequestDetails, thePointcut, theParams);
	}

	protected abstract IInterceptorBroadcaster getInterceptorBroadcaster();

	public IBaseOperationOutcome createErrorOperationOutcome(String theMessage, String theCode) {
		return createOperationOutcome(OO_SEVERITY_ERROR, theMessage, theCode);
	}

	public IBaseOperationOutcome createInfoOperationOutcome(String theMessage) {
		return createOperationOutcome(OO_SEVERITY_INFO, theMessage, "informational");
	}

	private IBaseOperationOutcome createOperationOutcome(String theSeverity, String theMessage, String theCode) {
		IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(getContext());
		OperationOutcomeUtil.addIssue(getContext(), oo, theSeverity, theMessage, null, theCode);
		return oo;
	}

	@NotNull
	protected ResourceGoneException createResourceGoneException(IBasePersistedResource theResourceEntity) {
		StringBuilder b = new StringBuilder();
		b.append("Resource was deleted at ");
		b.append(new InstantType(theResourceEntity.getDeleted()).getValueAsString());
		ResourceGoneException retVal = new ResourceGoneException(b.toString());
		retVal.setResourceId(theResourceEntity.getIdDt());
		return retVal;
	}

	/**
	 * Provide the DaoConfig
	 */
	protected abstract DaoConfig getConfig();

	/**
	 * Returns the resource type for this DAO, or null if this is a system-level DAO
	 */
	@Nullable
	protected abstract String getResourceName();

	/**
	 * Provides the FHIR context
	 */
	protected abstract FhirContext getContext();


	@Transactional(propagation = Propagation.SUPPORTS)
	public void translateRawParameters(Map<String, List<String>> theSource, SearchParameterMap theTarget) {
		if (theSource == null || theSource.isEmpty()) {
			return;
		}

		Map<String, RuntimeSearchParam> searchParams = mySearchParamRegistry.getActiveSearchParams(getResourceName());

		Set<String> paramNames = theSource.keySet();
		for (String nextParamName : paramNames) {
			QualifierDetails qualifiedParamName = QualifierDetails.extractQualifiersFromParameterName(nextParamName);
			RuntimeSearchParam param = searchParams.get(qualifiedParamName.getParamName());
			if (param == null) {
				String msg = getContext().getLocalizer().getMessageSanitized(BaseHapiFhirResourceDao.class, "invalidSearchParameter", qualifiedParamName.getParamName(), new TreeSet<>(searchParams.keySet()));
				throw new InvalidRequestException(msg);
			}

			// Should not be null since the check above would have caught it
			RuntimeResourceDefinition resourceDef = getContext().getResourceDefinition(getResourceName());
			RuntimeSearchParam paramDef = mySearchParamRegistry.getSearchParamByName(resourceDef, qualifiedParamName.getParamName());

			for (String nextValue : theSource.get(nextParamName)) {
				QualifiedParamList qualifiedParam = QualifiedParamList.splitQueryStringByCommasIgnoreEscape(qualifiedParamName.getWholeQualifier(), nextValue);
				List<QualifiedParamList> paramList = Collections.singletonList(qualifiedParam);
				IQueryParameterAnd<?> parsedParam = ParameterUtil.parseQueryParams(getContext(), paramDef, nextParamName, paramList);
				theTarget.add(qualifiedParamName.getParamName(), parsedParam);
			}

		}
	}

}
