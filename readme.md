###配置 intent-filter
AndroidManifest.xml

参考<https://segmentfault.com/a/1190000014223480>
```xml
<activity android:name=".MainActivity">
    <!-- 需要添加下面的intent-filter配置 -->
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data
            android:host="myhost"
            android:path="/main"
            android:port="1024"
            android:scheme="myscheme" />
    </intent-filter>
</activity>
```
###测试网页
###main 下新建 assets 文件，写了简单的 Html 网页用于 WebView 展示，来进行测试。

###index.html：

```html
<html>
<head>
    <meta charset="UTF-8">
</head>
<body>
<h1>这是一个 WebView</h1>

<a href="market://details?id=com.tencent.mm">open app with market</a>
<br/>
<br/>

<a href="myscheme://myhost:1024/main?key1=value1&key2=value2">open app with Uri Scheme</a>

<br/>
<br/>


</body>
</html>
```
###Web View 加载：

###webView.loadUrl("file:///android_asset/index.html");
##目标页面
##接受参数，做相应的处理。

```java
Intent intent = getIntent();
if (null != intent && null != intent.getData()) {
    // uri 就相当于 web 页面中的链接
    Uri uri = intent.getData();
    Log.e(TAG, "uri=" +uri);
    String scheme = uri.getScheme();
    String host = uri.getHost();
    int port = uri.getPort();
    String path = uri.getPath();
    String key1 = uri.getQueryParameter("key1");
    String key2 = uri.getQueryParameter("key2");
    Log.e(TAG, "scheme=" + scheme + ",host=" + host
            + ",port=" + port + ",path=" + path
            + ",query=" + uri.getQuery()
            + ",key1=" + key1 + "，key2=" + key2);
}
```
打印消息如下：

uri=myscheme://myhost:1024/main?key1=value1&key2=value2
scheme=myscheme,host=myhost,port=1024,path=/main,query=key1=value1&key2=value2,key1=value1，key2=value2
原理
myscheme://myhost:1024/main?key1=value1&key2=value2，通过一个链接，为什么能启动相应的 APP 呢？Web 唤起 Android app 的实现及原理，一文说到关键代码在 Android 6.0 的原生浏览器的 shouldOverrideUrlLoading 方法，核心实现在 UrlHandler 这个类中。代码如下：
```java
final static String SCHEME_WTAI = "wtai://wp/";
final static String SCHEME_WTAI_MC = "wtai://wp/mc;";
boolean shouldOverrideUrlLoading(Tab tab, WebView view, String url) {
    if (view.isPrivateBrowsingEnabled()) {
        // Don't allow urls to leave the browser app when in
        // private browsing mode
        return false;
    }
    if (url.startsWith(SCHEME_WTAI)) {
        // wtai://wp/mc;number
        // number=string(phone-number)
        if (url.startsWith(SCHEME_WTAI_MC)) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(WebView.SCHEME_TEL +
                            url.substring(SCHEME_WTAI_MC.length())));
            mActivity.startActivity(intent);
            // before leaving BrowserActivity, close the empty child tab.
            // If a new tab is created through JavaScript open to load this
            // url, we would like to close it as we will load this url in a
            // different Activity.
            mController.closeEmptyTab();
            return true;
        }
        //……
    }
```
