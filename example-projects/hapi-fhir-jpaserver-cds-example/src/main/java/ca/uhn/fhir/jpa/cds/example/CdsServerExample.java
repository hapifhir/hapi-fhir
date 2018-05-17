package ca.uhn.fhir.jpa.cds.example;

import org.opencds.cqf.servlet.BaseServlet;

import javax.servlet.ServletException;

public class CdsServerExample extends BaseServlet {

    // Default setup - STU3 support only
    // Source project location: https://github.com/DBCG/cqf-ruler

    @SuppressWarnings("unchecked")
    @Override
    protected void initialize() throws ServletException {
        super.initialize();

        // Add additional config and/or resource providers
    }
}
