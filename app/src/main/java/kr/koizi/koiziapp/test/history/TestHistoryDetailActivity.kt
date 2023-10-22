package kr.koizi.koiziapp.test.history

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.adapter.AnswerAdapter
import kr.koizi.koiziapp.data.request.TestAnswerRequest
import kr.koizi.koiziapp.databinding.ActivityTestHistoryDetailBinding
import kr.koizi.koiziapp.retrofit.RetrofitService
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.ExceptionAlertDialog
import kotlinx.coroutines.*
import kr.koizi.koiziapp.main.MainActivity
import kr.koizi.koiziapp.test.SupportFragment

class TestHistoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestHistoryDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarTestAnswer.tvTitle.text = getString(R.string.title_test_result)

        binding.toolbarTestAnswer.btnBack.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        val answerList = intent.getIntegerArrayListExtra("answer")

        val shimmerContainer = binding.shimmerViewContainer

        shimmerContainer.startShimmer()
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch(Dispatchers.IO) {
            try {
                val answerRequest = TestAnswerRequest(answers = answerList as? List<Int>)
                val answerResponse = answerRequest.let {
                    RetrofitService.retrofitService.getTestAnswer(it)
                }

                withContext(Dispatchers.Main) {
                    val body = answerResponse.body()
                    if (answerResponse.body()?.result == "OK") {
                        val safety = getString(R.string.safety)
                        val safety25 = getString(R.string.safety25)
                        val safety50 = getString(R.string.safety50)
                        val safety75 = getString(R.string.safety75)
                        val safety100 = getString(R.string.safety100)
                        var spannableString = SpannableString(safety + safety25)
                        val boldText = SpannableString(MainActivity.nickName)
                        boldText.setSpan(StyleSpan(Typeface.BOLD), 0, MainActivity.nickName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                        val fullText = SpannableStringBuilder()
                        fullText.append(boldText)
                        fullText.append("님,")

                        binding.btnSupport.setOnClickListener {
                            val bottomSheetFragment = SupportFragment()
                            val args = Bundle().apply {
                                body?.safety?.let { it1 -> putInt("safety", it1) }
                            }
                            bottomSheetFragment.arguments = args
                            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                        }
                        when (body?.safety) {
                            1 -> {
                                spannableString = SpannableString(safety + safety25)
                                spannableString.setSpan(
                                    ForegroundColorSpan(
                                        ContextCompat.getColor(
                                            this@TestHistoryDetailActivity,
                                            R.color.error
                                        )
                                    ),
                                    safety.length,
                                    safety.length + safety25.length,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                                binding.ivSafety.setImageResource(R.drawable.safety25)
                                binding.tvContent.text = getString(R.string.title_answer25)
                                fullText.append(getString(R.string.text_answer25))
                            }
                            2 -> {
                                spannableString = SpannableString(safety + safety50)
                                spannableString.setSpan(
                                    ForegroundColorSpan(
                                        ContextCompat.getColor(
                                            this@TestHistoryDetailActivity,
                                            R.color.orange
                                        )
                                    ),
                                    safety.length,
                                    safety.length + safety50.length,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                                binding.ivSafety.setImageResource(R.drawable.safety50)
                                binding.tvContent.text = getString(R.string.title_answer50)
                                fullText.append(getString(R.string.text_answer50))
                            }
                            3 -> {
                                spannableString = SpannableString(safety + safety75)
                                spannableString.setSpan(
                                    ForegroundColorSpan(
                                        ContextCompat.getColor(
                                            this@TestHistoryDetailActivity,
                                            R.color.warning
                                        )
                                    ),
                                    safety.length,
                                    safety.length + safety75.length,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                                binding.ivSafety.setImageResource(R.drawable.safety75)
                                binding.tvContent.text = getString(R.string.title_answer75)
                                fullText.append(getString(R.string.text_answer75))
                            }
                            4 -> {
                                spannableString = SpannableString(safety + safety100)
                                spannableString.setSpan(
                                    ForegroundColorSpan(
                                        ContextCompat.getColor(
                                            this@TestHistoryDetailActivity,
                                            R.color.green
                                        )
                                    ),
                                    safety.length,
                                    safety.length + safety100.length,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                                binding.ivSafety.setImageResource(R.drawable.safety100)
                                binding.tvContent.text = getString(R.string.title_answer100)
                                fullText.append(getString(R.string.text_answer100))
                            }
                        }
                        binding.tvSafety.text = spannableString
                        binding.tvSupport.text = fullText
                    }

                    val itemList = listOf("집수리 안내서", "복지용구", "공사 서비스")

                    val guide = answerResponse.body()?.guide
                    binding.recyclerAnswer.layoutManager = LinearLayoutManager(
                        this@TestHistoryDetailActivity, LinearLayoutManager.VERTICAL, false)
                    binding.recyclerAnswer.adapter = answerResponse.body()?.guide.orEmpty().let {
                        AnswerAdapter(this@TestHistoryDetailActivity, itemList, guide)
                    }
                    binding.recyclerAnswer.visibility = VISIBLE
                    shimmerContainer.stopShimmer()
                    shimmerContainer.visibility = View.GONE
                }
            } catch (e: Exception) {
                Log.d("에러가 발생 : ", e.toString())
                ExceptionAlertDialog.handleException(this@TestHistoryDetailActivity)
            }
        }
    }
}