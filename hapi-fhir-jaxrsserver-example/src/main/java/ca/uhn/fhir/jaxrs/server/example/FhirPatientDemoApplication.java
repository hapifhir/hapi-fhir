package ca.uhn.fhir.jaxrs.server.example;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Fhir Patient Demo Application
 * 
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
@ApplicationPath(value=FhirPatientDemoApplication.PATH)
public class FhirPatientDemoApplication extends Application {
    /** The demo application path */
    public final static String PATH = "/jaxrs-demo";
}
