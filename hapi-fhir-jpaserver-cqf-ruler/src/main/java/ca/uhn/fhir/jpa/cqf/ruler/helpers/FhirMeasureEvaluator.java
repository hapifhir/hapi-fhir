package ca.uhn.fhir.jpa.cqf.ruler.helpers;

import org.hl7.fhir.dstu3.model.*;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Interval;

import java.util.*;


/**
 * Created by Bryn on 5/7/2016.
 */
public class FhirMeasureEvaluator {

    private MeasureReport resolveGroupings(MeasureReport report, Measure measure, Context context)
    {
        HashMap<String,Resource> resources = new HashMap<>();

        // for each measure group
        for (Measure.MeasureGroupComponent group : measure.getGroup()) {
            MeasureReport.MeasureReportGroupComponent reportGroup = new MeasureReport.MeasureReportGroupComponent();
            // TODO: Do I need to do this copy? Will HAPI FHIR do this automatically?
            reportGroup.setIdentifier(group.getIdentifier().copy());
            report.getGroup().add(reportGroup);
            for (Measure.MeasureGroupPopulationComponent population : group.getPopulation()) {
                // evaluate the criteria expression, should return true/false, translate to 0/1 for report
                Object result = context.resolveExpressionRef(population.getCriteria()).evaluate(context);
                int count = 0;
                if (result instanceof Boolean) {
                    count = (Boolean)result ? 1 : 0;
                }
                else if (result instanceof Iterable) {
                    for (Object item : (Iterable)result) {
                        count++;
                        if (item instanceof Resource) {
                            resources.put(((Resource)item).getId(), (Resource)item);
                        }
                    }
                }
                MeasureReport.MeasureReportGroupPopulationComponent populationReport = new MeasureReport.MeasureReportGroupPopulationComponent();
                populationReport.setCount(count);
                populationReport.setCode(population.getCode());

                reportGroup.getPopulation().add(populationReport);
            }
        }

        ArrayList<String> expressionNames = new ArrayList<>();
        // HACK: Hijacking Supplemental data to specify the evaluated resources
        // In reality, this should be specified explicitly, but I'm not sure what else to do here....
        for (Measure.MeasureSupplementalDataComponent supplementalData : measure.getSupplementalData()) {
            expressionNames.add(supplementalData.getCriteria());
        }

        // TODO: Need to return both the MeasureReport and the EvaluatedResources Bundle
        FhirMeasureBundler bundler = new FhirMeasureBundler();
        //String[] expressionNameArray = new String[expressionNames.size()];
        //expressionNameArray = expressionNames.toArray(expressionNameArray);
        //org.hl7.fhir.dstu3.model.Bundle evaluatedResources = bundler.bundle(context, expressionNameArray);
        org.hl7.fhir.dstu3.model.Bundle evaluatedResources = bundler.bundle(resources.values());
        evaluatedResources.setId(UUID.randomUUID().toString());
        //String jsonString = fhirClient.getFhirContext().newJsonParser().encodeResourceToString(evaluatedResources);
        //ca.uhn.fhir.rest.api.MethodOutcome result = fhirClient.create().resource(evaluatedResources).execute();
        report.setEvaluatedResources(new Reference('#' + evaluatedResources.getId()));
        report.addContained(evaluatedResources);
        return report;
    }

    // Patient Evaluation
    public MeasureReport evaluate(Context context, Measure measure, Patient patient, Date periodStart, Date periodEnd) {
        MeasureReport report = new MeasureReport();
        report.setMeasure(new Reference(measure));
        report.setPatient(new Reference(patient));
        Period reportPeriod = new Period();
        reportPeriod.setStart(periodStart);
        reportPeriod.setEnd(periodEnd);
        report.setPeriod(reportPeriod);
        report.setType(MeasureReport.MeasureReportType.INDIVIDUAL);

        Interval measurementPeriod = new Interval(DateTime.fromJavaDate(periodStart), true, DateTime.fromJavaDate(periodEnd), true);
        context.setParameter(null, "Measurement Period", measurementPeriod);

        return resolveGroupings(report, measure, context);
    }

    public MeasureReport evaluate(Context context, Measure measure, Patient patient, Interval measurementPeriod) {
        return evaluate(context, measure, patient, (Date)measurementPeriod.getStart(), (Date)measurementPeriod.getEnd());
    }

    // Population evaluation
//    public MeasureReport evaluate(Context context, Measure measure, List<Patient> patients,
//                                  Interval measurementPeriod, MeasureReport.MeasureReportType type)
//    {
//        MeasureReport report = new MeasureReport();
//        report.setMeasure(new Reference(measure));
//        Period reportPeriod = new Period();
//        reportPeriod.setStart((Date) measurementPeriod.getStart());
//        reportPeriod.setEnd((Date) measurementPeriod.getEnd());
//        report.setPeriod(reportPeriod);
//        report.setType(type);
//
//        return resolveGroupings(report, measure, context, patients);
//    }

    public MeasureReport evaluate(Context context, Measure measure, List<Patient> population,
                                  Interval measurementPeriod, MeasureReport.MeasureReportType type)
    {
        MeasureReport report = new MeasureReport();
        report.setMeasure(new Reference(measure));
        Period reportPeriod = new Period();
        reportPeriod.setStart((Date) measurementPeriod.getStart());
        reportPeriod.setEnd((Date) measurementPeriod.getEnd());
        report.setPeriod(reportPeriod);
        report.setType(type);

        context.setParameter(null, "Measurement Period", measurementPeriod);

        HashMap<String,Resource> resources = new HashMap<>();

        // for each measure group
        for (Measure.MeasureGroupComponent group : measure.getGroup()) {
            MeasureReport.MeasureReportGroupComponent reportGroup = new MeasureReport.MeasureReportGroupComponent();
            reportGroup.setIdentifier(group.getIdentifier());
            report.getGroup().add(reportGroup);

            for (Measure.MeasureGroupPopulationComponent pop : group.getPopulation()) {
                int count = 0;
                // Worried about performance here with big populations...
                for (Patient patient : population) {
                    context.setContextValue("Patient", patient);
                    Object result = context.resolveExpressionRef(pop.getCriteria()).evaluate(context);
                    if (result instanceof Boolean) {
                        count += (Boolean) result ? 1 : 0;
                    }
                    else if (result instanceof Iterable) {
                        for (Object item : (Iterable) result) {
                            count++;
                            if (item instanceof Resource) {
                                resources.put(((Resource) item).getId(), (Resource) item);
                            }
                        }
                    }
                }
                MeasureReport.MeasureReportGroupPopulationComponent populationReport = new MeasureReport.MeasureReportGroupPopulationComponent();
                populationReport.setCount(count);
                populationReport.setCode(pop.getCode());

                /*
                    TODO - it is a reference to a list...
                    Probably want to create the list and POST it, then include a reference to it.
                */
//                if (patients != null) {
//                    ListResource list = new ListResource();
//                    populationReport.setPatients();
//                }

                reportGroup.getPopulation().add(populationReport);
            }

        }

        ArrayList<String> expressionNames = new ArrayList<>();
        // HACK: Hijacking Supplemental data to specify the evaluated resources
        // In reality, this should be specified explicitly, but I'm not sure what else to do here....
        for (Measure.MeasureSupplementalDataComponent supplementalData : measure.getSupplementalData()) {
            expressionNames.add(supplementalData.getCriteria());
        }

        FhirMeasureBundler bundler = new FhirMeasureBundler();
        org.hl7.fhir.dstu3.model.Bundle evaluatedResources = bundler.bundle(resources.values());
        evaluatedResources.setId(UUID.randomUUID().toString());
        report.setEvaluatedResources(new Reference('#' + evaluatedResources.getId()));
        report.addContained(evaluatedResources);
        return report;
    }
}
