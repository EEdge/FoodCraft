package sfsu.csc413.foodcraft;

/**
 * This class handles the xml response from BigOven
 * Uses code from: http://www.mysamplecode.com/2011/11/android-parse-xml-file-example-using.html
 * @author Maria Lienkaemper
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

        // set value
        if (localName.equalsIgnoreCase("Term"))
            entry.setTerm(currentValue);
        else if (localName.equalsIgnoreCase("Definition"))
            entry.setDefinition(currentValue);
        else if (localName.equalsIgnoreCase("GlossaryEntryID"))
            entry.setGlossaryEntryID(currentValue);
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

