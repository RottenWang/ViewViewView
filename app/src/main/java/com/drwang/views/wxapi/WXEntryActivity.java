package com.drwang.views.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.drwang.views.util.ShareUtils;
import com.sina.weibo.sdk.utils.LogUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by Administrator on 2017/7/12.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IWXAPI wxapi = ShareUtils.getWxapi();
        if (wxapi != null)
            wxapi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        String result = null;
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK: {
                result = "分享成功";
            }
            break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "分享取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "分享取消";
                break;
            default:
                result = "分享取消";
                break;
        }
        if (result != null){
            Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
        }
        Log.d("wangchen","baseResp.errCode =" + baseResp.errCode);
        this.finish();
    }


}
