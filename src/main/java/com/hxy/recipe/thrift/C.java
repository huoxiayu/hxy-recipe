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
public class C implements org.apache.thrift.TBase<C, C._Fields>, java.io.Serializable, Cloneable, Comparable<C> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("C");

  private static final org.apache.thrift.protocol.TField C_INT_FIELD_DESC = new org.apache.thrift.protocol.TField("cInt", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField C_LONG_FIELD_DESC = new org.apache.thrift.protocol.TField("cLong", org.apache.thrift.protocol.TType.I64, (short)2);
  private static final org.apache.thrift.protocol.TField C_INT_LIST_FIELD_DESC = new org.apache.thrift.protocol.TField("cIntList", org.apache.thrift.protocol.TType.LIST, (short)3);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new CStandardSchemeFactory());
    schemes.put(TupleScheme.class, new CTupleSchemeFactory());
  }

  public int cInt; // optional
  public long cLong; // optional
  public List<Integer> cIntList; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    C_INT((short)1, "cInt"),
    C_LONG((short)2, "cLong"),
    C_INT_LIST((short)3, "cIntList");

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
        case 1: // C_INT
          return C_INT;
        case 2: // C_LONG
          return C_LONG;
        case 3: // C_INT_LIST
          return C_INT_LIST;
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
  private static final int __CINT_ISSET_ID = 0;
  private static final int __CLONG_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.C_INT,_Fields.C_LONG,_Fields.C_INT_LIST};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.C_INT, new org.apache.thrift.meta_data.FieldMetaData("cInt", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.C_LONG, new org.apache.thrift.meta_data.FieldMetaData("cLong", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.C_INT_LIST, new org.apache.thrift.meta_data.FieldMetaData("cIntList", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(C.class, metaDataMap);
  }

  public C() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public C(C other) {
    __isset_bitfield = other.__isset_bitfield;
    this.cInt = other.cInt;
    this.cLong = other.cLong;
    if (other.isSetCIntList()) {
      List<Integer> __this__cIntList = new ArrayList<Integer>(other.cIntList);
      this.cIntList = __this__cIntList;
    }
  }

  public C deepCopy() {
    return new C(this);
  }

  @Override
  public void clear() {
    setCIntIsSet(false);
    this.cInt = 0;
    setCLongIsSet(false);
    this.cLong = 0;
    this.cIntList = null;
  }

  public int getCInt() {
    return this.cInt;
  }

  public C setCInt(int cInt) {
    this.cInt = cInt;
    setCIntIsSet(true);
    return this;
  }

  public void unsetCInt() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __CINT_ISSET_ID);
  }

  /** Returns true if field cInt is set (has been assigned a value) and false otherwise */
  public boolean isSetCInt() {
    return EncodingUtils.testBit(__isset_bitfield, __CINT_ISSET_ID);
  }

  public void setCIntIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __CINT_ISSET_ID, value);
  }

  public long getCLong() {
    return this.cLong;
  }

  public C setCLong(long cLong) {
    this.cLong = cLong;
    setCLongIsSet(true);
    return this;
  }

  public void unsetCLong() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __CLONG_ISSET_ID);
  }

  /** Returns true if field cLong is set (has been assigned a value) and false otherwise */
  public boolean isSetCLong() {
    return EncodingUtils.testBit(__isset_bitfield, __CLONG_ISSET_ID);
  }

  public void setCLongIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __CLONG_ISSET_ID, value);
  }

  public int getCIntListSize() {
    return (this.cIntList == null) ? 0 : this.cIntList.size();
  }

  public java.util.Iterator<Integer> getCIntListIterator() {
    return (this.cIntList == null) ? null : this.cIntList.iterator();
  }

  public void addToCIntList(int elem) {
    if (this.cIntList == null) {
      this.cIntList = new ArrayList<Integer>();
    }
    this.cIntList.add(elem);
  }

  public List<Integer> getCIntList() {
    return this.cIntList;
  }

  public C setCIntList(List<Integer> cIntList) {
    this.cIntList = cIntList;
    return this;
  }

  public void unsetCIntList() {
    this.cIntList = null;
  }

  /** Returns true if field cIntList is set (has been assigned a value) and false otherwise */
  public boolean isSetCIntList() {
    return this.cIntList != null;
  }

  public void setCIntListIsSet(boolean value) {
    if (!value) {
      this.cIntList = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case C_INT:
      if (value == null) {
        unsetCInt();
      } else {
        setCInt((Integer)value);
      }
      break;

    case C_LONG:
      if (value == null) {
        unsetCLong();
      } else {
        setCLong((Long)value);
      }
      break;

    case C_INT_LIST:
      if (value == null) {
        unsetCIntList();
      } else {
        setCIntList((List<Integer>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case C_INT:
      return getCInt();

    case C_LONG:
      return getCLong();

    case C_INT_LIST:
      return getCIntList();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case C_INT:
      return isSetCInt();
    case C_LONG:
      return isSetCLong();
    case C_INT_LIST:
      return isSetCIntList();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof C)
      return this.equals((C)that);
    return false;
  }

  public boolean equals(C that) {
    if (that == null)
      return false;

    boolean this_present_cInt = true && this.isSetCInt();
    boolean that_present_cInt = true && that.isSetCInt();
    if (this_present_cInt || that_present_cInt) {
      if (!(this_present_cInt && that_present_cInt))
        return false;
      if (this.cInt != that.cInt)
        return false;
    }

    boolean this_present_cLong = true && this.isSetCLong();
    boolean that_present_cLong = true && that.isSetCLong();
    if (this_present_cLong || that_present_cLong) {
      if (!(this_present_cLong && that_present_cLong))
        return false;
      if (this.cLong != that.cLong)
        return false;
    }

    boolean this_present_cIntList = true && this.isSetCIntList();
    boolean that_present_cIntList = true && that.isSetCIntList();
    if (this_present_cIntList || that_present_cIntList) {
      if (!(this_present_cIntList && that_present_cIntList))
        return false;
      if (!this.cIntList.equals(that.cIntList))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_cInt = true && (isSetCInt());
    list.add(present_cInt);
    if (present_cInt)
      list.add(cInt);

    boolean present_cLong = true && (isSetCLong());
    list.add(present_cLong);
    if (present_cLong)
      list.add(cLong);

    boolean present_cIntList = true && (isSetCIntList());
    list.add(present_cIntList);
    if (present_cIntList)
      list.add(cIntList);

    return list.hashCode();
  }

  @Override
  public int compareTo(C other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetCInt()).compareTo(other.isSetCInt());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCInt()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.cInt, other.cInt);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetCLong()).compareTo(other.isSetCLong());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCLong()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.cLong, other.cLong);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetCIntList()).compareTo(other.isSetCIntList());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCIntList()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.cIntList, other.cIntList);
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
    StringBuilder sb = new StringBuilder("C(");
    boolean first = true;

    if (isSetCInt()) {
      sb.append("cInt:");
      sb.append(this.cInt);
      first = false;
    }
    if (isSetCLong()) {
      if (!first) sb.append(", ");
      sb.append("cLong:");
      sb.append(this.cLong);
      first = false;
    }
    if (isSetCIntList()) {
      if (!first) sb.append(", ");
      sb.append("cIntList:");
      if (this.cIntList == null) {
        sb.append("null");
      } else {
        sb.append(this.cIntList);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws TException {
    // check for required fields
    // check for sub-struct validity
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

  private static class CStandardSchemeFactory implements SchemeFactory {
    public CStandardScheme getScheme() {
      return new CStandardScheme();
    }
  }

  private static class CStandardScheme extends StandardScheme<C> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, C struct) throws TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // C_INT
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.cInt = iprot.readI32();
              struct.setCIntIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // C_LONG
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.cLong = iprot.readI64();
              struct.setCLongIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // C_INT_LIST
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list0 = iprot.readListBegin();
                struct.cIntList = new ArrayList<Integer>(_list0.size);
                int _elem1;
                for (int _i2 = 0; _i2 < _list0.size; ++_i2)
                {
                  _elem1 = iprot.readI32();
                  struct.cIntList.add(_elem1);
                }
                iprot.readListEnd();
              }
              struct.setCIntListIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, C struct) throws TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.isSetCInt()) {
        oprot.writeFieldBegin(C_INT_FIELD_DESC);
        oprot.writeI32(struct.cInt);
        oprot.writeFieldEnd();
      }
      if (struct.isSetCLong()) {
        oprot.writeFieldBegin(C_LONG_FIELD_DESC);
        oprot.writeI64(struct.cLong);
        oprot.writeFieldEnd();
      }
      if (struct.cIntList != null) {
        if (struct.isSetCIntList()) {
          oprot.writeFieldBegin(C_INT_LIST_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.I32, struct.cIntList.size()));
            for (int _iter3 : struct.cIntList)
            {
              oprot.writeI32(_iter3);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class CTupleSchemeFactory implements SchemeFactory {
    public CTupleScheme getScheme() {
      return new CTupleScheme();
    }
  }

  private static class CTupleScheme extends TupleScheme<C> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, C struct) throws TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetCInt()) {
        optionals.set(0);
      }
      if (struct.isSetCLong()) {
        optionals.set(1);
      }
      if (struct.isSetCIntList()) {
        optionals.set(2);
      }
      oprot.writeBitSet(optionals, 3);
      if (struct.isSetCInt()) {
        oprot.writeI32(struct.cInt);
      }
      if (struct.isSetCLong()) {
        oprot.writeI64(struct.cLong);
      }
      if (struct.isSetCIntList()) {
        {
          oprot.writeI32(struct.cIntList.size());
          for (int _iter4 : struct.cIntList)
          {
            oprot.writeI32(_iter4);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, C struct) throws TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(3);
      if (incoming.get(0)) {
        struct.cInt = iprot.readI32();
        struct.setCIntIsSet(true);
      }
      if (incoming.get(1)) {
        struct.cLong = iprot.readI64();
        struct.setCLongIsSet(true);
      }
      if (incoming.get(2)) {
        {
          org.apache.thrift.protocol.TList _list5 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.I32, iprot.readI32());
          struct.cIntList = new ArrayList<Integer>(_list5.size);
          int _elem6;
          for (int _i7 = 0; _i7 < _list5.size; ++_i7)
          {
            _elem6 = iprot.readI32();
            struct.cIntList.add(_elem6);
          }
        }
        struct.setCIntListIsSet(true);
      }
    }
  }

}

