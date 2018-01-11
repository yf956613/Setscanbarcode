package com.setscanbarcode;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemProperties;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.setscanbarcode.adapter.ContentAdapter;
import com.setscanbarcode.bean.ContentBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import win.reginer.adapter.CommonRvAdapter;


public class MainActivity extends AppCompatActivity implements CommonRvAdapter.OnItemClickListener {

    private ContentAdapter mAdapter;
    private List<ContentBean> mList = new ArrayList<>();
    SharedPreferencesUtil preferencesUtil;
    boolean[] boolArr;
    boolean[] display;
    String TAG = "SETSCAN";
    private String INIT_SERVICE = "com.scanservice.init";

    private ContentBean contentBean1;
    private ContentBean contentBean2;
    private ContentBean contentBean3;
    private ContentBean contentBean4;
    private ContentBean contentBean5;
    private ContentBean contentBean6;
    private ContentBean contentBean7;
    private ContentBean contentBean8;
    private ContentBean contentBean9;
    private ContentBean contentBean10;
    private ContentBean contentBean11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferencesUtil = SharedPreferencesUtil.getInstance(this, "setscan");

        initData();
        intentFilter();
        boolArr = new boolean[items.length];
        display = new boolean[displayItems.length];
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initData() {

        displayItems = new String[]{getString(R.string.scanning_head_decode),
                getString(R.string.rear_camera_decode), getString(R.string.close_quick_scan)};

        //1.启动快捷扫描
        contentBean1 = new ContentBean();
        contentBean1.setTitle(getString(R.string.scan_key));
        contentBean1.setDescribe(preferencesUtil.read(jiluxuanze, getString(R.string.click_to_select_scan_head_or_turn_off_scan)));
        contentBean1.setTvVisible(true);
        contentBean1.setCbVisible(false);
        contentBean1.setCheck(preferencesUtil.read(isEnable, false));
        mList.add(contentBean1);

        //2.文字：扫描成功
        ContentBean contentBean101 = new ContentBean();
        contentBean101.setTitle("");
        contentBean101.setDescribe(getString(R.string.scanning_success));
        contentBean101.setTvVisible(true);
        contentBean101.setCbVisible(false);
        contentBean101.setCheck(false);
        mList.add(contentBean101);

        //3.焦点显示：显示扫描结果
        contentBean2 = new ContentBean();
        contentBean2.setTitle(getString(R.string.scan_results));
        contentBean2.setDescribe(getString(R.string.describe_scan_results));
        contentBean2.setTvVisible(true);
        contentBean2.setCbVisible(true);
        contentBean2.setCheck(preferencesUtil.read(isShowdecode, true));
        mList.add(contentBean2);

        //4.提示音
        contentBean3 = new ContentBean();
        contentBean3.setTitle(getString(R.string.scan_sound));
        contentBean3.setDescribe(getString(R.string.describe_scan_sound));
        contentBean3.setTvVisible(true);
        contentBean3.setCbVisible(true);
        contentBean3.setCheck(preferencesUtil.read(isSound, true));
        mList.add(contentBean3);

        //5.震动
        contentBean4 = new ContentBean();
        contentBean4.setTitle(getString(R.string.scan_vibration));
        contentBean4.setDescribe(getString(R.string.describe_scan_vibration));
        contentBean4.setTvVisible(true);
        contentBean4.setCbVisible(true);
        contentBean4.setCheck(preferencesUtil.read(isVibrator, true));
        mList.add(contentBean4);

        //6.保存图片
        contentBean5 = new ContentBean();
        contentBean5.setTitle(getString(R.string.save_image));
        contentBean5.setDescribe(getString(R.string.describe_save_image));
        contentBean5.setTvVisible(true);
        contentBean5.setCbVisible(true);
        contentBean5.setCheck(preferencesUtil.read(isSaveImage, false));
        mList.add(contentBean5);

        //7.文字：扫描设置
        ContentBean contentBean102 = new ContentBean();
        contentBean102.setTitle("");
        contentBean102.setDescribe(getString(R.string.scan_settings));
        contentBean102.setTvVisible(true);
        contentBean102.setCbVisible(false);
        contentBean102.setCheck(false);
        mList.add(contentBean102);

        //8.连续扫描
        contentBean6 = new ContentBean();
        contentBean6.setTitle(getString(R.string.continuous_scan));
        contentBean6.setDescribe(getString(R.string.describe_continuous_scan));
        contentBean6.setTvVisible(true);
        contentBean6.setCbVisible(true);
        contentBean6.setCheck(preferencesUtil.read(isContinuous, false));
        mList.add(contentBean6);

        //9.瞄准灯
        contentBean7 = new ContentBean();
        contentBean7.setTitle(getString(R.string.aiming_lamp));
        contentBean7.setDescribe(getString(R.string.aiming_lamp_description));
        contentBean7.setTvVisible(true);
        contentBean7.setCbVisible(true);
        contentBean7.setCheck(preferencesUtil.read(miaozhundeng, true));
        mList.add(contentBean7);

        //10.补光灯
        contentBean8 = new ContentBean();
        contentBean8.setTitle(getString(R.string.fill_light));
        contentBean8.setDescribe(getString(R.string.white_light));
        contentBean8.setCheck(preferencesUtil.read(isFlash, true));
        contentBean8.setTvVisible(true);
        contentBean8.setCbVisible(true);
        mList.add(contentBean8);

        //11.条码类型
        contentBean9 = new ContentBean();
        contentBean9.setTitle(getString(R.string.barcode_type));
        contentBean9.setTvVisible(false);
        contentBean9.setCbVisible(false);
        contentBean9.setCheck(false);
        mList.add(contentBean9);

        //12.自定义前缀
        contentBean10 = new ContentBean();
        contentBean10.setTitle(getString(R.string.custom_prefix));
        contentBean10.setTvVisible(false);
        contentBean10.setCbVisible(false);
        contentBean10.setCheck(false);
        mList.add(contentBean10);

        //13.自定义后缀
        contentBean11 = new ContentBean();
        contentBean11.setTitle(getString(R.string.custom_suffix));
        contentBean11.setDescribe("");
        contentBean11.setTvVisible(false);
        contentBean11.setCbVisible(false);
        contentBean11.setCheck(false);
        mList.add(contentBean11);

        contentBean1.setEnable(true);
        contentBean2.setEnable(true);
        contentBean3.setEnable(true);
        contentBean4.setEnable(true);
        contentBean5.setEnable(true);
        contentBean6.setEnable(true);
        contentBean7.setEnable(true);
        contentBean8.setEnable(true);
        contentBean9.setEnable(true);
        contentBean10.setEnable(true);
        contentBean11.setEnable(true);

        initEnable();
    }

    //修改设置选项可点击与不可点击
    private void initEnable() {
        if (preferencesUtil.read(isEnable, true)) {

            contentBean2.setEnable(true);
            contentBean3.setEnable(true);
            contentBean4.setEnable(true);
            contentBean5.setEnable(true);
            contentBean6.setEnable(true);
            contentBean7.setEnable(true);
            contentBean8.setEnable(true);
            contentBean9.setEnable(true);
            contentBean10.setEnable(true);
            contentBean11.setEnable(true);
        } else {

            contentBean2.setEnable(false);
            contentBean3.setEnable(false);
            contentBean4.setEnable(false);
            contentBean5.setEnable(false);
            contentBean6.setEnable(false);
            contentBean7.setEnable(false);
            contentBean8.setEnable(false);
            contentBean9.setEnable(false);
            contentBean10.setEnable(false);
            contentBean11.setEnable(false);
        }
    }

    private void initView() {
        mAdapter = new ContentAdapter(MainActivity.this, R.layout.adapter_content, mList);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv_content);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.notifyDataSetChanged();
    }


    private void sendBroadcast(String stirng, boolean b) {
        Intent intent = new Intent();
        intent.setAction(stirng);
        intent.putExtra("enableDecode", b);
        sendBroadcast(intent);
    }

    private void sendBroadcasts(String stirng, String s) {
        Intent intent = new Intent();
        intent.setAction(stirng);
        intent.putExtra("enableDecode", s);
        sendBroadcast(intent);
    }

    private static String isFront = "isfront";
    private static String isEnable = "isenable";
    private static String isShowdecode = "isshowdecode";
    private static String isSound = "issound";
    private static String isVibrator = "isvibrator";
    private static String isFlash = "isflash";
    private static String isContinuous = "iscontinuous";
    private static String isSaveImage = "issaveimage";
    private static String qianzhui = "qianzhui";
    private static String houzhui = "houzhui";

    //调整设置界面，修改显示效果
    private static String jiluxuanze = "jiluxuanze";
    private static String miaozhundeng = "miaozhundeng";
    private static String buguangdeng = "buguangdeng";

    private int position;

    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int position) {
        mList.get(position).setCheck(!mList.get(position).isCheck());
        boolean b = mList.get(position).isCheck();
        this.position = position;
        Log.d(TAG, "点击了:" + position );
        switch (position) {
            case 0:
                if(isWorked(this, "com.scanbarcodeservice.FxService")){
                    break;
                }
                //弹出对话框，选择摄像头解码或关闭。
                // TODO: 2018/1/2 弹出对话框，详细设置在对话框选择后修改
                    DisplayCameraSelection();

//                if (b) {
//                    Intent BarcodeIntent = new Intent();
//                    ComponentName cn = new ComponentName("com.scanservice","com.scanbarcodeservice.ScanServices");
//                    BarcodeIntent.setComponent(cn);
//                    // TODO: 2018/1/2 向intent中添加前后置的请求，可以直接发给服务，让服务获取intent后选取前后置
//                    this.startService(BarcodeIntent);
//                    preferencesUtil.write(isEnable, b);
//                    SystemProperties.set("persist.sys.keyreport", "true");
//                    initEnable();
//
//                } else {
//                    Intent BarcodeIntent = new Intent();
//                    ComponentName cn = new ComponentName("com.scanservice","com.scanbarcodeservice.ScanServices");
//                    BarcodeIntent.setComponent(cn);
//                    this.stopService(BarcodeIntent);
//                    preferencesUtil.write(isEnable, b);
//                    SystemProperties.set("persist.sys.keyreport","false");
//                    initEnable();
//                }
                mAdapter.notifyDataSetChanged();
                break;

            case 1:
                //不可点击的文字描述：扫描成功
                break;
//                //使用前置摄像头
//                if(isWorked(this, "com.scanbarcodeservice.FxService")){
//                    break;
//                }
//
//                if (b) {
//                    if (contentBean6.isCheck()) {
//                        sendBroadcast("com.setscan.flash", false);
//                        contentBean6.setCheck(false);
//                        preferencesUtil.write(isFlash, false);
//                    }
//                    SystemProperties.set("persist.sys.scancamera", "front");
//                    sendBroadcast("com.setscan.front", true);
//                    preferencesUtil.write(isFront, b);
//                    contentBean6.setEnable(false);
//                } else {
//                    SystemProperties.set("persist.sys.scancamera", "back");
//                    sendBroadcast("com.setscan.front", false);
//                    preferencesUtil.write(isFront, b);
//                    contentBean6.setEnable(true);
//                }
//                break;

            case 2:
                //焦点扫描，显示扫描结果
                if (b) {
                    sendBroadcast("com.setscan.showdecode", true);
                    preferencesUtil.write(isShowdecode, b);
                } else {
                    sendBroadcast("com.setscan.showdecode", false);
                    preferencesUtil.write(isShowdecode, b);
                }
                break;
            case 3:
                //提示音
                if (b) {
                    sendBroadcast("com.setscan.sound", true);
                    preferencesUtil.write(isSound, b);
                } else {
                    sendBroadcast("com.setscan.sound", false);
                    preferencesUtil.write(isSound, b);
                }
                break;
            case 4:
                //震动
                if (b) {
                    sendBroadcast("com.setscan.vibrator", true);
                    preferencesUtil.write(isVibrator, b);
                } else {
                    sendBroadcast("com.setscan.vibrator", false);
                    preferencesUtil.write(isVibrator, b);
                }
                break;
            case 5:
                //保存图片
                if (b) {
                    sendBroadcast("com.setscan.issaveimage", true);
                    preferencesUtil.write(isSaveImage, b);
                } else {
                    sendBroadcast("com.setscan.issaveimage", false);
                    preferencesUtil.write(isSaveImage, b);
                }
                break;
            case 6:
                //不可点击的文字提示：扫描设置
                break;
            case 7:
                //连续扫描
                if (b) {
                    sendBroadcast("com.setscan.continuous", true);
                    preferencesUtil.write(isContinuous, b);
                } else {
                    sendBroadcast("com.setscan.continuous", false);
                    preferencesUtil.write(isContinuous, b);
                }
                break;
            case 8:
                //瞄准灯，同补光灯一样。当后置时，此选项不可用
                if(SystemProperties.get("persist.sys.scancamera").equals("back")) {
                    break;
                }
                if (b) {
                    sendBroadcast("com.setscan.miaozhundeng", true);
                    preferencesUtil.write(miaozhundeng, b);
                } else {
                    sendBroadcast("com.setscan.miaozhundeng", false);
                    preferencesUtil.write(miaozhundeng, b);
                }
                break;
            case 9:
                //补光灯
                // TODO: 2018/1/2 判断一下，后置正在扫描时不可修改
                if(isWorked(this, "com.scanbarcodeservice.FxService")){
                    break;
                }

                if (b) {
                    sendBroadcast("com.setscan.flash", true);
                    preferencesUtil.write(isFlash, b);
                } else {
                    sendBroadcast("com.setscan.flash", false);
                    preferencesUtil.write(isFlash, b);
                }
                break;
            case 10:
                showMultiChoiceItems();
                break;
            case 11:
                showInputDialog(getString(R.string.custom_prefix));
                break;
            case 12:
                showInputDialog(getString(R.string.custom_suffix));
                break;

                default:
                    break;
        }

        mAdapter.notifyDataSetChanged();
    }

    private void showInputDialog(String title) {
        final EditText editText = new EditText(this);
        final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title).setView(editText).setPositiveButton(getString(R.string.dialog_sure), null)
                .setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        //这里必须要先调show()方法，后面的getButton才有效
        dialog.show();

        if(position == 11){
            editText.setText(preferencesUtil.read(qianzhui, ""));
        }else if(position == 12){
            editText.setText(preferencesUtil.read(houzhui, ""));
        }
        //将光标移动到最后显示最下面的信息.
        Editable text = editText.getText();
        Selection.setSelection(text, text.length());


        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                if(s.length() > 20) {
                    Toast.makeText(MainActivity.this, getString(R.string.scan_too_long_toast), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (position == 11) {
                    preferencesUtil.write(qianzhui, s);
                    sendBroadcasts("com.setscan.qianzhui", s);
                } else if (position == 12) {
                    preferencesUtil.write(houzhui, s);
                    sendBroadcasts("com.setscan.houzhui", s);
                }
                dialog.dismiss();
            }
        });

//        final EditText editText = new EditText(this);
//        final AlertDialog.Builder inputDialog = new AlertDialog.Builder(this);
//
//        inputDialog.setTitle(title).setView(editText);
//
//        if(position == 11){
//            editText.setText(preferencesUtil.read(qianzhui, ""));
//        }else if(position == 12){
//            editText.setText(preferencesUtil.read(houzhui, ""));
//        }
//        //将光标移动到最后显示最下面的信息.
//        Editable text = editText.getText();
//        Selection.setSelection(text, text.length());
//
//
//        inputDialog.setPositiveButton(getString(R.string.dialog_sure), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String s = editText.getText().toString();
//
//                if (position == 11) {
//                    preferencesUtil.write(qianzhui, s);
//                    sendBroadcasts("com.setscan.qianzhui", s);
//                } else if (position == 12) {
//                    preferencesUtil.write(houzhui, s);
//                    sendBroadcasts("com.setscan.houzhui", s);
//                }
//            }
//        }).setNegativeButton(getString(R.string.dialog_cancel), null).show();

    }



    private ListView lv; //加上ALL共47个 0-46
    private final String[] items = {"UPCA", "UPCA_2CHAR_ADDENDA", "UPCA_5CHAR_ADDENDA", "UPCE0", "UPCE1",
            "UPCE_EXPAND", "UPCE_2CHAR_ADDENDA", "UPCE_5CHAR_ADDENDA", "EAN8", "EAN8_2CHAR_ADDENDA",
            "EAN8_5CHAR_ADDENDA", "EAN13", "EAN13_2CHAR_ADDENDA", "EAN13_5CHAR_ADDENDA", "EAN13_ISBN",
            "CODE128", "GS1_128", "C128_ISBT", "CODE39", "COUPON_CODE", "TRIOPTIC", "I25", "S25", "IATA25",
            "M25", "CODE93", "CODE11", "CODABAR", "TELEPEN", "MSI", "RSS_14", "RSS_LIMITED", "RSS_EXPANDED",
            "CODABLOCK_F", "PDF417", "MICROPDF", "COMPOSITE", "COMPOSITE_WITH_UPC", "AZTEC", "MAXICODE",
            "DATAMATRIX", "DATAMATRIX_RECTANGLE", "QR", "HANXIN", "HK25", "KOREA_POST", "ALL"};


    private String[] displayItems = new String[3];



    /**
     * 条码类型的选择，弹出对话框来确定
     */
    private void showMultiChoiceItems() {
        Log.i(TAG, "showMultiChoiceItems: " + Arrays.toString(boolArr));
        for (int i = 0; i < boolArr.length; i++) {
            if (i >= 8 && i <= 15 || i == 42){
                boolArr[i] = preferencesUtil.read("decodetype" + i, true);
                Log.i(TAG, "showMultiChoiceItems: " + boolArr[i]);
            } else {
                boolArr[i] = preferencesUtil.read("decodetype" + i, false);
                Log.i(TAG, "showMultiChoiceItems: " + boolArr[i]);
            }

        }
        final AlertDialog builder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_title))
                .setMultiChoiceItems(items, boolArr, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//                        boolArr[which] = isChecked;
                        if (which != 46 && isChecked) {

                            boolArr[which] = isChecked;
                        } else if (which != 46 && !isChecked) {
                            SparseBooleanArray sb;
                            sb = lv.getCheckedItemPositions();
                                if (!sb.get(46)) {
                                    lv.setItemChecked(46, false);
                                }
                            boolArr[which] = isChecked;

                        }

                        if (which == 46 && isChecked){ //如果全选，则为全选
                            //把显示的勾选全置为ture
                            SparseBooleanArray sb;
                            sb = lv.getCheckedItemPositions();
                            for (int j = 0; j <= 46; j++) {
                                if (!sb.get(j)) {
                                    lv.setItemChecked(j, true);
                                }
                            }
                            for (int i = 0; i < 47; i++) {
                                boolArr[i] = true;

                            }
                        } else if (which == 46 && !isChecked) {
                            //把显示的勾选全置为false
                            for (int i = 0; i < boolArr.length; i++) {
                                boolArr[i] = false;
                            }
                            lv.clearChoices();
                            lv.setSelection(46);


                        }


                    }
                }).setPositiveButton(getString(R.string.dialog_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int j = 0;
                        String[] decodeTypes = new String[boolArr.length];
                        for (int i = 0; i < boolArr.length; i++) {
                                decodeTypes[j] = items[i];
                                preferencesUtil.write("decodetype" + i, boolArr[i]);
                                j++;
                        }

                        Intent intent = new Intent();
                        intent.setAction("com.setscan.decodetype");
                        Bundle bundle = new Bundle();
                        bundle.putStringArray("enableDecode", decodeTypes);
                        bundle.putBooleanArray("enableflag", boolArr);
                        intent.putExtras(bundle);
                        sendBroadcast(intent);

                    }
                }).setNegativeButton(getString(R.string.dialog_cancel), null).create();
        lv = builder.getListView();
        builder.show();

    }


    /**
     * 点击快捷扫描时，显示的界面内容及点击效果,此为单选
     */
    private void DisplayCameraSelection() {
        Log.i(TAG, "DisplayCameraSelection: " + Arrays.toString(display));
        int choose = 2;
        for (int i = 0; i < display.length; i++) {
            display[i] = preferencesUtil.read("displaytype" + i, false);
            if (display[i]){
                choose = i;
            }
            Log.i(TAG, "DisplayCameraSelection: " + display[i]);
        }

        //准备显示3个选项，极其相关使能的关系
        final AlertDialog builder = new AlertDialog.Builder(this)
                .setTitle(R.string.select_the_scan_head_or_close_the_quick_scan)
                .setSingleChoiceItems(displayItems, choose, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0) {

                            display[which] = true;
                            display[1] = false;
                            display[2] = false;

                        } else if (which == 1 ) {

                            display[0] = false;
                            display[which] = true;
                            display[2] = false;

                        } else if (which == 2 ) {

                            display[0] = false;
                            display[1] = false;
                            display[which] = true;

                        }
                    }

                }).setPositiveButton(getString(R.string.dialog_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for (int i = 0; i < display.length; i++) {
                            preferencesUtil.write("displaytype" + i, display[i]);
                        }

                        //发广播，判断一下服务起没起，没起来就起服务，起来了就只改摄像头

//                        Intent intent = new Intent();
//                        intent.setAction("com.setscan.decodetype");
//                        Bundle bundle = new Bundle();
//                        bundle.putStringArray("enableDecode", decodeTypes);
//                        bundle.putBooleanArray("enableflag", boolArr);
//                        intent.putExtras(bundle);
//                        sendBroadcast(intent);


                        if (display[0] ||display[1]){
                            //扫描头0，摄像头1
                            if (isWorked(MainActivity.this, "com.scanbarcodeservice.ScanServices")) {
                                //在运行就只发广播
                                if (display[0]){
                                    SystemProperties.set("persist.sys.scancamera", "front");
                                    sendBroadcast("com.setscan.front", true);
                                    preferencesUtil.write(isFront, true);
                                    preferencesUtil.write(jiluxuanze, getString(R.string.scanning_head_decode));
                                    contentBean1.setDescribe(preferencesUtil.read(jiluxuanze, getString(R.string.scanning_head_decode)));
                                    contentBean7.setEnable(true);
                                } else if (display[1]){
                                    SystemProperties.set("persist.sys.scancamera", "back");
                                    sendBroadcast("com.setscan.front", false);
                                    preferencesUtil.write(isFront, false);
                                    preferencesUtil.write(jiluxuanze, getString(R.string.rear_camera_decode));
                                    contentBean1.setDescribe(preferencesUtil.read(jiluxuanze, getString(R.string.rear_camera_decode)));
                                    contentBean7.setEnable(false);
                                }

                            } else {
                                //服务没运行，则启动服务并在intent中加入摄像头的选择
                                Intent BarcodeIntent = new Intent();
                                ComponentName cn = new ComponentName("com.scanservice","com.scanbarcodeservice.ScanServices");
                                BarcodeIntent.setComponent(cn);
                                // TODO: 2018/1/2 向intent中添加前后置的请求，可以直接发给服务，让服务判断systemproperties后选取前后置
                                if (display[0]) {

                                    SystemProperties.set("persist.sys.scancamera", "front");
                                    preferencesUtil.write(isFront, true);
                                    preferencesUtil.write(jiluxuanze, getString(R.string.scanning_head_decode));
                                    contentBean1.setDescribe(preferencesUtil.read(jiluxuanze, getString(R.string.scanning_head_decode)));
                                    contentBean7.setEnable(true);
                                } else if (display[1]) {

                                    SystemProperties.set("persist.sys.scancamera", "back");
                                    preferencesUtil.write(isFront, false);
                                    preferencesUtil.write(jiluxuanze, getString(R.string.rear_camera_decode));
                                    contentBean1.setDescribe(preferencesUtil.read(jiluxuanze, getString(R.string.rear_camera_decode)));
                                    contentBean7.setEnable(false);
                                }

                                startService(BarcodeIntent);
                                preferencesUtil.write(isEnable, true);
                                SystemProperties.set("persist.sys.keyreport", "true");
                                initEnable();

                            }

                        } else {
                            //2,关闭扫描服务,已关闭则不做操作，未关闭则关闭
                            if (!isWorked(MainActivity.this, "com.scanbarcodeservice.ScanServices")){
                                return;
                            }

                            Intent BarcodeIntent = new Intent();
                            ComponentName cn = new ComponentName("com.scanservice","com.scanbarcodeservice.ScanServices");
                            BarcodeIntent.setComponent(cn);
                            stopService(BarcodeIntent);
                            preferencesUtil.write(isEnable, false);
                            SystemProperties.set("persist.sys.keyreport","false");
                            initEnable();
                            preferencesUtil.write(jiluxuanze, getString(R.string.close_quick_scan));
                            contentBean1.setDescribe(preferencesUtil.read(jiluxuanze, getString(R.string.close_quick_scan)));

                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }).setNegativeButton(getString(R.string.dialog_cancel), null).create();
        lv = builder.getListView();
        builder.show();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    //接收扫描服务的广播,使选项能够改变使用状态
    private void intentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(INIT_SERVICE);
        registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String state = intent.getAction();
            assert state != null;
            if (state.equals(INIT_SERVICE)) {
                initEnable();
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    /**
     * 判断某个服务是否正在运行的方法
     * <p>
     * <p>
     * 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     *
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isWorked(Context context, String name) {
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        assert myManager != null;
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (name.equals(runningService.get(i).service.getClassName())) {
                return true;
            }
        }
        return false;
    }



}
