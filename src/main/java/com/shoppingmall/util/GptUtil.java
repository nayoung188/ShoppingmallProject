package com.shoppingmall.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class GptUtil{

    @Value("${chatgpt.api-key}")
    private String apiKey;

    private String apiUrl = "https://api.openai.com/v1/chat/completions";

    private final RestTemplate restTemplate = new RestTemplate();

    public List<String> categoryRecommendation(List<String> checkedLabels){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        StringBuilder contentBuilder = new StringBuilder("");
        for(String checkedLabel : checkedLabels){
            contentBuilder.append("'").append(checkedLabel).append("', ");
        }
        contentBuilder.delete(contentBuilder.length() - 2, contentBuilder.length());
        contentBuilder.append(" 이 카테고리들을 조합해서 쇼핑몰에 상품 등록하기 위한 추천 키워드 10개만, 효과적인 광고를 위한 상세설명 문구 5개만 만들어줘.");
        String content = contentBuilder.toString();

        List<Map<String, Object>> messages = new ArrayList<>();
        Map<String, Object> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", content);
        messages.add(userMessage);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", messages);
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("max_tokens", 1000);
        requestBody.put("temperature", 1);


        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

        if(response.getStatusCode() == HttpStatus.OK){
            String responseBody = response.getBody();
            List<String> recommendation = getRecommendation(responseBody);
            return recommendation;
        } else {
            throw new RuntimeException("api호출 오류 ::: " + response.getStatusCode());
        }
    }

    private List<String> getRecommendation(String response) {
        List<String> recommendations = new ArrayList<>();
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            JsonNode choicesNode = jsonNode.get("choices");

            if(choicesNode != null && choicesNode.isArray() && choicesNode.size() > 0){
                for (JsonNode choiceNode : choicesNode) {
                    JsonNode messageNode  = choiceNode.get("message");
                    if(messageNode != null && messageNode.isObject()){
                        JsonNode contentNode = messageNode.get("content");
                        if(contentNode != null && contentNode.isTextual()){
                            String recommendation = contentNode.asText();
                            String removeNum = recommendation.replaceAll("\\d+", "");
                            String[] recommendationItems = removeNum.split("\\.");

                            List<String> categoryList = new ArrayList<>();
                            List<String> desctriptionList = new ArrayList<>();

                            boolean isCategorySection = false;
                            boolean isDescriptionSection = false;

                            for (String item : recommendationItems){
                                String trimmedItem = item.trim();
                                if(trimmedItem.isEmpty()){
                                    continue;
                                }
                                if(trimmedItem.contains("키워드")){
                                    isCategorySection = true;
                                    isDescriptionSection = false;
                                    categoryList.add("카테고리");
                                } else if(trimmedItem.contains("상세")){
                                    isCategorySection = false;
                                    isDescriptionSection = true;
                                    desctriptionList.add("상세설명");
                                } else {
                                    if (isCategorySection){
                                        categoryList.add(trimmedItem);
                                    } else if(isDescriptionSection){
                                        desctriptionList.add(trimmedItem);
                                    }
                                }
                            }
                            recommendations.addAll(categoryList);
                            recommendations.addAll(desctriptionList);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return recommendations;
    }

}
