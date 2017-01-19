package org.hl7.fhir.dstu2016may.metamodel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hl7.fhir.dstu2016may.formats.IParser.OutputStyle;
import org.hl7.fhir.dstu2016may.formats.JsonCreator;
import org.hl7.fhir.dstu2016may.formats.JsonCreatorCanonical;
import org.hl7.fhir.dstu2016may.formats.JsonCreatorGson;
import org.hl7.fhir.dstu2016may.metamodel.Element.SpecialElement;
import org.hl7.fhir.dstu2016may.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.dstu2016may.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.dstu2016may.model.OperationOutcome.IssueType;
import org.hl7.fhir.dstu2016may.model.StructureDefinition;
import org.hl7.fhir.dstu2016may.utils.IWorkerContext;
import org.hl7.fhir.dstu2016may.utils.JsonTrackingParser;
import org.hl7.fhir.dstu2016may.utils.JsonTrackingParser.LocationData;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.xhtml.XhtmlParser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonParser extends ParserBase {

	private JsonCreator json;
	private Map<JsonElement, LocationData> map;

	public JsonParser(IWorkerContext context) {
		super(context);
	}

	@Override
	public Element parse(InputStream stream) throws Exception {
		// if we're parsing at this point, then we're going to use the custom parser
		map = new HashMap<JsonElement, LocationData>();
		String source = TextFile.streamToString(stream);
		if (policy == ValidationPolicy.EVERYTHING) {
			JsonObject obj = null; 
      try {
			  obj = JsonTrackingParser.parse(source, map);
      } catch (Exception e) {  
				logError(-1, -1, "(document)", IssueType.INVALID, "Error parsing JSON: "+e.getMessage(), IssueSeverity.FATAL);
      	return null;
      }
		  assert (map.containsKey(obj));
			return parse(obj);	
		} else {
			JsonObject obj = (JsonObject) new com.google.gson.JsonParser().parse(source);
			assert (map.containsKey(obj));
			return parse(obj);	
		} 
	}

	public Element parse(JsonObject object, Map<JsonElement, LocationData> map) throws Exception {
		this.map = map;
		return parse(object);
	}

  public Element parse(JsonObject object) throws Exception {
		JsonElement rt = object.get("resourceType");
		if (rt == null) {
			logError(line(object), col(object), "$", IssueType.INVALID, "Unable to find resourceType property", IssueSeverity.FATAL);
			return null;
		} else {
			String name = rt.getAsString();
			String path = "/"+name;

			StructureDefinition sd = getDefinition(line(object), col(object), name);
			if (sd == null)
				return null;

			Element result = new Element(name, new Property(context, sd.getSnapshot().getElement().get(0), sd));
			checkObject(object, path);
			result.markLocation(line(object), col(object));
			result.setType(name);
			parseChildren(path, object, result, true);
			result.numberChildren();
			return result;
		}
	}

	private void checkObject(JsonObject object, String path) throws FHIRFormatError {
		if (policy == ValidationPolicy.EVERYTHING) {
			boolean found = false;
			for (Entry<String, JsonElement> e : object.entrySet()) {
				//    		if (!e.getKey().equals("fhir_comments")) {
				found = true;
				break;
				//    		}
			}
			if (!found)
				logError(line(object), col(object), path, IssueType.INVALID, "Object must have some content", IssueSeverity.ERROR);
		}
	}

	private void parseChildren(String path, JsonObject object, Element context, boolean hasResourceType) throws DefinitionException, FHIRFormatError {
		reapComments(object, context);
		List<Property> properties = getChildProperties(context.getProperty(), context.getName(), null);
		Set<String> processed = new HashSet<String>();
		if (hasResourceType)
			processed.add("resourceType");
		processed.add("fhir_comments");

		// note that we do not trouble ourselves to maintain the wire format order here - we don't even know what it was anyway
		// first pass: process the properties
		for (Property property : properties) {
			if (property.isChoice()) {
				for (TypeRefComponent type : property.getDefinition().getType()) {
					String eName = property.getName().substring(0, property.getName().length()-3) + Utilities.capitalize(type.getCode());
					if (!ParserBase.isPrimitive(type.getCode()) && object.has(eName)) {
						parseChildComplex(path, object, context, processed, property, eName);
						break;
					} else if (ParserBase.isPrimitive(type.getCode()) && (object.has(eName) || object.has("_"+eName))) {
						parseChildPrimitive(object, context, processed, property, path, eName);
						break;
					}
				}
			} else if (property.isPrimitive(null)) {
				parseChildPrimitive(object, context, processed, property, path, property.getName());
			} else if (object.has(property.getName())) {
				parseChildComplex(path, object, context, processed, property, property.getName());
			}
		}

		// second pass: check for things not processed
		if (policy != ValidationPolicy.NONE) {
			for (Entry<String, JsonElement> e : object.entrySet()) {
				if (!processed.contains(e.getKey())) {
					logError(line(e.getValue()), col(e.getValue()), path, IssueType.STRUCTURE, "Unrecognised property '@"+e.getKey()+"'", IssueSeverity.ERROR);      		
				}
			}
		}
	}

	private void parseChildComplex(String path, JsonObject object, Element context, Set<String> processed, Property property, String name) throws FHIRFormatError, DefinitionException {
		processed.add(name);
		String npath = path+"/"+property.getName();
		JsonElement e = object.get(name);
		if (property.isList() && (e instanceof JsonArray)) {
			JsonArray arr = (JsonArray) e;
			for (JsonElement am : arr) {
				parseChildComplexInstance(npath, object, context, property, name, am);
			}
		} else {
			parseChildComplexInstance(npath, object, context, property, name, e);
		}
	}

	private void parseChildComplexInstance(String npath, JsonObject object, Element context, Property property, String name, JsonElement e) throws FHIRFormatError, DefinitionException {
		if (e instanceof JsonObject) {
			JsonObject child = (JsonObject) e;
			Element n = new Element(name, property).markLocation(line(child), col(child));
			checkObject(child, npath);
			context.getChildren().add(n);
			if (property.isResource())
				parseResource(npath, child, n);
			else
				parseChildren(npath, child, n, false);
		} else 
			logError(line(e), col(e), npath, IssueType.INVALID, "This property must be "+(property.isList() ? "an Array" : "an Object")+", not a "+e.getClass().getName(), IssueSeverity.ERROR);
	}
	
	private void parseChildPrimitive(JsonObject object, Element context, Set<String> processed, Property property, String path, String name) throws FHIRFormatError, DefinitionException {
		String npath = path+"/"+property.getName();
		processed.add(name);
		processed.add("_"+name);
		JsonElement main = object.has(name) ? object.get(name) : null; 
		JsonElement fork = object.has("_"+name) ? object.get("_"+name) : null;
		if (main != null || fork != null) {
			if (property.isList() && ((main == null) || (main instanceof JsonArray)) &&((fork == null) || (fork instanceof JsonArray)) ) {
				JsonArray arr1 = (JsonArray) main;
				JsonArray arr2 = (JsonArray) fork;
				for (int i = 0; i < Math.max(arrC(arr1), arrC(arr2)); i++) {
					JsonElement m = arrI(arr1, i);
					JsonElement f = arrI(arr2, i);
					parseChildPrimitiveInstance(context, property, name, npath, m, f);
				}
			} else
				parseChildPrimitiveInstance(context, property, name, npath, main, fork);
		}
	}

	private JsonElement arrI(JsonArray arr, int i) {
  	return arr == null || i >= arr.size() || arr.get(i) instanceof JsonNull ? null : arr.get(i);
	}

	private int arrC(JsonArray arr) {
  	return arr == null ? 0 : arr.size();
	}

	private void parseChildPrimitiveInstance(Element context, Property property, String name, String npath,
	    JsonElement main, JsonElement fork) throws FHIRFormatError, DefinitionException {
	if (main != null && !(main instanceof JsonPrimitive))
		logError(line(main), col(main), npath, IssueType.INVALID, "This property must be an simple value, not a "+main.getClass().getName(), IssueSeverity.ERROR);
	else if (fork != null && !(fork instanceof JsonObject))
		logError(line(fork), col(fork), npath, IssueType.INVALID, "This property must be an object, not a "+fork.getClass().getName(), IssueSeverity.ERROR);
	else {
		Element n = new Element(name, property).markLocation(line(main != null ? main : fork), col(main != null ? main : fork));
		context.getChildren().add(n);
		if (main != null) {
			JsonPrimitive p = (JsonPrimitive) main;
			n.setValue(p.getAsString());
			if (!n.getProperty().isChoice() && n.getType().equals("xhtml")) {
				try {
	    	  n.setXhtml(new XhtmlParser().setValidatorMode(policy == ValidationPolicy.EVERYTHING).parse(n.getValue(), null).getDocumentElement());
				} catch (Exception e) {
					logError(line(main), col(main), npath, IssueType.INVALID, "Error parsing XHTML: "+e.getMessage(), IssueSeverity.ERROR);
				}
			}
			if (policy == ValidationPolicy.EVERYTHING) {
				// now we cross-check the primitive format against the stated type
				if (Utilities.existsInList(n.getType(), "boolean")) {
					if (!p.isBoolean())
						logError(line(main), col(main), npath, IssueType.INVALID, "Error parsing JSON: the primitive value must be a boolean", IssueSeverity.ERROR);
				} else if (Utilities.existsInList(n.getType(), "integer", "unsignedInt", "positiveInt", "decimal")) {
					if (!p.isNumber())
						logError(line(main), col(main), npath, IssueType.INVALID, "Error parsing JSON: the primitive value must be a number", IssueSeverity.ERROR);
				} else if (!p.isString())
				  logError(line(main), col(main), npath, IssueType.INVALID, "Error parsing JSON: the primitive value must be a string", IssueSeverity.ERROR);
			}
		}
		if (fork != null) {
			JsonObject child = (JsonObject) fork;
			checkObject(child, npath);
			parseChildren(npath, child, n, false);
		}
	}
	}


	private void parseResource(String npath, JsonObject res, Element parent) throws DefinitionException, FHIRFormatError {
		JsonElement rt = res.get("resourceType");
		if (rt == null) {
			logError(line(res), col(res), npath, IssueType.INVALID, "Unable to find resourceType property", IssueSeverity.FATAL);
		} else {
			String name = rt.getAsString();
			StructureDefinition sd = context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+name);
			if (sd == null)
				throw new FHIRFormatError("Contained resource does not appear to be a FHIR resource (unknown name '"+name+"')");
			parent.updateProperty(new Property(context, sd.getSnapshot().getElement().get(0), sd), parent.getProperty().getName().equals("contained") ? SpecialElement.CONTAINED : SpecialElement.BUNDLE_ENTRY);
			parent.setType(name);
			parseChildren(npath, res, parent, true);
		}
	}

	private void reapComments(JsonObject object, Element context) {
		if (object.has("fhir_comments")) {
			JsonArray arr = object.getAsJsonArray("fhir_comments");
			for (JsonElement e : arr) {
				context.getComments().add(e.getAsString());
			}
		}
	}

	private int line(JsonElement e) {
		if (map == null|| !map.containsKey(e))
			return -1;
		else
			return map.get(e).getLine();
	}

	private int col(JsonElement e) {
		if (map == null|| !map.containsKey(e))
			return -1;
		else
			return map.get(e).getCol();
  }


	protected void prop(String name, String value) throws IOException {
		if (name != null)
			json.name(name);
		json.value(value);
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


	@Override
	public void compose(Element e, OutputStream stream, OutputStyle style, String identity) throws Exception {
		OutputStreamWriter osw = new OutputStreamWriter(stream, "UTF-8");
		if (style == OutputStyle.CANONICAL)
			json = new JsonCreatorCanonical(osw);
		else
			json = new JsonCreatorGson(osw);
		json.setIndent(style == OutputStyle.PRETTY ? "  " : "");
		json.beginObject();
		prop("resourceType", e.getType());
		Set<String> done = new HashSet<String>();
		for (Element child : e.getChildren()) {
			compose(e.getName(), e, done, child);
		}
		json.endObject();
		json.finish();
		osw.flush();
	}

	private void compose(String path, Element e, Set<String> done, Element child) throws IOException {
		if (child.getSpecial() == SpecialElement.BUNDLE_ENTRY || !child.getProperty().isList()) {// for specials, ignore the cardinality of the stated type
			compose(path, child);
		} else if (!done.contains(child.getName())) {
			done.add(child.getName());
			List<Element> list = e.getChildrenByName(child.getName());
			composeList(path, list);
		}
	}

	private void composeList(String path, List<Element> list) throws IOException {
		// there will be at least one element
		String name = list.get(0).getName();
		boolean complex = true;
		if (list.get(0).isPrimitive()) {
			boolean prim = false;
			complex = false;
			for (Element item : list) { 
				if (item.hasValue())
					prim = true;
				if (item.hasChildren())
					complex = true;
			}
			if (prim) {
				openArray(name);
				for (Element item : list) { 
					if (item.hasValue())
						primitiveValue(null, item);
					else
						json.nullValue();
				}				
				closeArray();
			}
			name = "_"+name;
		}
		if (complex) {
			openArray(name);
			for (Element item : list) { 
				if (item.hasChildren()) {
					open(null);
					if (item.getProperty().isResource()) {
						prop("resourceType", item.getType());
					}
					Set<String> done = new HashSet<String>();
					for (Element child : item.getChildren()) {
						compose(path+"."+name+"[]", item, done, child);
					}
					close();
				} else
					json.nullValue();
			}				
			closeArray();
		}		
	}

	private void primitiveValue(String name, Element item) throws IOException {
		if (name != null)
			json.name(name);
	  String type = item.getType();
	  if (Utilities.existsInList(type, "boolean"))
	  	json.value(item.getValue().trim().equals("true") ? new Boolean(true) : new Boolean(false));
	  else if (Utilities.existsInList(type, "integer", "unsignedInt", "positiveInt"))
	  	json.value(new Integer(item.getValue()));
	  else if (Utilities.existsInList(type, "decimal"))
	  	json.value(new BigDecimal(item.getValue()));
	  else
	  	json.value(item.getValue());	
	}

	private void compose(String path, Element element) throws IOException {
		String name = element.getName();
		if (element.isPrimitive() || ParserBase.isPrimitive(element.getType())) {
			if (element.hasValue())
				primitiveValue(name, element);
			name = "_"+name;
		}
		if (element.hasChildren()) {
			open(name);
			if (element.getProperty().isResource()) {
				prop("resourceType", element.getType());
			}
			Set<String> done = new HashSet<String>();
			for (Element child : element.getChildren()) {
				compose(path+"."+element.getName(), element, done, child);
			}
			close();
		}
	}

}
