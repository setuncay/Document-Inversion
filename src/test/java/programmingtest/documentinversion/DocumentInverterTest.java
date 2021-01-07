package programmingtest.documentinversion;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

public class DocumentInverterTest {
    private DocumentInverter inverter;

    @Before
    public void setUp() {
        this.inverter = new DocumentInverter();
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsErrorWhenGivenSourceFilePathIsInvalid() {
        // Given
        final String invalidFilePath = "thisSourceFileDoesnotExist.xml";
        // When
        inverter.invert(invalidFilePath);
    }

    @Test
    public void canRevertSimpleXml1() throws FileNotFoundException {
        // Given
        File xmlFile = ResourceUtils.getFile("classpath:test1.xml");
        // When
        final Document document = inverter.invert(xmlFile);
        final String invertedDocumentString = inverter.xmlString(document);
        assertEquals("<c><b><a/></b></c>", invertedDocumentString);
    }

    @Test
    public void canRevertSimpleXml2() throws FileNotFoundException {
        // Given
        File xmlFile = ResourceUtils.getFile("classpath:test2.xml");
        // When
        final Document document = inverter.invert(xmlFile);
        final String invertedDocumentString = inverter.xmlString(document);
        assertEquals("<d><b/><c><a/></c></d>", invertedDocumentString);
    }

    @Test(expected = IllegalStateException.class)
    public void xmlWithMultipleLeafNodesAreInvalid() throws FileNotFoundException {
        // Given
        File xmlFile = ResourceUtils.getFile("classpath:invalid.xml");
        // When
        final Document document = inverter.invert(xmlFile);
    }
}
