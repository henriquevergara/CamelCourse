package com.microservice.camel.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Esta classe foi utilizada para aprender um pouco do timer (dispara uma ação de tempos em tempos),
 * também foi utilizado o conceito de transform,processor e bean(transform e processor).
 */

@Component
public class MyFirstTimerRouter extends RouteBuilder {

    @Autowired
    private GetCurrentTimeBean getCurrentTimeBean;

    @Autowired
    SimpleLogginProcessingComponent simpleLogginProcessingComponent;

    @Override
    public void configure() throws Exception {
        from("timer:first-timer")
                .log("${body}")
                .transform().constant("My constant message.")
                .log("${body}")
                .bean(getCurrentTimeBean, "getCurrentTime")
                .log("${body}")
                .bean(simpleLogginProcessingComponent)
                .log("${body}")
                .process(new SimpleLogProcessor())
                .to("log:first-timer");
    }

}

@Component
class GetCurrentTimeBean {

    public String getCurrentTime() {
        return "Time now is " + LocalDate.now();
    }

}

@Component
class SimpleLogginProcessingComponent {

    private Logger logger = LoggerFactory.getLogger(SimpleLogginProcessingComponent.class);

    public void process(String message) {
        logger.info("SimpleLogginProcessingComponent {}", message);
    }

}

class SimpleLogProcessor implements Processor {

    private Logger logger = LoggerFactory.getLogger(SimpleLogginProcessingComponent.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("SimpleLogProcessor {}", exchange.getMessage().getBody());
    }
}




