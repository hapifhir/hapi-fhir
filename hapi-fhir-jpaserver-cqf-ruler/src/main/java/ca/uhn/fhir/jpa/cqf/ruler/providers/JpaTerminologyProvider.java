package ca.uhn.fhir.jpa.cqf.ruler.providers;

import ca.uhn.fhir.jpa.provider.dstu3.JpaResourceProviderDstu3;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.terminology.CodeSystemInfo;
import org.opencds.cqf.cql.terminology.TerminologyProvider;
import org.opencds.cqf.cql.terminology.ValueSetInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher Schuler on 7/17/2017.
 */
public class JpaTerminologyProvider implements TerminologyProvider {
    private JpaResourceProviderDstu3<ValueSet> valueSetProvider;
    private JpaResourceProviderDstu3<CodeSystem> codeSystemProvider;

    public JpaTerminologyProvider(JpaResourceProviderDstu3<ValueSet> valueSetProvider, JpaResourceProviderDstu3<CodeSystem> codeSystemProvider) {
        this.valueSetProvider = valueSetProvider;
        this.codeSystemProvider = codeSystemProvider;
    }

    @Override
    public boolean in(Code code, ValueSetInfo valueSet) throws ResourceNotFoundException {
        for (Code c : expand(valueSet)) {
            if (c == null) continue;
            if (c.getCode().equals(code.getCode()) && c.getSystem().equals(code.getSystem())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterable<Code> expand(ValueSetInfo valueSet) throws ResourceNotFoundException {
        ValueSet vs = valueSetProvider.getDao().read(new IdType(valueSet.getId()));
        List<Code> codes = new ArrayList<>();
        for (ValueSet.ValueSetExpansionContainsComponent expansion : vs.getExpansion().getContains()) {
            codes.add(new Code().withCode(expansion.getCode()).withSystem(expansion.getSystem()));
        }
        return codes;
    }

    @Override
    public Code lookup(Code code, CodeSystemInfo codeSystem) throws ResourceNotFoundException {
        CodeSystem cs = codeSystemProvider.getDao().read(new IdType(codeSystem.getId()));
        for (CodeSystem.ConceptDefinitionComponent concept : cs.getConcept()) {
            if (concept.getCode().equals(code.getCode()))
                return code.withSystem(codeSystem.getId()).withDisplay(concept.getDisplay());
        }
        return code;
    }
}
