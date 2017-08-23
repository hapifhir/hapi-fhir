/**
 * Define a grammar called FhirMapper
 */
grammar FhirMapBase;

mappingUnit: keyMap keyUses+ (keyImports)* group ;

keyMap: MAP structureMap '=' quotedString ; 

keyUses: USES structureDefinition AS keyUsesName (';')? ;

keyUsesName: keyUsesNameSource | keyUsesNameTarget | keyUsesNameQueried | keyUsesNameProduced ;
keyUsesNameSource: SOURCE ;
keyUsesNameTarget: TARGET ;
keyUsesNameQueried:  QUERIED  ;
keyUsesNameProduced: PRODUCED ;

keyImports: IMPORTS structureMap (';')? ;

group: groupStart ruleInput* ruleInstance* groupEnd ;
groupStart: GROUP groupType? identifier groupExtends?;
groupExtends:EXTENDS identifier ;
groupEnd: ENDGROUP ;

groupType: FOR groupTypeValue ;
groupTypeValue: groupTypeType | groupTypeTypeTypes;
groupTypeType: TYPES ;
groupTypeTypeTypes: TYPE_TYPES ;

ruleInput: INPUT ruleInputName ':' ruleInputType ruleInputMode?  (';')? ;
ruleInputName:  identifier ;
ruleInputType: identifier ;
ruleInputMode: AS ruleInputModes ;
ruleInputModes: ruleInputModesSource | ruleInputModesTarget;
ruleInputModesSource: SOURCE ;
ruleInputModesTarget: TARGET ;

ruleInstance: ruleName ':' FOR ruleSources ruleMake? ;
ruleName: identifier ( '.' identifier)*;

ruleSources: ruleSource (',' ruleSource)* ;
ruleSource : ruleContext ruleType? ruleDefault? ruleListOption? ruleVariable? ruleWherePath? ruleCheckPath? ;

ruleType: ':' identifier (integer '..' integer)? ;
ruleDefault: DEFAULT identifier ;
//#! What are list options...
ruleListOption: 'xxxxyyyyyzzzzzz' ;

ruleVariable: AS identifier ;
ruleContext: ruleContextElement ('.' ruleContextElement)*; 
ruleContextElement: identifier | quotedString ;

ruleWherePath : WHERE fhirPath ;
ruleCheckPath : CHECK fhirPath ;

ruleMake: MAKE ruleTargets ruleDependents? ;
ruleTargets: ruleTarget (',' ruleTarget)* ;

ruleDependents: THEN '{' ruleInstance* '}' ;

ruleTarget
	: ruleTargetAppend
	| ruleTargetAs
	| ruleTargetAssign
	| ruleTargetC
	| ruleTargetCast
	| ruleTargetCC
	| ruleTargetCp
	| ruleTargetCopy
	| ruleTargetCreate
	| ruleTargetDateOp
	| ruleTargetEscape
	| ruleTargetEvaluate
	| ruleTargetId
	| ruleTargetPointer
	| ruleTargetQty
	| ruleTargetReference
	| ruleTargetTranslate
	| ruleTargetTruncate
	| ruleTargetUuid
	;

ruleTargetContext: ruleContext '=' ;

ruleTargetAs: ruleContext ruleTargetVariable;

ruleTargetAssign: ruleTargetContext ruleTargetAssignValue ruleTargetVariable?;
ruleTargetAssignValue: quotedStringWQuotes | identifier;

ruleTargetAppend: ruleTargetContext? APPEND '('  ruleTargetAppendSources ')' ruleTargetVariable? ;
ruleTargetAppendSources: ruleTargetAppendSource ( ',' ruleTargetAppendSource )* ;
ruleTargetAppendSource: identifier | quotedStringWQuotes ;

ruleTargetC: ruleTargetContext? C '(' ruleTargetCSystem ',' ruleTargetCCode (',' ruleTargetCDisplay)? ')' ruleTargetVariable? ;
ruleTargetCSystem: quotedUrl ;
ruleTargetCCode: quotedStringWQuotes | identifier;
ruleTargetCDisplay: quotedString ;

ruleTargetCast: ruleTargetContext? CAST '(' ruleTargetCastSource (',' ruleTargetCastType)? ')' ruleTargetVariable? ;
ruleTargetCastSource: identifier ;
ruleTargetCastType: identifier ;

ruleTargetCC: (ruleTargetCC1 | ruleTargetCC2)  ruleTargetVariable?;
ruleTargetCC1: ruleTargetContext? CC '(' ruleTargetCC1Text ')' ruleTargetVariable? ;
ruleTargetCC1Text: quotedString;
ruleTargetCC2: ruleTargetContext? CC '(' ruleTargetCC2System ',' ruleTargetCC2Code (',' ruleTargetCC2Display)? ')' ruleTargetVariable? ;
ruleTargetCC2System: quotedUrl ;
ruleTargetCC2Code: quotedStringWQuotes | identifier;
ruleTargetCC2Display: quotedString ;

ruleTargetCp: ruleTargetContext? CP '(' (ruleTargetCpSystem ',')? ruleTargetCpVariable ')' ruleTargetVariable? ;
ruleTargetCpSystem: quotedUrl ;
ruleTargetCpVariable: identifier ;

ruleTargetCopy: ruleTargetContext COPY '(' ruleTargetCopySource ')' ruleTargetVariable? ;
ruleTargetCopySource: identifier ;

ruleTargetCreate: ruleTargetContext? CREATE'(' ruleTargetCreateType ')' ruleTargetVariable? ;
ruleTargetCreateType: quotedIdentifier ;

//# Not Tested
ruleTargetDateOp: ruleTargetContext? DATEOP '(' ruleTargetDateOpVariable ',' ruleTargetDateOpOperation (',' ruleTargetDateOpVariable2)? ')' ruleTargetVariable? ;
ruleTargetDateOpVariable: identifier ;
ruleTargetDateOpOperation: quotedString ;
ruleTargetDateOpVariable2: identifier ;

ruleTargetEscape: ruleTargetContext? ESCAPE '(' ruleTargetEscapeVariable ','  ruleTargetEscapeString1 (',' ruleTargetEscapeString2)? ')' ruleTargetVariable? ;
ruleTargetEscapeVariable: identifier;
ruleTargetEscapeString1: quotedString;
ruleTargetEscapeString2: quotedString;

ruleTargetEvaluate: ruleTargetContext? EVALUATE '(' ruleTargetEvaluateObject ',' ruleTargetEvaluateObjectElement ')' ruleTargetVariable? ;
ruleTargetEvaluateObject: identifier;
ruleTargetEvaluateObjectElement: identifier;

ruleTargetId: ruleTargetContext? ID '(' ruleTargetIdSystem ',' ruleTargetIdValue (',' ruleTargetIdType)? ')' ruleTargetVariable? ;
ruleTargetIdSystem: quotedUrl ;
ruleTargetIdValue: identifier ;
ruleTargetIdType: identifier ;

ruleTargetPointer: ruleTargetContext?  POINTER'(' ruleTargetPointerResource ')' ruleTargetVariable? ;
ruleTargetPointerResource: identifier;

ruleTargetQty: ruleTargetQty1 | ruleTargetQty2 | ruleTargetQty3 ;
ruleTargetQty1: ruleTargetContext? QTY '(' ruleTargetQty1Text ')' ruleTargetVariable? ;
ruleTargetQty1Text: quotedString ;

ruleTargetQty2: ruleTargetContext? QTY '(' ruleTargetQty2Value ',' ruleTargetQty2UnitString ',' ruleTargetQty2System ')' ruleTargetVariable? ;
ruleTargetQty2Value: identifier ;
ruleTargetQty2UnitString: quotedString ;
ruleTargetQty2System: quotedUrl ;

ruleTargetQty3: ruleTargetContext? QTY '(' ruleTargetQty3Value ',' ruleTargetQty3UnitString ',' ruleTargetQty3CodeVariable ')' ruleTargetVariable? ;
ruleTargetQty3Value: identifier ;
ruleTargetQty3UnitString: quotedString ;
ruleTargetQty3CodeVariable: identifier ;

ruleTargetReference: ruleTargetContext? REFERENCE '(' ruleTargetReferenceSource ')' ruleTargetVariable? ;
ruleTargetReferenceSource: identifier | quotedStringWQuotes ;

ruleTargetTranslate: ruleTargetContext? TRANSLATE'(' ruleTargetTranslateSource ',' ruleTargetTranslateMap ',' ruleTargetTranslateOutput')' ruleTargetVariable? ;
ruleTargetTranslateSource: identifier ;
ruleTargetTranslateMap: quotedUrl;
ruleTargetTranslateOutput
	: ruleTargetTranslateOutputCode 
	| ruleTargetTranslateOutputSystem 
	| ruleTargetTranslateOutputDisplay
	| ruleTargetTranslateOutputCoding
	| ruleTargetTranslateOutputCodeableConcept
	;
ruleTargetTranslateOutputCode: CODE ;
ruleTargetTranslateOutputSystem: SYSTEM ;
ruleTargetTranslateOutputDisplay: DISPLAY;
ruleTargetTranslateOutputCoding: CODING;
ruleTargetTranslateOutputCodeableConcept: CODEABLECONCEPT;

ruleTargetTruncate: ruleTargetContext? TRUNCATE'(' ruleTargetTruncateSource ',' ruleTargetTruncateLength ')' ruleTargetVariable? ;
ruleTargetTruncateSource: identifier | quotedStringWQuotes;
ruleTargetTruncateLength: integer ;

ruleTargetUuid: ruleTargetContext? UUID'(' ')' ruleTargetVariable? ;

ruleTargetVariable: AS identifier ;

//#! What is fhir path format ...
fhirPath: quotedString ;


identifier
	: IDENTIFIER 
	| SOURCE 
	| TARGET 
	| QUERIED 
	| PRODUCED
	| APPEND
	| CAST
	| C
	| CC
	| CP
	| CODEABLECONCEPT
	| CODING
	| COPY
	| CODE
	| DISPLAY
	| CREATE
	| DATEOP
	| DEFAULT
	| ESCAPE
	| EVALUATE
	| ID
	| MAP
	| POINTER
	| QTY
	| REFERENCE
 	| SYSTEM
	| TRANSLATE
	| TRUNCATE
	| TYPES
	| UUID
	;

integer: DIGITS;
quotedIdentifier: QIDENTIFIER ;
quotedStringWQuotes: QSTRING | QIDENTIFIER;
quotedString: QSTRING | QIDENTIFIER;
quotedUrl: quotedString ;
structureDefinition:  quotedUrl ;
structureMap: quotedUrl ;

/*
 * Lexer rules.
 */
fragment DIGIT_FRAG: [0-9] ;
fragment HEX_FRAG: [a-fA-F0-9] ;
fragment LETTER_FRAG: [a-zA-Z] ;

HEX: '%' HEX_FRAG HEX_FRAG+ ;

DIGITS: DIGIT_FRAG+ ;

SOURCE: 'source';
TARGET: 'target' ;
QUERIED:  'queried' ;
PRODUCED: 'produced' ;

QIDENTIFIER:  '"' LETTER_FRAG (LETTER_FRAG | DIGIT_FRAG | '.' |'-' | '_' | HEX_FRAG)* '"' ; 
QSTRING:  '"' .*? '"'; 

APPEND: 'append' ;
AS: 'as' ;
CAST: 'cast' ;
C: 'c' ;
CC: 'cc' ;
CODE: 'code' ;
CP: 'cp' ;
CHECK: 'check' ;
CODEABLECONCEPT: 'codeableConcept' ;
CODING: 'coding' ;
COPY: 'copy' ;
CREATE: 'create' ;
DATEOP: 'dateOp' ;
DEFAULT: 'default' ;
DISPLAY: 'display' ;
ENDGROUP : 'endgroup' ;
ESCAPE: 'escape' ;
EVALUATE: 'evaluate' ;
EXTENDS : 'extends' ;
FOR: 'for' ;
GROUP : 'group' ;
ID: 'id' ;
IMPORTS: 'imports' ; 
INPUT: 'input' ;
MAKE: 'make' ;
MAP: 'map' ;
POINTER: 'pointer' ;
QTY: 'qty' ;
REFERENCE: 'reference' ;
SYSTEM : 'system' ;
THEN: 'then' ;
TRANSLATE: 'translate' ;
TRUNCATE: 'truncate' ;
TYPES : 'types' ;
TYPE_TYPES : 'type+types' ;
UUID: 'uuid' ;
USES: 'uses' ;
WHERE: 'where' ;

IDENTIFIER: LETTER_FRAG (LETTER_FRAG | DIGIT_FRAG | '-' | '_' | HEX_FRAG)* ;

WS: [ \t\n\r] -> skip;

LINE_COMMENT: '//' ~[\r\n]* -> skip;