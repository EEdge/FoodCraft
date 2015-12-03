package sfsu.csc413.foodcraft;

/**
 * http://www.mysamplecode.com/2011/11/android-parse-xml-file-example-using.html
 */
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GlossaryXmlHandler extends DefaultHandler {

    Boolean currentElement = false;
    String currentValue = "";
    GlossaryData entry = null;

    // Called when tag starts
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        currentElement = true;
        currentValue = "";
        if (localName.equals("GlossaryEntry")) {
            entry = new GlossaryData();
        }

    }

    // Called when tag closing
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        currentElement = false;

        /** set value */
        if (localName.equalsIgnoreCase("Term"))
            entry.setTerm(currentValue);
        else if (localName.equalsIgnoreCase("Definition"))
            entry.setDefinition(currentValue);
    }

    // Called to get tag characters
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        if (currentElement) {
            currentValue = currentValue +  new String(ch, start, length);
        }

    }

}

