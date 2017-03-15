package ca.uhn.fhir.jpa.service;

import org.hl7.fhir.dstu3.model.DateTimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TMinusService {

    private static final Logger logger = LoggerFactory.getLogger(TMinusService.class);

    private static final String TMINUS = "Tminus";
    private static final String WEEK = "w";
    private static final String DAY = "d";
    private static final String HOUR = "h";
    private static final String MINUTE = "m";
    private static final String SECOND = "s";

    private static final long MINUTE_AS_MILLIS = 60 * 1000;
    private static final long HOUR_AS_MILLIS = 60 * 60 * 1000;
    private static final long DAY_AS_MILLIS = 24 * 60 * 60 * 1000;
    private static final long WEEK_AS_MILLIS = 7 * 24 * 60 * 60 * 1000;

    private static final Pattern TMINUS_PATTERN_REGEX = Pattern.compile("&([a-zA-Z0-9_]+)=Tminus([0-9]+)([wdhms])");

    public static void main(String ... aaa){
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;
        String payloadCriteria = criteria + "&effectiveDate=Tminus1m" + "&_format=xml";

        Pattern myPattern = TMINUS_PATTERN_REGEX;
        Matcher matcher = myPattern.matcher(payloadCriteria);

        if (matcher.find()) {
            String tMinus = matcher.group();
            String tMinusVarName = tMinus.substring(1, tMinus.indexOf("="));
            String tMinusValue = tMinus.substring(tMinus.indexOf(TMINUS) + TMINUS.length(), tMinus.length() - 1);
            String tMinusPeriod = tMinus.substring(tMinus.length() - 1);

            System.out.println(matcher.group());
            System.out.println(tMinusVarName);
            System.out.println(tMinusValue);
            System.out.println(tMinusPeriod);
        }else{
            System.out.println("mmm");
        }
    }

    public static String parseCriteria(String criteria) {
        Matcher matcher = TMINUS_PATTERN_REGEX.matcher(criteria);
        String response = criteria;
        boolean matcherFound = false;
        Date currentDate = new Date();


        while (matcher.find()) {
            matcherFound = true;

            String tMinus = matcher.group();
            String tMinusVarName = tMinus.substring(1, tMinus.indexOf("="));
            String tMinusValue = tMinus.substring(tMinus.indexOf(TMINUS) + TMINUS.length(), tMinus.length() - 1);
            String tMinusPeriod = tMinus.substring(tMinus.length() - 1);
            long tMinusMillis = getTMinusValueAsLong(tMinusValue, tMinusPeriod);
            String dateValue = getDateParameterValue(tMinusMillis, tMinusVarName, currentDate);

            logger.debug("Tminus value replaced in criteria: " + criteria);
            response =  response.replace(tMinus, dateValue);
        }

        if(!matcherFound){
            logger.debug("Tminus value not found in criteria: " + criteria);
        }

        return response;
    }

    private static String getDateParameterValue(long tMinusMillis, String tMinusVarName, Date currentDate){
        Date lowerDate = new Date(currentDate.getTime() - tMinusMillis);

        DateTimeType lowerDateTimeType = new DateTimeType(lowerDate);
        DateTimeType currentDateTimeType = new DateTimeType(currentDate);

        return "&" + tMinusVarName + "=%3E%3D" + lowerDateTimeType.getValueAsString() + "&" + tMinusVarName + "=%3C%3D" + currentDateTimeType.getValueAsString();
    }

    private static long getTMinusValueAsLong(String tMinusValue, String tMinusPeriod) {
        long tMinusLongValue = Long.parseLong(tMinusValue);
        long tMinusMillis;

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

        return tMinusMillis;
    }
}
