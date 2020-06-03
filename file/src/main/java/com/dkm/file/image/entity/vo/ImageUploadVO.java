package com.dkm.file.image.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: HuangJie
 * @Date: 2020/4/2 13:37
 * @Version: 1.0V
 */
@Data
public class ImageUploadVO {
    private Integer cont;
    private String uploadImageUrl;
    private String uploadImageType;
    private LocalDateTime uploadImageTime;
    private String uploadImageLabel;
}
