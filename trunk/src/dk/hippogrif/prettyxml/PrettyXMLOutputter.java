/*
    Copyright (C) 2005 Jesper Goertz
    All Rights Reserved, http://hippogrif.dk/sw/prettyxml
 
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.
 
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 
    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package dk.hippogrif.prettyxml;

import java.io.*;
import java.util.*;

import org.jdom.output.*;
import org.jdom.*;

/**
 * An extension to JDOM's {@link org.jdom.output.XMLOutputter XMLOutputter} providing attribute sorting and indentation.<p>
 * This code is not threadsafe, indentation level is handled with a private member.<p>
 * Private methods copied from JDOM 1.0, XMLOutputter.java revision 1.112
 *
 * @author Jesper Goertz
 */
public class PrettyXMLOutputter extends XMLOutputter {
    private boolean sortAttributes;
    private boolean indentAttributes;
    private int level;
    
    /** Creates a new instance of PrettyXMLOutputter */
    public PrettyXMLOutputter() {
        super();
    }
    
    /**
     * This will create a <code>PrettyXMLOutputter</code> with the specified
     * format characteristics.  Note the format object is cloned internally
     * before use.
     *
     * @param format the format to clone
     */
    public PrettyXMLOutputter(Format format) {
        super(format);
    }
    
    /**
     * This will create a <code>PrettyXMLOutputter</code> with all the
     * options as set in the given <code>XMLOutputter</code>.
     *
     * @param that an XMLOutputter
     */
    public PrettyXMLOutputter(XMLOutputter that) {
        super(that);
    }
    
    /**
     * This will create a <code>PrettyXMLOutputter</code> with all the
     * options as set in the given <code>PrettyXMLOutputter</code>.
     *
     * @param that a PrettyXMLOutputter
     */
    public PrettyXMLOutputter(PrettyXMLOutputter that) {
        super(that);
        sortAttributes = that.sortAttributes;
        indentAttributes = that.indentAttributes;
    }
    
    /**
     * Controls sorting of attributes, default is no sorting.
     *
     * @param sortAttributes true iff attributes shall be sorted
     */
    public void setSortAttributes(boolean sortAttributes) {
        this.sortAttributes = sortAttributes;
    }
    
    public boolean getSortAttributes() {
        return sortAttributes;
    }

    /**
     * Controls indentation of attributes, default is no indentation.
     *
     * @param indentAttributes true iff attributes shall be indented
     */
    public void setIndentAttributes(boolean indentAttributes) {
        this.indentAttributes = indentAttributes;
    }
    
    public boolean getIndentAttributes() {
        return indentAttributes;
    }
    
    protected void printElement(Writer out, Element element,
            int level, NamespaceStack namespaces)
            throws IOException {
        this.level = level;
        super.printElement(out, element, level, namespaces);
    }
    
    protected void printAttributes(Writer out, List attributes, Element parent,
            NamespaceStack namespaces)
            throws IOException {
        if (sortAttributes) {
            TreeMap map = new TreeMap();
            for (int i = 0; i < attributes.size(); i++) {
                Attribute attribute = (Attribute) attributes.get(i);
                map.put(attribute.getQualifiedName(), attribute);
            }
            attributes = new ArrayList(map.values());
        }
        if (indentAttributes && attributes.size()>0 && 
                currentFormat.getIndent() != null && !currentFormat.getIndent().equals("")) {
            for (int i = 0; i < attributes.size(); i++) {
                newline(out);
                indent(out, level+1);
                Attribute attribute = (Attribute) attributes.get(i);
                Namespace ns = attribute.getNamespace();
                if ((ns != Namespace.NO_NAMESPACE) &&
                        (ns != Namespace.XML_NAMESPACE)) {
                    printNamespace(out, ns, namespaces);
                }
                
                out.write(" ");
                printQualifiedName(out, attribute);
                out.write("=");
                
                out.write("\"");
                out.write(escapeAttributeEntities(attribute.getValue()));
                out.write("\"");
            }
            newline(out);
            indent(out, level);
            List content = parent.getContent();
            int start = skipLeadingWhite(content, 0);
            int size = content.size();
            if (start<size && level>0) out.write(" ");
        } else super.printAttributes(out, attributes, parent, namespaces);
    }
    
    private void printNamespace(Writer out, Namespace ns,
            NamespaceStack namespaces)
            throws IOException {
        String prefix = ns.getPrefix();
        String uri = ns.getURI();
        
        // Already printed namespace decl?
        if (uri.equals(namespaces.getURI(prefix))) {
            return;
        }
        
        out.write(" xmlns");
        if (!prefix.equals("")) {
            out.write(":");
            out.write(prefix);
        }
        out.write("=\"");
        out.write(uri);
        out.write("\"");
        namespaces.push(ns);
    }
    
    private void printQualifiedName(Writer out, Attribute a) throws IOException {
        String prefix = a.getNamespace().getPrefix();
        if ((prefix != null) && (!prefix.equals(""))) {
            out.write(prefix);
            out.write(':');
            out.write(a.getName());
        } else {
            out.write(a.getName());
        }
    }
    
    private void indent(Writer out, int level) throws IOException {
        if (level <= 0) return;
        for (int i = 0; i < level-1; i++) {
            out.write(currentFormat.getIndent());
        }
        String indent = currentFormat.getIndent().substring(0, currentFormat.getIndent().length()-1);
        out.write(indent);
    }
    
    public void output(Document doc, Writer out) throws IOException {

        printDeclaration(out, doc, currentFormat.getEncoding());

        // Print out root element, as well as any root level
        // comments and processing instructions,
        // starting with no indentation
        List content = doc.getContent();
        int size = content.size();
        for (int i = 0; i < size; i++) {
            Object obj = content.get(i);

            if (obj instanceof Element) {
                printElement(out, doc.getRootElement(), 0, createNamespaceStack());
            }
            else if (obj instanceof Comment) {
                printComment(out, (Comment) obj);
            }
            else if (obj instanceof ProcessingInstruction) {
                printProcessingInstruction(out, (ProcessingInstruction) obj);
            }
            else if (obj instanceof DocType) {
                printDocType(out, doc.getDocType());
                // Always print line separator after declaration, helps the
                // output look better and is semantically inconsequential
                out.write(currentFormat.getLineSeparator());
            }
            else {
                // XXX if we get here then we have a illegal content, for
                //     now we'll just ignore it
            }

            newline(out);
            indent(out, 0);
        }

        // Output final line separator unless already done
        // We output this no matter what the newline flags say
        if (size > 0 && currentFormat.getIndent() == null) {
            out.write(currentFormat.getLineSeparator());
        }

        out.flush();
    }

    private void newline(Writer out) throws IOException {
        if (currentFormat.getIndent() != null) {
            out.write(currentFormat.getLineSeparator());
        }
    }
    
    private NamespaceStack createNamespaceStack() {
       // actually returns a XMLOutputter.NamespaceStack (see below)
       return new MyNamespaceStack();
    }

    protected class MyNamespaceStack
        extends NamespaceStack
    {
    }

    private int skipLeadingWhite(List content, int start) {
        if (start < 0) {
            start = 0;
        }

        int index = start;
        int size = content.size();
        if (currentFormat.getTextMode() == Format.TextMode.TRIM_FULL_WHITE
                || currentFormat.getTextMode() == Format.TextMode.NORMALIZE
                || currentFormat.getTextMode() == Format.TextMode.TRIM) {
            while (index < size) {
                if (!isAllWhitespace(content.get(index))) {
                    return index;
                }
                index++;
            }
        }
        return index;
    }

    private boolean isAllWhitespace(Object obj) {
        String str = null;

        if (obj instanceof String) {
            str = (String) obj;
        }
        else if (obj instanceof Text) {
            str = ((Text) obj).getText();
        }
        else if (obj instanceof EntityRef) {
            return false;
        }
        else {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            if (!isWhitespace(str.charAt(i)))
                return false;
        }
        return true;
    }

    private static boolean isWhitespace(char c) {
        if (c==' ' || c=='\n' || c=='\t' || c=='\r' ){
            return true;
        }
        return false;
    }
}
