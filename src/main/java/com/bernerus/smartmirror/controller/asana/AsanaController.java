package com.bernerus.smartmirror.controller.asana;

import com.bernerus.smartmirror.model.asana.AsanaProjectData;
import com.bernerus.smartmirror.model.asana.AsanaProjects;
import com.bernerus.smartmirror.model.asana.AsanaTasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by FRLU7457 on 2017-03-01.
 */

@Controller
public class AsanaController {
  private static final Logger log = LoggerFactory.getLogger(AsanaController.class);

  @Value("${asana.access.token}")
  private String accessToken;


  public <T> T callAsana(String url, Class<T> expectedClass) {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    String urlPrefix = "https://app.asana.com/api/1.0/";
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.add("Authorization", "Bearer " + accessToken);
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
    ResponseEntity<T> response = restTemplate.exchange(urlPrefix + url, HttpMethod.GET, request, expectedClass);
    return response.getBody();
  }

  public AsanaTasks getAsanaTasks() {
    AsanaProjects projects = callAsana("projects", AsanaProjects.class);
    AsanaProjectData smartMirrorProject = projects.getData()
      .stream()
      .filter(project -> project.getName().equals("Smartmirror"))
      .findFirst().orElse(null);
    if (smartMirrorProject != null) {
      log.debug("Found Asana Project");
      AsanaTasks tasks = callAsana("projects/" + smartMirrorProject.getId() + "/tasks?completed_since=now", AsanaTasks.class);
      if (log.isDebugEnabled()) {
        log.debug("Found the following tasks in the project:");
        tasks.getData().forEach(task -> log.debug(task.getName()));
      }
      return tasks;
    } else {
      log.warn("Could not find an Asana Project called Smartmirror");
    }
    return new AsanaTasks();

  }
}
