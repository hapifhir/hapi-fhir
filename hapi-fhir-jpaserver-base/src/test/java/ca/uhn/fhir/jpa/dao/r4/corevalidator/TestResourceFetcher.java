package ca.uhn.fhir.jpa.dao.r4.corevalidator;

import ca.uhn.fhir.jpa.dao.r4.corevalidator.gson.TestEntry;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r5.elementmodel.Element;
import org.hl7.fhir.r5.elementmodel.Manager;
import org.hl7.fhir.r5.elementmodel.ObjectConverter;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.test.utils.TestingUtilities;
import org.hl7.fhir.r5.utils.IResourceValidator;

import java.io.IOException;

public class TestResourceFetcher implements IResourceValidator.IValidatorResourceFetcher {

    private final TestEntry testEntry;

    public TestResourceFetcher(TestEntry testEntry) {
        this.testEntry = testEntry;
    }

    @Override
    public Element fetch(Object appContext, String url) throws IOException, FHIRException {
        Element res = null;
        if (url.equals("Patient/test")) {
            res = new ObjectConverter(TestingUtilities.context()).convert(new Patient());
        } else if (TestingUtilities.findTestResource("validator", url.replace("/", "-").toLowerCase()+".json")) {
            res = Manager.makeParser(TestingUtilities.context(), Manager.FhirFormat.JSON)
                    .parse(TestingUtilities.loadTestResourceStream("validator", url.replace("/", "-").toLowerCase()+".json"));
        } else if (TestingUtilities.findTestResource("validator", url.replace("/", "-").toLowerCase()+".xml")) {
            res = Manager.makeParser(TestingUtilities.context(), Manager.FhirFormat.XML)
                    .parse(TestingUtilities.loadTestResourceStream("validator", url.replace("/", "-").toLowerCase()+".xml"));
        }
        if (res == null && url.contains("/")) {
            String tail = url.substring(url.indexOf("/")+1);
            if (TestingUtilities.findTestResource("validator", tail.replace("/", "-").toLowerCase()+".json")) {
                res = Manager.makeParser(TestingUtilities.context(), Manager.FhirFormat.JSON)
                        .parse(TestingUtilities.loadTestResourceStream("validator", tail.replace("/", "-").toLowerCase()+".json"));
            } else if (TestingUtilities.findTestResource("validator", tail.replace("/", "-").toLowerCase()+".xml")) {
                res =  Manager.makeParser(TestingUtilities.context(), Manager.FhirFormat.XML)
                        .parse(TestingUtilities.loadTestResourceStream("validator", tail.replace("/", "-").toLowerCase()+".xml"));
            }
        }
        return res;
    }

    @Override
    public IResourceValidator.ReferenceValidationPolicy validationPolicy(Object appContext, String path, String url) {
        if (testEntry.getValidate() != null)
            return IResourceValidator.ReferenceValidationPolicy.valueOf(testEntry.getValidate());
        else
            return IResourceValidator.ReferenceValidationPolicy.IGNORE;
    }

    @Override
    public boolean resolveURL(Object appContext, String path, String url) throws FHIRException {
        return !url.contains("example.org");
    }

}
