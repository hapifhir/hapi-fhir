package org.hl7.fhir.utilities;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.File;

public class Utilities {

	public static String appendSlash(String definitions) {
		return definitions.endsWith(File.separator) ? definitions : definitions + File.separator;
	}

	public static String capitalize(String s) {
		if (s == null)
			return null;
		if (s.length() == 0)
			return s;
		if (s.length() == 1)
			return s.toUpperCase();

		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	public static boolean charInSet(char value, char... array) {
		for (int i : array)
			if (value == i)
				return true;
		return false;
	}

	public static boolean equivalent(String l, String r) {
		if (noString(l) && noString(r))
			return true;
		if (noString(l) || noString(r))
			return false;
		return l.toLowerCase().equals(r.toLowerCase());
	}

	public static boolean equivalentNumber(String l, String r) {
		if (noString(l) && noString(r))
			return true;
		if (noString(l) || noString(r))
			return false;
		l = l.toLowerCase().trim();
		r = r.toLowerCase().trim(); // not that this should make any difference
		return l.startsWith(r) || r.startsWith(l);
	}

	public static String escapeJava(String doco) {
		if (doco == null)
			return "";

		StringBuilder b = new StringBuilder();
		for (char c : doco.toCharArray()) {
			if (c == '\r')
				b.append("\\r");
			else if (c == '\n')
				b.append("\\n");
			else if (c == '"')
				b.append("\\\"");
			else if (c == '\\')
				b.append("\\\\");
			else
				b.append(c);
		}
		return b.toString();
	}

	public static String escapeXml(String doco) {
		if (doco == null)
			return "";

		StringBuilder b = new StringBuilder();
		for (char c : doco.toCharArray()) {
			if (c == '<')
				b.append("&lt;");
			else if (c == '>')
				b.append("&gt;");
			else if (c == '&')
				b.append("&amp;");
			else if (c == '"')
				b.append("&quot;");
			else
				b.append(c);
		}
		return b.toString();
	}

	public static boolean existsInList(int value, int... array) {
		for (int i : array)
			if (value == i)
				return true;
		return false;
	}

	public static boolean existsInList(String value, String... array) {
		if (value == null)
			return false;
		for (String s : array)
			if (value.equals(s))
				return true;
		return false;
	}

	public static boolean isAbsoluteUrl(String ref) {
		return ref.startsWith("http:") || ref.startsWith("https:") || ref.startsWith("urn:uuid:") || ref.startsWith("urn:oid:");
	}

	public static boolean isDecimal(String string) {
		try {
			float r = Float.parseFloat(string);
			return r != r + 1; // just to suppress the hint
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isHex(String string) {
		try {
			int i = Integer.parseInt(string, 16);
			return i != i + 1;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isInteger(String string) {
		try {
			int i = Integer.parseInt(string);
			return i != i + 1;
		} catch (Exception e) {
			return false;
		}
	}

	public static String normalize(String s) {
		if (noString(s))
			return null;
		StringBuilder b = new StringBuilder();
		boolean isWhitespace = false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (!Character.isWhitespace(c)) {
				b.append(Character.toLowerCase(c));
				isWhitespace = false;
			} else if (!isWhitespace) {
				b.append(' ');
				isWhitespace = true;
			}
		}
		return b.toString().trim();
	}

	public static boolean noString(String theValue) {
		return isBlank(theValue);
	}

	public static String padLeft(String src, char c, int len) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < len - src.length(); i++)
			s.append(c);
		s.append(src);
		return s.toString();

	}

	public static String path(String... args) {
		StringBuilder s = new StringBuilder();
		boolean d = false;
		for (String arg : args) {
			if (!d)
				d = !noString(arg);
			else if (!s.toString().endsWith(File.separator))
				s.append(File.separator);
			String a = arg;
			a = a.replace("\\", File.separator);
			if (s.length() > 0 && a.startsWith(File.separator))
				a = a.substring(File.separator.length());

			if ("..".equals(a)) {
				int i = s.substring(0, s.length() - 1).lastIndexOf(File.separator);
				s = new StringBuilder(s.substring(0, i + 1));
			} else
				s.append(a);
		}
		return s.toString();
	}

	public static String pathReverse(String... args) {
		StringBuilder s = new StringBuilder();
		boolean d = false;
		for (String arg : args) {
			if (!d)
				d = !noString(arg);
			else if (!s.toString().endsWith("/"))
				s.append("/");
			s.append(arg);
		}
		return s.toString();
	}

	public static String uncapitalize(String s) {
		if (s == null)
			return null;
		if (s.length() == 0)
			return s;
		if (s.length() == 1)
			return s.toLowerCase();

		return s.substring(0, 1).toLowerCase() + s.substring(1);
	}

	 public static int charCount(String s, char c) {
       int res = 0;
       for (char ch : s.toCharArray())
             if (ch == c)
               res++;
       return res;
	 }

	 public static boolean isURL(String s) {
	    boolean ok = s.matches("^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*");
	    return ok;
	 }
	 
}
