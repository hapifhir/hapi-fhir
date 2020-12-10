package ca.uhn.fhir.cql.common.provider;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.ValueSet;
import org.opencds.cqf.cql.engine.fhir.terminology.R4FhirTerminologyProvider;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.terminology.ValueSetInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class R4ApelonFhirTerminologyProvider extends R4FhirTerminologyProvider {
    private Map<String, List<Code>> cache = new HashMap<>();

    public R4ApelonFhirTerminologyProvider() {
        super();
    }

    public R4ApelonFhirTerminologyProvider(IGenericClient fhirClient) {
        super(fhirClient);
    }

    @Override
    public Iterable<Code> expand(ValueSetInfo valueSet) throws ResourceNotFoundException {
        String id = valueSet.getId();
        if (this.cache.containsKey(id)) {
            return this.cache.get(id);
        }

        String url = this.resolveByIdentifier(valueSet);

        Parameters respParam = this.getFhirClient().operation().onType(ValueSet.class).named("expand")
                .withSearchParameter(Parameters.class, "url", new StringParam(url))
                .andSearchParameter("includeDefinition", new StringParam("true")).useHttpGet().execute();

        ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();
        List<Code> codes = new ArrayList<>();
        for (ValueSet.ValueSetExpansionContainsComponent codeInfo : expanded.getExpansion().getContains()) {
            Code nextCode = new Code().withCode(codeInfo.getCode()).withSystem(codeInfo.getSystem())
                    .withVersion(codeInfo.getVersion()).withDisplay(codeInfo.getDisplay());
            codes.add(nextCode);
        }

        this.cache.put(id, codes);
        return codes;
    }

    public String resolveByIdentifier(ValueSetInfo valueSet) {
        String valueSetId = valueSet.getId();

        // Turns out we got a FHIR url. Let's use that.
        if (valueSetId.startsWith("http")) {
            return valueSetId;
        }

        valueSetId = valueSetId.replace("urn:oid:", "");

        IQuery<Bundle> bundleQuery = this.getFhirClient().search().byUrl("ValueSet?identifier=" + valueSetId)
                .returnBundle(Bundle.class).accept("application/fhir+xml");

        Bundle searchResults = bundleQuery.execute();

        if (searchResults.hasEntry()) {
            for (BundleEntryComponent bec : searchResults.getEntry()) {
                if (bec.hasResource()) {
                    String id = bec.getResource().getIdElement().getIdPart();
                    if (id.equals(valueSetId)) {
                        return ((ValueSet) bec.getResource()).getUrl();
                    }
                }

            }
        }

        return null;
    }
}
