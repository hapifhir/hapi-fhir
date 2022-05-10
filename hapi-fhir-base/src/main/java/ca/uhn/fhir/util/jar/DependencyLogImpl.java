package ca.uhn.fhir.util.jar;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import ca.uhn.fhir.util.XmlUtil;

public class DependencyLogImpl implements IDependencyLog {
	private static final Attributes.Name BUNDLE_SYMBOLIC_NAME = new Attributes.Name("Bundle-SymbolicName");
	private static final Attributes.Name BUNDLE_VENDOR = new Attributes.Name("Bundle-Vendor");
	private static final Attributes.Name BUNDLE_VERSION = new Attributes.Name("Bundle-Version");
	private static final Attributes.Name IMPLEMENTATION_TITLE = new Attributes.Name("Implementation-Title");
	private static final Attributes.Name IMPLEMENTATION_VENDOR = new Attributes.Name("Implementation-Vendor");
	private static final Attributes.Name IMPLEMENTATION_VERSION = new Attributes.Name("Implementation-Version");
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(XmlUtil.class);
	
	@Override
	public void logStaxImplementation(Class<?> theClass) {
		try {
			URL rootUrl = getRootUrlForClass(theClass);
			if (rootUrl == null) {
				ourLog.info("Unable to determine location of StAX implementation containing class");
			} else {
				Manifest manifest;
				URL metaInfUrl = new URL(rootUrl, "META-INF/MANIFEST.MF");
				InputStream is = metaInfUrl.openStream();
				try {
					manifest = new Manifest(is);
				} finally {
					is.close();
				}
				Attributes attrs = manifest.getMainAttributes();
				String title = attrs.getValue(IMPLEMENTATION_TITLE);
				String symbolicName = attrs.getValue(BUNDLE_SYMBOLIC_NAME);
				if (symbolicName != null) {
					int i = symbolicName.indexOf(';');
					if (i != -1) {
						symbolicName = symbolicName.substring(0, i);
					}
				}
				String vendor = attrs.getValue(IMPLEMENTATION_VENDOR);
				if (vendor == null) {
					vendor = attrs.getValue(BUNDLE_VENDOR);
				}
				String version = attrs.getValue(IMPLEMENTATION_VERSION);
				if (version == null) {
					version = attrs.getValue(BUNDLE_VERSION);
				}
				if (ourLog.isDebugEnabled()) {
					ourLog.debug("FHIR XML procesing will use StAX implementation at {}\n  Title:         {}\n  Symbolic name: {}\n  Vendor:        {}\n  Version:       {}", new Object[] { rootUrl, title, symbolicName, vendor, version } );
				} else {
					ourLog.info("FHIR XML procesing will use StAX implementation '{}' version '{}'", title, version);
				}
			}
		} catch (Throwable e) {
			ourLog.info("Unable to determine StAX implementation: " + e.getMessage());
		}
	}
	
	private static URL getRootUrlForClass(Class<?> cls) {
		ClassLoader classLoader = cls.getClassLoader();
		String resource = cls.getName().replace('.', '/') + ".class";
		if (classLoader == null) {
			// A null class loader means the bootstrap class loader. In this case we use the
			// system class loader. This is safe since we can assume that the system class
			// loader uses parent first as delegation policy.
			classLoader = ClassLoader.getSystemClassLoader();
		}
		URL url = classLoader.getResource(resource);
		if (url == null) {
			return null;
		}
		String file = url.getFile();
		if (file.endsWith(resource)) {
			try {
				return new URL(url.getProtocol(), url.getHost(), url.getPort(), file.substring(0, file.length() - resource.length()));
			} catch (MalformedURLException ex) {
				return null;
			}
		}
		return null;
	}
}
