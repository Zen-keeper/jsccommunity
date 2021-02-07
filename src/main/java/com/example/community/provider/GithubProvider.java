package com.example.community.provider;

import com.alibaba.fastjson.JSON;
import com.example.community.dto.AccessTokenDto;
import com.example.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {

    public String getAccesstoken(AccessTokenDto accessTokenDto) {
//        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        MediaType mediaType = MediaType.Companion.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        //json 是accessTokenDto的json形式
//        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDto));
        RequestBody body = RequestBody.Companion.create(JSON.toJSONString(accessTokenDto),mediaType);
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String str = response.body().string();
            System.out.print(str);
            return str;
        } catch (IOException e) {
        }
        return  null;
    }
    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+ accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();//获取response
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string,GithubUser.class); //把回复解析成GithubUser
            return githubUser;
        } catch (IOException e) {

        }
        return null;
    }

}
