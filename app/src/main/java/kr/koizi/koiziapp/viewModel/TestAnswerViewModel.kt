package kr.koizi.koiziapp.viewModel

import androidx.lifecycle.ViewModel
import kr.koizi.koiziapp.data.request.TestSaveRequest
import kr.koizi.koiziapp.retrofit.RetrofitService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TestAnswerViewModel : ViewModel() {
    suspend fun saveTestAnswers(userId: String, answers: List<Int>): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                RetrofitService.retrofitService.getTestSave(userId, TestSaveRequest(answers, userId))
                true // 저장 성공
            } catch (e: Exception) {
                false // 저장 실패
            }
        }
    }
}