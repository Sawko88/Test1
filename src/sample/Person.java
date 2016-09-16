package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ToolBar;

/**
 * Created by shestakov.aa on 31.08.2016.
 */
public class Person {
    public String firstName = "";
    public String lastName = "";
    public String email = "";

    public Person() {
        this("", "", "");
    }

    public Person(String firstName, String lastName, String email) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String fName) {
        firstName=fName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String fName) {
        lastName=fName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String fName) {
        email=fName;
    }
}
