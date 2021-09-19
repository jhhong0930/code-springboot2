package org.zerock.mreview.controller;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mreview.dto.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class UploadController {

    /**
     * 파일을 저장하는 단계에서는 다음과 같은 사항을 고려해야 한다
     * - 업로드된 확장자가 이미지만 가능하도록 검사(첨부파일을 이용한 원격 쉘)
     *   - 첨부파일을 이용하여 쉘 스크립트 파일 등을 업로드하여 공격하는 기법들도 있기 때문에
     *   - 브라우저에서 파일을 업로드하는 순간이나 서버에서 파일을 저장하는 순간에도 이를 검사해야 한다
     *   - MultipartFile 에서 제공하는 getContentType()을 이용하여 처리가 가능하다
     *
     * - 동일한 이름의 파일이 업로드 된다면 기존 파일을 덮어쓰는 문제
     *   - 첨부파일의 이름이 같으면 기존의 파일이 사라지고 새로운 파일로 변경되기 때문에
     *   - 시간 값을 파일 이름에 추가하거나 UUID를 이용하여 고유한 값을 만들어 사용한다
     *
     * - 업로드된 파일을 저장하는 폴더의 용량
     *   - 동일한 폴더에 많은 파일이 쌓이게 되면 성능이 저하된다
     *   - 일반적으로 파일이 저장되는 시점의 년/월/일 폴더를 따로 생성하여 보관한다
     */

    @Value("${org.zerock.upload.path}") // application.properties의 변수
    private String uploadPath;

    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) {

        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        for (MultipartFile uploadFile : uploadFiles) {

            // 이미지 파일만 업로드 가능
            if (!uploadFile.getContentType().startsWith("image")) {
                log.warn("This file is not image type");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            String originalName = uploadFile.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

            log.info("fileName = {}", fileName);

            // 날짜 폴더 생성
            String folderPath = makeFolder();

            // UUID
            String uuid = UUID.randomUUID().toString();

            // 저장할 파일 이름 중간에 "_"를 이용해서 구분
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid +"_" + fileName;

            Path savePath = Paths.get(saveName);

            try {

                // 원본 파일 저장
                uploadFile.transferTo(savePath);

                // 섬네일 생성
                String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator +
                        "s_" + uuid + "_" + fileName;

                // 섬네일 파일 이름은 중간에 s_로 시작하도록
                File thumbnailFile = new File(thumbnailSaveName);

                // 섬네일 생성
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);

                resultDTOList.add(new UploadResultDTO(fileName, uuid, folderPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }

    private String makeFolder() {

        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath =  str.replace("//", File.separator);

        // make folder
        File uploadPathFolder = new File(uploadPath, folderPath);

        if (uploadPathFolder.exists() == false) {
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName) {

        ResponseEntity<byte[]> result = null;

        try {
            String srcFileName =  URLDecoder.decode(fileName,"UTF-8");

            log.info("fileName: " + srcFileName);

            File file = new File(uploadPath +File.separator+ srcFileName);

            log.info("file: " + file);

            HttpHeaders header = new HttpHeaders();

            // MIME타입 처리
            header.add("Content-Type", Files.probeContentType(file.toPath()));
            // 파일 데이터 처리
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    /**
     * 업로드 파일 삭제
     * - 파일의 URL이 년/월/일/uuid_파일명 으로 구성되어 있으므로 파일의 취를 찾아 삭제할 수 있다
     * - 원본 파일과 함께 섬네일 파일도 같이 삭제
     */
    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName) {

        String srcFileName = null;

        try {
            srcFileName = URLDecoder.decode(fileName, "UTF-8");
            File file = new File(uploadPath + File.separator + srcFileName);
            boolean result = file.delete();

            File thumbnail = new File(file.getParent(), "s_" + file.getName());

            result = thumbnail.delete();

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
