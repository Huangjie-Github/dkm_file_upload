package com.dkm.file.image.controller;

import com.alibaba.fastjson.JSONObject;
import com.dkm.file.image.entity.bo.ImageUploadBO;
import com.dkm.file.image.entity.vo.ImageUploadVO;
import com.dkm.file.image.service.IUploadImageService;
import com.dkm.utils.BodyUtil;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: HuangJie
 * @Date: 2020/4/2 10:54
 * @Version: 1.0V
 */
@Api(value = "图片上传Controller",tags = "图片上传接口")
@RestController
@RequestMapping("/images")
public class UploadImageController {

    @Autowired
    private IUploadImageService uploadImageService;

    @Autowired
    private BodyUtil bodyUtil;

    @ApiOperation(value = "图片上传接口",notes = "在验证完服务器上没有需要的图片时，再调用此接口上传图片")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "file",value = "图片的MultipartFile对象",dataType = "file",required = true,paramType = "form"),
        @ApiImplicitParam(name = "cont",value = "图片验证接口返回的cont值",dataType = "Integer",required = true,paramType = "form"),
        @ApiImplicitParam(name = "imageMd5",value = "图片的MD5值",dataType = "String",required = true,paramType = "form"),
        @ApiImplicitParam(name = "label",value = "标签",dataType = "String",required = true,paramType = "form")
    })
    @PostMapping(value = "/upload")
    public ImageUploadVO upload(@RequestParam("file")MultipartFile multipartFile,
                                @RequestParam("cont") Integer cont,
                                @RequestParam("imageMd5") String imageMd5,
                                @RequestParam("label") String label){
        ImageUploadBO imageUploadBO = uploadImageService.uploadImage(multipartFile,cont,imageMd5,label);
        ImageUploadVO imageUploadVO = new ImageUploadVO();
        BeanUtils.copyProperties(imageUploadBO, imageUploadVO);

        return imageUploadVO;
    }

    @ApiOperation(value = "图片验证接口",notes = "发送图片的MD5，验证该图片在服务器上是否存在，存在则返回地址，不存在则地址为空")
    @ApiImplicitParam(name = "imageMd5",value = "图片的MD5值",dataType = "String",required = true,paramType = "body")
    @PostMapping(value = "/contrast",produces = "application/json")
    public ImageUploadVO contrast(HttpServletRequest request){
        JSONObject bodyJson = bodyUtil.bodyJson(request);
        System.out.println(bodyJson);
        String imageMd5 = bodyJson.getString("imageMd5");
        ImageUploadBO imageUploadBO = uploadImageService.contrastImage(imageMd5);
        ImageUploadVO imageUploadVO = new ImageUploadVO();
        BeanUtils.copyProperties(imageUploadBO,imageUploadVO);


        return imageUploadVO;
    }

}
