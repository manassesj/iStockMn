package bsi.mpoo.istock.services;

import android.widget.EditText;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

public abstract class MaskGenerator {

    public static String unmask(String text) {
        return text.replaceAll("[.]", "").replaceAll("[-]", "")
                .replaceAll("[/]", "").replaceAll("[(]", "")
                .replaceAll("[)]", "").replaceAll(" ", "")
                .replaceAll(",", "");
    }

    public static void  mask(EditText editText, String maskType){
        SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter(maskType);
        MaskTextWatcher maskTextWatcher = new MaskTextWatcher(editText, simpleMaskFormatter);
        editText.addTextChangedListener(maskTextWatcher);
    }

    public static String unmaskedTextToStringMasked(String text, String maskType){
        StringBuilder result = new StringBuilder();
        int index = 0;
        for (char character: maskType.toCharArray()){
            if (character == 'N' || character == 'L' || character == 'A' || character == 'l' || character == 'U'){
                result.append(text.charAt(index));
                index++;
                continue;
            }
            result.append(character);
        }
        return result.toString();
    }
}
