package cn.sunshine.basenetwork;


import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cn.sunshine.basenetwork.callback.BaseResponse;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiService {

    /**
     *普通写法 例子:
     */
    @GET("service/getIpInfo.php/")
    Observable<Result<JSONObject>> getData(@Query("ip") String ip);

    /**
     * GET 通用写法
     */
    @GET("{url}")
    Observable<Result<JSONObject>> executeGet(
            @Path(value = "url", encoded = true) String url,
            @QueryMap Map<String, String> maps);

    /**
     * POST 通用写法
     */
    @POST("url")
    Observable<Result<JSONObject>> executePost(
            @Path(value = "url", encoded = true) String url,
            @FieldMap Map<String, String> maps);
    /**
     * GET 通用写法
     */
    @GET("{url}")
    Call<Result<JSONObject>> executeTGet(
            @Path(value = "url", encoded = true) String url,
            @QueryMap Map<String, String> maps);



    /**
     * 通过 List<MultipartBody.Part> 传入多个part实现多文件上传
     * @param parts 每个part代表一个
     * @return 状态信息
     */
    @Multipart
    @POST("url")
    Call<BaseResponse<String>> uploadFilesWithParts(
            @Path(value = "url", encoded = true) String url,
            @Part() List<MultipartBody.Part> parts);

    /**
     * 通过 MultipartBody和@body作为参数来上传
     * @param multipartBody MultipartBody包含多个Part
     * @return 状态信息
     */
    @POST("url")
    Call<BaseResponse<String>> uploadFileWithRequestBody(
            @Path(value = "url", encoded = true) String url,
            @Body MultipartBody multipartBody);

    /**
     * 上传单个文件-例如上传 头像
     * @param url
     * @param file
     * @return
     */
    @Multipart
    @POST("url")
    Call<ResponseBody> uploadFile(
            @Path(value = "url", encoded = true) String url,
            @Part() MultipartBody.Part file);

    /**
     * 下载文件
     * @param url
     * @return
     */
    @POST("{url}")
    Call<ResponseBody> downloadFiles(
            @Url String url);
}
