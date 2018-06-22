package com.finance.ymt.sgr.finance.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.finance.ymt.sgr.finance.R;
import com.finance.ymt.sgr.finance.config.AppCon;
import com.finance.ymt.sgr.finance.http.HttpUtils;
import com.finance.ymt.sgr.finance.model.CommonModel;
import com.finance.ymt.sgr.finance.model.Result;
import com.finance.ymt.sgr.finance.model.User;
import com.finance.ymt.sgr.finance.ui.order.OrderActivity;
import com.finance.ymt.sgr.finance.util.StartActivityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.login_bt_commit)
    Button login_bt_commit;

    @BindView(R.id.login_ed_usename)
    EditText login_ed_usename;

    @BindView(R.id.login_ed_password)
    EditText login_ed_password;
    private Unbinder unbinder;

    CommonModel commonModel;
    SharedPreferences pref ;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
        commonModel=new CommonModel(this);
        pref = this.getSharedPreferences(AppCon.USER_KEY,MODE_PRIVATE);
        editor = pref.edit();
        String temp= pref.getString(AppCon.SCCESS_TOKEN_KEY,"");
        if(temp==null||temp.equals("")){

        }else{
            StartActivityUtil.skipAnotherActivity(LoginActivity.this,OrderActivity.class);
            finish();
        }
    }

    @OnClick({ R.id.login_bt_commit,R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_bt_commit://登录

                if(login_ed_usename.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this,"请输入用户名",Toast.LENGTH_SHORT).show();
                }else if(login_ed_password.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                }else{
                    User user=new User();
                    user.setPassword(login_ed_password.getText().toString());
                    user.setUsername(login_ed_usename.getText().toString());
                    commonModel.getLogin(user, new HttpUtils.OnHttpResultListener() {
                        @Override
                        public void onResult(Object result) {
                           Result<String> temp=(Result<String>)result;
                           if(temp.status.equals("200")){
                               editor.putString(AppCon.SCCESS_TOKEN_KEY,temp.content);
                               editor.commit();
                               StartActivityUtil.skipAnotherActivity(LoginActivity.this,OrderActivity.class);
                               finish();
                           }else{
                               Toast.makeText(LoginActivity.this,temp.message,Toast.LENGTH_SHORT).show();
                           }
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
                }


                break;
            case R.id.back://退出
                finish();
                break;



        }
    }

}
