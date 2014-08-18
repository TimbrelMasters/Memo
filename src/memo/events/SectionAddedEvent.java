
package memo.events;

import memo.model.Section;


public class SectionAddedEvent extends ModelChangedEvent {

    private Section section;
    
    public SectionAddedEvent() {
        super();
    }
    
    public SectionAddedEvent(Section section) {
        super();
        this.section = section;
    }
    
    
    @Override
    public void perform() {
        view.addSection(section);
    }
    
    
    
}
