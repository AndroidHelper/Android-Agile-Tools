
package cn.core.mobile.library.tools;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class InputUtils {
    public static void hideInputMethod(View decorView) {
        InputMethodManager imm = (InputMethodManager) decorView.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(decorView.getApplicationWindowToken(), 0);
        }
    }
}
