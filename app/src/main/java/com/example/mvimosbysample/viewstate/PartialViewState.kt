package com.example.mvimosbysample.viewstate

interface PartialViewState {
    class Loading : PartialViewState
    class GotQuestion(var question: String) : PartialViewState
    class GotAnswer(var answer: String) : PartialViewState
    class Error(var error: Throwable) : PartialViewState
}
