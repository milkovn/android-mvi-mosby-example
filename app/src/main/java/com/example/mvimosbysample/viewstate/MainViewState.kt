package com.example.mvimosbysample.viewstate

class MainViewState(
    var loading: Boolean,
    var questionShown: Boolean,
    var answerShown: Boolean,
    var textToShow: String,
    var error: Throwable?
)
