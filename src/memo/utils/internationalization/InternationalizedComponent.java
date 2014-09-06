
package memo.utils.internationalization;


public abstract class InternationalizedComponent {

    private final String key;
    
    public InternationalizedComponent(String key) {
        this.key = key;
    }
    
    public abstract void setText(String text);
    
    public String getKey() {
        return key;
    }
    
}
