package ca.uhn.fhir.jpa.cqf.ruler.builders;

import org.opencds.cqf.cql.runtime.DateTime;

import java.util.Date;

public class JavaDateBuilder extends BaseBuilder<Date> {

    public JavaDateBuilder() {
        super(new Date());
    }

    public JavaDateBuilder buildFromDateTime(DateTime dateTime) {
        org.joda.time.DateTime dt = new org.joda.time.DateTime(dateTime.getPartial());
        complexProperty = dt.toDate();
        return this;
    }
}
