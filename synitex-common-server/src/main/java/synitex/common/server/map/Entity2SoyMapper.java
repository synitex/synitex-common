package synitex.common.server.map;

import com.google.common.base.Function;
import com.google.template.soy.data.SoyListData;
import com.google.template.soy.data.SoyMapData;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import javax.inject.Singleton;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class Entity2SoyMapper implements IEntity2SoyMapper {

    private static Set<Class> primitiveTypes = new HashSet<Class>();
    static {
        primitiveTypes.add(String.class);
        primitiveTypes.add(Integer.class);
        primitiveTypes.add(Integer.TYPE);
        primitiveTypes.add(Long.class);
        primitiveTypes.add(Long.TYPE);
        primitiveTypes.add(Boolean.class);
        primitiveTypes.add(Boolean.TYPE);
        primitiveTypes.add(Double.class);
        primitiveTypes.add(Double.TYPE);
    }

    public Entity2SoyMapper() {

    }

    @Override
    public SoyMapData map(Object obj) {
        return mapImpl(obj);
    }

    @Override
    public <T extends Object> SoyListData mapList(List<T> objects) {
        return buildSoyList(objects, new Function<T, SoyMapData>() {
            @Override
            public SoyMapData apply(T value) {
                SoyMapData mapData = mapImpl(value);
                return mapData;
            }
        });
    }

    private SoyMapData mapImpl(final Object data) {
        final SoyMapData map = new SoyMapData();
        ReflectionUtils.doWithFields(data.getClass(), new FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                ReflectionUtils.makeAccessible(field);
                Object obj = field.get(data);
                if (obj == null) {
                    return;
                }

                String id = field.getName();
                Class<?> type = field.getType();
                Type gtype = field.getGenericType();

                if (isListType(type, gtype)) {

                    Class<?> itemType = resolveClassOfListItem(gtype);
                    if (primitiveTypes.contains(itemType)) {
                        map.put(id, obj);
                    /*} else if (itemType == LocaleString.class) {
                        List listOfStrings = Lists.transform((List) obj, new Function<Object, String>() {
                            @Override
                            public String apply(Object value) {
                                return stringResolver.resolve((LocaleString) value);
                            }
                        });
                        synitex.common.servermap.put(id, listOfStrings);*/
                    } else {
                        map.put(id, buildSoyList((List) obj, new Function<Object, SoyMapData>() {
                            @Override
                            public SoyMapData apply(Object value) {
                                SoyMapData mapData = mapImpl(value);
                                return mapData;
                            }
                        }));
                    }

                } else if (primitiveTypes.contains(type)) {

                    if(type == Long.TYPE || type == Long.class) {
                        Long longObj = (Long) obj;
                        double doubleObj = longObj.doubleValue();
                        map.put(id, doubleObj);
                    } else {
                        map.put(id, obj);
                    }

                } else {

                    map.put(id, mapImpl(obj));

                }
            }
        });

        return map;
    }

    private <IN> SoyListData buildSoyList(List<IN> list, Function<IN, SoyMapData> function) {
        if (CollectionUtils.isEmpty(list)) {
            return new SoyListData();
        }
        SoyListData data = new SoyListData();
        for (IN t : list) {
            SoyMapData r = function.apply(t);
            if (r != null) {
                data.add(r);
            }
        }
        return data;
    }

    private boolean isListType(Class<?> clazz, Type gtype) {
        return List.class.isAssignableFrom(clazz) && ParameterizedType.class.isAssignableFrom(gtype.getClass());
    }

    private Class<?> resolveClassOfListItem(Type gtype) {
        ParameterizedType pt = (ParameterizedType) gtype;
        Type[] actualTypes = pt.getActualTypeArguments();
        Class<?> itemType = (Class<?>) actualTypes[0];
        return itemType;
    }

}
