package ca.uhn.fhir.cql.common.helper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// NOTICE: This is trash code. It's for a one-off. Don't fix it.
public class ClientHelperDos {

    public static IGenericClient getClient(FhirContext fhirContext, String url) {
        fhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
        return fhirContext.newRestfulGenericClient(url);
    }

    // Overload in case you need to specify a specific version of the context
    public static IGenericClient getClient(FhirContext fhirContext, org.hl7.fhir.dstu3.model.Endpoint endpoint) {
        IGenericClient client = getClient(fhirContext, endpoint.getAddress());
        if (endpoint.hasHeader()) {
            List<String> headerList = endpoint.getHeader().stream().map(headerString -> headerString.asStringValue())
                    .collect(Collectors.toList());
            registerAuth(client, headerList);
        }
        return client;
    }

    public static IGenericClient getClient(FhirContext fhirContext, org.hl7.fhir.r4.model.Endpoint endpoint) {
        IGenericClient client = getClient(fhirContext, endpoint.getAddress());
        if (endpoint.hasHeader()) {
            List<String> headerList = endpoint.getHeader().stream().map(headerString -> headerString.asStringValue())
                    .collect(Collectors.toList());
            registerAuth(client, headerList);
        }
        return client;
    }

    private static void registerAuth(IGenericClient client, List<String> headerList) {
        Map<String, String> headerMap = setupHeaderMap(headerList);
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            IClientInterceptor headInterceptor = new org.opencds.cqf.cql.engine.fhir.terminology.HeaderInjectionInterceptor(
                    entry.getKey(), entry.getValue());
            client.registerInterceptor(headInterceptor);
        }
    }

    private static Map<String, String> setupHeaderMap(List<String> headerList) {
        Map<String, String> headerMap = new HashMap<String, String>();
        String leftAuth = null;
        String rightAuth = null;
        if (headerList.size() < 1 || headerList.isEmpty()) {
            leftAuth = null;
            rightAuth = null;
            headerMap.put(leftAuth, rightAuth);
        } else {
            for (String header : headerList) {
                if (!header.contains(":")) {
                    throw new RuntimeException("Endpoint header must contain \":\" .");
                }
                String[] authSplit = header.split(":");
                leftAuth = authSplit[0];
                rightAuth = authSplit[1];
                headerMap.put(leftAuth, rightAuth);
            }

        }
        return headerMap;
    }

}
