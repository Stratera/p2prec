package com.strateratech.dhs.peerrate.web.controller;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

/**
 * 
 * class to register application converters and formatters.
 * 
 * @author 2020
 * @version 1.0
 * @date: 08/4/2015
 *
 */
@Configurable
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	/**
	 * Method to install label Converters
	 * 
	 * @param registry
	 */
	public void installLabelConverters(FormatterRegistry registry) {
	//	registry.addConverter(new SymbolNameConverter());
	//	registry.addConverter(new ProjectDraftTypeConverter());
	}

	/**
	 * Method to make calls after the properties are set.
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		installLabelConverters(getObject());
	}
}
