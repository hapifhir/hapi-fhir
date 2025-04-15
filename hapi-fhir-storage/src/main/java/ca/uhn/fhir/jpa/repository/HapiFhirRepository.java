package ca.uhn.fhir.jpa.repository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.repository.Repository;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.method.PageMethodBinding;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.collect.Multimap;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ca.uhn.fhir.jpa.repository.RequestDetailsCloner.startWith;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class leverages DaoRegistry from Hapi-fhir to implement CRUD FHIR API operations constrained to provide only the operations necessary for the cql-evaluator modules to function.
 **/
@SuppressWarnings("squid:S1135")
public class HapiFhirRepository implements Repository {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(HapiFhirRepository.class);
	private final DaoRegistry daoRegistry;
	private final RequestDetails requestDetails;
	private final RestfulServer restfulServer;

	public HapiFhirRepository(DaoRegistry daoRegistry, RequestDetails requestDetails, RestfulServer restfulServer) {
		this.daoRegistry = daoRegistry;
		this.requestDetails = requestDetails;
		this.restfulServer = restfulServer;
	}

	@Override
	public <T extends IBaseResource, I extends IIdType> T read(
			Class<T> resourceType, I id, Map<String, String> headers) {
		var details = startWith(requestDetails)
				.setAction(RestOperationTypeEnum.READ)
				.addHeaders(headers)
				.create();
		return daoRegistry.getResourceDao(resourceType).read(id, details);
	}

	@Override
	public <T extends IBaseResource> MethodOutcome create(T resource, Map<String, String> headers) {
		var details = startWith(requestDetails)
				.setAction(RestOperationTypeEnum.CREATE)
				.addHeaders(headers)
				.create();
		return daoRegistry.getResourceDao(resource).create(resource, details);
	}

	@Override
	public <I extends IIdType, P extends IBaseParameters> MethodOutcome patch(
			I id, P patchParameters, Map<String, String> headers) {
		var details = startWith(requestDetails)
				.setAction(RestOperationTypeEnum.PATCH)
				.addHeaders(headers)
				.create();
		// TODO update FHIR patchType once FHIRPATCH bug has been fixed
		return daoRegistry.getResourceDao(id.getResourceType()).patch(id, null, null, null, patchParameters, details);
	}

	@Override
	public <T extends IBaseResource> MethodOutcome update(T resource, Map<String, String> headers) {
		var details = startWith(requestDetails)
				.setAction(RestOperationTypeEnum.UPDATE)
				.addHeaders(headers)
				.create();

		return daoRegistry.getResourceDao(resource).update(resource, details);
	}

	@Override
	public <T extends IBaseResource, I extends IIdType> MethodOutcome delete(
			Class<T> resourceType, I id, Map<String, String> headers) {
		var details = startWith(requestDetails)
				.setAction(RestOperationTypeEnum.DELETE)
				.addHeaders(headers)
				.create();

		return daoRegistry.getResourceDao(resourceType).delete(id, details);
	}

	@Override
	public <B extends IBaseBundle, T extends IBaseResource> B search(
			Class<B> bundleType,
			Class<T> resourceType,
			Multimap<String, List<IQueryParameterType>> searchParameters,
			Map<String, String> headers) {
		var details = startWith(requestDetails)
				.setAction(RestOperationTypeEnum.SEARCH_TYPE)
				.addHeaders(headers)
				.create();
		SearchConverter converter = new SearchConverter();
		converter.convertParameters(searchParameters, fhirContext());
		details.setParameters(converter.resultParameters);
		details.setResourceName(restfulServer.getFhirContext().getResourceType(resourceType));
		var bundleProvider = daoRegistry.getResourceDao(resourceType).search(converter.searchParameterMap, details);

		if (bundleProvider == null) {
			return null;
		}

		return createBundle(details, bundleProvider, null);
	}

	private <B extends IBaseBundle> B createBundle(
			RequestDetails requestDetails, IBundleProvider bundleProvider, String pagingAction) {
		var count = RestfulServerUtils.extractCountParameter(requestDetails);
		var linkSelf = RestfulServerUtils.createLinkSelf(requestDetails.getFhirServerBase(), requestDetails);

		Set<Include> includes = new HashSet<>();
		var reqIncludes = requestDetails.getParameters().get(Constants.PARAM_INCLUDE);
		if (reqIncludes != null) {
			for (String nextInclude : reqIncludes) {
				includes.add(new Include(nextInclude));
			}
		}

		var offset = RestfulServerUtils.tryToExtractNamedParameter(requestDetails, Constants.PARAM_PAGINGOFFSET);
		if (offset == null || offset < 0) {
			offset = 0;
		}
		var start = offset;
		if (bundleProvider.size() != null) {
			start = Math.max(0, Math.min(offset, bundleProvider.size()));
		}

		BundleTypeEnum bundleType = null;
		var bundleTypeValues = requestDetails.getParameters().get(Constants.PARAM_BUNDLETYPE);
		if (bundleTypeValues != null) {
			bundleType = BundleTypeEnum.VALUESET_BINDER.fromCodeString(bundleTypeValues[0]);
		} else {
			bundleType = BundleTypeEnum.SEARCHSET;
		}

		return unsafeCast(BundleProviderUtil.createBundleFromBundleProvider(
				restfulServer,
				requestDetails,
				count,
				linkSelf,
				includes,
				bundleProvider,
				start,
				bundleType,
				pagingAction));
	}

	// TODO: The main use case for this is paging through Bundles, but I suppose that technically
	// we ought to handle any old link. Maybe this is also an escape hatch for "custom non-FHIR
	// repository action"?
	@Override
	public <B extends IBaseBundle> B link(Class<B> bundleType, String url, Map<String, String> headers) {
		var details = startWith(requestDetails)
				.setAction(RestOperationTypeEnum.GET_PAGE)
				.addHeaders(headers)
				.create();
		var urlParts = UrlUtil.parseUrl(url);
		details.setCompleteUrl(url);
		details.setParameters(UrlUtil.parseQueryStrings(urlParts.getParams()));

		var pagingProvider = restfulServer.getPagingProvider();
		if (pagingProvider == null) {
			throw new InvalidRequestException("This server does not support paging");
		}

		var pagingAction = details.getParameters().get(Constants.PARAM_PAGINGACTION)[0];

		IBundleProvider bundleProvider;

		String pageId = null;
		String[] pageIdParams = details.getParameters().get(Constants.PARAM_PAGEID);
		if (pageIdParams != null && pageIdParams.length > 0 && isNotBlank(pageIdParams[0])) {
			pageId = pageIdParams[0];
		}

		if (pageId != null) {
			// This is a page request by Search ID and Page ID
			bundleProvider = pagingProvider.retrieveResultList(details, pagingAction, pageId);
			validateHaveBundleProvider(pagingAction, bundleProvider);
		} else {
			// This is a page request by Search ID and Offset
			bundleProvider = pagingProvider.retrieveResultList(details, pagingAction);
			validateHaveBundleProvider(pagingAction, bundleProvider);
		}

		return createBundle(details, bundleProvider, pagingAction);
	}

	private void validateHaveBundleProvider(String pagingAction, IBundleProvider bundleProvider) {
		// Return an HTTP 410 if the search is not known
		if (bundleProvider == null) {
			ourLog.info("Client requested unknown paging ID[{}]", pagingAction);
			String msg =
					fhirContext().getLocalizer().getMessage(PageMethodBinding.class, "unknownSearchId", pagingAction);
			throw new ResourceGoneException(msg);
		}
	}

	@Override
	public <C extends IBaseConformance> C capabilities(Class<C> capabilityStatementType, Map<String, String> headers) {
		var method = restfulServer.getServerConformanceMethod();
		if (method == null) {
			return null;
		}
		var details = startWith(requestDetails)
				.setAction(RestOperationTypeEnum.METADATA)
				.addHeaders(headers)
				.create();
		return unsafeCast(method.provideCapabilityStatement(restfulServer, details));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <B extends IBaseBundle> B transaction(B bundle, Map<String, String> headers) {
		var details = startWith(requestDetails)
				.setAction(RestOperationTypeEnum.TRANSACTION)
				.addHeaders(headers)
				.create();
		return unsafeCast(daoRegistry.getSystemDao().transaction(details, bundle));
	}

	@Override
	public <R extends IBaseResource, P extends IBaseParameters> R invoke(
			String name, P parameters, Class<R> returnType, Map<String, String> headers) {
		var details = startWith(requestDetails)
				.setAction(RestOperationTypeEnum.EXTENDED_OPERATION_SERVER)
				.addHeaders(headers)
				.setOperation(name)
				.setParameters(parameters)
				.create();

		return invoke(details);
	}

	@Override
	public <P extends IBaseParameters> MethodOutcome invoke(String name, P parameters, Map<String, String> headers) {
		var details = startWith(requestDetails)
				.setAction(RestOperationTypeEnum.EXTENDED_OPERATION_SERVER)
				.addHeaders(headers)
				.setOperation(name)
				.setParameters(parameters)
				.create();

		return invoke(details);
	}

	@Override
	public <R extends IBaseResource, P extends IBaseParameters, T extends IBaseResource> R invoke(
			Class<T> resourceType, String name, P parameters, Class<R> returnType, Map<String, String> headers) {
		var details = startWith(requestDetails)
				.setAction(RestOperationTypeEnum.EXTENDED_OPERATION_SERVER)
				.addHeaders(headers)
				.setOperation(name)
				.setResourceType(resourceType.getSimpleName())
				.setParameters(parameters)
				.create();

		return invoke(details);
	}

	@Override
	public <P extends IBaseParameters, T extends IBaseResource> MethodOutcome invoke(
			Class<T> resourceType, String name, P parameters, Map<String, String> headers) {
		var details = startWith(requestDetails)
				.setAction(RestOperationTypeEnum.EXTENDED_OPERATION_SERVER)
				.addHeaders(headers)
				.setOperation(name)
				.setResourceType(resourceType.getSimpleName())
				.setParameters(parameters)
				.create();

		return invoke(details);
	}

	@Override
	public <R extends IBaseResource, P extends IBaseParameters, I extends IIdType> R invoke(
			I id, String name, P parameters, Class<R> returnType, Map<String, String> headers) {
		var details = startWith(requestDetails)
				.setAction(RestOperationTypeEnum.EXTENDED_OPERATION_SERVER)
				.addHeaders(headers)
				.setOperation(name)
				.setResourceType(id.getResourceType())
				.setId(id)
				.setParameters(parameters)
				.create();

		return invoke(details);
	}

	@Override
	public <P extends IBaseParameters, I extends IIdType> MethodOutcome invoke(
			I id, String name, P parameters, Map<String, String> headers) {
		var details = startWith(requestDetails)
				.setAction(RestOperationTypeEnum.EXTENDED_OPERATION_SERVER)
				.addHeaders(headers)
				.setOperation(name)
				.setResourceType(id.getResourceType())
				.setId(id)
				.setParameters(parameters)
				.create();

		return invoke(details);
	}

	private void notImplemented() {
		throw new NotImplementedOperationException("history not yet implemented");
	}

	@Override
	public <B extends IBaseBundle, P extends IBaseParameters> B history(
			P parameters, Class<B> bundleType, Map<String, String> headers) {
		notImplemented();

		return null;
	}

	@Override
	public <B extends IBaseBundle, P extends IBaseParameters, T extends IBaseResource> B history(
			Class<T> resourceType, P parameters, Class<B> bundleType, Map<String, String> headers) {
		notImplemented();

		return null;
	}

	@Override
	public <B extends IBaseBundle, P extends IBaseParameters, I extends IIdType> B history(
			I id, P parameters, Class<B> bundleType, Map<String, String> headers) {
		notImplemented();

		return null;
	}

	@Override
	public FhirContext fhirContext() {
		return restfulServer.getFhirContext();
	}

	protected <R> R invoke(RequestDetails details) {
		try {
			return unsafeCast(
					restfulServer.determineResourceMethod(details, null).invokeServer(restfulServer, details));
		} catch (IOException exception) {
			throw new InternalErrorException(exception);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T unsafeCast(Object object) {
		return (T) object;
	}
}
