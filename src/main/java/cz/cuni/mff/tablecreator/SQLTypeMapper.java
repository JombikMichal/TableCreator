package cz.cuni.mff.tablecreator;

import javax.persistence.EnumType;
import java.util.Map;
import java.util.Optional;

public class SQLTypeMapper {
    private static final int DEFAULT_SIZE = 255;
    private static Map<Class,String> map =
            Map.of(Integer.class,"INTEGER",
            String.class, "VARCHAR",
            int.class, "INTEGER");



    private static Class getTypeFromEnum(EnumType enumType){
       switch (enumType){
           case STRING:
               return String.class;
           case ORDINAL:
               return Integer.class;
           default:
               throw new IllegalArgumentException("Unsupported enum!");
       }
    }

    public static String getSqlString(Class T, Optional<Integer> size){
        System.out.println(T);
        if(map.containsKey(T)){
            if(T == String.class){
                if(size.isPresent()){
                    return map.get(T) + "(" + size.get() +")";
                }else {
                    return map.get(T) + "(" + DEFAULT_SIZE +")";
                }
            }
            return map.get(T);
        }else {
            throw new IllegalArgumentException("Not supported");
        }
    }

    public static String getSqlString(EnumType enumType, Optional<Integer> size){
        Class T = getTypeFromEnum(enumType);
        if(map.containsKey(T)){
            if (T == String.class){
                if(size.isPresent()){
                    return map.get(T) + "(" + size.get() +")";
                }else {
                    return map.get(T) + "(" + DEFAULT_SIZE +")";
                }
            }
            return map.get(T);

        }else {
            throw new IllegalArgumentException("Not supported");
        }
    }
}
