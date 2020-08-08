package com.turelo.itunesbrowsersample.models

import java.lang.Exception

data class ErrorWithRetryAction(val exception: Exception, val retry: () -> Unit)