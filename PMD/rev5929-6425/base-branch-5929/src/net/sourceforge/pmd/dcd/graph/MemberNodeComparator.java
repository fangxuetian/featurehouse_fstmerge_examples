
package net.sourceforge.pmd.dcd.graph;

import java.util.Comparator;


public class MemberNodeComparator implements Comparator<MemberNode> {

	public static final MemberNodeComparator INSTANCE = new MemberNodeComparator();

	private MemberNodeComparator() {
	}

	public int compare(MemberNode node1, MemberNode node2) {
		if (node1 instanceof FieldNode) {
			if (node2 instanceof FieldNode) {
				return node1.compareTo(node2);
			} else {
				return -1;
			}
		} else if (node1 instanceof ConstructorNode) {
			if (node2 instanceof FieldNode) {
				return 1;
			} else if (node2 instanceof ConstructorNode) {
				return node1.compareTo(node2);
			} else {
				return -1;
			}
		} else {
			if (node2 instanceof MethodNode) {
				return node1.compareTo(node2);
			} else {
				return 1;
			}
		}
	}
}
