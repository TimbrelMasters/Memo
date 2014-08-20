package memo.model;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 * @author Илья
 */

public class CardSetMarshaller {

    private File outputXML;
    private JAXBContext jaxbContext;
    private Marshaller jaxbMarshaller;

    public CardSetMarshaller() {
        try {
            jaxbContext = JAXBContext.newInstance(CardSet.class);
            jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        }
        catch (JAXBException ex) {
            Logger.getLogger(CardSetMarshaller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setOutputXML(String output) {
        this.outputXML = new File(output);
    }

    public void setOutputXML(File output) {
        this.outputXML = output;
    }

    public File getOutputXML() {
        return this.outputXML;
    }

    public void marshallCardSet(CardSet cardSet) {
        try {
            jaxbMarshaller.marshal(cardSet, this.outputXML);
        } catch (JAXBException e) {
        }
    }
}
//class Test {
//    public static void main(String[] args) {
//        CardSetMarshaller cardSetMarshaller = new CardSetMarshaller();
//        cardSetMarshaller.setOutputXML("C:\\Temp\\out.xml");
//        CardSet cardSet = new CardSet("Present Simple");
//        cardSet.addCard(new Card("do"));
//        cardSet.addCard(new Card("foo"));
//        cardSet.addCard(new Card("goo"));
//        cardSetMarshaller.marshallCardSet(cardSet);
//
//    }
//}
