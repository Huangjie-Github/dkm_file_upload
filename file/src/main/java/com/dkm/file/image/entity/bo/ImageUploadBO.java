package com.dkm.file.image.entity.bo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: HuangJie
 * @Date: 2020/4/2 13:36
 * @Version: 1.0V
 */
@Data
public class ImageUploadBO {
    private Integer cont;
    private String uploadImageUrl;
    private String uploadImageType;
    private LocalDateTime uploadImageTime;
    private String uploadImageLabel;
}
