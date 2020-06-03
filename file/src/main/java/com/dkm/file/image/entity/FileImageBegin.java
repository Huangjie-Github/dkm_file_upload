package com.dkm.file.image.entity;

import lombok.Data;

import javax.print.attribute.standard.PrinterURI;
import java.time.LocalDateTime;

/**
 * @Author: HuangJie
 * @Date: 2020/4/2 15:40
 * @Version: 1.0V
 */
@Data
public class FileImageBegin {
    private String md5;
    private String uploadImageUrl;
    private String uploadImageType;
    private LocalDateTime uploadImageTime;
    private String uploadImageLabel;
}
