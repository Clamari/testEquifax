package com.equifax.course;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
	@PropertySource("classpath:responseCodes.properties")
	})
public class GlobalPropertiesConfig {

}