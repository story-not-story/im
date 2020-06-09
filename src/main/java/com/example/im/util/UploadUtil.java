package com.example.im.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author HuJun
 * @date 2020/5/30 6:07 下午
 */
public class UploadUtil {
    public static String upload(MultipartFile imgFile, String id, String path) {
        String originName = imgFile.getOriginalFilename();
        String newName = UUID.randomUUID().toString().replace("-", "");
        int index = originName.indexOf('.');
        if (index >= 0) {
            newName += originName.substring(index);
        }
        File dir = new File(path + id + "/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(path + id, newName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            imgFile.transferTo(file);
            return id + "/" + newName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Boolean isExists(String path, String fileName) {
        File file = new File(path, fileName);
        return file.exists() && file.isFile();
    }
}
