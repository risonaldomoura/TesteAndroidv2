package com.riso.zup.bank.loginScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.riso.zup.bank.R;
import com.riso.zup.bank.helpers.Utils;
import com.riso.zup.bank.models.UserInfo;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener,
        LoginInteractor.View{

    @BindView(R.id.btn_login) Button btnLogin;

    @BindView(R.id.edt_user) TextInputEditText edtUser;
    @BindView(R.id.edt_password) TextInputEditText edtPassword;

    @BindString(R.string.loading_message) String loadingMessage;
    @BindString(R.string.error_message) String erroMessage;

    Utils utils = new Utils();
    Context ct;

    LoginPresenter presenter = new LoginPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ct = this;

        retrieveDataUserCache();

        initControl();
    }

    private void initControl(){
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btnLogin){
            //To to when click in Login button
            loginUser();
        }
    }

    private void retrieveDataUserCache() {
        String user = utils.retrieveStringCache(R.string.pref_file_bank, R.string.pref_key_user);
        if(user!= null)
            edtUser.setText(user);
    }

    private void loginUser(){
        String user = edtUser.getText().toString();
        String password = edtPassword.getText().toString();

        utils.showProgressDialog(ct, loadingMessage);

        presenter.login(user, password);
    }

    private void callHomeActivity(UserInfo userInfo) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("userInfo", userInfo);
        startActivity(intent);
    }

    @Override
    public void loginOK(String user, UserInfo userInfo) {

        utils.saveStringCache(
                R.string.pref_file_bank,
                R.string.pref_key_user,
                user);

        utils.hideProgressDialog();

        callHomeActivity(userInfo);

        finish();
    }

    @Override
    public void loginError(Error error) {
        if(error ==  null) {
            utils.showErrorDialog(ct, erroMessage);
        }
        else{
            utils.showErrorDialog(ct, error.getMessage());
        }
        utils.hideProgressDialog();
    }

    @Override
    public void loginError(int error) {
        utils.showErrorDialog(ct, getString(error));
        utils.hideProgressDialog();
    }
}
