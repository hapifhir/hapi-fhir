package ca.uhn.fhir.cql.r4.builder;

/*-
 * #%L
 * HAPI FHIR JPA Server - Clinical Quality Language
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.cql.common.builder.BaseBuilder;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.MeasureReport;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Reference;
import org.opencds.cqf.cql.engine.runtime.DateTime;
import org.opencds.cqf.cql.engine.runtime.Interval;

import java.util.Date;

public class MeasureReportBuilder extends BaseBuilder<MeasureReport> {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ca.uhn.fhir.cql.dstu3.builder.MeasureReportBuilder.class);

	public MeasureReportBuilder() {
        super(new MeasureReport());
    }

    public MeasureReportBuilder buildStatus(String status) {
        try {
            this.complexProperty.setStatus(MeasureReport.MeasureReportStatus.fromCode(status));
        } catch (FHIRException e) {
			  ourLog.warn("Exception caught while attempting to set Status to '" + status + "', assuming status COMPLETE!"
				  + System.lineSeparator() + e.getMessage());
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
