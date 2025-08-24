package co.com.autentication.r2dbc.adapter;

import co.com.autentication.model.gateways.TransactionGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

/**
 * Adapter implementation for managing database transactions using R2DBC.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionGatewayAdapter implements TransactionGateway {

  private final TransactionalOperator operator;

  @Override
  public <T> Mono<T> execute(Mono<T> action) {
    log.info("Executing transaction");
    return operator.transactional(action)
        .doOnSuccess(result -> log.info("Transaction completed successfully"))
        .doOnError(error -> log.error("Transaction failed", error));
  }
}
