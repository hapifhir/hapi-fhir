package ca.uhn.fhir.tinder.parser;

import ca.uhn.fhir.i18n.Msg;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.maven.plugin.MojoFailureException;

import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.tinder.model.BaseElement;
import ca.uhn.fhir.tinder.model.BaseRootType;
import ca.uhn.fhir.tinder.model.Composite;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class DatatypeGeneratorUsingSpreadsheet extends BaseStructureSpreadsheetParser {

	@Override
	protected void postProcess(BaseElement theTarget) throws MojoFailureException {
		super.postProcess(theTarget);

		/*
		 * Datatype values are all implied to be in the summary view for some reason. They have a column in the
		 * spreadsheet but it's never populated.
		 */
		theTarget.setSummary("Y");
	}

	public DatatypeGeneratorUsingSpreadsheet(String theVersion, String theBaseDir) throws MojoFailureException {
		super(theVersion, theBaseDir);
		super.setFilenameSuffix("Dt");
	}

	@Override
	protected String getTemplate() {
		String template = super.getTemplate();
		if (template != null) {
			return template;
		}
		return "dstu".equals(getVersion()) ? "/vm/dt_composite_dstu.vm" : "/vm/dt_composite.vm";
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
				if (def != null) {
					if (classInfo.getName().contains("Bound") == false) {
						getNameToDatatypeClass().put(def.name(), classInfo.getName());
					}
				}
			}
		} catch (IOException e) {
			throw new MojoFailureException(Msg.code(182) + e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			throw new MojoFailureException(Msg.code(183) + e.getMessage(), e);
		}

		try {
			ImmutableSet<ClassInfo> tlc = ClassPath.from(getClass().getClassLoader()).getTopLevelClasses(thePackageBase + ".composite");
			for (ClassInfo classInfo : tlc) {
				DatatypeDef def = Class.forName(classInfo.getName()).getAnnotation(DatatypeDef.class);
				if (def != null) {
					if (classInfo.getName().contains("Bound") == false) {
						getNameToDatatypeClass().put(def.name(), classInfo.getName());
					}
				}
			}
		} catch (IOException e) {
			throw new MojoFailureException(Msg.code(184) + e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			throw new MojoFailureException(Msg.code(185) + e.getMessage(), e);
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

		String version = getVersion();
		if (version.equals("dev")) {
			version = "dstu2";
		}

		retVal.add(("/dt/" + version + "/address.xml"));
		retVal.add(("/dt/" + version + "/attachment.xml"));
		retVal.add(("/dt/" + version + "/codeableconcept.xml"));
		retVal.add(("/dt/" + version + "/coding.xml"));
		retVal.add(("/dt/" + version + "/humanname.xml"));
		retVal.add(("/dt/" + version + "/identifier.xml"));
		retVal.add(("/dt/" + version + "/period.xml"));
		retVal.add(("/dt/" + version + "/ratio.xml"));
		retVal.add(("/dt/" + version + "/quantity.xml"));
		retVal.add(("/dt/" + version + "/range.xml"));
		retVal.add(("/dt/" + version + "/sampleddata.xml"));

		if ("dstu".equals(version)) {
			retVal.add(("/dt/" + version + "/contact.xml"));
			// retVal.add(("/dt/" + myVersion + "/resourcereference.xml"));
			retVal.add(("/dt/" + version + "/schedule.xml"));
		}

		if (!version.equals("dstu")) {
			retVal.add(("/dt/" + version + "/meta.xml"));
			retVal.add(("/dt/" + version + "/annotation.xml"));
			retVal.add(("/dt/" + version + "/attachment.xml"));
			retVal.add(("/dt/" + version + "/contactpoint.xml"));
			retVal.add(("/dt/" + version + "/elementdefinition.xml"));
			retVal.add(("/dt/" + version + "/timing.xml"));
			retVal.add(("/dt/" + version + "/signature.xml"));
		}

		return retVal;
	}

}
