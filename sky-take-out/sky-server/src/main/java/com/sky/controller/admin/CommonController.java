package com.sky.controller.admin;

import java.io.File;
import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sky.constant.FilePathConstant;
import com.sky.result.Result;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
public class CommonController {
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        log.info("上传的文件：{}",file);
        String filename = file.getOriginalFilename();
        String filePath = FilePathConstant.STATIC_IMG_RESOURCE_1;
        File targetFile = new File(filePath,filename);
        file.transferTo(targetFile);
        
        return Result.success();
    }
}
