package com.example.mvimosbysample.presenter;

import com.example.mvimosbysample.datasource.DataSourceInteractor;
import com.example.mvimosbysample.mvpview.MainView;
import com.example.mvimosbysample.viewstate.MainViewState;
import com.example.mvimosbysample.viewstate.PartialViewState;
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter extends MviBasePresenter<MainView, MainViewState> {

    private DataSourceInteractor dataSourceInteractor;

    public MainPresenter() {
        this.dataSourceInteractor = new DataSourceInteractor();
    }

    @Override
    protected void bindIntents() {
        Observable<PartialViewState> askQuestion = intent(MainView::askQuestionIntent)
                .switchMap(questionId -> dataSourceInteractor.askQuestion(questionId)
                        .map(question -> (PartialViewState) new PartialViewState.GotQuestion(question))
                        .startWith(new PartialViewState.Loading())
                        .onErrorReturn(PartialViewState.Error::new)
                        .subscribeOn(Schedulers.io()));

        Observable<PartialViewState> getAnswer = intent(MainView::getAnswerIntent)
                .switchMap(questionId -> dataSourceInteractor.getAnswer(questionId)
                        .map(answer -> (PartialViewState) new PartialViewState.GotAnswer(answer))
                        .startWith(new PartialViewState.Loading())
                        .onErrorReturn(PartialViewState.Error::new)
                        .subscribeOn(Schedulers.io()));

        MainViewState initialState = new MainViewState(false, false, false, "Ask Your question", null);
        Observable<PartialViewState> allIntents = Observable.merge(askQuestion, getAnswer)
                .observeOn(AndroidSchedulers.mainThread());

        subscribeViewState(allIntents.scan(initialState, this::viewStateReducer), MainView::render);
    }

    private MainViewState viewStateReducer(MainViewState previousState, PartialViewState changedStatePart) {
        if (changedStatePart instanceof PartialViewState.Loading) {
            previousState.setLoading(true);
        }

        if (changedStatePart instanceof PartialViewState.GotQuestion) {
            previousState.setLoading(false);
            previousState.setQuestionShown(true);
            previousState.setAnswerShown(false);
            previousState.setTextToShow(((PartialViewState.GotQuestion) changedStatePart).getQuestion());
        }

        if (changedStatePart instanceof PartialViewState.GotAnswer) {
            previousState.setLoading(false);
            previousState.setQuestionShown(false);
            previousState.setAnswerShown(true);
            previousState.setTextToShow(((PartialViewState.GotAnswer) changedStatePart).getAnswer());
        }

        if (changedStatePart instanceof PartialViewState.Error) {
            previousState.setLoading(false);
            previousState.setError(((PartialViewState.Error) changedStatePart).getError());
        }

        return previousState;
    }
}
