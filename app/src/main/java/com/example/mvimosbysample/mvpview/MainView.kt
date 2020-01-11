package com.example.mvimosbysample.mvpview

import com.example.mvimosbysample.viewstate.MainViewState
import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface MainView : MvpView {

    fun askQuestionIntent(): Observable<Int>

    fun getAnswerIntent(): Observable<Int>

    fun render(viewState: MainViewState)
}
