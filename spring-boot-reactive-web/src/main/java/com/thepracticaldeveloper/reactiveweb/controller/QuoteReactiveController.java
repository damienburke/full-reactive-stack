package com.thepracticaldeveloper.reactiveweb.controller;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thepracticaldeveloper.reactiveweb.domain.Quote;
import com.thepracticaldeveloper.reactiveweb.repository.QuoteMongoReactiveRepository;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class QuoteReactiveController {

  private static final int DELAY_PER_ITEM_MS = 100;

  private QuoteMongoReactiveRepository quoteMongoReactiveRepository;

  public QuoteReactiveController(final QuoteMongoReactiveRepository quoteMongoReactiveRepository) {
    this.quoteMongoReactiveRepository = quoteMongoReactiveRepository;
  }

  @GetMapping("/quotes-reactive")
  public Flux<Quote> getQuoteFlux() {
    // If you want to use a shorter version of the Flux, use take(100) at the end of the statement
    // below
    return quoteMongoReactiveRepository
        .findAll()
        .delayElements(Duration.ofMillis(DELAY_PER_ITEM_MS));
  }

  @GetMapping("/quotes-reactive-paged")
  public Flux<Quote> getQuoteFlux(
      final @RequestParam(name = "page") int page, final @RequestParam(name = "size") int size) {


    /**
     * The controller - i.. the subscriber - can control how fast the data should be pulled from the
     * database.
     */
    return quoteMongoReactiveRepository
        .retrieveAllQuotesPaged(PageRequest.of(page, size))
        //.subscribe());
        .delayElements(Duration.ofMillis(DELAY_PER_ITEM_MS));
  }

  @GetMapping("/quotes-reactive-count")
  public Mono<Long> getQuoteMono() {
    return quoteMongoReactiveRepository.count();
  }
}
