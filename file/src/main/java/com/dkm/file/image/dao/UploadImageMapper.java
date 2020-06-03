package com.dkm.file.image.dao;

import com.dkm.IBaseMapper.IBaseMapper;
import com.dkm.file.image.entity.FileImageBegin;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

/**
 * @Author: HuangJie
 * @Date: 2020/4/2 14:15
 * @Version: 1.0V
 */
@Mapper
@Component
public interface UploadImageMapper extends IBaseMapper<FileImageBegin> {

    /**
     * 验证dkm_file_upload数据库中是否有指定名称的表
     * @param tableName 表名称
     * @return 验证结果
     */
    @Select("SELECT count(TABLE_NAME) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='dkm_file_upload' AND TABLE_NAME=#{tableName}")
    Boolean contrastTable(@Param("tableName")String tableName);

    /**
     * 根据表名称  复制一个指定表结构的表
     * @param tableName 表名称
     * @return 创建结果
     */
    @Insert("create table if not exists ${tableName} like file_image_begin")
    void createTable(@Param("tableName")String tableName);

    /**
     * 根据表名称呵md5字段内容查询结果
     * @param tableName 表名称
     * @param md5 md5
     * @return 返回查询结果
     */
    @Select("select * from ${tableName} where md5 = #{md5}")
    FileImageBegin selectMd5(@Param("tableName")String tableName,@Param("md5") String md5);

    /**
     * 查询指定数据表的数据条数
     * @param tableName 表名称
     * @return 数据条数
     */
    @Select("select count(md5) from ${tableName}")
    Integer selectMd5Count(@Param("tableName")String tableName);

    /**
     * 给指定表名称添加记录
     * @param tableName 表名称
     * @param fileImageBegin 添加的参数
     * @return 返回的结果
     */
    @Insert("insert into ${tableName}(md5,upload_image_url,upload_image_type,upload_image_time,upload_image_label) values (#{fileImageBegin.md5},#{fileImageBegin.uploadImageUrl},#{fileImageBegin.uploadImageType},#{fileImageBegin.uploadImageTime},#{fileImageBegin.uploadImageLabel})")
    Integer insertTableCount(@Param("tableName")String tableName,@Param("fileImageBegin") FileImageBegin fileImageBegin);
}
