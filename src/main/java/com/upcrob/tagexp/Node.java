package com.upcrob.tagexp;

import java.util.List;
import java.util.ArrayList;

/**
 * Describes a parse tree node.
 */
class Node {
	final NodeType type;
	List<Node> children;
	String value;

	Node(NodeType type) {
		this.type = type;
		children = new ArrayList<Node>(1);
		value = null;
	}
}
