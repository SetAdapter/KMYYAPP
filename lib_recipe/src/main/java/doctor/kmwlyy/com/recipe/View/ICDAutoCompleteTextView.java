package doctor.kmwlyy.com.recipe.View;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import doctor.kmwlyy.com.recipe.Event.Http_getICDs_Event;
import doctor.kmwlyy.com.recipe.Event.NetService;
import doctor.kmwlyy.com.recipe.Model.Constant;
import doctor.kmwlyy.com.recipe.Model.DiagDetailBean;
import doctor.kmwlyy.com.recipe.Model.DrugDetail;
import doctor.kmwlyy.com.recipe.Model.ICD;

/**
 * Created by Administrator on 2016/8/20.
 */
public class ICDAutoCompleteTextView extends AutoCompleteTextView {
    public static final String TAG = "ICDAutoCompleteTextView";
    public static Boolean ENABLE = true;
    private MyAdatper adapter;
    private Context mContext;
    private String type;
    private List<DiagDetailBean> historyList = new ArrayList<>();
    private String historyStr = "";

    public ICDAutoCompleteTextView(Context context){
        super(context);
        mContext = context;
        init(context);
    }
    public ICDAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context);
    }

    private void init(Context context){
        adapter = new MyAdatper(context);
        setAdapter(adapter);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                adapter.mList.clear();
                if(s.toString().trim().length() > 0 && (!s.toString().equals(historyStr)) && ENABLE){
                    if(isSearch(s+"")){
                        searchICD(s+"");
                    }
                }
                historyStr = s.toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before,int count) {}
        });
        // default=2 当输入一个字符的时候就开始检测
        setThreshold(1);
    }

    class MyAdatper extends BaseAdapter implements Filterable {
        List<DiagDetailBean> mList;
        private Context mContext;
        private MyFilter mFilter;

        public MyAdatper(Context context) {
            mContext = context;
            mList = new ArrayList<DiagDetailBean>();
        }

        public void setData(String str,List<DiagDetailBean> list){
            for(int i=0;i<list.size();i++){
                if(!list.get(i).DiseaseName.equals(str)){
                    mList.add(list.get(i));
                    historyList.add(list.get(i));
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public String getItem(int position) {
            return mList == null ? null : mList.get(position).DiseaseName;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                TextView tv = new TextView(mContext);
                tv.setTextColor(Color.BLACK);
                tv.setTextSize(20);
                tv.setPadding(10,10,10,10);
                convertView = tv;
            }
            TextView txt = (TextView) convertView;
            txt.setText(mList.get(position).DiseaseName);
            return txt;
        }

        @Override
        public Filter getFilter() {
            if (mFilter == null) {
                mFilter = new MyFilter();
            }
            return mFilter;
        }

        private class MyFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (mList == null) {
                    mList = new ArrayList<DiagDetailBean>();
                }
                results.values = mList;
                results.count = mList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        }
    }

    public void setValue(Context context,String type) {
        this.mContext = context;
        this.type = type;
    }


    private void searchICD(String s) {
        if(s.trim().length() > 0 ){
            Http_getICDs_Event event = new Http_getICDs_Event(s, type, new HttpListener<List<DiagDetailBean>>(

            ) {
                @Override
                public void onError(int code, String msg) {
                    if (DebugUtils.debug) {
                        Log.i(TAG, "searchICD error , code : " + code + " , msg : " + msg);
                    }

                }

                @Override
                public void onSuccess(List<DiagDetailBean> list) {
                    if (DebugUtils.debug) {
                        Log.i(TAG, "searchICD success");
                    }
                    adapter.mList.clear();
                    if(list.size() > 0){
                        Constant.setDiagDetailList(list);
                        adapter.setData(getText().toString(),list);
                        showDropDown();
                    }

                }
            });
            HttpClient httpClient = NetService.createClient(mContext, event);
            httpClient.start();
        }
    }

    /**
     * 判断是否发送搜索请求
     * @param str
     * @return
     */
    public boolean isSearch(String str){
        Boolean flag = true;
        for(int i=0;i<historyList.size();i++){
            if(historyList.get(i).DiseaseName.equals(str)){
                historyList.clear();
                flag = false;
            }
        }
        return flag;
    }
}
