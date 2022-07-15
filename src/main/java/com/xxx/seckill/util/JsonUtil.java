package com.xxx.seckill.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Json工具类
 */
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();


    /**
     * 将对象转换成json字符串
     * @param obj
     * @return
     */
    public static String object2JsonStr(Object obj){
        try{
            return objectMapper.writeValueAsString(obj);
        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将字符串转换为对象
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T jsonStr2Object(String jsonStr, Class<T> clazz){
        try{
            return objectMapper.readValue(jsonStr.getBytes("UTF-8"), clazz);
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将json数据转化成pojo对象list
     * @param jsonStr
     * @param beanType
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToList(String jsonStr, Class<T> beanType){
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, beanType);
        try{
            List<T> list = objectMapper.readValue(jsonStr, javaType);
            return list;
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }



}
