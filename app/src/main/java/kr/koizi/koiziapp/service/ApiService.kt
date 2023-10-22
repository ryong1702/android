package kr.koizi.koiziapp.service

import kr.koizi.koiziapp.data.request.*
import kr.koizi.koiziapp.data.response.ConsultingHistoryResponse
import kr.koizi.koiziapp.data.response.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    //notice
    @GET("/api/v1/m/notice")
    suspend fun getNotice(): Response<NoticeResponse>
    //join
    @GET("/api/v1/m/join/require_phone_certification/{phone}/{pur_flg}")
    suspend fun getRequirePhone(@Path("phone") phone: String, @Path("pur_flg") purFlg: Int): Response<JoinResponse>

    @GET("/api/v1/m/join/phone_certification/{phone}/{certification}")
    suspend fun getPhone(@Path("phone") phone: String, @Path("certification") certification: String): Response<JoinResponse>

    @GET("/api/v1/m/join/user_id_verified/{user_id}/{channel_type}")
    suspend fun getUserId(@Path("user_id") userId: String, @Path("channel_type") channelType: Int): Response<JoinResponse>

    @POST("/api/v1/m/join/account")
    suspend fun getRegister(@Body body: JoinAccountInfoRequest): Response<JoinResponse>

    //login
    @POST("/api/v1/m/login")
    suspend fun getLogin(@Body body: LoginRequest): Response<LoginResponse>
    @POST("/api/v1/m/secession")
    suspend fun getSecession(@Body body: SecessionRequest): Response<SecessionResponse>
    @POST("/api/v2/m/login")
    suspend fun getKakaoLogin(@Body body: KakaoLoginRequest): Response<KakaoLoginResponse>

    //lost-info
    @GET("/api/v1/m/lost-info/id-find/{phone}/{auth_num}")
    suspend fun getIdFind(@Path("phone") phone: String, @Path("auth_num") certification: String): Response<LostInfoResponse>
    @GET("/api/v1/m/lost-info/require-pw-certification/{phone}/{user_id}")
    suspend fun getRequirePw(@Path("phone") phone: String, @Path("user_id") userId: String): Response<LostInfoResponse>
    @POST("/api/v1/m/lost-info/reset-pw")
    suspend fun getResetPw(@Body body: LostInfoResetPwRequest): Response<LostInfoResponse>

    //main
    @GET("/api/v1/m/main/person-alarm/{user_id}")
    suspend fun getPersonAlarm(@Path("user_id") userId: String): Response<PersonAlarmResponse>

    @GET("/api/v1/m/main/interior-progress/{user_id}")
    suspend fun getInteriorProgress(@Path("user_id") userId: String): Response<InteriorProgressResponse>

    @GET("/api/v2/m/main/tip")
    suspend fun getTip(): Response<InteriorTipResponse>

    //alarm
    @GET("/api/v1/m/alarm/status-list/{user_id}")
    suspend fun getAlarmStatus(@Path("user_id") userId: String): Response<AlarmResponse>
    @GET("/api/v1/m/alarm/status-update/{key_id}")
    suspend fun getAlarmStatusUpdate(@Path("key_id") keyId: String): Response<AlarmStatusUpdateResponse>

    //show-photo
    @GET("/api/v1/m/photo/{img_id}")
    suspend fun getPhoto(@Path("img_id") imgId: String): Response<String>

    //interior consulting
    @GET("/api/v1/m/interior-consulting/history/{user_id}")
    suspend fun getConsultingHistory(@Path("user_id") userId: String): Response<ConsultingHistoryResponse>

    //sample interior
    @GET("/api/v1/m/sample-test/history/{user_id}")
    suspend fun getTestHistory(@Path("user_id") userId: String): Response<SampleHistoryResponse>
    @POST("/api/v1/m/sample-test/answer")
    suspend fun getTestAnswer(@Body body: TestAnswerRequest): Response<TestAnswerResponse>
    @POST("/api/v1/m/sample-test/save/{user_id}")
    suspend fun getTestSave(@Path("user_id") userId: String, @Body body: TestSaveRequest): Response<TestSaveResponse>

    //interior progress
    @GET("/api/v1/m/interior-progress/detail/{progress_id}")
    suspend fun getInteriorProgressDetail(@Path("progress_id") progressId: String): Response<InteriorDetailResponse>
    @GET("/api/v1/m/interior-progress/history/{user_id}")
    suspend fun getInteriorProgressHistory(@Path("user_id") userId: String): Response<InteriorHistoryResponse>
}