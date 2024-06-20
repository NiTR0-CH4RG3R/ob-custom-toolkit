/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.com). All Rights Reserved.
 *
 * This software is the property of WSO2 Inc. and its suppliers, if any.
 * Dissemination of any information or reproduction of any material contained
 * herein is strictly forbidden, unless permitted by WSO2 in accordance with
 * the WSO2 Commercial License available at http://wso2.com/licenses.
 * For specific language governing the permissions and limitations under this
 * license, please see the license as well as any agreement you’ve entered into
 * with WSO2 governing the purchase of this software and any associated services.
 */

package com.wso2.openbanking.gateway.executor;

import com.wso2.openbanking.accelerator.common.error.OpenBankingErrorCodes;
import com.wso2.openbanking.accelerator.gateway.executor.core.OpenBankingGatewayExecutor;
import com.wso2.openbanking.accelerator.gateway.executor.model.OBAPIRequestContext;
import com.wso2.openbanking.accelerator.gateway.executor.model.OBAPIResponseContext;
import com.wso2.openbanking.accelerator.gateway.executor.model.OpenBankingExecutorError;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;


/**
 * API Resource Access Validation executor.
 * This executor validates the grant type.
 */
public class SampleOpenBankingGatewayExecutor implements OpenBankingGatewayExecutor {

    @Override
    public void preProcessRequest(OBAPIRequestContext obapiRequestContext) {
        // TASK :   - You should only allow API calls that contain the AISP role in the provided token.
        //          - You should return a proper error message with 403 Forbidden as the HTTP Status.

        // The payload is a json object. So I'm checking if the payload contains an aisp_role attribute in the payload.

        // Convert the payload string to a org.json.simple.JSONObject object.
        JSONObject modifiedPayloadJSON = null;

        try {
            modifiedPayloadJSON = (JSONObject) (new JSONParser()).parse(obapiRequestContext.getModifiedPayload());
        } catch (ParseException e) {
            ArrayList<OpenBankingExecutorError> errors = new ArrayList<>();
            errors.add(new OpenBankingExecutorError(OpenBankingErrorCodes.SERVER_ERROR_CODE,
                    "Internal server error", e.getMessage(), OpenBankingErrorCodes.SERVER_ERROR_CODE));
            obapiRequestContext.setError(true);
            obapiRequestContext.setErrors(errors);
            return;
        }

        // Check if an aisp_role attribute is present in the payload
        if (!modifiedPayloadJSON.containsKey("aisp_role")) {
            ArrayList<OpenBankingExecutorError> errors = new ArrayList<>();
            errors.add(new OpenBankingExecutorError(OpenBankingErrorCodes.FORBIDDEN_CODE,
                    "Forbidden", "aisp_role is required in the payload.", OpenBankingErrorCodes.FORBIDDEN_CODE));
            obapiRequestContext.setError(true);
            obapiRequestContext.setErrors(errors);
            return;
        }



    }

    @Override
    public void postProcessRequest(OBAPIRequestContext obapiRequestContext) {

    }

    @Override
    public void preProcessResponse(OBAPIResponseContext obapiResponseContext) {

    }

    @Override
    public void postProcessResponse(OBAPIResponseContext obapiResponseContext) {

    }
}
