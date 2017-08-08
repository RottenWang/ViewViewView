package com.drwang.views.support.fresco;

/**
 * Created by Administrator on 2017/8/8.
 */

public class FrescoScheme {

//    类型	SCHEME	示例
//    远程图片	http://, https://	HttpURLConnection 或者参考 使用其他网络加载方案
//    本地文件	file://	FileInputStream
//    Content provider	content://	ContentResolver
//    asset目录下的资源	asset://	AssetManager
//    res目录下的资源	res://	Resources.openRawResource
//    Uri中指定图片数据	data:mime/type;base64,

    public static final String SCHEME_HTTP = "http://";
    public static final String SCHEME_HTTPS = "https://";
    public static final String SCHEME_FILE = "file://";
    public static final String SCHEME_CONTENT = "content://";
    public static final String SCHEME_ASSET = "asset://";
    public static final String SCHEME_RES = "res://";
    public static final String SCHEME_DATA = "data:mime/type;base64";
}
