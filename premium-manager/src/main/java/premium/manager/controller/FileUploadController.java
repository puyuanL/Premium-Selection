package premium.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import premium.manager.service.FileUploadService;
import premium.model.vo.common.Result;
import premium.model.vo.common.ResultCodeEnum;

@RestController
@RequestMapping("/admin/system/file")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping(value = "/fileUpload")
    public Result<String> fileUpload(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileUploadService.upload(file);
        return Result.build(fileUrl, ResultCodeEnum.SUCCESS);
    }

}
