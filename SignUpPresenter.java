package com.pettersonapps.booklava.authorisation.signup;

import com.pettersonapps.booklava.core.annotation.Gender;
import com.pettersonapps.booklava.core.mvp.model.Error;
import com.pettersonapps.booklava.core.mvp.model.User;
import com.pettersonapps.booklava.core.mvp.presenter.AbsBasePresenter;
import com.pettersonapps.booklava.core.usecase.DefaultInternetSubscriber;
import com.pettersonapps.booklava.core.usecase.DefaultSubscriber;
import com.pettersonapps.booklava.core.usecase.SignUpUseCase;
import com.pettersonapps.booklava.utils.Validator;

/**
 * Created by tomashmondok on 8/10/16.
 */
public class SignUpPresenter extends AbsBasePresenter<SignUpContract.SignUpView>
        implements SignUpContract.ViewActionsListener {

    private final SignUpUseCase mSignUpUseCase;

    public SignUpPresenter(final SignUpUseCase signUpUseCase,
                           final SignUpContract.SignUpView view) {
        attach(view);
        mSignUpUseCase = signUpUseCase;
    }

    @Override
    public void signUpClicked(final String userName,
                              final @Gender String gender,
                              final String email,
                              final String password,
                              final String confirmPassword) {
        if (Validator.isEmpty(userName)) {
            getView().incorrectUserName();
            return;
        }
        if (Validator.isEmpty(email)) {
            getView().incorrectEmail(SignUpContract.ErrorType.EMPTY);
            return;
        }
        if (!Validator.isCorrectEmail(email)) {
            getView().incorrectEmail(SignUpContract.ErrorType.INCORRECT);
            return;
        }
        if (Validator.isEmpty(password)) {
            getView().incorrectPassword(SignUpContract.ErrorType.EMPTY);
            return;
        }
        if (!Validator.isCorrectPassword(password)) {
            getView().incorrectPassword(SignUpContract.ErrorType.INCORRECT);
            return;
        }
        if (Validator.isEmpty(confirmPassword)) {
            getView().incorrectConfirmPassword(SignUpContract.ErrorType.EMPTY);
            return;
        }
        if (!Validator.isEquals(password, confirmPassword)) {
            getView().incorrectConfirmPassword(SignUpContract.ErrorType.INCORRECT);
            return;
        }
        mSignUpUseCase.setData(userName, gender, email, password, confirmPassword);
        mSignUpUseCase.execute(new SignUpSubscriber());
    }

    @Override
    public void detach() {
        super.detach();
        mSignUpUseCase.unsubscribe();
    }

    class SignUpSubscriber extends DefaultInternetSubscriber<User> {

        @Override
        public void handleUnprocessableEntity(final Error error) {
            super.handleUnprocessableEntity(error);
            final String errorString;
            if ((errorString = error.getFirstError()) == null) return;
            if (!isViewAttached()) return;
            getView().onSignUpFailed(errorString);
        }

        @Override
        public void onCompleted() {
            super.onCompleted();
            if (!isViewAttached()) return;
            getView().showProgress(false);
            getView().onSignUpSuccess();
        }

        @Override
        public void onError(final Throwable e) {
            super.onError(e);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void onStart() {
            super.onStart();
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }
    }
}
