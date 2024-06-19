package com.wso2.openbanking.consent.extensions.manage.impl;

import com.wso2.openbanking.accelerator.common.exception.ConsentManagementException;
import com.wso2.openbanking.accelerator.consent.extensions.common.ConsentException;
import com.wso2.openbanking.accelerator.consent.extensions.common.ResponseStatus;
import com.wso2.openbanking.accelerator.consent.extensions.manage.impl.DefaultConsentManageHandler;
import com.wso2.openbanking.accelerator.consent.extensions.manage.model.ConsentManageData;
import com.wso2.openbanking.accelerator.consent.mgt.service.ConsentCoreService;
import com.wso2.openbanking.accelerator.consent.mgt.service.impl.ConsentCoreServiceImpl;
import net.minidev.json.JSONObject;

import java.util.HashMap;

/**
 * This is a Consent Management Handle Implementation that expands from the DefaultConsentManageHandler created as part
 * of the OB toolkit tutorial. The only part this extends is the post function that store a custom attribute in the
 * consent_attributes table.
 */
public class ConsentManageHandlerImpl extends DefaultConsentManageHandler {

    public ConsentManageHandlerImpl() {
    }

    @Override
    public void handlePost(ConsentManageData consentManageData) throws ConsentException {
        // Call the default handlePost, so it can do all the default stuff done by the DefaultConsentManagerHandler.
        super.handlePost(consentManageData);

        // Check whether the is_one_off_consent attribute is present in the request payload.
        JSONObject requestPayload = (JSONObject) consentManageData.getPayload();
        // NOTE :   In the DefaultConsentManageHandler implementation they have done a sanity check where they check
        //          whether the request payload isn't null and some other stuff. We don't have to do that here since
        //          we are calling the super function. But if we are implementing this class from scratch make sure
        //          to do those checks. LOL!.

        // Check if the request payload contains a key named 'is_one_off_consent'.
        if (!requestPayload.containsKey("is_one_off_consent")) {
            throw new ConsentException(ResponseStatus.BAD_REQUEST,
                    "is_one_off_consent attribute is missing from the payload.");
        }

        boolean isOneOffConsent = (boolean) requestPayload.get("is_one_off_consent");

        HashMap<String, String> additionalAttributes = new HashMap<>();
        additionalAttributes.put( "is_one_off_consent", String.valueOf(isOneOffConsent) );

        // The DefaultConsentManageHandler class stores the newly created consentId in the Data.ConsentId in the
        // response payload in the consentManageData.
        JSONObject responsePayload = (JSONObject) consentManageData.getResponsePayload();
        JSONObject data = (JSONObject) responsePayload.get("Data");
        String consentId = data.getAsString("ConsentId");

        // Create an object of ConsentCoreServiceImpl class, so we can get access to the storeConsentAttributes()
        // function that calls to the database and store our attributes.
        ConsentCoreService consentCoreService = new ConsentCoreServiceImpl();

        try {
            // Store the attribute.
            consentCoreService.storeConsentAttributes(consentId, additionalAttributes);
        } catch (ConsentManagementException e) {
            throw new ConsentException(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        // We must append the additional attributes to the response as well
        // NOTE :   It is better if we could add the custom attributes all at once from using something like the
        //          'additionalAttributes' variable. So, we will only have to worry about adding them to the map above,
        //          and they will be automatically appended to the response. But the problem is 'additionalAttributes'
        //          is a <String, String> hash map. And all the type information of the value will be lost. We could
        //          maintain a separate <String, Object> map, but it seems like a way too much overkill for this task.
        //          This is just a note for the future.

        data.put("is_one_off_consent", isOneOffConsent);
        responsePayload.replace("Data", data);
        consentManageData.setResponsePayload(responsePayload);
    }


}
