/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.packages.loader;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.ClasspathUtil;
import jakarta.annotation.Nullable;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.npm.BasePackageCacheManager;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class PackageLoaderSvc extends BasePackageCacheManager {

	private static final Logger ourLog = LoggerFactory.getLogger(PackageLoaderSvc.class);

	public NpmPackageData fetchPackageFromPackageSpec(PackageInstallationSpec theSpec) throws IOException {
		if (isNotBlank(theSpec.getPackageUrl())) {
			byte[] contents = loadPackageUrlContents(theSpec.getPackageUrl());
			return createNpmPackageDataFromData(
					theSpec.getName(),
					theSpec.getVersion(),
					theSpec.getPackageUrl(),
					new ByteArrayInputStream(contents));
		}

		return fetchPackageFromServerInternal(theSpec.getName(), theSpec.getVersion());
	}

	/**
	 * Loads the package, but won't save it anywhere.
	 * Returns the data to the caller
	 *
	 * @return - a POJO containing information about the NpmPackage, as well as it's contents
	 * 			as fetched from the server
	 * @throws IOException
	 */
	public NpmPackageData fetchPackageFromPackageSpec(String thePackageId, String thePackageVersion)
			throws FHIRException, IOException {
		return fetchPackageFromServerInternal(thePackageId, thePackageVersion);
	}

	private NpmPackageData fetchPackageFromServerInternal(String thePackageId, String thePackageVersion)
			throws IOException {
		BasePackageCacheManager.InputStreamWithSrc pkg = this.loadFromPackageServer(thePackageId, thePackageVersion);

		if (pkg == null) {
			throw new ResourceNotFoundException(
					Msg.code(1301) + "Unable to locate package " + thePackageId + "#" + thePackageVersion);
		}

		NpmPackageData npmPackage = createNpmPackageDataFromData(
				thePackageId, thePackageVersion == null ? pkg.version : thePackageVersion, pkg.url, pkg.stream);

		return npmPackage;
	}

	/**
	 * Creates an NpmPackage data object.
	 *
	 * @param thePackageId - the id of the npm package
	 * @param thePackageVersionId - the version id of the npm package
	 * @param theSourceDesc - the installation spec description or package url
	 * @param thePackageTgzInputStream - the package contents.
	 *                                  Typically fetched from a server, but can be added directly to the package spec
	 * @return
	 * @throws IOException
	 */
	public NpmPackageData createNpmPackageDataFromData(
			String thePackageId, String thePackageVersionId, String theSourceDesc, InputStream thePackageTgzInputStream)
			throws IOException {
		Validate.notBlank(thePackageId, "thePackageId must not be null");
		Validate.notBlank(thePackageVersionId, "thePackageVersionId must not be null");
		Validate.notNull(thePackageTgzInputStream, "thePackageTgzInputStream must not be null");

		byte[] bytes = IOUtils.toByteArray(thePackageTgzInputStream);

		ourLog.info("Parsing package .tar.gz ({} bytes) from {}", bytes.length, theSourceDesc);

		NpmPackage npmPackage = NpmPackage.fromPackage(new ByteArrayInputStream(bytes));

		return new NpmPackageData(
				thePackageId, thePackageVersionId, theSourceDesc, bytes, npmPackage, thePackageTgzInputStream);
	}

	@Override
	public NpmPackage loadPackageFromCacheOnly(String theS, @Nullable String theS1) {
		throw new UnsupportedOperationException(Msg.code(2215)
				+ "Cannot load from cache. "
				+ "Caching not supported in PackageLoaderSvc. Use JpaPackageCache instead.");
	}

	@Override
	public NpmPackage addPackageToCache(String theS, String theS1, InputStream theInputStream, String theS2)
			throws IOException {
		throw new UnsupportedOperationException(Msg.code(2216)
				+ "Cannot add to cache. "
				+ "Caching not supported in PackageLoaderSvc. Use JpaPackageCache instead.");
	}

	@Override
	public NpmPackage loadPackage(String theS, String theS1) throws FHIRException {
		/*
		 * We throw an exception because while we could pipe this call through
		 * to loadPackageOnly ourselves, returning NpmPackage details
		 * on their own provides no value if nothing is cached/loaded onto hard disk somewhere
		 *
		 */
		throw new UnsupportedOperationException(Msg.code(2217)
				+ "No packages are cached; "
				+ " this service only loads from the server directly. "
				+ "Call fetchPackageFromServer to fetch the npm package from the server. "
				+ "Or use JpaPackageCache for a cache implementation.");
	}

	public byte[] loadPackageUrlContents(String thePackageUrl) {
		if (thePackageUrl.startsWith("classpath:")) {
			return ClasspathUtil.loadResourceAsByteArray(thePackageUrl.substring("classpath:".length()));
		} else if (thePackageUrl.startsWith("file:")) {
			try {
				byte[] bytes = Files.readAllBytes(Paths.get(new URI(thePackageUrl)));
				return bytes;
			} catch (IOException | URISyntaxException e) {
				throw new InternalErrorException(
						Msg.code(2031) + "Error loading \"" + thePackageUrl + "\": " + e.getMessage());
			}
		} else {
			HttpClientConnectionManager connManager = new BasicHttpClientConnectionManager();
			try (CloseableHttpResponse request = HttpClientBuilder.create()
					.setConnectionManager(connManager)
					.build()
					.execute(new HttpGet(thePackageUrl))) {
				if (request.getStatusLine().getStatusCode() != 200) {
					throw new ResourceNotFoundException(Msg.code(1303) + "Received HTTP "
							+ request.getStatusLine().getStatusCode() + " from URL: " + thePackageUrl);
				}
				return IOUtils.toByteArray(request.getEntity().getContent());
			} catch (IOException e) {
				throw new InternalErrorException(
						Msg.code(1304) + "Error loading \"" + thePackageUrl + "\": " + e.getMessage());
			}
		}
	}
}
