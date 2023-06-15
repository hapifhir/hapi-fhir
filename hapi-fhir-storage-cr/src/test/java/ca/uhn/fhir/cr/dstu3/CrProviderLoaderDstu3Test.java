package ca.uhn.fhir.cr.dstu3;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.uhn.fhir.cr.BaseCrDstu3Test;
import ca.uhn.fhir.cr.config.CrProviderLoader;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;

class CrProviderLoaderDstu3Test extends BaseCrDstu3Test {
    private static final Logger ourLog = LoggerFactory.getLogger(CrProviderLoaderDstu3Test.class);

    @Autowired CrProviderLoader myCrProviderLoader;

    @Autowired private ResourceProviderFactory myResourceProviderFactory;

    @Test
    public void testContextLoads() {
        myResourceProviderFactory.createProviders();
        ourLog.info("The CqlProviderLoader loaded and was able to create Providers.");
    }
}
