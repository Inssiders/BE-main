package com.inssider.api.common.config;

import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class EndpointPrefixConfig implements WebMvcConfigurer {

  @Override
  public void configurePathMatch(@NonNull PathMatchConfigurer configurer) {
    configurer.addPathPrefix(
        "/api", c -> c.getPackageName().startsWith("com.inssider.api.domains"));
  }
}
