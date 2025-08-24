package co.com.autentication.r2dbc.adapter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class TransactionGatewayAdapterTest {

  @Mock
  private TransactionalOperator operator;

  @InjectMocks
  private TransactionGatewayAdapter transactionGatewayAdapter;

  @Test
  void execute_shouldCompleteSuccessfully() {
    Mono<String> action = Mono.just("ok");
    when(operator.transactional(any(Mono.class))).thenReturn(action);

    StepVerifier.create(transactionGatewayAdapter.execute(Mono.just("ok")))
        .expectNext("ok")
        .verifyComplete();
  }

  @Test
  void execute_shouldHandleError() {
    RuntimeException ex = new RuntimeException("fail");
    Mono<String> errorMono = Mono.error(ex);
    when(operator.transactional(any(Mono.class))).thenReturn(errorMono);

    StepVerifier.create(transactionGatewayAdapter.execute(Mono.just("fail")))
        .expectErrorMatches(error -> error instanceof RuntimeException && error.getMessage()
            .equals("fail"))
        .verify();
  }
}