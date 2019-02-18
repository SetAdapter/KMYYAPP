package doctor.kmwlyy.com.recipe.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/8/30.
 */
public class MyEditView extends EditText {
    private int index = -1;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public MyEditView(Context context) {
        super(context);
        // TODO: do something here if you want
    }

    public MyEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO: do something here if you want
    }

    public MyEditView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO: do something here if you want
    }
}
