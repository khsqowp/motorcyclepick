//package com.example.motorcyclepick.typehandler;
//
//import org.apache.ibatis.type.BaseTypeHandler;
//import org.apache.ibatis.type.JdbcType;
//
//import java.sql.CallableStatement;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class CustomEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {
//    private final Class<E> type;
//
//    public CustomEnumTypeHandler(Class<E> type) {
//        if (type == null) {
//            throw new IllegalArgumentException("Type argument cannot be null");
//        }
//        this.type = type;
//    }
//
//    @Override
//    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
//        ps.setInt(i, parameter.ordinal());
//    }
//
//    @Override
//    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
//        int ordinal = rs.getInt(columnName);
//        return rs.wasNull() ? null : type.getEnumConstants()[ordinal];
//    }
//
//    @Override
//    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
//        int ordinal = rs.getInt(columnIndex);
//        return rs.wasNull() ? null : type.getEnumConstants()[ordinal];
//    }
//
//    @Override
//    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
//        int ordinal = cs.getInt(columnIndex);
//        return cs.wasNull() ? null : type.getEnumConstants()[ordinal];
//    }
//}