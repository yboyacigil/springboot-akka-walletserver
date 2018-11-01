package com.yboyacigil.experiments.springboot.akka.walletserver.endpoints;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.concurrent.CompletionException;

@Provider
public class EndpointExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        if (e instanceof CompletionException) {
            Throwable t = e.getCause();

            if (t instanceof Exception) {
                return toResponse((Exception) t);
            } else {
                ErrorInfo errorInfo = errorInfoFromThrowable(t);
                return Response.serverError().entity(errorInfo).type(MediaType.APPLICATION_JSON).build();
            }
        }

        if (e instanceof WebApplicationException) {
            WebApplicationException exception = (WebApplicationException) e;

            Response response = exception.getResponse();

            ErrorInfo errorInfo = ErrorInfo.builder()
                .status(response.getStatus())
                .message(exception.getMessage())
                .cause(exception.getCause() != null ? exception.getCause().toString() : null)
                .build();

            return Response.status(response.getStatus()).entity(errorInfo).type(MediaType.APPLICATION_JSON).build();
        }

        ErrorInfo errorInfo = errorInfoFromThrowable(e);
        return Response.serverError().entity(errorInfo).type(MediaType.APPLICATION_JSON).build();
    }

    private ErrorInfo errorInfoFromThrowable(Throwable t) {
        return ErrorInfo.builder()
            .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
            .message(String.valueOf(t))
            .cause(t.getCause() != null ? t.getCause().toString() : null)
            .build();
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ErrorInfo {
        private int status;
        private String message;
        private String cause;
    }

}
