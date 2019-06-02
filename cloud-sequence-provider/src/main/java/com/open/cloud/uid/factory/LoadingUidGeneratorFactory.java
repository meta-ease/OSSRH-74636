package com.open.cloud.uid.factory;

import com.open.cloud.uid.api.UidGenerator;
import com.open.cloud.uid.impl.LoadingUidGenerator;
import com.open.cloud.uid.proxy.UidGeneratorProxy;
import lombok.extern.slf4j.Slf4j;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/2 9:53
 **/
@Slf4j
@Deprecated
public class LoadingUidGeneratorFactory extends UidGeneratorFactory {

    private static LoadingUidGeneratorFactory inst = null;


    private LoadingUidGeneratorFactory() {
        UidGenerator uidGenerator = getUidGenerator(LoadingUidGenerator.class);
        this.uidGeneratorProxy = new UidGeneratorProxy(uidGenerator);
    }


    public static LoadingUidGeneratorFactory getInstance() {
        if (inst == null) {
            synchronized (LoadingUidGeneratorFactory.class) {
                if (inst == null) {
                    inst = new LoadingUidGeneratorFactory();
                }
            }
        }
        return inst;
    }


}
