package com.wuzuqing.component_base.util.notifyutil;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.wuzuqing.component_base.util.ObjectUtils;

import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/2/13 0013.
 */

public class NotifyUtil {
    public static Context context;

    public static NotificationManager getNm() {
        return nm;
    }

    private static NotificationManager nm;

    public static void init(Context context1) {
        context = context1;
        nm = (NotificationManager) context1
                .getSystemService(Activity.NOTIFICATION_SERVICE);
    }

    public static SingleLineBuilder buildSimple(int id, int smallIcon, CharSequence contentTitle, CharSequence contentText, PendingIntent contentIntent) {
        SingleLineBuilder builder = new SingleLineBuilder();
        builder.setBase(smallIcon, contentTitle, contentText)
                .setId(id)
                .setContentIntent(contentIntent);
        return builder;
    }

    @Deprecated
    public static ProgressBuilder buildProgress(int id, int smallIcon, CharSequence contentTitle, int progress, int max) {
        ProgressBuilder builder = new ProgressBuilder();
        builder.setBase(smallIcon, contentTitle, progress + "/" + max)
                .setId(id);
        builder.setProgress(max, progress, false);
        return builder;
    }

    public static ProgressBuilder buildProgress(int id, int smallIcon, CharSequence contentTitle, int progress, int max, String format) {
        ProgressBuilder builder = new ProgressBuilder();
        builder.setBase(smallIcon, contentTitle, progress + "/" + max)
                .setId(id);
        builder.setProgressAndFormat(progress, max, false, format);
        return builder;
    }

    public static BigPicBuilder buildBigPic(int id, int smallIcon, CharSequence contentTitle, CharSequence contentText, CharSequence summaryText) {
        BigPicBuilder builder = new BigPicBuilder();
        builder.setBase(smallIcon, contentTitle, contentText).setId(id);
        builder.setSummaryText(summaryText);
        return builder;
    }

    public static BigTextBuilder buildBigText(int id, int smallIcon, CharSequence contentTitle, CharSequence contentText) {
        BigTextBuilder builder = new BigTextBuilder();
        builder.setBase(smallIcon, contentTitle, contentText).setId(id);
        //builder.setSummaryText(summaryText);
        return builder;
    }

    public static MailboxBuilder buildMailBox(int id, int smallIcon, CharSequence contentTitle) {
        MailboxBuilder builder = new MailboxBuilder();
        builder.setBase(smallIcon, contentTitle, "").setId(id);
        return builder;
    }

    public static MediaBuilder buildMedia(int id, int smallIcon, CharSequence contentTitle, CharSequence contentText) {
        MediaBuilder builder = new MediaBuilder();
        builder.setBase(smallIcon, contentTitle, contentText).setId(id);
        return builder;
    }
    /*public static CustomViewBuilder buildCustomView(BigPicBuilder builder){

    }*/

    public static void notify(int id, Notification notification) {
        nm.notify(id, notification);
    }

    public static PendingIntent buildIntent(Class clazz) {
        return buildIntent(clazz, null);
    }

    public static PendingIntent buildIntent(Class clazz, Map<String, Object> params) {
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        Intent intent = new Intent(NotifyUtil.context, clazz);
        if (ObjectUtils.isNotEmpty(params)) {
            putExtra(intent, params);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pi = PendingIntent.getActivity(NotifyUtil.context, 0, intent, flags);
        return pi;
    }

    private static void putExtra(Intent intent, Map<String, Object> params) {
        Set<String> ketSet = params.keySet();
        for (String key : ketSet) {
            Object value = params.get(key);
            if (value instanceof String) {
                intent.putExtra(key, (String) value);
            } else if (value instanceof Boolean) {
                intent.putExtra(key, (Boolean) value);
            } else if (value instanceof Integer) {
                intent.putExtra(key, (Integer) value);
            }
        }
    }

    public static void cancel(int id) {
        if (nm != null) {
            nm.cancel(id);
        }
    }

    public static void cancelAll() {
        if (nm != null) {
            nm.cancelAll();
        }
    }


}
