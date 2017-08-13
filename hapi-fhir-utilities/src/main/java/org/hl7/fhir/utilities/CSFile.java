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

import java.io.File;
import java.io.IOException;

/**
 * Extends the usual Java File to make it case-sensitive: if the file to be opened has
 * a different casing in the filesystem (but is found anyway base Windows is case-insensitive), 
 * the constructor will throw. Allows early detection of miscased filenames.
 * @author Ewout
 *
 */
public class CSFile extends File {
  private static final long serialVersionUID = -2017155765132460726L;

  public CSFile(String pathname) throws IOException {	  
    super(pathname);
    
//    if (exists() && getParent() != null) 
//    {
//      String n = getName();
//      File f = new File(getParent());
//      String[] l = f.list();
//      boolean ok = false;
//      for (String n1 : l) {
//        if (n1.equals(n))
//          ok = true;
//      }
//      if (!ok)
//        throw new Error("Case mismatch of file "+ pathname);
//    }
    
    //EK: Original code above looked extremely wasteful: every single
    //attempt to open a file triggers a directory listing
    if (exists()) 
    {
    	if(!this.getCanonicalFile().getName().equals(this.getName()))
    		throw new Error("Case mismatch of file "+ pathname+": found "+this.getCanonicalFile().getName());
    }
  }

  
}
