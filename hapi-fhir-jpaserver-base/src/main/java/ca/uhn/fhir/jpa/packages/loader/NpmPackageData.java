/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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

import org.hl7.fhir.utilities.npm.NpmPackage;

import java.io.InputStream;

public class NpmPackageData {

	/**
	 * package id (npm id)
	 */
	private final String myPackageId;

	/**
	 * package version id (npm version)
	 */
	private final String myPackageVersionId;

	/**
	 * package description (url to find package, often)
	 */
	private final String mySourceDesc;

	/**
	 * The raw bytes of the entire package
	 */
	private final byte[] myBytes;

	/**
	 * The actual NpmPackage.
	 */
	private final NpmPackage myPackage;

	/**
	 * The raw stream of the entire npm package contents
	 */
	private final InputStream myInputStream;

	public NpmPackageData(
			String thePackageId,
			String thePackageVersionId,
			String theSourceDesc,
			byte[] theBytes,
			NpmPackage thePackage,
			InputStream theStream) {
		myPackageId = thePackageId;
		myPackageVersionId = thePackageVersionId;
		mySourceDesc = theSourceDesc;
		myBytes = theBytes;
		myPackage = thePackage;
		myInputStream = theStream;
	}

	public byte[] getBytes() {
		return myBytes;
	}

	public NpmPackage getPackage() {
		return myPackage;
	}

	public InputStream getInputStream() {
		return myInputStream;
	}

	public String getPackageId() {
		return myPackageId;
	}

	public String getPackageVersionId() {
		return myPackageVersionId;
	}

	public String getSourceDesc() {
		return mySourceDesc;
	}
}
