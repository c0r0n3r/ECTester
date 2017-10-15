package cz.crcs.ectester.reader.output;

import cz.crcs.ectester.reader.Util;
import cz.crcs.ectester.reader.response.Response;
import cz.crcs.ectester.reader.test.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.OutputStream;

/**
 * @author Jan Jancar johny@neuromancer.sk
 */
public class XMLOutputWriter implements OutputWriter {
    private OutputStream output;
    private Document doc;
    private Node root;

    public XMLOutputWriter(OutputStream output) throws ParserConfigurationException {
        this.output = output;
        this.doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    }

    @Override
    public void begin() {
        root = doc.createElement("testRun");
        doc.appendChild(root);
    }

    private Element responseElement(Response r) {
        Element responseElem = doc.createElement("response");
        responseElem.setAttribute("successful", r.successful() ? "true" : "false");

        Element apdu = doc.createElement("apdu");
        apdu.setTextContent(Util.bytesToHex(r.getAPDU().getBytes()));
        responseElem.appendChild(apdu);

        Element naturalSW = doc.createElement("natural-sw");
        naturalSW.setTextContent(String.valueOf(r.getNaturalSW()));
        responseElem.appendChild(naturalSW);

        Element sws = doc.createElement("sws");
        for (int i = 0; i < r.getNumSW(); ++i) {
            Element sw = doc.createElement("sw");
            sw.setTextContent(String.valueOf(r.getSW(i)));
            sws.appendChild(sw);
        }
        responseElem.appendChild(sws);

        Element duration = doc.createElement("duration");
        duration.setTextContent(String.valueOf(r.getDuration()));
        responseElem.appendChild(duration);

        Element description = doc.createElement("desc");
        description.setTextContent(r.getDescription());
        responseElem.appendChild(description);

        return responseElem;
    }

    @Override
    public void printResponse(Response r) {
        root.appendChild(responseElement(r));
    }

    private Element testElement(Test t) {
        Element testElem = doc.createElement("test");

        if (t instanceof Test.Simple) {
            Test.Simple test = (Test.Simple) t;
            testElem.setAttribute("type", "simple");
            testElem.appendChild(responseElement(test.getResponse()));
        } else if (t instanceof Test.Compound) {
            Test.Compound test = (Test.Compound) t;
            testElem.setAttribute("type", "compound");
            for (Test innerTest : test.getTests()) {
                testElem.appendChild(testElement(innerTest));
            }
        }

        Element description = doc.createElement("desc");
        description.setTextContent(t.getDescription());
        testElem.appendChild(description);

        Element result = doc.createElement("result");
        result.setTextContent(t.getResult().toString());
        testElem.appendChild(result);

        return testElem;
    }

    @Override
    public void printTest(Test t) {
        if (!t.hasRun())
            return;
        root.appendChild(testElement(t));
    }

    @Override
    public void end() {
        try {
            DOMSource domSource = new DOMSource(doc);
            StreamResult result = new StreamResult(output);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}