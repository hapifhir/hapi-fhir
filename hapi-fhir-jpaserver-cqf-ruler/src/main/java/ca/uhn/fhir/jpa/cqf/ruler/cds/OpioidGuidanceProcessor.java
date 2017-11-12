package ca.uhn.fhir.jpa.cqf.ruler.cds;

import ca.uhn.fhir.jpa.rp.dstu3.LibraryResourceProvider;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelInfoLoader;
import org.cqframework.cql.cql2elm.ModelInfoProvider;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.execution.Library;
import org.hl7.elm.r1.VersionedIdentifier;
import org.hl7.elm_modelinfo.r1.ModelInfo;
import org.hl7.fhir.dstu3.model.PlanDefinition;
import org.hl7.fhir.exceptions.FHIRException;
import ca.uhn.fhir.jpa.cqf.ruler.config.STU3LibraryLoader;
import ca.uhn.fhir.jpa.cqf.ruler.config.STU3LibrarySourceProvider;
import org.opencds.cqf.cql.data.fhir.BaseFhirDataProvider;
import org.opencds.cqf.cql.data.fhir.FhirDataProviderStu3;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.execution.LibraryLoader;
import ca.uhn.fhir.jpa.cqf.ruler.omtk.OmtkDataProvider;

import javax.xml.bind.JAXB;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class OpioidGuidanceProcessor extends MedicationPrescribeProcessor {

    private ModelManager modelManager;
    private ModelManager getModelManager() {
        if (modelManager == null) {
            modelManager = new ModelManager();
            ModelInfoProvider infoProvider = () -> {
                Path p = Paths.get("src/main/resources/cds/OMTK-modelinfo-0.1.0.xml").toAbsolutePath();
                return JAXB.unmarshal(new File(p.toString()), ModelInfo.class);
            };
            ModelInfoLoader.registerModelInfoProvider(new VersionedIdentifier().withId("OMTK").withVersion("0.1.0"), infoProvider);
        }
        return modelManager;
    }

    private LibraryManager libraryManager;
    private LibraryManager getLibraryManager() {
        if (libraryManager == null) {
            libraryManager = new LibraryManager(getModelManager());
            libraryManager.getLibrarySourceLoader().clearProviders();
            libraryManager.getLibrarySourceLoader().registerProvider(getLibrarySourceProvider());
        }
        return libraryManager;
    }

    private LibraryLoader libraryLoader;
    private LibraryLoader getLibraryLoader() {
        if (libraryLoader == null) {
            libraryLoader = new STU3LibraryLoader(libraryResourceProvider, getLibraryManager(), getModelManager());
        }
        return libraryLoader;
    }

    private STU3LibrarySourceProvider librarySourceProvider;
    private STU3LibrarySourceProvider getLibrarySourceProvider() {
        if (librarySourceProvider == null) {
            librarySourceProvider = new STU3LibrarySourceProvider(libraryResourceProvider);
        }
        return librarySourceProvider;
    }

    public OpioidGuidanceProcessor(CdsHooksRequest request, PlanDefinition planDefinition, LibraryResourceProvider libraryResourceProvider)
            throws FHIRException
    {
        super(request, planDefinition, libraryResourceProvider);
    }

    @Override
    public List<CdsCard> process() {

        // read opioid library
        Library library = getLibraryLoader().load(new org.cqframework.cql.elm.execution.VersionedIdentifier().withId("OpioidCdsStu3").withVersion("0.1.0"));

        // resolve data providers
        // the db file is issued to properly licensed implementers -- see README for more info
        String path = Paths.get("src/main/resources/cds/OpioidManagementTerminologyKnowledge.db").toAbsolutePath().toString().replace("\\", "/");
        String connString = "jdbc:sqlite://" + path;
        OmtkDataProvider omtkProvider = new OmtkDataProvider(connString);
        BaseFhirDataProvider dstu3Provider = new FhirDataProviderStu3().setEndpoint(request.getFhirServerEndpoint());

        Context executionContext = new Context(library);
        executionContext.registerLibraryLoader(getLibraryLoader());
        executionContext.registerDataProvider("http://org.opencds/opioid-cds", omtkProvider);
        executionContext.registerDataProvider("http://hl7.org/fhir", dstu3Provider);
        executionContext.setExpressionCaching(true);
//        executionContext.setEnableTraceLogging(true);
        executionContext.setParameter(null, "Orders", activePrescriptions);

        List<CdsCard> cards = resolveActions(executionContext);
        if (cards.isEmpty()) {
            cards.add(
                    new CdsCard()
                            .setSummary("Success")
                            .setDetail("Prescription satisfies recommendation #5 of the cdc opioid guidance.")
                            .setIndicator("info")
                            .setLinks(
                                    Collections.singletonList(
                                            new CdsCard.Links()
                                                    .setLabel("CDC Recommendations for prescribing opioids")
                                                    .setUrl("https://guidelines.gov/summaries/summary/50153/cdc-guideline-for-prescribing-opioids-for-chronic-pain---united-states-2016#420")
                                    )
                            )
            );
        }

        return cards;
    }
}
