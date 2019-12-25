package com.kmsoft.community.provider;

import com.alibaba.fastjson.JSON;
import com.kmsoft.community.dto.AccessTokenDTO;
import com.kmsoft.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token?client_id="+accessTokenDTO.getClient_Id()
                        +"&client_secret="+accessTokenDTO.getClient_secret()
                        +"&code="+accessTokenDTO.getCode()
                        +"&redirect_uri="+accessTokenDTO.getRedirect_url()
                        +"&state="+accessTokenDTO.getState())
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String accessToken = string.split("&")[0].split("=")[1];
            return accessToken;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization","token "+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String userMsgStr = response.body().string();
            GithubUser githubUser = JSON.parseObject(userMsgStr, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
