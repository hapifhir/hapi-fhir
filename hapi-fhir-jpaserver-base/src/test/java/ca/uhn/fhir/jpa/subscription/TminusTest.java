package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.service.TMinusService;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TminusTest {

    @Test
    public void testDays() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;
        String payloadCriteria = criteria + "&effectiveDate=Tminus10d" + "&noEffectiveDate=Tminus1d" + "&_format=xml";
        System.out.println(payloadCriteria);
        System.out.println("Tminus10d with the current datetime - 10d");
        payloadCriteria = TMinusService.parseCriteria(payloadCriteria);
        System.out.println(payloadCriteria);
        Assert.assertTrue(!payloadCriteria.contains("Tminus"));
    }

    @Test
    public void testSeconds() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;
        String payloadCriteria = criteria + "&date=Tminus200s" + "&_format=xml";
        System.out.println(payloadCriteria);
        System.out.println("replace Tminus200s with the current datetime - 200s");
        payloadCriteria = TMinusService.parseCriteria(payloadCriteria);
        System.out.println(payloadCriteria);
        Assert.assertTrue(!payloadCriteria.contains("Tminus"));
    }

    @Test
    public void testMinutes() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;
        String payloadCriteria = criteria + "&effectiveDate=Tminus1m" + "&_format=xml";
        System.out.println(payloadCriteria);
        System.out.println("replace Tminus1m with the current datetime - 1m");
        payloadCriteria = TMinusService.parseCriteria(payloadCriteria);
        System.out.println(payloadCriteria);
        Assert.assertTrue(!payloadCriteria.contains("Tminus"));
    }

    @Test
    public void testMinutesAtTheEnd() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;
        String payloadCriteria = criteria + "&date=Tminus1m";
        System.out.println(payloadCriteria);
        System.out.println("replace Tminus1m with the current datetime - 1m");
        payloadCriteria = TMinusService.parseCriteria(payloadCriteria);
        System.out.println(payloadCriteria);
        Assert.assertTrue(!payloadCriteria.contains("Tminus"));
    }

    @Test
    public void testWithoutTminus() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;
        String payloadCriteria = criteria;
        System.out.println(payloadCriteria);
        System.out.println("test without a Tminus");
        String newPayloadCriteria = TMinusService.parseCriteria(payloadCriteria);
        System.out.println(payloadCriteria);
        Assert.assertTrue(!payloadCriteria.contains("Tminus"));
        Assert.assertTrue(payloadCriteria.equals(newPayloadCriteria));
    }

    @Test
    public void testWithoutTminusUnits() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;
        String payloadCriteria = criteria + "&date=Tminus1";
        System.out.println(payloadCriteria);
        System.out.println("check Tminus without units");
        String newPayloadCriteria = TMinusService.parseCriteria(payloadCriteria);
        System.out.println(payloadCriteria);
        Assert.assertTrue(payloadCriteria.contains("Tminus"));
        Assert.assertTrue(payloadCriteria.equals(newPayloadCriteria));
    }

    @Test
    public void testWithoutTminusValue() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;
        String payloadCriteria = criteria + "&date=Tminusm";
        System.out.println(payloadCriteria);
        System.out.println("check Tminus without a value");
        String newPayloadCriteria = TMinusService.parseCriteria(payloadCriteria);
        System.out.println(payloadCriteria);
        Assert.assertTrue(payloadCriteria.contains("Tminus"));
        Assert.assertTrue(payloadCriteria.equals(newPayloadCriteria));
    }

    @Test
    public void parsingCriteria() {
        String criteria = "Observation?code=SNOMED-CT|1000000050&date=Tminus1d&_format=xml";
        //String criteria = "Observation?code=SNOMED-CT|1000000050&date=%3E%3D2017-03-05T12:55:02-08:00&_format=xml";

        String TMINUS = "Tminus";
        String WEEK = "w";
        String DAY = "d";
        String HOUR = "h";
        String MINUTE = "m";
        String SECOND = "s";

        long MINUTE_AS_MILLIS = 60 * 1000;
        long HOUR_AS_MILLIS = 60 * 60 * 1000;
        long DAY_AS_MILLIS = 24 * 60 * 60 * 1000;
        long WEEK_AS_MILLIS = 7 * 24 * 60 * 60 * 1000;

        Pattern pattern = Pattern.compile("Tminus([0-9]+)([wdhms])");
        Matcher matcher = pattern.matcher(criteria);

        if (matcher.find()) {
            String tMinus = matcher.group();
            String tMinusValue = tMinus.substring(TMINUS.length(), tMinus.length() - 1);
            String tMinusPeriod = tMinus.substring(tMinus.length() - 1);

            long tMinusLongValue = Long.parseLong(tMinusValue);
            long tMinusMillis = 0L;

            if (WEEK.equals(tMinusPeriod)) {
                tMinusMillis = WEEK_AS_MILLIS * tMinusLongValue;
            } else if (DAY.equals(tMinusPeriod)) {
                tMinusMillis = DAY_AS_MILLIS * tMinusLongValue;
            } else if (HOUR.equals(tMinusPeriod)) {
                tMinusMillis = HOUR_AS_MILLIS * tMinusLongValue;
            } else if (MINUTE.equals(tMinusPeriod)) {
                tMinusMillis = MINUTE_AS_MILLIS * tMinusLongValue;
            } else if (SECOND.equals(tMinusPeriod)) {
                tMinusMillis = 1000 * tMinusLongValue;
            } else {
                throw new IllegalArgumentException("Period not recognized: " + tMinusPeriod);
            }

            Date currentDate = new Date();
            Date lowerDate = new Date(currentDate.getTime() - tMinusMillis);

            DateTimeType lowerDateTimeType = new DateTimeType(lowerDate);
            DateTimeType currentDateTimeType = new DateTimeType(currentDate);

            String dateValue = "%3E%3D" + lowerDateTimeType.getValueAsString() + "&date=%3C%3D" + currentDateTimeType.getValueAsString();
            String formattedCriteria = criteria.replace(tMinus, dateValue);

            System.out.println(tMinus);
            System.out.println(tMinusValue);
            System.out.println(tMinusPeriod);
            System.out.println(tMinusMillis);
            System.out.println(currentDate);
            System.out.println(lowerDate);
            System.out.println(lowerDateTimeType.getValueAsString());
            System.out.println(formattedCriteria);
        } else {
            System.out.println("nothing");
        }
    }
}
