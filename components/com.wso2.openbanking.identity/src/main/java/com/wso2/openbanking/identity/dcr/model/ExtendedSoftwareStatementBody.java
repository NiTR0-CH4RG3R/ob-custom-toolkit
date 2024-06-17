package com.wso2.openbanking.identity.dcr.model;

import com.google.gson.annotations.SerializedName;
import com.wso2.openbanking.accelerator.identity.dcr.model.SoftwareStatementBody;

/**
 * ExtendedSoftwareStatementBody class is an extension of SoftwareStatementBody class which adds an additional
 * logo_uri parameter to the SSA.
 */
public class ExtendedSoftwareStatementBody extends SoftwareStatementBody {

    public String getLogoURI() {
        return this.logoURI;
    }

    public void setLogoURI(String logoURI) {
        this.logoURI = logoURI;
    }

    @SerializedName("logo_uri")
    private String logoURI;

}
