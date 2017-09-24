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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;

/**
 * Baseclass for readers that read data from files in comma separated file format
 * @author Ewout
 *
 */
public class CSVReader extends InputStreamReader {
	
	public CSVReader(InputStream in) throws UnsupportedEncodingException {
		super(in, "UTF-8");
	}

	private String[] cols;
  private String[] cells;
  
	public void readHeaders() throws IOException, FHIRException {
    cols = parseLine();  
	}
	

  public boolean line() throws IOException, FHIRException {
    if (ready()) {
      cells = parseLine();
      return true;
    }  else
      return false;
  }

  public boolean has(String name) {
    return cell(name) != null;
  }
  
  public String cell(String name) {
    int index = -1;
    for (int i = 0; i < cols.length; i++) {
      if (name.equals(cols[i]))
        index = i;
    }
    if (index == -1)
      throw new Error("no cell "+name);
    String s = cells.length >= index ? cells[index] : null;
    if (Utilities.noString(s))
      return null;
    return s;
  }
    
	protected boolean parseBoolean(String column) {
		if (column == null)
			return false;
		else if (column.equalsIgnoreCase("y") || column.equalsIgnoreCase("yes") || column.equalsIgnoreCase("true") || column.equalsIgnoreCase("1"))
			return true;
		else
			return false;
	}

	protected static String getColumn(String[] titles, String[] values, String column)  {
		int c = -1;
	//	String s = "";
		for (int i = 0; i < titles.length; i++) {
		//	s = s + ","+titles[i];
			if (titles[i].equalsIgnoreCase(column))
				c = i;
		}
		if (c == -1)
			return ""; // throw new Exception("unable to find column "+column+" in "+s.substring(1));
		else if (values.length <= c)
			return "";
		else
			return values[c];
	}

	
	/**
	 * Split one line in a CSV file into its rows. Comma's appearing in double quoted strings will
	 * not be seen as a separator.
	 * @return
	 * @throws IOException 
	 * @throws FHIRException 
	 * @
	 */
	public String[] parseLine() throws IOException, FHIRException  {
		List<String> res = new ArrayList<String>();
		StringBuilder b = new StringBuilder();
		boolean inQuote = false;

		while (inQuote || (peek() != '\r' && peek() != '\n')) {
			char c = peek();
			next();
			if (c == '"') 
				inQuote = !inQuote;
			else if (!inQuote && c == ',') {
				res.add(b.toString().trim());
				b = new StringBuilder();
			}
			else 
				b.append(c);
		}
		res.add(b.toString().trim());
		while (ready() && (peek() == '\r' || peek() == '\n')) {
			next();
		}
		
		String[] r = new String[] {};
		r = res.toArray(r);
		return r;
		
	}

	private int state = 0;
	private char pc;
	
	private char peek() throws FHIRException, IOException 
	{
	  if (state == 0)
		  next();
	  if (state == 1)
		  return pc;
	  else
		  throw new FHIRException("read past end of source");
	}
	
	private void next() throws FHIRException, IOException 
	{
		  if (state == 2)
			  throw new FHIRException("read past end of source");
          state = 1;
		  int i = read();
		  if (i == -1)
			  state = 2;
		  else 
			  pc = (char) i;
	}


}
