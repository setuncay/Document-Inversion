package programmingtest.documentinversion;

import com.google.common.collect.Lists;
import com.google.common.graph.Traverser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.w3c.dom.Node.DOCUMENT_NODE;
import static org.w3c.dom.Node.DOCUMENT_TYPE_NODE;


public class DocumentInverter {
    private final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    public Document invert(String filePath) {
        final File xmlFile = new File(filePath);
        return this.invert(xmlFile);
    }

    public Document invert(File xmlFile) {
        this.verifySourceFile(xmlFile);
        final Document document = parseFile(xmlFile);
        return invert(document);
    }

    public String xmlString(Document document) {
        final StringWriter stringWriter = new StringWriter();
        final Transformer transformer;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
            return stringWriter.toString();
        } catch (TransformerException e) {
            return null;
        }
    }

    private Document invert(Document document) {
       final List<Node> xmlNodes = depthLevelTraversed(document);
       return invertedDocument(xmlNodes);
    }

    private List<Node> depthLevelTraversed(Document document) {
        final Traverser<Node> domTraverser = Traverser.forTree(node -> () -> new NodeIterator(node));
        final Iterable<Node> nodes = domTraverser.breadthFirst(document);

        return
                Lists.reverse(
                        StreamSupport
                                .stream(nodes.spliterator(), false)
                                .filter(node -> node.getNodeType() != Node.TEXT_NODE && node.getNodeType() != DOCUMENT_NODE && node.getNodeType() != DOCUMENT_TYPE_NODE)
                                .collect(Collectors.toList())
                );

    }

    private Document invertedDocument(List<Node> originalNodes) {
        try {
            final Document invertedDocument = factory.newDocumentBuilder().newDocument();
            Node lastInserted = null;
            Node lastProcessed = null;
            for (Node existingNode: originalNodes) {
                Node newNode = invertedDocument.importNode(existingNode, false);
                if (lastInserted == null) {
                    lastInserted = invertedDocument.appendChild(newNode);
                } else if (!lastProcessed.getParentNode().equals(existingNode.getParentNode())) {
                    lastInserted = lastInserted.appendChild(newNode);
                } else {
                    lastInserted = lastInserted.getParentNode().appendChild(newNode);
                }
                lastProcessed = existingNode;
            }
            return invertedDocument;
        } catch (Exception e) {
            throw new IllegalStateException("Unexpected error in creating inverted document");
        }
    }

    private Document parseFile(File xmlFile) {
        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(xmlFile);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new IllegalArgumentException("Error in parsing " + xmlFile.getAbsolutePath());
        }
    }

    private void verifySourceFile(File xmlFile) {
        if (!xmlFile.exists()) {
            throw new IllegalArgumentException("Provided Source File "
                    + xmlFile.getAbsolutePath() + " does not exist!");
        }
    }
}
