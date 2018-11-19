package wuzuqing.com.module_im.util;

import com.wuzuqing.component_base.net.ModelService;
import com.wuzuqing.component_base.net.common_callback.INetCallback;
import com.wuzuqing.component_base.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class DownloadUtil {

    private static List<String> taskUrls = new ArrayList<>();


    public static void download(String url, String path) {
        download(url, path, result -> taskUrls.remove(url));
    }

    public static void download(String url, String path, INetCallback<String> callback) {
        if (!taskUrls.contains(url) && !FileUtils.isFileExists(path)) {
            taskUrls.add(url);
            ModelService.downloadFile(url, path, callback);
        }
    }
    public static void removeTask(String url){
        taskUrls.remove(url);
    }
}
