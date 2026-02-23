package com.tutorial.ai.linkedin_agent.external;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.net.URI;
import java.util.Map;

@HttpExchange("https://api.linkedin.com/v2")
public interface LinkedInImageRegister {

    @PostExchange("/assets?action=registerUpload")
    Map<String,Object> registerImage(@RequestBody Map<String,Object> body, @RequestParam("oauth2_access_token") String token);

    @PostExchange
    void uploadImage(URI uploadUrl, @RequestHeader("Authorization") String header, @RequestHeader("Content-type") String contentType, @RequestBody byte[] binaryImageFile);

    @PostExchange("/ugcPosts")
    void postImage(@RequestHeader("Authorization") String header,  @RequestHeader("X-Restli-Protocol-Version") String restliVersion, @RequestBody Map<String,Object> body);

}
