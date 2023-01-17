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
		InputStream theStream
	) {
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
