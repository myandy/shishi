package com.myth.shishi;

import android.content.Context;
import android.widget.Toast;

import com.umeng.analytics.social.UMSocialService;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.beans.Source;
import com.umeng.comm.core.login.AbsLoginImpl;
import com.umeng.comm.core.login.LoginListener;
import com.umeng.socialize.SocializeException;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class XXXLoginImpl extends AbsLoginImpl {
    @Override
    protected void onLogin(Context mContext, LoginListener listener) {
//        UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login");
//        mController.doOauthVerify(mContext, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
//            @Override
//            public void onStart(SHARE_MEDIA platform) {
//                Toast.makeText(mContext, "授权开始", Toast.LENGTH_SHORT).show();
//            }
//            @Override
//            public void onError(SocializeException e, SHARE_MEDIA platform) {
//                Toast.makeText(mContext, "授权错误", Toast.LENGTH_SHORT).show();
//            }
//            @Override
//            public void onComplete(Bundle value, SHARE_MEDIA platform) {
//                Toast.makeText(mContext, "授权完成", Toast.LENGTH_SHORT).show();
//                //获取相关授权信息
//                mController.getPlatformInfo(MainActivity.this, SHARE_MEDIA.WEIXIN, new UMDataListener() {
//                    @Override
//                    public void onStart() {
//                        Toast.makeText(MainActivity.this, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
//                    }
//                    @Override
//                    public void onComplete(int status, Map<String, Object> info) {
//                        if(status == 200 && info != null){
//                            StringBuilder sb = new StringBuilder();
//                            Set<String> keys = info.keySet();
//                            for(String key : keys){
//                                sb.append(key+"="+info.get(key).toString()+"\r\n");
//                            }
//                            Log.d("TestData",sb.toString());
//                        }else{
//                            Log.d("TestData","发生错误："+status);
//                        }
//                    }
//                });
//            }
//            @Override
//            public void onCancel(SHARE_MEDIA platform) {
//                Toast.makeText(mContext, "授权取消", Toast.LENGTH_SHORT).show();
//            }
//        } );


        // 注意用户id、昵称、source是必填项
        CommUser loginedUser = new CommUser("用户id"); // 用户id
        loginedUser.name = "用户昵称"; // 用户昵称
        loginedUser.source = Source.SINA;// 登录系统来源,支持当前流行的第三方平台，如果是自有账号系统或者不在Source范围内，可不传此参数

        loginedUser.gender = CommUser.Gender.FEMALE;// 用户性别
        loginedUser.level = 1; // 用户等级，非必须字段
        loginedUser.score = 0;// 积分，非必须字段
        // 登录完成回调给社区SDK，200代表登录成功
        listener.onComplete(200, loginedUser);
    }
}