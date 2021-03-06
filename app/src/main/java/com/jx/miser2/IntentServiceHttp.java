package com.jx.miser2;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.mindpipe.android.logging.log4j.LogConfigurator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by li on 16-9-10.
 */

public class IntentServiceHttp extends IntentService {

    public Bundle bundle;
    OkHttpClient mOkHttpClient = new OkHttpClient();
    Request.Builder mReqBuilderC = new Request.Builder();
    Request.Builder mReqBuilderA = new Request.Builder();
    private String pid = "0";
    private static String CHATURL = "http://zhangtingx.top:888/getChatList/?pid=";
    private static String UNREADURL = "http://t.10jqka.com.cn/newcircle/message/getunread/";
    private static String ENTRUSTURL = "http://t.10jqka.com.cn/trace/trade/getEntrust/?_=";
    private List<BeanSMS> mMsgBeanList = new ArrayList<BeanSMS>();
    private List<BeanAlarms> mAlarmsBeanList = new ArrayList<BeanAlarms>();
    private List<BeanAlarms> mNewAlarmsBeanList = new ArrayList<BeanAlarms>();
    private ArrayList mlist = new ArrayList();//消息广播传递参数不能为局部变量，可能被销毁
    private String mcookstr;//保存cookies
   private Logger gLogger;

    public void configLog()
    {
        final LogConfigurator logConfigurator = new LogConfigurator();

        logConfigurator.setFileName(Environment.getExternalStorageDirectory() + File.separator + "miser.log");
        // Set the root log level
        logConfigurator.setRootLevel(Level.DEBUG);
        // Set log level of a specific logger
        logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.configure();

        gLogger = Logger.getLogger(this.getClass());
        gLogger = Logger.getLogger("miser");
    }

    public IntentServiceHttp() {
        //必须实现父类的构造方法
        super("IntentServiceHttp");
        mcookstr = readFileSdcardFile("/mnt/sdcard/miser.cook");
        addHttpHeaders(mReqBuilderA);
        //gLogger = Logger.getLogger("miser");
       configLog();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("miserIntentService","onBind");
        return super.onBind(intent);
    }


    @Override
    public void onCreate() {
        Log.d("miserIntentService","onCreate");
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d("miserIntentService","onStart");
        super.onStart(intent, startId);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("miserIntentService","onStartCommand");
        flags = Service.START_STICKY;//设置为粘滞服务
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void setIntentRedelivery(boolean enabled) {
        super.setIntentRedelivery(enabled);
        Log.d("miserIntentService","setIntentRedelivery");
    }

    @Override
    public void onDestroy() {
        Log.d("miserIntentService","onDestroy");
        super.onDestroy();
    }

    private void getConversationAndSendIntent()
    {
        String url = CHATURL + pid;
        try {
            mReqBuilderC.url(url);
            Request request = mReqBuilderC.build();
            Response response = mOkHttpClient.newCall(request).execute();
            String resString = response.body().string();

            Document doc = Jsoup.parse(resString);
            Elements contents = doc.getElementsByAttributeValue("class", "chattxt");
            for (Element li : contents) {
                pid = li.attr("pid");
                String Text = li.ownText();
                String time = li.getElementsByTag("span").text();

                BeanSMS MsgBean = new BeanSMS();
                MsgBean.mMsg = Text;
                MsgBean.mName = "铁扇公主";
                MsgBean.mTime = time;
                mMsgBeanList.add(MsgBean);
            }

        } catch (Exception e) {
          //  gLogger.debug(e.toString());
            e.printStackTrace();
        }
        if (!mMsgBeanList.isEmpty()) {
            bundle = new Bundle();
            ArrayList list = new ArrayList();
            list.add(mMsgBeanList);
            bundle.putParcelableArrayList("list", list);
            Intent I1 = new Intent();
            I1.setAction("com.miser.li.miser.CONVERSATION");//(MainActivity.CONVERSATION_CHANGED_ACTION);
            I1.putExtras(bundle);
            sendBroadcast(I1);
            mMsgBeanList.clear();
        }
    }

    private void addHttpHeaders(Request.Builder reqBuilder)
    {
        reqBuilder.addHeader("Accept","application/json, text/javascript, */*; q=0.01");
        //reqBuilder.addHeader("Accept-Encoding","gzip, deflate, sdch");//服务端没有压缩
        reqBuilder.addHeader("Connection","keep-alive");//mcookstr);
        reqBuilder.addHeader("Cookie",mcookstr);//"user=MDp0eDNtYWppYTg6Ok5vbmU6NTAwOjM1OTMzMjQzNTo3LDExMTExMTExMTExLDQwOzQ0LDExLDQwOzYsMSw0MDs1LDEsNDA6Mzo6OjM0OTMzMjQzNToxNDczNDM1MTY5Ojo6MTQ3MTE1NTI0MDo2MDQ4MDA6MDpmMzBhMDVkMjI4MzIzNWY3YTE5ODBlOWU5NGNmNzgxMzpkZWZhdWx0XzI6MA%3D%3D; userid=349332435; u_name=tx3majia8; escapename=tx3majia8; ticket=801e9f1d86559834352871a14a4afffe; Hm_lvt_da7579fd91e2c6fa5aeb9d1620a9b333=1473435129,1473476007; Hm_lpvt_da7579fd91e2c6fa5aeb9d1620a9b333=1473476007; Hm_lvt_78c58f01938e4d85eaf619eae71b4ed1=1473435129,1473476007; Hm_lpvt_78c58f01938e4d85eaf619eae71b4ed1=1473476007");//mcookstr);
        reqBuilder.addHeader("Referer","http://t.10jqka.com.cn/circle/8530");
        reqBuilder.addHeader("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.101 Safari/537.36");
        reqBuilder.addHeader("X-Requested-With","XMLHttpRequest");
    }

    //读SD中的文件
    public String readFileSdcardFile(String fileName)
    {
        String res="";
        try{
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;

            while ((line = reader.readLine()) != null) {
                res += line;
            }
            reader.close();
        }

        catch(Exception e){
           // gLogger.debug(e.toString());
            e.printStackTrace();
        }
        return res;
    }
    //写数据到SD中的文件
    public void writeFileSdcardFile(String fileName,String write_str)
    {
        try{

            FileOutputStream fout = new FileOutputStream(fileName);
            byte [] bytes = write_str.getBytes();

            fout.write(bytes);
            fout.close();
        }

        catch(Exception e){
           // gLogger.debug(e.toString());
            e.printStackTrace();
        }
    }

    /*
    private Boolean hasJsonData()//先判断是否有新数据
    {


        try {
            mReqBuilderA.url(UNREADURL);
            Request request = mReqBuilderA.build();
            Response response = mOkHttpClient.newCall(request).execute();
            String jsonString = response.body().string();
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(jsonString);
                Integer errorCode = jsonObject.getInt("errorCode");
                String errorMsg = jsonObject.getString("errorMsg");
                if (errorCode != 0)
                {
                    //gLogger.debug(errorMsg);
                    return false;
                }
                //JSONObject jsonRes = jsonObject.getJSONObject("result");
                if (jsonObject.getJSONObject("result").getInt("ace") > 0)//有新数据
                {
                    return true;
                }
            } catch (JSONException e) {
                //gLogger.debug(e.toString());
                e.printStackTrace();
            }

        }
        catch (Exception e) {
            //gLogger.debug(e.toString());
            e.printStackTrace();
        }

        return true;

    }*/
    private void getAlarmsAndSendIntent() throws InterruptedException {
        ENTRUSTURL += String.valueOf(System.currentTimeMillis());
        try {
            mReqBuilderA.url(ENTRUSTURL);
            Request request = mReqBuilderA.build();
            Response response = mOkHttpClient.newCall(request).execute();
            String jsonString = response.body().string();

            JSONObject jsonObject;
            BeanAlarms alarmsBean;
            try {
                jsonObject = new JSONObject(jsonString);
                if ("成功".equals(jsonObject.getString("errormsg")))
                {
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        jsonObject = jsonArray.getJSONObject(i);
                        alarmsBean = new BeanAlarms();
                        alarmsBean.masterName = jsonObject.getString("nickname");
                        alarmsBean.masterIconUrl = jsonObject.getString("avatar");
                        alarmsBean.number = jsonObject.getString("wtsl");
                        alarmsBean.sharesname = jsonObject.getString("zqmc") + "   " + jsonObject.getString("zqdm");
                        alarmsBean.time = jsonObject.getString("time");
                        alarmsBean.wdjg = jsonObject.getString("wtjg");

                        String type = jsonObject.getString("type");
                        if ("B".equals(type))
                        {
                            alarmsBean.type = "买";
                        }
                        else if ("S".equals(type))
                        {
                            alarmsBean.type = "卖";
                        }
                        //查询是否为新消息
                        Boolean bExist = false;
                        for (BeanAlarms exist: mAlarmsBeanList)
                        {
                            if (exist.equals(alarmsBean))
                            {
                                bExist = true;
                                break;
                            }
                        }
                        if (!bExist)//该数据是新的
                        {
                            mAlarmsBeanList.add(alarmsBean);
                            mNewAlarmsBeanList.add(alarmsBean);
                        }
                    }
                }
                else
                {
                  ;//  gLogger.debug(jsonObject.getString("errormsg"));
                }

            } catch (JSONException e) {
                //gLogger.debug(e.toString());
                e.printStackTrace();
            }

        } catch (IOException e) {
            //gLogger.debug(e.toString());
            e.printStackTrace();
        }

        //return mNewAlarmsBeenList;
        if (!mNewAlarmsBeanList.isEmpty()) {
            bundle = new Bundle();
            mlist.clear();
            mlist.add(mNewAlarmsBeanList);
            bundle.putParcelableArrayList("list", mlist);
            Intent I1 = new Intent();
            I1.setAction("com.miser.li.miser.ALARMS");//(MainActivity.CONVERSATION_CHANGED_ACTION);
            I1.putExtras(bundle);
            sendBroadcast(I1);
            mNewAlarmsBeanList.clear();
            //Thread.sleep(50000);
            //mNewAlarmsBeanList.clear();
        }
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        //Intent是从Activity发过来的，携带识别参数，根据参数不同执行不同的任务
       /* String action = intent.getExtras().getString("param");
        if (action.equals("oper1")) {
            Log.d("miserIntentService","onStartCommand");System.out.println("Operation1");
        }else if (action.equals("oper2")) {
            Log.d("miserIntentService","onStartCommand");System.out.println("Operation2");
        }*/
        Log.d("miserIntentService","onHandleIntent");
        while(true)
        {
            try
            {

               getAlarmsAndSendIntent();
                getConversationAndSendIntent();
                Thread.sleep(2000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }



}