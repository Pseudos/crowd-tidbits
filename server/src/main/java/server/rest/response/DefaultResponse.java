package server.rest.response;


/**
 * The basic response, containing common factors. To be used for basic response
 * 
 * @author Sydney
 * 
 */
public class DefaultResponse {
    private Integer status;
    private String message;
    private String source;

    /**
     * 
     * @return some valid and sensible HTTP status code
     */
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
