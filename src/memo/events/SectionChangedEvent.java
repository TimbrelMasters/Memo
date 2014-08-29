package memo.events;

import memo.model.Section;

/**
 *
 * @author Pisarik
 */
public class SectionChangedEvent extends ModelChangedEvent{

    private final int sectionIndex;
    private final Section section;

    public SectionChangedEvent(int sectionIndex, Section section){
        this.sectionIndex = sectionIndex;
        this.section = section;
    }

    @Override
    public void perform() {
        view.changeSectionName(sectionIndex, section.getName());
    }

}
