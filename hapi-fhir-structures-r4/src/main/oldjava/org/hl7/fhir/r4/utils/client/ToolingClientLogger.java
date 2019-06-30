package org.hl7.fhir.r4.utils.client;

import java.util.List;

public interface ToolingClientLogger {

  public void logRequest(String method, String url, List<String> headers, byte[] body);
  public void logResponse(String outcome, List<String> headers, byte[] body);
}
