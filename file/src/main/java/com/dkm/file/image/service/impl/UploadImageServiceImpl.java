package com.dkm.file.image.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.file.image.dao.UploadImageMapper;
import com.dkm.file.image.entity.FileImageBegin;
import com.dkm.file.image.entity.bo.ImageUploadBO;
import com.dkm.file.image.entity.bo.TableCreateBO;
import com.dkm.file.image.service.IUploadImageService;
import com.dkm.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;

/**
 * @Author: HuangJie
 * @Date: 2020/4/2 10:58
 * @Version: 1.0V
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UploadImageServiceImpl implements IUploadImageService {
    private final static Integer TABLE_COUNT_MAX = 5;

    @Value("${image.upload.localhost.path}")
    private String uploadLocalPath;
    @Value("${image.upload.controller.path}")
    private String imageUploadPath;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private UploadImageMapper uploadImageMapper;


    @Override
    public ImageUploadBO uploadImage(MultipartFile multipartFile,Integer cont,String imageMd5,String label) {
        //数据验证
        if (multipartFile.isEmpty()){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"图片为空");
        }
        if (cont<1||cont>8){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"cont值错误");
        }
        //生成图片新的名称
        //图片的原名称
        String fileName = multipartFile.getOriginalFilename();
        //文件名称为空就用jpg，不为空就获取原图片的尾缀
        String lastName = ".jpg";
       if (fileName!=null){
           lastName = fileName.substring(fileName.indexOf("."));
       }
        //生成唯一标识，然后再拼接上文件的尾注，作为图片的新名称
        String imageName = idGenerator.getNumberId()+lastName;
        //获取当前时间
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        //上传之后生成的时间加文件名的图片地址
        String imagePath = year + File.separator + month + File.separator + day + File.separator + imageName;
        //写入本地
        File file = new File(uploadLocalPath + imagePath);
        if (!file.exists()){
            try {
                boolean mkdirs = file.getParentFile().mkdirs();
                boolean newFile = file.createNewFile();
                multipartFile.transferTo(file);
            } catch (IOException e) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"图片创建失败，请重试");
            }
        }
        //表名称
        StringBuilder tableName = new StringBuilder();
        tableName.append("file_image_begin");
        for (int i=0; i<cont; i++){
            String substring = imageMd5.substring(i * 4 , (i + 1) * 4);
            tableName.append("_").append(substring);
        }
        //写入数据库
        try {
            FileImageBegin fileImageBegin = new FileImageBegin();
            fileImageBegin.setMd5(imageMd5);
            fileImageBegin.setUploadImageUrl(imageUploadPath+year+"/"+month+"/"+day+"/"+imageName);
            fileImageBegin.setUploadImageType(multipartFile.getContentType());
            fileImageBegin.setUploadImageTime(LocalDateTime.now());
            fileImageBegin.setUploadImageLabel(label);
            Integer integer = uploadImageMapper.insertTableCount(tableName.toString(), fileImageBegin);
            if (integer==1){
                ImageUploadBO imageUploadBO = new ImageUploadBO();
                BeanUtils.copyProperties(fileImageBegin, imageUploadBO);
                imageUploadBO.setCont(cont);
                return imageUploadBO;
            }else {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"数据信息同步失败，请重试");
            }
        }catch (Exception e){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"数据库操作失败，请检查参数后重试");
        }
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ImageUploadBO contrastImage(String imageMd5) {
        ImageUploadBO imageUploadBO = new ImageUploadBO();
        TableCreateBO tableCreateBO = this.createTableDiGui("file_image_begin", imageMd5, 0);
        assert tableCreateBO != null;
        BeanUtils.copyProperties(tableCreateBO,imageUploadBO);
        return imageUploadBO;
    }

     /**
     * 初始化的tableName为file_image_begin
     * @param tableName 表名称
     * @param md5 md5
     */
    private TableCreateBO createTableDiGui(String tableName,String md5,int cont){
        TableCreateBO tableCreateBO = new TableCreateBO();
        //标识运行到第几阶段
        int contTwo = cont+1;
        tableCreateBO.setCont(contTwo);

        //得到传过来的表名称的后段落
        String substring = tableName.substring(16);
        //后段落有多少下划线，也就是进行第几次了
        Integer much = underlineSize(substring);

        if (much==8){
            return null;
        }else {
            String strLast = md5.substring(much * 4, (much + 1) * 4);
            String tableNameTwo = tableName+"_"+strLast;
            //查看该阶梯的表是否存在
            Boolean contrastTable = uploadImageMapper.contrastTable(tableNameTwo);

            if (contrastTable){
                //表存在,查询数据
                FileImageBegin fileImageBegin = uploadImageMapper.selectMd5(tableNameTwo, md5);
                if (fileImageBegin!=null){
                    //找到对应的数据,返回服务器地址
                    tableCreateBO.setUploadImageUrl(fileImageBegin.getUploadImageUrl());
                    tableCreateBO.setUploadImageType(fileImageBegin.getUploadImageType());
                    return tableCreateBO;
                }else {
                    //没找到数据的时候，判断一下，当前表是否数据封顶，封顶就进入二阶段
                    Integer selectMd5Count = uploadImageMapper.selectMd5Count(tableNameTwo);

                    if (selectMd5Count>=TABLE_COUNT_MAX){
                        //表值最大，进入二阶段解读
                        return this.createTableDiGui(tableNameTwo,md5,contTwo);
                    }else {
                        //表值非最大，进入二阶段查询
                        return this.selectTableDiGui(tableNameTwo, md5);
                    }
                }
            }else {
                //表不存在,创建表，并且返回空
                uploadImageMapper.createTable(tableNameTwo);
                return tableCreateBO;
            }
        }
    }
    private TableCreateBO selectTableDiGui(String tableName,String imageMd5){
        TableCreateBO tableCreateBO = new TableCreateBO();
        //得到传过来的表名称的后段落
        String substring = tableName.substring(16);
        //后段落有多少下划线，也就是进行第几次了
        Integer much = underlineSize(substring);

        if (much==8){
            return null;
        }else {
            String latName = imageMd5.substring(much * 4, (much + 1) * 4);
            String tableNameTwo = tableName+"_"+latName;
            //查询下一阶段表是否存在
            Boolean aBoolean = uploadImageMapper.contrastTable(tableNameTwo);
            if (aBoolean){
                //数据查询
                FileImageBegin fileImageBegin = uploadImageMapper.selectMd5(tableNameTwo, imageMd5);
                if (fileImageBegin!=null){
                    BeanUtils.copyProperties(fileImageBegin,tableCreateBO);
                    tableCreateBO.setCont(much+1);
                    return tableCreateBO;
                }else {
                    return this.selectTableDiGui(tableNameTwo,imageMd5);
                }
            }else {
                tableCreateBO.setCont(much);
                return tableCreateBO;
            }
        }
    }
    private Integer underlineSize(String substring){
        String[] split = substring.split("");
        int size = 0;
        for (String str : split) {
            if ("_".equals(str)){
                size++;
            }
        }
        return size;
    }
}