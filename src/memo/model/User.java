package memo.model;

/**
 *
 * @author Pisarik
 */
public class User {
    private String name;
    
    public User(String name){
        this.name = name;
    }
    
    public User(){
        this("Unknown");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }    

    @Override
    public String toString() {
        return name;
    }

}
