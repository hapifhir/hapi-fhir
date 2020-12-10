package ca.uhn.fhir.cql.dstu3.builder;

import ca.uhn.fhir.cql.common.builder.BaseBuilder;
import org.hl7.fhir.dstu3.model.MeasureReport;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.exceptions.FHIRException;
import org.opencds.cqf.cql.engine.runtime.Interval;

import java.util.Date;

public class MeasureReportBuilder extends BaseBuilder<MeasureReport> {

    public MeasureReportBuilder() {
        super(new MeasureReport());
    }

    public MeasureReportBuilder buildStatus(String status) {
        try {
            this.complexProperty.setStatus(MeasureReport.MeasureReportStatus.fromCode(status));
        } catch (FHIRException e) {
            // default to complete
            this.complexProperty.setStatus(MeasureReport.MeasureReportStatus.COMPLETE);
        }
        return this;
    }

    public MeasureReportBuilder buildType(MeasureReport.MeasureReportType type) {
        this.complexProperty.setType(type);
        return this;
    }

    public MeasureReportBuilder buildMeasureReference(String measureRef) {
        this.complexProperty.setMeasure(new Reference(measureRef));
        return this;
    }

    public MeasureReportBuilder buildPatientReference(String patientRef) {
        this.complexProperty.setPatient(new Reference(patientRef));
        return this;
    }

    public MeasureReportBuilder buildPeriod(Interval period) {
        this.complexProperty.setPeriod(new Period().setStart((Date) period.getStart()).setEnd((Date) period.getEnd()));
        return this;
    }
}
