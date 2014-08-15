
package memo.controller;

import memo.events.ModelChangedEvent;


public interface ModelChangedListener {
    
    public void modelChanged(ModelChangedEvent event);
    
}
