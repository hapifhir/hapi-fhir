package org.hl7.fhir.utilities;

/*-
 * #%L
 * org.hl7.fhir.utilities
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.util.Collections;
import java.util.Set;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import com.github.rjeschke.txtmark.Processor;

public class MarkDownProcessor {

  public enum Dialect {DARING_FIREBALL, COMMON_MARK};
  
  private Dialect dialect;
  
  
  public MarkDownProcessor(Dialect dialect) {
    super();
    this.dialect = dialect;
  }


  public String process(String source, String context) {
    switch (dialect) {
    case DARING_FIREBALL : return Processor.process(source); 
    case COMMON_MARK : return processCommonMark(source); 
    default: throw new Error("Unknown Markdown Dialect: "+dialect.toString()+" at "+context); 
    }
  }

  private String processCommonMark(String source) {
    Set<Extension> extensions = Collections.singleton(TablesExtension.create());
    Parser parser = Parser.builder().extensions(extensions).build();
    Node document = parser.parse(source);
    HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();
    String html = renderer.render(document);
    html = html.replace("<table>", "<table class=\"grid\">");
    return html;  
  }
  
}
