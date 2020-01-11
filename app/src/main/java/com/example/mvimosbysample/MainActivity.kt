package com.example.mvimosbysample

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.mvimosbysample.mvpview.MainView
import com.example.mvimosbysample.presenter.MainPresenter
import com.example.mvimosbysample.viewstate.MainViewState
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import java.util.*


class MainActivity : MviActivity<MainView, MainPresenter>(), MainView {

    private var textView: TextView? = null
    private var askQuestionButton: Button? = null
    private var getAnswerButton: Button? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.text_view)
        askQuestionButton = findViewById(R.id.btn_question)
        getAnswerButton = findViewById(R.id.btn_answer)
        progressBar = findViewById(R.id.progress_bar)
    }

    override fun createPresenter(): MainPresenter {
        return MainPresenter()
    }

    override fun askQuestionIntent(): Observable<Int> {
        return RxView.clicks(askQuestionButton!!)
            .map<Int> { Random().nextInt() }
    }

    override fun getAnswerIntent(): Observable<Int> {
        return RxView.clicks(getAnswerButton!!)
            .map<Int> { Random().nextInt() }
    }

    override fun render(viewState: MainViewState) {
        if (viewState.loading) {
            progressBar?.visibility = View.VISIBLE
            askQuestionButton?.isEnabled = false
            getAnswerButton?.isEnabled = false
        } else if (viewState.error != null) {
            progressBar?.visibility = View.GONE
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        } else if (!viewState.questionShown || viewState.answerShown) {
            progressBar?.visibility = View.GONE
            askQuestionButton?.isEnabled = true
            getAnswerButton?.isEnabled = false
            textView?.text = viewState.textToShow
        } else {
            progressBar?.visibility = View.GONE
            askQuestionButton?.isEnabled = false
            getAnswerButton?.isEnabled = true
            textView?.text = viewState.textToShow
        }
    }
}
