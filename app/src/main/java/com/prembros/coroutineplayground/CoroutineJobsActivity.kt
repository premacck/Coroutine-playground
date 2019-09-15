package com.prembros.coroutineplayground

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_coroutine_jobs.*
import kotlinx.coroutines.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class CoroutineJobsActivity : AppCompatActivity() {

  private lateinit var job: CompletableJob

  companion object {
    const val PROGRESS_MAX = 100
    const val PROGRESS_START = 0
    const val JOB_TIME = 4000   // Time taken to complete the job
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_coroutine_jobs)
    btn_coroutine_job.onClick {
      if (!::job.isInitialized) {
        initJob()
      }
      progress_job.startJobOrCancel(job)
    }
  }

  private fun initJob() {
    setButtonText(getString(R.string.start_job))
    setJobText("")
    //initializing every time because if a job is cancelled, we can't re-use that job. We have to create a new one.
    job = Job()
    job.invokeOnCompletion { error ->
      error?.message?.let { message ->
        println("Job $job was cancelled : $message")
        showToast(message)
      }
    }
    resetProgress()
  }

  private fun ProgressBar.startJobOrCancel(job: Job) {
    if (progress > 0) {
      val cancelMessage = "Job $job is already active. Cancelling..."
      println(cancelMessage)
      setJobText(cancelMessage)
      resetJob()
    } else {
      setButtonText(getString(R.string.cancel_job))
      setJobText(getString(R.string.job_running, job.toString()))
      // Adds the job to the CoroutineScope and creates a new CoroutineContext
      CoroutineScope(Dispatchers.IO + job).launch {
        println("Coroutine $this is activated with job $job")
        for (i in PROGRESS_START..PROGRESS_MAX) {
          delay((JOB_TIME / PROGRESS_MAX).toLong())
          this@startJobOrCancel.progress = i
        }
        withContext(Dispatchers.Main) {
          resetProgress()
          setButtonText(getString(R.string.start_job))
          setJobText(getString(R.string.job_is_complete, job.toString()))
        }
      }
    }
  }

  private fun resetProgress() {
    progress_job.max = PROGRESS_MAX
    progress_job.progress = PROGRESS_START
  }

  private fun setButtonText(text: String) {
    btn_coroutine_job.text = text
  }

  private fun setJobText(text: String) {
    tv_coroutine_job_result.text = text
  }

  private fun resetJob() {
    if (job.isActive || job.isCompleted) {
      job.cancel(CancellationException("Resetting job: by user")) // the CancellationException will be caught in job.invokeOnCompletion block.
    }
    initJob()
  }

  private fun showToast(message: String) {
    GlobalScope.launch(Dispatchers.Main) { toast(message) }
  }
}
