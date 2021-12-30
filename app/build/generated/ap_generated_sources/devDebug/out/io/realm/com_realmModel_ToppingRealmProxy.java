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
public class com_realmModel_ToppingRealmProxy extends com.realmModel.Topping
    implements RealmObjectProxy, com_realmModel_ToppingRealmProxyInterface {

    static final class ToppingColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long iOptionIdIndex;
        long vOptionNameIndex;
        long fPriceIndex;
        long fUserPriceIndex;
        long fUserPriceWithSymbolIndex;
        long iMenuItemIdIndex;
        long iFoodMenuIdIndex;

        ToppingColumnInfo(OsSchemaInfo schemaInfo) {
            super(7);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("Topping");
            this.iOptionIdIndex = addColumnDetails("iOptionId", "iOptionId", objectSchemaInfo);
            this.vOptionNameIndex = addColumnDetails("vOptionName", "vOptionName", objectSchemaInfo);
            this.fPriceIndex = addColumnDetails("fPrice", "fPrice", objectSchemaInfo);
            this.fUserPriceIndex = addColumnDetails("fUserPrice", "fUserPrice", objectSchemaInfo);
            this.fUserPriceWithSymbolIndex = addColumnDetails("fUserPriceWithSymbol", "fUserPriceWithSymbol", objectSchemaInfo);
            this.iMenuItemIdIndex = addColumnDetails("iMenuItemId", "iMenuItemId", objectSchemaInfo);
            this.iFoodMenuIdIndex = addColumnDetails("iFoodMenuId", "iFoodMenuId", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        ToppingColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new ToppingColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final ToppingColumnInfo src = (ToppingColumnInfo) rawSrc;
            final ToppingColumnInfo dst = (ToppingColumnInfo) rawDst;
            dst.iOptionIdIndex = src.iOptionIdIndex;
            dst.vOptionNameIndex = src.vOptionNameIndex;
            dst.fPriceIndex = src.fPriceIndex;
            dst.fUserPriceIndex = src.fUserPriceIndex;
            dst.fUserPriceWithSymbolIndex = src.fUserPriceWithSymbolIndex;
            dst.iMenuItemIdIndex = src.iMenuItemIdIndex;
            dst.iFoodMenuIdIndex = src.iFoodMenuIdIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private ToppingColumnInfo columnInfo;
    private ProxyState<com.realmModel.Topping> proxyState;

    com_realmModel_ToppingRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (ToppingColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.realmModel.Topping>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$iOptionId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.iOptionIdIndex);
    }

    @Override
    public void realmSet$iOptionId(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.iOptionIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.iOptionIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.iOptionIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.iOptionIdIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$vOptionName() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.vOptionNameIndex);
    }

    @Override
    public void realmSet$vOptionName(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.vOptionNameIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.vOptionNameIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.vOptionNameIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.vOptionNameIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$fPrice() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.fPriceIndex);
    }

    @Override
    public void realmSet$fPrice(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.fPriceIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.fPriceIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.fPriceIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.fPriceIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$fUserPrice() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.fUserPriceIndex);
    }

    @Override
    public void realmSet$fUserPrice(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.fUserPriceIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.fUserPriceIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.fUserPriceIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.fUserPriceIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$fUserPriceWithSymbol() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.fUserPriceWithSymbolIndex);
    }

    @Override
    public void realmSet$fUserPriceWithSymbol(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.fUserPriceWithSymbolIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.fUserPriceWithSymbolIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.fUserPriceWithSymbolIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.fUserPriceWithSymbolIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$iMenuItemId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.iMenuItemIdIndex);
    }

    @Override
    public void realmSet$iMenuItemId(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.iMenuItemIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.iMenuItemIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.iMenuItemIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.iMenuItemIdIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$iFoodMenuId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.iFoodMenuIdIndex);
    }

    @Override
    public void realmSet$iFoodMenuId(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.iFoodMenuIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.iFoodMenuIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.iFoodMenuIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.iFoodMenuIdIndex, value);
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("Topping", 7, 0);
        builder.addPersistedProperty("iOptionId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("vOptionName", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("fPrice", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("fUserPrice", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("fUserPriceWithSymbol", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("iMenuItemId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("iFoodMenuId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static ToppingColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new ToppingColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "Topping";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "Topping";
    }

    @SuppressWarnings("cast")
    public static com.realmModel.Topping createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        com.realmModel.Topping obj = realm.createObjectInternal(com.realmModel.Topping.class, true, excludeFields);

        final com_realmModel_ToppingRealmProxyInterface objProxy = (com_realmModel_ToppingRealmProxyInterface) obj;
        if (json.has("iOptionId")) {
            if (json.isNull("iOptionId")) {
                objProxy.realmSet$iOptionId(null);
            } else {
                objProxy.realmSet$iOptionId((String) json.getString("iOptionId"));
            }
        }
        if (json.has("vOptionName")) {
            if (json.isNull("vOptionName")) {
                objProxy.realmSet$vOptionName(null);
            } else {
                objProxy.realmSet$vOptionName((String) json.getString("vOptionName"));
            }
        }
        if (json.has("fPrice")) {
            if (json.isNull("fPrice")) {
                objProxy.realmSet$fPrice(null);
            } else {
                objProxy.realmSet$fPrice((String) json.getString("fPrice"));
            }
        }
        if (json.has("fUserPrice")) {
            if (json.isNull("fUserPrice")) {
                objProxy.realmSet$fUserPrice(null);
            } else {
                objProxy.realmSet$fUserPrice((String) json.getString("fUserPrice"));
            }
        }
        if (json.has("fUserPriceWithSymbol")) {
            if (json.isNull("fUserPriceWithSymbol")) {
                objProxy.realmSet$fUserPriceWithSymbol(null);
            } else {
                objProxy.realmSet$fUserPriceWithSymbol((String) json.getString("fUserPriceWithSymbol"));
            }
        }
        if (json.has("iMenuItemId")) {
            if (json.isNull("iMenuItemId")) {
                objProxy.realmSet$iMenuItemId(null);
            } else {
                objProxy.realmSet$iMenuItemId((String) json.getString("iMenuItemId"));
            }
        }
        if (json.has("iFoodMenuId")) {
            if (json.isNull("iFoodMenuId")) {
                objProxy.realmSet$iFoodMenuId(null);
            } else {
                objProxy.realmSet$iFoodMenuId((String) json.getString("iFoodMenuId"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.realmModel.Topping createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        final com.realmModel.Topping obj = new com.realmModel.Topping();
        final com_realmModel_ToppingRealmProxyInterface objProxy = (com_realmModel_ToppingRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("iOptionId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$iOptionId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$iOptionId(null);
                }
            } else if (name.equals("vOptionName")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$vOptionName((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$vOptionName(null);
                }
            } else if (name.equals("fPrice")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$fPrice((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$fPrice(null);
                }
            } else if (name.equals("fUserPrice")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$fUserPrice((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$fUserPrice(null);
                }
            } else if (name.equals("fUserPriceWithSymbol")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$fUserPriceWithSymbol((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$fUserPriceWithSymbol(null);
                }
            } else if (name.equals("iMenuItemId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$iMenuItemId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$iMenuItemId(null);
                }
            } else if (name.equals("iFoodMenuId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$iFoodMenuId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$iFoodMenuId(null);
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return realm.copyToRealm(obj);
    }

    private static com_realmModel_ToppingRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.realmModel.Topping.class), false, Collections.<String>emptyList());
        io.realm.com_realmModel_ToppingRealmProxy obj = new io.realm.com_realmModel_ToppingRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.realmModel.Topping copyOrUpdate(Realm realm, ToppingColumnInfo columnInfo, com.realmModel.Topping object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (com.realmModel.Topping) cachedRealmObject;
        }

        return copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.realmModel.Topping copy(Realm realm, ToppingColumnInfo columnInfo, com.realmModel.Topping newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.realmModel.Topping) cachedRealmObject;
        }

        com_realmModel_ToppingRealmProxyInterface realmObjectSource = (com_realmModel_ToppingRealmProxyInterface) newObject;

        Table table = realm.getTable(com.realmModel.Topping.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.iOptionIdIndex, realmObjectSource.realmGet$iOptionId());
        builder.addString(columnInfo.vOptionNameIndex, realmObjectSource.realmGet$vOptionName());
        builder.addString(columnInfo.fPriceIndex, realmObjectSource.realmGet$fPrice());
        builder.addString(columnInfo.fUserPriceIndex, realmObjectSource.realmGet$fUserPrice());
        builder.addString(columnInfo.fUserPriceWithSymbolIndex, realmObjectSource.realmGet$fUserPriceWithSymbol());
        builder.addString(columnInfo.iMenuItemIdIndex, realmObjectSource.realmGet$iMenuItemId());
        builder.addString(columnInfo.iFoodMenuIdIndex, realmObjectSource.realmGet$iFoodMenuId());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_realmModel_ToppingRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        return realmObjectCopy;
    }

    public static long insert(Realm realm, com.realmModel.Topping object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.realmModel.Topping.class);
        long tableNativePtr = table.getNativePtr();
        ToppingColumnInfo columnInfo = (ToppingColumnInfo) realm.getSchema().getColumnInfo(com.realmModel.Topping.class);
        long rowIndex = OsObject.createRow(table);
        cache.put(object, rowIndex);
        String realmGet$iOptionId = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$iOptionId();
        if (realmGet$iOptionId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.iOptionIdIndex, rowIndex, realmGet$iOptionId, false);
        }
        String realmGet$vOptionName = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$vOptionName();
        if (realmGet$vOptionName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.vOptionNameIndex, rowIndex, realmGet$vOptionName, false);
        }
        String realmGet$fPrice = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$fPrice();
        if (realmGet$fPrice != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fPriceIndex, rowIndex, realmGet$fPrice, false);
        }
        String realmGet$fUserPrice = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$fUserPrice();
        if (realmGet$fUserPrice != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fUserPriceIndex, rowIndex, realmGet$fUserPrice, false);
        }
        String realmGet$fUserPriceWithSymbol = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$fUserPriceWithSymbol();
        if (realmGet$fUserPriceWithSymbol != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fUserPriceWithSymbolIndex, rowIndex, realmGet$fUserPriceWithSymbol, false);
        }
        String realmGet$iMenuItemId = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$iMenuItemId();
        if (realmGet$iMenuItemId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.iMenuItemIdIndex, rowIndex, realmGet$iMenuItemId, false);
        }
        String realmGet$iFoodMenuId = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$iFoodMenuId();
        if (realmGet$iFoodMenuId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.iFoodMenuIdIndex, rowIndex, realmGet$iFoodMenuId, false);
        }
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.realmModel.Topping.class);
        long tableNativePtr = table.getNativePtr();
        ToppingColumnInfo columnInfo = (ToppingColumnInfo) realm.getSchema().getColumnInfo(com.realmModel.Topping.class);
        com.realmModel.Topping object = null;
        while (objects.hasNext()) {
            object = (com.realmModel.Topping) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = OsObject.createRow(table);
            cache.put(object, rowIndex);
            String realmGet$iOptionId = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$iOptionId();
            if (realmGet$iOptionId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.iOptionIdIndex, rowIndex, realmGet$iOptionId, false);
            }
            String realmGet$vOptionName = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$vOptionName();
            if (realmGet$vOptionName != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.vOptionNameIndex, rowIndex, realmGet$vOptionName, false);
            }
            String realmGet$fPrice = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$fPrice();
            if (realmGet$fPrice != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fPriceIndex, rowIndex, realmGet$fPrice, false);
            }
            String realmGet$fUserPrice = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$fUserPrice();
            if (realmGet$fUserPrice != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fUserPriceIndex, rowIndex, realmGet$fUserPrice, false);
            }
            String realmGet$fUserPriceWithSymbol = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$fUserPriceWithSymbol();
            if (realmGet$fUserPriceWithSymbol != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fUserPriceWithSymbolIndex, rowIndex, realmGet$fUserPriceWithSymbol, false);
            }
            String realmGet$iMenuItemId = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$iMenuItemId();
            if (realmGet$iMenuItemId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.iMenuItemIdIndex, rowIndex, realmGet$iMenuItemId, false);
            }
            String realmGet$iFoodMenuId = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$iFoodMenuId();
            if (realmGet$iFoodMenuId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.iFoodMenuIdIndex, rowIndex, realmGet$iFoodMenuId, false);
            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.realmModel.Topping object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.realmModel.Topping.class);
        long tableNativePtr = table.getNativePtr();
        ToppingColumnInfo columnInfo = (ToppingColumnInfo) realm.getSchema().getColumnInfo(com.realmModel.Topping.class);
        long rowIndex = OsObject.createRow(table);
        cache.put(object, rowIndex);
        String realmGet$iOptionId = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$iOptionId();
        if (realmGet$iOptionId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.iOptionIdIndex, rowIndex, realmGet$iOptionId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.iOptionIdIndex, rowIndex, false);
        }
        String realmGet$vOptionName = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$vOptionName();
        if (realmGet$vOptionName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.vOptionNameIndex, rowIndex, realmGet$vOptionName, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.vOptionNameIndex, rowIndex, false);
        }
        String realmGet$fPrice = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$fPrice();
        if (realmGet$fPrice != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fPriceIndex, rowIndex, realmGet$fPrice, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fPriceIndex, rowIndex, false);
        }
        String realmGet$fUserPrice = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$fUserPrice();
        if (realmGet$fUserPrice != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fUserPriceIndex, rowIndex, realmGet$fUserPrice, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fUserPriceIndex, rowIndex, false);
        }
        String realmGet$fUserPriceWithSymbol = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$fUserPriceWithSymbol();
        if (realmGet$fUserPriceWithSymbol != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fUserPriceWithSymbolIndex, rowIndex, realmGet$fUserPriceWithSymbol, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fUserPriceWithSymbolIndex, rowIndex, false);
        }
        String realmGet$iMenuItemId = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$iMenuItemId();
        if (realmGet$iMenuItemId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.iMenuItemIdIndex, rowIndex, realmGet$iMenuItemId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.iMenuItemIdIndex, rowIndex, false);
        }
        String realmGet$iFoodMenuId = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$iFoodMenuId();
        if (realmGet$iFoodMenuId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.iFoodMenuIdIndex, rowIndex, realmGet$iFoodMenuId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.iFoodMenuIdIndex, rowIndex, false);
        }
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.realmModel.Topping.class);
        long tableNativePtr = table.getNativePtr();
        ToppingColumnInfo columnInfo = (ToppingColumnInfo) realm.getSchema().getColumnInfo(com.realmModel.Topping.class);
        com.realmModel.Topping object = null;
        while (objects.hasNext()) {
            object = (com.realmModel.Topping) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = OsObject.createRow(table);
            cache.put(object, rowIndex);
            String realmGet$iOptionId = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$iOptionId();
            if (realmGet$iOptionId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.iOptionIdIndex, rowIndex, realmGet$iOptionId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.iOptionIdIndex, rowIndex, false);
            }
            String realmGet$vOptionName = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$vOptionName();
            if (realmGet$vOptionName != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.vOptionNameIndex, rowIndex, realmGet$vOptionName, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.vOptionNameIndex, rowIndex, false);
            }
            String realmGet$fPrice = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$fPrice();
            if (realmGet$fPrice != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fPriceIndex, rowIndex, realmGet$fPrice, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.fPriceIndex, rowIndex, false);
            }
            String realmGet$fUserPrice = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$fUserPrice();
            if (realmGet$fUserPrice != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fUserPriceIndex, rowIndex, realmGet$fUserPrice, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.fUserPriceIndex, rowIndex, false);
            }
            String realmGet$fUserPriceWithSymbol = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$fUserPriceWithSymbol();
            if (realmGet$fUserPriceWithSymbol != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fUserPriceWithSymbolIndex, rowIndex, realmGet$fUserPriceWithSymbol, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.fUserPriceWithSymbolIndex, rowIndex, false);
            }
            String realmGet$iMenuItemId = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$iMenuItemId();
            if (realmGet$iMenuItemId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.iMenuItemIdIndex, rowIndex, realmGet$iMenuItemId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.iMenuItemIdIndex, rowIndex, false);
            }
            String realmGet$iFoodMenuId = ((com_realmModel_ToppingRealmProxyInterface) object).realmGet$iFoodMenuId();
            if (realmGet$iFoodMenuId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.iFoodMenuIdIndex, rowIndex, realmGet$iFoodMenuId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.iFoodMenuIdIndex, rowIndex, false);
            }
        }
    }

    public static com.realmModel.Topping createDetachedCopy(com.realmModel.Topping realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.realmModel.Topping unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.realmModel.Topping();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.realmModel.Topping) cachedObject.object;
            }
            unmanagedObject = (com.realmModel.Topping) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_realmModel_ToppingRealmProxyInterface unmanagedCopy = (com_realmModel_ToppingRealmProxyInterface) unmanagedObject;
        com_realmModel_ToppingRealmProxyInterface realmSource = (com_realmModel_ToppingRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$iOptionId(realmSource.realmGet$iOptionId());
        unmanagedCopy.realmSet$vOptionName(realmSource.realmGet$vOptionName());
        unmanagedCopy.realmSet$fPrice(realmSource.realmGet$fPrice());
        unmanagedCopy.realmSet$fUserPrice(realmSource.realmGet$fUserPrice());
        unmanagedCopy.realmSet$fUserPriceWithSymbol(realmSource.realmGet$fUserPriceWithSymbol());
        unmanagedCopy.realmSet$iMenuItemId(realmSource.realmGet$iMenuItemId());
        unmanagedCopy.realmSet$iFoodMenuId(realmSource.realmGet$iFoodMenuId());

        return unmanagedObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("Topping = proxy[");
        stringBuilder.append("{iOptionId:");
        stringBuilder.append(realmGet$iOptionId() != null ? realmGet$iOptionId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{vOptionName:");
        stringBuilder.append(realmGet$vOptionName() != null ? realmGet$vOptionName() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{fPrice:");
        stringBuilder.append(realmGet$fPrice() != null ? realmGet$fPrice() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{fUserPrice:");
        stringBuilder.append(realmGet$fUserPrice() != null ? realmGet$fUserPrice() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{fUserPriceWithSymbol:");
        stringBuilder.append(realmGet$fUserPriceWithSymbol() != null ? realmGet$fUserPriceWithSymbol() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{iMenuItemId:");
        stringBuilder.append(realmGet$iMenuItemId() != null ? realmGet$iMenuItemId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{iFoodMenuId:");
        stringBuilder.append(realmGet$iFoodMenuId() != null ? realmGet$iFoodMenuId() : "null");
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
        com_realmModel_ToppingRealmProxy aTopping = (com_realmModel_ToppingRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aTopping.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aTopping.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aTopping.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
