package com.shoppingmall.service;

import com.shoppingmall.entity.ProdImageEntity;
import com.shoppingmall.repository.ProdImageRepository;
import com.shoppingmall.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ProdImageService {

    private final ProdImageRepository prodImageRepository;

    private final FileUtil fileUtil;

    public void saveProdImage(ProdImageEntity prodImageEntity, MultipartFile multipartFile) throws Exception{
        String name = multipartFile.getName();
        String imageType = "";
        String imageName = "";
        String imageUrl = "";
        String originalName = multipartFile.getOriginalFilename();

        if(name.equalsIgnoreCase("mainImage")){
            imageType = "메인이미지";
        } else if(name.equalsIgnoreCase("subImages")){
            imageType = "추가이미지";
        } else if (name.equalsIgnoreCase("informationImages")) {
            imageType = "상세설명이미지";
        }

        if(!StringUtils.isEmpty(originalName)){
            imageName = fileUtil.saveImageFile(multipartFile, imageType);
            String folderName = "";
            if(imageType.equalsIgnoreCase("메인이미지")){
                folderName = "mainImage";
            } else if (imageType.equalsIgnoreCase("추가이미지")) {
                folderName = "subImage";
            } else if (imageType.equalsIgnoreCase("상세설명이미지")) {
                folderName = "informationImage";
            }
            imageUrl = "/storage/uploadImages/" + folderName + "/" + imageName;
        }
        prodImageEntity.updateItemImage(originalName, imageName, imageUrl, imageType);
        prodImageRepository.save(prodImageEntity);
    }

    public Map<String, Object> saveSummernoteImage(MultipartHttpServletRequest multipartRequest){
        MultipartFile multipartFile = multipartRequest.getFile("file");
        String filePath = fileUtil.uploadSummernote();
        String fileName = fileUtil.uploadImagesRename(multipartFile.getOriginalFilename());

        File dir = new File(filePath);
        if(dir.exists() == false){
            dir.mkdirs();
        }

        File file = new File(dir, fileName);

        try{
            multipartFile.transferTo(file);
        } catch (Exception e){
            e.printStackTrace();
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("src", "/storage/uploadImages/informationImage/" + fileName);
        map.put("fileName", fileName);
        System.out.println("filePath ::: " + filePath);
        System.out.println("map ::: " + map);
        return map;
    }


}
