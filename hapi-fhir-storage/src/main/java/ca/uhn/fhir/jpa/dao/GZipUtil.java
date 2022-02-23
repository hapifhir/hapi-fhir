package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR Storage api
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
import ca.uhn.fhir.parser.DataFormatException;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZipUtil {

	public static String decompress(byte[] theResource) {
		GZIPInputStream is;
		try {
			is = new GZIPInputStream(new ByteArrayInputStream(theResource));
			return IOUtils.toString(is, "UTF-8");
		} catch (IOException e) {
			throw new DataFormatException(Msg.code(516) + "Failed to decompress contents", e);
		}
	}

	public static byte[] compress(String theEncoded) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			GZIPOutputStream gos = new GZIPOutputStream(os);
			IOUtils.write(theEncoded, gos, "UTF-8");
			gos.close();
			os.close();
			byte[] retVal = os.toByteArray();
			return retVal;
		} catch (IOException e) {
			throw new DataFormatException(Msg.code(517) + "Compress contents", e);
		}
	}

}
