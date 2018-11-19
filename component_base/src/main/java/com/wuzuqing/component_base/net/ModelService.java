package com.wuzuqing.component_base.net;

import android.os.AsyncTask;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.wuzuqing.component_base.api.BaseApiService;
import com.wuzuqing.component_base.base.mvc.BaseVcObserver;
import com.wuzuqing.component_base.base.mvp.inter.IView;
import com.wuzuqing.component_base.net.common_callback.INetCallback;
import com.wuzuqing.component_base.util.LogUtils;
import com.wuzuqing.component_base.util.RxUtils;
import com.wuzuqing.component_data.bean.BaseObj;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Created by TOME .
 * @时间 2018/5/16 9:52
 * @描述 ${TODO}
 */

public class ModelService {

    /**
     * 接口回调
     *
     * @param <T>
     */
    public interface MethodSelect<T, S> {
        Observable<BaseObj<T>> selectM(S service);
    }


    /**
     * 获取远程基本数据
     * 带进度条的方法
     *
     * @param mView
     * @return
     */
    public static <T, S> BaseVcObserver<BaseObj<T>> getRemoteData(boolean isShowHUD, IView mView, Class<S> clz,
                                                                  MethodSelect<T, S> select, INetCallback<T> callback) {
        //设置不同的BaseUrl
        return select.selectM(HttpHelper.getDefault(2)
                .create(clz))
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseVcObserver<BaseObj<T>>(mView, isShowHUD) {
                                   @Override
                                   public void onNext(BaseObj<T> result) {
//                                       LogUtils.d("获取数据", ":" + result);
                                       if (isShowHUD) {
                                           mView.dismissHUD();
                                       }
                                       callback.onSuccess(result.getData());
                                   }
                               }
                );
    }


    /**
     * 封装了list的刷新
     *
     * @param mView
     * @param select
     * @param callback
     * @param <T>
     * @return
     */
    public static <T, S> BaseVcObserver<BaseObj<T>> getRemoteListData(IView mView, SmartRefreshLayout rlRefresh,
                                                                      Class<S> clz,
                                                                      MethodSelect<T, S> select, INetCallback<T> callback) {
        //设置不同的BaseUrl
        return select.selectM(HttpHelper.getDefault(2)
                .create(clz))
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseVcObserver<BaseObj<T>>(mView, rlRefresh, false) {
                                   @Override
                                   public void onNext(BaseObj<T> result) {
//                                       Logger.json("获取数据"+result.toString());
                                       callback.onSuccess(result.getData());
                                   }
                               }
                );
    }


    /**
     * 下载图片
     *
     * @return
     */
    public static void downloadFile(String fileUrl, String outFile, INetCallback<String> callback) {
        final BaseApiService downloadService =
                HttpHelper.getDefault(2).create(BaseApiService.class);
        new AsyncTask<Void, Long, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Call<ResponseBody> call = downloadService.downloadFileWithDynamicUrlAsync("example"+ fileUrl);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            boolean writtenToDisk = writeResponseBodyToDisk(response.body(), outFile);
                            if (writtenToDisk) {
                                LogUtils.d("download finish");
                                callback.onSuccess(outFile);
                            } else {
                                callback.onSuccess(null);
                            }
                        } else {
                            callback.onSuccess(null);
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        callback.onSuccess(null);
                    }
                });
                return null;
            }
        }.execute();

    }

    private static boolean writeResponseBodyToDisk(ResponseBody body, String outFile) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(outFile);
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;
                }

                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }


    public static MultipartBody.Part formatPath(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return MultipartBody.Part.createFormData("file", file.getName(), requestFile);
    }

    /**
     * 上传图片
     *
     * @param mView
     * @param file
     * @return
     */
    public static void uploadFile(IView mView, File file, String sId, INetCallback<String> callback) {
        ((MethodSelect<String, BaseApiService>) service -> service.uploadFile(formatPath(file), sId))
                .selectM(HttpHelper.getDefault(2)
                        .create(BaseApiService.class))
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(
                        new BaseVcObserver<BaseObj<String>>(mView) {
                            @Override
                            public void onNext(BaseObj<String> result) {
                                callback.onSuccess(result.getData());
                            }
                        }
                );
    }

}
