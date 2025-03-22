package com.splatref.splatrefbackend.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {
        // get name of file
        // get hash of file
        try {
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());

            MessageDigest md5 = MessageDigest.getInstance("MD5");
            String fileName = HexFormat.of().formatHex(md5.digest(file.getBytes())) + "." + fileExtension;

            if (!(fileExtension.equals("png") || fileExtension.equals("jpg"))) {
                throw new IllegalArgumentException("Uploaded Image is of wrong filetype: " + fileName);
            }

            // to get the file path
            String filePath = path + File.separator + fileName;

            // create file object
            File f = new File(path);

            if (!f.exists()) {
                f.mkdir();
            }

            // copy file or upload to path

            Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (NoSuchAlgorithmException e) {
            throw new FileNotFoundException("MD5 checksum checking failed");
        }
    }

    @Override
    public InputStream getResourceFile(String path, String fileName) throws FileNotFoundException {
        String filePath = path + File.separator + fileName;

        return new FileInputStream(filePath);
    }
}
