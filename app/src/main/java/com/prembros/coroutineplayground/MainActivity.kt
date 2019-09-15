package com.prembros.coroutineplayground

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk27.coroutines.onClick

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    btn_launch_basics.onClick { startActivity(intentFor<CoroutineBasicsActivity>()) }

    btn_launch_jobs.onClick { startActivity(intentFor<CoroutineJobsActivity>()) }
  }
}
