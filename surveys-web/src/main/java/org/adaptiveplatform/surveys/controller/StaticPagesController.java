package org.adaptiveplatform.surveys.controller;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Properties;

@Controller
public class StaticPagesController {

    @Resource(name = "warDeploymentProperties")
    private Properties warDeploymentProperties;

    @RequestMapping("/")
    public String defaultPage() {
//        return "redirect:/static/surveys-flex.swf";
        return "staticPages/landingPage";
    }

    @RequestMapping("/version")
    public HttpEntity<String> version() {
        StringBuilder body = new StringBuilder();
        body.append("DEPLOYMENT PROPERTIES:\n");
        for (Map.Entry<Object, Object> property : warDeploymentProperties.entrySet()) {
            body.append(property.getKey());
            body.append('=');
            body.append(property.getValue());
            body.append('\n');
        }
        return HttpResponseUtils.createHttpResponse(body.toString(), "text/plain; charset=UTF-8");
    }
}
