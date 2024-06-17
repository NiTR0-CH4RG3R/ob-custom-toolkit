/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.com). All Rights Reserved.
 *
 * This software is the property of WSO2 Inc. and its suppliers, if any.
 * Dissemination of any information or reproduction of any material contained
 * herein is strictly forbidden, unless permitted by WSO2 in accordance with
 * the WSO2 Software License available at https://wso2.com/licenses/eula/3.1.
 * For specific language governing the permissions and limitations under this
 * license, please see the license as well as any agreement you’ve entered into
 * with WSO2 governing the purchase of this software and any associated services.
 */

package com.wso2.openbanking.identity.dcr.validation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.wso2.openbanking.accelerator.identity.dcr.exception.DCRValidationException;
import com.wso2.openbanking.accelerator.identity.dcr.model.RegistrationRequest;
import com.wso2.openbanking.accelerator.identity.dcr.utils.ValidatorUtils;
import com.wso2.openbanking.accelerator.identity.dcr.validation.DCRCommonConstants;
import com.wso2.openbanking.accelerator.identity.dcr.validation.DefaultRegistrationValidatorImpl;
import com.wso2.openbanking.identity.dcr.model.ExtendedRegistrationResponse;
import com.wso2.openbanking.identity.dcr.model.ExtendedSoftwareStatementBody;

import java.util.Map;

/**
 * Sample implementation for dcr registration VALIDATOR class.
 */
public class SampleRegistrationValidatorImpl extends DefaultRegistrationValidatorImpl {

    @Override
    public void validatePost(RegistrationRequest registrationRequest) throws DCRValidationException {
        // Call the validatePost() method in the parent class so we don't miss any validations done by the parent class
        super.validatePost(registrationRequest);

        // In the doc they have used a utility class called ValidationUtils. But now that class is missing.
        // This is the closest thing. So...
        ValidatorUtils.getValidationViolations(registrationRequest);

        // Check whether the SSA parameters contain a parameter named logo_uri. If not throw the exception.
        if (!registrationRequest.getSsaParameters().containsKey("logoUri")) {
            throw new DCRValidationException(DCRCommonConstants.INVALID_SSA, "SSA must contain a logoUri parameter");
        }
    }

    @Override
    public void validateGet(String clientId) throws DCRValidationException {
        super.validateGet(clientId);
    }

    @Override
    public void validateDelete(String clientId) throws DCRValidationException {
        super.validateDelete(clientId);
    }

    @Override
    public void validateUpdate(RegistrationRequest registrationRequest) throws DCRValidationException {
        super.validateUpdate(registrationRequest);
    }

    @Override
    public void setSoftwareStatementPayload(RegistrationRequest registrationRequest, String decodedSSA) {
        // Set the extended software statement instead of the normal one
        ExtendedSoftwareStatementBody extendedSoftwareStatementBody = new GsonBuilder().create()
                .fromJson(decodedSSA, ExtendedSoftwareStatementBody.class);
        registrationRequest.setSoftwareStatementBody(extendedSoftwareStatementBody);
    }

    @Override
    public String getRegistrationResponse(Map<String, Object> spMetaData) {
        // It seems that the logo_uri is already present in the spMetaData map. So we only have to map the spMetaData
        // map to the ExtendedRegistrationResponse object.
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(spMetaData);
        ExtendedRegistrationResponse extendedRegistrationResponse =
                gson.fromJson(jsonElement, ExtendedRegistrationResponse.class);
        return gson.toJson(extendedRegistrationResponse);
    }
}
