package com.bernerus.smartmirror.controller;

import com.bernerus.smartmirror.model.asana.AsanaUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * Created by FRLU7457 on 2017-03-01.
 */
/*
{"data":{"id":12561375407065,
"email":"fredrik.lunde3@gmail.com"
,"name":"Fredrik Lunde"
,"photo":{"image_21x21":"https://s3.amazonaws.com/profile_photos/12561375407065.eSOsxIXIjNrd9Jw6fXuq_21x21.png",
"image_27x27":"https://s3.amazonaws.com/profile_photos/12561375407065.eSOsxIXIjNrd9Jw6fXuq_27x27.png",
"image_36x36":"https://s3.amazonaws.com/profile_photos/12561375407065.eSOsxIXIjNrd9Jw6fXuq_36x36.png",
"image_60x60":"https://s3.amazonaws.com/profile_photos/12561375407065.eSOsxIXIjNrd9Jw6fXuq_60x60.png",
"image_128x128":"https://s3.amazonaws.com/profile_photos/12561375407065.eSOsxIXIjNrd9Jw6fXuq_128x128.png"},
"workspaces":[{"id":498346170860,"name":"Personal Projects"}]}}
*/
@Controller
public class AsanaController {
    private static final Logger log = LoggerFactory.getLogger(AsanaController.class);

    @Value("${asana.access.token}")
    private String accessToken;


    public <T> T callAsana(String url, Class<T> expectedClass){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String urlPrefix = "https://app.asana.com/api/1.0/";
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Bearer " + accessToken);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<T> response = restTemplate.exchange(urlPrefix + url, HttpMethod.GET, request, expectedClass);
        log.info("AAAAAAAAAAAASSSSSSSSSAAAAAAANNNNNNAAAAAAA!!!" + response.getBody().toString());
        return response.getBody();
    }

    @PostConstruct
    public void init(){
        AsanaUser user = callAsana("users/me", AsanaUser.class);
        String workspaceID = String.valueOf(user.getData().getWorkspaces().get(0).getId());
        //AsanaProject projects = callAsana("workspaces/" + workspaceID + "/projects", AsanaProject.class);
    }
}
