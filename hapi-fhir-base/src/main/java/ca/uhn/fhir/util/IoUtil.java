package ca.uhn.fhir.util;

/*-
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2018 Simpatico Intelligent Systems Inc
 * %%
 * All rights reserved.
 * #L%
 */

import java.io.Closeable;
import java.io.IOException;

public class IoUtil {

	/**
	 * Replacement for the deprecated commons-lang method of the same name. Use sparingly
	 * since they are right that most uses of this should be replaced with try-with-resources
	 */
	public static void closeQuietly(final AutoCloseable theCloseable) {
		try {
			if (theCloseable != null) {
				theCloseable.close();
			}
		} catch (final Exception ioe) {
			// ignore
		}
	}

}
