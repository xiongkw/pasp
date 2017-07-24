package com.github.pasp.context.mapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import com.github.pasp.core.IBeanMapper;
import com.github.pasp.core.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.core.ResolvableType;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * IBeanMapper的简单实现
 * </p>
 *
 * @author xiongkw
 */
public class SimpleBeanMapper implements IBeanMapper {

    public static IBeanMapper INSTANCE = new SimpleBeanMapper();

    private SimpleBeanMapper() {

    }

    public <T> T map(Object obj, Class<T> cl) {
        if (obj == null) {
            return null;
        }
        T target = (T) BeanUtils.instantiate(cl);
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(cl);
        if (propertyDescriptors == null || propertyDescriptors.length == 0) {
            return target;
        }
        Class<?> source = obj.getClass();
        for (PropertyDescriptor targetPd : propertyDescriptors) {
            Class<?> propertyType = targetPd.getPropertyType();
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod == null) {
                continue;
            }
            PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source, targetPd.getName());
            if (sourcePd == null) {
                continue;
            }
            Method readMethod = sourcePd.getReadMethod();
            if (readMethod == null) {
                continue;
            }

            try {
                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                    readMethod.setAccessible(true);
                }
                Object value = readMethod.invoke(obj);
                if (propertyType.isAssignableFrom(List.class) && value instanceof List) {
                    Class<?> resolve = ResolvableType.forMethodParameter(writeMethod, 0).getGeneric(0).resolve();
                    if (resolve != null) {
                        value = mapAll((List) value, resolve);
                    }
                } else if (propertyType.isAssignableFrom(Map.class) && value instanceof Map) {
                    Class<?> resolve = ResolvableType.forMethodParameter(writeMethod, 0).getGeneric(1).resolve();
                    Set<Map.Entry> set = ((Map) value).entrySet();
                    if (resolve != null) {
                        value = new HashMap();
                        for (Map.Entry entry : set) {
                            ((Map) value).put(entry.getKey(), map(entry.getValue(), resolve));
                        }
                    }
                } else if (!ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                    value = map(value, propertyType);
                }
                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                    writeMethod.setAccessible(true);
                }
                writeMethod.invoke(target, value);
            } catch (Throwable ex) {
                throw new FatalBeanException(
                        "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
            }
        }
        return target;
    }

    @Override
    public <T> List<T> mapAll(List<?> list, Class<T> cl) {
        if (list == null) {
            return null;
        }
        List<T> result = new ArrayList<T>();
        for (Object o : list) {
            result.add(map(o, cl));
        }
        return result;
    }

    @Override
    public <T, E> Page<T> mapPageInfo(Page<E> pageInfo, Class<T> cl) {
        List<E> list = pageInfo.getList();
        pageInfo.setList(null);

        Page<T> result = new Page<T>();
        BeanUtils.copyProperties(pageInfo, result);
        if (!CollectionUtils.isEmpty(list)) {
            List<T> toList = mapAll(list, cl);
            result.setList(toList);
        }
        return result;
    }
}
