package com.strateratech.dhs.peerrate.web.controller.error;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.strateratech.dhs.peerrate.web.enumeration.I18nErrorKey;
import com.strateratech.dhs.peerrate.web.enumeration.I18nMessageKey;

/**
 * 
 * Controller class - Contains all global exception handler.
 * 
 * @author 2020
 * @version 1.0
 * @date: 08/4/2015
 *
 */
@Component
@ControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);
    @Inject
    private ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource;

    /**
     * Method to build Response when the exception occurs.
     * 
     * @param status
     * @param message
     * @param ex
     * @return RestError
     */
    private ResponseEntity<RestError> buildResponse(HttpStatus status, String message, Throwable ex) {
        RestError restError = new RestError(status, message);
        log.info("GlobalControllerExceptionHandler {}", restError.getStatus(), ex);
        return new ResponseEntity<>(restError, restError.getStatus());
    }

    /**
     * Constructs error response entity with exception.
     * 
     * @param status
     * @param messageKey
     * @param params
     * @param ex
     * @return
     */
    private ResponseEntity<RestError> buildResponse(HttpStatus status, String messageKey, Object[] params,
            Throwable ex) {
        log.info("GlobalControllerExceptionHandler status={} messageKey={}", status, messageKey, ex);
    	log.debug("showing status={} message key={} locale={} resourceBundleMessageSource == null {}", status,
                messageKey, LocaleContextHolder.getLocale(), 
                reloadableResourceBundleMessageSource == null);
        log.debug("messages for {}: {}", LocaleContextHolder.getLocale(), reloadableResourceBundleMessageSource);
        RestError restError = new RestError(status,
                reloadableResourceBundleMessageSource.getMessage(messageKey, params, LocaleContextHolder.getLocale()));

        return new ResponseEntity<>(restError, restError.getStatus());
    }

    /**
     * Constructs error response entity complete using exception and message key
     * as I18nErrorKey enum member
     * 
     * @param status
     * @param messageKey
     * @param params
     * @param ex
     * @return
     */
    private ResponseEntity<RestError> buildResponse(HttpStatus status, I18nErrorKey messageKey, Object[] params,
            Throwable ex) {
        return buildResponse(status, messageKey.name(), params, ex);
    }

    /**
     * Method to handle Global Exceptions - HttpMessageNotReadableException *
     * BAD_REQUEST
     * 
     * @param ex
     * @return RestError
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<RestError> handleGlobalException(HttpMessageNotReadableException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, I18nErrorKey.BAD_REQUEST, new Object[] { ex.getMessage() }, ex);
    }

    /**
     * Method to handle Global Exceptions - IllegalArgumentException BAD_REQUEST
     * 
     * @param ex
     * @return RestError
     */
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<RestError> handleGlobalException(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, I18nErrorKey.BAD_REQUEST, new Object[] { ex.getMessage() }, ex);
    }

    /**
     * Method to handle Global Exceptions - org.apache.lucene.queryparser.classic.ParseException BAD_REQUEST
     * 
     * TODO:  Come back here and make this handler 
     * I18N/Internationalized (load the message from the I18N resource bundle)
     * 
     * @param ex
     * @return RestError
     */
    @ExceptionHandler(org.apache.lucene.queryparser.classic.ParseException.class)
    protected ResponseEntity<RestError> handleGlobalException(org.apache.lucene.queryparser.classic.ParseException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, I18nErrorKey.SEARCH_SYNTAX_ERROR, new Object[] {  }, ex);
    }


    /**
     * Method to handle Global Exceptions - org.springframework.security.access.AccessDeniedException BAD_REQUEST
     * 
     * TODO:  Come back here and make this handler 
     * I18N/Internationalized (load the message from the I18N resource bundle)
     * 
     * @param ex
     * @return RestError
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<RestError> handleGlobalException(AccessDeniedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, I18nErrorKey.FORBIDDEN, new Object[] {  }, ex);
    }

    
    /**
     * Method to handle Global Exceptions - MethodArgumentNotValidException
     * BAD_REQUEST
     * 
     * @param ex
     * @return RestError
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<RestError> handleGlobalException(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder(
                reloadableResourceBundleMessageSource.getMessage("validation_message_error_prefix",
                        new Object[] { ex.getBindingResult().getErrorCount() }, LocaleContextHolder.getLocale()));
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String valMessage = null;
            try {
                log.debug("Data available {}",
                        ToStringBuilder.reflectionToString(ex.getBindingResult(), ToStringStyle.MULTI_LINE_STYLE));
                valMessage = reloadableResourceBundleMessageSource.getMessage(error.getCode(),
                        new Object[] { ex.getBindingResult().getErrorCount() }, LocaleContextHolder.getLocale());
            } catch (NoSuchMessageException nsme) {
                log.warn("Failed to find message for key {} on object{}, try looking here: {}", error.getCode(),
                        error.getObjectName(), error.getCodes());
                for (String altKey : error.getCodes()) {
                    try {
                        valMessage = reloadableResourceBundleMessageSource.getMessage(altKey,
                                new Object[] { ex.getBindingResult() }, LocaleContextHolder.getLocale());
                    } catch (NoSuchMessageException nsme2) {
                        log.debug("{} returned no valid I18N property for validation message.  "
                                + "This may be OK since we try all standard named properties according "
                                + "to spring's strategy for identifying valid properties for a given "
                                + "JSR303 annotated constraint until one returns a valid string.", altKey);
                    }
                    if (!StringUtils.isBlank(valMessage)) {
                        log.debug("identified a validation message for {} ", altKey);
                        break;
                    }
                }
            }
            if (StringUtils.isBlank(valMessage)) {
                log.warn("Unable to locate property for JSR303 annotation error message.  Using binding result "
                        + "error toString() which will not be \"internationalized\" and may not be even "
                        + " intelligable [{}]", ex.getBindingResult().getAllErrors().toString());
                valMessage = ex.getBindingResult().getAllErrors().toString();
            }
            sb.append("[").append(valMessage).append("] ");
        }
        return buildResponse(HttpStatus.BAD_REQUEST, sb.toString(), ex);
    }


    /**
     * Method to handle Global Exceptions - CustomValidationException
     * BAD_REQUEST (400) 
     * 
     * NOTE: This method attempts to interpret binding result errors as I18N message keys.  If it 
     * can't find the right keys, it tries several alternatives until it eventually defaults
     * to a string error which may not be localized and therefore unreadable to our foreign clients. 
     * 
     * @param ex
     * @return RestError
     */
	@ExceptionHandler(CustomValidationException.class)
	protected ResponseEntity<RestError> handleGlobalException(CustomValidationException ex) {
		StringBuilder sb = new StringBuilder(
				reloadableResourceBundleMessageSource.getMessage(I18nErrorKey.VALIDATION_ERROR_PREFIX.name(),
						new Object[] { ex.getErrors().size() }, LocaleContextHolder.getLocale()));
		for (Pair<String, List<Object>> error : ex.getErrors()) {
			String valMessage = null;
			try {
				valMessage = reloadableResourceBundleMessageSource.getMessage(error.getLeft(),
						error.getRight().toArray(), LocaleContextHolder.getLocale());
			} catch (NoSuchMessageException nsme) {
				log.warn("Failed to find message for key {} on object{}, try looking here: {}", error.getLeft());
				try {
					valMessage = reloadableResourceBundleMessageSource.getMessage(I18nMessageKey.GENERIC_ERROR.name(),
							new Object[] { error.getLeft() }, LocaleContextHolder.getLocale());
				} catch (NoSuchMessageException nsme2) {
					log.debug("{} returned no valid I18N property for validation message.  "
							+ "This may be OK since we try all standard named properties according "
							+ "to spring's strategy for identifying valid properties for a given "
							+ "JSR303 annotated constraint until one returns a valid string.", error.getLeft());
				}
			}
		
			if (StringUtils.isBlank(valMessage)) {
				log.warn("Unable to locate property for JSR303 annotation error message.  Using binding result "
						+ "error toString() which will not be \"internationalized\" and may not be even "
						+ " intelligable [{}]", ex.getErrors().toString());
				valMessage = ex.getErrors().toString();
			}
			sb.append("[").append(valMessage).append("] ");
		}
		return buildResponse(HttpStatus.BAD_REQUEST, sb.toString(), ex);
    }

    /**
     * Handles MissingServletRequestParameterException and returns 400 / bad
     * request
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<RestError> handleGlobalException(MissingServletRequestParameterException ex) {
        StringBuilder sb = new StringBuilder(reloadableResourceBundleMessageSource.getMessage(
                I18nErrorKey.VALIDATION_ERROR_PREFIX.name(), new Object[] { 1 }, LocaleContextHolder.getLocale()));

        sb.append(reloadableResourceBundleMessageSource.getMessage(ex.getMessage(), new Object[] {},
                LocaleContextHolder.getLocale()));
        return buildResponse(HttpStatus.BAD_REQUEST, sb.toString(), ex);

    }

    /**
     * Method to handle Global Exceptions - INTERNAL_SERVER_ERROR
     * INTERNAL_SERVER_ERROR
     * 
     * @param ex
     * @return RestError
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<RestError> handleGlobalException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, I18nErrorKey.UNCAUGHT_ERROR,
                new Object[] { ex.getMessage().toString() }, ex);
    }

    /**
     * Method to handle Global Exceptions - BAD_GATEWAY BAD_GATEWAY
     * 
     * @param ex
     * @return RestError
     */
    @ExceptionHandler(IOException.class)
    protected ResponseEntity<RestError> handleGlobalException(IOException ex) {
        return buildResponse(HttpStatus.BAD_GATEWAY, I18nErrorKey.UNCAUGHT_ERROR, new Object[] { ex.getMessage() }, ex);
    }

    /**
     * Method to handle Global Exceptions - NullPointerException
     * 
     * @param ex
     * @return RestError
     */
    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<RestError> handleGlobalException(NullPointerException ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, I18nErrorKey.UNCAUGHT_ERROR,
                new Object[] { "Null Pointer Exception" }, ex);
    }

    /**
     * Method to handle Global Exceptions - EntityNotFoundException * NOT_FOUND
     * 
     * @param ex
     * @return RestError
     */
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<RestError> handleGlobalException(EntityNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, I18nErrorKey.NOT_FOUND, new Object[] { ex.getMessage() }, ex);
    }

    /**
     * Method to handle Global Exceptions -
     * RequestedOublicationVersionVsEntityMismatchException
     * 
     * @param ex
     * @return RestError
     */
    @ExceptionHandler(RequestPublicationVersionVsEntityMismatchException.class)
    protected ResponseEntity<RestError> handleGlobalException(RequestPublicationVersionVsEntityMismatchException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, I18nErrorKey.BAD_REQUEST, new Object[] { ex.getMessage() }, ex);
    }

    /**
     * This is the global exception handler for unsupported media type exception
     * (HTTP 415)
     * 
     * @param ex
     * @return ResponseEntity<RestError>
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<RestError> handleGlobalException(HttpMediaTypeNotSupportedException ex) {
        return buildResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, I18nErrorKey.UNSUPPORTED_MEDIA_TYPE,
                new Object[] { ex.getMessage() }, ex);
    }

}