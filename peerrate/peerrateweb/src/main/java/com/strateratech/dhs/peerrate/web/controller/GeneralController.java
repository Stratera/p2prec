package com.strateratech.dhs.peerrate.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import springfox.documentation.annotations.ApiIgnore;

/**
 * Does basic redirect to root of uri application based on the root url
 * configured in the environment configuration.
 *
 * @author 2020
 * @version 1.0
 * @date: 08/4/2015
 *
 */

/**
 * We omitted this from REST API documentation because it is not a real rest
 * endpoint. It's only purpose is to facilitate automatic redirects while the UI
 * does the saml processing.
 *
 */
@ApiIgnore
@Component
@Controller
public class GeneralController {

    @Value("${ui_base_url}")
    private String uiRootUrl;

    /**
     * Method does the Url redirect.
     *
     * @return String uiRootUrl
     */
    @RequestMapping(value = "/")
    public String redirectRootUrl() {
        return "redirect:" + uiRootUrl + "/";
    }

}
