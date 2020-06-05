package ca.uhn.fhir.jpa.dao.predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JpaSystemProperties {

	private static final Logger ourLog = LoggerFactory.getLogger(JpaSystemProperties.class);
	public static final String HFJ_FORCE_IN_CLAUSES_HF_50 = "hfj_force_in_clauses_hf50";
	private static boolean ourOptmizeSingleElementInExpression;

	static {
		updateSettingsBasedOnCurrentSystemProperties();
	}

	/**
	 * Non instantiable
	 */
	private JpaSystemProperties() {
	}

	public static void updateSettingsBasedOnCurrentSystemProperties() {
		ourOptmizeSingleElementInExpression = !("true".equals(System.getProperty(HFJ_FORCE_IN_CLAUSES_HF_50)));
		if (ourOptmizeSingleElementInExpression) {
			ourLog.warn("Forcing IN(..) clauses for single element SQL predicates");
		}
	}

	public static boolean isOptmizeSingleElementInExpression() {
		return ourOptmizeSingleElementInExpression;
	}
}
