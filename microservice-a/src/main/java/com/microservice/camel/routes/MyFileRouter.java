package com.microservice.camel.routes;

import org.apache.camel.Body;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

//@Component
public class MyFileRouter extends RouteBuilder {

    @Autowired
    private DeciderBean deciderBean;

    @Override
    public void configure() throws Exception {
        from("file:files/input")
                //.pipeline()
        .routeId("Files-Input-Route")
        .transform().body(String.class)
        .choice()//Content Based Routing
            .when(simple("${file:ext} == 'xml'"))
                .log("XML file")
                .when(method(deciderBean))
                    .log("Not an XML file but contains USD")
                .otherwise()
                    .log("Not an XML file")
                .end()

        .to("file:files/output");

        from("direct:log-file-values")
                .log("${messageHistory} ${file:absolute.path}")
                .log("${file:name} ${file:name.ext} ${file:name.noext} ${file:onlyname}")
                .log("${file:onlyname.noext} ${file:parent} ${file:path} ${file:absolute}")
                .log("${file:size} ${file:modified}")
                .log("${routeId} ${camelId} ${body}");

    }
}

@Component
class DeciderBean {

    Logger logger = LoggerFactory.getLogger(DeciderBean.class);

    public boolean isThisConditionMet(@Body String body,
                                      @Headers Map<String,String> headers,
                                      @ExchangeProperties Map<String,String> exchangeProperties) {

        logger.info("DeciderBean {} {} {}", body, headers, exchangeProperties);
        return true;
    }
}