/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : dkm_file_upload

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 03/06/2020 21:18:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file_image_begin
-- ----------------------------
DROP TABLE IF EXISTS `file_image_begin`;
CREATE TABLE `file_image_begin`  (
  `md5` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图片的MD5值',
  `upload_image_url` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图片访问地址',
  `upload_image_type` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图片的类型',
  `upload_image_time` datetime(0) NOT NULL COMMENT '上传时间',
  `upload_image_label` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件的标签',
  PRIMARY KEY (`md5`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
