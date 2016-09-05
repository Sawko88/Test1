package sample;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * Created by shestakov.aa on 29.08.2016.
 */
public class MaxLeingthTextFiekd implements javafx.beans.value.ChangeListener<String>{

    private int maxLength;
    private TextField textField;


    public MaxLeingthTextFiekd(TextField textField, int maxLength) {
        this.textField= textField;
        this.maxLength = maxLength;
    }


    public int getMaxLength() {
        return maxLength;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue == null) {
            return;
        }


        if (newValue.length() > maxLength) {
            textField.setText(oldValue);
        } else {
            textField.setText(newValue);
        }

    }
}
