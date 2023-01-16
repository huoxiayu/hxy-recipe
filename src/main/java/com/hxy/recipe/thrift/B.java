/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.hxy.recipe.thrift;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)", date = "2023-01-16")
public class B implements org.apache.thrift.TBase<B, B._Fields>, java.io.Serializable, Cloneable, Comparable<B> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("B");

  private static final org.apache.thrift.protocol.TField C_FIELD_DESC = new org.apache.thrift.protocol.TField("c", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField B_DOUBLE_FIELD_DESC = new org.apache.thrift.protocol.TField("bDouble", org.apache.thrift.protocol.TType.DOUBLE, (short)2);
  private static final org.apache.thrift.protocol.TField B_LONG_FIELD_DESC = new org.apache.thrift.protocol.TField("bLong", org.apache.thrift.protocol.TType.I64, (short)3);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new BStandardSchemeFactory());
    schemes.put(TupleScheme.class, new BTupleSchemeFactory());
  }

  public C c; // optional
  public double bDouble; // optional
  public long bLong; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    C((short)1, "c"),
    B_DOUBLE((short)2, "bDouble"),
    B_LONG((short)3, "bLong");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // C
          return C;
        case 2: // B_DOUBLE
          return B_DOUBLE;
        case 3: // B_LONG
          return B_LONG;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __BDOUBLE_ISSET_ID = 0;
  private static final int __BLONG_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.C,_Fields.B_DOUBLE,_Fields.B_LONG};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.C, new org.apache.thrift.meta_data.FieldMetaData("c", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, C.class)));
    tmpMap.put(_Fields.B_DOUBLE, new org.apache.thrift.meta_data.FieldMetaData("bDouble", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.DOUBLE)));
    tmpMap.put(_Fields.B_LONG, new org.apache.thrift.meta_data.FieldMetaData("bLong", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(B.class, metaDataMap);
  }

  public B() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public B(B other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetC()) {
      this.c = new C(other.c);
    }
    this.bDouble = other.bDouble;
    this.bLong = other.bLong;
  }

  public B deepCopy() {
    return new B(this);
  }

  @Override
  public void clear() {
    this.c = null;
    setBDoubleIsSet(false);
    this.bDouble = 0.0;
    setBLongIsSet(false);
    this.bLong = 0;
  }

  public C getC() {
    return this.c;
  }

  public B setC(C c) {
    this.c = c;
    return this;
  }

  public void unsetC() {
    this.c = null;
  }

  /** Returns true if field c is set (has been assigned a value) and false otherwise */
  public boolean isSetC() {
    return this.c != null;
  }

  public void setCIsSet(boolean value) {
    if (!value) {
      this.c = null;
    }
  }

  public double getBDouble() {
    return this.bDouble;
  }

  public B setBDouble(double bDouble) {
    this.bDouble = bDouble;
    setBDoubleIsSet(true);
    return this;
  }

  public void unsetBDouble() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __BDOUBLE_ISSET_ID);
  }

  /** Returns true if field bDouble is set (has been assigned a value) and false otherwise */
  public boolean isSetBDouble() {
    return EncodingUtils.testBit(__isset_bitfield, __BDOUBLE_ISSET_ID);
  }

  public void setBDoubleIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __BDOUBLE_ISSET_ID, value);
  }

  public long getBLong() {
    return this.bLong;
  }

  public B setBLong(long bLong) {
    this.bLong = bLong;
    setBLongIsSet(true);
    return this;
  }

  public void unsetBLong() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __BLONG_ISSET_ID);
  }

  /** Returns true if field bLong is set (has been assigned a value) and false otherwise */
  public boolean isSetBLong() {
    return EncodingUtils.testBit(__isset_bitfield, __BLONG_ISSET_ID);
  }

  public void setBLongIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __BLONG_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case C:
      if (value == null) {
        unsetC();
      } else {
        setC((C)value);
      }
      break;

    case B_DOUBLE:
      if (value == null) {
        unsetBDouble();
      } else {
        setBDouble((Double)value);
      }
      break;

    case B_LONG:
      if (value == null) {
        unsetBLong();
      } else {
        setBLong((Long)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case C:
      return getC();

    case B_DOUBLE:
      return getBDouble();

    case B_LONG:
      return getBLong();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case C:
      return isSetC();
    case B_DOUBLE:
      return isSetBDouble();
    case B_LONG:
      return isSetBLong();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof B)
      return this.equals((B)that);
    return false;
  }

  public boolean equals(B that) {
    if (that == null)
      return false;

    boolean this_present_c = true && this.isSetC();
    boolean that_present_c = true && that.isSetC();
    if (this_present_c || that_present_c) {
      if (!(this_present_c && that_present_c))
        return false;
      if (!this.c.equals(that.c))
        return false;
    }

    boolean this_present_bDouble = true && this.isSetBDouble();
    boolean that_present_bDouble = true && that.isSetBDouble();
    if (this_present_bDouble || that_present_bDouble) {
      if (!(this_present_bDouble && that_present_bDouble))
        return false;
      if (this.bDouble != that.bDouble)
        return false;
    }

    boolean this_present_bLong = true && this.isSetBLong();
    boolean that_present_bLong = true && that.isSetBLong();
    if (this_present_bLong || that_present_bLong) {
      if (!(this_present_bLong && that_present_bLong))
        return false;
      if (this.bLong != that.bLong)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_c = true && (isSetC());
    list.add(present_c);
    if (present_c)
      list.add(c);

    boolean present_bDouble = true && (isSetBDouble());
    list.add(present_bDouble);
    if (present_bDouble)
      list.add(bDouble);

    boolean present_bLong = true && (isSetBLong());
    list.add(present_bLong);
    if (present_bLong)
      list.add(bLong);

    return list.hashCode();
  }

  @Override
  public int compareTo(B other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetC()).compareTo(other.isSetC());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetC()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.c, other.c);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetBDouble()).compareTo(other.isSetBDouble());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetBDouble()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.bDouble, other.bDouble);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetBLong()).compareTo(other.isSetBLong());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetBLong()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.bLong, other.bLong);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("B(");
    boolean first = true;

    if (isSetC()) {
      sb.append("c:");
      if (this.c == null) {
        sb.append("null");
      } else {
        sb.append(this.c);
      }
      first = false;
    }
    if (isSetBDouble()) {
      if (!first) sb.append(", ");
      sb.append("bDouble:");
      sb.append(this.bDouble);
      first = false;
    }
    if (isSetBLong()) {
      if (!first) sb.append(", ");
      sb.append("bLong:");
      sb.append(this.bLong);
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws TException {
    // check for required fields
    // check for sub-struct validity
    if (c != null) {
      c.validate();
    }
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class BStandardSchemeFactory implements SchemeFactory {
    public BStandardScheme getScheme() {
      return new BStandardScheme();
    }
  }

  private static class BStandardScheme extends StandardScheme<B> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, B struct) throws TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // C
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.c = new C();
              struct.c.read(iprot);
              struct.setCIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // B_DOUBLE
            if (schemeField.type == org.apache.thrift.protocol.TType.DOUBLE) {
              struct.bDouble = iprot.readDouble();
              struct.setBDoubleIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // B_LONG
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.bLong = iprot.readI64();
              struct.setBLongIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, B struct) throws TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.c != null) {
        if (struct.isSetC()) {
          oprot.writeFieldBegin(C_FIELD_DESC);
          struct.c.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      if (struct.isSetBDouble()) {
        oprot.writeFieldBegin(B_DOUBLE_FIELD_DESC);
        oprot.writeDouble(struct.bDouble);
        oprot.writeFieldEnd();
      }
      if (struct.isSetBLong()) {
        oprot.writeFieldBegin(B_LONG_FIELD_DESC);
        oprot.writeI64(struct.bLong);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class BTupleSchemeFactory implements SchemeFactory {
    public BTupleScheme getScheme() {
      return new BTupleScheme();
    }
  }

  private static class BTupleScheme extends TupleScheme<B> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, B struct) throws TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetC()) {
        optionals.set(0);
      }
      if (struct.isSetBDouble()) {
        optionals.set(1);
      }
      if (struct.isSetBLong()) {
        optionals.set(2);
      }
      oprot.writeBitSet(optionals, 3);
      if (struct.isSetC()) {
        struct.c.write(oprot);
      }
      if (struct.isSetBDouble()) {
        oprot.writeDouble(struct.bDouble);
      }
      if (struct.isSetBLong()) {
        oprot.writeI64(struct.bLong);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, B struct) throws TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(3);
      if (incoming.get(0)) {
        struct.c = new C();
        struct.c.read(iprot);
        struct.setCIsSet(true);
      }
      if (incoming.get(1)) {
        struct.bDouble = iprot.readDouble();
        struct.setBDoubleIsSet(true);
      }
      if (incoming.get(2)) {
        struct.bLong = iprot.readI64();
        struct.setBLongIsSet(true);
      }
    }
  }

}

