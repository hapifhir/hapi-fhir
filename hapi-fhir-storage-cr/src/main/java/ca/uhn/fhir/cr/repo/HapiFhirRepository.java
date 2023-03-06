package ca.uhn.fhir.cr.repo;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.BundleLinks;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.opencds.cqf.fhir.api.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.cr.repo.RequestDetailsCloner.startWith;
import static ca.uhn.fhir.cr.repo.SearchConverter.convert;
import static ca.uhn.fhir.rest.server.RestfulServerUtils.prettyPrintResponse;

class HapiFhirRepository implements Repository {

	private final DaoRegistry myDaoRegistry;
	private final RequestDetails myRequestDetails;
	private final RestfulServer myRestfulServer;
	HapiFhirRepository(DaoRegistry theDaoRegistry, RequestDetails theRequestDetails, RestfulServer theRestfulServer) {
		this.myDaoRegistry = theDaoRegistry;
		this.myRequestDetails = theRequestDetails;
		this.myRestfulServer = theRestfulServer;
	}
	@Override
	public <T extends IBaseResource, I extends IIdType> T read(Class<T> theResourceType, I theId, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails).addHeaders(theHeaders).create();
		return this.myDaoRegistry.getResourceDao(theResourceType).read(theId, details);
	}

	@Override
	public <T extends IBaseResource> MethodOutcome create(T theResource, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails).addHeaders(theHeaders).create();;
		return this.myDaoRegistry.getResourceDao(theResource).create(theResource, details);
	}

	@Override
	public <I extends IIdType, P extends IBaseParameters> MethodOutcome patch(I theId, P thePatchParameters, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails).addHeaders(theHeaders).create();
		// TODO: conditional url, patch type, patch body?
		return this.myDaoRegistry.getResourceDao(theId.getResourceType()).patch(theId, null, null, null, thePatchParameters, details);
	}

	@Override
	public <T extends IBaseResource> MethodOutcome update(T theResource, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails).addHeaders(theHeaders).create();

		return this.myDaoRegistry.getResourceDao(theResource).update(theResource, details);
	}

	@Override
	public <T extends IBaseResource, I extends IIdType> MethodOutcome delete(Class<T> theResourceType, I theId, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails).addHeaders(theHeaders).create();

		return this.myDaoRegistry.getResourceDao(theResourceType).delete(theId, details);
	}

	@Override
	public <B extends IBaseBundle, T extends IBaseResource> B search(Class<B> theBundleType, Class<T> theResourceType, Map<String, List<IQueryParameterType>> theSearchParameters, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails).addHeaders(theHeaders).create();
		var converted = convert(theSearchParameters);
		var bundleProvider = this.myDaoRegistry.getResourceDao(theResourceType).search(converted, details);

		// TODO: The code to do this is in the BaseResourceReturningMethodBinding class.
		// Need to refactor / extract that.
		var links = new BundleLinks(myRequestDetails.getServerBaseForRequest(), null, prettyPrintResponse(myRequestDetails.getServer(), myRequestDetails), BundleTypeEnum.SEARCHSET);
		var linkSelf = RestfulServerUtils.createLinkSelf(myRequestDetails.getFhirServerBase(), myRequestDetails);
		links.setSelf(linkSelf);

		var bundleFactory = this.myRestfulServer.getFhirContext().newBundleFactory();
		bundleFactory.addRootPropertiesToBundle(bundleProvider.getUuid(), links, bundleProvider.size(), bundleProvider.getPublished());
		bundleFactory.addResourcesToBundle(bundleProvider.getAllResources(), BundleTypeEnum.SEARCHSET, myRequestDetails.getFhirServerBase(), null, null);

		var result = bundleFactory.getResourceBundle();

		return (B) result;
	}

	// TODO: The main use case for this is paging through Bundles, but I suppose that technically
	// we ought to handle any old link. Maybe this is also an escape hatch for "custom non-FHIR repository action"?
	@Override
	public <B extends IBaseBundle> B link(Class<B> theBundleType, String theUrl, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails).addHeaders(theHeaders).create();

		return null;
	}

	@Override
	public <C extends IBaseConformance> C capabilities(Class<C> theCapabilityStatementType, Map<String, String> theHeaders) {
		var method = this.myRestfulServer.getServerConformanceMethod();
		if (method == null) {
			return null;
		}
		var details = startWith(myRequestDetails).addHeaders(theHeaders).create();
		return (C) method.provideCapabilityStatement(this.myRestfulServer, details);
	}

	@Override
	public <B extends IBaseBundle> B transaction(B theBundle, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails).addHeaders(theHeaders).create();
		return (B) this.myDaoRegistry.getSystemDao().transaction(details, theBundle);
	}

	@Override
	public <R extends IBaseResource, P extends IBaseParameters> R invoke(String theName, P theParameters, Class<R> theReturnType, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails)
			.addHeaders(theHeaders)
			.setOperation(theName)
			.setParameters(theParameters)
			.create();

		return invoke(details);
	}

	@Override
	public <P extends IBaseParameters> MethodOutcome invoke(String theName, P theParameters, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails)
			.addHeaders(theHeaders)
			.setOperation(theName)
			.setParameters(theParameters)
			.create();

		return invoke(details);
	}

	@Override
	public <R extends IBaseResource, P extends IBaseParameters, T extends IBaseResource> R invoke(Class<T> theResourceType, String theName, P theParameters, Class<R> theReturnType, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails)
			.addHeaders(theHeaders)
			.setOperation(theName)
			.setResourceType(theResourceType.getSimpleName())
			.setParameters(theParameters)
			.create();

		return invoke(details);
	}

	@Override
	public <P extends IBaseParameters, T extends IBaseResource> MethodOutcome invoke(Class<T> theResourceType, String theName, P theParameters, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails)
			.addHeaders(theHeaders)
			.setOperation(theName)
			.setResourceType(theResourceType.getSimpleName())
			.setParameters(theParameters)
			.create();

		return invoke(details);
	}

	@Override
	public <R extends IBaseResource, P extends IBaseParameters, I extends IIdType> R invoke(I theId, String theName, P theParameters, Class<R> theReturnType, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails)
			.addHeaders(theHeaders)
			.setOperation(theName)
			.setResourceType(theId.getResourceType())
			.setId(theId)
			.setParameters(theParameters)
			.create();

		return invoke(details);
	}

	@Override
	public <P extends IBaseParameters, I extends IIdType> MethodOutcome invoke(I theId, String theName, P theParameters, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails)
			.addHeaders(theHeaders)
			.setOperation(theName)
			.setResourceType(theId.getResourceType())
			.setId(theId)
			.setParameters(theParameters)
			.create();

		return invoke(details);
	}

	@Override
	public <B extends IBaseBundle, P extends IBaseParameters> B history(P theParameters, Class<B> theReturnBundleType, Map<String, String> theHeaders) {
		throw new NotImplementedOperationException("history not yet implemented");
	}

	@Override
	public <B extends IBaseBundle, P extends IBaseParameters, T extends IBaseResource> B history(Class<T> theResourceType, P theParameters, Class<B> theReturnBundleType, Map<String, String> theHeaders) {
		throw new NotImplementedOperationException("history not yet implemented");
	}

	@Override
	public <B extends IBaseBundle, P extends IBaseParameters, I extends IIdType> B history(I theId, P theParameters, Class<B> theReturnBundleType, Map<String, String> theHeaders) {
		throw new NotImplementedOperationException("history not yet implemented.");
	}

	protected <R extends Object> R invoke(RequestDetails theDetails) {
		try {
			return (R) this.myRestfulServer
				.determineResourceMethod(theDetails, null)
				.invokeServer(this.myRestfulServer, theDetails);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
