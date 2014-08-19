
package memo.events;


public class SectionRemovedEvent extends ModelChangedEvent {

    private int sectionNumber;
    
    public SectionRemovedEvent() {
        super();
    }

    public SectionRemovedEvent(int i) {
        super();
        this.sectionNumber = i;
    }

    @Override
    public void perform() {
        view.removeSection(sectionNumber);
    }
    
    
    
}
