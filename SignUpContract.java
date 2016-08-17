package com.pettersonapps.booklava.authorisation.signup;

import com.pettersonapps.booklava.core.mvp.view.BaseView;

/**
 * Created by tomashmondok on 8/10/16.
 */
public interface SignUpContract {

    enum ErrorType {
        EMPTY, INCORRECT
    }

    interface SignUpView extends BaseView {
        void incorrectUserName();
        void incorrectPassword(final ErrorType errorType);
        void incorrectConfirmPassword(final ErrorType errorType);
        void incorrectEmail(final ErrorType errorType);
        void showProgress(final boolean visible);
        void onSignUpSuccess();
        void onSignUpFailed(final String message);

    }

    interface ViewActionsListener {
        void signUpClicked(final String userName, final String gender,
                           final String email, final String password,
                           final String confirmPassword);
    }
}
