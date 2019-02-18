package doctor.kmwlyy.com.recipe.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by Administrator on 2016/8/30.
 */
public class MySpinner extends Spinner {
    private int index = -1;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public MySpinner(Context context) {
        super(context);
        // TODO: do something here if you want
    }

    public MySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO: do something here if you want
    }

    public MySpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO: do something here if you want
    }
}
