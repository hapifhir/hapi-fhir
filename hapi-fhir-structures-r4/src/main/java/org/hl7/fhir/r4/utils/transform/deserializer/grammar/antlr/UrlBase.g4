/**
 * Define a grammar called FhirMapper
 */
grammar UrlBase;

/*
 * URL Definition. Modified from 
 * https://github.com/antlr/grammars-v4/blob/master/url/url.g4
 */

fragmentaddress
   : uri ('#' fragmentid)? WS?
   ;

uri
   : url
   ;

url
   : authority '://' login? host (':' port)? ('/' path)? ('?' search)?
   ;

authority
   : STRING
   ;

host
   : hostname
   | hostnumber
   ;

hostname
   : stringVal ('.' stringVal)*
   ;

hostnumber
   : DIGITS '.' DIGITS '.' DIGITS '.' DIGITS
   ;

port
   : DIGITS
   ;

path
   : stringVal ('/' stringVal)*
   ;

search
   : searchParameter ('&' searchParameter)*
   ;

searchParameter
    : searchParameterName ('=' searchParameterValue)?
	;

searchParameterName
	: STRING
	;

searchParameterValue
	: STRING | DIGITS | HEX
	;

user
   : STRING
   ;

login
    : user ':' password '@'
    ;

password
   : STRING
   ;

fragmentid
   : stringVal
   ;

stringVal
	: STRING
	;

HEX
    : ('%' [a-fA-F0-9] [a-fA-F0-9])+
    ;

STRING
   : ([a-zA-Z~] |HEX) ([a-zA-Z0-9.-] | HEX)*
   ;

DIGITS
   : [0-9] +
   ;


WS
   : [\r\n] +
   ;
