
package net.sourceforge.pmd.lang.java.rule;

import net.sourceforge.pmd.lang.ast.Node;

public class ImportWrapper {
    private Node node;
    private String name;
    private String fullname;

    public ImportWrapper(String fullname, String name, Node node) {
        this.fullname = fullname;
        this.name = name;
        this.node = node;
    }


    public boolean equals(Object other) {
        ImportWrapper i = (ImportWrapper) other;
        if(name == null && i.getName() == null){
            return i.getFullName().equals(fullname);
        }
        return i.getName().equals(name);
    }

    public int hashCode() {
        if(name == null){
            return fullname.hashCode();
        }
        return name.hashCode();
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullname;
    }

    public Node getNode() {
        return node;
    }
}

