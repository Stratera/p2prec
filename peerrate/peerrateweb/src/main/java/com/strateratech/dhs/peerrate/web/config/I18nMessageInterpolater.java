package com.strateratech.dhs.peerrate.web.config;

import java.util.Locale;

import javax.inject.Inject;
import javax.validation.MessageInterpolator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * Class responsible to mapping error codes to 18n messages
 * 
 * @author 2020
 * @version 1.0
 * @date: 08/4/2015
 *
 */
@Component("messageInterpolator")
public class I18nMessageInterpolater implements MessageInterpolator, MessageSourceAware, InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(I18nMessageInterpolater.class);

    @Inject
    private MessageSource messageSource;

    /**
     * interpolates messages using context with default local
     */
    @Override
    public String interpolate(String messageTemplate, Context context) {
        log.debug("Using ThreadLocal locale {} to resolve validation message", LocaleContextHolder.getLocale());
        return messageSource.getMessage(messageTemplate, new Object[] {}, LocaleContextHolder.getLocale());
    }

    /**
     * Interpolates 18n locale specific messages based on locale of client
     */
    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        log.debug("Using supplied locale {} to resolve validation message", locale);
        return messageSource.getMessage(messageTemplate, new Object[] {}, locale);
    }

    /**
     * @param messageSource
     */
    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * required by the intefrace implemented. not sure what this method is
     * supposed to do
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (messageSource == null) {
            throw new IllegalStateException(
                    "MessageSource was not injected, could not initialize " + this.getClass().getSimpleName());
        }
    }

}