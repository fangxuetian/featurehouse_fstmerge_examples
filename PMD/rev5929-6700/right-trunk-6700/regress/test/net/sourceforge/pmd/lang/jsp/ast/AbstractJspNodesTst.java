package test.net.sourceforge.pmd.lang.jsp.ast;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.pmd.lang.ast.JavaCharStream;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.jsp.ast.JspParser;

public abstract class AbstractJspNodesTst {

    public <T> void assertNumberOfNodes(Class<T> clazz, String source, int number) {
        Set<T> nodes = getNodes(clazz, source);
        assertEquals("Exactly " + number + " element(s) expected", number, nodes.size());
    }

    
    public <T> Set<T> getNodes(Class<T> clazz, String source) {
        JspParser parser = new JspParser(new JavaCharStream(new StringReader(source)));
        Node rootNode = parser.CompilationUnit();
        Set<T> nodes = new HashSet<T>();
        addNodeAndSubnodes(rootNode, nodes, clazz);
        return nodes;
    }

    
    public <T> Set<T> getNodesOfType(Class<T> clazz, Set allNodes) {
        Set<T> result = new HashSet<T>();
        for (Object node: allNodes) {
            if (clazz.equals(node.getClass())) {
                result.add((T)node);
            }
        }
        return result;
    }

    
    private <T> void addNodeAndSubnodes(Node node, Set<T> nodes, Class<T> clazz) {
        if (null != node) {
            if ((null == clazz) || (clazz.equals(node.getClass()))) {
                nodes.add((T)node);
            }
        }
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            addNodeAndSubnodes(node.jjtGetChild(i), nodes, clazz);
        }
    }

}
