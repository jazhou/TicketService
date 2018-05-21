package com.jazhou.ticketservice.rest;

import static com.jazhou.ticketservice.common.Constants.CONCURRENT_UPDATE_ERROR_MESSAGE;
import static com.jazhou.ticketservice.common.Constants.INTERNAL_SERVER_ERROR_MESSAGE;

import com.jazhou.ticketservice.common.RestErrorInformation;
import com.jazhou.ticketservice.common.Utils;
import com.jazhou.ticketservice.exception.ExceedLimitException;
import com.jazhou.ticketservice.exception.ResourceAlreadyExistException;
import com.jazhou.ticketservice.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exception handler for all rest controllers
 */
@ControllerAdvice(annotations = RestController.class)
public class RestControllerErrorHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RestControllerErrorHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)  //404
    @ResponseBody
    public RestErrorInformation handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException)
    {
        return createRestErrorInformation(resourceNotFoundException);
    }

    @ExceptionHandler(ExceedLimitException.class)
    @ResponseStatus(value = HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)  //509
    @ResponseBody
    public RestErrorInformation handleExceedLimitException(ExceedLimitException exceedLimitException)
    {
        return createRestErrorInformation(exceedLimitException);
    }

    @ExceptionHandler({
        IllegalArgumentException.class,
        ResourceAlreadyExistException.class,
        HttpMessageNotReadableException.class
    })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)  //400
    @ResponseBody
    public RestErrorInformation handleIllegalArgumentException(RuntimeException exception)
    {
        return createRestErrorInformation(exception);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)  //500
    @ResponseBody
    public RestErrorInformation handleGenericException(Exception illegalArgumentException)
    {
        LOGGER.error("logger exception:", illegalArgumentException);
        return createRestErrorInformation(INTERNAL_SERVER_ERROR_MESSAGE);
    }

    // concurrent updates
    @ExceptionHandler({ObjectOptimisticLockingFailureException.class, HibernateOptimisticLockingFailureException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)  //400
    @ResponseBody
    public RestErrorInformation handleConcurrentUpdateException(Exception exception)
    {
        LOGGER.error("logger exception:", exception);
        return createRestErrorInformation(CONCURRENT_UPDATE_ERROR_MESSAGE);
    }

    private RestErrorInformation createRestErrorInformation(RuntimeException exception)
    {
        LOGGER.error("logger exception:", exception);
        return createRestErrorInformation(exception.getMessage());
    }

    private RestErrorInformation createRestErrorInformation(String errorMessage)
    {
        RestErrorInformation restErrorInformation = new RestErrorInformation();

        restErrorInformation.setId(Utils.getUniqueId());
        restErrorInformation.setMessage(errorMessage);
        return restErrorInformation;
    }

}
