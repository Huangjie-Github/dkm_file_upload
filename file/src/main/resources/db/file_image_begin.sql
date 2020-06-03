create table file_image_begin(
	md5 varchar(36) not null comment '图片的MD5值',
	upload_image_url varchar(256) not null comment '图片访问地址',
	upload_image_type varchar(36) not null comment '图片的类型'，
	upload_image_time datetime not null comment '上传时间',
	upload_image_label varchar(36) not null comment '文件的标签',
	primary key (md5)
)engine=innodb default charset =utf8;
