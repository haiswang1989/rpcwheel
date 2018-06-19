package com.wheel.rpc.serialization.impl.json;

import com.alibaba.fastjson.JSONObject;
import com.wheel.rpc.serialization.impl.AbstractSerializer;

/**
 * 通过Fastjson进行序列化以及反序列化
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月6日 下午2:58:20
 */
public class FastjsonSerializer extends AbstractSerializer {

    @Override
    public byte[] serialize(Object obj) {
        return JSONObject.toJSONBytes(obj);
    }

    @Override
    public <T> T deSerialize(byte[] bytes, Class<T> clazz) {
        return JSONObject.parseObject(new String(bytes), clazz);
    }
}
