package org.hl7.fhir.r4.formats;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.List;

import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.Element;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Type;
import org.hl7.fhir.r4.utils.formats.JsonTrackingParser;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.xhtml.XhtmlComposer;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.hl7.fhir.utilities.xhtml.XhtmlParser;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
/**
 * General parser for JSON content. You instantiate an JsonParser of these, but you 
 * actually use parse or parseGeneral defined on this class
 * 
 * The two classes are separated to keep generated and manually maintained code apart.
 */
public abstract class JsonParserBase extends ParserBase implements IParser {
	
  @Override
  public ParserType getType() {
	  return ParserType.JSON;
  }

	// private static com.google.gson.JsonParser  parser = new com.google.gson.JsonParser();
  
  // -- in descendent generated code --------------------------------------
  
  abstract protected Resource parseResource(JsonObject json) throws IOException, FHIRFormatError;
  abstract protected Type parseType(JsonObject json, String type) throws IOException, FHIRFormatError;
  abstract protected Type parseAnyType(JsonObject json, String type) throws IOException, FHIRFormatError;
  abstract protected Type parseType(String prefix, JsonObject json) throws IOException, FHIRFormatError;
  abstract protected boolean hasTypeName(JsonObject json, String prefix);
  abstract protected void composeResource(Resource resource) throws IOException;
  abstract protected void composeTypeInner(Type type) throws IOException;

  /* -- entry points --------------------------------------------------- */

  /**
   * @throws FHIRFormatError 
   * Parse content that is known to be a resource
   * @throws IOException 
   * @throws  
   */
  @Override
  public Resource parse(InputStream input) throws IOException, FHIRFormatError {
    JsonObject json = loadJson(input);
    return parseResource(json);
  }

  /**
   * parse xml that is known to be a resource, and that has already been read into a JSON object  
   * @throws IOException 
   * @throws FHIRFormatError 
   */
  public Resource parse(JsonObject json) throws FHIRFormatError, IOException {
    return parseResource(json);
  }

  @Override
  public Type parseType(InputStream input, String type) throws IOException, FHIRFormatError {
    JsonObject json = loadJson(input);
    return parseType(json, type);
  }

  @Override
  public Type parseAnyType(InputStream input, String type) throws IOException, FHIRFormatError {
    JsonObject json = loadJson(input);
    return parseAnyType(json, type);
  }

  /**
   * Compose a resource to a stream, possibly using pretty presentation for a human reader (used in the spec, for example, but not normally in production)
   * @throws IOException 
   */
  @Override
  public void compose(OutputStream stream, Resource resource) throws IOException {
    OutputStreamWriter osw = new OutputStreamWriter(stream, "UTF-8");
    if (style == OutputStyle.CANONICAL)
      json = new JsonCreatorCanonical(osw);
    else
      json = new JsonCreatorDirect(osw); // use this instead of Gson because this preserves decimal formatting
    json.setIndent(style == OutputStyle.PRETTY ? "  " : "");
    json.beginObject();
    composeResource(resource);
    json.endObject();
    json.finish();
    osw.flush();
  }

  /**
   * Compose a resource using a pre-existing JsonWriter
   * @throws IOException 
   */
  public void compose(JsonCreator writer, Resource resource) throws IOException {
    json = writer;
    composeResource(resource);
  }
  
  @Override
  public void compose(OutputStream stream, Type type, String rootName) throws IOException {
    OutputStreamWriter osw = new OutputStreamWriter(stream, "UTF-8");
    if (style == OutputStyle.CANONICAL)
      json = new JsonCreatorCanonical(osw);
    else
      json = new JsonCreatorDirect(osw);// use this instead of Gson because this preserves decimal formatting
    json.setIndent(style == OutputStyle.PRETTY ? "  " : "");
    json.beginObject();
    composeTypeInner(type);
    json.endObject();
    json.finish();
    osw.flush();
  }
    

  
  /* -- json routines --------------------------------------------------- */

  protected JsonCreator json;
  private boolean htmlPretty;
  
  private JsonObject loadJson(InputStream input) throws JsonSyntaxException, IOException {
    return JsonTrackingParser.parse(TextFile.streamToString(input), null);
    // return parser.parse(TextFile.streamToString(input)).getAsJsonObject();
  }
  
//  private JsonObject loadJson(String input) {
//    return parser.parse(input).getAsJsonObject();
//  }
//  
  protected void parseElementProperties(JsonObject json, Element e) throws IOException, FHIRFormatError {
    if (json != null && json.has("id"))
      e.setId(json.get("id").getAsString());
    if (!Utilities.noString(e.getId()))
      idMap.put(e.getId(), e);
    if (json.has("fhir_comments") && handleComments) {
      JsonArray array = json.getAsJsonArray("fhir_comments");
      for (int i = 0; i < array.size(); i++) {
        e.getFormatCommentsPre().add(array.get(i).getAsString());
      }
    }
  }
  
  protected XhtmlNode parseXhtml(String value) throws IOException, FHIRFormatError {
    XhtmlParser prsr = new XhtmlParser();
    try {
		return prsr.parse(value, "div").getChildNodes().get(0);
	} catch (org.hl7.fhir.exceptions.FHIRFormatError e) {
		throw new FHIRFormatError(e.getMessage(), e);
	}
  }
  
  protected DomainResource parseDomainResource(JsonObject json) throws FHIRFormatError, IOException {
	  return (DomainResource) parseResource(json);
  }

	protected void writeNull(String name) throws IOException {
		json.nullValue();
	}
	protected void prop(String name, String value) throws IOException {
		if (name != null)
			json.name(name);
		json.value(value);
	}

  protected void prop(String name, java.lang.Boolean value) throws IOException {
    if (name != null)
      json.name(name);
    json.value(value);
  }

  protected void prop(String name, BigDecimal value) throws IOException {
    if (name != null)
      json.name(name);
    json.value(value);
  }

  protected void propNum(String name, String value) throws IOException {
    if (name != null)
      json.name(name);
    json.valueNum(value);
  }

  protected void prop(String name, java.lang.Integer value) throws IOException {
    if (name != null)
      json.name(name);
    json.value(value);
  }

	protected void composeXhtml(String name, XhtmlNode html) throws IOException {
		if (!Utilities.noString(xhtmlMessage)) {
      prop(name, "<div>!-- "+xhtmlMessage+" --></div>");
		} else {
		XhtmlComposer comp = new XhtmlComposer(XhtmlComposer.XML, htmlPretty);
		prop(name, comp.compose(html));
		}
	}

	protected void open(String name) throws IOException {
		if (name != null) 
			json.name(name);
		json.beginObject();
	}

	protected void close() throws IOException {
		json.endObject();
	}

	protected void openArray(String name) throws IOException {
		if (name != null) 
			json.name(name);
		json.beginArray();
	}

	protected void closeArray() throws IOException {
		json.endArray();
	}

	protected void openObject(String name) throws IOException {
		if (name != null) 
			json.name(name);
		json.beginObject();
	}

	protected void closeObject() throws IOException {
		json.endObject();
	}

//  protected void composeBinary(String name, Binary element) {
//    if (element != null) {
//      prop("resourceType", "Binary");
//      if (element.getXmlId() != null)
//        prop("id", element.getXmlId());
//      prop("contentType", element.getContentType());
//      prop("content", toString(element.getContent()));
//    }    
//    
//  }

  protected boolean anyHasExtras(List<? extends Element> list) {
	  for (Element e : list) {
	  	if (e.hasExtension() || !Utilities.noString(e.getId()))
	  		return true;
	  }
	  return false;
  }

	protected boolean makeComments(Element element) {
		return handleComments && (style != OutputStyle.CANONICAL) && !(element.getFormatCommentsPre().isEmpty() && element.getFormatCommentsPost().isEmpty());
	}
	
  protected void composeDomainResource(String name, DomainResource e) throws IOException {
	  openObject(name);
	  composeResource(e);
	  close();
	  
  }

  protected abstract void composeType(String prefix, Type type) throws IOException;

  
  abstract void composeStringCore(String name, StringType value, boolean inArray) throws IOException;

  protected void composeStringCore(String name, IIdType value, boolean inArray) throws IOException {
	  composeStringCore(name, new StringType(value.getValue()), inArray);
  }    

  abstract void composeStringExtras(String name, StringType value, boolean inArray) throws IOException;

  protected void composeStringExtras(String name, IIdType value, boolean inArray) throws IOException {
	  composeStringExtras(name, new StringType(value.getValue()), inArray);
  }    
  
  protected void parseElementProperties(JsonObject theAsJsonObject, IIdType theReferenceElement) throws FHIRFormatError, IOException {
	  parseElementProperties(theAsJsonObject, (Element)theReferenceElement);
  }

  protected void parseElementProperties(JsonObject theAsJsonObject, IdType theReferenceElement) throws FHIRFormatError, IOException {
	  parseElementProperties(theAsJsonObject, (Element)theReferenceElement);
  }

}
