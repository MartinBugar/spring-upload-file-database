package com.martyx.springuploadfiledatabase.controller;


import com.martyx.springuploadfiledatabase.message.ResponseFile;
import com.martyx.springuploadfiledatabase.message.ResponseMessage;
import com.martyx.springuploadfiledatabase.model.FileDB;
import com.martyx.springuploadfiledatabase.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("http://localhost:8081")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;


    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile (@RequestParam("multipartFile") MultipartFile multipartFile) {
        String message = "";
        try {
            fileStorageService.store(multipartFile);
            message = "Uploaded file successfully : " + multipartFile.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception ex){
            message = "Could not upload the file : " + multipartFile.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    public ResponseEntity<List<ResponseFile>> getListFiles(){

        List <ResponseFile> files = fileStorageService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .pathSegment(dbFile.getId())
                    .toUriString();

            return new ResponseFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length );
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    public ResponseEntity<byte[]> getFile (@PathVariable String id) {
        FileDB fileDB = fileStorageService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename =\"" + fileDB.getName() + "\"" )
                .body(fileDB.getData());
    }

}
