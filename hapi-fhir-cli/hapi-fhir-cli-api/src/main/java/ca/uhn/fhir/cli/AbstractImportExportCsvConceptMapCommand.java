package ca.uhn.fhir.cli;

/*-
 * #%L
 * HAPI FHIR - Command Line Client - API
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import com.google.common.collect.Sets;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import static org.apache.commons.lang3.StringUtils.isAllBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class AbstractImportExportCsvConceptMapCommand extends BaseRequestGeneratingCommand {
	// TODO: Don't use qualified names for loggers in HAPI CLI.
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(AbstractImportExportCsvConceptMapCommand.class);

	protected static final String CONCEPTMAP_URL_PARAM = "u";
	protected static final String CONCEPTMAP_URL_PARAM_LONGOPT = "url";
	protected static final String CONCEPTMAP_URL_PARAM_NAME = "url";
	protected static final String CONCEPTMAP_URL_PARAM_DESC = "The URL of the ConceptMap resource to be imported/exported (i.e. ConceptMap.url).";
	protected static final String FILE_PARAM = "f";
	protected static final String FILE_PARAM_LONGOPT = "filename";
	protected static final String FILE_PARAM_NAME = "filename";
	protected static final String FILE_PARAM_DESC = "The path and filename of the CSV file to be imported/exported (e.g. ./input.csv, ./output.csv, etc.).";

	protected IGenericClient client;
	protected String conceptMapUrl;
	protected FhirVersionEnum fhirVersion;
	protected String file;


	@Override
	protected Collection<Object> getFilterOutVersions() {
		return Sets.newHashSet(FhirVersionEnum.DSTU2_1,
			FhirVersionEnum.DSTU2_HL7ORG, FhirVersionEnum.DSTU2);
	}

	protected BufferedReader getBufferedReader() throws IOException {
		return new BufferedReader(getInputStreamReader());
	}

	protected InputStreamReader getInputStreamReader() throws IOException {
		return new InputStreamReader(getBOMInputStream());
	}

	protected BOMInputStream getBOMInputStream() throws IOException {
		return new BOMInputStream(
			getInputStream(),
			false,
			ByteOrderMark.UTF_8,
			ByteOrderMark.UTF_16BE,
			ByteOrderMark.UTF_16LE,
			ByteOrderMark.UTF_32BE,
			ByteOrderMark.UTF_32LE);
	}

	protected InputStream getInputStream() throws IOException {
		return Files.newInputStream(Paths.get(file), StandardOpenOption.READ);
	}

	@Override
	public void run(CommandLine theCommandLine) throws ParseException, ExecutionException {
		parseFhirContext(theCommandLine);
		FhirContext ctx = getFhirContext();

		String targetServer = theCommandLine.getOptionValue(BASE_URL_PARAM);
		if (isBlank(targetServer)) {
			throw new ParseException(Msg.code(1583) + "No target server (-" + BASE_URL_PARAM + ") specified.");
		} else if (!targetServer.startsWith("http") && !targetServer.startsWith("file")) {
			throw new ParseException(Msg.code(1584) + "Invalid target server specified, must begin with 'http' or 'file'.");
		}

		conceptMapUrl = theCommandLine.getOptionValue(CONCEPTMAP_URL_PARAM);
		if (isBlank(conceptMapUrl)) {
			throw new ParseException(Msg.code(1585) + "No ConceptMap URL (" + CONCEPTMAP_URL_PARAM + ") specified.");
		} else {
			ourLog.info("Specified ConceptMap URL (ConceptMap.url): {}", conceptMapUrl);
		}

		file = theCommandLine.getOptionValue(FILE_PARAM);
		if (isBlank(file)) {
			throw new ParseException(Msg.code(1586) + "No file (" + FILE_PARAM + ") specified.");
		}
		if (!file.endsWith(".csv")) {
			file = file.concat(".csv");
		}

		parseAdditionalParameters(theCommandLine);

		client = super.newClient(theCommandLine);
		fhirVersion = ctx.getVersion().getVersion();
		if (fhirVersion != FhirVersionEnum.DSTU3
			&& fhirVersion != FhirVersionEnum.R4) {
			throw new ParseException(Msg.code(1587) + "This command does not support FHIR version " + fhirVersion + ".");
		}

		if (theCommandLine.hasOption(VERBOSE_LOGGING_PARAM)) {
			client.registerInterceptor(new LoggingInterceptor(true));
		}

		process();
	}

	protected void parseAdditionalParameters(CommandLine theCommandLine) {}

	protected abstract void process() throws ParseException, ExecutionException;

	protected enum Header {
		SOURCE_CODE_SYSTEM,
		SOURCE_CODE_SYSTEM_VERSION,
		TARGET_CODE_SYSTEM,
		TARGET_CODE_SYSTEM_VERSION,
		SOURCE_CODE,
		SOURCE_DISPLAY,
		TARGET_CODE,
		TARGET_DISPLAY,
		EQUIVALENCE,
		COMMENT
	}

	protected class TemporaryConceptMapGroup {
		private String source;
		private String sourceVersion;
		private String target;
		private String targetVersion;

		public TemporaryConceptMapGroup() {
		}

		public TemporaryConceptMapGroup(String theSource, String theSourceVersion, String theTarget, String theTargetVersion) {
			this.source = theSource;
			this.sourceVersion = theSourceVersion;
			this.target = theTarget;
			this.targetVersion = theTargetVersion;
		}

		public boolean hasSource() {
			return isNotBlank(source);
		}

		public String getSource() {
			return source;
		}

		public TemporaryConceptMapGroup setSource(String theSource) {
			this.source = theSource;
			return this;
		}

		public boolean hasSourceVersion() {
			return isNotBlank(sourceVersion);
		}

		public String getSourceVersion() {
			return sourceVersion;
		}

		public TemporaryConceptMapGroup setSourceVersion(String theSourceVersion) {
			this.sourceVersion = theSourceVersion;
			return this;
		}

		public boolean hasTarget() {
			return isNotBlank(target);
		}

		public String getTarget() {
			return target;
		}

		public TemporaryConceptMapGroup setTarget(String theTarget) {
			this.target = theTarget;
			return this;
		}

		public boolean hasTargetVersion() {
			return isNotBlank(targetVersion);
		}

		public String getTargetVersion() {
			return targetVersion;
		}

		public TemporaryConceptMapGroup setTargetVersion(String theTargetVersion) {
			this.targetVersion = theTargetVersion;
			return this;
		}

		public boolean hasValues() {
			return !isAllBlank(getSource(), getSourceVersion(), getTarget(), getTargetVersion());
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;

			if (!(o instanceof TemporaryConceptMapGroup)) return false;

			TemporaryConceptMapGroup that = (TemporaryConceptMapGroup) o;

			return new EqualsBuilder()
				.append(getSource(), that.getSource())
				.append(getSourceVersion(), that.getSourceVersion())
				.append(getTarget(), that.getTarget())
				.append(getTargetVersion(), that.getTargetVersion())
				.isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37)
				.append(getSource())
				.append(getSourceVersion())
				.append(getTarget())
				.append(getTargetVersion())
				.toHashCode();
		}
	}

	protected class TemporaryConceptMapGroupElement {
		private String code;
		private String display;

		public TemporaryConceptMapGroupElement() {
		}

		public TemporaryConceptMapGroupElement(String theCode, String theDisplay) {
			this.code = theCode;
			this.display = theDisplay;
		}

		public boolean hasCode() {
			return isNotBlank(code);
		}

		public String getCode() {
			return code;
		}

		public TemporaryConceptMapGroupElement setCode(String theCode) {
			this.code = theCode;
			return this;
		}

		public boolean hasDisplay() {
			return isNotBlank(display);
		}

		public String getDisplay() {
			return display;
		}

		public TemporaryConceptMapGroupElement setDisplay(String theDisplay) {
			this.display = theDisplay;
			return this;
		}

		public boolean hasValues() {
			return !isAllBlank(getCode(), getDisplay());
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;

			if (!(o instanceof TemporaryConceptMapGroupElement)) return false;

			TemporaryConceptMapGroupElement that = (TemporaryConceptMapGroupElement) o;

			return new EqualsBuilder()
				.append(getCode(), that.getCode())
				.append(getDisplay(), that.getDisplay())
				.isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37)
				.append(getCode())
				.append(getDisplay())
				.toHashCode();
		}
	}

	protected class TemporaryConceptMapGroupElementTarget {
		private String code;
		private String display;
		private String equivalence;
		private String comment;

		public TemporaryConceptMapGroupElementTarget() {
		}

		public TemporaryConceptMapGroupElementTarget(String theCode, String theDisplay, String theEquivalence, String theComment) {
			this.code = theCode;
			this.display = theDisplay;
			this.equivalence = theEquivalence;
			this.comment = theComment;
		}

		public boolean hasCode() {
			return isNotBlank(code);
		}

		public String getCode() {
			return code;
		}

		public TemporaryConceptMapGroupElementTarget setCode(String theCode) {
			this.code = theCode;
			return this;
		}

		public boolean hasDisplay() {
			return isNotBlank(display);
		}

		public String getDisplay() {
			return display;
		}

		public TemporaryConceptMapGroupElementTarget setDisplay(String theDisplay) {
			this.display = theDisplay;
			return this;
		}

		public boolean hasEquivalence() {
			return isNotBlank(equivalence);
		}

		public String getEquivalence() {
			return equivalence;
		}

		public TemporaryConceptMapGroupElementTarget setEquivalence(String theEquivalence) {
			this.equivalence = theEquivalence;
			return this;
		}

		public boolean hasComment() {
			return isNotBlank(comment);
		}

		public String getComment() {
			return comment;
		}

		public TemporaryConceptMapGroupElementTarget setComment(String theComment) {
			this.comment = theComment;
			return this;
		}

		public boolean hasValues() {
			return !isAllBlank(getCode(), getDisplay(), getEquivalence(), getComment());
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;

			if (!(o instanceof TemporaryConceptMapGroupElementTarget)) return false;

			TemporaryConceptMapGroupElementTarget that = (TemporaryConceptMapGroupElementTarget) o;

			return new EqualsBuilder()
				.append(getCode(), that.getCode())
				.append(getDisplay(), that.getDisplay())
				.append(getEquivalence(), that.getEquivalence())
				.append(getComment(), that.getComment())
				.isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37)
				.append(getCode())
				.append(getDisplay())
				.append(getEquivalence())
				.append(getComment())
				.toHashCode();
		}
	}
}
