package com.example.demo.h2_stream;


import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.example.demo.utils.Utils;
@SpringBootApplication
@RestController
@RequestMapping("/api")
public class StockController {
  /**
   * 
   * @return a bulk a of stock at once ( no streaming)
   */
  @GetMapping("/stock_bulk")
  public List<String> bulk() {
    List<String> l = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      l.add(new String("AAPL-"+ Math.random() * 100));
    }
    return l;
    
  }


 
  
  /**
   * Method reads a few thousand lines from the file and sends it as a response. This is continued as a streaming response. 
   * I have got enought points to believe (90%) it is actually using H2 frames ( streaming response) . ]
   * It is not conclusive if h2 multiplexing is actually utilized.\
   * Plus it is throwing an exception when under heavy load testing with K6  (100 VUS and test for 10 second)
   * If you increase the number of lines read from the file, you will get rid of the exception.
   * org.springframework.web.context.request.async.AsyncRequestNotUsableException: Response not usable after async request completion.
    at org.springframework.web.context.request.async.StandardServletAsyncWebRequest$LifecycleHttpServletResponse.obtainLockAndCheckState(StandardServletAsyncWebRequest.java:314)
   * @return
   * @throws Exception
   */
  @GetMapping("/stock_streaming_response_body")
  public ResponseEntity<StreamingResponseBody> stock_streaming_response_body() throws Exception{
   //THIS
   System.out.println("sending response ");
  
   StreamingResponseBody stream = outputStream -> {
    int i =1;
    List<String> l=new ArrayList<>();
    do {
      try {
        l=Utils.readFile("src/main/resources/data/stock_price.csv", i, i+9999);
        byte[] bytes = l.stream().collect(Collectors.joining("\n")).getBytes();
        outputStream.write(bytes);
        outputStream.flush();
        i = i+10000+1;
        Thread.sleep(10);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }while(!l.isEmpty());
   };
   
    
    return ResponseEntity.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(stream);
  }

  

  /* 
  * Method reads a few thousand records from the db and sends it as a response. This is continued as a streaming response. 
  * I have got enought points to believe (90%) it is actually using H2 frames ( streaming response) . ]
  * It is not conclusive if h2 multiplexing is actually utilized.\
  * Plus it is throwing an exception when under heavy load testing with K6  (100 VUS and test for 10 second)
  * If you increase the number of lines read from the file, you will get rid of the exception.
  * org.springframework.web.context.request.async.AsyncRequestNotUsableException: Response not usable after async request completion.
   at org.springframework.web.context.request.async.StandardServletAsyncWebRequest$LifecycleHttpServletResponse.obtainLockAndCheckState(StandardServletAsyncWebRequest.java:314)
  * @return
  */
  
  @GetMapping("/stock_stream_db")
  public ResponseEntity<StreamingResponseBody> stock_stream_db() throws Exception{
   //THIS
   System.out.println("sending response ");
  
   StreamingResponseBody stream = outputStream -> {
    int i =1;
    List<String> l=new ArrayList<>();
    do {
      try {
        l=Utils.readFromDB(10000,i);
        byte[] bytes = l.stream().collect(Collectors.joining("\n")).getBytes();
        outputStream.write(bytes);
        outputStream.flush();
        i++;
        Thread.sleep(10); // thought this will help client from overwhelming but no success
      } catch (Exception e) {
        e.printStackTrace();
      }
    }while(!l.isEmpty());
   };
   
    
    return ResponseEntity.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(stream);
  }

  /**
   * FLUX is a reactive stream which can help in sending streaming response but encountered some errors here too.
   * @return
   */
  @GetMapping("/flux_file")
  public Flux<String> flux_file() {
   return Flux.fromIterable(Utils.readFile("src/main/resources/stocks.txt", 0, 1000));
    // List<String> l = new ArrayList<>();
    // for (int i = 0; i < 1000; i++) {
    //   l.add(new String("AAPL-"+ Math.random() * 100));
    // }
    // return Flux.fromStream( l.stream() ).delayElements(Duration.ofMillis(100));
    // Simulate generating new stock data
    // return Flux.generate(emitter -> {
    //   for (int i = 0; i < 100000; i++) {
    //     String stock = new String("AAPL-"+ Math.random() * 100);
    //     emitter.next(stock);
    //   }
    //   emitter.complete(); // Simulate one-time data for simplicity
    // });
  }
  @GetMapping("/stock_stream")
  public Flux<String> all() {
    List<String> l = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      l.add(new String("AAPL-"+ Math.random() * 100));
    }
    return Flux.fromStream( l.stream() ).delayElements(Duration.ofMillis(100));
    // Simulate generating new stock data
    // return Flux.generate(emitter -> {
    //   for (int i = 0; i < 100000; i++) {
    //     String stock = new String("AAPL-"+ Math.random() * 100);
    //     emitter.next(stock);
    //   }
    //   emitter.complete(); // Simulate one-time data for simplicity
    // });
  }

  public static void main(String[] args) {
    SpringApplication.run(StockController.class, args);
  }
}