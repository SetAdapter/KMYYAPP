package doctor.kmwlyy.com.recipe.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kmwlyy.core.util.ToastUtils;

import doctor.kmwlyy.com.recipe.Model.PatientDiagnoseBean;
import doctor.kmwlyy.com.recipe.R;

/**
 * Created by Administrator on 2016/8/20.
 */
public class ZhuSuFragment extends Fragment{
    private EditText et_ZhuSu;
    private EditText et_present;
    private EditText et_allergic;
    private EditText et_past;
    private EditText et_chubu;
    private EditText et_advise;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_zhusu, null);
        et_ZhuSu = (EditText) view.findViewById(R.id.et_ZhuSu);
        et_present = (EditText) view.findViewById(R.id.et_present);
        et_allergic = (EditText) view.findViewById(R.id.et_allergic);
        et_past = (EditText) view.findViewById(R.id.et_past);
        et_chubu = (EditText) view.findViewById(R.id.et_chubu);
        et_advise = (EditText) view.findViewById(R.id.et_advise);

        return view;
    }

    /**
     * 检查参数是否都填了
     * @return
     */
    public Boolean checkParms(){
        if(et_ZhuSu.getText().toString().isEmpty()||
           et_present.getText().toString().isEmpty()||
           et_allergic.getText().toString().isEmpty()||
           et_past.getText().toString().isEmpty()||
           et_chubu.getText().toString().isEmpty()||
           et_advise.getText().toString().isEmpty() ){
            ToastUtils.showShort(getActivity(),getResources().getString(R.string.string_recipe_name_remind2));
            return false;
        }
        return true;
    }

    /**
     * 返回数据，进行保存
     * @return
     */
    public PatientDiagnoseBean.MedicalRecordBean getData(){
        PatientDiagnoseBean.MedicalRecordBean bean = new PatientDiagnoseBean.MedicalRecordBean();
        bean.setSympton(et_ZhuSu.getText().toString().trim());
        bean.setPresentHistoryIllness(et_present.getText().toString().trim());
        bean.setAllergicHistory(et_allergic.getText().toString().trim());
        bean.setPastMedicalHistory(et_past.getText().toString().trim());
        bean.setAdvised(et_advise.getText().toString().trim());
        bean.setPreliminaryDiagnosis(et_chubu.getText().toString().trim());
        return bean;
    }

    public void setData(PatientDiagnoseBean.MedicalRecordBean bean){
        et_ZhuSu.setText(bean.getSympton());
        et_present.setText(bean.getPresentHistoryIllness());
        et_allergic.setText(bean.getAllergicHistory());
        et_past.setText(bean.getPastMedicalHistory());
        et_advise.setText(bean.getAdvised());
        et_chubu.setText(bean.getPreliminaryDiagnosis());
    }

}
