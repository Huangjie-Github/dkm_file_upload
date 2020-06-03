package com.dkm.file.image.entity.bo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: HuangJie
 * @Date: 2020/4/3 9:22
 * @Version: 1.0V
 */
@Data
public class TableCreateBO {
    private Integer cont;
    private String uploadImageUrl;
    private String uploadImageType;
    private LocalDateTime uploadImageTime;
    private String uploadImageLabel;
}
