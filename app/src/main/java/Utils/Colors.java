package Utils;

import android.content.Context;
import android.util.TypedValue;

public class Colors {
    public static int getAttrColor(Context context, int attrColor) {
        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(attrColor, value, true);

        return value.data;
    }
}
