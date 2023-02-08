package com.sahil.ecom.dto.response;


/**
 * The interface Response dto.
 *
 * @param <T> the type parameter
 */
public interface ResponseDto<T> {

    /**
     * Sets message.
     *
     * @param message the message
     */
    void setMessage(String message);

    /**
     * Sets code.
     *
     * @param code the code
     */
    void setCode(Integer code);

    /**
     * Sets status.
     *
     * @param status the status
     */
    void setStatus(Integer status);

    /**
     * Sets data.
     *
     * @param data the data
     */
    void setData(T data);

    /**
     * Gets message.
     *
     * @return the message
     */
    String getMessage();

    /**
     * Gets code.
     *
     * @return the code
     */
    Integer getCode();

    /**
     * Gets status.
     *
     * @return the status
     */
    Integer getStatus();

    /**
     * Gets data.
     *
     * @return the data
     */
    T getData();
}
