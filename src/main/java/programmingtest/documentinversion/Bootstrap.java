package programmingtest.documentinversion;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.w3c.dom.Document;

import java.io.IOException;

import static java.lang.System.exit;

@SpringBootApplication
public class Bootstrap implements CommandLineRunner {
    public static void main(String[] args) {
        final SpringApplication app = new SpringApplication(Bootstrap.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) {
        if (args.length != 1) {
            System.out.println("You must provide the path to your program file as an argument");
            exit(1);
        } else {
            try {
                final DocumentInverter documentInverter = new DocumentInverter();
                final Document document = documentInverter.invert(args[0]);
                print(document);
            } catch (Exception ex) {
                System.out.println("Error in Running Document Inversion Program:");
                System.out.println(ex.getMessage());
            }
        }
    }

    void print(Document document) {
        OutputFormat format = new OutputFormat(document);
        format.setIndenting(true);
        format.setOmitXMLDeclaration(true);
        XMLSerializer serializer = new XMLSerializer(System.out, format);
        try {
            serializer.serialize(document);
        } catch (IOException ex) {
            System.out.println("Error in Running Document Inversion Program:");
            System.out.println(ex.getMessage());
        }
    }
}
