
package net.sourceforge.pmd.lang.xml.ast;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sourceforge.pmd.lang.ast.ParseException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlParser {

    protected Document parseDocument(Reader reader) throws ParseException {
	try {
	    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	    Document document = documentBuilder.parse(new InputSource(reader));
	    return document;
	} catch (ParserConfigurationException e) {
	    throw new ParseException(e);
	} catch (SAXException e) {
	    throw new ParseException(e);
	} catch (IOException e) {
	    throw new ParseException(e);
	}
    }

    public XmlNode parse(Reader reader) {
	Document document = parseDocument(reader);
	return createProxy(document.getDocumentElement());
    }

    public XmlNode createProxy(Node node) {
	
	LinkedHashSet<Class<?>> interfaces = new LinkedHashSet<Class<?>>();
	interfaces.add(XmlNode.class);
	addAllInterfaces(interfaces, node.getClass());

	XmlNode proxy = (XmlNode) Proxy.newProxyInstance(XmlParser.class.getClassLoader(), interfaces
		.toArray(new Class[interfaces.size()]), new XmlNodeInvocationHandler(node));
	return proxy;
    }

    public void addAllInterfaces(Set<Class<?>> interfaces, Class<?> clazz) {
	interfaces.addAll(Arrays.asList((Class<?>[]) clazz.getInterfaces()));
	if (clazz.getSuperclass() != null) {
	    addAllInterfaces(interfaces, clazz.getSuperclass());
	}
    }

    public class XmlNodeInvocationHandler implements InvocationHandler {
	private final Node node;

	public XmlNodeInvocationHandler(Node node) {
	    this.node = node;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	    
	    if (method.getDeclaringClass().isAssignableFrom(XmlNode.class) && !"java.lang.Object".equals(method.getDeclaringClass().getName())) {
		if ("jjtGetNumChildren".equals(method.getName())) {
		    return node.hasChildNodes() ? node.getChildNodes().getLength() : 0;
		} else if ("jjtGetChild".equals(method.getName())) {
		    return createProxy(node.getChildNodes().item(((Integer)args[0]).intValue()));
		} else if ("getImage".equals(method.getName())) {
		    return node.getNodeName();
		} else if ("getBeginLine".equals(method.getName())) {
		    return new Integer(-1);
		} else if ("getBeginColumn".equals(method.getName())) {
		    return new Integer(-1);
		} else if ("getEndLine".equals(method.getName())) {
		    return new Integer(-1);
		} else if ("getEndColumn".equals(method.getName())) {
		    return new Integer(-1);
		}
		throw new UnsupportedOperationException("Method not supported for XmlNode: " + method);
	    }
	    
	    else {
		if ("toString".equals(method.getName())) {
		    if (node instanceof Element) {
			return ((Element)node).getNodeName();
		    }
		}
		Object result = method.invoke(node, args);
		return result;
	    }
	}
    }
}
