package com.bernerus.smartmirror.controller;

import com.asana.Client;
import com.asana.OAuthApp;
import com.asana.dispatcher.OAuthDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * Created by FRLU7457 on 2017-03-01.
 */
//@Controller
public class AsanaController {
    private static final Logger log = LoggerFactory.getLogger(AsanaController.class);

    @Value("${asana.client.id}")
    private String clientId;

    @Value("${asana.client.secret}")
    private String clientSecret;

    public void connect(){
        OAuthApp app = new OAuthApp(
                clientId,
                clientSecret,
                "urn:ietf:wg:oauth:2.0:oob"
        );
        Client client = Client.oauth(app);

        OAuthDispatcher dispatcher = (OAuthDispatcher)client.dispatcher;
        String state = UUID.randomUUID().toString();
        String url = dispatcher.app.getAuthorizationUrl(state);
        log.info(url);

    }
    @PostConstruct
    public void init(){
        connect();
    }
}
