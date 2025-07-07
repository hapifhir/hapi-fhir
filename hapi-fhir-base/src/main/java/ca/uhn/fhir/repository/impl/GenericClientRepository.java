package ca.uhn.fhir.repository.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.repository.IRepository;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import ca.uhn.fhir.rest.gclient.IHistoryTyped;
import ca.uhn.fhir.util.ParametersUtil;
import com.google.common.collect.Multimap;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

public class GenericClientRepository implements IRepository {

    public GenericClientRepository(IGenericClient client) {
        this.client = client;
    }

    private IGenericClient client;

    protected IGenericClient getClient() {
        return this.client;
    }

    @Override
    public <T extends IBaseResource, I extends IIdType> T read(
            Class<T> resourceType, I id, Map<String, String> headers) {
        var op = this.client.read().resource(resourceType).withId(id);
        return this.addHeaders(op, headers).execute();
    }

    @Override
    public <T extends IBaseResource> MethodOutcome create(T resource, Map<String, String> headers) {
        var op = this.client.create().resource(resource);
        return this.addHeaders(op, headers).execute();
    }

    @Override
    public <I extends IIdType, P extends IBaseParameters> MethodOutcome patch(
            I id, P patchParameters, Map<String, String> headers) {
        var op = this.client.patch().withFhirPatch(patchParameters).withId(id);
        return this.addHeaders(op, headers).execute();
    }

    @Override
    public <T extends IBaseResource> MethodOutcome update(T resource, Map<String, String> headers) {
        var op = this.client.update().resource(resource).withId(resource.getIdElement());
        return this.addHeaders(op, headers).execute();
    }

    @Override
    public <T extends IBaseResource, I extends IIdType> MethodOutcome delete(
            Class<T> resourceType, I id, Map<String, String> headers) {
        var op = this.client.delete().resourceById(id);
        return this.addHeaders(op, headers).execute();
    }

    @Override
    public <B extends IBaseBundle, T extends IBaseResource> B search(
            Class<B> bundleType,
            Class<T> resourceType,
            Multimap<String, List<IQueryParameterType>> searchParameters,
            Map<String, String> headers) {
        var params = new HashMap<String, List<IQueryParameterType>>();
        if (searchParameters != null) {
            for (var key : searchParameters.keySet()) {
                var flattenLists =
                        searchParameters.get(key).stream().flatMap(List::stream).toList();

                params.put(key, flattenLists);
            }
            searchParameters.entries().forEach(p -> params.put(p.getKey(), p.getValue()));
        }
        return search(bundleType, resourceType, params, Collections.emptyMap());
    }

    @Override
    public <B extends IBaseBundle, T extends IBaseResource> B search(
            Class<B> bundleType,
            Class<T> resourceType,
            Map<String, List<IQueryParameterType>> searchParameters,
            Map<String, String> headers) {
        var op = this.client.search().forResource(resourceType).returnBundle(bundleType);
        if (searchParameters != null) {
            op = op.where(searchParameters);
        }

        if (headers != null) {
            for (var entry : headers.entrySet()) {
                op = op.withAdditionalHeader(entry.getKey(), entry.getValue());
            }
        }

        return this.addHeaders(op, headers).execute();
    }

    @Override
    public <C extends IBaseConformance> C capabilities(Class<C> resourceType, Map<String, String> headers) {
        var op = this.client.capabilities().ofType(resourceType);
        return this.addHeaders(op, headers).execute();
    }

    @Override
    public <B extends IBaseBundle> B transaction(B transaction, Map<String, String> headers) {
        var op = this.client.transaction().withBundle(transaction);
        return this.addHeaders(op, headers).execute();
    }

    @Override
    public <B extends IBaseBundle> B link(Class<B> bundleType, String url, Map<String, String> headers) {
        var op = this.client.loadPage().byUrl(url).andReturnBundle(bundleType);
        return this.addHeaders(op, headers).execute();
    }

    @Override
    public <R extends IBaseResource, P extends IBaseParameters> R invoke(
            String name, P parameters, Class<R> returnType, Map<String, String> headers) {
        var op = this.client
                .operation()
                .onServer()
                .named(name)
                .withParameters(parameters)
                .returnResourceType(returnType);
        return this.addHeaders(op, headers).execute();
    }

    @Override
    public <P extends IBaseParameters> MethodOutcome invoke(String name, P parameters, Map<String, String> headers) {
        var op = this.client
                .operation()
                .onServer()
                .named(name)
                .withParameters(parameters)
                .returnMethodOutcome();
        return this.addHeaders(op, headers).execute();
    }

    @Override
    public <R extends IBaseResource, P extends IBaseParameters, T extends IBaseResource> R invoke(
            Class<T> resourceType, String name, P parameters, Class<R> returnType, Map<String, String> headers) {
        var op = this.client
                .operation()
                .onType(resourceType)
                .named(name)
                .withParameters(parameters)
                .returnResourceType(returnType);
        return this.addHeaders(op, headers).execute();
    }

    @Override
    public <P extends IBaseParameters, T extends IBaseResource> MethodOutcome invoke(
            Class<T> resourceType, String name, P parameters, Map<String, String> headers) {
        var op = this.client
                .operation()
                .onType(resourceType)
                .named(name)
                .withParameters(parameters)
                .returnMethodOutcome();
        return this.addHeaders(op, headers).execute();
    }

    @Override
    public <R extends IBaseResource, P extends IBaseParameters, I extends IIdType> R invoke(
            I id, String name, P parameters, Class<R> returnType, Map<String, String> headers) {
        var op = this.client
                .operation()
                .onInstance(id)
                .named(name)
                .withParameters(parameters)
                .returnResourceType(returnType);
        return this.addHeaders(op, headers).execute();
    }

    @Override
    public <P extends IBaseParameters, I extends IIdType> MethodOutcome invoke(
            I id, String name, P parameters, Map<String, String> headers) {
        var op = this.client
                .operation()
                .onInstance(id)
                .named(name)
                .withParameters(parameters)
                .returnMethodOutcome();
        return this.addHeaders(op, headers).execute();
    }

    @Override
    public <B extends IBaseBundle, P extends IBaseParameters> B history(
            P parameters, Class<B> returnType, Map<String, String> headers) {
        var op = this.client.history().onServer().returnBundle(returnType);
        this.addHistoryParams(null, parameters);
        return this.addHeaders(op, headers).execute();
    }

    @Override
    public <B extends IBaseBundle, P extends IBaseParameters, T extends IBaseResource> B history(
            Class<T> resourceType, P parameters, Class<B> returnType, Map<String, String> headers) {
        var op = this.client.history().onType(resourceType).returnBundle(returnType);
        this.addHistoryParams(null, parameters);
        return this.addHeaders(op, headers).execute();
    }

    @Override
    public <B extends IBaseBundle, P extends IBaseParameters, I extends IIdType> B history(
            I id, P parameters, Class<B> returnType, Map<String, String> headers) {
        var op = this.client.history().onInstance(id).returnBundle(returnType);
        this.addHistoryParams(null, parameters);
        return this.addHeaders(op, headers).execute();
    }

    @Override
    public FhirContext fhirContext() {
        return this.getClient().getFhirContext();
    }

    @SuppressWarnings("unchecked")
    protected <B extends IBaseBundle, P extends IBaseParameters> void addHistoryParams(
            IHistoryTyped<B> operation, P parameters) {

        var ctx = this.client.getFhirContext();
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
