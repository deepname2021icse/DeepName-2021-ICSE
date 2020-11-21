// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: shared.proto

package bm.project.types;

public final class Shared {
  private Shared() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  /**
   * Protobuf enum {@code boa.types.ChangeKind}
   *
   * <pre>
   ** Describes the kind of change for an artifact or program entity/element 
   * </pre>
   */
  public enum ChangeKind
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <code>UNKNOWN = 0;</code>
     */
    UNKNOWN(0, 0),
    /**
     * <code>ADDED = 1;</code>
     *
     * <pre>
     ** The artifact or program entity/element did not already exist and was added 
     * </pre>
     */
    ADDED(1, 1),
    /**
     * <code>DELETED = 2;</code>
     *
     * <pre>
     ** The artifact or program entity/element was deleted 
     * </pre>
     */
    DELETED(2, 2),
    /**
     * <code>MODIFIED = 3;</code>
     *
     * <pre>
     ** The artifact or program entity/element already existed and was modified 
     * </pre>
     */
    MODIFIED(4, 3),
    /**
     * <code>RENAMED = 4;</code>
     *
     * <pre>
     ** The label of the artifact or program entity/element was renamed 
     * </pre>
     */
    RENAMED(6, 4),
    /**
     * <code>MOVED = 5;</code>
     *
     * <pre>
     ** The artifact or program entity/element was moved to a different parent 
     * </pre>
     */
    MOVED(7, 5),
    /**
     * <code>COPIED = 6;</code>
     *
     * <pre>
     ** The artifact or program entity/element was copied from another one 
     * </pre>
     */
    COPIED(8, 6),
    /**
     * <code>MERGED = 7;</code>
     *
     * <pre>
     ** The artifact or program entity/element was merged 
     * </pre>
     */
    MERGED(9, 7),
    /**
     * <code>UNCHANGED = 8;</code>
     *
     * <pre>
     ** The artifact or program entity/element was unchanged 
     * </pre>
     */
    UNCHANGED(10, 8),
    /**
     * <code>UNMAPPED = 9;</code>
     */
    UNMAPPED(11, 9),
    ;

    /**
     * <code>REMOVED = 2;</code>
     *
     * <pre>
     ** @exclude 
     * </pre>
     */
    public static final ChangeKind REMOVED = DELETED;
    /**
     * <code>CHANGED = 3;</code>
     *
     * <pre>
     ** The artifact or program entity/element already existed and was changed 
     * </pre>
     */
    public static final ChangeKind CHANGED = MODIFIED;
    /**
     * <code>UNKNOWN = 0;</code>
     */
    public static final int UNKNOWN_VALUE = 0;
    /**
     * <code>ADDED = 1;</code>
     *
     * <pre>
     ** The artifact or program entity/element did not already exist and was added 
     * </pre>
     */
    public static final int ADDED_VALUE = 1;
    /**
     * <code>DELETED = 2;</code>
     *
     * <pre>
     ** The artifact or program entity/element was deleted 
     * </pre>
     */
    public static final int DELETED_VALUE = 2;
    /**
     * <code>REMOVED = 2;</code>
     *
     * <pre>
     ** @exclude 
     * </pre>
     */
    public static final int REMOVED_VALUE = 2;
    /**
     * <code>MODIFIED = 3;</code>
     *
     * <pre>
     ** The artifact or program entity/element already existed and was modified 
     * </pre>
     */
    public static final int MODIFIED_VALUE = 3;
    /**
     * <code>CHANGED = 3;</code>
     *
     * <pre>
     ** The artifact or program entity/element already existed and was changed 
     * </pre>
     */
    public static final int CHANGED_VALUE = 3;
    /**
     * <code>RENAMED = 4;</code>
     *
     * <pre>
     ** The label of the artifact or program entity/element was renamed 
     * </pre>
     */
    public static final int RENAMED_VALUE = 4;
    /**
     * <code>MOVED = 5;</code>
     *
     * <pre>
     ** The artifact or program entity/element was moved to a different parent 
     * </pre>
     */
    public static final int MOVED_VALUE = 5;
    /**
     * <code>COPIED = 6;</code>
     *
     * <pre>
     ** The artifact or program entity/element was copied from another one 
     * </pre>
     */
    public static final int COPIED_VALUE = 6;
    /**
     * <code>MERGED = 7;</code>
     *
     * <pre>
     ** The artifact or program entity/element was merged 
     * </pre>
     */
    public static final int MERGED_VALUE = 7;
    /**
     * <code>UNCHANGED = 8;</code>
     *
     * <pre>
     ** The artifact or program entity/element was unchanged 
     * </pre>
     */
    public static final int UNCHANGED_VALUE = 8;
    /**
     * <code>UNMAPPED = 9;</code>
     */
    public static final int UNMAPPED_VALUE = 9;


    public final int getNumber() { return value; }

    public static ChangeKind valueOf(int value) {
      switch (value) {
        case 0: return UNKNOWN;
        case 1: return ADDED;
        case 2: return DELETED;
        case 3: return MODIFIED;
        case 4: return RENAMED;
        case 5: return MOVED;
        case 6: return COPIED;
        case 7: return MERGED;
        case 8: return UNCHANGED;
        case 9: return UNMAPPED;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<ChangeKind>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<ChangeKind>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<ChangeKind>() {
            public ChangeKind findValueByNumber(int number) {
              return ChangeKind.valueOf(number);
            }
          };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(index);
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return bm.project.types.Shared.getDescriptor().getEnumTypes().get(0);
    }

    private static final ChangeKind[] VALUES = {
      UNKNOWN, ADDED, DELETED, REMOVED, MODIFIED, CHANGED, RENAMED, MOVED, COPIED, MERGED, UNCHANGED, UNMAPPED, 
    };

    public static ChangeKind valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }

    private final int index;
    private final int value;

    private ChangeKind(int index, int value) {
      this.index = index;
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:boa.types.ChangeKind)
  }

  public interface PersonOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    // required string username = 1;
    /**
     * <code>required string username = 1;</code>
     *
     * <pre>
     ** The person's username 
     * </pre>
     */
    boolean hasUsername();
    /**
     * <code>required string username = 1;</code>
     *
     * <pre>
     ** The person's username 
     * </pre>
     */
    java.lang.String getUsername();
    /**
     * <code>required string username = 1;</code>
     *
     * <pre>
     ** The person's username 
     * </pre>
     */
    com.google.protobuf.ByteString
        getUsernameBytes();

    // optional string real_name = 2;
    /**
     * <code>optional string real_name = 2;</code>
     *
     * <pre>
     ** The person's real name, if known 
     * </pre>
     */
    boolean hasRealName();
    /**
     * <code>optional string real_name = 2;</code>
     *
     * <pre>
     ** The person's real name, if known 
     * </pre>
     */
    java.lang.String getRealName();
    /**
     * <code>optional string real_name = 2;</code>
     *
     * <pre>
     ** The person's real name, if known 
     * </pre>
     */
    com.google.protobuf.ByteString
        getRealNameBytes();

    // optional string email = 3;
    /**
     * <code>optional string email = 3;</code>
     *
     * <pre>
     ** The person's email address, if known 
     * </pre>
     */
    boolean hasEmail();
    /**
     * <code>optional string email = 3;</code>
     *
     * <pre>
     ** The person's email address, if known 
     * </pre>
     */
    java.lang.String getEmail();
    /**
     * <code>optional string email = 3;</code>
     *
     * <pre>
     ** The person's email address, if known 
     * </pre>
     */
    com.google.protobuf.ByteString
        getEmailBytes();
  }
  /**
   * Protobuf type {@code boa.types.Person}
   *
   * <pre>
   ** A unique person's information 
   * </pre>
   */
  public static final class Person extends
      com.google.protobuf.GeneratedMessage
      implements PersonOrBuilder {
    // Use Person.newBuilder() to construct.
    private Person(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private Person(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final Person defaultInstance;
    public static Person getDefaultInstance() {
      return defaultInstance;
    }

    public Person getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private Person(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              bitField0_ |= 0x00000001;
              username_ = input.readBytes();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              realName_ = input.readBytes();
              break;
            }
            case 26: {
              bitField0_ |= 0x00000004;
              email_ = input.readBytes();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return bm.project.types.Shared.internal_static_boa_types_Person_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return bm.project.types.Shared.internal_static_boa_types_Person_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              bm.project.types.Shared.Person.class, bm.project.types.Shared.Person.Builder.class);
    }

    public static com.google.protobuf.Parser<Person> PARSER =
        new com.google.protobuf.AbstractParser<Person>() {
      public Person parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Person(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<Person> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    // required string username = 1;
    public static final int USERNAME_FIELD_NUMBER = 1;
    private java.lang.Object username_;
    /**
     * <code>required string username = 1;</code>
     *
     * <pre>
     ** The person's username 
     * </pre>
     */
    public boolean hasUsername() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>required string username = 1;</code>
     *
     * <pre>
     ** The person's username 
     * </pre>
     */
    public java.lang.String getUsername() {
      java.lang.Object ref = username_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          username_ = s;
        }
        return s;
      }
    }
    /**
     * <code>required string username = 1;</code>
     *
     * <pre>
     ** The person's username 
     * </pre>
     */
    public com.google.protobuf.ByteString
        getUsernameBytes() {
      java.lang.Object ref = username_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        username_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    // optional string real_name = 2;
    public static final int REAL_NAME_FIELD_NUMBER = 2;
    private java.lang.Object realName_;
    /**
     * <code>optional string real_name = 2;</code>
     *
     * <pre>
     ** The person's real name, if known 
     * </pre>
     */
    public boolean hasRealName() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional string real_name = 2;</code>
     *
     * <pre>
     ** The person's real name, if known 
     * </pre>
     */
    public java.lang.String getRealName() {
      java.lang.Object ref = realName_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          realName_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string real_name = 2;</code>
     *
     * <pre>
     ** The person's real name, if known 
     * </pre>
     */
    public com.google.protobuf.ByteString
        getRealNameBytes() {
      java.lang.Object ref = realName_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        realName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    // optional string email = 3;
    public static final int EMAIL_FIELD_NUMBER = 3;
    private java.lang.Object email_;
    /**
     * <code>optional string email = 3;</code>
     *
     * <pre>
     ** The person's email address, if known 
     * </pre>
     */
    public boolean hasEmail() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional string email = 3;</code>
     *
     * <pre>
     ** The person's email address, if known 
     * </pre>
     */
    public java.lang.String getEmail() {
      java.lang.Object ref = email_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          email_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string email = 3;</code>
     *
     * <pre>
     ** The person's email address, if known 
     * </pre>
     */
    public com.google.protobuf.ByteString
        getEmailBytes() {
      java.lang.Object ref = email_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        email_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private void initFields() {
      username_ = "";
      realName_ = "";
      email_ = "";
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      if (!hasUsername()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getUsernameBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getRealNameBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(3, getEmailBytes());
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getUsernameBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getRealNameBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, getEmailBytes());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static bm.project.types.Shared.Person parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static bm.project.types.Shared.Person parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static bm.project.types.Shared.Person parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static bm.project.types.Shared.Person parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static bm.project.types.Shared.Person parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static bm.project.types.Shared.Person parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static bm.project.types.Shared.Person parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static bm.project.types.Shared.Person parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static bm.project.types.Shared.Person parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static bm.project.types.Shared.Person parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(bm.project.types.Shared.Person prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code boa.types.Person}
     *
     * <pre>
     ** A unique person's information 
     * </pre>
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements bm.project.types.Shared.PersonOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return bm.project.types.Shared.internal_static_boa_types_Person_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return bm.project.types.Shared.internal_static_boa_types_Person_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                bm.project.types.Shared.Person.class, bm.project.types.Shared.Person.Builder.class);
      }

      // Construct using boa.types.Shared.Person.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        username_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        realName_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        email_ = "";
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return bm.project.types.Shared.internal_static_boa_types_Person_descriptor;
      }

      public bm.project.types.Shared.Person getDefaultInstanceForType() {
        return bm.project.types.Shared.Person.getDefaultInstance();
      }

      public bm.project.types.Shared.Person build() {
        bm.project.types.Shared.Person result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public bm.project.types.Shared.Person buildPartial() {
        bm.project.types.Shared.Person result = new bm.project.types.Shared.Person(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.username_ = username_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.realName_ = realName_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.email_ = email_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof bm.project.types.Shared.Person) {
          return mergeFrom((bm.project.types.Shared.Person)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(bm.project.types.Shared.Person other) {
        if (other == bm.project.types.Shared.Person.getDefaultInstance()) return this;
        if (other.hasUsername()) {
          bitField0_ |= 0x00000001;
          username_ = other.username_;
          onChanged();
        }
        if (other.hasRealName()) {
          bitField0_ |= 0x00000002;
          realName_ = other.realName_;
          onChanged();
        }
        if (other.hasEmail()) {
          bitField0_ |= 0x00000004;
          email_ = other.email_;
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (!hasUsername()) {
          
          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        bm.project.types.Shared.Person parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (bm.project.types.Shared.Person) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      // required string username = 1;
      private java.lang.Object username_ = "";
      /**
       * <code>required string username = 1;</code>
       *
       * <pre>
       ** The person's username 
       * </pre>
       */
      public boolean hasUsername() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>required string username = 1;</code>
       *
       * <pre>
       ** The person's username 
       * </pre>
       */
      public java.lang.String getUsername() {
        java.lang.Object ref = username_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          username_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>required string username = 1;</code>
       *
       * <pre>
       ** The person's username 
       * </pre>
       */
      public com.google.protobuf.ByteString
          getUsernameBytes() {
        java.lang.Object ref = username_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          username_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>required string username = 1;</code>
       *
       * <pre>
       ** The person's username 
       * </pre>
       */
      public Builder setUsername(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        username_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required string username = 1;</code>
       *
       * <pre>
       ** The person's username 
       * </pre>
       */
      public Builder clearUsername() {
        bitField0_ = (bitField0_ & ~0x00000001);
        username_ = getDefaultInstance().getUsername();
        onChanged();
        return this;
      }
      /**
       * <code>required string username = 1;</code>
       *
       * <pre>
       ** The person's username 
       * </pre>
       */
      public Builder setUsernameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        username_ = value;
        onChanged();
        return this;
      }

      // optional string real_name = 2;
      private java.lang.Object realName_ = "";
      /**
       * <code>optional string real_name = 2;</code>
       *
       * <pre>
       ** The person's real name, if known 
       * </pre>
       */
      public boolean hasRealName() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional string real_name = 2;</code>
       *
       * <pre>
       ** The person's real name, if known 
       * </pre>
       */
      public java.lang.String getRealName() {
        java.lang.Object ref = realName_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          realName_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string real_name = 2;</code>
       *
       * <pre>
       ** The person's real name, if known 
       * </pre>
       */
      public com.google.protobuf.ByteString
          getRealNameBytes() {
        java.lang.Object ref = realName_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          realName_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string real_name = 2;</code>
       *
       * <pre>
       ** The person's real name, if known 
       * </pre>
       */
      public Builder setRealName(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        realName_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string real_name = 2;</code>
       *
       * <pre>
       ** The person's real name, if known 
       * </pre>
       */
      public Builder clearRealName() {
        bitField0_ = (bitField0_ & ~0x00000002);
        realName_ = getDefaultInstance().getRealName();
        onChanged();
        return this;
      }
      /**
       * <code>optional string real_name = 2;</code>
       *
       * <pre>
       ** The person's real name, if known 
       * </pre>
       */
      public Builder setRealNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        realName_ = value;
        onChanged();
        return this;
      }

      // optional string email = 3;
      private java.lang.Object email_ = "";
      /**
       * <code>optional string email = 3;</code>
       *
       * <pre>
       ** The person's email address, if known 
       * </pre>
       */
      public boolean hasEmail() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional string email = 3;</code>
       *
       * <pre>
       ** The person's email address, if known 
       * </pre>
       */
      public java.lang.String getEmail() {
        java.lang.Object ref = email_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          email_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string email = 3;</code>
       *
       * <pre>
       ** The person's email address, if known 
       * </pre>
       */
      public com.google.protobuf.ByteString
          getEmailBytes() {
        java.lang.Object ref = email_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          email_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string email = 3;</code>
       *
       * <pre>
       ** The person's email address, if known 
       * </pre>
       */
      public Builder setEmail(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        email_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string email = 3;</code>
       *
       * <pre>
       ** The person's email address, if known 
       * </pre>
       */
      public Builder clearEmail() {
        bitField0_ = (bitField0_ & ~0x00000004);
        email_ = getDefaultInstance().getEmail();
        onChanged();
        return this;
      }
      /**
       * <code>optional string email = 3;</code>
       *
       * <pre>
       ** The person's email address, if known 
       * </pre>
       */
      public Builder setEmailBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        email_ = value;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:boa.types.Person)
    }

    static {
      defaultInstance = new Person(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:boa.types.Person)
  }

  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_boa_types_Person_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_boa_types_Person_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\014shared.proto\022\tboa.types\"<\n\006Person\022\020\n\010u" +
      "sername\030\001 \002(\t\022\021\n\treal_name\030\002 \001(\t\022\r\n\005emai" +
      "l\030\003 \001(\t*\252\001\n\nChangeKind\022\013\n\007UNKNOWN\020\000\022\t\n\005A" +
      "DDED\020\001\022\013\n\007DELETED\020\002\022\013\n\007REMOVED\020\002\022\014\n\010MODI" +
      "FIED\020\003\022\013\n\007CHANGED\020\003\022\013\n\007RENAMED\020\004\022\t\n\005MOVE" +
      "D\020\005\022\n\n\006COPIED\020\006\022\n\n\006MERGED\020\007\022\r\n\tUNCHANGED" +
      "\020\010\022\014\n\010UNMAPPED\020\t\032\002\020\001B\002H\001"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_boa_types_Person_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_boa_types_Person_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_boa_types_Person_descriptor,
              new java.lang.String[] { "Username", "RealName", "Email", });
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }

  // @@protoc_insertion_point(outer_class_scope)
}