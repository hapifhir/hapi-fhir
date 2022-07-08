package ca.uhn.fhir.okhttp.utils;

/*
 * #%L
 * HAPI FHIR OkHttp Client
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
