package org.hl7.fhir.instance.terminologies;

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

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.instance.formats.IParser;
import org.hl7.fhir.instance.model.OperationOutcome;
import org.hl7.fhir.instance.model.Resource;
import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.instance.terminologies.ValueSetExpander;
import org.hl7.fhir.instance.terminologies.ValueSetExpanderFactory;
import org.hl7.fhir.instance.terminologies.ValueSetExpanderSimple;
import org.hl7.fhir.instance.utils.IWorkerContext;
import org.hl7.fhir.instance.utils.ToolingExtensions;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.xhtml.XhtmlComposer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ValueSetExpansionCache implements ValueSetExpanderFactory {

  public class CacheAwareExpander implements ValueSetExpander {

	  @Override
	  public ValueSetExpansionOutcome expand(ValueSet source) throws Exception {
	  	if (expansions.containsKey(source.getUrl()))
	  		return expansions.get(source.getUrl());
	  	ValueSetExpander vse = new ValueSetExpanderSimple(context, ValueSetExpansionCache.this);
	  	ValueSetExpansionOutcome vso = vse.expand(source);
	  	if (vso.getError() != null) {
	  	  // well, we'll see if the designated server can expand it, and if it can, we'll cache it locally
	  		vso = context.expandVS(source, false);
	  		FileOutputStream s = new FileOutputStream(Utilities.path(cacheFolder, makeFile(source.getUrl())));
	  		context.newXmlParser().setOutputStyle(IParser.OutputStyle.PRETTY).compose(s, vso.getValueset());
	  		s.close();
	  	}
	  	expansions.put(source.getUrl(), vso);
	  	return vso;
	  }

    private String makeFile(String url) {
      return url.replace("$", "").replace(":", "").replace("//", "/").replace("/", "_")+".xml";
    }
  }

  private static final String VS_ID_EXT = "http://tools/cache";

  private final Map<String, ValueSetExpander.ValueSetExpansionOutcome> expansions = new HashMap<String, ValueSetExpander.ValueSetExpansionOutcome>();
  private final IWorkerContext context;
  private final String cacheFolder;
	
	public ValueSetExpansionCache(IWorkerContext context) {
    super();
    cacheFolder = null;
    this.context = context;
  }
  
	public ValueSetExpansionCache(IWorkerContext context, String cacheFolder) throws FHIRFormatError, IOException {
    super();
    this.context = context;
    this.cacheFolder = cacheFolder;
    if (this.cacheFolder != null)
      loadCache();
  }
  
	private void loadCache() throws FHIRFormatError, IOException {
	  File[] files = new File(cacheFolder).listFiles();
    for (File f : files) {
      if (f.getName().endsWith(".xml")) {
        final FileInputStream is = new FileInputStream(f);
        try {	   
        Resource r = context.newXmlParser().setOutputStyle(IParser.OutputStyle.PRETTY).parse(is);
        if (r instanceof OperationOutcome) {
          OperationOutcome oo = (OperationOutcome) r;
          expansions.put(ToolingExtensions.getExtension(oo,VS_ID_EXT).getValue().toString(),
            new ValueSetExpander.ValueSetExpansionOutcome(new XhtmlComposer(true, false).composePlainText(oo.getText().getDiv())));
        } else {
          ValueSet vs = (ValueSet) r; 
          expansions.put(vs.getUrl(), new ValueSetExpander.ValueSetExpansionOutcome(vs, null));
        }
        } catch (Exception theE) {
          throw new FHIRFormatError(theE);
        } finally {
          IOUtils.closeQuietly(is);
        }
      }
    }
  }

  @Override
	public ValueSetExpander getExpander() {
		return new CacheAwareExpander();
		// return new ValueSetExpander(valuesets, codesystems);
	}

}
