package ca.uhn.fhir.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class TestUtil {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TestUtil.class);

	/**
	 * THIS IS FOR UNIT TESTS ONLY
	 * 
	 * When we run the unit tests in cobertura, JUnit doesn't seem to clean up static fields which leads to 
	 * tons of memory being used by the end and the JVM crashes in Travis. Manually clearing all of the
	 * static fields seems to solve this.
	 */
	@CoverageIgnore
	public static void clearAllStaticFieldsForUnitTest() {
		
		Class<?> theType;
		try {
			throw new Exception();
		} catch (Exception e) {
			StackTraceElement[] st = e.getStackTrace();
			StackTraceElement elem = st[1];
			String clazzName = elem.getClassName();
			try {
				theType = Class.forName(clazzName);
			} catch (ClassNotFoundException e1) {
				throw new Error(e);
			}
		}
		
		for (Field next : Arrays.asList(theType.getDeclaredFields())) {
			if (Modifier.isStatic(next.getModifiers())) {
				if (!Modifier.isFinal(next.getModifiers()) && !next.getType().isPrimitive()) {
					ourLog.info("Clearing value of field: {}", next.toString());
					try {
						next.setAccessible(true);
						next.set(theType, null);
					} catch (Exception e) {
						throw new Error(e);
					}
				}
			}
		}
	}
	
}
