// antlr4 -Dlanguage=Java -o parser Calc.g4

grammar Calc;

MUL : '*';
DIV : '/';
ADD : '+';
SUB : '-';

NUMBER : [0-9]+ ;

WHITESPACE : [ \r\n\t]+ -> skip;

start : expression EOF;

expression
    : expression op=('*' | '/') expression  # MulOrDiv
    | expression op=('+' | '-') expression  # AddOrSub
    | NUMBER                                # Number
    ;
