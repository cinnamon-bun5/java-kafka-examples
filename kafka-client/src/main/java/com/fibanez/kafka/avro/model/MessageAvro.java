/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.fibanez.kafka.avro.model;

import org.apache.avro.specific.SpecificData;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class MessageAvro extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -3036976700688813931L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"MessageAvro\",\"namespace\":\"com.fibanez.kafka.avro.model\",\"fields\":[{\"name\":\"id\",\"type\":\"long\"},{\"name\":\"message\",\"type\":\"string\"},{\"name\":\"timestamp\",\"type\":\"long\"},{\"name\":\"error\",\"type\":[\"string\",\"null\"]},{\"name\":\"errorCode\",\"type\":[\"long\",\"null\"]}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public long id;
  @Deprecated public java.lang.CharSequence message;
  @Deprecated public long timestamp;
  @Deprecated public java.lang.CharSequence error;
  @Deprecated public java.lang.Long errorCode;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public MessageAvro() {}

  /**
   * All-args constructor.
   * @param id The new value for id
   * @param message The new value for message
   * @param timestamp The new value for timestamp
   * @param error The new value for error
   * @param errorCode The new value for errorCode
   */
  public MessageAvro(java.lang.Long id, java.lang.CharSequence message, java.lang.Long timestamp, java.lang.CharSequence error, java.lang.Long errorCode) {
    this.id = id;
    this.message = message;
    this.timestamp = timestamp;
    this.error = error;
    this.errorCode = errorCode;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return id;
    case 1: return message;
    case 2: return timestamp;
    case 3: return error;
    case 4: return errorCode;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: id = (java.lang.Long)value$; break;
    case 1: message = (java.lang.CharSequence)value$; break;
    case 2: timestamp = (java.lang.Long)value$; break;
    case 3: error = (java.lang.CharSequence)value$; break;
    case 4: errorCode = (java.lang.Long)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'id' field.
   * @return The value of the 'id' field.
   */
  public java.lang.Long getId() {
    return id;
  }

  /**
   * Sets the value of the 'id' field.
   * @param value the value to set.
   */
  public void setId(java.lang.Long value) {
    this.id = value;
  }

  /**
   * Gets the value of the 'message' field.
   * @return The value of the 'message' field.
   */
  public java.lang.CharSequence getMessage() {
    return message;
  }

  /**
   * Sets the value of the 'message' field.
   * @param value the value to set.
   */
  public void setMessage(java.lang.CharSequence value) {
    this.message = value;
  }

  /**
   * Gets the value of the 'timestamp' field.
   * @return The value of the 'timestamp' field.
   */
  public java.lang.Long getTimestamp() {
    return timestamp;
  }

  /**
   * Sets the value of the 'timestamp' field.
   * @param value the value to set.
   */
  public void setTimestamp(java.lang.Long value) {
    this.timestamp = value;
  }

  /**
   * Gets the value of the 'error' field.
   * @return The value of the 'error' field.
   */
  public java.lang.CharSequence getError() {
    return error;
  }

  /**
   * Sets the value of the 'error' field.
   * @param value the value to set.
   */
  public void setError(java.lang.CharSequence value) {
    this.error = value;
  }

  /**
   * Gets the value of the 'errorCode' field.
   * @return The value of the 'errorCode' field.
   */
  public java.lang.Long getErrorCode() {
    return errorCode;
  }

  /**
   * Sets the value of the 'errorCode' field.
   * @param value the value to set.
   */
  public void setErrorCode(java.lang.Long value) {
    this.errorCode = value;
  }

  /**
   * Creates a new MessageAvro RecordBuilder.
   * @return A new MessageAvro RecordBuilder
   */
  public static com.fibanez.kafka.avro.model.MessageAvro.Builder newBuilder() {
    return new com.fibanez.kafka.avro.model.MessageAvro.Builder();
  }

  /**
   * Creates a new MessageAvro RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new MessageAvro RecordBuilder
   */
  public static com.fibanez.kafka.avro.model.MessageAvro.Builder newBuilder(com.fibanez.kafka.avro.model.MessageAvro.Builder other) {
    return new com.fibanez.kafka.avro.model.MessageAvro.Builder(other);
  }

  /**
   * Creates a new MessageAvro RecordBuilder by copying an existing MessageAvro instance.
   * @param other The existing instance to copy.
   * @return A new MessageAvro RecordBuilder
   */
  public static com.fibanez.kafka.avro.model.MessageAvro.Builder newBuilder(com.fibanez.kafka.avro.model.MessageAvro other) {
    return new com.fibanez.kafka.avro.model.MessageAvro.Builder(other);
  }

  /**
   * RecordBuilder for MessageAvro instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<MessageAvro>
    implements org.apache.avro.data.RecordBuilder<MessageAvro> {

    private long id;
    private java.lang.CharSequence message;
    private long timestamp;
    private java.lang.CharSequence error;
    private java.lang.Long errorCode;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.fibanez.kafka.avro.model.MessageAvro.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.message)) {
        this.message = data().deepCopy(fields()[1].schema(), other.message);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.timestamp)) {
        this.timestamp = data().deepCopy(fields()[2].schema(), other.timestamp);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.error)) {
        this.error = data().deepCopy(fields()[3].schema(), other.error);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.errorCode)) {
        this.errorCode = data().deepCopy(fields()[4].schema(), other.errorCode);
        fieldSetFlags()[4] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing MessageAvro instance
     * @param other The existing instance to copy.
     */
    private Builder(com.fibanez.kafka.avro.model.MessageAvro other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.message)) {
        this.message = data().deepCopy(fields()[1].schema(), other.message);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.timestamp)) {
        this.timestamp = data().deepCopy(fields()[2].schema(), other.timestamp);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.error)) {
        this.error = data().deepCopy(fields()[3].schema(), other.error);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.errorCode)) {
        this.errorCode = data().deepCopy(fields()[4].schema(), other.errorCode);
        fieldSetFlags()[4] = true;
      }
    }

    /**
      * Gets the value of the 'id' field.
      * @return The value.
      */
    public java.lang.Long getId() {
      return id;
    }

    /**
      * Sets the value of the 'id' field.
      * @param value The value of 'id'.
      * @return This builder.
      */
    public com.fibanez.kafka.avro.model.MessageAvro.Builder setId(long value) {
      validate(fields()[0], value);
      this.id = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'id' field has been set.
      * @return True if the 'id' field has been set, false otherwise.
      */
    public boolean hasId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'id' field.
      * @return This builder.
      */
    public com.fibanez.kafka.avro.model.MessageAvro.Builder clearId() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'message' field.
      * @return The value.
      */
    public java.lang.CharSequence getMessage() {
      return message;
    }

    /**
      * Sets the value of the 'message' field.
      * @param value The value of 'message'.
      * @return This builder.
      */
    public com.fibanez.kafka.avro.model.MessageAvro.Builder setMessage(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.message = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'message' field has been set.
      * @return True if the 'message' field has been set, false otherwise.
      */
    public boolean hasMessage() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'message' field.
      * @return This builder.
      */
    public com.fibanez.kafka.avro.model.MessageAvro.Builder clearMessage() {
      message = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'timestamp' field.
      * @return The value.
      */
    public java.lang.Long getTimestamp() {
      return timestamp;
    }

    /**
      * Sets the value of the 'timestamp' field.
      * @param value The value of 'timestamp'.
      * @return This builder.
      */
    public com.fibanez.kafka.avro.model.MessageAvro.Builder setTimestamp(long value) {
      validate(fields()[2], value);
      this.timestamp = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'timestamp' field has been set.
      * @return True if the 'timestamp' field has been set, false otherwise.
      */
    public boolean hasTimestamp() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'timestamp' field.
      * @return This builder.
      */
    public com.fibanez.kafka.avro.model.MessageAvro.Builder clearTimestamp() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'error' field.
      * @return The value.
      */
    public java.lang.CharSequence getError() {
      return error;
    }

    /**
      * Sets the value of the 'error' field.
      * @param value The value of 'error'.
      * @return This builder.
      */
    public com.fibanez.kafka.avro.model.MessageAvro.Builder setError(java.lang.CharSequence value) {
      validate(fields()[3], value);
      this.error = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'error' field has been set.
      * @return True if the 'error' field has been set, false otherwise.
      */
    public boolean hasError() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'error' field.
      * @return This builder.
      */
    public com.fibanez.kafka.avro.model.MessageAvro.Builder clearError() {
      error = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'errorCode' field.
      * @return The value.
      */
    public java.lang.Long getErrorCode() {
      return errorCode;
    }

    /**
      * Sets the value of the 'errorCode' field.
      * @param value The value of 'errorCode'.
      * @return This builder.
      */
    public com.fibanez.kafka.avro.model.MessageAvro.Builder setErrorCode(java.lang.Long value) {
      validate(fields()[4], value);
      this.errorCode = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'errorCode' field has been set.
      * @return True if the 'errorCode' field has been set, false otherwise.
      */
    public boolean hasErrorCode() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'errorCode' field.
      * @return This builder.
      */
    public com.fibanez.kafka.avro.model.MessageAvro.Builder clearErrorCode() {
      errorCode = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    @Override
    public MessageAvro build() {
      try {
        MessageAvro record = new MessageAvro();
        record.id = fieldSetFlags()[0] ? this.id : (java.lang.Long) defaultValue(fields()[0]);
        record.message = fieldSetFlags()[1] ? this.message : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.timestamp = fieldSetFlags()[2] ? this.timestamp : (java.lang.Long) defaultValue(fields()[2]);
        record.error = fieldSetFlags()[3] ? this.error : (java.lang.CharSequence) defaultValue(fields()[3]);
        record.errorCode = fieldSetFlags()[4] ? this.errorCode : (java.lang.Long) defaultValue(fields()[4]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  private static final org.apache.avro.io.DatumWriter
    WRITER$ = new org.apache.avro.specific.SpecificDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  private static final org.apache.avro.io.DatumReader
    READER$ = new org.apache.avro.specific.SpecificDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}