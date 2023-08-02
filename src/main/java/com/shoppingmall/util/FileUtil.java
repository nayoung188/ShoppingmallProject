package com.shoppingmall.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;

@Component
public class FileUtil {

    @Value("${uploadPath}")
    private String uploadPath;

    // 경로구분자
    private String sep = Matcher.quoteReplacement(File.separator);

    public String getFullPath(String filename){
        String fullPath = uploadImagePath() + filename;
        return fullPath;
    }

    public String uploadImagesRename(String originalFilename){
        String extension = null;
        if(originalFilename.endsWith("tar.gz")){
            extension = "tar.gz";
        } else {
            String[] arr = originalFilename.split("\\.");
            extension = arr[arr.length - 1];
        }
        return UUID.randomUUID().toString().replaceAll("\\-","") + "." + extension;
    }

    public String saveImageFile(MultipartFile multipartFile, String imageType) throws IOException{
        if(multipartFile.isEmpty()){
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String saveFilename = uploadImagesRename(originalFilename);

        String directoryPath = uploadImagePath();

        if( imageType.equalsIgnoreCase("메인이미지")){
            directoryPath += "mainImage" + sep;
        } else if (imageType.equalsIgnoreCase("추가이미지")) {
            directoryPath += "subImage" + sep;
        }

        File directory = new File(directoryPath);
        if(!directory.exists()){
            directory.mkdirs();
        }

        multipartFile.transferTo(new File(directory, saveFilename));

        return saveFilename;
    }

    public String uploadImagePath(){
        String uploadImagePath = uploadPath + sep + "storage" + sep + "uploadImages" + sep;
        return uploadImagePath;
    }

    public String uploadSummernote(){
        String summernotePath = uploadPath + "storage/uploadImages/informationImage/";
        return summernotePath;
    }

    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath);
        if(deleteFile.exists()){
            deleteFile.delete();
        }
    }

    public String uploadLicensePath(){
        String licensePath = uploadPath + "storage/uploadImages/licenseImages/";
        return licensePath;
    }

    public String licenseRename(String originalFilename){
        String extension = null;
        if(originalFilename.endsWith("tar.gz")){
            extension = "tar.gz";
        } else {
            String[] arr = originalFilename.split("\\.");
            extension = arr[arr.length - 1];
        }
        String uuid = UUID.randomUUID().toString().replaceAll("\\-","");
        uuid = uuid.substring(0,6);
        String fileRename = uuid + "." + extension;
        System.out.println(fileRename);
        return fileRename;
    }

    public String saveLicenseImage(MultipartFile multipartFile, Long memberNo,String licenseType) throws IOException{
        if(multipartFile.isEmpty()){
            return null;
        }
        String licenseImageName = multipartFile.getOriginalFilename();
        String saveImageName = memberNo + "_" + licenseType + "_" + licenseRename(licenseImageName);
        System.out.println(saveImageName);

        String directoryPath = uploadLicensePath();

        File directory = new File(directoryPath);
        if(!directory.exists()){
            directory.mkdirs();
        }
        multipartFile.transferTo(new File (directory, saveImageName));

        String imageUrl = directoryPath + saveImageName;
        System.out.println(imageUrl);

        return imageUrl;
    }



}
