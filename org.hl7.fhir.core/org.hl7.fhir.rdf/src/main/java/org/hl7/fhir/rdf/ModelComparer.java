package org.hl7.fhir.rdf;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.jena.graph.Node_Blank;
import org.apache.jena.graph.Node_Literal;
import org.apache.jena.graph.Node_URI;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

public class ModelComparer {

  public enum TripleType {
    BNODE,
    URI,  // o1.getClass() == Node_URI.class
    STRING,
    BOOLEAN,
    INTEGER,
    BASE64BINARY,
    DATE,
    DATETIME,
    GYEAR,
    GYEARMONTH,
    DECIMAL; 
  }

  
  public class TypedTriple {
    public String object;
    private String subject;
    private String predicate;
    private TripleType type;

    public TypedTriple(Triple trip) {
      subject = trip.getSubject().toString();
      predicate = trip.getPredicate().toString();
      if (trip.getObject().getClass() == Node_URI.class) {
        type = TripleType.URI;
        object = trip.getObject().toString();
      } else if (trip.getObject().getClass() == Node_Blank.class) {
        type = TripleType.BNODE;
        object = trip.getObject().toString();
      } else if (trip.getObject().getClass() == Node_Literal.class) {
        if (trip.getObject().getLiteralDatatypeURI().equals("http://www.w3.org/2001/XMLSchema#decimal")) {
          type = TripleType.DECIMAL;
          object = trip.getObject().getLiteralLexicalForm();
        } else if (trip.getObject().getLiteralDatatypeURI().equals("http://www.w3.org/2001/XMLSchema#string")) {
          type = TripleType.STRING;
          object = trip.getObject().getLiteralLexicalForm();
        } else if (trip.getObject().getLiteralDatatypeURI().equals("http://www.w3.org/2001/XMLSchema#date")) {
          type = TripleType.DATE;
          object = trip.getObject().getLiteralLexicalForm();
        } else if (trip.getObject().getLiteralDatatypeURI().equals("http://www.w3.org/2001/XMLSchema#dateTime")) {
          type = TripleType.DATETIME;
          object = trip.getObject().getLiteralLexicalForm();
        } else if (trip.getObject().getLiteralDatatypeURI().equals("http://www.w3.org/2001/XMLSchema#gYear")) {
          type = TripleType.GYEAR;
          object = trip.getObject().getLiteralLexicalForm();
        } else if (trip.getObject().getLiteralDatatypeURI().equals("http://www.w3.org/2001/XMLSchema#gYearMonth")) {
          type = TripleType.GYEARMONTH;
          object = trip.getObject().getLiteralLexicalForm();
        } else if (trip.getObject().getLiteralDatatypeURI().equals("http://www.w3.org/2001/XMLSchema#integer")) {
          type = TripleType.INTEGER;
          object = trip.getObject().getLiteralLexicalForm();
        } else if (trip.getObject().getLiteralDatatypeURI().equals("http://www.w3.org/2001/XMLSchema#boolean")) {
          type = TripleType.BOOLEAN;
          object = trip.getObject().getLiteralLexicalForm();
        } else if (trip.getObject().getLiteralDatatypeURI().equals("http://www.w3.org/2001/XMLSchema#base64Binary")) {
          type = TripleType.BASE64BINARY;
          object = trip.getObject().getLiteralLexicalForm();
        } else {
          throw new Error("not done yet ("+trip.getObject().getLiteralDatatypeURI()+")");          
        }
      } else {
        throw new Error("not done yet ("+trip.getObject().getClass().getName()+")");
      }
    }

    public String getSubject() {
      return subject;
    }

    public String getObject() {
      return object;
    }

    public String getPredicate() {
      return predicate;
    }

    public TripleType getType() {
      return type;
    }
  }

  private Model model1;
  private Model model2;
  private String name1;
  private String name2;
  List<TypedTriple> tl1;
  List<TypedTriple> tl2;

  public ModelComparer setModel1(Model model, String name) throws IOException {
    model1 = model;
    name1 = name;
    tl1 = listAllTriples(model1);
    log(model, "c:\\temp\\triples-"+name+".txt");
    return this;
  }

  private void log(Model model, String filename) throws IOException {
    StringBuilder b = new StringBuilder();
    b.append(filename);
    b.append("\r\n");
    for ( final StmtIterator res = model.listStatements(); res.hasNext(); ) {
      final Statement r = res.next();
      Triple t = r.asTriple();
      b.append(t.getSubject().toString());
      b.append("\t");
      b.append(t.getPredicate().toString());
      b.append("\t");
      b.append(t.getObject().toString());
      b.append("\r\n");
    }
//    TextFile.stringToFile(b.toString(), filename);
//    System.out.println(b.toString());
  }

  public ModelComparer setModel2(Model model, String name) throws IOException {
    model2 = model;
    name2 = name;
    tl2 = listAllTriples(model2);
    log(model, "c:\\temp\\triples-"+name+".txt");
    return this;
  }

  public List<String> compare() {
    Set<String> ep1 = listEntryPoints(tl1);
    Set<String> ep2 = listEntryPoints(tl2);
    List<String> diffs = new ArrayList<String>();
    if (ep1.size() != ep2.size())
      diffs.add("Entry point counts differ");
    if (ep1.size() != 1)
      diffs.add("Entry point count != 1");
    String ep = ep1.iterator().next();
    compare(diffs, ep, ep, ep);
    return diffs;
  }

  
  private void compare(List<String> diffs, String url1, String url2, String statedPath) {
    List<TypedTriple> pl1 = listAllProperties(tl1, url1);
    List<TypedTriple> pl2 = listAllProperties(tl2, url2);
    Set<String> handled = new HashSet<String>();
    for (TypedTriple t : pl1) {
      String pred = t.getPredicate();
      if (!handled.contains(pred)) {
        comparePredicate(diffs, statedPath, pred, pl1, pl2);
      }
    }
  }

  private void comparePredicate(List<String> diffs, String statedPath, String pred, List<TypedTriple> pl1, List<TypedTriple> pl2) {
    List<TypedTriple> ml1 = listMatchingProperties(pl1, pred);
    List<TypedTriple> ml2 = listMatchingProperties(pl2, pred);
    if (ml1.size() != ml2.size()) {
      if (!isExpectedDifference(statedPath, pred, ml1.size(), ml2.size()))
        diffs.add("Difference at "+statedPath+" for "+pred+": "+name1+" has "+Integer.toString(ml1.size())+" values, but "+name2+" has "+Integer.toString(ml2.size())+" values");
    } else if (ml1.size() == 1) {
      compareObjects(diffs, statedPath, pred, ml1.get(0), ml2.get(0));
    } else for (int i = 0; i < ml1.size(); i++) {
      String id = pred+"["+Integer.toString(i)+"]";
      TypedTriple o1 = getByIndex(ml1, tl1, i, statedPath, id);
      if (o1 == null)
        diffs.add("Unable to find "+statedPath+" / "+id+" in "+name1);
      else {
        TypedTriple o2 = getByIndex(ml2, tl2, i, statedPath, id);
        if (o2 == null)
          diffs.add("Unable to find "+statedPath+" / "+id+" in "+name2);        
        else
          compareObjects(diffs, statedPath, id, o1, o2);
      }
    }
  }

  private void compareObjects(List<String> diffs, String statedPath, String pred, TypedTriple o1, TypedTriple o2) {
    if (o1.getType() == TripleType.BNODE || o2.getType() == TripleType.BNODE ) {
      // bnodes: follow the nodes
      compare(diffs, o1.toString(), o2.toString(), statedPath+" / "+pred);
    } else if (o1.getType() == TripleType.URI && o2.getType() == TripleType.URI) {
      // if either is a url, just compare literal values
      String u1 = o1.getObject();
      String u2 = o2.getObject();
      if (u1.startsWith("\"") && u1.endsWith("\""))
        u1 = u1.substring(1, u1.length()-1);
      if (u2.startsWith("\"") && u2.endsWith("\""))
        u2 = u2.substring(1, u2.length()-1);
      if (!u1.equals(u2)) 
        diffs.add("Difference at "+statedPath+" for "+pred+": URL objects have different values: "+name1+" = "+u1+", "+name2+" = "+u2+"");
    } else if (o1.getType() == o2.getType()) {
      if (o1.getType().equals(TripleType.DECIMAL)) {
        BigDecimal d1 = new BigDecimal(o1.getObject());
        BigDecimal d2 = new BigDecimal(o2.getObject());
        if (d1.compareTo(d2) != 0) 
          diffs.add("Difference at "+statedPath+" for "+pred+": Literal decimal objects have different values: "+name1+" = "+o1.getObject()+", "+name2+" = "+o2.getObject()+"");
      } else if (!o1.getObject().equals(o2.getObject())) {
        diffs.add("Difference at "+statedPath+" for "+pred+": Literal objects have different values: "+name1+" = "+o1.getObject()+", "+name2+" = "+o2.getObject()+"");
      }  
    } else if (o1.getClass() != o2.getClass()) 
      diffs.add("Difference at "+statedPath+" for "+pred+": Literal objects have different types: "+name1+" = "+o1.getType().toString()+", "+name2+" = "+o2.getType().toString()+"");
  }

  private TypedTriple getByIndex(List<TypedTriple> matches, List<TypedTriple> all, int index, String statedPath, String id) {
    for (TypedTriple t : matches) {
      for (TypedTriple s : all) {
        if (s.getSubject().equals(t.getObject()) && 
            s.getPredicate().equals("http://hl7.org/fhir/index") && 
            s.getObject().equals(Integer.toString(index))) {
          return t;
        }
      }
    }
    return null;
  }

  private boolean isExpectedDifference(String statedPath, String pred, int c1, int c2) {
//    if (pred.equals("http://hl7.org/fhir/nodeRole") && c1 == 1 && c2 == 0)
//      return true;
//    if (pred.equals("http://hl7.org/fhir/index") && c1 == 1 && c2 == 0)
//      return true;
    return false;
  }

  private List<TypedTriple> listMatchingProperties(List<TypedTriple> list, String pred) {
    List<TypedTriple> props = new ArrayList<TypedTriple>();
    for (TypedTriple t : list) {
      if (t.getPredicate().equals(pred))
        props.add(t);
    }
    return props;
  }

  private List<TypedTriple> listAllProperties(List<TypedTriple> list, String subject) {
    List<TypedTriple> props = new ArrayList<TypedTriple>();
    for (TypedTriple t : list) {
      if (t.getSubject().toString().equals(subject))
        props.add(t);
    }
    return props;
  }

  private Set<String> listEntryPoints(List<TypedTriple> list) {
    Set<String> ep1 = new HashSet<String>();
    for (TypedTriple t : list) {
      boolean found = false;
      for (TypedTriple s : list) {
        if (t.getSubject().equals(s.getObject())) {
          found = true;
          break;
        }
      }
      if (!found)
        ep1.add(t.getSubject());
    };
    return ep1;
  }

  private List<TypedTriple> listAllTriples(Model m1) {
    List<TypedTriple> tl1 = new ArrayList<TypedTriple>();
    for ( final StmtIterator res = m1.listStatements(); res.hasNext(); ) {
      final Statement r = res.next();
      tl1.add(new TypedTriple(r.asTriple()));
    }
    return tl1;
  }

}
