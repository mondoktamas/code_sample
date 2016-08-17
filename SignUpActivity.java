package com.pettersonapps.booklava.authorisation.signup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pettersonapps.booklava.BaseActivity;
import com.pettersonapps.booklava.R;
import com.pettersonapps.booklava.core.constants.Constants;
import com.pettersonapps.booklava.core.usecase.SignUpUseCase;
import com.pettersonapps.booklava.utils.Injection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

/**
 * Created by tomashmondok on 8/5/16.
 */
public class SignUpActivity extends BaseActivity implements SignUpContract.SignUpView {

    @BindView(R.id.edit_user_name)
    EditText mUserName;
    @BindView(R.id.layout_user_name)
    TextInputLayout mUserNameContainer;
    @BindView(R.id.edit_email)
    EditText mEmail;
    @BindView(R.id.layout_email)
    TextInputLayout mEmailContainer;
    @BindView(R.id.edit_password)
    EditText mPassword;
    @BindView(R.id.layout_password)
    TextInputLayout mPasswordContainer;
    @BindView(R.id.edit_confirm_password)
    EditText mConfirmPassword;
    @BindView(R.id.layout_confirm_password)
    TextInputLayout mConfirmPasswordContainer;
    @BindView(R.id.button_male)
    RadioButton mMale;
    @BindView(R.id.button_female)
    RadioButton mFemale;
    @BindView(R.id.layout_progress_bar)
    RelativeLayout mProgressLoader;

    private SignUpPresenter mPresenter;

    public static Intent getLaunchIntent(final Context context) {
        return new Intent(context, SignUpActivity.class);
    }

    @Override
    protected void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        mPresenter = new SignUpPresenter(new SignUpUseCase(this, Injection.provideUserRepository()), this);
    }

    @OnClick(R.id.button_sign_up)
    public void onSignUpButtonClicked() {
        mPresenter.signUpClicked(mUserName.getText().toString().trim(),
                mMale.isChecked() ? Constants.GENDER_MALE : Constants.GENDER_FEMALE,
                mEmail.getText().toString().trim(),
                mPassword.getText().toString().trim(),
                mConfirmPassword.getText().toString().trim());
    }

    @OnFocusChange({R.id.edit_user_name, R.id.edit_email, R.id.edit_password, R.id.edit_confirm_password})
    public void onClearErrorMessages() {
        clearErrorMessages();
    }

    @OnTextChanged({R.id.edit_user_name, R.id.edit_email, R.id.edit_password, R.id.edit_confirm_password})
    public void onEditTextChanged() {
        clearErrorMessages();
    }

    private void clearErrorMessages() {
        mUserNameContainer.setError(null);
        mEmailContainer.setError(null);
        mPasswordContainer.setError(null);
        mConfirmPasswordContainer.setError(null);
    }

    @Override
    public void incorrectUserName() {
        mUserNameContainer.setError(getString(R.string.sign_up_empty_user_name));
    }

    @Override
    public void incorrectPassword(final SignUpContract.ErrorType errorType) {
        switch (errorType) {
            case EMPTY:
                mPasswordContainer.setError(getString(R.string.sign_up_empty_password));
                break;
            case INCORRECT:
                mPasswordContainer.setError(getString(R.string.sign_up_incorrect_password));
                break;
        }
    }

    @Override
    public void incorrectConfirmPassword(final SignUpContract.ErrorType errorType) {
        switch (errorType) {
            case EMPTY:
                mConfirmPasswordContainer.setError(getString(R.string.sign_up_empty_password));
                break;
            case INCORRECT:
                mConfirmPasswordContainer.setError(getString(R.string.sign_up_incorrect_confirm_password));
                break;
        }
    }

    @Override
    public void incorrectEmail(final SignUpContract.ErrorType errorType) {
        switch (errorType) {
            case EMPTY:
                mEmailContainer.setError(getString(R.string.sign_up_empty_email));
                break;
            case INCORRECT:
                mEmailContainer.setError(getString(R.string.sign_up_incorrect_email));
                break;
        }
    }

    @Override
    public void showProgress(final boolean visible) {
        mProgressLoader.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onSignUpSuccess() {
        //TODO open language selection screen
    }

    @Override
    public void onSignUpFailed(final String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        mPresenter.detach();
        super.onDestroy();
    }
}
