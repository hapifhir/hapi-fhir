package ca.uhn.fhir.narrative.template.filters;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

class Date extends Filter {

    private static Locale locale = Locale.ENGLISH;
    private static Set<String> datePatterns = new HashSet<String>();

    private final static java.util.Map<Character, SimpleDateFormat> LIQUID_TO_JAVA_FORMAT =
            new java.util.HashMap<Character, SimpleDateFormat>();

    static {
        addDatePattern("yyyy-MM-dd HH:mm:ss");
        addDatePattern("EEE MMM ddhh:mm:ss yyyy");
        init();
    }

    /*
     * (Object) date(input, format)
     *
     * Reformat a date
     *
     * %a - The abbreviated weekday name (``Sun'')
     * %A - The  full  weekday  name (``Sunday'')
     * %b - The abbreviated month name (``Jan'')
     * %B - The  full  month  name (``January'')
     * %c - The preferred local date and time representation
     * %d - Day of the month (01..31)
     * %H - Hour of the day, 24-hour clock (00..23)
     * %I - Hour of the day, 12-hour clock (01..12)
     * %j - Day of the year (001..366)
     * %m - Month of the year (01..12)
     * %M - Minute of the hour (00..59)
     * %p - Meridian indicator (``AM''  or  ``PM'')
     * %S - Second of the minute (00..60)
     * %U - Week  number  of the current year,
     *      starting with the first Sunday as the first
     *      day of the first week (00..53)
     * %W - Week  number  of the current year,
     *      starting with the first Monday as the first
     *      day of the first week (00..53)
     * %w - Day of the week (Sunday is 0, 0..6)
     * %x - Preferred representation for the date alone, no time
     * %X - Preferred representation for the time alone, no date
     * %y - Year without a century (00..99)
     * %Y - Year with century
     * %Z - Time zone name
     * %% - Literal ``%'' character
     */
    @Override
    public Object apply(Object value, Object... params) {

        try {
            final Long seconds;

            if(super.asString(value).equals("now")) {
                seconds = System.currentTimeMillis() / 1000L;
            }
            else if(super.isNumber(value)) {
                // No need to divide this by 1000, the param is expected to be in seconds already!
                seconds = super.asNumber(value).longValue();
            }
            else {
                seconds = trySeconds(super.asString(value)); // formatter.parse(super.asString(value)).getTime() / 1000L;

                if(seconds == null) {
                    return value;
                }
            }

            final java.util.Date date = new java.util.Date(seconds * 1000L);
            final String format = super.asString(super.get(0, params));

            if(format == null || format.trim().isEmpty()) {
                return value;
            }

            final java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(date);

            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < format.length(); i++) {

                char ch = format.charAt(i);

                if (ch == '%') {

                    i++;

                    if (i == format.length()) {
                        // a trailing (single) '%' sign: just append it
                        builder.append("%");
                        break;
                    }

                    char next = format.charAt(i);

                    SimpleDateFormat javaFormat = LIQUID_TO_JAVA_FORMAT.get(next);

                    if (javaFormat == null) {
                        // no valid date-format: append the '%' and the 'next'-char
                        builder.append("%").append(next);
                    }
                    else {
                        builder.append(javaFormat.format(date));
                    }
                }
                else {
                    builder.append(ch);
                }
            }

            return builder.toString();
        }
        catch (Exception e) {
            return value;
        }
    }

    private static void init() {

        // %% - Literal ``%'' character
        LIQUID_TO_JAVA_FORMAT.put('%', new SimpleDateFormat("%", locale));

        // %a - The abbreviated weekday name (``Sun'')
        LIQUID_TO_JAVA_FORMAT.put('a', new SimpleDateFormat("EEE", locale));

        // %A - The  full  weekday  name (``Sunday'')
        LIQUID_TO_JAVA_FORMAT.put('A', new SimpleDateFormat("EEEE", locale));

        // %b - The abbreviated month name (``Jan'')
        LIQUID_TO_JAVA_FORMAT.put('b', new SimpleDateFormat("MMM", locale));
        LIQUID_TO_JAVA_FORMAT.put('h', new SimpleDateFormat("MMM", locale));

        // %B - The  full  month  name (``January'')
        LIQUID_TO_JAVA_FORMAT.put('B', new SimpleDateFormat("MMMM", locale));

        // %c - The preferred local date and time representation
        LIQUID_TO_JAVA_FORMAT.put('c', new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", locale));

        // %d - Day of the month (01..31)
        LIQUID_TO_JAVA_FORMAT.put('d', new SimpleDateFormat("dd", locale));

        // %H - Hour of the day, 24-hour clock (00..23)
        LIQUID_TO_JAVA_FORMAT.put('H', new SimpleDateFormat("HH", locale));

        // %I - Hour of the day, 12-hour clock (01..12)
        LIQUID_TO_JAVA_FORMAT.put('I', new SimpleDateFormat("hh", locale));

        // %j - Day of the year (001..366)
        LIQUID_TO_JAVA_FORMAT.put('j', new SimpleDateFormat("DDD", locale));

        // %m - Month of the year (01..12)
        LIQUID_TO_JAVA_FORMAT.put('m', new SimpleDateFormat("MM", locale));

        // %M - Minute of the hour (00..59)
        LIQUID_TO_JAVA_FORMAT.put('M', new SimpleDateFormat("mm", locale));

        // %p - Meridian indicator (``AM''  or  ``PM'')
        LIQUID_TO_JAVA_FORMAT.put('p', new SimpleDateFormat("a", locale));

        // %S - Second of the minute (00..60)
        LIQUID_TO_JAVA_FORMAT.put('S', new SimpleDateFormat("ss", locale));

        // %U - Week  number  of the current year,
        //      starting with the first Sunday as the first
        //      day of the first week (00..53)
        LIQUID_TO_JAVA_FORMAT.put('U', new SimpleDateFormat("ww", locale));

        // %W - Week  number  of the current year,
        //      starting with the first Monday as the first
        //      day of the first week (00..53)
        LIQUID_TO_JAVA_FORMAT.put('W', new SimpleDateFormat("ww", locale));

        // %w - Day of the week (Sunday is 0, 0..6)
        LIQUID_TO_JAVA_FORMAT.put('w', new SimpleDateFormat("F", locale));

        // %x - Preferred representation for the date alone, no time
        LIQUID_TO_JAVA_FORMAT.put('x', new SimpleDateFormat("MM/dd/yy", locale));

        // %X - Preferred representation for the time alone, no date
        LIQUID_TO_JAVA_FORMAT.put('X', new SimpleDateFormat("HH:mm:ss", locale));

        // %y - Year without a century (00..99)
        LIQUID_TO_JAVA_FORMAT.put('y', new SimpleDateFormat("yy", locale));

        // %Y - Year with century
        LIQUID_TO_JAVA_FORMAT.put('Y', new SimpleDateFormat("yyyy", locale));

        // %Z - Time zone name
        LIQUID_TO_JAVA_FORMAT.put('Z', new SimpleDateFormat("z", locale));
    }

    /**
     * Changes the locale.
     *
     * @param locale the new locale.
     */
    public static void setLocale(Locale locale) {
        Date.locale = locale;
        init();
    }

    /**
     * Adds a new Date-pattern to be used when parsing a string to a Date.
     *
     * @param pattern the pattern.
     */
    public static void addDatePattern(String pattern) {

        if(pattern == null) {
            throw new NullPointerException("date-pattern cannot be null");
        }

        datePatterns.add(pattern);
    }

    /**
     * Removed a Date-pattern to be used when parsing a string to a Date.
     *
     * @param pattern the pattern.
     */
    public static void removeDatePattern(String pattern) {

        datePatterns.remove(pattern);
    }

    /*
     * Try to parse `str` into a Date and return this Date as seconds
     * since EPOCH, or null if it could not be parsed.
     */
    private Long trySeconds(String str) {

        for(String pattern : datePatterns) {

            SimpleDateFormat parser = new SimpleDateFormat(pattern, locale);

            try {
                long milliseconds = parser.parse(str).getTime();
                return milliseconds / 1000L;
            }
            catch(Exception e) {
                // Just ignore and try the next pattern in `datePatterns`.
            }
        }

        // Could not parse the string into a meaningful date, return null.
        return null;
    }
}
