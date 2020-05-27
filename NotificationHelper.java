import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Random;

import sam.myanycar.samsungFire.Loading;
import sam.myanycar.samsungFire.NotificationDeleteBroadcastReceiver;
import sam.myanycar.samsungFire.R;

import static android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT;
import static sam.myanycar.samsungFire.provide.Uinfo.NOTIFICATION_CHANNEL;

public class NotificationHelper {

    private static final int NOTIFICATION_ID = 777;

    /**
     * 실제 알림이 이뤄짐
     * @param context context
     * @param title 타이틀
     * @param message 메시지
     * @param bicTitle
     * @param bicMessage
     * @param bitmap 사진
     * @param map 각종 이벤트 및 링크관련
     */
    private static void displayNotification(Context context, String title, String message, String bicTitle, String bicMessage, Bitmap
            bitmap, Map<?,?> map) {

        //////////////////////////////
        // 파라미터 분기
        //////////////////////////////

        // 타이틀이나 메시지가 널이면 리턴
        if(title == null || message == null) {
            return;
        }

        // 맵 분석용
        String _type = "", _url = "", _contentID = "";

        // 맵이있다면
        if(map != null) {
            _type = (String)map.get(FirebaseUtil.KEY_TYPE);
            _type = _type == null ? "": _type;
            _url = (String)map.get(FirebaseUtil.KEY_URL);
            _url = _url == null ? "": _url;
            _contentID = (String)map.get(FirebaseUtil.KEY_CONTENT_ID);
            _contentID = _contentID == null ? "": _contentID;
        }

        //////////////////////////////
        // 인텐트 생성
        //////////////////////////////
        Intent notifyIntent = new Intent(context, Loading.class);

        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        //////////////////////////////
        // notification 생성
        //////////////////////////////

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
                    .setSmallIcon(_getNotificationSmallIcon())  // 아이콘
                    .setContentTitle(title)         // 타이틀
                    .setContentText(message)
                    .setPriority(PRIORITY_DEFAULT)
                    .setAutoCancel(true)            // 클릭시 제거
                    .setContentIntent(getPendingIntent(context, _contentID, _url))
                    .setContentIntent(notifyPendingIntent);

        Notification notification = mBuilder.build();

        //////////////////////////////
        // 스타일 변경
        //////////////////////////////

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle(mBuilder);
        bigTextStyle.bigText(message);
        bigTextStyle.setBigContentTitle(title);


        // 사진
        if (bitmap != null && _type.equals(FirebaseUtil.TYPE_BIG_PICTURE)) {
            // bicTitle 값이 없으면 title 로 대체
            if(bicTitle == null) {
                bicTitle = title;
            }
            // bicMessage 값이 없으면 message 로 대체
            if(bicMessage == null) {
                bicMessage = message;
            }

            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                    .setBigContentTitle(bicTitle)
                    .setSummaryText(bicMessage);


            mBuilder.setStyle(bigPictureStyle);
        }

        //////////////////////////////
        // 알림 트리거
        //////////////////////////////
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(randomNumber(), notification);

    }

    private static int randomNumber() {
        // 공통진행
        Random random = new Random();
        int randomID = random.nextInt(1000000);
        return randomID;
    }

    /**
     * 패딩인텐트 만들기
     *
     * @param context
     * @param contentID // 파이어배에스 데이터에서 받은 알림 고유 ID
     * @return PendingIntent
     */

    private static PendingIntent getPendingIntent(Context context, String contentID, String url) {
        Intent intent = new Intent(context, Loading.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (url != null && !url.equals("")) intent.putExtra("URL",url);
        if (contentID != null && !contentID.equals("")) intent.putExtra(FirebaseUtil.KEY_CONTENT_ID,contentID);

        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    /**
     * 패딩인텐트 만들기 - 알림 지우기전용(브로드케스트)
     *
     * @param context
     * @param contentID // 파이어베이스 데이터에서 받은 알림 고유 ID
     * @return PendingIntent
     */
    private static PendingIntent getDeletePendingIntent(Context context, String contentID){
        Intent intent = new Intent(context, NotificationDeleteBroadcastReceiver.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (contentID != null && !contentID.equals("")){
            intent.putExtra(FirebaseUtil.KEY_CONTENT_ID,contentID);
        }
        return PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
    }

    /**
     * 알림 전용 작은아이콘 가져오기
     *
     * @return int R주소
     */

    private static int _getNotificationSmallIcon() {
//		boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
//		return useWhiteIcon ? R.drawable.android_icon_136 : R.drawable.icon;
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= 25);
//		return useWhiteIcon ? R.drawable.android_icon_136 : R.drawable.icon;
        return useWhiteIcon ? R.drawable.ic_direct_136 : R.drawable.icon;
    }

    /**
     *  타이틀, 메시지만 있는 기본 스타일
     * @param context context
     * @param title 타이틀
     * @param message 메시지
     */
    public static void displayNotification(Context context, String title, String message) {
        displayNotification(context, title, message, null, null, null, null);
    }

    /**
     * 이미지, 맵이있는 빅픽쳐 스타일
     * @param context context
     * @param bitmap 비트맵
     * @param map 객체 - 타이틀과 메시지
     */
    private static void displayNotification(Context context, Bitmap bitmap, Map<?,?> map) {
        String title = (String)map.get("TITLE");
        String message = (String)map.get("CONTENT");
        String bicTitle = (String)map.get("BIG_TITLE");
        String bicMessage = (String)map.get("BIG_CONTENT");
        displayNotification(context, title, message, bicTitle, bicMessage, bitmap, map);
    }

    /**
     * 비트맵 존재시 가져오기를 위한 분기문
     *
     * @param context context
     * @param map - 파이어베이스에서 온 데이터 맵
     */
    public static void displayNotification(Context context, Map<?,?> map){

        String bitmapUrl = (String)map.get("BIG_PICTURE");
        Bitmap bitmap = null;
        // image 가 존재할 경우
        if(bitmapUrl != null){
            bitmap = _getBitmapFromUrl(bitmapUrl);
        }

        displayNotification(context, bitmap, map);
    }

    /**
     * 비트맵 URL 로부터 불러오기
     * @param imageUrl url
     * @return Bitmap
     */
    private static Bitmap _getBitmapFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }


}
