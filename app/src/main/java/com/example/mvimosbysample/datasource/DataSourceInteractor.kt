package com.example.mvimosbysample.datasource

import io.reactivex.Observable
import java.util.concurrent.TimeUnit


class DataSourceInteractor {

    fun askQuestion(questionId: Int): Observable<String?>? {
        return Observable.just("Do you like Ice Cream?")
            .delay(3000, TimeUnit.MILLISECONDS)
    }

    fun getAnswer(questionId: Int): Observable<String?>? {
        return Observable.just("Yes, I do!")
            .delay(3000, TimeUnit.MILLISECONDS)
    }
}
