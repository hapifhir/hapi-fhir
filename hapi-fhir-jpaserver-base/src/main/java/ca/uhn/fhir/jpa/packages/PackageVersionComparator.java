package ca.uhn.fhir.jpa.packages;

import java.math.BigDecimal;
import java.util.Comparator;

import static org.apache.commons.lang3.StringUtils.isNumeric;

public class PackageVersionComparator implements Comparator<String> {
	public static final PackageVersionComparator INSTANCE = new PackageVersionComparator();

	@Override
	public int compare(String o1, String o2) {

		String[] o1parts = o1.split("\\.");
		String[] o2parts = o2.split("\\.");

		for (int i = 0; i < o1parts.length && i < o2parts.length; i++) {
			String i1part = o1parts[i];
			String i2part = o2parts[i];

			if (isNumeric(i1part)) {
				if (isNumeric(i2part)) {
					int cmp = new BigDecimal(i1part).compareTo(new BigDecimal(i2part));
					if (cmp != 0) {
						return cmp;
					}
				}
			}

			int cmp = i1part.compareTo(i2part);
			if (cmp != 0) {
				return cmp;
			}
		}

		return o1parts.length - o2parts.length;
	}
}
