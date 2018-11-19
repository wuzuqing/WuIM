package com.wuzuqing.component_im.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author WChao
 * 2017年4月16日 上午11:36:53
 */
public class JsonKit {

	private static SerializeConfig mapping = new SerializeConfig();

	static {
		mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
		mapping.put(java.sql.Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
		mapping.put(java.sql.Timestamp.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
		mapping.put(java.sql.Time.class, new SimpleDateFormatSerializer("HH:mm:ss"));
	}

	public static SerializeConfig put(Class<?> clazz, SerializeFilter filter) {
		mapping.addFilter(clazz, filter);
		return mapping;
	}
	
	public static SerializeConfig put(Class<?> clazz, ObjectSerializer serializer) {
		mapping.put(clazz, serializer);
		return mapping;
	}

	public static <T> T toBean(String jsonString, Class<T> tt) {
		try {
			if (jsonString==null || jsonString.trim().length()==0) {
				return null;
			}

			T t = JSON.parseObject(jsonString, tt);
			return t;
		} catch (Throwable e) {
            System.out.println("json解析失败:\r\n{}"+jsonString);
			throw new RuntimeException(e);
		}
	}
	
	public static <T> T toBean(byte[] bytes, Class<T> tt) {
		try {
			if (bytes == null) {
				return null;
			}

			T t = JSON.parseObject(bytes, tt);
			return t;
		} catch (Throwable e) {
            System.out.println("json解析失败:\r\n{}");
			throw new RuntimeException(e);
		}
	}
	
	public static String toFormatedJson(Object bean) {
		try {
			return JSON.toJSONString(bean, mapping, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.PrettyFormat);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String toJSONEnumNoUsingName(Object bean){
		  int features=SerializerFeature.config(JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.WriteEnumUsingName, false);
		  return JSONObject.toJSONString(bean,features);
	}
	
	public static String toJSONString(Object bean, SerializerFeature serializerFeature){
		  return JSONObject.toJSONString(bean,mapping,serializerFeature);
	}
	
	public static String toJSONString(Object bean) {
		try {
			return JSON.toJSONString(bean, mapping, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public static String toJSONString(Object bean, SerializeFilter serializeFilter) {
		try {
			if (serializeFilter != null) {
				return JSON.toJSONString(bean, mapping, serializeFilter, SerializerFeature.DisableCircularReferenceDetect);
			} else {
				return JSON.toJSONString(bean, mapping, SerializerFeature.DisableCircularReferenceDetect);
			}

		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	public static byte[] toJsonBytes(Object bean) {
		return JSON.toJSONBytes(bean, mapping, SerializerFeature.DisableCircularReferenceDetect);
	}
	
	public static byte[] toJSONBytesEnumNoUsingName(Object bean){
		 int features=SerializerFeature.config(JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.WriteEnumUsingName, false);
		 return JSONObject.toJSONBytes(bean,features);
	}
	
	/**
	 * 成指定类型集合;
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> toArray(List<String> datas, Class<T> clazz){
		if(datas == null){
			return null;
		}
		List<T> result  = new ArrayList<T>();
		for(String obj : datas){
			result.add(toBean(obj, clazz));
		}
		return result;
	}
}
