package programmingtest.documentinversion;

import org.w3c.dom.Node;

import java.util.Iterator;

public class NodeIterator implements Iterator<Node> {
    private final Node node;
    private int currentChildIndex;
    public NodeIterator(Node node) {
        this.node = node;
        this.currentChildIndex = 0;
    }

    @Override
    public boolean hasNext() {
        return this.currentChildIndex < node.getChildNodes().getLength();
    }

    @Override
    public Node next() {
        return this.node.getChildNodes().item(currentChildIndex++);
    }
}
