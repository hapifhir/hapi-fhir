/*
Copyright (c) 2011+, HL7, Inc
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this 
   list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, 
   this list of conditions and the following disclaimer in the documentation 
   and/or other materials provided with the distribution.
 * Neither the name of HL7 nor the names of its contributors may be used to 
   endorse or promote products derived from this software without specific 
   prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
POSSIBILITY OF SUCH DAMAGE.

 */
package org.hl7.fhir.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipGenerator {

  private Set<String> names = new HashSet<String>();
	FileOutputStream dest;
	ZipOutputStream out;

	public ZipGenerator(String filename) throws FileNotFoundException  {
		dest = new FileOutputStream(filename);
		out = new ZipOutputStream(new BufferedOutputStream(dest));
    out.setLevel(Deflater.BEST_COMPRESSION);
	}

	public void close() throws IOException  {
		out.close();
	}

	static final int BUFFER = 2048;

	public void addFromZip(String zipFilename) throws IOException {
		byte[] buf = new byte[1024];

		ZipInputStream zin = new ZipInputStream(
				new FileInputStream(zipFilename));

		try {
			ZipEntry entry = zin.getNextEntry();
			while (entry != null) {
				String name = entry.getName();

				names.add(name);
				// Add ZIP entry to output stream.
				out.putNextEntry(new ZipEntry(name));
				// Transfer bytes from the ZIP file to the output file
				int len;
				while ((len = zin.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				entry = zin.getNextEntry();
			}
		} finally {
			zin.close();
		}
	}

	public void addFolder(String actualDir, String statedDir, boolean omitIfExists) throws IOException  {
		File fd = new CSFile(actualDir);
		String files[] = fd.list();
		for (String f : files) {
			if (new CSFile(Utilities.path(actualDir, f)).isDirectory())
				addFolder(Utilities.path(actualDir, f), Utilities.pathURL(statedDir, f), omitIfExists);
			else
				addFileName(Utilities.pathURL(statedDir, f), Utilities.path(actualDir, f), omitIfExists);
		}
	}

  public void addFolder(String actualDir, String statedDir, boolean omitIfExists, String noExt) throws IOException  {
    File fd = new CSFile(actualDir);
    String files[] = fd.list();
    for (String f : files) {
      if (new CSFile(Utilities.path(actualDir, f)).isDirectory())
        addFolder(Utilities.path(actualDir, f), Utilities.pathURL(statedDir, f), omitIfExists, noExt);
      else if (noExt == null || !f.endsWith(noExt))
        addFileName(Utilities.pathURL(statedDir, f), Utilities.path(actualDir, f), omitIfExists);
    }
  }

	public void addFiles(String actualDir, String statedDir, String ext, String noExt) throws FileNotFoundException, IOException {
		byte data[] = new byte[BUFFER];
		statedDir = statedDir.replace("\\", "/");
		File f = new CSFile(actualDir);

		String files[] = f.list();
		for (int i = 0; i < files.length; i++) {
			if ( new CSFile(actualDir + files[i]).isFile() && ((ext == null || files[i].endsWith(ext)) && (noExt == null || !files[i].endsWith(noExt)))) {
				FileInputStream fi = new FileInputStream(actualDir + files[i]);
				BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
				ZipEntry entry = new ZipEntry(statedDir + files[i]);
				names.add(statedDir + files[i]);
				out.putNextEntry(entry);
				int count;
				while ((count = origin.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				}
				origin.close();
			}
		}
	}

  public void addFilesFiltered(String actualDir, String statedDir, String ext, String[] noExt) throws FileNotFoundException, IOException {
    byte data[] = new byte[BUFFER];
    statedDir = statedDir.replace("\\", "/");
    File f = new CSFile(actualDir);

    String files[] = f.list();
    for (int i = 0; i < files.length; i++) {
      if ( new CSFile(actualDir + files[i]).isFile() && ((ext == null || files[i].endsWith(ext)))) {
        boolean ok = true;
        for (String n : noExt) {
          ok = ok && !files[i].endsWith(n);
        }
        if (ok) {
          FileInputStream fi = new FileInputStream(actualDir + files[i]);
          BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
          ZipEntry entry = new ZipEntry(statedDir + files[i]);
          names.add(statedDir + files[i]);
          out.putNextEntry(entry);
          int count;
          while ((count = origin.read(data, 0, BUFFER)) != -1) {
            out.write(data, 0, count);
          }
          origin.close();
        }
      }
    }
  }

	public void addFileSource(String path, String cnt, boolean omitIfExists) throws IOException  {
		File tmp = Utilities.createTempFile("tmp", ".tmp");
		TextFile.stringToFile(cnt, tmp.getAbsolutePath());
		addFileName(path, tmp.getAbsolutePath(), omitIfExists);
		tmp.delete();
	}

	public void addFileName(String statedPath, String actualPath, boolean omitIfExists) throws IOException  {
	  if (!omitIfExists || !names.contains(statedPath)) {
	    byte data[] = new byte[BUFFER];
	    FileInputStream fi = new FileInputStream(actualPath);
	    BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
	    ZipEntry entry = new ZipEntry(statedPath);
	    names.add(statedPath);
	    out.putNextEntry(entry);
	    int count;
	    while ((count = origin.read(data, 0, BUFFER)) != -1) {
	      out.write(data, 0, count);
	    }
	    origin.close();
	  }
	}

  public void addBytes(String statedPath, byte[] content, boolean omitIfExists) throws IOException  {
    if (!omitIfExists || !names.contains(statedPath)) {
      byte data[] = new byte[BUFFER];
      InputStream fi = new ByteArrayInputStream(content);
      BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
      ZipEntry entry = new ZipEntry(statedPath);
      names.add(statedPath);
      out.putNextEntry(entry);
      int count;
      while ((count = origin.read(data, 0, BUFFER)) != -1) {
        out.write(data, 0, count);
      }
      origin.close();
    }
  }

  public void addMimeTypeFile(String statedPath, String actualPath) throws IOException  {
  //  byte data[] = new byte[BUFFER];
    CRC32 crc = new CRC32();
    
  //  FileInputStream fi = new FileInputStream(actualPath);
  //  BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
    out.setLevel(0);
    ZipEntry entry = new ZipEntry(statedPath);
    entry.setExtra(null);
    names.add(statedPath);
    String contents = "application/epub+zip";
    crc.update(contents.getBytes());
    entry.setCompressedSize(contents.length());
    entry.setSize(contents.length());
    entry.setCrc(crc.getValue());
    entry.setMethod(ZipEntry.STORED);
    out.putNextEntry(entry);
 //   int count;
//    while ((count = origin.read(data, 0, BUFFER)) != -1) {
//      out.write(data, 0, count);
//    }
  //  origin.close();
    out.write(contents.getBytes(),0,contents.length());
    out.setLevel(Deflater.BEST_COMPRESSION);
  }

  public void addStream(String statedPath, InputStream fi, boolean omitIfExists) throws IOException  {
    if (!omitIfExists || !names.contains(statedPath)) {
      byte data[] = new byte[BUFFER];
      BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
      ZipEntry entry = new ZipEntry(statedPath);
      names.add(statedPath);
      out.putNextEntry(entry);
      int count;
      while ((count = origin.read(data, 0, BUFFER)) != -1) {
        out.write(data, 0, count);
      }
      origin.close();
    }
  }

}
