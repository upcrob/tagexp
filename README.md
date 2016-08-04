# Overview

Tagexp is a very simple utility library (a single class, really) that can be used
to evaluate tag expressions on a collection of strings.  For example, suppose that
you have a collection of documents where each has a set of tags associated with it.
A document about Minnesota sports, for example, might be tagged with, "Minnesota",
"Sports", "Vikings", and "Twins", among others.

Now, suppose that you want to make complex search available to your end users and
create queries like, "find all documents that include the terms 'Minnesota' and
'Vikings', but not 'Sports'."  The Tagexp library makes this possible by evaluating
the truth value of such queries against collections of tag strings.

## Usage

Suppose that the document mentioned above has its tags stored in a Java Set called,
`tags`.  We can run the following query against it to determine if it includes
the tags 'Minnesota' and 'Vikings' but not 'Sports':

	Evaluator eval = new Evaluator();	// Get an Evaluator instance
	boolean isAboutHistoricalVikings = eval.evaluate("(Minnesota and Vikings) and not Sports", tags);
	
The `Evaluator` class supports the following operators.  Note that these are case-sensitive:

* and - Logical AND.
* or - Logical OR.
* not - Logical NOT.  The "-" character also functions as a logical NOT.
* xor - Logical XOR.

Sub-expressions can be grouped using parentheses.  All other strings are treated
as individual tags.  Note that if multiple tags are separated by spaces in an expression,
they are treated as if they had an AND operator between them.

By default, tag searches are case-sensitive.  Setting the 'caseSensitive' parameter
on the `evaluate()` method to false will cause searching to be case-insensitive.
Note that doing so may reduce the performance of the evaluation.

## Building

Gradle is used to build the Tagexp JAR:

	./gradlew build

## Dependencies

In order to use Tagexp in another program or build, the ANLTR 3 JAR will need to be
included on the classpath.  To add this JAR to a Gradle build from Maven Central,
simply include the following dependency in your build script:

	compile 'org.antlr:antlr:3.5.2'
