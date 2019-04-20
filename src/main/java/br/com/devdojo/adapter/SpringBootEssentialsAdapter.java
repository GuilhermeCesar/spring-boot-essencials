package br.com.devdojo.adapter;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

public class SpringBootEssentialsAdapter extends WebMvcConfigurerAdapter {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver pgamr = new PageableHandlerMethodArgumentResolver();
        pgamr.setFallbackPageable(new PageRequest(0,5));

        argumentResolvers.add(pgamr);
    }
}

