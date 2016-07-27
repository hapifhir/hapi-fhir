package ca.uhn.fhir.okhttp.utils;

public class UrlStringUtils {

    public static String withTrailingQuestionMarkRemoved(String input) {
        return input.replaceAll("\\?$", "");
    }

    public static String everythingAfterFirstQuestionMark(String input) {
        return input.substring(input.indexOf('?') + 1);
    }

    public static boolean hasQuestionMark(StringBuilder sb) {
        return sb.indexOf("?") != -1;
    }

    public static void deleteLastCharacter(StringBuilder sb) {
        sb.deleteCharAt(sb.length() - 1);
    }

    public static boolean endsWith(StringBuilder sb, char c) {
        return sb.length() > 0 && sb.charAt(sb.length() - 1) == c;
    }

}