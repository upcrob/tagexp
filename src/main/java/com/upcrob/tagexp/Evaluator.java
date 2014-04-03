package com.upcrob.tagexp;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

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
		// Evaluate, case-sensitive
		return evaluate(expression, tags, true);
	}
	
	/**
	 * Evaluates a tag expression against an input Collection of Strings
	 * to determine its truth value.
	 *
	 * @param expression Input expression String.
	 * @param tags Input Collection of tag Strings.
	 * @param caseSensitive Toggles case-sensitivity of tag-search.  Note that
	 *   a case-insensitive search may be slower.
	 * @throws ParseException Thrown if the expression could not be parsed.
	 */
	public boolean evaluate(String expression, Collection<String> tags, boolean caseSensitive) {
		// Verify arguments are non-null
		if (expression == null || tags == null) {
			throw new IllegalArgumentException("Input arguments must be non-null.");
		}
		
		// Setup lexer and parser
		TagExpLexer lexer = new TagExpLexer(new ANTLRStringStream(expression));
		TagExpParser parser = new TagExpParser(new CommonTokenStream(lexer));
		
		// Build parse tree
		Node root;
		try {
			root = parser.eval();
		} catch (RecognitionException e) {
			throw new ParseException("Invalid expression.", e);
		}

		// Evaluate tree
		return eval(root, new HashSet<String>(tags), caseSensitive);
	}

	/**
	 * Helper method that does most of the evaluation work.
	 */
	private boolean eval(Node root, Set<String> tags, boolean caseSensitive) {
		switch (root.type) {
			case OR:
				// Do a logical OR on all children of this node
				for (Node n : root.children) {
					if (eval(n, tags, caseSensitive)) {
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
					if (eval(n, tags, caseSensitive)) {
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
					if (!eval(n, tags, caseSensitive)) {
						// Return false if any of the
						// sub-trees are false
						return false;
					}
				}
				return true;
			case NOT:
				// Do a logical NOT on this node's sub-tree
				Node child = root.children.get(0);
				return !eval(child, tags, caseSensitive);
			case TERM:
				// Determine if this term is in the tag set
				if (caseSensitive) {
					// Case-sensitive search
					return tags.contains(root.value);
				} else {
					// Case-insensitive search
					for (String tag : tags) {
						if (tag.equalsIgnoreCase(root.value)) {
							return true;
						}
					}
					return false;
				}
		}
		throw new ParseException("Invalid parse tree.");
	}
}
