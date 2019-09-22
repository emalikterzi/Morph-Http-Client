package com.emt.morph.meta;

public class ImmutableParameterMeta {

   private final MetaType metaType;
   private final int index;
   private final Class<?> typeClass;
   private final String value;
   private final String defaultValue;

   public ImmutableParameterMeta(MetaType metaTyoe, int index, Class<?> typeClass) {
      this(metaTyoe, index, typeClass, null, null);
   }

   public ImmutableParameterMeta(MetaType metaTyoe, int index, Class<?> typeClass, String value, String defaultValue) {
      this.metaType = metaTyoe;
      this.index = index;
      this.typeClass = typeClass;
      this.value = value;
      this.defaultValue = defaultValue;
   }

   public MetaType getMetaType() {
      return metaType;
   }

   public int getIndex() {
      return index;
   }

   public Class<?> getTypeClass() {
      return typeClass;
   }

   public String getValue() {
      return value;
   }

   public String getDefaultValue() {
      return defaultValue;
   }

   public enum MetaType {
      BODY, QUERY_PARAM, PATH_PARAM
   }
}
