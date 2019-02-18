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

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;

import java.util.ArrayList;
import java.util.List;

import doctor.kmwlyy.com.recipe.Event.Http_getDrugs_Event;
import doctor.kmwlyy.com.recipe.Event.Http_getICDs_Event;
import doctor.kmwlyy.com.recipe.Event.NetService;
import doctor.kmwlyy.com.recipe.Model.Constant;
import doctor.kmwlyy.com.recipe.Model.DrugBean;
import doctor.kmwlyy.com.recipe.Model.DrugDetail;
import doctor.kmwlyy.com.recipe.Model.ICD;

/**
 * Created by Administrator on 2016/8/20.
 */
public class DrugAutoCompleteTextView extends AutoCompleteTextView {
    public static final String TAG = "DrugAutoCompleteTextVi";
    public static Boolean ENABLE = true;
    private MyAdatper adapter;
    private Context mContext;
    private String type;
    private Boolean isSearch = true; //控制是否发请求查找相关药品
    private List<DrugDetail> historyList = new ArrayList<>();
    private String historyStr = "";

    public DrugAutoCompleteTextView(Context context){
        super(context);
        mContext = context;
        init(context);
    }
    public DrugAutoCompleteTextView(Context context, AttributeSet attrs) {
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
                adapter.clearData();
                if(s.toString().trim().length() > 0 && (!s.toString().equals(historyStr)) && ENABLE){
                    if(isSearch(s+"")){
                        searchDrug(s+"");
                    }
                }
                historyStr = s.toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        // default=2 当输入一个字符的时候就开始检测
        setThreshold(1);
    }

    class MyAdatper extends BaseAdapter implements Filterable {
        List<DrugDetail> mList;
        private Context mContext;
        private MyFilter mFilter;

        public MyAdatper(Context context) {
            mContext = context;
            mList = new ArrayList<DrugDetail>();
        }

        public void clearData(){
            mList.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public String getItem(int position) {
            return mList == null ? null : mList.get(position).DrugName;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DrugDetail drugDetail = mList.get(position);
            if (convertView == null) {
                TextView tv = new TextView(mContext);
                tv.setTextColor(Color.BLACK);
                tv.setTextSize(20);
                tv.setPadding(10,10,10,10);
                convertView = tv;
            }
            TextView txt = (TextView) convertView;
            txt.setText(drugDetail.DrugName);
            return txt;
        }

        public void setData(String str,List<DrugDetail> list){
            for(int i=0;i<list.size();i++){
                if(!list.get(i).DrugName.equals(str)){
                    mList.add(list.get(i));
                    historyList.add(list.get(i));
                }
            }
            notifyDataSetChanged();
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
                    mList = new ArrayList<DrugDetail>();
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


    public void searchDrug(String s) {
        if(s.trim().length() > 0 ) {
            Http_getDrugs_Event event = new Http_getDrugs_Event(s, type, new HttpListener<List<DrugDetail>>(

            ) {
                @Override
                public void onError(int code, String msg) {
                    if (DebugUtils.debug) {
                        Log.i(TAG, "searchICD error , code : " + code + " , msg : " + msg);
                    }
                }

                @Override
                public void onSuccess(List<DrugDetail> list) {
                    if (DebugUtils.debug) {
                        Log.i(TAG, "searchICD success");
                    }

                    adapter.clearData();
                    if (list.size() > 0) {
                        adapter.setData(getText().toString(),list);
                        //先保存下，方便后面使用
                        Constant.setDrugList(list);
                        showDropDown();
                    }

                }
            });
            HttpClient httpClient = NetService.createClient(mContext, event);
            httpClient.start();
        }
    }

    public Boolean getSearch() {
        return isSearch;
    }

    public void setSearch(Boolean search) {
        isSearch = search;
    }

    /**
     * 判断是否发送搜索请求
     * @param str
     * @return
     */
    public boolean isSearch(String str){
        Boolean flag = true;
        for(int i=0;i<historyList.size();i++){
            if(historyList.get(i).DrugName.equals(str)){
                historyList.clear();
                flag = false;
            }
        }
        return flag;
    }
}
