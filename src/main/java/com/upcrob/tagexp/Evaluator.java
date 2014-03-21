package com.upcrob.tagexp;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

import java.util.*;

/**
 * An Evaluator contains facilities for evaluating the truth value of tag
 * expressions in relation to an input Collection of tags.
 */
public class Evaluator {
	
	/**
	 * Evaluates a tag expression against an input Collection of Strings
	 * to determine its truth value.
	 *
	 * For instance, the expression, "(a or b) and not c" should return
	 * true when the input Collection consists of {"a", "d", "e"} since
	 * it contains the String, "a" ("b" would also have worked) but not
	 * the String, "c".
	 *
	 * @param expression Input expression String.
	 * @param tags Input Collection of tag Strings.
	 * @throws ParseException Thrown if the expression could not be parsed.
	 */
	public boolean evaluate(String expression, Collection<String> tags) {
		// Setup lexer and parser
		SetExpLexer lexer = new SetExpLexer(new ANTLRStringStream(expression));
		SetExpParser parser = new SetExpParser(new CommonTokenStream(lexer));
		
		// Build parse tree
		Node root;
		try {
			root = parser.eval();
		} catch (RecognitionException e) {
			throw new ParseException("Invalid expression.", e);
		}

		// Evaluate tree
		return eval(root, new HashSet<String>(tags));
	}

	/**
	 * Helper method that does most of the evaluation work.
	 */
	private boolean eval(Node root, Set<String> tags) {
		switch (root.type) {
			case OR:
				// Do a logical OR on all children of this node
				for (Node n : root.children) {
					if (eval(n, tags)) {
						// Return true if any of the
						// sub-trees are true
						return true;
					}
				}
				return false;
			case XOR:
				// Do a logical XOR on all children of this node
				boolean found = false;
				for (Node n : root.children) {
					if (eval(n, tags)) {
						if (found) {
							// Sub-tree already evaluated to
							// true, return false
							return false;
						}
						found = true;
					}
				}
				return found;
			case AND:
				// Do a logical AND on all children of this node
				for (Node n : root.children) {
					if (!eval(n, tags)) {
						// Return false if any of the
						// sub-trees are false
						return false;
					}
				}
				return true;
			case NOT:
				// Do a logical NOT on this node's sub-tree
				Node child = root.children.get(0);
				return !eval(child, tags);
			case TERM:
				// Determine if this term is in the tag set
				return tags.contains(root.value);
		}
		throw new ParseException("Invalid parse tree.");
	}
}
