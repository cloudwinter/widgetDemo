package com.summer.demo.http.model;

import com.summer.demo.http.entity.ADListInfo;
import com.summer.demo.http.param.ADParam;
import com.summer.network.okhttputil.entity.OKMessage;
import com.summer.network.okhttputil.protocol.OKProtocol;
import com.summer.network.okhttputil.protocol.OKProtocolAction;
import com.summer.network.okhttputil.protocol.OKProtocolGroup;

/**
 * Created by xiayundong on 2017/10/25.
 */

public class OKTestProtocol implements OKProtocol {

    public final static String ADD_URL = "http://172.24.4.3:8080/rebateapp/app/ad";

    static {
        OKProtocolGroup.addProtocolAction(ADParam.class, new OKProtocolAction(ADD_URL, OKMessage.class, ADListInfo.class));
    }

    @Override
    public void add(OKProtocolGroup protocolGroup) {

    }
}
