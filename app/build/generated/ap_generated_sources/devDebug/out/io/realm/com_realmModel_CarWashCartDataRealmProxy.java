package io.realm;


import android.annotation.TargetApi;
import android.os.Build;
import android.util.JsonReader;
import android.util.JsonToken;
import io.realm.ImportFlag;
import io.realm.ProxyUtils;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnInfo;
import io.realm.internal.OsList;
import io.realm.internal.OsObject;
import io.realm.internal.OsObjectSchemaInfo;
import io.realm.internal.OsSchemaInfo;
import io.realm.internal.Property;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.Row;
import io.realm.internal.Table;
import io.realm.internal.android.JsonUtils;
import io.realm.internal.objectstore.OsObjectBuilder;
import io.realm.log.RealmLog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("all")
public class com_realmModel_CarWashCartDataRealmProxy extends com.realmModel.CarWashCartData
    implements RealmObjectProxy, com_realmModel_CarWashCartDataRealmProxyInterface {

    static final class CarWashCartDataColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long itemCountIndex;
        long driverIdIndex;
        long SpecialInstructionIndex;
        long finalTotalIndex;
        long CategoryListItemIndex;
        long vSymbolIndex;

        CarWashCartDataColumnInfo(OsSchemaInfo schemaInfo) {
            super(6);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("CarWashCartData");
            this.itemCountIndex = addColumnDetails("itemCount", "itemCount", objectSchemaInfo);
            this.driverIdIndex = addColumnDetails("driverId", "driverId", objectSchemaInfo);
            this.SpecialInstructionIndex = addColumnDetails("SpecialInstruction", "SpecialInstruction", objectSchemaInfo);
            this.finalTotalIndex = addColumnDetails("finalTotal", "finalTotal", objectSchemaInfo);
            this.CategoryListItemIndex = addColumnDetails("CategoryListItem", "CategoryListItem", objectSchemaInfo);
            this.vSymbolIndex = addColumnDetails("vSymbol", "vSymbol", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        CarWashCartDataColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new CarWashCartDataColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final CarWashCartDataColumnInfo src = (CarWashCartDataColumnInfo) rawSrc;
            final CarWashCartDataColumnInfo dst = (CarWashCartDataColumnInfo) rawDst;
            dst.itemCountIndex = src.itemCountIndex;
            dst.driverIdIndex = src.driverIdIndex;
            dst.SpecialInstructionIndex = src.SpecialInstructionIndex;
            dst.finalTotalIndex = src.finalTotalIndex;
            dst.CategoryListItemIndex = src.CategoryListItemIndex;
            dst.vSymbolIndex = src.vSymbolIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private CarWashCartDataColumnInfo columnInfo;
    private ProxyState<com.realmModel.CarWashCartData> proxyState;

    com_realmModel_CarWashCartDataRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (CarWashCartDataColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.realmModel.CarWashCartData>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$itemCount() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.itemCountIndex);
    }

    @Override
    public void realmSet$itemCount(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.itemCountIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.itemCountIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.itemCountIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.itemCountIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$driverId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.driverIdIndex);
    }

    @Override
    public void realmSet$driverId(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.driverIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.driverIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.driverIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.driverIdIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$SpecialInstruction() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.SpecialInstructionIndex);
    }

    @Override
    public void realmSet$SpecialInstruction(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.SpecialInstructionIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.SpecialInstructionIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.SpecialInstructionIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.SpecialInstructionIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$finalTotal() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.finalTotalIndex);
    }

    @Override
    public void realmSet$finalTotal(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.finalTotalIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.finalTotalIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.finalTotalIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.finalTotalIndex, value);
    }

    @Override
    public com.adapter.files.CategoryListItem realmGet$CategoryListItem() {
        proxyState.getRealm$realm().checkIfValid();
        if (proxyState.getRow$realm().isNullLink(columnInfo.CategoryListItemIndex)) {
            return null;
        }
        return proxyState.getRealm$realm().get(com.adapter.files.CategoryListItem.class, proxyState.getRow$realm().getLink(columnInfo.CategoryListItemIndex), false, Collections.<String>emptyList());
    }

    @Override
    public void realmSet$CategoryListItem(com.adapter.files.CategoryListItem value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            if (proxyState.getExcludeFields$realm().contains("CategoryListItem")) {
                return;
            }
            if (value != null && !RealmObject.isManaged(value)) {
                value = ((Realm) proxyState.getRealm$realm()).copyToRealm(value);
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                // Table#nullifyLink() does not support default value. Just using Row.
                row.nullifyLink(columnInfo.CategoryListItemIndex);
                return;
            }
            proxyState.checkValidObject(value);
            row.getTable().setLink(columnInfo.CategoryListItemIndex, row.getIndex(), ((RealmObjectProxy) value).realmGet$proxyState().getRow$realm().getIndex(), true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().nullifyLink(columnInfo.CategoryListItemIndex);
            return;
        }
        proxyState.checkValidObject(value);
        proxyState.getRow$realm().setLink(columnInfo.CategoryListItemIndex, ((RealmObjectProxy) value).realmGet$proxyState().getRow$realm().getIndex());
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$vSymbol() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.vSymbolIndex);
    }

    @Override
    public void realmSet$vSymbol(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.vSymbolIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.vSymbolIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.vSymbolIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.vSymbolIndex, value);
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("CarWashCartData", 6, 0);
        builder.addPersistedProperty("itemCount", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("driverId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("SpecialInstruction", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("finalTotal", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedLinkProperty("CategoryListItem", RealmFieldType.OBJECT, "CategoryListItem");
        builder.addPersistedProperty("vSymbol", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static CarWashCartDataColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new CarWashCartDataColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "CarWashCartData";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "CarWashCartData";
    }

    @SuppressWarnings("cast")
    public static com.realmModel.CarWashCartData createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = new ArrayList<String>(1);
        if (json.has("CategoryListItem")) {
            excludeFields.add("CategoryListItem");
        }
        com.realmModel.CarWashCartData obj = realm.createObjectInternal(com.realmModel.CarWashCartData.class, true, excludeFields);

        final com_realmModel_CarWashCartDataRealmProxyInterface objProxy = (com_realmModel_CarWashCartDataRealmProxyInterface) obj;
        if (json.has("itemCount")) {
            if (json.isNull("itemCount")) {
                objProxy.realmSet$itemCount(null);
            } else {
                objProxy.realmSet$itemCount((String) json.getString("itemCount"));
            }
        }
        if (json.has("driverId")) {
            if (json.isNull("driverId")) {
                objProxy.realmSet$driverId(null);
            } else {
                objProxy.realmSet$driverId((String) json.getString("driverId"));
            }
        }
        if (json.has("SpecialInstruction")) {
            if (json.isNull("SpecialInstruction")) {
                objProxy.realmSet$SpecialInstruction(null);
            } else {
                objProxy.realmSet$SpecialInstruction((String) json.getString("SpecialInstruction"));
            }
        }
        if (json.has("finalTotal")) {
            if (json.isNull("finalTotal")) {
                objProxy.realmSet$finalTotal(null);
            } else {
                objProxy.realmSet$finalTotal((String) json.getString("finalTotal"));
            }
        }
        if (json.has("CategoryListItem")) {
            if (json.isNull("CategoryListItem")) {
                objProxy.realmSet$CategoryListItem(null);
            } else {
                com.adapter.files.CategoryListItem CategoryListItemObj = com_adapter_files_CategoryListItemRealmProxy.createOrUpdateUsingJsonObject(realm, json.getJSONObject("CategoryListItem"), update);
                objProxy.realmSet$CategoryListItem(CategoryListItemObj);
            }
        }
        if (json.has("vSymbol")) {
            if (json.isNull("vSymbol")) {
                objProxy.realmSet$vSymbol(null);
            } else {
                objProxy.realmSet$vSymbol((String) json.getString("vSymbol"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.realmModel.CarWashCartData createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        final com.realmModel.CarWashCartData obj = new com.realmModel.CarWashCartData();
        final com_realmModel_CarWashCartDataRealmProxyInterface objProxy = (com_realmModel_CarWashCartDataRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("itemCount")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$itemCount((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$itemCount(null);
                }
            } else if (name.equals("driverId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$driverId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$driverId(null);
                }
            } else if (name.equals("SpecialInstruction")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$SpecialInstruction((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$SpecialInstruction(null);
                }
            } else if (name.equals("finalTotal")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$finalTotal((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$finalTotal(null);
                }
            } else if (name.equals("CategoryListItem")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    objProxy.realmSet$CategoryListItem(null);
                } else {
                    com.adapter.files.CategoryListItem CategoryListItemObj = com_adapter_files_CategoryListItemRealmProxy.createUsingJsonStream(realm, reader);
                    objProxy.realmSet$CategoryListItem(CategoryListItemObj);
                }
            } else if (name.equals("vSymbol")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$vSymbol((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$vSymbol(null);
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return realm.copyToRealm(obj);
    }

    private static com_realmModel_CarWashCartDataRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.realmModel.CarWashCartData.class), false, Collections.<String>emptyList());
        io.realm.com_realmModel_CarWashCartDataRealmProxy obj = new io.realm.com_realmModel_CarWashCartDataRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.realmModel.CarWashCartData copyOrUpdate(Realm realm, CarWashCartDataColumnInfo columnInfo, com.realmModel.CarWashCartData object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null) {
            final BaseRealm otherRealm = ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm();
            if (otherRealm.threadId != realm.threadId) {
                throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
            }
            if (otherRealm.getPath().equals(realm.getPath())) {
                return object;
            }
        }
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        RealmObjectProxy cachedRealmObject = cache.get(object);
        if (cachedRealmObject != null) {
            return (com.realmModel.CarWashCartData) cachedRealmObject;
        }

        return copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.realmModel.CarWashCartData copy(Realm realm, CarWashCartDataColumnInfo columnInfo, com.realmModel.CarWashCartData newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.realmModel.CarWashCartData) cachedRealmObject;
        }

        com_realmModel_CarWashCartDataRealmProxyInterface realmObjectSource = (com_realmModel_CarWashCartDataRealmProxyInterface) newObject;

        Table table = realm.getTable(com.realmModel.CarWashCartData.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.itemCountIndex, realmObjectSource.realmGet$itemCount());
        builder.addString(columnInfo.driverIdIndex, realmObjectSource.realmGet$driverId());
        builder.addString(columnInfo.SpecialInstructionIndex, realmObjectSource.realmGet$SpecialInstruction());
        builder.addString(columnInfo.finalTotalIndex, realmObjectSource.realmGet$finalTotal());
        builder.addString(columnInfo.vSymbolIndex, realmObjectSource.realmGet$vSymbol());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_realmModel_CarWashCartDataRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        // Finally add all fields that reference other Realm Objects, either directly or through a list
        com.adapter.files.CategoryListItem CategoryListItemObj = realmObjectSource.realmGet$CategoryListItem();
        if (CategoryListItemObj == null) {
            realmObjectCopy.realmSet$CategoryListItem(null);
        } else {
            com.adapter.files.CategoryListItem cacheCategoryListItem = (com.adapter.files.CategoryListItem) cache.get(CategoryListItemObj);
            if (cacheCategoryListItem != null) {
                realmObjectCopy.realmSet$CategoryListItem(cacheCategoryListItem);
            } else {
                realmObjectCopy.realmSet$CategoryListItem(com_adapter_files_CategoryListItemRealmProxy.copyOrUpdate(realm, (com_adapter_files_CategoryListItemRealmProxy.CategoryListItemColumnInfo) realm.getSchema().getColumnInfo(com.adapter.files.CategoryListItem.class), CategoryListItemObj, update, cache, flags));
            }
        }

        return realmObjectCopy;
    }

    public static long insert(Realm realm, com.realmModel.CarWashCartData object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.realmModel.CarWashCartData.class);
        long tableNativePtr = table.getNativePtr();
        CarWashCartDataColumnInfo columnInfo = (CarWashCartDataColumnInfo) realm.getSchema().getColumnInfo(com.realmModel.CarWashCartData.class);
        long rowIndex = OsObject.createRow(table);
        cache.put(object, rowIndex);
        String realmGet$itemCount = ((com_realmModel_CarWashCartDataRealmProxyInterface) object).realmGet$itemCount();
        if (realmGet$itemCount != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.itemCountIndex, rowIndex, realmGet$itemCount, false);
        }
        String realmGet$driverId = ((com_realmModel_CarWashCartDataRealmProxyInterface) object).realmGet$driverId();
        if (realmGet$driverId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.driverIdIndex, rowIndex, realmGet$driverId, false);
        }
        String realmGet$SpecialInstruction = ((com_realmModel_CarWashCartDataRealmProxyInterface) object).realmGet$SpecialInstruction();
        if (realmGet$SpecialInstruction != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.SpecialInstructionIndex, rowIndex, realmGet$SpecialInstruction, false);
        }
        String realmGet$finalTotal = ((com_realmModel_CarWashCartDataRealmProxyInterface) object).realmGet$finalTotal();
        if (realmGet$finalTotal != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.finalTotalIndex, rowIndex, realmGet$finalTotal, false);
        }

        com.adapter.files.CategoryListItem CategoryListItemObj = ((com_realmModel_CarWashCartDataRealmProxyInterface) object).realmGet$CategoryListItem();
        if (CategoryListItemObj != null) {
            Long cacheCategoryListItem = cache.get(CategoryListItemObj);
            if (cacheCategoryListItem == null) {
                cacheCategoryListItem = com_adapter_files_CategoryListItemRealmProxy.insert(realm, CategoryListItemObj, cache);
            }
            Table.nativeSetLink(tableNativePtr, columnInfo.CategoryListItemIndex, rowIndex, cacheCategoryListItem, false);
        }
        String realmGet$vSymbol = ((com_realmModel_CarWashCartDataRealmProxyInterface) object).realmGet$vSymbol();
        if (realmGet$vSymbol != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.vSymbolIndex, rowIndex, realmGet$vSymbol, false);
        }
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.realmModel.CarWashCartData.class);
        long tableNativePtr = table.getNativePtr();
        CarWashCartDataColumnInfo columnInfo = (CarWashCartDataColumnInfo) realm.getSchema().getColumnInfo(com.realmModel.CarWashCartData.class);
        com.realmModel.CarWashCartData object = null;
        while (objects.hasNext()) {
            object = (com.realmModel.CarWashCartData) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = OsObject.createRow(table);
            cache.put(object, rowIndex);
            String realmGet$itemCount = ((com_realmModel_CarWashCartDataRealmProxyInterface) object).realmGet$itemCount();
            if (realmGet$itemCount != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.itemCountIndex, rowIndex, realmGet$itemCount, false);
            }
            String realmGet$driverId = ((com_realmModel_CarWashCartDataRealmProxyInterface) object).realmGet$driverId();
            if (realmGet$driverId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.driverIdIndex, rowIndex, realmGet$driverId, false);
            }
            String realmGet$SpecialInstruction = ((com_realmModel_CarWashCartDataRealmProxyInterface) object).realmGet$SpecialInstruction();
            if (realmGet$SpecialInstruction != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.SpecialInstructionIndex, rowIndex, realmGet$SpecialInstruction, false);
            }
            String realmGet$finalTotal = ((com_realmModel_CarWashCartDataRealmProxyInterface) object).realmGet$finalTotal();
            if (realmGet$finalTotal != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.finalTotalIndex, rowIndex, realmGet$finalTotal, false);
            }

            com.adapter.files.CategoryListItem CategoryListItemObj = ((com_realmModel_CarWashCartDataRealmProxyInterface) object).realmGet$CategoryListItem();
            if (CategoryListItemObj != null) {
                Long cacheCategoryListItem = cache.get(CategoryListItemObj);
                if (cacheCategoryListItem == null) {
                    cacheCategoryListItem = com_adapter_files_CategoryListItemRealmProxy.insert(realm, CategoryListItemObj, cache);
                }
                table.setLink(columnInfo.CategoryListItemIndex, rowIndex, cacheCategoryListItem, false);
            }
            String realmGet$vSymbol = ((com_realmModel_CarWashCartDataRealmProxyInterface) object).realmGet$vSymbol();
            if (realmGet$vSymbol != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.vSymbolIndex, rowIndex, realmGet$vSymbol, false);
            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.realmModel.CarWashCartData object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.realmModel.CarWashCartData.class);
        long tableNativePtr = table.getNativePtr();
        CarWashCartDataColumnInfo columnInfo = (CarWashCartDataColumnInfo) realm.getSchema().getColumnInfo(com.realmModel.CarWashCartData.class);
        long rowIndex = OsObject.createRow(table);
        cache.put(object, rowIndex);
        String realmGet$itemCount = ((com_realmModel_CarWashCartDataRealmProxyInterface) object).realmGet$itemCount();
        if (realmGet$itemCount != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.itemCountIndex, rowIndex, realmGet$itemCount, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.itemCountIndex, rowIndex, false);
        }
        String realmGet$driverId = ((com_realmModel_CarWashCartDataRealmProxyInterface) object).realmGet$driverId();
        if (realmGet$driverId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.driverIdIndex, rowIndex, realmGet$driverId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.driverIdIndex, rowIndex, false);
        }
        String realmGet$SpecialInstruction = ((com_realmModel_CarWashCartDataRealmProxyInterface) object).realmGet$SpecialInstruction();
        if (realmGet$SpecialInstruction != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.SpecialInstructionIndex, rowIndex, realmGet$SpecialInstruction, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.SpecialInstructionIndex, rowIndex, false);
        }
        String realmGet$finalTotal = ((com_realmModel_CarWashCartDataRealmProxyInterface) object).realmGet$finalTotal();
        if (realmGet$finalTotal != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.finalTotalIndex, rowIndex, realmGet$finalTotal, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.finalTotalIndex, rowIndex, false);
        }

        com.adapter.files.CategoryListItem CategoryListItemObj = ((com_realmModel_CarWashCartDataRealmProxyInterface) object).realmGet$CategoryListItem();
        if (CategoryListItemObj != null) {
            Long cacheCategoryListItem = cache.get(CategoryListItemObj);
            if (cacheCategoryListItem == null) {
                cacheCategoryListItem = com_adapter_files_CategoryListItemRealmProxy.insertOrUpdate(realm, CategoryListItemObj, cache);
            }
            Table.nativeSetLink(tableNativePtr, columnInfo.CategoryListItemIndex, rowIndex, cacheCategoryListItem, false);
        } else {
            Table.nativeNullifyLink(tableNativePtr, columnInfo.CategoryListItemIndex, rowIndex);
        }
        String realmGet$vSymbol = ((com_realmModel_CarWashCartDataRealmProxyInterface) object).realmGet$vSymbol();
        if (realmGet$vSymbol != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.vSymbolIndex, rowIndex, realmGet$vSymbol, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.vSymbolIndex, rowIndex, false);
        }
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.realmModel.CarWashCartData.class);
        long tableNativePtr = table.getNativePtr();
        CarWashCartDataColumnInfo columnInfo = (CarWashCartDataColumnInfo) realm.getSchema().getColumnInfo(com.realmModel.CarWashCartData.class);
        com.realmModel.CarWashCartData object = null;
        while (objects.hasNext()) {
            object = (com.realmModel.CarWashCartData) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = OsObject.createRow(table);
            cache.put(object, rowIndex);
            String realmGet$itemCount = ((com_realmModel_CarWashCartDataRealmProxyInterface) object).realmGet$itemCount();
            if (realmGet$itemCount != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.itemCountIndex, rowIndex, realmGet$itemCount, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.itemCountIndex, rowIndex, false);
            }
            String realmGet$driverId = ((com_realmModel_CarWashCartDataRealmProxyInterface) object).realmGet$driverId();
            if (realmGet$driverId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.driverIdIndex, rowIndex, realmGet$driverId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.driverIdIndex, rowIndex, false);
            }
            String realmGet$SpecialInstruction = ((com_realmModel_CarWashCartDataRealmProxyInterface) object).realmGet$SpecialInstruction();
            if (realmGet$SpecialInstruction != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.SpecialInstructionIndex, rowIndex, realmGet$SpecialInstruction, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.SpecialInstructionIndex, rowIndex, false);
            }
            String realmGet$finalTotal = ((com_realmModel_CarWashCartDataRealmProxyInterface) object).realmGet$finalTotal();
            if (realmGet$finalTotal != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.finalTotalIndex, rowIndex, realmGet$finalTotal, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.finalTotalIndex, rowIndex, false);
            }

            com.adapter.files.CategoryListItem CategoryListItemObj = ((com_realmModel_CarWashCartDataRealmProxyInterface) object).realmGet$CategoryListItem();
            if (CategoryListItemObj != null) {
                Long cacheCategoryListItem = cache.get(CategoryListItemObj);
                if (cacheCategoryListItem == null) {
                    cacheCategoryListItem = com_adapter_files_CategoryListItemRealmProxy.insertOrUpdate(realm, CategoryListItemObj, cache);
                }
                Table.nativeSetLink(tableNativePtr, columnInfo.CategoryListItemIndex, rowIndex, cacheCategoryListItem, false);
            } else {
                Table.nativeNullifyLink(tableNativePtr, columnInfo.CategoryListItemIndex, rowIndex);
            }
            String realmGet$vSymbol = ((com_realmModel_CarWashCartDataRealmProxyInterface) object).realmGet$vSymbol();
            if (realmGet$vSymbol != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.vSymbolIndex, rowIndex, realmGet$vSymbol, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.vSymbolIndex, rowIndex, false);
            }
        }
    }

    public static com.realmModel.CarWashCartData createDetachedCopy(com.realmModel.CarWashCartData realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.realmModel.CarWashCartData unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.realmModel.CarWashCartData();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.realmModel.CarWashCartData) cachedObject.object;
            }
            unmanagedObject = (com.realmModel.CarWashCartData) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_realmModel_CarWashCartDataRealmProxyInterface unmanagedCopy = (com_realmModel_CarWashCartDataRealmProxyInterface) unmanagedObject;
        com_realmModel_CarWashCartDataRealmProxyInterface realmSource = (com_realmModel_CarWashCartDataRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$itemCount(realmSource.realmGet$itemCount());
        unmanagedCopy.realmSet$driverId(realmSource.realmGet$driverId());
        unmanagedCopy.realmSet$SpecialInstruction(realmSource.realmGet$SpecialInstruction());
        unmanagedCopy.realmSet$finalTotal(realmSource.realmGet$finalTotal());

        // Deep copy of CategoryListItem
        unmanagedCopy.realmSet$CategoryListItem(com_adapter_files_CategoryListItemRealmProxy.createDetachedCopy(realmSource.realmGet$CategoryListItem(), currentDepth + 1, maxDepth, cache));
        unmanagedCopy.realmSet$vSymbol(realmSource.realmGet$vSymbol());

        return unmanagedObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("CarWashCartData = proxy[");
        stringBuilder.append("{itemCount:");
        stringBuilder.append(realmGet$itemCount() != null ? realmGet$itemCount() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{driverId:");
        stringBuilder.append(realmGet$driverId() != null ? realmGet$driverId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{SpecialInstruction:");
        stringBuilder.append(realmGet$SpecialInstruction() != null ? realmGet$SpecialInstruction() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{finalTotal:");
        stringBuilder.append(realmGet$finalTotal() != null ? realmGet$finalTotal() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{CategoryListItem:");
        stringBuilder.append(realmGet$CategoryListItem() != null ? "CategoryListItem" : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{vSymbol:");
        stringBuilder.append(realmGet$vSymbol() != null ? realmGet$vSymbol() : "null");
        stringBuilder.append("}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public ProxyState<?> realmGet$proxyState() {
        return proxyState;
    }

    @Override
    public int hashCode() {
        String realmName = proxyState.getRealm$realm().getPath();
        String tableName = proxyState.getRow$realm().getTable().getName();
        long rowIndex = proxyState.getRow$realm().getIndex();

        int result = 17;
        result = 31 * result + ((realmName != null) ? realmName.hashCode() : 0);
        result = 31 * result + ((tableName != null) ? tableName.hashCode() : 0);
        result = 31 * result + (int) (rowIndex ^ (rowIndex >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        com_realmModel_CarWashCartDataRealmProxy aCarWashCartData = (com_realmModel_CarWashCartDataRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aCarWashCartData.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aCarWashCartData.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aCarWashCartData.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
