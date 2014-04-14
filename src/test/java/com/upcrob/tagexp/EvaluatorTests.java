package com.upcrob.tagexp;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

import java.util.Set;
import java.util.HashSet;

/**
 * JUnit tests for the Evaluator class.
 */
public class EvaluatorTests {
	
	private Evaluator eval;
	private Set<String> tags;
	
	@Before
	public void setup() {
		eval = new Evaluator();
		tags = new HashSet<String>();
		tags.add("a");
		tags.add("b");
		tags.add("c");
		tags.add("d");
		tags.add("y-z");
	}
	
	@Test
	public void testSingleExists() {
		assertTrue(eval.evaluate("a", tags));
	}
	
	@Test
	public void testSingleNotExist() {
		assertFalse(eval.evaluate("e", tags));
	}
	
	@Test
	public void testSpaceAnd() {
		assertTrue(eval.evaluate("a b", tags));
	}
	
	@Test
	public void testKeywordAnd() {
		assertTrue(eval.evaluate("a and b", tags));
	}
	
	@Test
	public void testAndNotTrue() {
		assertFalse(eval.evaluate("a and e", tags));
	}
	
	@Test
	public void testOr() {
		assertTrue(eval.evaluate("a or e", tags));
	}
	
	@Test
	public void testOrNotTrue() {
		assertFalse(eval.evaluate("e or g", tags));
	}
	
	@Test
	public void testXor() {
		assertTrue(eval.evaluate("a xor e", tags));
	}
	
	@Test
	public void testXorTooMany() {
		assertFalse(eval.evaluate("a xor b", tags));
	}
	
	@Test
	public void testXorTooFew() {
		assertFalse(eval.evaluate("e xor g", tags));
	}
	
	@Test
	public void testDashNot() {
		assertTrue(eval.evaluate("-e", tags));
	}
	
	@Test
	public void testKeywordNot() {
		assertTrue(eval.evaluate("not e", tags));
	}
	
	@Test
	public void testNotFalse() {
		assertFalse(eval.evaluate("not a", tags));
	}
	
	@Test
	public void testParens() {
		assertTrue(eval.evaluate("(a)", tags));
	}
	
	@Test
	public void testComplex1() {
		assertTrue(eval.evaluate("(a and e) or b", tags));
	}
	
	@Test
	public void testComplex2() {
		assertTrue(eval.evaluate("a b c d", tags));
	}
	
	@Test
	public void testComplex3() {
		assertTrue(eval.evaluate("(a and b) or (c and e)", tags));
	}
	
	@Test
	public void testComplex4() {
		assertTrue(eval.evaluate("(a and b) and not (e or g)", tags));
	}
	
	@Test
	public void testCaseSensitive() {
		assertFalse(eval.evaluate("A", tags));
	}
	
	@Test
	public void testCaseInsensitive() {
		assertTrue(eval.evaluate("A", tags, false));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullExpression() {
		eval.evaluate(null, tags);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullCollection() {
		eval.evaluate("a or b", null);
	}
	
	@Test(expected=ParseException.class)
	public void testDoubleKeyword() {
		eval.evaluate("a or and b", tags);
	}
	
	@Test(expected=ParseException.class)
	public void testNoOperand() {
		eval.evaluate("or", tags);
	}
	
	@Test(expected=ParseException.class)
	public void testUnexpectedEnding() {
		eval.evaluate("a and", tags);
	}
	
	@Test(expected=ParseException.class)
	public void testUnexpectedStart() {
		eval.evaluate("and b", tags);
	}
	
	@Test
	public void testEmptyCollection() {
		assertFalse(eval.evaluate("a or b", new HashSet<String>()));
	}

	@Test
	public void testInnerDash() {
		assertTrue(eval.evaluate("y-z", tags));
	}

	@Test
	public void testStartingDash() {
		assertTrue(eval.evaluate("a -z", tags));
	}
}
