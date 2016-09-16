package sample;

/**
 * Created by shestakov.aa on 02.09.2016.
 */
public class Context {
    private final static Context instance = new Context();

    public static Context getInstance() {
        return instance;
    }

    public String country = "Rus";

    public String currentCountry() {
        return country;
    }
}
