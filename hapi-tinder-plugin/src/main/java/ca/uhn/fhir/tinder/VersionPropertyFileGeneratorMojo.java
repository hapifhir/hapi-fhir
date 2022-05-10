package ca.uhn.fhir.tinder;

import ca.uhn.fhir.i18n.Msg;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.google.common.reflect.ClassPath;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.InstantType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.springframework.util.Assert;

//@Mojo(name = "generate-version-propertyfile", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class VersionPropertyFileGeneratorMojo extends AbstractMojo {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(VersionPropertyFileGeneratorMojo.class);

	// @Parameter(alias = "packageName", required = true)
	private String packageName;

	// @Parameter(alias = "targetFile", required = true)
	private File targetFile;

	@Override
	public void execute() throws MojoFailureException {
		TreeMap<String, Class<?>> resourceTypes = new TreeMap<>();
		TreeMap<String, Class<?>> datatypeTypes = new TreeMap<>();

		List<ClassPath.ClassInfo> components = null;
		try {
			components = ClassPath
				.from(VersionPropertyFileGeneratorMojo.class.getClassLoader())
				.getTopLevelClasses()
				.stream()
				.filter(t -> {
					return t.getPackageName().equals(packageName);
				})
				.collect(Collectors.toList());
		} catch (IOException e) {
			throw new MojoFailureException(Msg.code(108) + e.getMessage(), e);
		}

		Assert.isTrue(components.size() > 50, "Only have " + components.size() + " components");

		for (ClassPath.ClassInfo next : components) {

			Class<?> clazz = next.load();

			if (IBaseResource.class.isAssignableFrom(clazz)) {
				ResourceDef annotation = clazz.getAnnotation(ResourceDef.class);
				if (annotation == null) {
					continue;
				}
				ourLog.info("Found resource: {}", annotation.name());
				resourceTypes.put(annotation.name(), clazz);
			} else if (IBaseDatatype.class.isAssignableFrom(clazz)) {
				DatatypeDef annotation = clazz.getAnnotation(DatatypeDef.class);
				if (annotation == null) {
					continue;
				}
				ourLog.info("Found datatype: {}", annotation.name());
				String name = annotation.name();
				if (datatypeTypes.containsKey(name)) {
					name = annotation.name() + ".2";
				}
				if (datatypeTypes.containsKey(name)) {
					throw new Error(Msg.code(109) + "Already have " + name);
				}
				datatypeTypes.put(name, clazz);
			}

		}
		
		try {
			Class<?> clazz = Class.forName("org.hl7.fhir.utilities.xhtml.XhtmlNode");
			DatatypeDef annotation = clazz.getAnnotation(DatatypeDef.class);
			ourLog.info("Found datatype: {}", annotation.name());
			datatypeTypes.put(annotation.name(), clazz);
		} catch (ClassNotFoundException e1) {
			throw new MojoFailureException(Msg.code(110) + "Unknown", e1);
		}
		
		ourLog.info("Found {} resources and {} datatypes", resourceTypes.size(), datatypeTypes.size());
		ourLog.info("Writing propertyfile: {}", targetFile.getAbsolutePath());

		FileWriter w = null;
		try {
			w = new FileWriter(targetFile, false);
			w.write("# This file contains version definitions\n");
			w.write("# Generated: " + InstantType.now().getValueAsString() + "\n\n");
			for (Entry<String, Class<?>> nextEntry : resourceTypes.entrySet()) {
				w.write("resource.");
				w.write(nextEntry.getKey());
				w.write("=");
				w.write(nextEntry.getValue().getName());
				w.write("\n");
			}
			w.write("\n");
			for (Entry<String, Class<?>> nextEntry : datatypeTypes.entrySet()) {
				w.write("datatype.");
				w.write(nextEntry.getKey());
				w.write("=");
				w.write(nextEntry.getValue().getName());
				w.write("\n");
			}
			w.flush();
		} catch (IOException e) {
			throw new MojoFailureException(Msg.code(111) + "Failed to write property file", e);
		} finally {
			IOUtils.closeQuietly(w);
		}
	}

	public static void main(String[] theArgs) throws MojoFailureException {

//		VersionPropertyFileGeneratorMojo m = new VersionPropertyFileGeneratorMojo();
//		m.packageName = "org.hl7.fhir.r4.model";
//		m.targetFile = new File("hapi-fhir-structures-r4/src/main/resources/org/hl7/fhir/r4/model/fhirversion.properties");
//		m.execute();

		VersionPropertyFileGeneratorMojo m = new VersionPropertyFileGeneratorMojo();
		m.packageName = "org.hl7.fhir.r5.model";
		m.targetFile = new File("hapi-fhir-structures-r5/src/main/resources/org/hl7/fhir/r5/model/fhirversion.properties");
		m.execute();

//		m.packageName = "org.hl7.fhir.dstu3.model";
//		m.targetFile = new File("../hapi-fhir-structures-dstu3/src/main/resources/org/hl7/fhir/dstu3/model/fhirversion.properties");

//		m.packageName = "org.hl7.fhir.dstu2016may.model";
//		m.targetFile = new File("../hapi-fhir-structures-dstu2.1/src/main/resources/org/hl7/fhir/dstu2016may/model/fhirversion.properties");


	}

}
