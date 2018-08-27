package com.inventories;

import io.sentry.Sentry;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import javax.servlet.Filter;

@SpringBootApplication
//@ComponentScan(basePackages = "com.inventories")
public class InventoriesApplication {

    private static SentryClient sentry;

    public static void main(String[] args) {
        Sentry.init("https://9ffe2965c3034768a1c6e6524d2f1c70@sentry.io/1254226");
        sentry = SentryClientFactory.sentryClient();

        SpringApplication.run(InventoriesApplication.class, args);
    }

    //To Support Cache API
    @Bean
    public Filter filter(){
        ShallowEtagHeaderFilter filter=new ShallowEtagHeaderFilter();
        return filter;
    }
    //End Support Cache API
}
