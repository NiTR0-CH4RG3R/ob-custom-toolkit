package com.wso2.openbanking.identity.dcr.model;

import com.google.gson.annotations.SerializedName;
import com.wso2.openbanking.accelerator.identity.dcr.model.RegistrationResponse;

/**
 * ExtendedRegistrationResponse class extends the RegistrationResponse class in order to add logo_uri to the
 * registration response for the dcr request.
 */
public class ExtendedRegistrationResponse extends RegistrationResponse {

    public String getLogoURI() {
        return this.logoURI;
    }

    public void setLogoURI(String logoURI) {
        this.logoURI = logoURI;
    }

    @SerializedName("logo_uri")
    private String logoURI;

}
