package ca.uhn.fhir.jpa.config.min;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.util.BundleUtil;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.joran.util.ConfigurationWatchListUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.*;
import java.net.URL;
import java.util.Collection;

public class ResourceMinimizerMojo {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceMinimizerMojo.class);

	private String fhirVersion;

	private long myByteCount;
	private FhirContext myCtx;
	private int myFileCount;

	private File targetDirectory;

	public void execute() throws Exception {
		ourLog.info("Starting resource minimizer");

		if (myCtx != null) {
			// nothing
		} else if ("DSTU2".equals(fhirVersion)) {
			myCtx = FhirContext.forDstu2();
		} else if ("HL7ORG_DSTU2".equals(fhirVersion)) {
			myCtx = FhirContext.forDstu2Hl7Org();
		} else if ("DSTU2_1".equals(fhirVersion)) {
			myCtx = FhirContext.forDstu2_1();
		} else if ("DSTU3".equals(fhirVersion)) {
			myCtx = FhirContext.forDstu3();
		} else if ("R4".equals(fhirVersion)) {
			myCtx = FhirContext.forR4();
		} else {
			throw new Exception("Unknown version: " + fhirVersion);
		}

		ourLog.info("Looking for files in directory: {}", targetDirectory.getAbsolutePath());
		
		Collection<File> files = FileUtils.listFiles(targetDirectory, new String[] { "xml", "json" }, true);
		for (File nextFile : files) {
			ourLog.debug("Checking file: {}", nextFile);

			String inputString;
			try {
				inputString = IOUtils.toString(new FileInputStream(nextFile), "UTF-8");
			} catch (IOException e) {
				throw new Exception("Failed to read file: " + nextFile, e);
			}

			IParser parser = EncodingEnum.detectEncoding(inputString).newParser(myCtx);
			IBaseResource input = parser.parseResource(inputString);

			if (input instanceof IResource) {
				((IResource) input).getText().getDiv().setValueAsString((String) null);
				((IResource) input).getText().getStatus().setValueAsString((String) null);
				if (input instanceof Bundle) {
					for (Entry nextEntry : ((Bundle) input).getEntry()) {
						if (nextEntry.getResource() != null) {
							nextEntry.getResource().getText().getDiv().setValueAsString((String) null);
							nextEntry.getResource().getText().getStatus().setValueAsString((String) null);
						}
					}
				}
			} else {
				minimizeResource((IBaseResource)input);
			}

			String outputString = parser.setPrettyPrint(true).encodeResourceToString(input);
			StringBuilder b = new StringBuilder();
			for (String nextLine : outputString.split("\\n")) {
				int i;
				for (i = 0; i < nextLine.length(); i++) {
					if (nextLine.charAt(i) != ' ') {
						break;
					}
				}

				b.append(StringUtils.leftPad("", i / 3, ' '));
				b.append(nextLine.substring(i));
				b.append("\n");
			}
			outputString = b.toString();

			if (!inputString.equals(outputString)) {
				ourLog.info("Trimming contents of resource: {} - From {} to {}", nextFile, FileUtils.byteCountToDisplaySize(inputString.length()), FileUtils.byteCountToDisplaySize(outputString.length()));
				myByteCount += (inputString.length() - outputString.length());
				myFileCount++;
				try {
					String f = nextFile.getAbsolutePath();
					Writer w = new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8");
					w = new BufferedWriter(w);
					w.append(outputString);
					w.close();
				} catch (IOException e) {
					throw new Exception("Failed to write " + nextFile, e);
				}

			}

		}
	}

	public long getByteCount() {
		return myByteCount;
	}

	public int getFileCount() {
		return myFileCount;
	}

	private void minimizeResource(IBaseResource theInput) {
		if (theInput instanceof IBaseBundle) {
			for (IBaseResource next : BundleUtil.toListOfResources(myCtx, (IBaseBundle) theInput)) {
				minimizeResource(next);
			}
		}

		BaseRuntimeElementCompositeDefinition<?> element = (BaseRuntimeElementCompositeDefinition) myCtx.getElementDefinition(theInput.getClass());
		BaseRuntimeChildDefinition textElement = element.getChildByName("text");
		if (textElement != null) {
			textElement.getMutator().setValue(theInput, null);
		}
	}

	public static void main(String[] args) throws Exception {
		FhirContext ctxDstu2;
//		FhirContext ctxDstu2_1;
		FhirContext ctxDstu3;
		FhirContext ctxR4;
		ctxDstu2 = FhirContext.forDstu2();
//		ctxDstu2_1 = FhirContext.forDstu2_1();
		ctxDstu3 = FhirContext.forDstu3();
		ctxR4 = FhirContext.forR4();

		LoggerContext loggerContext = ((ch.qos.logback.classic.Logger) ourLog).getLoggerContext();
		URL mainURL = ConfigurationWatchListUtil.getMainWatchURL(loggerContext);
		System.out.println(mainURL);
		// or even
		ourLog.info("Logback used '{}' as the configuration file.", mainURL);

		int fileCount = 0;
		long byteCount = 0;
		
		ResourceMinimizerMojo m = new ResourceMinimizerMojo();

		m.myCtx = ctxDstu2;
		m.targetDirectory = new File("./hapi-tinder-plugin/src/main/resources/vs/dstu2");
		m.fhirVersion = "DSTU2";
		m.execute();
		byteCount += m.getByteCount();
		fileCount += m.getFileCount();

		m = new ResourceMinimizerMojo();
		m.myCtx = ctxDstu2;
		m.targetDirectory = new File("./hapi-fhir-validation-resources-dstu2/src/main/resources/org/hl7/fhir/instance/model/valueset");
		m.fhirVersion = "DSTU2";
		m.execute();
		byteCount += m.getByteCount();
		fileCount += m.getFileCount();

		m = new ResourceMinimizerMojo();
		m.myCtx = ctxDstu2;
		m.targetDirectory = new File("./hapi-fhir-validation-resources-dstu2/src/main/resources/org/hl7/fhir/instance/model/profile");
		m.fhirVersion = "DSTU2";
		m.execute();
		byteCount += m.getByteCount();
		fileCount += m.getFileCount();

		m = new ResourceMinimizerMojo();
		m.myCtx = ctxDstu3;
		m.targetDirectory = new File("./hapi-fhir-validation-resources-dstu3/src/main/resources/org/hl7/fhir/dstu3/model/profile");
		m.fhirVersion = "DSTU3";
		m.execute();
		byteCount += m.getByteCount();
		fileCount += m.getFileCount();

		m = new ResourceMinimizerMojo();
		m.myCtx = ctxDstu3;
		m.targetDirectory = new File("./hapi-fhir-validation-resources-dstu3/src/main/resources/org/hl7/fhir/dstu3/model/valueset");
		m.fhirVersion = "DSTU3";
		m.execute();
		byteCount += m.getByteCount();

//		m = new ResourceMinimizerMojo();
//		m.myCtx = ctxDstu2_1;
//		m.targetDirectory = new File("./hapi-fhir-validation-resources-dstu2.1/src/main/resources/org/hl7/fhir/dstu2016may/model/profile");
//		m.fhirVersion = "DSTU2_1";
//		m.execute();
//		byteCount += m.getByteCount();
//		fileCount += m.getFileCount();
//
//		m = new ResourceMinimizerMojo();
//		m.myCtx = ctxDstu2_1;
//		m.targetDirectory = new File("./hapi-fhir-validation-resources-dstu2.1/src/main/resources/org/hl7/fhir/dstu2016may/model/valueset");
//		m.fhirVersion = "DSTU2_1";
//		m.execute();
//		byteCount += m.getByteCount();
//		fileCount += m.getFileCount();

		m = new ResourceMinimizerMojo();
		m.myCtx = ctxR4;
		m.targetDirectory = new File("./hapi-fhir-validation-resources-r4/src/main/resources/org/hl7/fhir/r4/model/profile");
		m.fhirVersion = "R4";
		m.execute();
		byteCount += m.getByteCount();
		fileCount += m.getFileCount();

		m = new ResourceMinimizerMojo();
		m.myCtx = ctxR4;
		m.targetDirectory = new File("./hapi-fhir-validation-resources-r4/src/main/resources/org/hl7/fhir/r4/model/valueset");
		m.fhirVersion = "R4";
		m.execute();
		byteCount += m.getByteCount();
		fileCount += m.getFileCount();

		ourLog.info("Trimmed {} files", fileCount);
		ourLog.info("Trimmed {} bytes", FileUtils.byteCountToDisplaySize(byteCount));
	}

}
