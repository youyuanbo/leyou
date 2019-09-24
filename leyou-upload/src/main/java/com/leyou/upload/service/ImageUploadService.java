package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LeYouException;
import com.leyou.upload.config.UploadProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

@Service
@Slf4j
public class ImageUploadService {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private UploadProperties uploadProperties;


    public String uploadImage(MultipartFile file) {
        try {
            //校验文件后缀名
            String contentType = file.getContentType();
            if (!uploadProperties.getAllowTypes().contains(contentType)) {
                throw new LeYouException(ExceptionEnum.INVALID_FILE_TYPE);
            }

            //校验文件
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new LeYouException(ExceptionEnum.INVALID_FILE_TYPE);
            }

            // 上传到FastDFS
            // String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
            return uploadProperties.getBaseUrl() + storePath.getFullPath();
        } catch (Exception e) {
            log.error("上传文件失败！", e);
            throw new LeYouException(ExceptionEnum.UPLOAD_FILE_ERROR);
        }
    }
}
