package com.shoppingmall.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class SmartStoreUtil {

    @Value("${naver-commerce-api.clientId")
    private String clientId;

    @Value("${naver-commerce-api.clientSecret")
    private String clientSecret;

    public String generateSignature(String clientId, String clientSecret, String timestamp){
        if(clientSecret == null){
            return "";
        }
        String password = StringUtils.joinWith("_", clientId, timestamp);
        String hashedPw = BCrypt.hashpw(password, clientSecret);
        return Base64.getUrlEncoder().encodeToString(hashedPw.getBytes(StandardCharsets.UTF_8));
    }

    public String getApiAuth(){
        try{
            String clientId = this.clientId;
            String clientSecret = this.clientSecret;
            Long timestamp = System.currentTimeMillis();
            String generateSignature = generateSignature(clientId, clientSecret, timestamp.toString());
            System.out.println("signature ::: " + generateSignature);
            String urlAccessToken = "https://api.commerce.naver.com/external/v1/oauth2/token";

            OkHttpClient client = new OkHttpClient();

            Map<String, String> apiData = new HashMap<>();
            apiData.put("client_id", clientId);
            apiData.put("timestamp", timestamp.toString());
            apiData.put("client_secret_sign", generateSignature);
            apiData.put("grant_type", "client_credentials");
            apiData.put("type", "SELF");        // 판매자 본인인 경우 self, 셀러인경우 관련판매자의 리소스에 대한 발급

            String query = encodeFormData(apiData);
            String url = urlAccessToken + "?" + query;

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "");
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            String responseData = response.body().string();

            JSONObject resultData = new JSONObject(responseData);
            System.out.println("resultData ::: " + resultData);

            if(!resultData.has("access_token") || resultData.getString("access_token").isEmpty()){
                throw new Exception("엑세스 토큰이 없습니다.");
            }
            return resultData.getString("access_token");
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private String encodeFormData(Map<String, String> apiData){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, String>entry : apiData.entrySet()){
            if(sb.length()>0){
                sb.append("&");
            }
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
        }
        return sb.toString();
    }

    public String getOrderList() {
        try{
            String orderListUrl = "https://api.commerce.naver.com/external/v1/pay-order/seller/product-orders/last-changed-statuses";
            String accessToken = getApiAuth();
            System.out.println("주문리스트에서 토큰 받나요  " + accessToken);
            OkHttpClient client = new OkHttpClient();

            Date now = new Date();
            Date startDate = new Date(now.getYear(), 6, 19, 0, 0, 0);
            startDate.setTime(startDate.getTime() - (startDate.getTimezoneOffset() * 60 * 1000)); // UTC 기준으로 변환
            Date endDate = new Date(startDate.getTime() + (20 * 60 * 60 * 1000));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            String iosFormEnd = sdf.format(endDate);
            String iosFormStart = sdf.format(startDate);
            System.out.println(iosFormEnd);
            System.out.println(iosFormStart);

            HttpUrl.Builder urlBuilder = HttpUrl.parse(orderListUrl).newBuilder();
            urlBuilder.addQueryParameter("lastChangedFrom", iosFormStart);
            urlBuilder.addQueryParameter("lastChangedTo", iosFormEnd);
            urlBuilder.addQueryParameter("lastChangedType", "DISPATCHED");
            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();

            System.out.println("request ::: " + request);

            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            String responseData = responseBody.string();
            System.out.println("response ::: " + response);
            System.out.println("responseData ::: " + responseData);
            responseBody.close();

            JSONObject resData = new JSONObject(responseData);
            System.out.println("resData ::: " + resData);


            if(!resData.has("data")){
                System.out.println("주문내역없음");
                return null;
            }

            JSONArray dataArr = resData.getJSONObject("data").getJSONArray("lastChangeStatuses");

            for (int i=0; i<dataArr.length(); i++){
                JSONObject data = dataArr.getJSONObject(i);
                System.out.println("주문 정보 ::: " + data);
                return data.toString();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray getOrderDetail(String orderIds){
//        String orderIds = "2023070679338531";
        String orderDetailUrl = "https://api.commerce.naver.com/external/v1/pay-order/seller/product-orders/query";
        String accessToken = getApiAuth();
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");

            JSONArray productOrderIds = new JSONArray();
            productOrderIds.put(orderIds);

            JSONObject params = new JSONObject();
            params.put("productOrderIds", productOrderIds);

            RequestBody requestBody = RequestBody.create(mediaType,params.toString());

            Request requestOrderDetail = new Request.Builder()
                    .url(orderDetailUrl)
                    .post(requestBody)
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("content-type", "application/json")
                    .build();
            Response responseOrderDetail = client.newCall(requestOrderDetail).execute();

            if(responseOrderDetail.isSuccessful()){
                String jsonData = responseOrderDetail.body().string();
                JSONObject resData = new JSONObject(jsonData);
                if(resData.has("data")){
                    JSONArray dataArray = resData.getJSONArray("data");
                    for(int i=0; i<dataArray.length(); i++){
                        JSONObject data = dataArray.getJSONObject(i);
                        printOrderDetail(data);
                    }
                    return dataArray;
                }else {
                    System.out.println("데이터없음");
                }
            }else {
                System.out.println("요청실패 :: " + responseOrderDetail.code() + " - " + responseOrderDetail.message());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static void printOrderDetail(JSONObject order){
        for(String key : order.keySet()){
            Object value = order.get(key);
            if(value instanceof JSONObject){
                printOrderDetail((JSONObject) value);
            }
        }
    }

    public String addProduct(String addInformation){
        String addProductUrl = "https://api.commerce.naver.com/external/v2/products";
        String accessToken = getApiAuth();

        try{
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");

            RequestBody body = RequestBody.create(mediaType, addInformation);
            Request request = new Request.Builder()
                    .url(addProductUrl)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("content-type", "application/json")
                    .build();

            Response responseAdd = client.newCall(request).execute();

            if(responseAdd.isSuccessful()){
                System.out.println("상품 등록 요청 성공!");
                String responseBody = responseAdd.body().string();
                System.out.println("responseBody ::: " + responseBody);
                return responseBody;
            } else {
                System.out.println("상품 등록 요청 실패 ::: " + responseAdd.code() + " / " + responseAdd.message());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getProductList(){
        String productListUrl = "https://api.commerce.naver.com/external/v1/products/search";
        String accessToken = getApiAuth();

        JSONObject jsonRequestBody = new JSONObject();
        jsonRequestBody.put("orderType", "NO");

        System.out.println(jsonRequestBody.toString());

        try{
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, jsonRequestBody.toString());

            Request request = new Request.Builder()
                    .url(productListUrl)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("content-type", "application/json")
                    .build();
            System.out.println(request);

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            System.out.println(responseBody);

            return responseBody;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
