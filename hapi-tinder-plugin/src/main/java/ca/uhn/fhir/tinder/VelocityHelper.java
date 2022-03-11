package ca.uhn.fhir.tinder;
/*
 * #%L
 * HAPI FHIR Tinder Plug-In
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

public class VelocityHelper {

	public static VelocityEngine configureVelocityEngine (File templateFile, String velocityPath, String propertyFile) throws IOException {
		VelocityEngine result = new VelocityEngine();
		boolean haveResourceLoader = false;
		boolean haveRuntimeReferences = false;
		
		if (propertyFile != null) {
			File propFile = new File(propertyFile);
			if (propFile.exists() && propFile.isFile() && propFile.canRead()) {
				InputStream propsIn = new FileInputStream(propFile);
				Properties props = new Properties();
				props.load(propsIn);
				propsIn.close();
				for (Entry<?,?> entry : props.entrySet()) {
					String key = (String)entry.getKey();
					result.setProperty(key, entry.getValue());
					if (RuntimeConstants.RESOURCE_LOADER.equals(key)) {
						haveResourceLoader = true;
					} else
					if (RuntimeConstants.RUNTIME_REFERENCES_STRICT.equals(key)) {
						haveRuntimeReferences = true;
					}
				}
			} else {
				throw new FileNotFoundException(Msg.code(94) + "Velocity property file ["+propertyFile+"] does not exist or is not readable.");
			}
		}
		
		if (!haveResourceLoader) {
			if (templateFile != null) {
				result.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
				result.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
				if (velocityPath != null) {
					result.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, velocityPath);
				} else {
					String path = templateFile.getCanonicalFile().getParent();
					if (null == path) {
						path = ".";
					}
					result.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, path);
				}
			} else {
				result.setProperty(RuntimeConstants.RESOURCE_LOADERS, "cp");
				result.setProperty("resource.loader.cp.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			}
		}
		
		if (!haveRuntimeReferences) {
			result.setProperty(RuntimeConstants.RUNTIME_REFERENCES_STRICT, Boolean.TRUE);
		}
		
		return result;
	}
}
