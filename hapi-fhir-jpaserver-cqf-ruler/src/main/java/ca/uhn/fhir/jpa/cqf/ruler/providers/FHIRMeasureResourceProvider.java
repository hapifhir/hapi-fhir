package ca.uhn.fhir.jpa.cqf.ruler.providers;

import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.provider.dstu3.JpaResourceProviderDstu3;
import ca.uhn.fhir.jpa.rp.dstu3.CodeSystemResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu3.LibraryResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu3.PatientResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu3.ValueSetResourceProvider;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.execution.VersionedIdentifier;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import ca.uhn.fhir.jpa.cqf.ruler.config.STU3LibraryLoader;
import ca.uhn.fhir.jpa.cqf.ruler.config.STU3LibrarySourceProvider;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.execution.LibraryLoader;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.terminology.TerminologyProvider;
import org.opencds.cqf.cql.terminology.fhir.FhirTerminologyProvider;
import ca.uhn.fhir.jpa.cqf.ruler.helpers.DateHelper;
import ca.uhn.fhir.jpa.cqf.ruler.helpers.FhirMeasureEvaluator;

import java.util.*;

/*
    IN	periodStart	1..1	date
    The start of the measurement period. In keeping with the semantics of the date parameter used in the FHIR search operation, the period will start at the beginning of the period implied by the supplied timestamp. E.g. a value of 2014 would set the period start to be 2014-01-01T00:00:00 inclusive

    IN	periodEnd	1..1	date
    The end of the measurement period. The period will end at the end of the period implied by the supplied timestamp. E.g. a value of 2014 would set the period end to be 2014-12-31T23:59:59 inclusive

    IN	measure	0..1	Reference
    The measure to evaluate. This parameter is only required when the operation is invoked on the resource type, it is not used when invoking the operation on a Measure instance

    IN	reportType	0..1	code
    The type of measure report, patient, patient-list, or population. If not specified, a default value of patient will be used if the patient parameter is supplied, otherwise, population will be used

    IN	patient	0..1	Reference
    Patient to evaluate against. If not specified, the measure will be evaluated for all patients that meet the requirements of the measure. If specified, only the referenced patient will be evaluated

    IN	practitioner	0..1	Reference
    Practitioner to evaluate. If specified, the measure will be evaluated only for patients whose primary practitioner is the identified practitioner

    IN	lastReceivedOn	0..1	dateTime
    The date the results of this measure were last received. This parameter is only valid for patient-level reports and is used to indicate when the last time a result for this patient was received. This information can be used to limit the set of resources returned for a patient-level report

    OUT	return	1..1	MeasureReport
    The results of the measure calculation. See the MeasureReport resource for a complete description of the output of this operation
*/

public class FHIRMeasureResourceProvider extends JpaResourceProviderDstu3<Measure> {

    private JpaDataProvider provider;
    private TerminologyProvider terminologyProvider;

    private Context context;
    private Interval measurementPeriod;
    private MeasureReport report = new MeasureReport();
    private FhirMeasureEvaluator evaluator = new FhirMeasureEvaluator();

    public FHIRMeasureResourceProvider(Collection<IResourceProvider> providers) {
        this.provider = new JpaDataProvider(providers);
    }

    private LibraryResourceProvider getLibraryResourceProvider() {
        return (LibraryResourceProvider)provider.resolveResourceProvider("Library");
    }

    private ModelManager modelManager;
    private ModelManager getModelManager() {
        if (modelManager == null) {
            modelManager = new ModelManager();
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
            libraryLoader = new STU3LibraryLoader(getLibraryResourceProvider(), getLibraryManager(), getModelManager());
        }
        return libraryLoader;
    }

    private STU3LibrarySourceProvider librarySourceProvider;
    private STU3LibrarySourceProvider getLibrarySourceProvider() {
        if (librarySourceProvider == null) {
            librarySourceProvider = new STU3LibrarySourceProvider(getLibraryResourceProvider());
        }
        return librarySourceProvider;
    }

    /*
        This is not "pure" FHIR.
        The "source", "user", "pass", and "primaryLibraryName" parameters were added to simplify the operation.
    */
    @Operation(name = "$evaluate", idempotent = true)
    public MeasureReport evaluateMeasure(
            @IdParam IdType theId,
            @OptionalParam(name="reportType") String reportType,
            @OptionalParam(name="patient") String patientId,
            @OptionalParam(name="practitioner") String practitioner,
            @OptionalParam(name="lastReceivedOn") String lastReceivedOn,
            @RequiredParam(name="startPeriod") String startPeriod,
            @RequiredParam(name="endPeriod") String endPeriod,
            @OptionalParam(name="source") String source,
            @OptionalParam(name="user") String user,
            @OptionalParam(name="pass") String pass,
            @OptionalParam(name="primaryLibraryName") String primaryLibraryName) throws InternalErrorException, FHIRException
    {
        Measure measure = this.getDao().read(theId);

        // load libraries referenced in measure
        // TODO: need better way to determine primary library
        //   - for now using a name convention: <measure ID>-library or primaryLibraryName param
        Library primary = null;
        for (Reference ref : measure.getLibrary()) {
            VersionedIdentifier vid = new VersionedIdentifier().withId(ref.getReference());
            Library temp = getLibraryLoader().load(vid);

            if (vid.getId().equals(measure.getIdElement().getIdPart() + "-logic")
                    || vid.getId().equals("Library/" + measure.getIdElement().getIdPart() + "-logic")
                    || (primaryLibraryName != null && temp.getIdentifier().getId().equals(primaryLibraryName)))
            {
                primary = temp;
                context = new Context(primary);
            }
        }
        if (primary == null) {
            throw new IllegalArgumentException(
                    "Primary library not found.\nFollow the naming conventions <measureID>-library or specify primary library in request."
            );
        }
        if (((STU3LibraryLoader)getLibraryLoader()).getLibraries().isEmpty()) {
            throw new IllegalArgumentException(String.format("Could not load library source for libraries referenced in %s measure.", measure.getId()));
        }

        // Prep - defining the context, measurementPeriod, terminology provider, and data provider
        context.registerLibraryLoader(getLibraryLoader());
        measurementPeriod =
                new Interval(
                        DateHelper.resolveRequestDate(startPeriod, true), true,
                        DateHelper.resolveRequestDate(endPeriod, false), true
                );

        if (source == null) {
            JpaResourceProviderDstu3<ValueSet> vs = (ValueSetResourceProvider) provider.resolveResourceProvider("ValueSet");
            JpaResourceProviderDstu3<CodeSystem> cs = (CodeSystemResourceProvider) provider.resolveResourceProvider("CodeSystem");
            terminologyProvider = new JpaTerminologyProvider(vs, cs);
        }
        else {
            terminologyProvider = user == null || pass == null ? new FhirTerminologyProvider().withEndpoint(source)
                    : new FhirTerminologyProvider().withBasicAuth(user, pass).withEndpoint(source);
        }
        provider.setTerminologyProvider(terminologyProvider);
        provider.setExpandValueSets(true);
        context.registerDataProvider("http://hl7.org/fhir", provider);

        // determine the report type (patient, patient-list, or population (summary))
        if (reportType != null) {
            switch (reportType) {
                case "patient": return evaluatePatientMeasure(measure, patientId);
                case "patient-list": return evaluatePatientListMeasure(measure, practitioner);
                case "population": return evaluatePopulationMeasure(measure);
                case "summary": return evaluatePopulationMeasure(measure);
                default:
                    throw new IllegalArgumentException("Invalid report type " + reportType);
            }
        }

        // default behavior
        else {
            if (patientId != null) return evaluatePatientMeasure(measure, patientId);
            if (practitioner != null) return evaluatePatientListMeasure(measure, practitioner);
            return evaluatePopulationMeasure(measure);
        }
    }

    private void validateReport() {
        if (report == null) {
            throw new InternalErrorException("MeasureReport is null");
        }

        if (report.getEvaluatedResources() == null) {
            throw new InternalErrorException("EvaluatedResources is null");
        }
    }

    private MeasureReport evaluatePatientMeasure(Measure measure, String patientId) {
        if (patientId == null) {
            throw new IllegalArgumentException("Patient id must be provided for patient type measure evaluation");
        }

        Patient patient = ((PatientResourceProvider) provider.resolveResourceProvider("Patient")).getDao().read(new IdType(patientId));
        if (patient == null) {
            throw new InternalErrorException("Patient is null");
        }

        context.setContextValue("Patient", patientId);

        report = evaluator.evaluate(context, measure, patient, measurementPeriod);
        validateReport();
        return report;
    }

    private MeasureReport evaluatePatientListMeasure(Measure measure, String practitioner) {
        SearchParameterMap map = new SearchParameterMap();
        map.add("general-practitioner", new ReferenceParam(practitioner));
        IBundleProvider patientProvider = ((PatientResourceProvider) provider.resolveResourceProvider("Patient")).getDao().search(map);
        List<IBaseResource> patientList = patientProvider.getResources(0, patientProvider.size());

        if (patientList.isEmpty()) {
            throw new IllegalArgumentException("No patients were found with practitioner reference " + practitioner);
        }

        List<Patient> patients = new ArrayList<>();
        patientList.forEach(x -> patients.add((Patient) x));

//        context.setContextValue("Population", patients);

        report = evaluator.evaluate(context, measure, patients, measurementPeriod, MeasureReport.MeasureReportType.PATIENTLIST);
        validateReport();
        return report;
    }

    private MeasureReport evaluatePopulationMeasure(Measure measure) {
        IBundleProvider patientProvider = ((PatientResourceProvider) provider.resolveResourceProvider("Patient")).getDao().search(new SearchParameterMap());
        List<IBaseResource> population = patientProvider.getResources(0, patientProvider.size());

        if (population.isEmpty()) {
            throw new IllegalArgumentException("No patients were found in the data provider at endpoint " + provider.getEndpoint());
        }

        List<Patient> patients = new ArrayList<>();
        population.forEach(x -> patients.add((Patient) x));

        report = evaluator.evaluate(context, measure, patients, measurementPeriod, MeasureReport.MeasureReportType.SUMMARY);
        validateReport();
        return report;
    }

    @Operation(name = "$data-requirements", idempotent = true)
    public org.hl7.fhir.dstu3.model.Library dataRequirements(@IdParam IdType theId,
                                                             @RequiredParam(name="startPeriod") String startPeriod,
                                                             @RequiredParam(name="endPeriod") String endPeriod)
            throws InternalErrorException, FHIRException
    {
        Measure measure = this.getDao().read(theId);

        // NOTE: This assumes there is only one library and it is the primary library for the measure.
        org.hl7.fhir.dstu3.model.Library libraryResource =
                getLibraryResourceProvider()
                        .getDao()
                        .read(new IdType(measure.getLibraryFirstRep().getReference()));

        // TODO: what are the period params for? Library.effectivePeriod?

        List<RelatedArtifact> dependencies = new ArrayList<>();
        for (RelatedArtifact dependency : libraryResource.getRelatedArtifact()) {
            if (dependency.getType().toCode().equals("depends-on")) {
                dependencies.add(dependency);
            }
        }

        List<Coding> typeCoding = new ArrayList<>();
        typeCoding.add(new Coding().setCode("module-definition"));
        org.hl7.fhir.dstu3.model.Library library =
                new org.hl7.fhir.dstu3.model.Library().setType(new CodeableConcept().setCoding(typeCoding));

        if (!dependencies.isEmpty()) {
            library.setRelatedArtifact(dependencies);
        }

        return library
                .setDataRequirement(libraryResource.getDataRequirement())
                .setParameter(libraryResource.getParameter());
    }

    @Search(allowUnknownParams=true)
    public IBundleProvider search(
            javax.servlet.http.HttpServletRequest theServletRequest,

            RequestDetails theRequestDetails,

            @Description(shortDefinition="Search the contents of the resource's data using a fulltext search")
            @OptionalParam(name=ca.uhn.fhir.rest.api.Constants.PARAM_CONTENT)
                    StringAndListParam theFtContent,

            @Description(shortDefinition="Search the contents of the resource's narrative using a fulltext search")
            @OptionalParam(name=ca.uhn.fhir.rest.api.Constants.PARAM_TEXT)
                    StringAndListParam theFtText,

            @Description(shortDefinition="Search for resources which have the given tag")
            @OptionalParam(name=ca.uhn.fhir.rest.api.Constants.PARAM_TAG)
                    TokenAndListParam theSearchForTag,

            @Description(shortDefinition="Search for resources which have the given security labels")
            @OptionalParam(name=ca.uhn.fhir.rest.api.Constants.PARAM_SECURITY)
                    TokenAndListParam theSearchForSecurity,

            @Description(shortDefinition="Search for resources which have the given profile")
            @OptionalParam(name=ca.uhn.fhir.rest.api.Constants.PARAM_PROFILE)
                    UriAndListParam theSearchForProfile,

            @Description(shortDefinition="Return resources linked to by the given target")
            @OptionalParam(name="_has")
                    HasAndListParam theHas,

            @Description(shortDefinition="The ID of the resource")
            @OptionalParam(name="_id")
                    TokenAndListParam the_id,

            @Description(shortDefinition="The language of the resource")
            @OptionalParam(name="_language")
                    StringAndListParam the_language,

            @Description(shortDefinition="What resource is being referenced")
            @OptionalParam(name="composed-of", targetTypes={  } )
                    ReferenceAndListParam theComposed_of,

            @Description(shortDefinition="The measure publication date")
            @OptionalParam(name="date")
                    DateRangeParam theDate,

            @Description(shortDefinition="What resource is being referenced")
            @OptionalParam(name="depends-on", targetTypes={  } )
                    ReferenceAndListParam theDepends_on,

            @Description(shortDefinition="What resource is being referenced")
            @OptionalParam(name="derived-from", targetTypes={  } )
                    ReferenceAndListParam theDerived_from,

            @Description(shortDefinition="The description of the measure")
            @OptionalParam(name="description")
                    StringAndListParam theDescription,

            @Description(shortDefinition="The time during which the measure is intended to be in use")
            @OptionalParam(name="effective")
                    DateRangeParam theEffective,

            @Description(shortDefinition="External identifier for the measure")
            @OptionalParam(name="identifier")
                    TokenAndListParam theIdentifier,

            @Description(shortDefinition="Intended jurisdiction for the measure")
            @OptionalParam(name="jurisdiction")
                    TokenAndListParam theJurisdiction,

            @Description(shortDefinition="Computationally friendly name of the measure")
            @OptionalParam(name="name")
                    StringAndListParam theName,

            @Description(shortDefinition="What resource is being referenced")
            @OptionalParam(name="predecessor", targetTypes={  } )
                    ReferenceAndListParam thePredecessor,

            @Description(shortDefinition="Name of the publisher of the measure")
            @OptionalParam(name="publisher")
                    StringAndListParam thePublisher,

            @Description(shortDefinition="The current status of the measure")
            @OptionalParam(name="status")
                    TokenAndListParam theStatus,

            @Description(shortDefinition="What resource is being referenced")
            @OptionalParam(name="successor", targetTypes={  } )
                    ReferenceAndListParam theSuccessor,

            @Description(shortDefinition="The human-friendly name of the measure")
            @OptionalParam(name="title")
                    StringAndListParam theTitle,

            @Description(shortDefinition="Topics associated with the module")
            @OptionalParam(name="topic")
                    TokenAndListParam theTopic,

            @Description(shortDefinition="The uri that identifies the measure")
            @OptionalParam(name="url")
                    UriAndListParam theUrl,

            @Description(shortDefinition="The business version of the measure")
            @OptionalParam(name="version")
                    TokenAndListParam theVersion,

            @RawParam
                    Map<String, List<String>> theAdditionalRawParams,

            @IncludeParam(reverse=true)
                    Set<Include> theRevIncludes,
            @Description(shortDefinition="Only return resources which were last updated as specified by the given range")
            @OptionalParam(name="_lastUpdated")
                    DateRangeParam theLastUpdated,

            @IncludeParam(allow= {
                    "Measure:composed-of",
                    "Measure:depends-on",
                    "Measure:derived-from",
                    "Measure:predecessor",
                    "Measure:successor",
                    "Measure:composed-of",
                    "Measure:depends-on",
                    "Measure:derived-from",
                    "Measure:predecessor",
                    "Measure:successor",
                    "Measure:composed-of",
                    "Measure:depends-on",
                    "Measure:derived-from",
                    "Measure:predecessor",
                    "Measure:successor",
                    "Measure:composed-of",
                    "Measure:depends-on",
                    "Measure:derived-from",
                    "Measure:predecessor",
                    "Measure:successor",
                    "Measure:composed-of",
                    "Measure:depends-on",
                    "Measure:derived-from",
                    "Measure:predecessor",
                    "Measure:successor",
                    "*"
            })
                    Set<Include> theIncludes,

            @Sort
                    SortSpec theSort,

            @ca.uhn.fhir.rest.annotation.Count
                    Integer theCount
    ) {
        startRequest(theServletRequest);
        try {
            SearchParameterMap paramMap = new SearchParameterMap();
            paramMap.add(ca.uhn.fhir.rest.api.Constants.PARAM_CONTENT, theFtContent);
            paramMap.add(ca.uhn.fhir.rest.api.Constants.PARAM_TEXT, theFtText);
            paramMap.add(ca.uhn.fhir.rest.api.Constants.PARAM_TAG, theSearchForTag);
            paramMap.add(ca.uhn.fhir.rest.api.Constants.PARAM_SECURITY, theSearchForSecurity);
            paramMap.add(ca.uhn.fhir.rest.api.Constants.PARAM_PROFILE, theSearchForProfile);
            paramMap.add("_has", theHas);
            paramMap.add("_id", the_id);
            paramMap.add("_language", the_language);
            paramMap.add("composed-of", theComposed_of);
            paramMap.add("date", theDate);
            paramMap.add("depends-on", theDepends_on);
            paramMap.add("derived-from", theDerived_from);
            paramMap.add("description", theDescription);
            paramMap.add("effective", theEffective);
            paramMap.add("identifier", theIdentifier);
            paramMap.add("jurisdiction", theJurisdiction);
            paramMap.add("name", theName);
            paramMap.add("predecessor", thePredecessor);
            paramMap.add("publisher", thePublisher);
            paramMap.add("status", theStatus);
            paramMap.add("successor", theSuccessor);
            paramMap.add("title", theTitle);
            paramMap.add("topic", theTopic);
            paramMap.add("url", theUrl);
            paramMap.add("version", theVersion);
            paramMap.setRevIncludes(theRevIncludes);
            paramMap.setLastUpdated(theLastUpdated);
            paramMap.setIncludes(theIncludes);
            paramMap.setSort(theSort);
            paramMap.setCount(theCount);
//            paramMap.setRequestDetails(theRequestDetails);

            getDao().translateRawParameters(theAdditionalRawParams, paramMap);

            return getDao().search(paramMap, theRequestDetails);
        } finally {
            endRequest(theServletRequest);
        }
    }
}
