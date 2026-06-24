package ca.uhn.fhir.tinder.ts;

// Created by Claude Opus 4.8

import ca.uhn.fhir.tinder.VelocityHelper;
import org.apache.commons.io.FileUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * Renders a {@link TsModel} to a directory of TypeScript source files: one {@code I<name>.ts} per
 * interface, one {@code <name>.ts} per enumeration, and a barrel {@code index.ts} re-exporting them all.
 */
public class TypescriptWriter {

	private static final Logger ourLog = LoggerFactory.getLogger(TypescriptWriter.class);

	private static final String INTERFACE_TEMPLATE = "/vm/ts_interface.vm";
	private static final String ENUM_TEMPLATE = "/vm/ts_enum.vm";
	private static final String INDEX_TEMPLATE = "/vm/ts_index.vm";

	public void writeModel(TsModel theModel, File theTargetDirectory) throws IOException {
		FileUtils.forceMkdir(theTargetDirectory);
		VelocityEngine engine = VelocityHelper.configureVelocityEngine(null, null, null);

		for (TsInterface nextInterface : theModel.getInterfaces()) {
			VelocityContext ctx = new VelocityContext();
			ctx.put("tsInterface", nextInterface);
			render(
					engine,
					INTERFACE_TEMPLATE,
					ctx,
					new File(theTargetDirectory, nextInterface.getInterfaceName() + ".ts"));
		}

		for (TsEnum nextEnum : theModel.getEnums()) {
			VelocityContext ctx = new VelocityContext();
			ctx.put("tsEnum", nextEnum);
			render(engine, ENUM_TEMPLATE, ctx, new File(theTargetDirectory, nextEnum.getName() + ".ts"));
		}

		VelocityContext indexCtx = new VelocityContext();
		indexCtx.put("model", theModel);
		render(engine, INDEX_TEMPLATE, indexCtx, new File(theTargetDirectory, "index.ts"));

		ourLog.info(
				"Wrote {} interfaces and {} enums for FHIR {} to {}",
				theModel.getInterfaces().size(),
				theModel.getEnums().size(),
				theModel.getVersion(),
				theTargetDirectory.getAbsolutePath());
	}

	private void render(VelocityEngine theEngine, String theTemplate, VelocityContext theContext, File theOutputFile)
			throws IOException {
		try (InputStream templateStream = getClass().getResourceAsStream(theTemplate)) {
			if (templateStream == null) {
				throw new IOException("Unable to locate template on classpath: " + theTemplate);
			}
			try (InputStreamReader templateReader = new InputStreamReader(templateStream, StandardCharsets.UTF_8);
					OutputStreamWriter writer = new OutputStreamWriter(
							new FileOutputStream(theOutputFile, false), StandardCharsets.UTF_8)) {
				theEngine.evaluate(theContext, writer, theTemplate, templateReader);
			}
		}
	}
}
