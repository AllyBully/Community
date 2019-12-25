package com.kmsoft.community.dto;

public class AccessTokenDTO {
    private String client_Id;
    private String client_secret;
    private String code;
    private String redirect_url;
    private String state;

    public String getClient_Id() {
        return client_Id;
    }

    public void setClient_Id(String client_Id) {
        this.client_Id = client_Id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
