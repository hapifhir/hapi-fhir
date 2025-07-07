package ca.uhn.fhir.repository.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.repository.IRepository;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import ca.uhn.fhir.rest.gclient.IHistoryTyped;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.rest.gclient.IUntypedQuery;
import ca.uhn.fhir.util.ParametersUtil;
import com.google.common.collect.Multimap;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Adaptor from IGenericClient to IRepository.
 * Based on the clinical reasoning org.opencds.cqf.fhir.utility.repository.RestRepository.
 */
public class GenericClientRepository implements IRepository {

	public GenericClientRepository(IGenericClient theGenericClient) {
		this.myGenericClient = theGenericClient;
	}

	private final IGenericClient myGenericClient;

	protected IGenericClient getClient() {
		return this.myGenericClient;
	}

	@Override
	public <T extends IBaseResource, I extends IIdType> T read(
			Class<T> theResourceType, I theId, Map<String, String> theHeaders) {
		var op = this.myGenericClient.read().resource(theResourceType).withId(theId);
		return this.addHeaders(op, theHeaders).execute();
	}

	@Override
	public <T extends IBaseResource> MethodOutcome create(T theResource, Map<String, String> theHeaders) {
		var op = this.myGenericClient.create().resource(theResource);
		return this.addHeaders(op, theHeaders).execute();
	}

	@Override
	public <I extends IIdType, P extends IBaseParameters> MethodOutcome patch(
			I theId, P thePatchparameters, Map<String, String> theHeaders) {
		var op = this.myGenericClient.patch().withFhirPatch(thePatchparameters).withId(theId);
		return this.addHeaders(op, theHeaders).execute();
	}

	@Override
	public <T extends IBaseResource> MethodOutcome update(T theResource, Map<String, String> theHeaders) {
		var op = this.myGenericClient.update().resource(theResource).withId(theResource.getIdElement());
		return this.addHeaders(op, theHeaders).execute();
	}

	@Override
	public <T extends IBaseResource, I extends IIdType> MethodOutcome delete(
			Class<T> theResourcetype, I theId, Map<String, String> theHeaders) {
		var op = this.myGenericClient.delete().resourceById(theId);
		return this.addHeaders(op, theHeaders).execute();
	}

	@Override
	public <B extends IBaseBundle, T extends IBaseResource> B search(
			Class<B> theBundleType,
			Class<T> theSearchResourceType,
			Multimap<String, List<IQueryParameterType>> theSearchParameters,
			Map<String, String> theHeaders) {
		IUntypedQuery<IBaseBundle> search = this.myGenericClient.search();
		IQuery<IBaseBundle> iBaseBundleIQuery = search.forResource(theSearchResourceType);
		var op = iBaseBundleIQuery.returnBundle(theBundleType);
		if (theSearchParameters != null) {
			theSearchParameters.entries().forEach(e -> op.where(Map.of(e.getKey(), e.getValue())));
		}

		return this.addHeaders(op, theHeaders).execute();
	}

	@Override
	public <C extends IBaseConformance> C capabilities(Class<C> theResourceType, Map<String, String> theHeaders) {
		var op = this.myGenericClient.capabilities().ofType(theResourceType);
		return this.addHeaders(op, theHeaders).execute();
	}

	@Override
	public <B extends IBaseBundle> B transaction(B theBundle, Map<String, String> theHeaders) {
		var op = this.myGenericClient.transaction().withBundle(theBundle);
		return this.addHeaders(op, theHeaders).execute();
	}

	@Override
	public <B extends IBaseBundle> B link(Class<B> theBundleType, String url, Map<String, String> theHeaders) {
		var op = this.myGenericClient.loadPage().byUrl(url).andReturnBundle(theBundleType);
		return this.addHeaders(op, theHeaders).execute();
	}

	@Override
	public <R extends IBaseResource, P extends IBaseParameters> R invoke(
			String theOperationName, P theParameters, Class<R> theReturnType, Map<String, String> theHeaders) {
		var op = this.myGenericClient
				.operation()
				.onServer()
				.named(theOperationName)
				.withParameters(theParameters)
				.returnResourceType(theReturnType);
		return this.addHeaders(op, theHeaders).execute();
	}

	@Override
	public <P extends IBaseParameters> MethodOutcome invoke(
			String theOperationName, P theParameters, Map<String, String> theHeaders) {
		var op = this.myGenericClient
				.operation()
				.onServer()
				.named(theOperationName)
				.withParameters(theParameters)
				.returnMethodOutcome();
		return this.addHeaders(op, theHeaders).execute();
	}

	@Override
	public <R extends IBaseResource, P extends IBaseParameters, T extends IBaseResource> R invoke(
			Class<T> theResourceType,
			String theOperationName,
			P theParameters,
			Class<R> theReturnType,
			Map<String, String> theHeaders) {
		var op = this.myGenericClient
				.operation()
				.onType(theResourceType)
				.named(theOperationName)
				.withParameters(theParameters)
				.returnResourceType(theReturnType);
		return this.addHeaders(op, theHeaders).execute();
	}

	@Override
	public <P extends IBaseParameters, T extends IBaseResource> MethodOutcome invoke(
			Class<T> theResourceType, String theOperationName, P parameters, Map<String, String> theHeaders) {
		var op = this.myGenericClient
				.operation()
				.onType(theResourceType)
				.named(theOperationName)
				.withParameters(parameters)
				.returnMethodOutcome();
		return this.addHeaders(op, theHeaders).execute();
	}

	@Override
	public <R extends IBaseResource, P extends IBaseParameters, I extends IIdType> R invoke(
			I theId, String theOperationName, P theParameters, Class<R> theReturnType, Map<String, String> theHeaders) {
		var op = this.myGenericClient
				.operation()
				.onInstance(theId)
				.named(theOperationName)
				.withParameters(theParameters)
				.returnResourceType(theReturnType);
		return this.addHeaders(op, theHeaders).execute();
	}

	@Override
	public <P extends IBaseParameters, I extends IIdType> MethodOutcome invoke(
			I theResourceId, String theOperationName, P theParameters, Map<String, String> theHeaders) {
		var op = this.myGenericClient
				.operation()
				.onInstance(theResourceId)
				.named(theOperationName)
				.withParameters(theParameters)
				.returnMethodOutcome();
		return this.addHeaders(op, theHeaders).execute();
	}

	@Override
	public <B extends IBaseBundle, P extends IBaseParameters> B history(
			P theParameters, Class<B> theReturnType, Map<String, String> theHeaders) {
		var op = this.myGenericClient.history().onServer().returnBundle(theReturnType);
		this.addHistoryParams(null, theParameters);
		return this.addHeaders(op, theHeaders).execute();
	}

	@Override
	public <B extends IBaseBundle, P extends IBaseParameters, T extends IBaseResource> B history(
			Class<T> theResourceType, P theParameters, Class<B> theReturnType, Map<String, String> theHeaders) {
		var op = this.myGenericClient.history().onType(theResourceType).returnBundle(theReturnType);
		this.addHistoryParams(op, theParameters);
		return this.addHeaders(op, theHeaders).execute();
	}

	@Override
	public <B extends IBaseBundle, P extends IBaseParameters, I extends IIdType> B history(
			I theResourceId, P theParameters, Class<B> theReturnType, Map<String, String> theHeaders) {
		var op = this.myGenericClient.history().onInstance(theResourceId).returnBundle(theReturnType);
		this.addHistoryParams(null, theParameters);
		return this.addHeaders(op, theHeaders).execute();
	}

	@Nonnull
	@Override
	public FhirContext fhirContext() {
		return this.getClient().getFhirContext();
	}

	@SuppressWarnings("unchecked")
	protected <B extends IBaseBundle, P extends IBaseParameters> void addHistoryParams(
			IHistoryTyped<B> operation, P parameters) {

		var ctx = this.myGenericClient.getFhirContext();
		var count = ParametersUtil.getNamedParameterValuesAsInteger(ctx, parameters, "_count");
		if (count != null && !count.isEmpty()) {
			operation.count(count.get(0));
		}

		// TODO: Figure out how to handle date ranges for the _at parameter

		var since = ParametersUtil.getNamedParameter(ctx, parameters, "_since");
		if (since.isPresent()) {
			operation.since((IPrimitiveType<Date>) since.get());
		}
	}

	protected <T extends IClientExecutable<T, ?>> T addHeaders(T op, Map<String, String> headers) {
		if (headers != null) {
			for (var entry : headers.entrySet()) {
				op = op.withAdditionalHeader(entry.getKey(), entry.getValue());
			}
		}

		return op;
	}
}
