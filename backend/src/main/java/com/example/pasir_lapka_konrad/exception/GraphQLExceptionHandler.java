package com.example.pasir_lapka_konrad.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class GraphQLExceptionHandler implements DataFetcherExceptionResolver {
    @Override
    public @NotNull Mono<List<GraphQLError>> resolveException(@NotNull Throwable exception, @NotNull DataFetchingEnvironment environment) {
        if (exception instanceof ConstraintViolationException validationException) {
            List<GraphQLError> errors = validationException.getConstraintViolations().stream()
                    .map(violation -> GraphqlErrorBuilder.newError(environment)
                            .message("Błąd walidacji: " + violation.getMessage())
                            .build())
                    .toList();

            return Mono.just(errors);
        }

        GraphQLError error = GraphqlErrorBuilder.newError(environment)
                .message("Wystąpił błąd: " + exception.getMessage())
                .build();

        return Mono.just(List.of(error));
    }
}
