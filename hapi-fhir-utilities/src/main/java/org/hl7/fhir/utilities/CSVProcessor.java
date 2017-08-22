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


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.hl7.fhir.exceptions.FHIRException;

/**
 * A file processor that reads a templated source file with markers ([%columnname%]), reads data
 * from a CSV file and inserts data from that CSV file into those markers. Supports loops to
 * interate through the CSV file.
 * @author Ewout
 *
 */
public class CSVProcessor {

  public class DataReader extends CSVReader {

    public DataReader(InputStream data) throws UnsupportedEncodingException {
      super(data);
    }

    public void process() throws IOException, FHIRException  {
      String[] titles = parseLine();
      while (ready())
      {
        String[] values = parseLine();
        processLine(titles, values);
      }     
      close();
    }

    private void processLine(String[] titles, String[] values) throws FHIRException  {
      count++;
      String src = loop;
      while (src.contains("[%")) {
        int i1 = src.indexOf("[%");
        int i2 = src.indexOf("%]");
        String s1 = src.substring(0, i1);
        String s2 = src.substring(i1 + 2, i2).trim();
        String s3 = src.substring(i2+2);
        if ("count".equals(s2))
          src = s1+Integer.toString(count)+s3;
        else {
          boolean b = false;
          for (String t : titles) {
            if (t.equals(s2)) {
              src = s1+getColumn(titles, values, s2)+s3;
              b = true;
            }
          }
          if (!b)
            throw new FHIRException("unknown column: '"+s2+"'");
        }
      }
      dest.append(src);
    }
  }

  private InputStream source;
  private DataReader data;
  private OutputStreamWriter out;

  private String start;
  private String loop;
  private int count = 0;
  private String stop;
  private StringBuilder dest;
  
  public void setSource(InputStream source) {
    this.source = source;
  }

  public void setData(InputStream data) {
    try {
      this.data = new DataReader(data);
    } catch (UnsupportedEncodingException e) {
      // DataReader is fixed to "UTF-8", so this exception cannot really occur
    }   
  }

  public void setOutput(OutputStream out) throws UnsupportedEncodingException {
    this.out = new OutputStreamWriter(out, "UTF-8");   
  }

  public void process() throws IOException, FHIRException  {
    buildTemplate(readSource());
    dest = new StringBuilder();
    dest.append(start);
    data.process();
    dest.append(stop);
    out.write(dest.toString());
    out.close();
  }

  private void buildTemplate(String template) throws FHIRException  {
    int i = template.indexOf("[%loop");
    if (i < 0)
      throw new FHIRException("Unable to process template - didn't find [%loop");
    start = template.substring(0, i);
    template = template.substring(i+6);
    i = template.indexOf("%]");
    if (i < 0)
      throw new FHIRException("Unable to process template - didn't find %] matching [%loop");
    String tmp = template.substring(0, i);
    if (tmp != null && !tmp.equals("")) {
      if (!tmp.startsWith(" count="))
        throw new FHIRException("Unable to process template - unrecognised content on [%loop");
      count = Integer.parseInt(tmp.substring(7));
    }
    
    template = template.substring(i+2);
    i = template.indexOf("[%endloop%]");
    if (i < 0)
      throw new FHIRException("Unable to process template - didn't find [%endloop%]");
    loop = template.substring(0, i);
    stop = template.substring(i+11);
  }

  private String readSource() throws IOException  {
    StringBuilder s = new StringBuilder();
    InputStreamReader r = new InputStreamReader(source,"UTF-8");
    while (r.ready()) {
      s.append((char) r.read()); 
    }
    r.close();
    return s.toString();
  }

}
