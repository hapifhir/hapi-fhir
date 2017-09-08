package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseCommand implements Comparable<BaseCommand> {

	private static final String SPEC_DEFAULT_VERSION = "dstu3";
	private static final Logger ourLog = LoggerFactory.getLogger(BaseCommand.class);
	private FhirContext myFhirCtx;

	public BaseCommand() {
		super();
	}

	protected void addFhirVersionOption(Options theOptions) {
		Option opt = new Option("f", "fhirversion", true, "Spec version to upload (default is '" + SPEC_DEFAULT_VERSION + "')");
		opt.setRequired(false);
		theOptions.addOption(opt);
	}

	@Override
	public int compareTo(BaseCommand theO) {
		return getCommandName().compareTo(theO.getCommandName());
	}

	private void downloadFileFromInternet(CloseableHttpResponse result, File localFile) throws IOException {
		FileOutputStream buffer = FileUtils.openOutputStream(localFile);
		try {

			long maxLength = result.getEntity().getContentLength();
			long nextLog = -1;
			// ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int nRead;
			byte[] data = new byte[16384];
			while ((nRead = result.getEntity().getContent().read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
				long fileSize = FileUtils.sizeOf(localFile);
				if (fileSize > nextLog) {
					System.err.print("\r" + Ansi.ansi().eraseLine());
					System.err.print(FileUtils.byteCountToDisplaySize(fileSize));
					if (maxLength > 0) {
						System.err.print(" [");
						int stars = (int) (50.0f * ((float) fileSize / (float) maxLength));
						for (int i = 0; i < stars; i++) {
							System.err.print("*");
						}
						for (int i = stars; i < 50; i++) {
							System.err.print(" ");
						}
						System.err.print("]");
					}
					System.err.flush();
					nextLog += 100000;
				}
			}
			buffer.flush();

			System.err.println();
			System.err.flush();
		} finally {
			IOUtils.closeQuietly(buffer);
		}
	}

	public abstract String getCommandDescription();

	public abstract String getCommandName();

	public abstract Options getOptions();

	protected FhirContext getSpecVersionContext(CommandLine theCommandLine) throws ParseException {
		if (myFhirCtx == null) {
			String specVersion = theCommandLine.getOptionValue("f", SPEC_DEFAULT_VERSION);
			specVersion = specVersion.toLowerCase();
			FhirVersionEnum version;
			if ("dstu2".equals(specVersion)) {
				version = FhirVersionEnum.DSTU2;
			} else if ("dstu3".equals(specVersion)) {
				version = FhirVersionEnum.DSTU3;
			} else if ("r4".equals(specVersion)) {
				version = FhirVersionEnum.R4;
			} else {
				throw new ParseException("Unknown spec version: " + specVersion);
			}

			myFhirCtx = new FhirContext(version);
		}
		return myFhirCtx;
	}

//	public FhirContext getFhirCtx() {
//		if (myFhirCtx == null) {
//			myFhirCtx = FhirContext.forDstu2();
//		}
//		return myFhirCtx;
//	}

	protected Collection<File> loadFile(FhirContext theCtx, String theSpecUrl, String theFilepath, boolean theCacheFile) throws IOException {
		String userHomeDir = System.getProperty("user.home");

		File applicationDir = new File(userHomeDir + File.separator + "." + "hapi-fhir-cli");
		FileUtils.forceMkdir(applicationDir);

		Collection<File> inputFiles;
		if (isNotBlank(theFilepath)) {
			ourLog.info("Loading from local path: {}", theFilepath);

			if (theFilepath.startsWith("~" + File.separator)) {
				theFilepath = userHomeDir + theFilepath.substring(1);
			}

			File suppliedFile = new File(FilenameUtils.normalize(theFilepath));

			if (suppliedFile.isDirectory()) {
				inputFiles = FileUtils.listFiles(suppliedFile, new String[]{"zip"}, false);
			} else {
				inputFiles = Collections.singletonList(suppliedFile);
			}

		} else {

			File cacheDir = new File(applicationDir, "cache");
			FileUtils.forceMkdir(cacheDir);

			File inputFile = new File(cacheDir, "examples-json-" + theCtx.getVersion().getVersion() + ".zip");

			Date cacheExpiryDate = DateUtils.addHours(new Date(), -12);

			if (!inputFile.exists() | (theCacheFile && FileUtils.isFileOlder(inputFile, cacheExpiryDate))) {

				File exampleFileDownloading = new File(cacheDir, "examples-json-" + theCtx.getVersion().getVersion() + ".zip.partial");

				HttpGet get = new HttpGet(theSpecUrl);
				CloseableHttpClient client = HttpClientBuilder.create().build();
				CloseableHttpResponse result = client.execute(get);

				if (result.getStatusLine().getStatusCode() != 200) {
					throw new CommandFailureException("Got HTTP " + result.getStatusLine().getStatusCode() + " response code loading " + theSpecUrl);
				}

				ourLog.info("Downloading from remote url: {}", theSpecUrl);
				downloadFileFromInternet(result, exampleFileDownloading);

				FileUtils.deleteQuietly(inputFile);
				FileUtils.moveFile(exampleFileDownloading, inputFile);

				if (!theCacheFile) {
					inputFile.deleteOnExit();
				}

				ourLog.info("Successfully Loaded example pack ({})", FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(inputFile)));
				IOUtils.closeQuietly(result.getEntity().getContent());
			}

			inputFiles = Collections.singletonList(inputFile);

		}
		return inputFiles;
	}

	protected IGenericClient newClient(FhirContext ctx, String theBaseUrl) {
		ctx.getRestfulClientFactory().setSocketTimeout(10 * 60 * 1000);
		IGenericClient fhirClient = ctx.newRestfulGenericClient(theBaseUrl);
		return fhirClient;
	}

	public abstract void run(CommandLine theCommandLine) throws ParseException, Exception;

}
