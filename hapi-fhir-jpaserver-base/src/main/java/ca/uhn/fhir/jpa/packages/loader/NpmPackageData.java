package ca.uhn.fhir.jpa.packages.loader;

import org.hl7.fhir.utilities.npm.NpmPackage;

import java.io.InputStream;

public class NpmPackageData {

	private final String myPackageId;

	private final String myPackageVersionId;

	private final String mySourceDesc;

	private final byte[] myBytes;

	private final NpmPackage myPackage;

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
