package cz.cuni.mff.tablecreator;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;

public class TableCreator {

    public static String process(Class<?> cl) {
        // TODO: implement
        StringBuilder sql = new StringBuilder();
        Field[] declaredFields = cl.getDeclaredFields();
        sql.append("CREATE TABLE ");
        if (cl.isAnnotationPresent(Entity.class) && !cl.getAnnotation(Entity.class).name().isEmpty()) {
            sql.append(cl.getAnnotation(Entity.class).name());
        } else {
            sql.append(cl.getSimpleName());
        }
        sql.append("(");
        for (Field field : declaredFields) {
            System.out.println("haha " + field.getAnnotation(Enumerated.class));
            if (field.isAnnotationPresent(Id.class)) {
                sql.append(field.getName() + " BIGINT PRIMARY KEY ");
            }
            if (field.isAnnotationPresent(GeneratedValue.class)) {
                sql.append("AUTO_INCREMENT, ");
            }

            else if (field.isAnnotationPresent(Column.class)) {
                if (field.getAnnotation(Column.class).name().isEmpty()) {
                    if (field.isAnnotationPresent(Enumerated.class)){
                        sql.append(field.getName() + " " + SQLTypeMapper.getSqlString(field.getAnnotation(Enumerated.class).value(),
                                Optional.of(field.getAnnotation(Column.class).length())) + ", ");
                    }else {
                        sql.append(field.getName() + " " + SQLTypeMapper.getSqlString(field.getType(),
                                Optional.of(field.getAnnotation(Column.class).length())) + ", ");
                    }
                } else {
                    if (field.isAnnotationPresent(Enumerated.class)){
                        sql.append(field.getAnnotation(Column.class).name() + " " + SQLTypeMapper.getSqlString(field.getAnnotation(Enumerated.class).value(),
                                Optional.of(field.getAnnotation(Column.class).length())) + ", ");
                    }else {
                        sql.append(field.getAnnotation(Column.class).name() + " " + SQLTypeMapper.getSqlString(field.getType(),
                                Optional.of(field.getAnnotation(Column.class).length())) + ", ");
                    }
                }
                if (!field.getAnnotation(Column.class).nullable()){
                    sql.replace(sql.length() - 2, sql.length(),"");
                    sql.append(" NOT NULL, ");
                }
            } else if (!field.isAnnotationPresent(Transient.class) && !Modifier.isStatic(field.getModifiers())) {
                if (field.isAnnotationPresent(Enumerated.class)){
                    sql.append(field.getName() + " " + SQLTypeMapper.getSqlString(field.getAnnotation(Enumerated.class).value(), Optional.empty()) + ", ");
                }else {
                    sql.append(field.getName() + " " + SQLTypeMapper.getSqlString(field.getType(), Optional.empty()) + ", ");
                }
            }

        }
        sql.replace(sql.length() - 2, sql.length(),"");
        sql.append(");");

        return sql.toString();
    }

}
