package com.shoppingmall.controller;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class VisionController {

    private ImageAnnotatorClient visionClient;
    private Translate translate;

    @Autowired
    private Environment environment;

    @PostConstruct
    public void initializeVisionClient() throws IOException {
        // credential은 json파일로 저장되어있으며, 환경변수로 설정해놓음
        String credentialsPath = environment.getProperty("GOOGLE_APPLICATION_CREDENTIALS");
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath));
        visionClient = ImageAnnotatorClient.create(ImageAnnotatorSettings.newBuilder().setCredentialsProvider(() -> credentials).build());
        translate = TranslateOptions.getDefaultInstance().getService();
    }

    @RequestMapping("/visionAPI")
    public String visionAPI(){
        return "visionAPI";
    }

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<Map<String, List<String>>> uploadImage(MultipartFile[] files) throws IOException {
        List<String> labels = new ArrayList<>();
        List<String> imageSources = new ArrayList<>();
        for (MultipartFile file : files){
            if(!file.isEmpty()){
                byte[] bytes = file.getBytes();
                ByteString byteString = ByteString.copyFrom(bytes);
                Image image = Image.newBuilder().setContent(byteString).build();
                Feature feature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
                AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                        .addFeatures(feature)
                        .setImage(image)
                        .build();
                BatchAnnotateImagesResponse response = visionClient.batchAnnotateImages(List.of(request));
                for (EntityAnnotation annotation : response.getResponses(0).getLabelAnnotationsList()){
                    labels.add(annotation.getDescription());
                }
                String imageSource = "data:" + file.getContentType() + ";base64," + java.util.Base64.getEncoder().encodeToString(bytes);
                imageSources.add(imageSource);
            }
        }

        List<String> translatedLabels = new ArrayList<>();
        for(String label : labels) {
            Translation translation = translate.translate(label, Translate.TranslateOption.targetLanguage("ko"));
            translatedLabels.add(translation.getTranslatedText());
        }

        Map<String, List<String>> response = new HashMap<>();
        response.put("labels", translatedLabels);
        response.put("imageSources", imageSources);

        System.out.println("labels ::: " + response.get("labels"));

        return ResponseEntity.ok(response);
    }
}
