package com.maogousoft.logisticsmobile.driver.model;

import java.io.Serializable;

/**
 * Created by EdisonZhao on 15/5/24.
 */
public class InviteInfo implements Serializable {
    private String Contract_url;

    public String getContract_url() {
        return Contract_url;
    }

    public void setContract_url(String contract_url) {
        Contract_url = contract_url;
    }
}
