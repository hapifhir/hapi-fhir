package ca.uhn.fhir.cli;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.fusesource.jansi.Ansi;
import org.hl7.fhir.dstu3.hapi.validation.DefaultProfileValidationSupport;
import org.hl7.fhir.dstu3.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.omg.Dynamic.Parameter;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Bundle.EntryRequest;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.apache.GZipContentInterceptor;
import ca.uhn.fhir.rest.server.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;

public class UploadTerminologyCommand extends BaseCommand {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(UploadTerminologyCommand.class);

	@Override
	public String getCommandDescription() {
		//@formatter:off
		return "Uploads a terminology package (e.g. a SNOMED CT ZIP file) to a HAPI JPA server. "
				+ "Note that this command uses a custom operation that is only implemented on HAPI "
				+ "JPA servers that have been configured to accept it.";
		//@formatter:on
	}

	@Override
	public String getCommandName() {
		return "upload-terminology";
	}

	@Override
	public Options getOptions() {
		Options options = new Options();
		Option opt;

		addFhirVersionOption(options);

		opt = new Option("t", "target", true, "Base URL for the target server (e.g. \"http://example.com/fhir\")");
		opt.setRequired(true);
		options.addOption(opt);

		opt = new Option("u", "url", true, "The code system URL associated with this upload (e.g. " + IHapiTerminologyLoaderSvc.SCT_URL + ")");
		opt.setRequired(false);
		options.addOption(opt);

		opt = new Option("d", "data", true, "Local *.zip containing file to use to upload");
		opt.setRequired(false);
		options.addOption(opt);

		return options;
	}

	@Override
	public void run(CommandLine theCommandLine) throws Exception {
		FhirContext ctx = getSpecVersionContext(theCommandLine);

		String targetServer = theCommandLine.getOptionValue("t");
		if (isBlank(targetServer)) {
			throw new ParseException("No target server (-t) specified");
		} else if (targetServer.startsWith("http") == false && targetServer.startsWith("file") == false) {
			throw new ParseException("Invalid target server specified, must begin with 'http' or 'file'");
		}

		String termUrl = theCommandLine.getOptionValue("u");
		if (isBlank(termUrl)) {
			throw new ParseException("No URL provided");
		}
		
		String datafile = theCommandLine.getOptionValue("d");
		if (isBlank(datafile)) {
			throw new ParseException("No data file provided");
		}

		IGenericClient client = super.newClient(ctx, targetServer);
		IBaseParameters inputParameters;
		if (ctx.getVersion().getVersion() == FhirVersionEnum.DSTU3) {
			Parameters p = new Parameters();
			p.addParameter().setName("url").setValue(new StringType(termUrl));
			p.addParameter().setName("localfile").setValue(new StringType(datafile));
			inputParameters = p;
		} else {
			throw new ParseException("This command does not support FHIR version " + ctx.getVersion().getVersion());
		}

		ourLog.info("Beginning upload - This may take a while...");
		IBaseParameters response = client
			.operation()
			.onServer()
			.named("upload-external-code-system")
			.withParameters(inputParameters)
			.execute();

		ourLog.info("Upload complete!");
	}

}
