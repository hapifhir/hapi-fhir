/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.packages.loader;

import org.hl7.fhir.utilities.http.ManagedWebAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A scoped application of {@link PackageLoaderSettings}.
 * <p>
 * Package loader settings are JVM-global: applying them mutates static state in this class and in the
 * underlying core library. This scope records whatever was in effect when it was opened and puts it back
 * on {@link #close()}, so that a caller which needs particular settings for a bounded piece of work does
 * not leave them behind for whoever runs next.
 * <p>
 * Obtain one from {@link PackageLoaderSvc#applySettings(PackageLoaderSettings)} and use it with
 * try-with-resources. Scopes may be nested; they restore in reverse order of opening.
 * <p>
 * <b>This is safe for sequential use only.</b> Because the underlying state is a single global, two
 * threads holding overlapping scopes will interfere with each other, and the second to close will restore
 * a value the first never saw. Work that mutates these settings concurrently must be isolated some other
 * way, such as by running in its own JVM.
 */
public class PackageLoaderSettingsScope implements AutoCloseable {
	private static final Logger ourLog = LoggerFactory.getLogger(PackageLoaderSettingsScope.class);

	private final PackageLoaderSettings myPrevious;
	private final PackageLoaderSettings myApplied;
	private final boolean myPreviousSsrfProtectionEnabled;
	private boolean myClosed;

	/**
	 * Never construct directly
	 * This is created by PackageLoaderSvc.applySettings.
	 * So this should be always in the same package as PackageLoaderSvc
	 */
	PackageLoaderSettingsScope(
			PackageLoaderSettings thePrevious,
			PackageLoaderSettings theCurrent,
			boolean thePreviousSsrfProtectionEnabled) {
		myPrevious = thePrevious;
		myApplied = theCurrent;
		myPreviousSsrfProtectionEnabled = thePreviousSsrfProtectionEnabled;
	}

	/**
	 * Restores the settings that were in effect when this scope was opened.
	 * <p>
	 * Where settings had previously been applied, re-applying them recomputes the SSRF flag, so the flag is
	 * not restored separately. Where none had been applied there is nothing to recompute from, so the flag
	 * captured when this scope opened is put back explicitly. Resetting alone would not do: the library
	 * defaults SSRF protection to on, which is not necessarily what this JVM had in effect.
	 * <p>
	 * Closing more than once has no effect beyond the first call.
	 */
	@Override
	public void close() {
		synchronized (PackageLoaderSvc.class) {
			if (myClosed) {
				return;
			}
			myClosed = true;

			if (PackageLoaderSvc.getAppliedSettings() != myApplied) {
				ourLog.warn(
						"Package loader settings were changed while a scope was open; restoring the settings this scope replaced anyway. This usually means two scopes overlapped on different threads (typically in a test).");
			}

			if (myPrevious == null) {
				PackageLoaderSvc.resetSettings();
				ManagedWebAccess.setSsrfProtectionEnabled(myPreviousSsrfProtectionEnabled);
			} else {
				PackageLoaderSvc.doApplySettings(myPrevious);
			}
		}
	}
}
