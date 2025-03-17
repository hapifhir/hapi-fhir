package ca.uhn.fhir.broker.util;

import org.springframework.beans.factory.DisposableBean;

public class CloseUtil {
	public static void close(Object theObject) {
		try {
			if (theObject instanceof AutoCloseable) {
				((AutoCloseable) theObject).close();
			}
			if (theObject instanceof DisposableBean) {
				((DisposableBean) theObject).destroy();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
