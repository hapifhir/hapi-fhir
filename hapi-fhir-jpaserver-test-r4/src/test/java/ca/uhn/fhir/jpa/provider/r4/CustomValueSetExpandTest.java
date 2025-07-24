package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.term.TermLoaderSvcImpl;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.ClasspathUtil;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomValueSetExpandTest extends BaseResourceProviderR4Test {

  private static final Logger ourLog = LoggerFactory.getLogger(DiffProviderR4Test.class);

  @Autowired
  private FhirContext myFhirCtx;

  @Test
  public void testExpandAndPrint() throws Exception {
    // 1. Use low-level API to directly load ICD-10-CM CodeSystem (changed to 2023 version)
    TermLoaderSvcImpl termLoaderSvc = new TermLoaderSvcImpl(myTerminologyDeferredStorageSvc, myTermCodeSystemStorageSvc);
    
    // Load 2023 version of ICD-10-CM file
    String filename = "icd10cm-tabular-2023.xml";  // Use your 2023 version file
    
    String resource = ClasspathUtil.loadResource(filename);
    List<ITermLoaderSvc.FileDescriptor> descriptors = new ArrayList<>();
    descriptors.add(new ITermLoaderSvc.ByteArrayFileDescriptor(filename, resource.getBytes(StandardCharsets.UTF_8)));
    
    // Load ICD-10-CM
    termLoaderSvc.loadIcd10cm(descriptors, new SystemRequestDetails());
    myTerminologyDeferredStorageSvc.saveAllDeferred();
    
    // Verify loading success
    runInTransaction(() -> {
      TermCodeSystem codeSystem = myTermCodeSystemDao.findByCodeSystemUri(ITermLoaderSvc.ICD10CM_URI);
      ourLog.info("Loaded ICD-10-CM Version: " + codeSystem.getCurrentVersion().getCodeSystemVersionId());
      ourLog.info("Loaded concept count: " + myTermConceptDao.count());
    });


    // 2. Read and create ValueSet
    String vsJson = ClasspathUtil.loadResource("/my-valueset.json");
    ValueSet vs = myFhirCtx.newJsonParser().parseResource(ValueSet.class, vsJson);

    // Check ValueSet composition
    ourLog.info("ValueSet URL: " + vs.getUrl());
    ourLog.info("ValueSet compose includes:");
    vs.getCompose().getInclude().forEach(include -> {
      ourLog.info("  System: " + include.getSystem() + " Version: " + include.getVersion());
      if (include.hasFilter()) {
        include.getFilter().forEach(filter -> {
          ourLog.info("    Filter: " + filter.getProperty() + " " + filter.getOp() + " " + filter.getValue());
        });
      }
      include.getConcept().forEach(concept -> {
        ourLog.info("    Code: " + concept.getCode() + " Display: " + concept.getDisplay());
      });
    });

    myClient.create().resource(vs).execute();

    // 3. Assemble input Parameters and call $expand
    Parameters inParams = new Parameters();
    inParams.addParameter().setName("url").setValue(vs.getUrlElement());

    ValueSet expanded = null;
    int maxRetries = 3;
    int currentRetry = 0;
    
    while (expanded == null && currentRetry < maxRetries) {
      try {
        ourLog.info("Attempting to expand ValueSet (attempt " + (currentRetry + 1) + ")...");
        expanded = myClient
            .operation()
            .onType(ValueSet.class)
            .named("$expand")
            .withParameters(inParams)
            .returnResourceType(ValueSet.class)
            .execute();
        ourLog.info("ValueSet expansion successful!");
      } catch (Exception e) {
        currentRetry++;
        ourLog.info("Expansion failed (attempt " + currentRetry + "): " + e.getMessage());
        if (currentRetry < maxRetries) {
          ourLog.info("Waiting 5 seconds before retry...");
          Thread.sleep(5000);
        } else {
          ourLog.error("All retries failed, throwing exception");
          throw e;
        }
      }
    }

    // 4. Print all contains
    expanded.getExpansion().getContains().forEach(c -> {
      ourLog.info(
          "system=" + c.getSystem() +
              " code=" + c.getCode() +
              " display=" + c.getDisplay());
    }); 

    ourLog.info("Expansion successful! Found " + expanded.getExpansion().getContains().size() + " concepts.");

  }
}