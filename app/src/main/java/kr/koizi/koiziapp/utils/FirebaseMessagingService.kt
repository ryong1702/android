package kr.koizi.koiziapp.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "FirebaseService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.from)

        val prefSwitch = getSharedPreferences("notification_switch", Context.MODE_PRIVATE)
        val switchOn = prefSwitch.getBoolean("switch_on", true)

        // 스위치가 켜져 있을 때만 알림 보냄
        if (switchOn) {
            if(remoteMessage.notification != null ) {
                Log.i("바디: ", remoteMessage.notification?.body.toString())
                Log.i("타이틀: ", remoteMessage.notification?.title.toString())

                // 알림 생성
                sendNotification(remoteMessage)

                // 알림 개수 증가
                val pref = getSharedPreferences("notification", Context.MODE_PRIVATE)
                val count = pref.getInt("count", 0) + 1
                val editor = pref.edit()
                editor.putInt("count", count).apply()
                editor.apply()

                Log.i("알림 개수: ", count.toString())

            } else {
                Log.i("수신에러: ", "data가 비어있습니다. 메시지를 수신하지 못했습니다.")
                Log.i("data값: ", remoteMessage.data.toString())
            }
        }
    }

    override fun onNewToken(token: String) {
        // 새로운 FCM 토큰 수신 시 동작할 코드 작성
        Log.d(TAG, "new Token: $token")

        // 토큰 값을 따로 저장해둔다.
        PreferencesManager(applicationContext).setFCMToken(token)

        Log.i("로그: ", "성공적으로 토큰을 저장함")
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(remoteMessage: RemoteMessage) {
        // RequestCode, Id를 고유값으로 지정하여 알림이 개별 표시되도록 함
        val uniId: Int = (System.currentTimeMillis() / 7).toInt()

        // 일회용 PendingIntent
        // PendingIntent : Intent 의 실행 권한을 외부의 어플리케이션에게 위임한다.
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Activity Stack 을 경로만 남긴다. A-B-C-D-B => A-B
        val pendingIntent = PendingIntent.getActivity(this, uniId, intent, PendingIntent.FLAG_ONE_SHOT)

        // 알림 채널 이름
        val channelId = "firebase_notification_channel_id"

        // 알림 소리
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // 알림에 대한 UI 정보와 작업을 지정한다.
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher) // 아이콘 설정
            .setContentTitle(remoteMessage.notification?.title.toString()) // 제목
            .setContentText(remoteMessage.notification?.body.toString()) // 메시지 내용
            .setAutoCancel(true)
            .setSound(soundUri) // 알림 소리
            .setContentIntent(pendingIntent) // 알림 실행 시 Intent

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 버전 이후에는 채널이 필요하다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 생성
        notificationManager.notify(uniId, notificationBuilder.build())
    }
}