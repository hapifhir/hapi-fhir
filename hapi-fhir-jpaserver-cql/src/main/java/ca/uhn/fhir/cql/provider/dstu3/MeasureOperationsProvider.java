package ca.uhn.fhir.cql.provider.dstu3;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.rp.dstu3.MeasureResourceProvider;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Composition;
import org.hl7.fhir.dstu3.model.DetectedIssue;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.Group;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Library;
import org.hl7.fhir.dstu3.model.ListResource;
import org.hl7.fhir.dstu3.model.Measure;
import org.hl7.fhir.dstu3.model.MeasureReport;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.Narrative;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.RelatedArtifact;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;

import org.opencds.cqf.cql.engine.data.DataProvider;
import org.opencds.cqf.cql.engine.execution.LibraryLoader;
import org.opencds.cqf.tooling.library.stu3.NarrativeProvider;
import org.opencds.cqf.tooling.measure.stu3.CqfMeasure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class MeasureOperationsProvider {

    private NarrativeProvider narrativeProvider;
    private HQMFProvider hqmfProvider;
    private DataRequirementsProvider dataRequirementsProvider;

    private LibraryResolutionProvider<Library> libraryResolutionProvider;
    private MeasureResourceProvider measureResourceProvider;
    private DaoRegistry registry;
    private EvaluationProviderFactory factory;

    private static final Logger logger = LoggerFactory.getLogger(MeasureOperationsProvider.class);

    @Autowired
    public MeasureOperationsProvider(DaoRegistry registry, EvaluationProviderFactory factory,
                                     NarrativeProvider narrativeProvider, HQMFProvider hqmfProvider,
                                     LibraryResolutionProvider<Library> libraryResolutionProvider,
                                     MeasureResourceProvider measureResourceProvider) {
        this.registry = registry;
        this.factory = factory;

        this.libraryResolutionProvider = libraryResolutionProvider;
        this.narrativeProvider = narrativeProvider;
        this.hqmfProvider = hqmfProvider;
        this.dataRequirementsProvider = new DataRequirementsProvider();
        this.measureResourceProvider = measureResourceProvider;
    }

    @Operation(name = "$hqmf", idempotent = true, type = Measure.class)
    public Parameters hqmf(@IdParam IdType theId) {
        Measure theResource = this.measureResourceProvider.getDao().read(theId);
        String hqmf = this.generateHQMF(theResource);
        Parameters p = new Parameters();
        p.addParameter().setValue(new StringType(hqmf));
        return p;
    }

    @Operation(name = "$refresh-generated-content", type = Measure.class)
    public MethodOutcome refreshGeneratedContent(RequestDetails theRequestDetails,
            @IdParam IdType theId) {
        Measure theResource = this.measureResourceProvider.getDao().read(theId);

        theResource.getRelatedArtifact().removeIf(
                relatedArtifact -> relatedArtifact.getType().equals(RelatedArtifact.RelatedArtifactType.DEPENDSON));

        CqfMeasure cqfMeasure = this.dataRequirementsProvider.createCqfMeasure(theResource,
                this.libraryResolutionProvider);

        // Ensure All Related Artifacts for all referenced Libraries
        if (!cqfMeasure.getRelatedArtifact().isEmpty()) {
            for (RelatedArtifact relatedArtifact : cqfMeasure.getRelatedArtifact()) {
                boolean artifactExists = false;
                // logger.info("Related Artifact: " + relatedArtifact.getUrl());
                for (RelatedArtifact resourceArtifact : theResource.getRelatedArtifact()) {
                    if (resourceArtifact.equalsDeep(relatedArtifact)) {
                        // logger.info("Equals deep true");
                        artifactExists = true;
                        break;
                    }
                }
                if (!artifactExists) {
                    theResource.addRelatedArtifact(relatedArtifact.copy());
                }
            }
        }
        
        try {
            Narrative n = this.narrativeProvider.getNarrative(this.measureResourceProvider.getContext(), cqfMeasure);
            theResource.setText(n.copy());
        } catch (Exception e) {
            logger.info("Error generating narrative", e);
        }

        // logger.info("Narrative: " + n.getDivAsString());
        return this.measureResourceProvider.search(theResource, theId,
                theRequestDetails.getConditionalUrl(RestOperationTypeEnum.UPDATE), theRequestDetails);
    }

    @Operation(name = "$get-narrative", idempotent = true, type = Measure.class)
    public Parameters getNarrative(@IdParam IdType theId) {
        Measure theResource = this.measureResourceProvider.getDao().read(theId);
        CqfMeasure cqfMeasure = this.dataRequirementsProvider.createCqfMeasure(theResource,
                this.libraryResolutionProvider);
        Narrative n = this.narrativeProvider.getNarrative(this.measureResourceProvider.getContext(), cqfMeasure);
        Parameters p = new Parameters();
        p.addParameter().setValue(new StringType(n.getDivAsString()));
        return p;
    }

    private String generateHQMF(Measure theResource) {
        CqfMeasure cqfMeasure = this.dataRequirementsProvider.createCqfMeasure(theResource,
                this.libraryResolutionProvider);
        return this.hqmfProvider.generateHQMF(cqfMeasure);
    }

    /*
     *
     * NOTE that the source, user, and pass parameters are not standard parameters
     * for the FHIR $evaluate-measure operation
     *
     */
    @Operation(name = "$evaluate-measure", idempotent = true, type = Measure.class)
    public MeasureReport evaluateMeasure(@IdParam IdType theId,
            @OperationParam(name = "periodStart") String periodStart,
            @OperationParam(name = "periodEnd") String periodEnd, @OperationParam(name = "measure") String measureRef,
            @OperationParam(name = "reportType") String reportType, @OperationParam(name = "patient") String patientRef,
            @OperationParam(name = "productLine") String productLine,
            @OperationParam(name = "practitioner") String practitionerRef,
            @OperationParam(name = "lastReceivedOn") String lastReceivedOn,
            @OperationParam(name = "source") String source, @OperationParam(name = "user") String user,
            @OperationParam(name = "pass") String pass) throws InternalErrorException, FHIRException {
        LibraryLoader libraryLoader = LibraryHelper.createLibraryLoader(this.libraryResolutionProvider);
        MeasureEvaluationSeed seed = new MeasureEvaluationSeed(this.factory, libraryLoader,
                this.libraryResolutionProvider);
        Measure measure = this.measureResourceProvider.getDao().read(theId);

        if (measure == null) {
            throw new RuntimeException("Could not find Measure/" + theId.getIdPart());
        }

        seed.setup(measure, periodStart, periodEnd, productLine, source, user, pass);

        // resolve report type
        MeasureEvaluation evaluator = new MeasureEvaluation(this.registry,
                seed.getMeasurementPeriod());
        if (reportType != null) {
            switch (reportType) {
                case "patient":
                    return evaluator.evaluatePatientMeasure(seed.getMeasure(), seed.getContext(), patientRef);
                case "patient-list":
                    return evaluator.evaluatePatientListMeasure(seed.getMeasure(), seed.getContext(), practitionerRef);
                case "population":
                    return evaluator.evaluatePopulationMeasure(seed.getMeasure(), seed.getContext());
                default:
                    throw new IllegalArgumentException("Invalid report type: " + reportType);
            }
        }

        // default report type is patient
        MeasureReport report = evaluator.evaluatePatientMeasure(seed.getMeasure(), seed.getContext(), patientRef);
        if (productLine != null) {
            Extension ext = new Extension();
            ext.setUrl("http://hl7.org/fhir/us/cqframework/cqfmeasures/StructureDefinition/cqfm-productLine");
            ext.setValue(new StringType(productLine));
            report.addExtension(ext);
        }

        return report;
    }

    // @Operation(name = "$evaluate-measure-with-source", idempotent = true)
    // public MeasureReport evaluateMeasure(@IdParam IdType theId,
    // @OperationParam(name = "sourceData", min = 1, max = 1, type = Bundle.class)
    // Bundle sourceData,
    // @OperationParam(name = "periodStart", min = 1, max = 1) String periodStart,
    // @OperationParam(name = "periodEnd", min = 1, max = 1) String periodEnd) {
    // if (periodStart == null || periodEnd == null) {
    // throw new IllegalArgumentException("periodStart and periodEnd are required
    // for measure evaluation");
    // }
    // LibraryLoader libraryLoader =
    // LibraryHelper.createLibraryLoader(this.libraryResourceProvider);
    // MeasureEvaluationSeed seed = new MeasureEvaluationSeed(this.factory,
    // libraryLoader, this.libraryResourceProvider);
    // Measure measure = this.getDao().read(theId);

    // if (measure == null) {
    // throw new RuntimeException("Could not find Measure/" + theId.getIdPart());
    // }

    // seed.setup(measure, periodStart, periodEnd, null, null, null, null);
    // BundleDataProviderStu3 bundleProvider = new
    // BundleDataProviderStu3(sourceData);
    // bundleProvider.setTerminologyProvider(provider.getTerminologyProvider());
    // seed.getContext().registerDataProvider("http://hl7.org/fhir",
    // bundleProvider);
    // MeasureEvaluation evaluator = new MeasureEvaluation(bundleProvider,
    // seed.getMeasurementPeriod());
    // return evaluator.evaluatePatientMeasure(seed.getMeasure(), seed.getContext(),
    // "");
    // }

    @Operation(name = "$care-gaps", idempotent = true, type = Measure.class)
    public Parameters careGapsReport(@OperationParam(name = "periodStart") String periodStart,
                                     @OperationParam(name = "periodEnd") String periodEnd, @OperationParam(name = "subject") String subject,
                                     @OperationParam(name = "topic") String topic,@OperationParam(name = "practitioner") String practitioner,
                                     @OperationParam(name = "measure") String measure, @OperationParam(name="status")String status,
                                     @OperationParam(name = "organization") String organization){
        //TODO: status - optional if null all gaps - if closed-gap code only those gaps that are closed if open-gap code only those that are open
        //TODO: topic should allow many and be a union of them
        //TODO: "The Server needs to make sure that practitioner is authorized to get the gaps in care report for and know what measures the practitioner are eligible or qualified."
        Parameters returnParams = new Parameters();
        if(careGapParameterValidation(periodStart, periodEnd, subject, topic, practitioner, measure, status, organization)) {
            if(subject.startsWith("Patient/")){
                returnParams.addParameter(new Parameters.ParametersParameterComponent()
                        .setName("Gaps in Care Report - " + subject)
                        .setResource(patientCareGap(periodStart, periodEnd, subject, topic, measure, status)));
                return returnParams;
            }else if(subject.startsWith("Group/")) {
                returnParams.setId((status==null?"all-gaps": status) + "-" + subject.replace("/","_") + "-report");
                (getPatientListFromGroup(subject))
                    .forEach(groupSubject ->{
                        Bundle patientGapBundle = patientCareGap(periodStart, periodEnd, groupSubject, topic, measure, status);
                        if(null != patientGapBundle){
                            returnParams.addParameter(new Parameters.ParametersParameterComponent()
                                    .setName("Gaps in Care Report - " + groupSubject)
                                    .setResource(patientGapBundle));
                        }
                    });
            }
            return returnParams;
        }
        if (practitioner == null || practitioner.equals("")) {
            return new Parameters().addParameter(
                    new Parameters.ParametersParameterComponent()
                            .setName("Gaps in Care Report - " + subject)
                            .setResource(patientCareGap(periodStart, periodEnd, subject, topic, measure,status)));
        }
        return returnParams;
    }

    private List<String> getPatientListFromGroup(String subjectGroupRef){
        List<String> patientList = new ArrayList<>();

        DataProvider dataProvider = this.factory.createDataProvider("FHIR", "3");
        Iterable<Object> groupRetrieve = dataProvider.retrieve("Group", "id", subjectGroupRef, "Group", null, null, null,
                null, null, null, null, null);
        Group group;
        if (groupRetrieve.iterator().hasNext()) {
            group = (Group) groupRetrieve.iterator().next();
            group.getMember().forEach(member -> patientList.add(member.getEntity().getReference()));
        }
        return patientList;
    }

    @SuppressWarnings("unused")
    private Boolean careGapParameterValidation(String periodStart, String periodEnd, String subject, String topic,
                                               String practitioner, String measure, String status, String organization){
        if(periodStart == null || periodStart.equals("") ||
            periodEnd == null || periodEnd.equals("")){
            throw new IllegalArgumentException("periodStart and periodEnd are required.");
        }
        //TODO - remove this - covered in check of subject/practitioner/organization - left in for now 'cause we need a subject to develop
        if (subject == null || subject.equals("")) {
            throw new IllegalArgumentException("Subject is required.");
        }
        if(null != subject) {
            if (!subject.startsWith("Patient/") && !subject.startsWith("Group/")) {
                throw new IllegalArgumentException("Subject must follow the format of either 'Patient/ID' OR 'Group/ID'.");
            }
        }
        if(null != status && (!status.equalsIgnoreCase("open-gap") && !status.equalsIgnoreCase("closed-gap"))){
            throw new IllegalArgumentException("If status is present, it must be either 'open-gap' or 'closed-gap'.");
        }
        if(null != practitioner && null == organization){
            throw new IllegalArgumentException("If a practitioner is specified then an organization must also be specified.");
        }
        if(null == subject && null == practitioner && null == organization){
            throw new IllegalArgumentException("periodStart AND periodEnd AND (subject OR organization OR (practitioner AND organization)) MUST be provided");
        }
        return true;
    }

    private Bundle patientCareGap(String periodStart, String periodEnd, String subject, String topic, String measure, String status) {
        //TODO: this is an org hack.  Need to figure out what the right thing is.
        IFhirResourceDao<Organization> orgDao = this.registry.getResourceDao(Organization.class);
        List<IBaseResource> org = orgDao.search(new SearchParameterMap()).getResources(0, 1);

        SearchParameterMap theParams = new SearchParameterMap();

        // if (theId != null) {
        //     var measureParam = new StringParam(theId.getIdPart());
        //     theParams.add("_id", measureParam);
        // }

        if (topic != null && !topic.equals("")) {
            TokenParam topicParam = new TokenParam(topic);
            theParams.add("topic", topicParam);
        }
        List<IBaseResource> measures = getMeasureList(theParams, measure);

        Bundle careGapReport = new Bundle();
        careGapReport.setType(Bundle.BundleType.DOCUMENT);
        // TODO: no timestamp on dstu3 care-gap report
        //careGapReport.setTimestamp(new Date());

        Composition composition = new Composition();
        composition.setStatus(Composition.CompositionStatus.FINAL)
                .setSubject(new Reference(subject.startsWith("Patient/") ? subject : "Patient/" + subject))
                .setTitle("Care Gap Report for " + subject)
                .setDate(new Date())
                .setType(new CodeableConcept()
                        .addCoding(new Coding()
                                .setCode("gaps-doc")
                                .setSystem("http://hl7.org/fhir/us/davinci-deqm/CodeSystem/gaps-doc-type")
                                .setDisplay("Gaps in Care Report")));

        List<MeasureReport> reports = new ArrayList<>();
        List<DetectedIssue> detectedIssues = new ArrayList<DetectedIssue>();
        MeasureReport report = null;
        boolean hasIssue = false;

        for (IBaseResource resource : measures) {
            Measure measureResource = (Measure) resource;
           
            Composition.SectionComponent section = new Composition.SectionComponent();

            if (measureResource.hasTitle()) {
                section.setTitle(measureResource.getTitle());
            }

            // TODO - this is configured for patient-level evaluation only
            report = evaluateMeasure(measureResource.getIdElement(), periodStart, periodEnd, null, "patient", subject, null,
            null, null, null, null, null);

            report.setId(UUID.randomUUID().toString());
            report.setDate(new Date());
            // TODO: No improvement notation on dstu3 report
            //report.setImprovementNotation(measureResource.getImprovementNotation());
            //TODO: this is an org hack && requires an Organization to be in the ruler
            if (org != null && org.size() > 0) {
                // TODO: No reporter on dstu3 report
                //report.setReporter(new Reference("Organization/" + org.get(0).getIdElement().getIdPart()));
            }
            report.setMeta(new Meta().addProfile("http://hl7.org/fhir/us/davinci-deqm/StructureDefinition/indv-measurereport-deqm"));
            //section.setFocus(new Reference("MeasureReport/" + report.getId()));
            //TODO: DetectedIssue
            //section.addEntry(new Reference("MeasureReport/" + report.getId()));

            if (report.hasGroup() && measureResource.hasScoring()) {
                int numerator = 0;
                int denominator = 0;
                for (MeasureReport.MeasureReportGroupComponent group : report.getGroup()) {
                    if (group.hasPopulation()) {
                        for (MeasureReport.MeasureReportGroupPopulationComponent population : group.getPopulation()) {
                            // TODO - currently configured for measures with only 1 numerator and 1
                            // denominator
                            if (population.hasCode()) {
                                if (population.getCode().hasCoding()) {
                                    for (Coding coding : population.getCode().getCoding()) {
                                        if (coding.hasCode()) {
                                            if (coding.getCode().equals("numerator") && population.hasCount()) {
                                                numerator = population.getCount();
                                            } else if (coding.getCode().equals("denominator")
                                                    && population.hasCount()) {
                                                denominator = population.getCount();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                //TODO: implement this per the spec
                //Holding off on implementation using Measure Score pending guidance re consideration for programs that don't perform the calculation (they just use numer/denom)
                double proportion = 0.0;
                if (measureResource.getScoring().hasCoding() && denominator != 0) {
                    for (Coding coding : measureResource.getScoring().getCoding()) {
                        if (coding.hasCode() && coding.getCode().equals("proportion")) {
                            if (denominator != 0.0 ) {
                                proportion = numerator / denominator;
                            }
                        }
                    }
                }

                // TODO - this is super hacky ... change once improvementNotation is specified
                // as a code
                String improvementNotation = measureResource.getImprovementNotation();
                if (((improvementNotation.equals("increase")) && (proportion < 1.0))
                        ||  ((improvementNotation.equals("decrease")) && (proportion > 0.0))
                        && (null == status || "".equalsIgnoreCase(status) || "open-gap".equalsIgnoreCase(status))) {
                        hasIssue = true;
                        DetectedIssue detectedIssue = new DetectedIssue();
                        detectedIssue.setId(UUID.randomUUID().toString());
                        detectedIssue.setStatus(DetectedIssue.DetectedIssueStatus.FINAL);
                        detectedIssue.setPatient(new Reference(subject.startsWith("Patient/") ? subject : "Patient/" + subject));
                        // TODO: No evidence on DSTU3 detected issue
                        // detectedIssue.getEvidence().add(new DetectedIssue.DetectedIssueEvidenceComponent().addDetail(new Reference("MeasureReport/" + report.getId())));
                        // CodeableConcept code = new CodeableConcept()
                        //    .addCoding(new Coding().setSystem("http://hl7.org/fhir/us/davinci-deqm/CodeSystem/detectedissue-category").setCode("care-gap"));
                        
                        // TODO: No code on DSTU3 detected issue
                        //detectedIssue.setCode(code);

                        section.addEntry(
                             new Reference("DetectedIssue/" + detectedIssue.getIdElement().getIdPart()));
                        composition.addSection(section);

                        detectedIssues.add(detectedIssue);
                }
                reports.add(report);

                // TODO - add other types of improvement notation cases
            }
        }
        Parameters parameters = new Parameters();
        if((null == status || status == "")                                 //everything
                || (hasIssue && !"closed-gap".equalsIgnoreCase(status))     //filter out closed-gap that has issues  for OPEN-GAP
                ||(!hasIssue && !"open-gap".equalsIgnoreCase(status))){     //filet out open-gap without issues  for CLOSE-GAP
            careGapReport.addEntry(new Bundle.BundleEntryComponent().setResource(composition));
            for (MeasureReport rep : reports) {
                careGapReport.addEntry(new Bundle.BundleEntryComponent().setResource(rep));
                if (report.hasContained()) {
                    for (Resource contained : report.getContained()) {
                        if (contained instanceof Bundle) {
                            addEvaluatedResourcesToParameters((Bundle) contained, parameters);
                            if(null != parameters && !parameters.isEmpty()) {
                                List <Reference> evaluatedResource = new ArrayList<>();
                                parameters.getParameter().forEach(parameter -> {
                                    Reference newEvaluatedResourceItem = new Reference();
                                    newEvaluatedResourceItem.setReference(parameter.getResource().getId());
                                    List<Extension> evalResourceExt = new ArrayList<>();
                                    evalResourceExt.add(new Extension("http://hl7.org/fhir/us/davinci-deqm/StructureDefinition/extension-ppopulationReference",
                                            new CodeableConcept()
                                                    .addCoding(new Coding("http://teminology.hl7.org/CodeSystem/measure-population", "initial-population", "initial-population"))));
                                    newEvaluatedResourceItem.setExtension(evalResourceExt);
                                    evaluatedResource.add(newEvaluatedResourceItem);
                                });
                                // TODO: Figure out for DSTU3
                                //report.setEvaluatedResource(evaluatedResource);
                            }
                        }
                    }
                }
            }
            for (DetectedIssue detectedIssue : detectedIssues) {
                careGapReport.addEntry(new Bundle.BundleEntryComponent().setResource(detectedIssue));
            }
        }
        if(careGapReport.getEntry().isEmpty()){
            return null;
        }
        return careGapReport;
    }

    private List<IBaseResource> getMeasureList(SearchParameterMap theParams, String measure){
        if(measure != null && measure.length() > 0){
            List<IBaseResource> finalMeasureList = new ArrayList<>();
            List<IBaseResource> allMeasures = this.measureResourceProvider
                    .getDao()
                    .search(theParams)
                    .getResources(0, 1000);
            for(String singleName: measure.split(",")){
                if (singleName.equals("")) {
                    continue;
                }
                allMeasures.forEach(measureResource -> {
                    if(((Measure)measureResource).getName().equalsIgnoreCase(singleName.trim())) {
                        if (measureResource != null) {
                            finalMeasureList.add(measureResource);
                        }
                    }
                });
            }
            return finalMeasureList;
        }else {
            return 
            //TODO: this needs to be restricted to only the current measure.  It seems to be returning all versions in history.
                this.measureResourceProvider.getDao().search(theParams).getResources(0, 1000)
                    .stream()
                    .filter(resource -> ((Measure)resource).getUrl() != null && !((Measure)resource).getUrl().equals(""))
                    .collect(Collectors.toList());
        }
    }

    @Operation(name = "$collect-data", idempotent = true, type = Measure.class)
    public Parameters collectData(@IdParam IdType theId, @OperationParam(name = "periodStart") String periodStart,
            @OperationParam(name = "periodEnd") String periodEnd, @OperationParam(name = "patient") String patientRef,
            @OperationParam(name = "practitioner") String practitionerRef,
            @OperationParam(name = "lastReceivedOn") String lastReceivedOn) throws FHIRException {
        // TODO: Spec says that the periods are not required, but I am not sure what to
        // do when they aren't supplied so I made them required
        MeasureReport report = evaluateMeasure(theId, periodStart, periodEnd, null, null, patientRef, null,
                practitionerRef, lastReceivedOn, null, null, null);
        report.setGroup(null);

        Parameters parameters = new Parameters();

        parameters.addParameter(
                new Parameters.ParametersParameterComponent().setName("measurereport").setResource(report));

        if (report.hasContained()) {
            for (Resource contained : report.getContained()) {
                if (contained instanceof Bundle) {
                    addEvaluatedResourcesToParameters((Bundle) contained, parameters);
                }
            }
        }

        // TODO: need a way to resolve referenced resources within the evaluated
        // resources
        // Should be able to use _include search with * wildcard, but HAPI doesn't
        // support that

        return parameters;
    }

    private void addEvaluatedResourcesToParameters(Bundle contained, Parameters parameters) {
        Map<String, Resource> resourceMap = new HashMap<>();
        if (contained.hasEntry()) {
            for (Bundle.BundleEntryComponent entry : contained.getEntry()) {
                if (entry.hasResource() && !(entry.getResource() instanceof ListResource)) {
                    if (!resourceMap.containsKey(entry.getResource().getIdElement().getValue())) {
                        parameters.addParameter(new Parameters.ParametersParameterComponent().setName("resource")
                                .setResource(entry.getResource()));

                        resourceMap.put(entry.getResource().getIdElement().getValue(), entry.getResource());

                        resolveReferences(entry.getResource(), parameters, resourceMap);
                    }
                }
            }
        }
    }

    private void resolveReferences(Resource resource, Parameters parameters, Map<String, Resource> resourceMap) {
        List<IBase> values;
        for (BaseRuntimeChildDefinition child : this.measureResourceProvider.getContext()
                .getResourceDefinition(resource).getChildren()) {
            values = child.getAccessor().getValues(resource);
            if (values == null || values.isEmpty()) {
                continue;
            }

            else if (values.get(0) instanceof Reference
                    && ((Reference) values.get(0)).getReferenceElement().hasResourceType()
                    && ((Reference) values.get(0)).getReferenceElement().hasIdPart()) {
                Resource fetchedResource = (Resource) registry
                        .getResourceDao(((Reference) values.get(0)).getReferenceElement().getResourceType())
                        .read(new IdType(((Reference) values.get(0)).getReferenceElement().getIdPart()));

                if (!resourceMap.containsKey(fetchedResource.getIdElement().getValue())) {
                    parameters.addParameter(new Parameters.ParametersParameterComponent().setName("resource")
                            .setResource(fetchedResource));

                    resourceMap.put(fetchedResource.getIdElement().getValue(), fetchedResource);
                }
            }
        }
    }

    // TODO - this needs a lot of work
    @Operation(name = "$data-requirements", idempotent = true, type = Measure.class)
    public Library dataRequirements(@IdParam IdType theId,
            @OperationParam(name = "startPeriod") String startPeriod,
            @OperationParam(name = "endPeriod") String endPeriod) throws InternalErrorException, FHIRException {

        Measure measure = this.measureResourceProvider.getDao().read(theId);
        return this.dataRequirementsProvider.getDataRequirements(measure, this.libraryResolutionProvider);
    }

    @SuppressWarnings("unchecked")
    @Operation(name = "$submit-data", idempotent = true, type = Measure.class)
    public Resource submitData(RequestDetails details, @IdParam IdType theId,
            @OperationParam(name = "measure-report", min = 1, max = 1, type = MeasureReport.class) MeasureReport report,
            @OperationParam(name = "resource") List<IAnyResource> resources) {
        Bundle transactionBundle = new Bundle().setType(Bundle.BundleType.TRANSACTION);

        /*
         * TODO - resource validation using $data-requirements operation (params are the
         * provided id and the measurement period from the MeasureReport)
         * 
         * TODO - profile validation ... not sure how that would work ... (get
         * StructureDefinition from URL or must it be stored in Ruler?)
         */

        transactionBundle.addEntry(createTransactionEntry(report));

        for (IAnyResource resource : resources) {
            Resource res = (Resource) resource;
            if (res instanceof Bundle) {
                for (Bundle.BundleEntryComponent entry : createTransactionBundle((Bundle) res).getEntry()) {
                    transactionBundle.addEntry(entry);
                }
            } else {
                // Build transaction bundle
                transactionBundle.addEntry(createTransactionEntry(res));
            }
        }

        return (Resource) ((IFhirSystemDao<Bundle, ?>)this.registry.getSystemDao()).transaction(details, transactionBundle);
    }

    private Bundle createTransactionBundle(Bundle bundle) {
        Bundle transactionBundle;
        if (bundle != null) {
            if (bundle.hasType() && bundle.getType() == Bundle.BundleType.TRANSACTION) {
                transactionBundle = bundle;
            } else {
                transactionBundle = new Bundle().setType(Bundle.BundleType.TRANSACTION);
                if (bundle.hasEntry()) {
                    for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
                        if (entry.hasResource()) {
                            transactionBundle.addEntry(createTransactionEntry(entry.getResource()));
                        }
                    }
                }
            }
        } else {
            transactionBundle = new Bundle().setType(Bundle.BundleType.TRANSACTION).setEntry(new ArrayList<>());
        }

        return transactionBundle;
    }

    private Bundle.BundleEntryComponent createTransactionEntry(Resource resource) {
        Bundle.BundleEntryComponent transactionEntry = new Bundle.BundleEntryComponent().setResource(resource);
        if (resource.hasId()) {
            transactionEntry.setRequest(
                    new Bundle.BundleEntryRequestComponent().setMethod(Bundle.HTTPVerb.PUT).setUrl(resource.getId()));
        } else {
            transactionEntry.setRequest(new Bundle.BundleEntryRequestComponent().setMethod(Bundle.HTTPVerb.POST)
                    .setUrl(resource.fhirType()));
        }
        return transactionEntry;
    }
}
