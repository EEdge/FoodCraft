package sfsu.csc413.foodcraft;

/**
 * This class sets and gets the parsed xml response to usable objects
 * @author Maria Lienkaemper
 */
public class GlossaryData {

    String term = null;
    String definition = null;
    String glossaryID = null;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDefinition() {return definition;}

    public void setDefinition(String definition) {this.definition = definition;}

    public String getGlossaryEntryID(){return glossaryID;}

    public void setGlossaryEntryID(String glossaryID){
        this.glossaryID = glossaryID;
    }
}
