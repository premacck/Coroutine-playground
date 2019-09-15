package com.prembros.coroutineplayground

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_coroutine_basics.*
import kotlinx.coroutines.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class CoroutineBasicsActivity : AppCompatActivity() {

  companion object {
    const val PLAYGROUND = "Playground:"
    const val RESULT_1 = "Result 1"
    const val RESULT_2 = "Result 2"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_coroutine_basics)
//        setConventionalClickListener()
    setAnkoClickListener()
  }

  private fun setConventionalClickListener() {
    btn_coroutine_basic_action.setOnClickListener {
      CoroutineScope(Dispatchers.IO).launch {
        mockApiRequest()
      }
    }
  }

  private fun setAnkoClickListener() {
    btn_coroutine_basic_action.onClick(Dispatchers.IO) {
      mockApiRequest()
    }
  }

  private suspend fun getApiResult1(): String {
    logThread("getApiResult1")
    delay(1000)
    return RESULT_1
  }

  private suspend fun getApiResult2(result1: String): String {
    logThread("getApiResult2")
    delay(1000)
    return "$RESULT_2 after taking in $result1"
  }

  private suspend fun mockApiRequest() {
    val result1 = getApiResult1()
    println("$PLAYGROUND $result1")
    /*tv_coroutine_result.text = result1     Will result in crash, as we're still on the Dispatchers.IO coroutine context,
                                             which is running on a background thread*/
    setNewText(result1)
    val result2 = getApiResult2(result1)
    println("$PLAYGROUND $result2")
    setNewText(result2)
  }

  @SuppressLint("SetTextI18n")
  private suspend fun setNewText(text: String) {
    withContext(Dispatchers.Main) {
      tv_coroutine_result.text = "${tv_coroutine_result.text} \n$text"
    }
  }

  private fun logThread(functionName: String) =
    println("$PLAYGROUND $functionName: ${Thread.currentThread().name}")
}
