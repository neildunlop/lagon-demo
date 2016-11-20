package com.pathfinder.blog.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.pathfinder.blog.api.BlogService;


/**
 * Google Guice module.
 */
public class BlogModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindServices(serviceBinding(BlogService.class, BlogServiceImpl.class));
    }
}