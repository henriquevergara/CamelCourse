package com.microservice.camel.routes;

import com.microservice.camel.ArrayListAggregationStrategy;
import com.microservice.camel.CurrencyExchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

//@Component
public class PatternsRouter extends RouteBuilder
{
    @Override
    public void configure() throws Exception {

        //Pipeline
        //Content Bases Routing - choice()
        //Multicast

        from("timer:multicast?period={{timePeriod}}")
                .to("log:something1","log:something2");
//
//        from("file:files/csv")
//        .unmarshal().csv()
//        .split(body())
//        .to("log:split-files");
//
//        // Split Message using delimiter
//        from("file:files/csv")
//        .convertBodyTo(String.class)
//        .split(body(),",")
//        .to("log:split-files-delimiter");


        //Aggregate
        //Messages => Aggregate => Endpoint
        from("file:files/aggregate-json")
        .unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class)
        .aggregate(simple("${body.to}"),new ArrayListAggregationStrategy())
        .completionSize(2)
        //.completionTimeout(HIGHEST)
        .to("log:aggregate-json");


    }
}
