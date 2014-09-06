
package memo.utils.internationalization;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public final class Internationalizator {
    
    private static Internationalizator instance;
    private static final String LANGUAGE_BUNDLE = 
                        "memo.utils.internationalization.properties.language";
    private Locale locale;
    private ResourceBundle resourceBundle;
    private ArrayList<InternationalizedComponent> observers;
    
    private Internationalizator() {
        locale = Locale.getDefault();
        resourceBundle = ResourceBundle.getBundle(LANGUAGE_BUNDLE, locale);
        observers = new ArrayList<>();
    }
    
    public static Internationalizator newInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new Internationalizator();
            return instance;
        }
    }
    
    public void setLocale(Locale locale) {
        this.locale = locale;
        resourceBundle = ResourceBundle.getBundle(LANGUAGE_BUNDLE, locale);
        notifyObservers();
    }
    
    public ResourceBundle getBundle() {
        return resourceBundle;
    }
    
    public void addObserver(InternationalizedComponent observer) {
        observers.add(observer);
        observer.setText(getString(observer.getKey()));
    }
    
    private void notifyObservers() {
        for(int i = 0; i < observers.size(); i++) {
            observers.get(i).setText(getString(observers.get(i).getKey()));
        }
    }
    
    private String getString(String keyName) {
        return resourceBundle.getString(keyName);
    }
}
