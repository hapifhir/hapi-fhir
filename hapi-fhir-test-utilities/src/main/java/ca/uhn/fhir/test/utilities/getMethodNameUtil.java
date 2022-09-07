package ca.uhn.fhir.test.utilities;

public class getMethodNameUtil {
	public static String getTestName() {
		return new Exception().getStackTrace()[1].getMethodName();
	}
}
