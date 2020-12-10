package ca.uhn.fhir.cql.r4.builder;

import ca.uhn.fhir.cql.common.builder.BaseBuilder;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.MeasureReport;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Reference;
import org.opencds.cqf.cql.engine.runtime.DateTime;
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

    public MeasureReportBuilder buildType(String type) {
        this.complexProperty.setType(MeasureReport.MeasureReportType.fromCode(type));
        return this;
    }

    public MeasureReportBuilder buildMeasureReference(String measureRef) {
        this.complexProperty.setMeasure(measureRef);
        return this;
    }

    public MeasureReportBuilder buildPatientReference(String patientRef) {
        this.complexProperty.setSubject(new Reference(patientRef));
        return this;
    }

    public MeasureReportBuilder buildPeriod(Interval period) {
        Object start = period.getStart();
        if (start instanceof DateTime) {
            this.complexProperty
                    .setPeriod(new Period().setStart(Date.from(((DateTime) start).getDateTime().toInstant()))
                            .setEnd(Date.from(((DateTime) period.getEnd()).getDateTime().toInstant())));
        } else if (start instanceof Date) {
            this.complexProperty
                    .setPeriod(new Period().setStart((Date) period.getStart()).setEnd((Date) period.getEnd()));
        }

        return this;
    }
}
