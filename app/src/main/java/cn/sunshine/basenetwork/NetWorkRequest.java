package cn.sunshine.basenetwork;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import cn.sunshine.basenetwork.callback.APIResult;
import cn.sunshine.basenetwork.callback.HttpCallback;
import cn.sunshine.basenetwork.callback.HttpCallbackT;
import cn.sunshine.basenetwork.rxjava.HttpResultGenerateFunc;
import cn.sunshine.basenetwork.rxjava.RxSchedulers;
import cn.sunshine.basenetwork.utils.FileUpDownloadUtil;
import cn.sunshine.basenetwork.utils.OkHttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by SunShine on 2018/3/6.
 */

public class NetWorkRequest {

    /**
     * 封装的统一的 POST 请求接口 回调中是实体类对象
     * @param url
     * @param params
     * @param callback
     */
    public static void POST(String url, Map<String, String> params, HttpCallback callback){
        HttpClientManager.getInstance().getApiService().executePost(url,params)
                .map(new HttpResultGenerateFunc())
                .compose(RxSchedulers.<APIResult>applyIoSchedulers())
                .subscribe(callback);
    }
    /**
     * 封装的统一的 GET 请求接口 回调中是实体类对象
     * @param url
     * @param params
     * @param callback
     */
    public static void GET(String url, Map<String, String> params, HttpCallback callback){
        HttpClientManager.getInstance().getApiService().executeGet(url,params)
                .map(new HttpResultGenerateFunc())
                .compose(RxSchedulers.<APIResult>applyIoSchedulers())
                .subscribe(callback);
    }
    /**
     * 封装的统一的 POST 请求接口 回调中是json对象
     * @param url
     * @param params
     * @param callback
     */
    public static void POST_T(String url, Map<String, String> params, HttpCallback callback){
        HttpClientTManager.getInstance().getApiService().executePost(url,params)
                .compose(RxSchedulers.applyIoSchedulers())
                .subscribe(callback);
    }
    /**
     * 封装的统一的 GET 请求接口 回调中是json对象
     * @param url
     * @param params
     * @param callback
     */
    public static void GET_T(String url, Map<String, String> params, HttpCallbackT callback){
        HttpClientTManager.getInstance().getApiService().executeTGet(url,params)
                .enqueue(callback);
    }
    /**
     * 上传多个文件
     * @param url
     * @param mFileList
     * @param httpCallback
     */
    private void uploadFile(String url, List<File> mFileList, HttpCallbackT httpCallback) {
        MultipartBody body = FileUpDownloadUtil.filesToMultipartBody(mFileList);
        HttpClientManager.getInstance().getApiService().uploadFileWithRequestBody(url,body)
                .enqueue(httpCallback);
    }

    /**
     * 上传一个文件
     * @param url
     * @param file
     * @param httpCallback
     */
    private void uploadOneFile(String url,File file, HttpCallbackT httpCallback) {
        MultipartBody.Part body = FileUpDownloadUtil.filesToMultipartBodyPart(file);
        HttpClientManager.getInstance().getApiService().uploadFile(url,body)
                .enqueue(httpCallback);
    }

    /**
     * 下载文件
     * @param url
     */
    private void downLoadFile(final Context context,String url,HttpCallbackT httpCallback2){
        HttpClientManager.getInstance().getApiService().downloadFiles(url)
                .enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Log.d("下载文件：", "server contacted and has file");
                            boolean writtenToDisk = FileUpDownloadUtil.writeResponseBodyToDisk(context,response.body());
                            Log.d("下载文件：", "file download was a success? " + writtenToDisk);
                        } else {
                            Log.d("下载文件：","下载文件失败");
                            Toast.makeText(context, "下载文件失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                        Log.e("下载文件：", "error");
                    }
                });
    }

    /**
     * 下载文件 以流的形式把apk写入的指定文件 得到file后进行安装
     * 参数一：请求Url
     * 参数二：保存文件的路径名
     * 参数三：保存文件的文件名
     */
    public static void download(final Activity context, final String url, final String saveDir) {
        Request request = new Request.Builder().url(url).build();
        Call call = OkHttpUtils.getInstance().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("xxx", e.toString());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    //apk保存路径
                    final String fileDir = FileUpDownloadUtil.isExistDir(saveDir);
                    //文件
                    File file = new File(fileDir, FileUpDownloadUtil.getNameFromUrl(url));
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "下载成功:" + fileDir + "," + FileUpDownloadUtil.getNameFromUrl(url), Toast.LENGTH_SHORT).show();
                        }
                    });
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }

                    fos.flush();
                    //apk下载完成后 调用系统的安装方法
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    context.startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (is != null) is.close();
                    if (fos != null) fos.close();


                }
            }
        });

    }
}