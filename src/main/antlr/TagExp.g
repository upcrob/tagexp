grammar TagExp;

@header {
	package com.upcrob.tagexp;
}

@lexer::header {
	package com.upcrob.tagexp;
}

@members {
  @Override
  public void emitErrorMessage(String message) {
    throw new ParseException("Parse error: " + message);
  }
}

eval returns [Node value]
	: n=or_exp EOF { $value = $n.value; }
	;

or_exp returns [Node value]
	@init {
		$value = new Node(NodeType.OR);
	}
	: a=xor_exp { $value.children.add($a.value); }
		(op='or' b=xor_exp {
			$value.children.add($b.value);
		})*
	;
	
xor_exp returns [Node value]
	@init {
		$value = new Node(NodeType.XOR);
	}
	: a=and_exp { $value.children.add($a.value); }
		(op=('xor') b=and_exp {
			$value.children.add($b.value);
		})*
	;

and_exp returns [Node value]
	@init {
		$value = new Node(NodeType.AND);
	}
	: a=not_exp {
		$value.children.add($a.value);
	} ('and'? b=not_exp {
		$value.children.add($b.value);
	} )*
	;

not_exp returns [Node value]
	: s=STR {
		$value = new Node(NodeType.TERM);
		$value.value = $s.text;
	}
	| ('not'|'-') s=STR {
		$value = new Node(NodeType.NOT);
		Node term = new Node(NodeType.TERM);
		term.value = $s.text;
		$value.children.add(term);
	}
	| ('not'|'-') '(' n=or_exp ')' {
		$value = new Node(NodeType.NOT);
		$value.children.add($n.value);
	}
	| '(' n=or_exp ')' {
		$value = $n.value;
	}
	;

STR
	:	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-')*
	;

WS 
	:   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
        ;

