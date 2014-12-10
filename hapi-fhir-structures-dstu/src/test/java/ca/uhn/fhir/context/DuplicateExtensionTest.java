package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.primitive.StringDt;
import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by Bill de Beaubien on 12/10/2014.
 */
public class DuplicateExtensionTest extends TestCase {
    @Test
    public void testScannerShouldAddProvidedResources() {
//        FhirContext ctx = new FhirContext();
//        RuntimeResourceDefinition patientDef = ctx.getResourceDefinition(CustomPatient.class);
//        patientDef.toProfile();
//
//        RuntimeResourceDefinition observationDef = ctx.getResourceDefinition(CustomObservation.class);
//        observationDef.toProfile();
    }

    @ProvidesResources(resources = CustomObservation.class)
    class CustomObservationProvider  {
    }

    @ProvidesResources(resources = CustomPatient.class)
    class CustomPatientProvider  {
    }

    @ResourceDef(name = "Patient", id = "CustomPatient")
    class CustomPatient extends Patient {
        @Child(name="idExt", order = 0)
        @Extension(url= "http://foo.org#id", definedLocally=true, isModifier=false)
        @Description(shortDefinition = "Contains the id of the resource")
        private StringDt myIdExt;

        public StringDt getIdExt() {
            if (myIdExt == null) {
                myIdExt = new StringDt();
            }
            return myIdExt;
        }

        public void setIdExt(StringDt theIdExt) {
            myIdExt = theIdExt;
        }
    }

    @ResourceDef(name = "Observation", id = "CustomObservation")
    class CustomObservation extends Observation {
        @Child(name="idExt", order = 0)
        @Extension(url= "http://foo.org#id", definedLocally=true, isModifier=false)
        @Description(shortDefinition = "Contains the id of the resource")
        private StringDt myIdExt;

        public StringDt getIdExt() {
            if (myIdExt == null) {
                myIdExt = new StringDt();
            }
            return myIdExt;
        }

        public void setIdExt(StringDt theIdExt) {
            myIdExt = theIdExt;
        }
    }
}
