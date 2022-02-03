package ca.uhn.fhir.tinder;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.tinder.parser.BaseStructureSpreadsheetParser;
import org.apache.commons.lang.WordUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

public class Configuration {

	private final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(Configuration.class);

	private String version;
	private File targetDirectory;
	private String packageSuffix;

	private String packageBase;
	private FhirContext fhirContext;
	private File packageDirectoryBase;

	private final List<String> resourceNames = new ArrayList<>();
	private String baseDir;

	public Configuration(String version, String baseDir, File targetDirectory, String packageBase, List<String> baseResourceNames, List<String> excludeResourceNames) {
		this.targetDirectory = targetDirectory;
		this.packageBase = packageBase;
		this.packageDirectoryBase = new File(targetDirectory, packageBase.replace(".", File.separatorChar + ""));

		switch (version) {
			case "dstu2":
				fhirContext = FhirContext.forDstu2();
				break;
			case "dstu3":
				fhirContext = FhirContext.forDstu3();
				packageSuffix = ".dstu3";
				break;
			case "r4":
				fhirContext = FhirContext.forR4();
				packageSuffix = ".r4";
				break;
			default:
				throw new IllegalArgumentException(Msg.code(92) + "Unknown version configured: " + version);
		}

		this.version = version;
		if (baseResourceNames == null || baseResourceNames.isEmpty()) {
			ourLog.info("No resource names supplied, going to use all resources from version: {}", fhirContext.getVersion().getVersion());

			Properties p = new Properties();
			try {
				p.load(fhirContext.getVersion().getFhirVersionPropertiesFile());
			} catch (IOException e) {
				throw new IllegalArgumentException(Msg.code(93) + "Failed to load version property file", e);
			}

			ourLog.debug("Property file contains: {}", p);

			TreeSet<String> keys = new TreeSet<String>();
			for (Object next : p.keySet()) {
				keys.add((String) next);
			}
			for (String next : keys) {
				if (next.startsWith("resource.")) {
					resourceNames.add(next.substring("resource.".length()).toLowerCase());
				}
			}

			if (fhirContext.getVersion().getVersion() == FhirVersionEnum.DSTU3) {
				resourceNames.remove("conformance");
			}
		} else {
			for (String resourceName : baseResourceNames) {
				resourceNames.add(resourceName.toLowerCase());
			}
		}

		if (excludeResourceNames != null) {
			for (String resourceName : excludeResourceNames) {
				resourceNames.remove(resourceName.toLowerCase());
			}
		}

		ourLog.info("Including the following resources: {}", resourceNames);
	}

	public File getPackageDirectoryBase() {
		return packageDirectoryBase;
	}

	public String getPackageSuffix() {
		return packageSuffix;
	}

	public List<String> getResourceNames() {
		return resourceNames;
	}

	public String getPackageBase() {
		return packageBase;
	}

	public String getVersion() {
		return version;
	}

	public String getResourcePackage() {
		if (BaseStructureSpreadsheetParser.determineVersionEnum(version).isRi()) {
			return "org.hl7.fhir." + version + ".model";
		}
		return "ca.uhn.fhir.model." + version + ".resource";
	}

	public String getVersionCapitalized() {
		String capitalize = WordUtils.capitalize(version);
		if ("Dstu".equals(capitalize)) {
			return "Dstu1";
		}
		return capitalize;
	}

	public File getTargetDirectory() {
		return targetDirectory;
	}

	public String getBaseDir() {
		return baseDir;
	}
}
