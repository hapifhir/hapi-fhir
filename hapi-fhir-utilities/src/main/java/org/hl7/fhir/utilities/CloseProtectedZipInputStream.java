package org.hl7.fhir.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

public class CloseProtectedZipInputStream extends ZipInputStream {

  public CloseProtectedZipInputStream(InputStream in) {
    super(in);
  }

  @Override
  public void close() throws IOException {
    // see stack overflow
  }

  public void actualClose() throws IOException {
    super.close();
  }

}
