package com.hqu.hquforum.provider;

import com.hqu.hquforum.dto.AccessTokenDTO;
import com.hqu.hquforum.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;

import java.io.IOException;

/**
 * @author Drf
 * @create 2021-03-26-2:14
 */

@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        // OkHttp
        MediaType mediatype = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediatype, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.github.com/user")
                    .header("Authorization","token" + accessToken)
                    .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {

        }
        return null;
    }

}
