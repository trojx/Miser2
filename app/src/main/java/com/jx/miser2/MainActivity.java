package com.jx.miser2;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.mindpipe.android.logging.log4j.LogConfigurator;
import etong.bottomnavigation.lib.BottomBarTab;
import etong.bottomnavigation.lib.BottomNavigationBar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationBar bottomLayout;

    private ArrayList<BottomNavigationBean> list = new ArrayList<BottomNavigationBean>();

    private UIReceiver mReceiver;
    private List<Fragment> mTables = new ArrayList<>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    //List<BeanSMS> mMsgBeanList;
   // List<BeanAlarms> mAlarmsBeanList;
    static int NotificationCount = 0;
    private Logger gLogger;
    /*public void configLog()
    {
        final LogConfigurator logConfigurator = new LogConfigurator();

        logConfigurator.setFileName(Environment.getExternalStorageDirectory() + File.separator + "miser.log");
        // Set the root log level
        logConfigurator.setRootLevel(Level.DEBUG);
        // Set log level of a specific logger
        logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.configure();

        //gLogger = Logger.getLogger(this.getClass());
        gLogger = Logger.getLogger("miser");
    }

*/
    //发送通知栏消息
    private  void Notification(String Title, String Content)
    {

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        android.support.v4.app.NotificationCompat.Builder mBuilder = new android.support.v4.app.NotificationCompat.Builder(this);
        //系统收到通知时，通知栏上面显示的文字。
        mBuilder.setTicker(Content);
        //显示在通知栏上的小图标
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        //通知标题
        mBuilder.setContentTitle(Title);
        //通知内容
        mBuilder.setContentText(Content);
        //mBuilder.setDefaults(Notification.DEFAULT_ALL);
        //mBuilder.setDefaults(Notification.DEFAULT_SOUND);//设置声音
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setDefaults(Notification.DEFAULT_LIGHTS);//设置指示灯，，，，，指示灯个震动都需要配置权限
        mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);//设置震动效果
        //设置大图标，即通知条上左侧的图片（如果只设置了小图标，则此处会显示小图标）
        //mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.share_sina));
        //显示在小图标左侧的数字
        mBuilder.setNumber(6);

        //设置为不可清除模式
       // mBuilder.setOngoing(true);

        //显示通知，id必须不重复，否则新的通知会覆盖旧的通知（利用这一特性，可以对通知进行更新）
        mNotificationManager.notify(NotificationCount++, mBuilder.build());
    }
    /*
    //判断是否是新消息
    public Boolean isHaveNew(List<BeanAlarms> Old, List<BeanAlarms> New)
    {
        Boolean ret = false;
        Boolean bExist = false;
        int sizeOfNew = New.size();
        int sizeOfOld = Old.size();
        for (int i = 0; i < sizeOfNew; i++)
        {
            bExist = false;
            BeanAlarms N = New.get(i);
            for (int j = 0; j < sizeOfOld; j++)
            {
                if (N.equals(Old.get(j)))
                {
                    bExist = true;
                    break;
                }
            }
            if (!bExist)
            {//有新元素
                ret = true;
                Notification(N.masterName, N.type + " " + N.sharesname + " " + N.wdjg);
            }

        }
        return ret;
    }*/

    //内部类接受广播 方便更新界面
    public class UIReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if(action.equals("com.miser.li.miser.CONVERSATION"))//(MainActivity.CONVERSATION_CHANGED_ACTION))
            {
                Bundle bundle = intent.getExtras();
                //List<BeanSMS> MsgBeanList;// = new ArrayList<ConvertsationBean>();

                ArrayList list = bundle.getParcelableArrayList("list");
                //mMsgBeanList.addAll((List<BeanSMS>) list.get(0));
                ((FragmentSMS)mTables.get(1)).UpdateUI((List<BeanSMS>) list.get(0));
                //mMsgBeanList.addAll(MsgBeanList);
            }
            else if(action.equals("com.miser.li.miser.ALARMS"))//(MainActivity.CONVERSATION_CHANGED_ACTION))
            {
                Bundle bundle = intent.getExtras();
                // = new ArrayList<ConvertsationBean>();

                ArrayList list = bundle.getParcelableArrayList("list");
                List<BeanAlarms> ABeanList = (List<BeanAlarms>) list.get(0);
                int size = ABeanList.size();
                for (int i = 0; i < size; ++i)
                {
                    Notification(ABeanList.get(i).masterName, ABeanList.get(i).type + " " + ABeanList.get(i).sharesname + " " + ABeanList.get(i).wdjg);
                }

                    //malarmsAdapter.addItem(mAlarmsBeanList);
                ((FragmentAlarms)mTables.get(2)).UpdateUI(ABeanList);

            }
        }

    }

    private void initTabFragment()
    {

        FragmentMaster masterFragment = new FragmentMaster();
        mTables.add(masterFragment);

        FragmentSMS SMSFragment = new FragmentSMS();
        mTables.add(SMSFragment);

        FragmentAlarms alarmsFragment = new FragmentAlarms();
        mTables.add(alarmsFragment);

        FragmentSetting settingFragment = new FragmentSetting();
        mTables.add(settingFragment);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ////////////////////
        //mMsgBeanList= new ArrayList<BeanSMS>();
        //mAlarmsBeanList= new ArrayList<BeanAlarms>();
        initTabFragment();
        mReceiver = new UIReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.miser.li.miser.CONVERSATION");
        filter.addAction("com.miser.li.miser.ALARMS");
       registerReceiver(mReceiver, filter);//(CONVERSATION_CHANGED_ACTION));
       // configLog();
        try{
            Intent it = new Intent("com.li.miser.IntentServiceHttp");
            it.setPackage(getPackageName());
           ComponentName b = startService(it);

            Log.d("8033", "try to start service:");
        } catch(Exception e){
            Log.d("8033", e.toString());
        }


        /////////////////////////
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //    沉浸式导航栏
            /*window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);*/
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Instant run");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, (FragmentMaster)mTables.get(0)).commitAllowingStateLoss();

        setUpBottomNavigationBar();

        ////TODO 动态设置tab数量
//        findViewById(R.id.addTab).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomLayout.addTab(R.drawable.selector_me, "Movies & Tv", 0xff4a5965);
//            }
//        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//适配虚拟按键
    }

    public void setUpBottomNavigationBar() {

        bottomLayout = (BottomNavigationBar) findViewById(R.id.bottomLayout);
        bottomLayout.setTabWidthSelectedScale(1.5f);
        bottomLayout.setTextDefaultVisible(false);
//        bottomLayout.setTextColorResId(R.color.color_tab_text);
        bottomLayout.addTab(R.drawable.selector_master, "老司机", 0xff4a5965);
        bottomLayout.addTab(R.drawable.selector_sms, "消息", 0xff096c54);
        bottomLayout.addTab(R.drawable.selector_alarms, "交易", 0xff8a6a64);
        bottomLayout.addTab(R.drawable.selector_setting, "设置", 0xff553b36);
        bottomLayout.setOnTabListener(new BottomNavigationBar.TabListener() {
            @Override
            public void onSelected(BottomBarTab tab, int position)
            {
                switch (position) {
                    case 0:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, (FragmentMaster)mTables.get(0))
//                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .commitAllowingStateLoss();
                        break;
                    case 1:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, (FragmentSMS)mTables.get(1))
//                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .commitAllowingStateLoss();
                        break;
                    case 2:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, (FragmentAlarms)mTables.get(2))
//                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .commitAllowingStateLoss();
                        break;
                    case 3:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, (FragmentSetting)mTables.get(3))
//                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .commitAllowingStateLoss();
                        break;
                    default:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, (FragmentMaster)mTables.get(0))
//                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .commitAllowingStateLoss();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.jx.miser2/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.jx.miser2/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
