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
grammar Liquid;

options {
  output=AST;
  ASTLabelType=CommonTree;
}

tokens {
  ASSIGNMENT;
  ATTRIBUTES;
  BLOCK;
  CAPTURE;
  CASE;
  COMMENT;
  CYCLE; 
  ELSE;
  FILTERS;
  FILTER;
  FOR_ARRAY;
  FOR_BLOCK;
  FOR_RANGE;
  GROUP;
  IF;
  ELSIF;
  INCLUDE;
  LOOKUP;
  OUTPUT;
  PARAMS;
  PLAIN;
  RAW;
  TABLE;
  UNLESS;
  WHEN;
  WITH;
  NO_SPACE;
  CUSTOM_TAG;
  CUSTOM_TAG_BLOCK;
}

@parser::header {
  package ca.uhn.fhir.narrative.template.parser;
}

@lexer::header {
  package ca.uhn.fhir.narrative.template.parser;
}

@parser::members {

  private Flavor flavor = Flavor.LIQUID;

  public LiquidParser(Flavor flavor, TokenStream input) {
    this(input, new RecognizerSharedState());
    this.flavor = flavor;
  }

  @Override
  public void reportError(RecognitionException e) {
    throw new RuntimeException(e); 
  }
}

@lexer::members {
  private boolean inTag = false;
  private boolean inRaw = false;

  private boolean openRawEndTagAhead() {

    if(!openTagAhead()) {
      return false;
    }

    int indexLA = 3;

    while(Character.isSpaceChar(input.LA(indexLA))) {
      indexLA++;
    }

    return input.LA(indexLA) == 'e' &&
           input.LA(indexLA + 1) == 'n' &&
           input.LA(indexLA + 2) == 'd' &&
           input.LA(indexLA + 3) == 'r' &&
           input.LA(indexLA + 4) == 'a' &&
           input.LA(indexLA + 5) == 'w';
  }

  private boolean openTagAhead() {
    return input.LA(1) == '{' && (input.LA(2) == '{' || input.LA(2) == '\u0025');
  }
  
  @Override
  public void reportError(RecognitionException e) {
    throw new RuntimeException(e); 
  }

  private String strip(String text, boolean singleQuoted) {
    return text.substring(1, text.length() - 1);
  }
}

/* parser rules */
parse
 : block EOF -> block
   //(t=. {System.out.printf("\%-20s '\%s'\n", tokenNames[$t.type], $t.text);})* EOF
 ;

block
 : (options{greedy=true;}: atom)* -> ^(BLOCK atom*)
 ;

atom
 : tag
 | output
 | assignment
 | Other -> PLAIN[$Other.text]
 ;

tag
 : custom_tag
 | raw_tag
 | comment_tag
 | if_tag
 | unless_tag
 | case_tag
 | cycle_tag
 | for_tag
 | table_tag
 | capture_tag
 | include_tag
 | break_tag
 | continue_tag
 ;

custom_tag
 : (TagStart Id (expr (Comma expr)*)? TagEnd -> ^(CUSTOM_TAG Id expr*))
   ((custom_tag_block)=> custom_tag_block    -> ^(CUSTOM_TAG_BLOCK Id expr* custom_tag_block))?
 ;

custom_tag_block
 : (options{greedy=false;}: atom)* TagStart EndId TagEnd -> ^(BLOCK atom*)
 ;

raw_tag
 : TagStart RawStart TagEnd raw_body TagStart RawEnd TagEnd -> raw_body
 ;

raw_body
 : other_than_tag_start -> RAW[$other_than_tag_start.text]
 ;

comment_tag
 : TagStart CommentStart TagEnd comment_body TagStart CommentEnd TagEnd -> comment_body
 ;

comment_body
 : other_than_tag_start -> COMMENT[$other_than_tag_start.text]
 ;

other_than_tag_start
 : ~TagStart*
 ;

if_tag
 : TagStart IfStart expr TagEnd block elsif_tag* else_tag? TagStart IfEnd TagEnd -> ^(IF expr block elsif_tag* ^(ELSE else_tag?))
 ;

elsif_tag
 : TagStart Elsif expr TagEnd block -> ^(ELSIF expr block)
 ;

else_tag
 : TagStart Else TagEnd block -> block
 ;

unless_tag
 : TagStart UnlessStart expr TagEnd block else_tag? TagStart UnlessEnd TagEnd -> ^(UNLESS expr block ^(ELSE else_tag?))
 ;

case_tag
 : TagStart CaseStart expr TagEnd Other? when_tag+ else_tag? TagStart CaseEnd TagEnd -> ^(CASE expr when_tag+ ^(ELSE else_tag?))
 ;

when_tag
 : TagStart When term ((Or | Comma) term)* TagEnd block -> ^(WHEN term+ block)
 ;

cycle_tag
 : TagStart Cycle cycle_group expr (Comma expr)* TagEnd -> ^(CYCLE cycle_group expr+)
 ;

cycle_group
 : ((expr Col)=> expr Col)? -> ^(GROUP expr?)
 ;

for_tag
 : for_array
 | for_range    
 ;

for_array
 : TagStart ForStart Id In lookup attribute* TagEnd
   for_block
   TagStart ForEnd TagEnd
   -> ^(FOR_ARRAY Id lookup for_block ^(ATTRIBUTES attribute*))
 ;

for_range
 : TagStart ForStart Id In OPar expr DotDot expr CPar attribute* TagEnd
   block
   TagStart ForEnd TagEnd
   -> ^(FOR_RANGE Id expr expr block ^(ATTRIBUTES attribute*))
 ;

for_block
 : a=block (TagStart Else TagEnd b=block)? -> ^(FOR_BLOCK block block?)
 ;

attribute
 : Id Col expr -> ^(Id expr)
 ;

table_tag
 : TagStart TableStart Id In lookup attribute* TagEnd block TagStart TableEnd TagEnd -> ^(TABLE Id lookup block ^(ATTRIBUTES attribute*))
 ;

capture_tag
 : TagStart CaptureStart ( Id TagEnd block TagStart CaptureEnd TagEnd  -> ^(CAPTURE Id block)
                         | Str TagEnd block TagStart CaptureEnd TagEnd -> ^(CAPTURE Id[$Str.text] block)
                         )
 ;

include_tag
 : TagStart Include ( {this.flavor == Flavor.JEKYLL}?=>
                      file_name_as_str TagEnd    -> ^(INCLUDE file_name_as_str ^(WITH    ))
                    | a=Str (With b=Str)? TagEnd -> ^(INCLUDE $a               ^(WITH $b?))
                    )
 ;

break_tag
 : TagStart Break TagEnd -> Break
 ;

continue_tag
 : TagStart Continue TagEnd -> Continue
 ;

output
 : OutStart expr filter* OutEnd -> ^(OUTPUT expr ^(FILTERS filter*))
 ;

filter
 : Pipe Id params? -> ^(FILTER Id ^(PARAMS params?))
 ;

params
 : Col expr (Comma expr)* -> expr+
 ;

assignment
 : TagStart Assign Id EqSign expr filter? TagEnd -> ^(ASSIGNMENT Id filter? expr)
 ;

expr
 : or_expr
 ;

or_expr
 : and_expr (Or^ and_expr)*
 ;

and_expr
 : contains_expr (And^ contains_expr)*
 ;

contains_expr
 : eq_expr (Contains^ eq_expr)?
 ;

eq_expr
 : rel_expr ((Eq | NEq)^ rel_expr)*
 ;

rel_expr
 : term ((LtEq | Lt | GtEq | Gt)^ term)?
 ;

term
 : DoubleNum
 | LongNum
 | Str
 | True
 | False
 | Nil
 | NoSpace+       -> NO_SPACE[$text]
 | lookup
 | Empty
 | OPar expr CPar -> expr
 ;

lookup
 : FluentPathExpression  -> ^(LOOKUP FluentPathExpression)
 ;

id
 : Id
 | Continue -> Id[$Continue.text]
 ;

id2
 : id
 | Empty        -> Id[$Empty.text]
 | CaptureStart -> Id[$CaptureStart.text]
 | CaptureEnd   -> Id[$CaptureEnd.text]
 | CommentStart -> Id[$CommentStart.text]
 | CommentEnd   -> Id[$CommentEnd.text]
 | RawStart     -> Id[$RawStart.text]
 | RawEnd       -> Id[$RawEnd.text]
 | IfStart      -> Id[$IfStart.text]
 | Elsif        -> Id[$Elsif.text]
 | IfEnd        -> Id[$IfEnd.text]
 | UnlessStart  -> Id[$UnlessStart.text]
 | UnlessEnd    -> Id[$UnlessEnd.text]
 | Else         -> Id[$Else.text]
 | Contains     -> Id[$Contains.text]
 | CaseStart    -> Id[$CaseStart.text]
 | CaseEnd      -> Id[$CaseEnd.text]
 | When         -> Id[$When.text]
 | Cycle        -> Id[$Cycle.text]
 | ForStart     -> Id[$ForStart.text]
 | ForEnd       -> Id[$ForEnd.text]
 | In           -> Id[$In.text]
 | And          -> Id[$And.text]
 | Or           -> Id[$Or.text]
 | TableStart   -> Id[$TableStart.text]
 | TableEnd     -> Id[$TableEnd.text]
 | Assign       -> Id[$Assign.text]
 | True         -> Id[$True.text]
 | False        -> Id[$False.text]
 | Nil          -> Id[$Nil.text]
 | Include      -> Id[$Include.text]
 | With         -> Id[$With.text]
 | EndId        -> Id[$EndId.text]
 | Break        -> Id[$Break.text]
 ;

file_name_as_str
 : other_than_tag_end -> Str[$text]
 ;

other_than_tag_end
 : ~TagEnd+
 ;

/* lexer rules */
OutStart : '{{' {inTag=true;};
OutEnd   : '}}' {inTag=false;};
TagStart : '{%' {inTag=true;};
TagEnd   : '%}' {inTag=false;};

Str : {inTag}?=> (SStr | DStr);

DotDot    : {inTag}?=> '..';
Dot       : {inTag}?=> '.';
NEq       : {inTag}?=> '!=' | '<>';
Eq        : {inTag}?=> '==';
EqSign    : {inTag}?=> '=';
GtEq      : {inTag}?=> '>=';
Gt        : {inTag}?=> '>';
LtEq      : {inTag}?=> '<=';
Lt        : {inTag}?=> '<';
Minus     : {inTag}?=> '-';
Pipe      : {inTag}?=> '|';
Col       : {inTag}?=> ':';
Comma     : {inTag}?=> ',';
OPar      : {inTag}?=> '(';
CPar      : {inTag}?=> ')';
OBr       : {inTag}?=> '[';
CBr       : {inTag}?=> ']';
QMark     : {inTag}?=> '?';

DoubleNum : {inTag}?=> '-'? Digit+ ( {input.LA(1) == '.' && input.LA(2) != '.'}?=> '.' Digit*
                                   | {$type = LongNum;}
                                   );
LongNum   : {inTag}?=> '-'? Digit+;
WS        : {inTag}?=> (' ' | '\t' | '\r' | '\n')+ {$channel=HIDDEN;};

FluentPathExpression 
 : {inTag}?=> (Letter | '_') (Letter | '_' | '-' | Digit | '.' | '[' | ']' | '(' | ')' )*
   {
     if($text.equals("capture"))           $type = CaptureStart;
     else if($text.equals("endcapture"))   $type = CaptureEnd;
     else if($text.equals("comment"))      $type = CommentStart;
     else if($text.equals("endcomment"))   $type = CommentEnd;
     else if($text.equals("raw"))        { $type = RawStart; inRaw = true; }
     else if($text.equals("endraw"))     { $type = RawEnd; inRaw = false; }
     else if($text.equals("if"))           $type = IfStart;
     else if($text.equals("elsif"))        $type = Elsif;
     else if($text.equals("endif"))        $type = IfEnd;
     else if($text.equals("unless"))       $type = UnlessStart;
     else if($text.equals("endunless"))    $type = UnlessEnd;
     else if($text.equals("else"))         $type = Else;
     else if($text.equals("contains"))     $type = Contains;
     else if($text.equals("case"))         $type = CaseStart;
     else if($text.equals("endcase"))      $type = CaseEnd;
     else if($text.equals("when"))         $type = When;
     else if($text.equals("cycle"))        $type = Cycle;
     else if($text.equals("for"))          $type = ForStart;
     else if($text.equals("endfor"))       $type = ForEnd;
     else if($text.equals("in"))           $type = In;
     else if($text.equals("and"))          $type = And;
     else if($text.equals("or"))           $type = Or;
     else if($text.equals("tablerow"))     $type = TableStart;
     else if($text.equals("endtablerow"))  $type = TableEnd;
     else if($text.equals("assign"))       $type = Assign;
     else if($text.equals("true"))         $type = True;
     else if($text.equals("false"))        $type = False;
     else if($text.equals("nil"))          $type = Nil;
     else if($text.equals("null"))         $type = Nil;
     else if($text.equals("include"))      $type = Include;
     else if($text.equals("with"))         $type = With;
     else if($text.startsWith("end"))      $type = EndId;
     else if($text.equals("break"))        $type = Break;
     else if($text.startsWith("continue")) $type = Continue;
     else if($text.startsWith("empty"))    $type = Empty;
   }
 ;


Id
 : {inTag}?=> (Letter | '_') (Letter | '_' | '-' | Digit)*
   {
     if($text.equals("capture"))           $type = CaptureStart;
     else if($text.equals("endcapture"))   $type = CaptureEnd;
     else if($text.equals("comment"))      $type = CommentStart;
     else if($text.equals("endcomment"))   $type = CommentEnd;
     else if($text.equals("raw"))        { $type = RawStart; inRaw = true; }
     else if($text.equals("endraw"))     { $type = RawEnd; inRaw = false; }
     else if($text.equals("if"))           $type = IfStart;
     else if($text.equals("elsif"))        $type = Elsif;
     else if($text.equals("endif"))        $type = IfEnd;
     else if($text.equals("unless"))       $type = UnlessStart;
     else if($text.equals("endunless"))    $type = UnlessEnd;
     else if($text.equals("else"))         $type = Else;
     else if($text.equals("contains"))     $type = Contains;
     else if($text.equals("case"))         $type = CaseStart;
     else if($text.equals("endcase"))      $type = CaseEnd;
     else if($text.equals("when"))         $type = When;
     else if($text.equals("cycle"))        $type = Cycle;
     else if($text.equals("for"))          $type = ForStart;
     else if($text.equals("endfor"))       $type = ForEnd;
     else if($text.equals("in"))           $type = In;
     else if($text.equals("and"))          $type = And;
     else if($text.equals("or"))           $type = Or;
     else if($text.equals("tablerow"))     $type = TableStart;
     else if($text.equals("endtablerow"))  $type = TableEnd;
     else if($text.equals("assign"))       $type = Assign;
     else if($text.equals("true"))         $type = True;
     else if($text.equals("false"))        $type = False;
     else if($text.equals("nil"))          $type = Nil;
     else if($text.equals("null"))         $type = Nil;
     else if($text.equals("include"))      $type = Include;
     else if($text.equals("with"))         $type = With;
     else if($text.startsWith("end"))      $type = EndId;
     else if($text.equals("break"))        $type = Break;
     else if($text.startsWith("continue")) $type = Continue;
     else if($text.startsWith("empty"))    $type = Empty;
   }
 ;

Other
 : ({!inTag && !openTagAhead()}?=> . )+
 | ({!inTag && inRaw && !openRawEndTagAhead()}?=> . )+
 ;

NoSpace
 : ~(' ' | '\t' | '\r' | '\n')
 ;

/* fragment rules */
fragment Letter : 'a'..'z' | 'A'..'Z';
fragment Digit  : '0'..'9';
fragment SStr   : '\'' ~'\''* '\'' {setText(strip($text, true));};
fragment DStr   : '"' ~'"'* '"'    {setText(strip($text, false));};

fragment CommentStart : 'CommentStart';
fragment CommentEnd : 'CommentEnd';
fragment RawStart : 'RawStart';
fragment RawEnd : 'RawEnd';
fragment IfStart : 'IfStart';
fragment IfEnd : 'IfEnd';
fragment Elsif : 'Elsif';
fragment UnlessStart : 'UnlessStart';
fragment UnlessEnd : 'UnlessEnd';
fragment Else : 'Else';
fragment Contains : 'contains';
fragment CaseStart : 'CaseStart';
fragment CaseEnd : 'CaseEnd';
fragment When : 'When';
fragment Cycle : 'Cycle';
fragment ForStart : 'ForStart';
fragment ForEnd : 'ForEnd';
fragment In : 'In';
fragment And : 'And';
fragment Or : 'Or';
fragment TableStart : 'TableStart';
fragment TableEnd : 'TableEnd';
fragment Assign : 'Assign';
fragment True : 'True';
fragment False : 'False';
fragment Nil : 'Nil';
fragment Include : 'Include';
fragment With : 'With';
fragment CaptureStart : 'CaptureStart';
fragment CaptureEnd : 'CaptureEnd';
fragment EndId : 'EndId';
fragment Break : 'Break';
fragment Continue : 'Continue';
fragment Empty : 'Empty';
