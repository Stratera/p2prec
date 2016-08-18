package com.strateratech.dhs.peerrate.web.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import com.strateratech.dhs.peerrate.web.controller.error.CustomValidationException;

/**
 * Provides various utility methods for converting/interacting with framework
 * objects separate from each other or business objects
 * 
 * @author 2020
 * @date Jul 13, 2016
 * @version
 *
 */
@Component
public class WebFrameworkHelper {
    private static final Logger log = LoggerFactory.getLogger(WebFrameworkHelper.class);
    @Inject
    private ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource;

    /**
     * Constructs a CustomValidationException from a Spring Errors Object
     * 
     * tries multiple possible valid message keys to find the right one (one for
     * which we have a valid error message configured). Each possibility throws
     * a NoSuchMessageException exception when there is no message for a given
     * key (which spring tries to guess at) so we catch it, log it and try the
     * next. If NO possibility returns a valid message, we use the default code
     * string as the message and move on.
     * 
     * possible error code keys provided by spring are ordered from most
     * specific to least specific. Below is an example of the error message keys
     * discovered when the section pattern is violated on
     * SymbolNameStructure.class [Pattern.symbolNameStructure.section,
     * Pattern.section, Pattern.java.lang.String, Pattern]
     * 
     * @param restModelObject
     * @param objectErrors
     * @return
     * @since Jul 13, 2016
     */
    public CustomValidationException newValidationException(Errors objectErrors) {
        List<Pair<String, List<Object>>> errors = new ArrayList<>();
        if (objectErrors != null && objectErrors.getAllErrors() != null) {
            for (ObjectError e : objectErrors.getAllErrors()) {
                log.debug("Found error code [{}] alternates are [{}] ", e.getCode(), e.getCodes());
                String code = null;
                for (String possibleCode : e.getCodes()) {
                    try {
                        String msg = reloadableResourceBundleMessageSource.getMessage(possibleCode, e.getArguments(),
                                LocaleContextHolder.getLocale());
                        log.debug("found {}", msg);
                        if (StringUtils.isNotBlank(msg)) {
                            code = possibleCode;
                            break;
                        }
                    } catch (NoSuchMessageException ex) {
                        log.info("no message found for key {}", possibleCode);
                        // NoSuchMessageException exception is thrown by Spring's framework when you
                        // try to access a message key that is not in the message properties file.
                        // The stacktrace for this is not interesting other than to know that it happened
                        // and what key property was missed.  The stacktrace is purposely ignored since it 
                        // would not be helpful and instead would just be noise in the logs.
                    }
                }
                if (StringUtils.isBlank(code)) {
                    code = e.getCode();
                }
                errors.add(Pair.of(code, Arrays.asList(e.getArguments())));
            }
        }
        return new CustomValidationException(errors);
    }

}
