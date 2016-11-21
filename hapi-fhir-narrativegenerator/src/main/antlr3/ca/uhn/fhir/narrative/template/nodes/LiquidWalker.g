/*
 * Copyright (c) 2010 by Bart Kiers
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * 
 * Project      : Liqp; a Liquid Template grammar/parser
 * Developed by : Bart Kiers, bart@big-o.nl
 */
tree grammar LiquidWalker;

options {
  tokenVocab=Liquid;
  ASTLabelType=CommonTree;
}

@header {
  package ca.uhn.fhir.narrative.template.nodes;

  import ca.uhn.fhir.narrative.template.parser.*;
  import ca.uhn.fhir.narrative.template.tags.*;
  import ca.uhn.fhir.narrative.template.filters.*;

  import java.util.Map;
}

@members {

  private Map<String, Tag> tags;
  private Map<String, Filter> filters;
  private Flavor flavor;

  public LiquidWalker(TreeNodeStream nodes, Map<String, Tag> tags, Map<String, Filter> filters) {
    this(nodes, tags, filters, Flavor.LIQUID);
  }

  public LiquidWalker(TreeNodeStream nodes, Map<String, Tag> tags, Map<String, Filter> filters, Flavor flavor) {
    super(nodes);
    this.tags = tags;
    this.filters = filters;
    this.flavor = flavor;
  }
}

walk returns [LNode node]
 : block {$node = $block.node;}
 ;

block returns [BlockNode node]
@init{$node = new BlockNode();}
 : ^(BLOCK (atom {$node.add($atom.node);})*)
 ;

atom returns [LNode node]
 : tag        {$node = $tag.node;}
 | output     {$node = $output.node;}
 | assignment {$node = $assignment.node;}
 | PLAIN      {$node = new AtomNode($PLAIN.text);}
 ;

tag returns [LNode node]
 : raw_tag          {$node = $raw_tag.node;}
 | comment_tag      {$node = $comment_tag.node;}
 | if_tag           {$node = $if_tag.node;}
 | unless_tag       {$node = $unless_tag.node;}
 | case_tag         {$node = $case_tag.node;}
 | cycle_tag        {$node = $cycle_tag.node;}
 | for_tag          {$node = $for_tag.node;}
 | table_tag        {$node = $table_tag.node;}
 | capture_tag      {$node = $capture_tag.node;}
 | include_tag      {$node = $include_tag.node;}
 | custom_tag       {$node = $custom_tag.node;}
 | custom_tag_block {$node = $custom_tag_block.node;}
 | break_tag        {$node = $break_tag.node;}
 | continue_tag     {$node = $continue_tag.node;}
 ;

raw_tag returns [LNode node]
 : RAW {$node = new TagNode("raw", tags.get("raw"), new AtomNode($RAW.text));}
 ;

comment_tag returns [LNode node]
 : COMMENT {$node = new TagNode("comment", tags.get("comment"), new AtomNode($COMMENT.text));}
 ;

if_tag returns [LNode node]
@init{List<LNode> nodes = new ArrayList<LNode>();}
 : ^(IF        e1=expr b1=block {nodes.add($e1.node); nodes.add($b1.node);}
      (^(ELSIF e2=expr b2=block {nodes.add($e2.node); nodes.add($b2.node);} ))*
       ^(ELSE         (b3=block {nodes.add(new AtomNode("TRUE")); nodes.add($b3.node);} )?)
    )
    {$node = new TagNode("if", tags.get("if"), nodes.toArray(new LNode[nodes.size()]));}
 ;

unless_tag returns [LNode node]
@init{List<LNode> nodes = new ArrayList<LNode>();}
 : ^(UNLESS expr b1=block {nodes.add($expr.node); nodes.add($b1.node);}
   ^(ELSE (b2=block       {nodes.add(new AtomNode(null)); nodes.add($b2.node);})?))
    {$node = new TagNode("unless", tags.get("unless"), nodes.toArray(new LNode[nodes.size()]));}
 ;

case_tag returns [LNode node]
@init{List<LNode> nodes = new ArrayList<LNode>();}
 : ^(CASE expr   {nodes.add($expr.node);}
    (when_tag    [nodes] )+
   ^(ELSE (block {nodes.add(nodes.get(0)); nodes.add($block.node);} )?))
    {$node = new TagNode("case", tags.get("case"), nodes.toArray(new LNode[nodes.size()]));}
 ;

when_tag[List<LNode> nodes]
 : ^(WHEN (expr {nodes.add($expr.node);})+ block) {nodes.add($block.node);}
 ;

cycle_tag returns [LNode node]
@init{List<LNode> nodes = new ArrayList<LNode>();}
 : ^(CYCLE cycle_group {nodes.add($cycle_group.node);} (e=expr {nodes.add($e.node);})+)
    {$node = new TagNode("cycle", tags.get("cycle"), nodes.toArray(new LNode[nodes.size()]));}
 ;

cycle_group returns [LNode node]
 : ^(GROUP expr?) {$node = $expr.node;}
 ;

for_tag returns [LNode node]
 : for_array {$node = $for_array.node;}
 | for_range {$node = $for_range.node;}
 ;

for_array returns [LNode node]
@init{
  List<LNode> expressions = new ArrayList<LNode>();
  expressions.add(new AtomNode(true));
}
 : ^(FOR_ARRAY Id lookup      {expressions.add(new AtomNode($Id.text)); expressions.add($lookup.node);}
      for_block               {expressions.add($for_block.node1); expressions.add($for_block.node2);}
      ^(ATTRIBUTES (attribute {expressions.add($attribute.node);})*)
    )
    {$node = new TagNode("for", tags.get("for"), expressions.toArray(new LNode[expressions.size()]));}
 ;

for_range returns [LNode node]
@init{
  List<LNode> expressions = new ArrayList<LNode>();
  expressions.add(new AtomNode(false));
}
 : ^(FOR_RANGE Id from=expr to=expr {expressions.add(new AtomNode($Id.text)); expressions.add($from.node); expressions.add($to.node);}
      block                         {expressions.add($block.node);}
      ^(ATTRIBUTES (attribute       {expressions.add($attribute.node);})*)
    )
    {$node = new TagNode("for", tags.get("for"), expressions.toArray(new LNode[expressions.size()]));}
 ;

for_block returns [LNode node1, LNode node2]
 : ^(FOR_BLOCK n1=block n2=block?)
    {
      $node1 = $n1.node;
      $node2 = $n2.node;
    }
 ;

attribute returns [LNode node]
 : ^(Id expr) {$node = new AttributeNode(new AtomNode($Id.text), $expr.node);}
 ;

table_tag returns [LNode node]
@init{
  List<LNode> expressions = new ArrayList<LNode>();
}
 : ^(TABLE
      Id                      {expressions.add(new AtomNode($Id.text));}
      lookup                  {expressions.add($lookup.node);}
      block                   {expressions.add($block.node);}
      ^(ATTRIBUTES (attribute {expressions.add($attribute.node);})*)
    )
    {$node = new TagNode("tablerow", tags.get("tablerow"), expressions.toArray(new LNode[expressions.size()]));}
 ;

capture_tag returns [LNode node]
 : ^(CAPTURE Id block) {$node = new TagNode("capture", tags.get("capture"), new AtomNode($Id.text), $block.node);}
 ;

include_tag returns [LNode node]
 : ^(INCLUDE file=Str ^(WITH (with=Str)?))
    {
      if($with.text != null) {
        $node = new TagNode("include", tags.get("include"), flavor, new AtomNode($file.text), new AtomNode($with.text));
      } else {
        $node = new TagNode("include", tags.get("include"), flavor, new AtomNode($file.text));
      }
    }
 ;

break_tag returns [LNode node]
 : Break {$node = new AtomNode(Tag.Statement.BREAK);}
 ;

continue_tag returns [LNode node]
 : Continue {$node = new AtomNode(Tag.Statement.CONTINUE);}
 ;


custom_tag returns [LNode node]
@init{List<LNode> expressions = new ArrayList<LNode>();}
 : ^(CUSTOM_TAG Id (expr {expressions.add($expr.node);})*)
    {$node = new TagNode($Id.text, tags.get($Id.text), expressions.toArray(new LNode[expressions.size()]));}
 ;

custom_tag_block returns [LNode node]
@init{List<LNode> expressions = new ArrayList<LNode>();}
 : ^(CUSTOM_TAG_BLOCK Id (expr {expressions.add($expr.node);})* block {expressions.add($block.node);})
    {$node = new TagNode($Id.text, tags.get($Id.text), expressions.toArray(new LNode[expressions.size()]));}
 ;

output returns [OutputNode node]
 : ^(OUTPUT expr {$node = new OutputNode($expr.node);} ^(FILTERS (filter {$node.addFilter($filter.node);})*))
 ;

filter returns [FilterNode node]
 : ^(FILTER Id {$node = new FilterNode($Id.text, filters.get($Id.text));} ^(PARAMS params[$node]?))
 ;

params[FilterNode node]
 : (expr {$node.add($expr.node);})+
 ;

assignment returns [TagNode node]
 : ^(ASSIGNMENT Id filter? expr) {$node = new TagNode("assign", tags.get("assign"), new AtomNode($Id.text), $filter.node, $expr.node);}
 ;

expr returns [LNode node]
 : ^(Or a=expr b=expr)       {$node = new OrNode($a.node, $b.node);}
 | ^(And a=expr b=expr)      {$node = new AndNode($a.node, $b.node);}
 | ^(Eq a=expr b=expr)       {$node = new EqNode($a.node, $b.node);}
 | ^(NEq a=expr b=expr)      {$node = new NEqNode($a.node, $b.node);}
 | ^(LtEq a=expr b=expr)     {$node = new LtEqNode($a.node, $b.node);}
 | ^(Lt a=expr b=expr)       {$node = new LtNode($a.node, $b.node);}
 | ^(GtEq a=expr b=expr)     {$node = new GtEqNode($a.node, $b.node);}
 | ^(Gt a=expr b=expr)       {$node = new GtNode($a.node, $b.node);}
 | ^(Contains a=expr b=expr) {$node = new ContainsNode($a.node, $b.node);}
 | LongNum                   {$node = new AtomNode(new Long($LongNum.text));}
 | DoubleNum                 {$node = new AtomNode(new Double($DoubleNum.text));}
 | Str                       {$node = new AtomNode($Str.text);}
 | True                      {$node = new AtomNode(true);}
 | False                     {$node = new AtomNode(false);}
 | Nil                       {$node = new AtomNode(null);}
 | NO_SPACE                  {$node = new AtomNode($NO_SPACE.text);}
 | lookup                    {$node = $lookup.node;}
 | Empty                     {$node = AtomNode.EMPTY;}
 ;

lookup returns [LookupNode node]
 : ^(LOOKUP
      FluentPathExpression      {$node = new LookupNode($FluentPathExpression.text);}
    )
 ;

