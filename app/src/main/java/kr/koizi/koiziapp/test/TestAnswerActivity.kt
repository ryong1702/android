package kr.koizi.koiziapp.test

import android.annotation.SuppressLint
import android.graphics.Typeface
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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.adapter.AnswerAdapter
import kr.koizi.koiziapp.data.request.TestAnswerRequest
import kr.koizi.koiziapp.databinding.ActivityTestAnswerBinding
import kr.koizi.koiziapp.main.MainActivity.Companion.nickName
import kr.koizi.koiziapp.main.MainActivity.Companion.userId
import kr.koizi.koiziapp.retrofit.RetrofitService
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.DialogHelper
import kr.koizi.koiziapp.utils.ExceptionAlertDialog
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher
import kr.koizi.koiziapp.viewModel.TestAnswerViewModel

class TestAnswerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestAnswerBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestAnswerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarTestAnswer.tvTitle.text = getString(R.string.title_test_result)

        binding.toolbarTestAnswer.btnBack.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        val gender = intent.getIntExtra("gender" , 0)
        val q1 = intent.getIntExtra("q1" , 0)
        val q2 = intent.getIntExtra("q2" , 0)
        val q3 = intent.getIntExtra("q3" , 0)
        val q4 = intent.getIntExtra("q4" , 0)
        val q5 = intent.getIntExtra("q5" , 0)
        val listQ = listOf(q1, q2, q3, q4, q5)
        val saveListQ = listOf(gender) + listQ

        binding.btnClose.setOnClickListener {
            IntentBasedActivitySwitcher(this@TestAnswerActivity).goToSampleTestActivity()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        val shimmerContainer = binding.shimmerViewContainer

        shimmerContainer.startShimmer()
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch(Dispatchers.IO) {
            try {
                val answerRequest = TestAnswerRequest(answers = listQ)
                val answerResponse = RetrofitService.retrofitService.getTestAnswer(answerRequest)

                withContext(Dispatchers.Main) {
                    val body = answerResponse.body()
                    if (answerResponse.body()?.result == "OK") {
                        val safety = getString(R.string.safety)
                        val safety25 = getString(R.string.safety25)
                        val safety50 = getString(R.string.safety50)
                        val safety75 = getString(R.string.safety75)
                        val safety100 = getString(R.string.safety100)
                        var spannableString = SpannableString(safety + safety25)
                        val boldText = SpannableString(nickName)
                        boldText.setSpan(StyleSpan(Typeface.BOLD), 0, nickName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

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
                                spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(
                                    this@TestAnswerActivity, R.color.error)), safety.length,
                                    safety.length + safety25.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                                binding.ivSafety.setImageResource(R.drawable.safety25)
                                binding.tvContent.text = getString(R.string.title_answer25)
                                fullText.append(getString(R.string.text_answer25))
                            }
                            2 -> {
                                spannableString = SpannableString(safety + safety50)
                                spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(
                                    this@TestAnswerActivity, R.color.orange)), safety.length,
                                    safety.length + safety50.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                                binding.ivSafety.setImageResource(R.drawable.safety50)
                                binding.tvContent.text = getString(R.string.title_answer50)
                                fullText.append(getString(R.string.text_answer50))
                            }
                            3 -> {
                                spannableString = SpannableString(safety + safety75)
                                spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(
                                    this@TestAnswerActivity, R.color.warning)), safety.length,
                                    safety.length + safety75.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                                binding.ivSafety.setImageResource(R.drawable.safety75)
                                binding.tvContent.text = getString(R.string.title_answer75)
                                fullText.append(getString(R.string.text_answer75))
                            }
                            4 -> {
                                spannableString = SpannableString(safety + safety100)
                                spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(
                                    this@TestAnswerActivity, R.color.green)), safety.length,
                                    safety.length + safety100.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
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
                    binding.recyclerAnswer.layoutManager = LinearLayoutManager(this@TestAnswerActivity, LinearLayoutManager.VERTICAL, false)
                    binding.recyclerAnswer.adapter = answerResponse.body()?.guide.orEmpty().let { AnswerAdapter(this@TestAnswerActivity, itemList, guide) }
                    binding.recyclerAnswer.visibility = VISIBLE
                    shimmerContainer.stopShimmer()
                    shimmerContainer.visibility = View.GONE

                    binding.toolbarTestAnswer.btnSave.setOnClickListener {
                        runOnUiThread {
                            DialogHelper.showAlertDialog(
                                context = this@TestAnswerActivity,
                                title = "저장하시겠습니까?",
                                message = "간편테스트 결과를 저장합니다.",
                                positiveButtonText = "예",
                                positiveButtonClickListener = {
                                    scope.launch {

                                        val viewModel = ViewModelProvider(this@TestAnswerActivity)[TestAnswerViewModel::class.java]
                                        // saveTestAnswers 함수에서 반환된 성공/실패 여부를 저장
                                        val isSaved = viewModel.saveTestAnswers(userId, saveListQ)
                                        if (isSaved) {
                                            DialogHelper.showAlertConfirmDialog(
                                                context = this@TestAnswerActivity,
                                                title = getString(R.string.save_ok),
                                                message = getString(R.string.save_ok_message),
                                                positiveButtonText = getString(R.string.yes),
                                                positiveButtonClickListener = {
                                                    IntentBasedActivitySwitcher(this@TestAnswerActivity).goToSampleTestActivity()
                                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                                }
                                            )
                                        } else {
                                            // 실패했을 때
                                            Toast.makeText(this@TestAnswerActivity, "저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("에러가 발생 : ", e.toString())
                ExceptionAlertDialog.handleException(this@TestAnswerActivity)
            }
        }
    }
}