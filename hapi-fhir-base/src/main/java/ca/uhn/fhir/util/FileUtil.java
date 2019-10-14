package ca.uhn.fhir.util;

import java.text.DecimalFormat;

public class FileUtil {
	// Use "bytes" instead of just "b" because it reads easier in logs
	private static final String[] UNITS = new String[]{"Bytes", "kB", "MB", "GB", "TB"};

	public static String formatFileSize(long theBytes) {
		if (theBytes <= 0) {
			return "0 " + UNITS[0];
		}
		int digitGroups = (int) (Math.log10(theBytes) / Math.log10(1024));
		digitGroups = Math.min(digitGroups, UNITS.length - 1);
		return new DecimalFormat("###0.#").format(theBytes / Math.pow(1024, digitGroups)) + " " + UNITS[digitGroups];
	}

}
