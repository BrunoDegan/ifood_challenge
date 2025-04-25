package com.brunodegan.ifood_challenge.base.ui

interface SnackbarUiStateHolder {
    data class SnackbarUi(val msg: String) : SnackbarUiStateHolder
}