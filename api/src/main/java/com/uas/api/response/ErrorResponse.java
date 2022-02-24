package com.uas.api.response;

public class ErrorResponse {

    /**
     * Error response message.
     */
    private String message;

    /**
     * Error response status.
     */
    private String status;

    /**
     * Error response constructor.
     */
    public ErrorResponse() {
    }

    /**
     * Error response constructor.
     * @param message error message.
     * @param status error status.
     */
    public ErrorResponse(final String message, final String status) {
        this.message = message;
        this.status = status;
    }

    /**
     * Get error response message.
     * @return message, response messages.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set error response message.
     * @param message set messages.
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * Get error response status.
     * @return status, the response status.
     */
    public String getStatus() {
        return status;
    }
    /**
     * Set error response status.
     * @param status the error response status.
     */
    public void setStatus(final String status) {
        this.status = status;
    }
}
