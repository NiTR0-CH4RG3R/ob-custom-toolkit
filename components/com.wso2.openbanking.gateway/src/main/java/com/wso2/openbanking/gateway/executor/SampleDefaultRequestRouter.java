/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.com). All Rights Reserved.
 *
 * This software is the property of WSO2 Inc. and its suppliers, if any.
 * Dissemination of any information or reproduction of any material contained
 * herein is strictly forbidden, unless permitted by WSO2 in accordance with
 * the WSO2 Commercial License available at http://wso2.com/licenses. For specific
 * language governing the permissions and limitations under this license,
 * please see the license as well as any agreement you’ve entered into with
 * WSO2 governing the purchase of this software and any associated services.
 */

package com.wso2.openbanking.gateway.executor;

import com.wso2.openbanking.accelerator.gateway.executor.core.DefaultRequestRouter;
import com.wso2.openbanking.accelerator.gateway.executor.core.OpenBankingGatewayExecutor;
import com.wso2.openbanking.accelerator.gateway.executor.model.OBAPIRequestContext;
import com.wso2.openbanking.accelerator.gateway.executor.model.OBAPIResponseContext;

import java.util.List;

/**
 * This is just for fun. I wanted to forward all the AISP api calls for my own api flow with a custom gateway executor.
 */
public class SampleDefaultRequestRouter extends DefaultRequestRouter {

    @Override
    public List<OpenBankingGatewayExecutor> getExecutorsForRequest(OBAPIRequestContext requestContext) {
        if (requestContext.getMsgInfo().getResource().contains("/aisp") && this.getExecutorMap().containsKey("AISP")) {
            return this.getExecutorMap().get("AISP");
        }
        return super.getExecutorsForRequest(requestContext);
    }

    @Override
    public List<OpenBankingGatewayExecutor> getExecutorsForResponse(OBAPIResponseContext responseContext) {
        if (responseContext.getMsgInfo().getResource().contains("/aisp") && this.getExecutorMap().containsKey("AISP")) {
            return this.getExecutorMap().get("AISP");
        }
        return super.getExecutorsForResponse(responseContext);
    }
}
