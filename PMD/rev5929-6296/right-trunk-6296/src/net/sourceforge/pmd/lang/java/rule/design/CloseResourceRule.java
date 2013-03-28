
package net.sourceforge.pmd.lang.java.rule.design;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import net.sourceforge.pmd.PropertyDescriptor;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTBlock;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.ast.ASTPrimarySuffix;
import net.sourceforge.pmd.lang.java.ast.ASTReferenceType;
import net.sourceforge.pmd.lang.java.ast.ASTReturnStatement;
import net.sourceforge.pmd.lang.java.ast.ASTStatementExpression;
import net.sourceforge.pmd.lang.java.ast.ASTTryStatement;
import net.sourceforge.pmd.lang.java.ast.ASTType;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.rule.properties.StringProperty;


public class CloseResourceRule extends AbstractJavaRule {

    private Set<String> types = new HashSet<String>();

    private Set<String> closeTargets = new HashSet<String>();
    private static final PropertyDescriptor closeTargetsDescriptor = new StringProperty("closeTargets",
            "Methods which may close this resource", "", 1.0f);

    private static final PropertyDescriptor typesDescriptor = new StringProperty("types",
            "Types that are affected by this rule", "", 2.0f);

    private static final Map<String, PropertyDescriptor> propertyDescriptorsByName = asFixedMap(new PropertyDescriptor[] { typesDescriptor, closeTargetsDescriptor });

    @Override
    protected Map<String, PropertyDescriptor> propertiesByName() {
        return propertyDescriptorsByName;
    }

    @Override
    public Object visit(ASTCompilationUnit node, Object data) {
        if (closeTargets.isEmpty() && getStringProperty(closeTargetsDescriptor) != null) {
            for (StringTokenizer st = new StringTokenizer(getStringProperty(closeTargetsDescriptor), ","); st.hasMoreTokens();) {
                closeTargets.add(st.nextToken());
            }
        }
        if (types.isEmpty() && getStringProperty(typesDescriptor) != null) {
            for (StringTokenizer st = new StringTokenizer(getStringProperty(typesDescriptor), ","); st.hasMoreTokens();) {
                types.add(st.nextToken());
            }
        }
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTMethodDeclaration node, Object data) {
        List<ASTLocalVariableDeclaration> vars = node.findDescendantsOfType(ASTLocalVariableDeclaration.class);
        List<ASTVariableDeclaratorId> ids = new ArrayList<ASTVariableDeclaratorId>();

        
        for (ASTLocalVariableDeclaration var: vars) {
            ASTType type = var.getTypeNode();

            if (type.jjtGetChild(0) instanceof ASTReferenceType) {
                ASTReferenceType ref = (ASTReferenceType) type.jjtGetChild(0);
                if (ref.jjtGetChild(0) instanceof ASTClassOrInterfaceType) {
                    ASTClassOrInterfaceType clazz = (ASTClassOrInterfaceType) ref.jjtGetChild(0);
                    if (types.contains(clazz.getImage())) {
                        ASTVariableDeclaratorId id = (ASTVariableDeclaratorId) var.jjtGetChild(1).jjtGetChild(0);
                        ids.add(id);
                    }
                }
            }
        }

        
        for (ASTVariableDeclaratorId x : ids) {
            ensureClosed((ASTLocalVariableDeclaration) x.jjtGetParent().jjtGetParent(), x, data);
        }
        return data;
    }

    private void ensureClosed(ASTLocalVariableDeclaration var,
                              ASTVariableDeclaratorId id, Object data) {
        
        
        String variableToClose = id.getImage();
        String target = variableToClose + ".close";
        Node n = var;

        while (!(n instanceof ASTBlock)) {
            n = n.jjtGetParent();
        }

        ASTBlock top = (ASTBlock) n;

        List<ASTTryStatement> tryblocks = top.findDescendantsOfType(ASTTryStatement.class);

        boolean closed = false;

        
        
        
        for (ASTTryStatement t : tryblocks) {
            if (t.getBeginLine() > id.getBeginLine() && t.hasFinally()) {
                ASTBlock f = (ASTBlock) t.getFinally().jjtGetChild(0);
                List<ASTName> names = f.findDescendantsOfType(ASTName.class);
                for (ASTName oName : names) {
                    String name = oName.getImage();
                    if (name.equals(target)) {
                        closed = true;
                        break;
                    }
                }
                if (closed) {
                    break;
                }

                List<ASTStatementExpression> exprs = new ArrayList<ASTStatementExpression>();
                f.findDescendantsOfType(ASTStatementExpression.class, exprs, true);
                for (ASTStatementExpression stmt : exprs) {
                    ASTPrimaryExpression expr =
                        stmt.getFirstChildOfType(ASTPrimaryExpression.class);
                    if (expr != null) {
                        ASTPrimaryPrefix prefix = expr.getFirstChildOfType(ASTPrimaryPrefix.class);
                        ASTPrimarySuffix suffix = expr.getFirstChildOfType(ASTPrimarySuffix.class);
                        if ((prefix != null) && (suffix != null)) {
                            if (prefix.getImage() == null) {
                                ASTName prefixName = prefix.getFirstChildOfType(ASTName.class);
                                if ((prefixName != null)
                                        && closeTargets.contains(prefixName.getImage()))
                                {
                                    
                                    
                                    closed = variableIsPassedToMethod(expr, variableToClose);
                                    if (closed) {
                                        break;
                                    }
                                }
                            } else if (suffix.getImage() != null) {
                                String prefixPlusSuffix =
                                        prefix.getImage()+ "." + suffix.getImage();
                                if (closeTargets.contains(prefixPlusSuffix)) {
                                    
                                    
                                    closed = variableIsPassedToMethod(expr, variableToClose);
                                    if (closed) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if (closed) {
                    break;
                }
            }
        }

        if (!closed) {
            
            
            
            List<ASTReturnStatement> returns = new ArrayList<ASTReturnStatement>();
            top.findDescendantsOfType(ASTReturnStatement.class, returns, true);
            for (ASTReturnStatement returnStatement : returns) {
                ASTName name = returnStatement.getFirstChildOfType(ASTName.class);
                if ((name != null) && name.getImage().equals(variableToClose)) {
                    closed = true;
                    break;
                }
            }
        }

        
        if (!closed) {
            ASTType type = (ASTType) var.jjtGetChild(0);
            ASTReferenceType ref = (ASTReferenceType) type.jjtGetChild(0);
            ASTClassOrInterfaceType clazz = (ASTClassOrInterfaceType) ref.jjtGetChild(0);
            addViolation(data, id, clazz.getImage());
        }
    }

    private boolean variableIsPassedToMethod(ASTPrimaryExpression expr, String variable) {
        List<ASTName> methodParams = new ArrayList<ASTName>();
        expr.findDescendantsOfType(ASTName.class, methodParams, true);
        for (ASTName pName : methodParams) {
            String paramName = pName.getImage();
            if (paramName.equals(variable)) {
                return true;
            }
        }
        return false;
    }
}