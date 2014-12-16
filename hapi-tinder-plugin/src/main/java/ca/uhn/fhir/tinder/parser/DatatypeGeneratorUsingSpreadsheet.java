package ca.uhn.fhir.tinder.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.maven.plugin.MojoFailureException;

import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.tinder.model.BaseRootType;
import ca.uhn.fhir.tinder.model.Composite;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class DatatypeGeneratorUsingSpreadsheet extends BaseStructureSpreadsheetParser {

	private String myVersion;

	public DatatypeGeneratorUsingSpreadsheet(String theVersion, String theBaseDir) {
		super(theVersion, theBaseDir);
		myVersion = theVersion;
	}

	@Override
	protected String getTemplate() {
		return "dstu".equals(myVersion) ? "/vm/dt_composite_dstu.vm" : "/vm/dt_composite.vm";
	}

	@Override
	protected String getFilenameSuffix() {
		return "Dt";
	}

	@Override
	protected Collection<InputStream> getInputStreams() {
		ArrayList<InputStream> retVal = new ArrayList<InputStream>();

		for (String next : getInputStreamNames()) {
			retVal.add(getClass().getResourceAsStream(next));
		}
		
		return retVal;
	}

	@Override
	public void writeAll(File theOutputDirectory, File theResourceOutputDirectory, String thePackageBase) throws MojoFailureException {
		
		try {
			ImmutableSet<ClassInfo> tlc = ClassPath.from(getClass().getClassLoader()).getTopLevelClasses(StringDt.class.getPackage().getName());
			for (ClassInfo classInfo : tlc) {
				DatatypeDef def = Class.forName(classInfo.getName()).getAnnotation(DatatypeDef.class);
				if (def!=null) {
					getNameToDatatypeClass().put(def.name(), classInfo.getName());
				}
			}
		} catch (IOException e) {
			throw new MojoFailureException(e.getMessage(),e);
		} catch (ClassNotFoundException e) {
			throw new MojoFailureException(e.getMessage(),e);
		}
		
		
		super.writeAll(theOutputDirectory, theResourceOutputDirectory, thePackageBase);
	}

	@Override
	protected BaseRootType createRootType() {
		return new Composite();
	}

	@Override
	protected List<String> getInputStreamNames() {
		ArrayList<String> retVal = new ArrayList<String>();

		retVal.add(("/dt/" + myVersion + "/address.xml"));
		retVal.add(("/dt/" + myVersion + "/attachment.xml"));
		retVal.add(("/dt/" + myVersion + "/codeableconcept.xml"));
		retVal.add(("/dt/" + myVersion + "/coding.xml"));
		retVal.add(("/dt/" + myVersion + "/humanname.xml"));
		retVal.add(("/dt/" + myVersion + "/identifier.xml"));
		retVal.add(("/dt/" + myVersion + "/period.xml"));
		retVal.add(("/dt/" + myVersion + "/ratio.xml"));
		retVal.add(("/dt/" + myVersion + "/quantity.xml"));
		retVal.add(("/dt/" + myVersion + "/range.xml"));
		retVal.add(("/dt/" + myVersion + "/sampleddata.xml"));
		
		if ("dstu".equals(myVersion)) {
			retVal.add(("/dt/" + myVersion + "/contact.xml"));
//			retVal.add(("/dt/" + myVersion + "/resourcereference.xml"));
			retVal.add(("/dt/" + myVersion + "/schedule.xml"));
		}
		
		if (!myVersion.equals("dstu")) {			
			retVal.add(("/dt/" + myVersion + "/attachment.xml"));
			retVal.add(("/dt/" + myVersion + "/contactpoint.xml"));
			retVal.add(("/dt/" + myVersion + "/elementdefinition.xml"));
			retVal.add(("/dt/" + myVersion + "/reference.xml"));
			retVal.add(("/dt/" + myVersion + "/timing.xml"));
		}
		
		return retVal;
	}


}
