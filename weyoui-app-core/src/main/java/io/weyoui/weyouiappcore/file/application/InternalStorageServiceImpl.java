package io.weyoui.weyouiappcore.file.application;

import io.weyoui.weyouiappcore.file.application.domain.SupportImgExtension;
import io.weyoui.weyouiappcore.product.command.domain.StorageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class InternalStorageServiceImpl implements StorageService{

    private final String filePath;

    public InternalStorageServiceImpl(@Value("${filePath}") String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean isAvailableType(String storageTypeCode) {
        return StorageType.findStorage(storageTypeCode).equals(StorageType.INTERNAL);
    }

    @Override
    public String save(MultipartFile file) {

        String ext = checkFileExt(file);

        String detailFilePath = createFilePath();
        String fileName = ThreadLocalRandom.current().nextInt(90000) + 10000 + "." + ext;

        File uploadFile = new File(detailFilePath, fileName);

        if(!uploadFile.exists()) {
            if(uploadFile.mkdirs()) log.info("파일 생성 : " + detailFilePath);
            else throw new RuntimeException("파일 디렉토리 경로 생성에 실패하였습니다.");
        }

        try {
            file.transferTo(uploadFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return uploadFile.getPath();
    }

    @Override
    public void delete(Long id, String path) {
        File file = new File(path);
        String name = file.getName();

        assert file.exists();

        boolean isDeleted = file.delete();

        if(isDeleted) log.info("파일이 성공적으로 삭제되었습니다. \n 파일이름 : " + name);
        else log.error("파일 삭제에 실패했습니다. \n 파일 이름 : " + name);

    }

    private String checkFileExt(MultipartFile file) {
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        if(!SupportImgExtension.hasExtension(extension)) throw new IllegalStateException("현재 서비스에서 지원하는 이미지 파일의 확장자는 png,jpg,jpeg,gif 입니다.");

        return extension;
    }


    private String createFilePath() {

        String saveFolderPath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return filePath + File.separator + saveFolderPath;
    }
}
