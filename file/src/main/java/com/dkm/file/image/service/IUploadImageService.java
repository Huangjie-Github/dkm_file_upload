package com.dkm.file.image.service;

import com.dkm.file.image.entity.bo.ImageUploadBO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: HuangJie
 * @Date: 2020/4/2 10:58
 * @Version: 1.0V
 */
public interface IUploadImageService {

    /**
     * 实现图片的上传存储，并返回图片的地址
     * @param multipartFile 上传到的文件
     * @param cont 表的分段位数
     * @param imageMd5 图片的额MD5值
     * @param label 标签
     * @return 返回上传之后还有服务器地址的返回BO对象
     */
    ImageUploadBO uploadImage(MultipartFile multipartFile,Integer cont,String imageMd5,String label);

    /**
     * 验证这个图片在数据库是否存在，存在就返回地址，不存在就返回空
     * @param imageMd5 图片的MD5
     * @return 返回BO对象
     */
    ImageUploadBO contrastImage(String imageMd5);
}
