package com.brunodegan.androidplayground.base.ui

interface SnackbarUiStateHolder {
    data class SnackbarUi(
        val msg: String,
    ) : SnackbarUiStateHolder
}
