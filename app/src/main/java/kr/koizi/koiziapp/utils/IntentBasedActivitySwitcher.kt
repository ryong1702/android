package kr.koizi.koiziapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import kr.koizi.koiziapp.alarm.AlarmActivity
import kr.koizi.koiziapp.interior.*
import kr.koizi.koiziapp.interior.history.ConsultingHistoryActivity
import kr.koizi.koiziapp.interior.history.ConsultingHistoryDetailActivity
import kr.koizi.koiziapp.interior.history.ProgressHistoryActivity
import kr.koizi.koiziapp.join.*
import kr.koizi.koiziapp.login.*
import kr.koizi.koiziapp.main.MainActivity
import kr.koizi.koiziapp.myPage.*
import kr.koizi.koiziapp.test.*
import kr.koizi.koiziapp.test.history.TestHistoryDetailActivity
import kr.koizi.koiziapp.test.history.TestHistoryActivity
import java.io.Serializable

class IntentBasedActivitySwitcher(val context: Context) {
    // 인텐트에 전달하는 데이터의 이름을 상수로 정의
    companion object {
        const val MARKETING = "marketing"
        const val PHONE = "phone"
        const val ID = "id"
        const val PASSWORD = "password"
        const val PREVIOUS_SCREEN = "previousScreen"
        const val TITLE = "title"
        const val FLG = "flg"
        const val PROGRESSID = "progressId"
        const val UPDATEAT = "updateAt"
        const val CONTENT = "content"
        const val GENDER = "gender"
        const val Q1 = "q1"
        const val Q2 = "q2"
        const val Q3 = "q3"
        const val Q4 = "q4"
        const val Q5 = "q5"
        const val ANSWER = "answer"
        const val TITLEID = "titleId"
    }

    // 인텐트를 생성하는 메소드
    private fun createIntent(targetClass: Class<*>, vararg extras: Pair<String, Any?>): Intent {
        val intent = Intent(context, targetClass)
//        for ((key, value) in extras) {
        extras.forEach { (key, value) ->
            when (value) {
                is Boolean -> intent.putExtra(key, value)
                is String -> intent.putExtra(key, value)
                is Int -> intent.putExtra(key, value)// 다른 타입에 대한 처리도 추가할 수 있습니다.
                is List<*> -> {
                    if (value.isNotEmpty() && value[0] is Serializable) {
                        intent.putExtra(key, ArrayList(value))
                    }
                }
            }
        }
        return intent
    }

    // 회원가입 ID 입력화면으로 이동
    fun goToRegisterIdActivity(marketing: Boolean, phone: String) {
        val intent = createIntent(RegisterIdActivity::class.java, MARKETING to marketing, PHONE to phone)
        context.startActivity(intent)
    }

    // 회원가입 패스워드 입력화면으로 이동
    fun goToRegisterPasswordActivity(marketing: Boolean, phone: String, id: String) {
        val intent = createIntent(RegisterPasswordActivity::class.java, MARKETING to marketing, PHONE to phone, ID to id)
        context.startActivity(intent)
    }

    // 회원가입 프로필 화면으로 이동
    fun goToRegisterProfileActivity(marketing: Boolean, phone: String, id: String, password: String) {
        val intent = createIntent(RegisterProfileActivity::class.java, MARKETING to marketing, PHONE to phone, ID to id, PASSWORD to password)
        context.startActivity(intent)
    }

    // 메인화면 이동
    fun goToMainActivity() {
        val intent = createIntent(MainActivity::class.java)
        context.startActivity(intent)
    }

    // 아이디, 패스워드 찾기 본인인증 휴대폰 번호 입력 화면으로 이동
    fun goToIdentityVerificationActivity(marketing: Boolean) {
        val intent = createIntent(IdentityVerificationActivity::class.java, MARKETING to marketing)
        context.startActivity(intent)
    }

    // 아이디, 패스워드 찾기 인증번호 입력화면으로 이동
    fun goToIdentityVerificationCodeActivity(marketing: Boolean, phone: String) {
        val intent = createIntent(IdentityVerificationCodeActivity::class.java, MARKETING to marketing, PHONE to phone)
        context.startActivity(intent)
    }

    // 메인화면 이동
    fun goToLoginActivity() {
        val intent = createIntent(LoginActivity::class.java)
        context.startActivity(intent)
    }

    // 메인화면 이동
    fun goToFindPassWordActivity() {
        val intent = createIntent(FindPassWordActivity::class.java)
        context.startActivity(intent)
    }

    // 패스워드 찾기 리셋 화면 이동
    fun goToPasswordResetActivity(id: String) {
        val intent = createIntent(PasswordResetActivity::class.java, ID to id)
        context.startActivity(intent)
    }

    // 아이디 찾기 아이디 검색 결과화면 이동
    fun goToFoundIdActivity(id: String) {
        val intent = createIntent(FoundIdActivity::class.java, ID to id)
        context.startActivity(intent)
    }

    fun deleteActivity() {
        val intent = Intent(context, LoginActivity::class.java).apply {
            // FLAG_ACTIVITY_CLEAR_TOP 플래그는 메인 액티비티 위에 있는 모든 액티비티들을 제거해줍니다.
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    // 아이디 찾기 핸드폰번호 인증 화면 이동
    fun goToVerificationCodeActivity(id: String?, phone: String, previousScreen: String) {
        val intent = createIntent(VerificationCodeActivity::class.java, ID to id, PHONE to phone, PREVIOUS_SCREEN to previousScreen)
        context.startActivity(intent)
    }

    // 인테리어 진행상황 화면으로 이동
    fun goToProgressActivity(flg: Int, progressId: String?, previousScreen: String) {
        val intent = createIntent(ProgressActivity::class.java, FLG to flg, PROGRESSID to progressId, PREVIOUS_SCREEN to previousScreen)
        context.startActivity(intent)
    }

    // 인테리어 문의하기 화면으로 이동
    fun goToAskActivity() {
        val intent = createIntent(AskActivity::class.java)
        context.startActivity(intent)
    }

    // 컨설팅 문의내역 화면으로 이동
    fun goToConsultingHistoryActivity() {
        val intent = createIntent(ConsultingHistoryActivity::class.java)
        context.startActivity(intent)
    }

    // 간편검사 화면으로 이동
    fun goToSampleTestActivity() {
        val intent = createIntent(SampleTestActivity::class.java)
        context.startActivity(intent)
    }

    // 전화어플 화면으로 이동
//    fun goToTellApp(managerPhone: String?) {
//        val intent = Intent(Intent.ACTION_DIAL, Uri.parse(managerPhone))
//        context.startActivity(intent)
//    }

    // 개인정보 수집안내 화면으로 이동
    fun goToAgreementCollectionPersonalInfoActivity() {
        val intent = createIntent(AgreementCollectionPersonalInfoActivity::class.java)
        context.startActivity(intent)
    }

    // 마이페이지 화면으로 이동
    fun goToMyPageActivity() {
        val intent = createIntent(MyPageActivity::class.java)
        context.startActivity(intent)
    }

    // 알람 화면으로 이동
    fun goToAlarmActivity() {
        val intent = createIntent(AlarmActivity::class.java)
        context.startActivity(intent)
    }

    // 이용약관 동의화면으로 이동
    fun goToTermsOfUseActivity(flg: Int) {
        val intent = createIntent(TermsOfUseActivity::class.java, FLG to flg)
        context.startActivity(intent)
    }

    // 아이디 찾기 화면으로 이동
    fun gotoFindIdActivity() {
        val intent = createIntent(FindIdActivity::class.java)
        context.startActivity(intent)
    }

    // 로그인 화면으로 이동
    fun gotoLoginActivity() {
        val intent = createIntent(LoginActivity::class.java)
        context.startActivity(intent)
    }

    // 인포 화면으로 이동
    fun gotoInfoActivity() {
        val intent = createIntent(InfoActivity::class.java)
        context.startActivity(intent)
    }

    // 공지 화면으로 이동
    fun gotoNoticeActivity() {
        val intent = createIntent(NoticeActivity::class.java)
        context.startActivity(intent)
    }

    // 설정 화면으로 이동
    fun gotoAppSettingActivity() {
        val intent = createIntent(AppSettingActivity::class.java)
        context.startActivity(intent)
    }

    // 공지사항 상세 화면으로 이동
    fun goToNoticeDetailActivity(title: String?, updateAt: String?, content: String?) {
        val intent = createIntent(NoticeDetailActivity::class.java, TITLE to title, UPDATEAT to updateAt, CONTENT to content)
        context.startActivity(intent)
    }

    // 성별 선택 화면 이동
    fun goToSelectGenderActivity() {
        val intent = createIntent(SelectGenderActivity::class.java)
        context.startActivity(intent)
    }

    // Q1 화면 이동
    fun goToTestQ1Activity(gender: Int?) {
        val intent = createIntent(TestQ1Activity::class.java, GENDER to gender)
        context.startActivity(intent)
    }

    // Q2 화면 이동
    fun goToTestQ2Activity(gender: Int?, q1: Int?) {
        val intent = createIntent(TestQ2Activity::class.java, GENDER to gender, Q1 to q1)
        context.startActivity(intent)
    }

    // Q3 화면 이동
    fun goToTestQ3Activity(gender: Int?, q1: Int?, q2: Int?) {
        val intent = createIntent(TestQ3Activity::class.java, GENDER to gender, Q1 to q1, Q2 to q2)
        context.startActivity(intent)
    }

    // Q4 화면 이동
    fun goToTestQ4Activity(gender: Int?, q1: Int?, q2: Int?, q3: Int?) {
        val intent = createIntent(TestQ4Activity::class.java, GENDER to gender, Q1 to q1, Q2 to q2, Q3 to q3)
        context.startActivity(intent)
    }

    // Q5 화면 이동
    fun goToTestQ5Activity(gender: Int?, q1: Int?, q2: Int?, q3: Int?, q4: Int?) {
        val intent = createIntent(TestQ5Activity::class.java, GENDER to gender, Q1 to q1, Q2 to q2, Q3 to q3, Q4 to q4)
        context.startActivity(intent)
    }

    // 테스트 답변 화면 이동
    fun goToTestAnswerActivity(gender: Int?, q1: Int?, q2: Int?, q3: Int?, q4: Int?, q5: Int?) {
        val intent = createIntent(TestAnswerActivity::class.java, GENDER to gender, Q1 to q1, Q2 to q2, Q3 to q3, Q4 to q4, Q5 to q5)
        context.startActivity(intent)
    }

    // 테스트 히스토리 화면 이동
    fun goToTestHistoryActivity() {
        val intent = createIntent(TestHistoryActivity::class.java)
        context.startActivity(intent)
    }

    // 진행상황 화면 이동
    fun goToProgressHistory() {
        val intent = createIntent(ProgressHistoryActivity::class.java)
        context.startActivity(intent)
    }

    // 간편테스트 결과 화면 이동
    fun goToTestHistoryActivityDetail(answer: List<Int>?) {
        val intent = createIntent(TestHistoryDetailActivity::class.java,ANSWER to answer)
        context.startActivity(intent)
    }

    // 컨설팅 상세 화면 이동
    fun goToConsultingHistoryDetailActivity(titleId: String?, previousScreen: String) {
        val intent = createIntent(ConsultingHistoryDetailActivity::class.java,TITLEID to titleId, PREVIOUS_SCREEN to previousScreen)
        context.startActivity(intent)
    }

    // 마케팅정보동의 화면 이동
    fun goToMarketingInformationActivity() {
        val intent = createIntent(MarketingInformationActivity::class.java)
        context.startActivity(intent)
    }

    // 개인정보처리방침 화면 이동
    fun goToPrivacyPolicyActivity() {
        val intent = createIntent(PrivacyPolicyActivity::class.java)
        context.startActivity(intent)
    }

    // 서비스 이용약관 화면 이동
    fun goToInfoTermsOfServiceActivity() {
        val intent = createIntent(InfoTermsOfServiceActivity::class.java)
        context.startActivity(intent)
    }

    // 외부링크로 이동
    fun goToExternalLink(link: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        context.startActivity(intent)
    }
}