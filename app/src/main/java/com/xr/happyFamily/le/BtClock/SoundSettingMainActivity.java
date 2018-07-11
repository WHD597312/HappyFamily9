package com.xr.happyFamily.le.BtClock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.xr.happyFamily.R;

public class SoundSettingMainActivity extends PreferenceActivity {

    private static final int SMS_RINGTONE_PICKED = 1;
    private static final int PHONE_RINGTONE_PICKED = 2;
    private static final int ALARM_RINGTONE_PICKED = 3;
    private static final int SDCARD_RINGTONE_PICKED = 4;

    public static final String NOTIFICATION_RINGTONE    = "pref_notification_ringtone";
    public static final String NOTIFICATION_RINGTONE_TITLE_NAME = "pref_notification_ringtone_name";
    public static final String PHONE_RINGTONE    = "pref_phone_ringtone";
    public static final String PHONE_RINGTONE_TITLE_NAME    = "pref_phone_ringtone_title_name";
    public static final String ALARM_RINGTONE    = "pref_alarm_ringtone";
    public static final String ALARM_RINGTONE_TITLE_NAME    = "pref_alarm_ringtone_title_name";
    public static final String SDCARD_RINGTONE    = "pref_sdcard_ringtone";
    public static final String SDCARD_RINGTONE_TITLE_NAME    = "pref_sdcard_ringtone_title_name";

    private String notificationStr;
    private String phoneStr;
    private String alarmStr;
    private String sdcardStr;

    private Preference mMmsSoundsPref;
    private Preference mPhoneSoundsPref;
    private Preference mAlarmSoundsPref;
    private Preference mSdcardSoundsPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        setMessagePreferences();
        setDefaultPreferences();
    }

    private void setMessagePreferences() {
        mMmsSoundsPref = findPreference("pref_sms_ringtone");
        mPhoneSoundsPref = findPreference("pref_phone_ringtone");
        mAlarmSoundsPref = findPreference("pref_alarm_ringtone");
        mSdcardSoundsPref = findPreference("pref_sdcard_ringtone");
    }

    private void setDefaultPreferences(){
        SharedPreferences innersharedPreferences = PreferenceManager.getDefaultSharedPreferences(SoundSettingMainActivity.this);
        String notificationRingtoneTitleName = innersharedPreferences.getString(NOTIFICATION_RINGTONE_TITLE_NAME, null);
        if(notificationRingtoneTitleName!=null){
            mMmsSoundsPref.setSummary(notificationRingtoneTitleName);
        }else{
            mMmsSoundsPref.setSummary(getString(R.string.pref_summary_notification_ringtone));
        }

        String phoneRingtoneTitleName = innersharedPreferences.getString(PHONE_RINGTONE_TITLE_NAME, null);
        if(phoneRingtoneTitleName!=null){
            mPhoneSoundsPref.setSummary(phoneRingtoneTitleName);
        }else{
            mPhoneSoundsPref.setSummary(getString(R.string.pref_summary_phone_ringtone));
        }

        String alarmRingtoneTitleName = innersharedPreferences.getString(ALARM_RINGTONE_TITLE_NAME, null);
        if(alarmRingtoneTitleName!=null){
            mAlarmSoundsPref.setSummary(alarmRingtoneTitleName);
        }else{
            mAlarmSoundsPref.setSummary(getString(R.string.pref_summary_alarm_ringtone));
        }

        String sdcardRingtoneTitleName = innersharedPreferences.getString(SDCARD_RINGTONE_TITLE_NAME, null);
        if(sdcardRingtoneTitleName!=null){
            mSdcardSoundsPref.setSummary(sdcardRingtoneTitleName);
        }else{
            mSdcardSoundsPref.setSummary(getString(R.string.pref_summary_sdcard_ringtone));
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
                                         Preference preference) {
        if (preference == mMmsSoundsPref){
            doPickSmsRingtone();
        }
        else if(preference == mPhoneSoundsPref){
            doPickPhoneRingtone();
        }
        else if(preference == mAlarmSoundsPref){
            doPickAlarmRingtone();
        }
        else if(preference == mSdcardSoundsPref){
            doPickSdcardRingtone();
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private void doPickSmsRingtone(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        notificationStr = sharedPreferences.getString(NOTIFICATION_RINGTONE, null);

        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        // Allow user to pick 'Default'
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        // Show only ringtones
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        //set the default Notification value
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        // Don't show 'Silent'
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);

        Uri notificationUri;
        if (notificationStr != null) {
            notificationUri = Uri.parse(notificationStr);
            // Put checkmark next to the current ringtone for this contact
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, notificationUri);
        } else {
            // Otherwise pick default ringtone Uri so that something is selected.
            notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            // Put checkmark next to the current ringtone for this contact
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, notificationUri);
        }

        // Launch!
        startActivityForResult(intent, SMS_RINGTONE_PICKED);
    }

    private void doPickPhoneRingtone(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        phoneStr = sharedPreferences.getString(PHONE_RINGTONE, null);

        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        // Allow user to pick 'Default'
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        // Show only ringtones
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
        //set the default Notification value
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        // Don't show 'Silent'
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);

        Uri phoneUri;
        if (phoneStr != null) {
            phoneUri = Uri.parse(phoneStr);
            // Put checkmark next to the current ringtone for this contact
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, phoneUri);
        } else {
            // Otherwise pick default ringtone Uri so that something is selected.
            phoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            // Put checkmark next to the current ringtone for this contact
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, phoneUri);
        }

        startActivityForResult(intent, PHONE_RINGTONE_PICKED);
    }

    private void doPickAlarmRingtone(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        alarmStr = sharedPreferences.getString(ALARM_RINGTONE, null);

        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        // Allow user to pick 'Default'
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        // Show only ringtones
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        //set the default Notification value
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        // Don't show 'Silent'
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);

        Uri alarmUri;
        if (alarmStr != null) {
            alarmUri = Uri.parse(alarmStr);
            // Put checkmark next to the current ringtone for this contact
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, alarmUri);
        } else {
            // Otherwise pick default ringtone Uri so that something is selected.
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            // Put checkmark next to the current ringtone for this contact
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, alarmUri);
        }

        startActivityForResult(intent, ALARM_RINGTONE_PICKED);
    }

    private void doPickSdcardRingtone(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sdcardStr = sharedPreferences.getString(SDCARD_RINGTONE, null);

        Uri sdcardUri = null;
        if (sdcardStr != null) {
            sdcardUri = Uri.parse(sdcardStr);
        }

        Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        innerIntent.setType("audio/*");
        //you could lookup the framework the type of audio,if you don`t want use the Recorder use the note code
//        innerIntent.setType("audio/aac");
//        innerIntent.setType("audio/mp3");
//        innerIntent.setType("audio/midi");
        // Put checkmark next to the current ringtone for this contact
        innerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, sdcardUri);
        Intent wrapperIntent = Intent.createChooser(innerIntent, null);
        startActivityForResult(wrapperIntent, SDCARD_RINGTONE_PICKED);
    }

    @Override
    protected void onResume() {
        setDefaultPreferences();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case SMS_RINGTONE_PICKED:{
                Uri pickedUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if(null == pickedUri){
                    editor.putString(NOTIFICATION_RINGTONE_TITLE_NAME, getString(R.string.select_ringtone_slient));
                    editor.putString(NOTIFICATION_RINGTONE, null);
                    editor.commit();
                }else{
                    Ringtone ringtone =  RingtoneManager.getRingtone(SoundSettingMainActivity.this, pickedUri);
                    String strRingtone = ringtone.getTitle(SoundSettingMainActivity.this);
                    editor.putString(NOTIFICATION_RINGTONE_TITLE_NAME, strRingtone);
                    editor.putString(NOTIFICATION_RINGTONE, pickedUri.toString());
                    editor.commit();
                }
                break;
            }
            case PHONE_RINGTONE_PICKED:{
                Uri pickedUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if(null == pickedUri){
                    editor.putString(PHONE_RINGTONE_TITLE_NAME, getString(R.string.select_ringtone_slient));
                    editor.putString(PHONE_RINGTONE, null);
                    editor.commit();
                }else{
                    phoneStr = pickedUri.toString();
                    Ringtone ringtone =  RingtoneManager.getRingtone(SoundSettingMainActivity.this, pickedUri);
                    String strRingtone = ringtone.getTitle(SoundSettingMainActivity.this);
                    editor.putString(PHONE_RINGTONE_TITLE_NAME, strRingtone);
                    editor.putString(PHONE_RINGTONE, pickedUri.toString());
                    editor.commit();
                }
                break;
            }
            case ALARM_RINGTONE_PICKED:{
                Uri pickedUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if(null == pickedUri){
                    editor.putString(ALARM_RINGTONE_TITLE_NAME, getString(R.string.select_ringtone_slient));
                    editor.putString(ALARM_RINGTONE, null);
                    editor.commit();
                }else{
                    Ringtone ringtone =  RingtoneManager.getRingtone(SoundSettingMainActivity.this, pickedUri);
                    String strRingtone = ringtone.getTitle(SoundSettingMainActivity.this);
                    editor.putString(ALARM_RINGTONE_TITLE_NAME, strRingtone);
                    editor.putString(ALARM_RINGTONE, pickedUri.toString());
                    editor.commit();
                }
                break;
            }
            case SDCARD_RINGTONE_PICKED:{
                Uri pickedUri = data.getData();
                if(null != pickedUri){
                    notificationStr = pickedUri.toString();
                    Ringtone ringtone =  RingtoneManager.getRingtone(SoundSettingMainActivity.this, pickedUri);
                    String strRingtone = ringtone.getTitle(SoundSettingMainActivity.this);
                    editor.putString(SDCARD_RINGTONE_TITLE_NAME, strRingtone);
                    editor.putString(SDCARD_RINGTONE, pickedUri.toString());
                    editor.commit();
                }
                break;
            }
            default:break;
        }
    }
}
