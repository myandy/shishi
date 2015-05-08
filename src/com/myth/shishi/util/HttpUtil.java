package com.myth.shishi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * 网络数据发送
 * 
 * @author ziminli
 * @version [版本号, 2014-6-25]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class HttpUtil
{
    private OnHttpListener onHttpListener;

    private enum req_type
    {
        post, get,
    }

    class HttpAsynTask extends AsyncTask<Object, Object, Object>
    {

        OnHttpListener uiListener;

        boolean isError;

        byte[] entity;

        int error_code;

        String msg;

        /**
         * 该方法将在执行实际的后台操作前被UI thread调用。可以在该方法中做一些准备工作，如在界面上显示一个进度条。
         */
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object... arg)
        {
            req_type t = (req_type) arg[0];
            URI uri = (URI) arg[1];
            uiListener = (OnHttpListener) arg[2];

            if (t == req_type.get)
            {
                return doGetInBackground(uri);
            }
            else
            {
                String content = (String) arg[3];
                return doPostInBackground(uri, content);
            }
        }

        private Object doPostInBackground(URI uri, String content)
        {
            Logs.warn("功能尚未开发完成");
            return null;
        }

        private Object doGetInBackground(URI uri)
        {

            HttpGet httpRequest;
            HttpResponse httpResponse;

            isError = true;
            try
            {
                httpRequest = new HttpGet(uri);
                httpResponse = new DefaultHttpClient().execute(httpRequest);

                if (httpResponse.getStatusLine().getStatusCode() == 200)
                {
                    entity = EntityUtils.toByteArray(httpResponse.getEntity());
                    isError = false;
                }
                else
                {
                    error_code = httpResponse.getStatusLine().getStatusCode();
                    isError = true;
                    msg = "";
                }
            }
            catch (ClientProtocolException e)
            {
                // e.printStackTrace();
                error_code = OnHttpListener.ErrCode_ClientProtocolException;
                isError = true;
                msg = e.getMessage();
            }
            catch (IOException e)
            {
                // e.printStackTrace();
                error_code = OnHttpListener.ErrCode_IOException;
                isError = true;
                msg = e.getMessage();
            }
            catch (IllegalStateException e)
            {
                // e.printStackTrace();
                error_code = OnHttpListener.ErrCode_IllegalStateException;
                isError = true;
                msg = e.getMessage();
            }

            return isError;
        }

        @Override
        protected void onPostExecute(Object result)
        {
            super.onPostExecute(result);

            // Log.d(TAG,"onPostExecute result:" + result);

            boolean isError = (Boolean) result;

            if (isError)
            {
                uiListener.onError(error_code, msg);
            }
            else
            {
                uiListener.onResp(entity);
            }
        }
    }

    /**
     * @author kinzhang
     */
    public interface OnHttpListener
    {

        /**
         * 从1001开始自定义错误码 1000段的是catch exception的
         */
        public static final int ErrCode_Exception = 1000;

        public static final int ErrCode_ClientProtocolException = 1001;

        public static final int ErrCode_IOException = 1002;

        public static final int ErrCode_IllegalStateException = 1003;

        public static final int ErrCode_URISyntaxException = 1004;

        public static final int ErrCode_NullUri = 2000;

        void onResp(byte[] data);

        void onError(int error_code, String msg);
    }

    public static String makeErrorMsg(int error_code, String msg)
    {
        return "network error: " + error_code + ":" + msg;
    }

    public byte[] reqPost(String url, byte[] content, OnHttpListener listener)
    {
        URI uri;
        try
        {
            uri = new URI(url);
            return reqPost(uri, content, listener);
        }
        catch (URISyntaxException e)
        {
            listener.onError(OnHttpListener.ErrCode_URISyntaxException, e.getMessage());
        }
        return null;
    }

    public byte[] reqPost(URI uri, byte[] content, OnHttpListener listener)
    {
        String reString = "";
        try
        {
            Logs.debug("req:" + uri.toString());

            OutputStream os = null;

            URLConnection conn = new URL(uri.toString()).openConnection();
            conn.setDoOutput(true);
            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(10 * 1000);
            os = conn.getOutputStream();
            os.write(content);
            os.flush();
            os.close();

            // 到这里已经完成了，不过我们还是看看返回信息吧
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;

            while ((line = reader.readLine()) != null)
            {
                reString += line;
            }

            listener.onResp(reString.getBytes());
        }
        catch (Exception e)
        {
            listener.onError(OnHttpListener.ErrCode_Exception, e.getMessage());
        }
        return reString.getBytes();
    }

    public void reqGet(String url, OnHttpListener listener)
    {
        URI uri;
        try
        {
            uri = new URI(url);
            reqGet(uri, listener);
        }
        catch (URISyntaxException e)
        {
            listener.onError(OnHttpListener.ErrCode_URISyntaxException, e.getMessage());
        }
    }

    /**
     * @param url 请求的url
     * @param listener
     * @throws
     */

    public void reqGet(URI uri, OnHttpListener listener)
    {

        try
        {

            Logs.debug("req:" + uri.toString());

            HttpGet httpRequest = new HttpGet(uri);

            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == 200)
            {
                // String strResult =
                // EntityUtils.toString(httpResponse.getEntity());

                listener.onResp(EntityUtils.toByteArray(httpResponse.getEntity()));
            }
            else
            {
                listener.onError(httpResponse.getStatusLine().getStatusCode(), httpResponse.getStatusLine().toString());
            }
        }
        catch (Exception e)
        {
            listener.onError(OnHttpListener.ErrCode_Exception, e.getMessage());
        }
    }

    public void reqGetAsync(URI url, OnHttpListener listener)
    {
        try
        {
            // Log.d(TAG, "req:" + url);
            Logs.debug("req:" + url);
            if (TextUtils.isEmpty(url.toString()))
            {
                listener.onError(OnHttpListener.ErrCode_NullUri, "");
                return;
            }

            new HttpAsynTask().execute(req_type.get, url, listener);
        }
        catch (Exception e)
        {
            listener.onError(OnHttpListener.ErrCode_Exception, e.getMessage());
        }
    }

    /**
     * 异步http get请求
     * 
     * @param url
     * @param listener
     */
    public void reqGetAsync(String url, OnHttpListener listener)
    {
        URI uri;
        try
        {
            uri = new URI(url);
            reqGetAsync(uri, listener);
        }
        catch (URISyntaxException e)
        {
            // e.printStackTrace();
            listener.onError(OnHttpListener.ErrCode_URISyntaxException, e.getMessage());
        }
    }

    public void reqPostAsync(String url, byte[] content, OnHttpListener listener)
    {
        URI uri;
        try
        {
            uri = new URI(url);
            reqPostAsync(uri, content, listener);
        }
        catch (URISyntaxException e)
        {
            // e.printStackTrace();
            listener.onError(OnHttpListener.ErrCode_URISyntaxException, e.getMessage());
        }
    }

    public void reqPostAsync(URI url, byte[] content, OnHttpListener listener)
    {
        try
        {
            Logs.debug("req:" + url);
            new HttpAsynTask().execute(req_type.post, url, listener, content);
        }
        catch (Exception e)
        {
            // e.printStackTrace();
            listener.onError(OnHttpListener.ErrCode_Exception, e.getMessage());
        }
    }

    public OnHttpListener getOnHttpListener()
    {
        return onHttpListener;
    }

    public void setOnHttpListener(OnHttpListener onHttpListener)
    {
        this.onHttpListener = onHttpListener;
    }

    /** gzip消息头 */
    // public static final String HEADER_GZIP_KEY = "Accept-Encoding";

    public static final String HEADER_GZIP_VALUE = "gzip";

    public static final int DEFAULT_TIMEOUT = 30000;

    /** https */
    public static HttpClient getNewHttpsClient()
    {
        try
        {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            return new DefaultHttpClient(ccm, params);
        }
        catch (Exception e)
        {
            Logs.error("e.getMessage:" + e.getMessage());
        }
        return new DefaultHttpClient();
    }

    public static String httpGet(String url, String queryString, int timeout)
    {
        if (TextUtils.isEmpty(url))
        {
            return null;
        }

        String responseData = null;

        if (!TextUtils.isEmpty(queryString))
        {
            url += "?" + queryString;
        }

        Logs.debug(url);

        HttpClient httpClient;// = new DefaultHttpClient();
        if (url.startsWith("https://"))
        {
            httpClient = getNewHttpsClient();
        }
        else
        {
            httpClient = new DefaultHttpClient();
        }

        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, timeout);
        HttpConnectionParams.setSoTimeout(params, timeout);

        try
        {

            HttpGet httpGet = new HttpGet(url);

            // 接受gzip压缩
            // httpGet.addHeader(HttpUtils.HEADER_GZIP_KEY,
            // HttpUtils.HEADER_GZIP_VALUE);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            // 200~300
            if (statusCode < HttpStatus.SC_OK || statusCode > HttpStatus.SC_MULTIPLE_CHOICES)
            {
                Logs.error(url + " failed: " + httpResponse.getStatusLine());
                return null;
            }

            HttpEntity entity = httpResponse.getEntity();
            responseData = EntityUtils.toString(entity, HTTP.UTF_8);

            Logs.debug("response:" + responseData);
        }
        catch (Exception e)
        {
            Logs.error(url + " " + e.toString());
        }
        finally
        {
            httpClient = null;
        }

        return responseData;
    }

    /**
     * Using GET method.
     * 
     * @param url The remote URL.
     * @param queryString The query string containing parameters
     * @return Response string.
     * @throws IOException
     */
    public static String httpGet(String url, String queryString)
    {
        return httpGet(url, queryString, DEFAULT_TIMEOUT);
    }

    /**
     * Using GET method.
     * 
     * @param url The remote URL.
     * @param queryString The query string containing parameters
     * @return Response string.
     * @throws IOException
     */
    public static String httpGet(String url, Bundle param)
    {
        return httpGet(url, generateQuery(param));
    }

    public static String httpPost(String url, Bundle param)
    {
        return httpPost(url, param, DEFAULT_TIMEOUT);
    }

    public static String httpPost(String url, Bundle param, int timeout)
    {
        return httpPost(url, param, timeout, null);
    }

    public static String httpPost(String url, Bundle param, int timeout, String cookie)
    {
        if (TextUtils.isEmpty(url))
        {
            return null;
        }

        Logs.debug(url + " " + param);

        // HttpClient httpClient = new DefaultHttpClient();
        HttpClient httpClient;// = new DefaultHttpClient();
        if (url.startsWith("https://"))
        {
            httpClient = getNewHttpsClient();
        }
        else
        {
            httpClient = new DefaultHttpClient();
        }

        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, timeout);
        HttpConnectionParams.setSoTimeout(params, timeout);

        String responseData = null;
        try
        {
            HttpPost httpPost = new HttpPost(url);

            if (param != null && !param.isEmpty())
            {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                for (String key : param.keySet())
                {
                    nameValuePairs.add(new BasicNameValuePair(key, param.getString(key)));
                }

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            }

            if (!TextUtils.isEmpty(cookie))
            {
                httpPost.setHeader("Cookie", cookie);
            }

            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
            {
                Logs.error("HttpPost Method failed: " + httpResponse.getStatusLine());
            }
            responseData = EntityUtils.toString(httpResponse.getEntity());

            Logs.debug("responseData = " + responseData);
        }
        catch (Exception e)
        {
            Logs.error(url + " " + e.toString());
        }
        finally
        {
            httpClient = null;
        }

        return responseData;
    }

    /** 生成http query */
    public static String generateQuery(Bundle param)
    {
        if (param == null || param.isEmpty())
        {
            return "";
        }

        StringBuffer sbBuffer = new StringBuffer();
        if (param != null)
        {
            int i = 0;
            for (String key : param.keySet())
            {

                if (i == 0)
                {
                    sbBuffer.append("");
                }
                else
                {
                    sbBuffer.append("&");
                }

                try
                {
                    sbBuffer.append(URLEncoder.encode(key, HTTP.UTF_8) + "="
                            + URLEncoder.encode(param.getString(key), HTTP.UTF_8));
                }
                catch (UnsupportedEncodingException e)
                {
                    Logs.error(e.toString());
                }

                i++;
            }
        }
        Logs.debug(sbBuffer.toString());
        return sbBuffer.toString();
    }

    static class MySSLSocketFactory extends SSLSocketFactory
    {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException
        {
            super(truststore);

            TrustManager tm = new X509TrustManager()
            {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
                {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
                {
                }

                public X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] {tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException,
                UnknownHostException
        {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException
        {
            return sslContext.getSocketFactory().createSocket();
        }
    }
}
