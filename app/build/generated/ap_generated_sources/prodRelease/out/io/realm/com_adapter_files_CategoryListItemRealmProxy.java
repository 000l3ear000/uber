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
public class com_adapter_files_CategoryListItemRealmProxy extends com.adapter.files.CategoryListItem
    implements RealmObjectProxy, com_adapter_files_CategoryListItemRealmProxyInterface {

    static final class CategoryListItemColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long typeIndex;
        long textIndex;
        long sectionPositionIndex;
        long listPositionIndex;
        long CountSubItemsIndex;
        long vTitleIndex;
        long vDescIndex;
        long iVehicleCategoryIdIndex;
        long vCategoryIndex;
        long eFareTypeIndex;
        long eFareValueIndex;
        long fFixedFareIndex;
        long fPricePerHourIndex;
        long fMinHourIndex;
        long iVehicleTypeIdIndex;
        long isAddIndex;

        CategoryListItemColumnInfo(OsSchemaInfo schemaInfo) {
            super(16);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("CategoryListItem");
            this.typeIndex = addColumnDetails("type", "type", objectSchemaInfo);
            this.textIndex = addColumnDetails("text", "text", objectSchemaInfo);
            this.sectionPositionIndex = addColumnDetails("sectionPosition", "sectionPosition", objectSchemaInfo);
            this.listPositionIndex = addColumnDetails("listPosition", "listPosition", objectSchemaInfo);
            this.CountSubItemsIndex = addColumnDetails("CountSubItems", "CountSubItems", objectSchemaInfo);
            this.vTitleIndex = addColumnDetails("vTitle", "vTitle", objectSchemaInfo);
            this.vDescIndex = addColumnDetails("vDesc", "vDesc", objectSchemaInfo);
            this.iVehicleCategoryIdIndex = addColumnDetails("iVehicleCategoryId", "iVehicleCategoryId", objectSchemaInfo);
            this.vCategoryIndex = addColumnDetails("vCategory", "vCategory", objectSchemaInfo);
            this.eFareTypeIndex = addColumnDetails("eFareType", "eFareType", objectSchemaInfo);
            this.eFareValueIndex = addColumnDetails("eFareValue", "eFareValue", objectSchemaInfo);
            this.fFixedFareIndex = addColumnDetails("fFixedFare", "fFixedFare", objectSchemaInfo);
            this.fPricePerHourIndex = addColumnDetails("fPricePerHour", "fPricePerHour", objectSchemaInfo);
            this.fMinHourIndex = addColumnDetails("fMinHour", "fMinHour", objectSchemaInfo);
            this.iVehicleTypeIdIndex = addColumnDetails("iVehicleTypeId", "iVehicleTypeId", objectSchemaInfo);
            this.isAddIndex = addColumnDetails("isAdd", "isAdd", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        CategoryListItemColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new CategoryListItemColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final CategoryListItemColumnInfo src = (CategoryListItemColumnInfo) rawSrc;
            final CategoryListItemColumnInfo dst = (CategoryListItemColumnInfo) rawDst;
            dst.typeIndex = src.typeIndex;
            dst.textIndex = src.textIndex;
            dst.sectionPositionIndex = src.sectionPositionIndex;
            dst.listPositionIndex = src.listPositionIndex;
            dst.CountSubItemsIndex = src.CountSubItemsIndex;
            dst.vTitleIndex = src.vTitleIndex;
            dst.vDescIndex = src.vDescIndex;
            dst.iVehicleCategoryIdIndex = src.iVehicleCategoryIdIndex;
            dst.vCategoryIndex = src.vCategoryIndex;
            dst.eFareTypeIndex = src.eFareTypeIndex;
            dst.eFareValueIndex = src.eFareValueIndex;
            dst.fFixedFareIndex = src.fFixedFareIndex;
            dst.fPricePerHourIndex = src.fPricePerHourIndex;
            dst.fMinHourIndex = src.fMinHourIndex;
            dst.iVehicleTypeIdIndex = src.iVehicleTypeIdIndex;
            dst.isAddIndex = src.isAddIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private CategoryListItemColumnInfo columnInfo;
    private ProxyState<com.adapter.files.CategoryListItem> proxyState;

    com_adapter_files_CategoryListItemRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (CategoryListItemColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.adapter.files.CategoryListItem>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public int realmGet$type() {
        proxyState.getRealm$realm().checkIfValid();
        return (int) proxyState.getRow$realm().getLong(columnInfo.typeIndex);
    }

    @Override
    public void realmSet$type(int value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.typeIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.typeIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$text() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.textIndex);
    }

    @Override
    public void realmSet$text(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.textIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.textIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.textIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.textIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public int realmGet$sectionPosition() {
        proxyState.getRealm$realm().checkIfValid();
        return (int) proxyState.getRow$realm().getLong(columnInfo.sectionPositionIndex);
    }

    @Override
    public void realmSet$sectionPosition(int value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.sectionPositionIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.sectionPositionIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public int realmGet$listPosition() {
        proxyState.getRealm$realm().checkIfValid();
        return (int) proxyState.getRow$realm().getLong(columnInfo.listPositionIndex);
    }

    @Override
    public void realmSet$listPosition(int value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.listPositionIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.listPositionIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public int realmGet$CountSubItems() {
        proxyState.getRealm$realm().checkIfValid();
        return (int) proxyState.getRow$realm().getLong(columnInfo.CountSubItemsIndex);
    }

    @Override
    public void realmSet$CountSubItems(int value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.CountSubItemsIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.CountSubItemsIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$vTitle() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.vTitleIndex);
    }

    @Override
    public void realmSet$vTitle(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.vTitleIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.vTitleIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.vTitleIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.vTitleIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$vDesc() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.vDescIndex);
    }

    @Override
    public void realmSet$vDesc(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.vDescIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.vDescIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.vDescIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.vDescIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$iVehicleCategoryId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.iVehicleCategoryIdIndex);
    }

    @Override
    public void realmSet$iVehicleCategoryId(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.iVehicleCategoryIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.iVehicleCategoryIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.iVehicleCategoryIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.iVehicleCategoryIdIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$vCategory() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.vCategoryIndex);
    }

    @Override
    public void realmSet$vCategory(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.vCategoryIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.vCategoryIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.vCategoryIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.vCategoryIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$eFareType() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.eFareTypeIndex);
    }

    @Override
    public void realmSet$eFareType(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.eFareTypeIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.eFareTypeIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.eFareTypeIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.eFareTypeIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$eFareValue() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.eFareValueIndex);
    }

    @Override
    public void realmSet$eFareValue(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.eFareValueIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.eFareValueIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.eFareValueIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.eFareValueIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$fFixedFare() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.fFixedFareIndex);
    }

    @Override
    public void realmSet$fFixedFare(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.fFixedFareIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.fFixedFareIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.fFixedFareIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.fFixedFareIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$fPricePerHour() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.fPricePerHourIndex);
    }

    @Override
    public void realmSet$fPricePerHour(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.fPricePerHourIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.fPricePerHourIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.fPricePerHourIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.fPricePerHourIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$fMinHour() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.fMinHourIndex);
    }

    @Override
    public void realmSet$fMinHour(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.fMinHourIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.fMinHourIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.fMinHourIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.fMinHourIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$iVehicleTypeId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.iVehicleTypeIdIndex);
    }

    @Override
    public void realmSet$iVehicleTypeId(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.iVehicleTypeIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.iVehicleTypeIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.iVehicleTypeIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.iVehicleTypeIdIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public boolean realmGet$isAdd() {
        proxyState.getRealm$realm().checkIfValid();
        return (boolean) proxyState.getRow$realm().getBoolean(columnInfo.isAddIndex);
    }

    @Override
    public void realmSet$isAdd(boolean value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setBoolean(columnInfo.isAddIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setBoolean(columnInfo.isAddIndex, value);
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("CategoryListItem", 16, 0);
        builder.addPersistedProperty("type", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("text", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("sectionPosition", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("listPosition", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("CountSubItems", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("vTitle", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("vDesc", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("iVehicleCategoryId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("vCategory", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("eFareType", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("eFareValue", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("fFixedFare", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("fPricePerHour", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("fMinHour", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("iVehicleTypeId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("isAdd", RealmFieldType.BOOLEAN, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static CategoryListItemColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new CategoryListItemColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "CategoryListItem";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "CategoryListItem";
    }

    @SuppressWarnings("cast")
    public static com.adapter.files.CategoryListItem createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        com.adapter.files.CategoryListItem obj = realm.createObjectInternal(com.adapter.files.CategoryListItem.class, true, excludeFields);

        final com_adapter_files_CategoryListItemRealmProxyInterface objProxy = (com_adapter_files_CategoryListItemRealmProxyInterface) obj;
        if (json.has("type")) {
            if (json.isNull("type")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'type' to null.");
            } else {
                objProxy.realmSet$type((int) json.getInt("type"));
            }
        }
        if (json.has("text")) {
            if (json.isNull("text")) {
                objProxy.realmSet$text(null);
            } else {
                objProxy.realmSet$text((String) json.getString("text"));
            }
        }
        if (json.has("sectionPosition")) {
            if (json.isNull("sectionPosition")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'sectionPosition' to null.");
            } else {
                objProxy.realmSet$sectionPosition((int) json.getInt("sectionPosition"));
            }
        }
        if (json.has("listPosition")) {
            if (json.isNull("listPosition")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'listPosition' to null.");
            } else {
                objProxy.realmSet$listPosition((int) json.getInt("listPosition"));
            }
        }
        if (json.has("CountSubItems")) {
            if (json.isNull("CountSubItems")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'CountSubItems' to null.");
            } else {
                objProxy.realmSet$CountSubItems((int) json.getInt("CountSubItems"));
            }
        }
        if (json.has("vTitle")) {
            if (json.isNull("vTitle")) {
                objProxy.realmSet$vTitle(null);
            } else {
                objProxy.realmSet$vTitle((String) json.getString("vTitle"));
            }
        }
        if (json.has("vDesc")) {
            if (json.isNull("vDesc")) {
                objProxy.realmSet$vDesc(null);
            } else {
                objProxy.realmSet$vDesc((String) json.getString("vDesc"));
            }
        }
        if (json.has("iVehicleCategoryId")) {
            if (json.isNull("iVehicleCategoryId")) {
                objProxy.realmSet$iVehicleCategoryId(null);
            } else {
                objProxy.realmSet$iVehicleCategoryId((String) json.getString("iVehicleCategoryId"));
            }
        }
        if (json.has("vCategory")) {
            if (json.isNull("vCategory")) {
                objProxy.realmSet$vCategory(null);
            } else {
                objProxy.realmSet$vCategory((String) json.getString("vCategory"));
            }
        }
        if (json.has("eFareType")) {
            if (json.isNull("eFareType")) {
                objProxy.realmSet$eFareType(null);
            } else {
                objProxy.realmSet$eFareType((String) json.getString("eFareType"));
            }
        }
        if (json.has("eFareValue")) {
            if (json.isNull("eFareValue")) {
                objProxy.realmSet$eFareValue(null);
            } else {
                objProxy.realmSet$eFareValue((String) json.getString("eFareValue"));
            }
        }
        if (json.has("fFixedFare")) {
            if (json.isNull("fFixedFare")) {
                objProxy.realmSet$fFixedFare(null);
            } else {
                objProxy.realmSet$fFixedFare((String) json.getString("fFixedFare"));
            }
        }
        if (json.has("fPricePerHour")) {
            if (json.isNull("fPricePerHour")) {
                objProxy.realmSet$fPricePerHour(null);
            } else {
                objProxy.realmSet$fPricePerHour((String) json.getString("fPricePerHour"));
            }
        }
        if (json.has("fMinHour")) {
            if (json.isNull("fMinHour")) {
                objProxy.realmSet$fMinHour(null);
            } else {
                objProxy.realmSet$fMinHour((String) json.getString("fMinHour"));
            }
        }
        if (json.has("iVehicleTypeId")) {
            if (json.isNull("iVehicleTypeId")) {
                objProxy.realmSet$iVehicleTypeId(null);
            } else {
                objProxy.realmSet$iVehicleTypeId((String) json.getString("iVehicleTypeId"));
            }
        }
        if (json.has("isAdd")) {
            if (json.isNull("isAdd")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'isAdd' to null.");
            } else {
                objProxy.realmSet$isAdd((boolean) json.getBoolean("isAdd"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.adapter.files.CategoryListItem createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        final com.adapter.files.CategoryListItem obj = new com.adapter.files.CategoryListItem();
        final com_adapter_files_CategoryListItemRealmProxyInterface objProxy = (com_adapter_files_CategoryListItemRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("type")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$type((int) reader.nextInt());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'type' to null.");
                }
            } else if (name.equals("text")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$text((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$text(null);
                }
            } else if (name.equals("sectionPosition")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$sectionPosition((int) reader.nextInt());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'sectionPosition' to null.");
                }
            } else if (name.equals("listPosition")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$listPosition((int) reader.nextInt());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'listPosition' to null.");
                }
            } else if (name.equals("CountSubItems")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$CountSubItems((int) reader.nextInt());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'CountSubItems' to null.");
                }
            } else if (name.equals("vTitle")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$vTitle((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$vTitle(null);
                }
            } else if (name.equals("vDesc")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$vDesc((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$vDesc(null);
                }
            } else if (name.equals("iVehicleCategoryId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$iVehicleCategoryId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$iVehicleCategoryId(null);
                }
            } else if (name.equals("vCategory")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$vCategory((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$vCategory(null);
                }
            } else if (name.equals("eFareType")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$eFareType((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$eFareType(null);
                }
            } else if (name.equals("eFareValue")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$eFareValue((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$eFareValue(null);
                }
            } else if (name.equals("fFixedFare")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$fFixedFare((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$fFixedFare(null);
                }
            } else if (name.equals("fPricePerHour")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$fPricePerHour((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$fPricePerHour(null);
                }
            } else if (name.equals("fMinHour")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$fMinHour((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$fMinHour(null);
                }
            } else if (name.equals("iVehicleTypeId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$iVehicleTypeId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$iVehicleTypeId(null);
                }
            } else if (name.equals("isAdd")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$isAdd((boolean) reader.nextBoolean());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'isAdd' to null.");
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return realm.copyToRealm(obj);
    }

    private static com_adapter_files_CategoryListItemRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.adapter.files.CategoryListItem.class), false, Collections.<String>emptyList());
        io.realm.com_adapter_files_CategoryListItemRealmProxy obj = new io.realm.com_adapter_files_CategoryListItemRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.adapter.files.CategoryListItem copyOrUpdate(Realm realm, CategoryListItemColumnInfo columnInfo, com.adapter.files.CategoryListItem object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (com.adapter.files.CategoryListItem) cachedRealmObject;
        }

        return copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.adapter.files.CategoryListItem copy(Realm realm, CategoryListItemColumnInfo columnInfo, com.adapter.files.CategoryListItem newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.adapter.files.CategoryListItem) cachedRealmObject;
        }

        com_adapter_files_CategoryListItemRealmProxyInterface realmObjectSource = (com_adapter_files_CategoryListItemRealmProxyInterface) newObject;

        Table table = realm.getTable(com.adapter.files.CategoryListItem.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addInteger(columnInfo.typeIndex, realmObjectSource.realmGet$type());
        builder.addString(columnInfo.textIndex, realmObjectSource.realmGet$text());
        builder.addInteger(columnInfo.sectionPositionIndex, realmObjectSource.realmGet$sectionPosition());
        builder.addInteger(columnInfo.listPositionIndex, realmObjectSource.realmGet$listPosition());
        builder.addInteger(columnInfo.CountSubItemsIndex, realmObjectSource.realmGet$CountSubItems());
        builder.addString(columnInfo.vTitleIndex, realmObjectSource.realmGet$vTitle());
        builder.addString(columnInfo.vDescIndex, realmObjectSource.realmGet$vDesc());
        builder.addString(columnInfo.iVehicleCategoryIdIndex, realmObjectSource.realmGet$iVehicleCategoryId());
        builder.addString(columnInfo.vCategoryIndex, realmObjectSource.realmGet$vCategory());
        builder.addString(columnInfo.eFareTypeIndex, realmObjectSource.realmGet$eFareType());
        builder.addString(columnInfo.eFareValueIndex, realmObjectSource.realmGet$eFareValue());
        builder.addString(columnInfo.fFixedFareIndex, realmObjectSource.realmGet$fFixedFare());
        builder.addString(columnInfo.fPricePerHourIndex, realmObjectSource.realmGet$fPricePerHour());
        builder.addString(columnInfo.fMinHourIndex, realmObjectSource.realmGet$fMinHour());
        builder.addString(columnInfo.iVehicleTypeIdIndex, realmObjectSource.realmGet$iVehicleTypeId());
        builder.addBoolean(columnInfo.isAddIndex, realmObjectSource.realmGet$isAdd());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_adapter_files_CategoryListItemRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        return realmObjectCopy;
    }

    public static long insert(Realm realm, com.adapter.files.CategoryListItem object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.adapter.files.CategoryListItem.class);
        long tableNativePtr = table.getNativePtr();
        CategoryListItemColumnInfo columnInfo = (CategoryListItemColumnInfo) realm.getSchema().getColumnInfo(com.adapter.files.CategoryListItem.class);
        long rowIndex = OsObject.createRow(table);
        cache.put(object, rowIndex);
        Table.nativeSetLong(tableNativePtr, columnInfo.typeIndex, rowIndex, ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$type(), false);
        String realmGet$text = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$text();
        if (realmGet$text != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.textIndex, rowIndex, realmGet$text, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.sectionPositionIndex, rowIndex, ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$sectionPosition(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.listPositionIndex, rowIndex, ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$listPosition(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.CountSubItemsIndex, rowIndex, ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$CountSubItems(), false);
        String realmGet$vTitle = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$vTitle();
        if (realmGet$vTitle != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.vTitleIndex, rowIndex, realmGet$vTitle, false);
        }
        String realmGet$vDesc = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$vDesc();
        if (realmGet$vDesc != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.vDescIndex, rowIndex, realmGet$vDesc, false);
        }
        String realmGet$iVehicleCategoryId = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$iVehicleCategoryId();
        if (realmGet$iVehicleCategoryId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.iVehicleCategoryIdIndex, rowIndex, realmGet$iVehicleCategoryId, false);
        }
        String realmGet$vCategory = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$vCategory();
        if (realmGet$vCategory != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.vCategoryIndex, rowIndex, realmGet$vCategory, false);
        }
        String realmGet$eFareType = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$eFareType();
        if (realmGet$eFareType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.eFareTypeIndex, rowIndex, realmGet$eFareType, false);
        }
        String realmGet$eFareValue = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$eFareValue();
        if (realmGet$eFareValue != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.eFareValueIndex, rowIndex, realmGet$eFareValue, false);
        }
        String realmGet$fFixedFare = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$fFixedFare();
        if (realmGet$fFixedFare != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fFixedFareIndex, rowIndex, realmGet$fFixedFare, false);
        }
        String realmGet$fPricePerHour = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$fPricePerHour();
        if (realmGet$fPricePerHour != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fPricePerHourIndex, rowIndex, realmGet$fPricePerHour, false);
        }
        String realmGet$fMinHour = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$fMinHour();
        if (realmGet$fMinHour != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fMinHourIndex, rowIndex, realmGet$fMinHour, false);
        }
        String realmGet$iVehicleTypeId = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$iVehicleTypeId();
        if (realmGet$iVehicleTypeId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.iVehicleTypeIdIndex, rowIndex, realmGet$iVehicleTypeId, false);
        }
        Table.nativeSetBoolean(tableNativePtr, columnInfo.isAddIndex, rowIndex, ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$isAdd(), false);
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.adapter.files.CategoryListItem.class);
        long tableNativePtr = table.getNativePtr();
        CategoryListItemColumnInfo columnInfo = (CategoryListItemColumnInfo) realm.getSchema().getColumnInfo(com.adapter.files.CategoryListItem.class);
        com.adapter.files.CategoryListItem object = null;
        while (objects.hasNext()) {
            object = (com.adapter.files.CategoryListItem) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = OsObject.createRow(table);
            cache.put(object, rowIndex);
            Table.nativeSetLong(tableNativePtr, columnInfo.typeIndex, rowIndex, ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$type(), false);
            String realmGet$text = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$text();
            if (realmGet$text != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.textIndex, rowIndex, realmGet$text, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.sectionPositionIndex, rowIndex, ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$sectionPosition(), false);
            Table.nativeSetLong(tableNativePtr, columnInfo.listPositionIndex, rowIndex, ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$listPosition(), false);
            Table.nativeSetLong(tableNativePtr, columnInfo.CountSubItemsIndex, rowIndex, ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$CountSubItems(), false);
            String realmGet$vTitle = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$vTitle();
            if (realmGet$vTitle != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.vTitleIndex, rowIndex, realmGet$vTitle, false);
            }
            String realmGet$vDesc = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$vDesc();
            if (realmGet$vDesc != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.vDescIndex, rowIndex, realmGet$vDesc, false);
            }
            String realmGet$iVehicleCategoryId = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$iVehicleCategoryId();
            if (realmGet$iVehicleCategoryId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.iVehicleCategoryIdIndex, rowIndex, realmGet$iVehicleCategoryId, false);
            }
            String realmGet$vCategory = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$vCategory();
            if (realmGet$vCategory != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.vCategoryIndex, rowIndex, realmGet$vCategory, false);
            }
            String realmGet$eFareType = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$eFareType();
            if (realmGet$eFareType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.eFareTypeIndex, rowIndex, realmGet$eFareType, false);
            }
            String realmGet$eFareValue = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$eFareValue();
            if (realmGet$eFareValue != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.eFareValueIndex, rowIndex, realmGet$eFareValue, false);
            }
            String realmGet$fFixedFare = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$fFixedFare();
            if (realmGet$fFixedFare != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fFixedFareIndex, rowIndex, realmGet$fFixedFare, false);
            }
            String realmGet$fPricePerHour = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$fPricePerHour();
            if (realmGet$fPricePerHour != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fPricePerHourIndex, rowIndex, realmGet$fPricePerHour, false);
            }
            String realmGet$fMinHour = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$fMinHour();
            if (realmGet$fMinHour != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fMinHourIndex, rowIndex, realmGet$fMinHour, false);
            }
            String realmGet$iVehicleTypeId = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$iVehicleTypeId();
            if (realmGet$iVehicleTypeId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.iVehicleTypeIdIndex, rowIndex, realmGet$iVehicleTypeId, false);
            }
            Table.nativeSetBoolean(tableNativePtr, columnInfo.isAddIndex, rowIndex, ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$isAdd(), false);
        }
    }

    public static long insertOrUpdate(Realm realm, com.adapter.files.CategoryListItem object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.adapter.files.CategoryListItem.class);
        long tableNativePtr = table.getNativePtr();
        CategoryListItemColumnInfo columnInfo = (CategoryListItemColumnInfo) realm.getSchema().getColumnInfo(com.adapter.files.CategoryListItem.class);
        long rowIndex = OsObject.createRow(table);
        cache.put(object, rowIndex);
        Table.nativeSetLong(tableNativePtr, columnInfo.typeIndex, rowIndex, ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$type(), false);
        String realmGet$text = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$text();
        if (realmGet$text != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.textIndex, rowIndex, realmGet$text, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.textIndex, rowIndex, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.sectionPositionIndex, rowIndex, ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$sectionPosition(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.listPositionIndex, rowIndex, ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$listPosition(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.CountSubItemsIndex, rowIndex, ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$CountSubItems(), false);
        String realmGet$vTitle = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$vTitle();
        if (realmGet$vTitle != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.vTitleIndex, rowIndex, realmGet$vTitle, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.vTitleIndex, rowIndex, false);
        }
        String realmGet$vDesc = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$vDesc();
        if (realmGet$vDesc != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.vDescIndex, rowIndex, realmGet$vDesc, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.vDescIndex, rowIndex, false);
        }
        String realmGet$iVehicleCategoryId = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$iVehicleCategoryId();
        if (realmGet$iVehicleCategoryId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.iVehicleCategoryIdIndex, rowIndex, realmGet$iVehicleCategoryId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.iVehicleCategoryIdIndex, rowIndex, false);
        }
        String realmGet$vCategory = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$vCategory();
        if (realmGet$vCategory != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.vCategoryIndex, rowIndex, realmGet$vCategory, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.vCategoryIndex, rowIndex, false);
        }
        String realmGet$eFareType = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$eFareType();
        if (realmGet$eFareType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.eFareTypeIndex, rowIndex, realmGet$eFareType, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.eFareTypeIndex, rowIndex, false);
        }
        String realmGet$eFareValue = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$eFareValue();
        if (realmGet$eFareValue != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.eFareValueIndex, rowIndex, realmGet$eFareValue, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.eFareValueIndex, rowIndex, false);
        }
        String realmGet$fFixedFare = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$fFixedFare();
        if (realmGet$fFixedFare != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fFixedFareIndex, rowIndex, realmGet$fFixedFare, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fFixedFareIndex, rowIndex, false);
        }
        String realmGet$fPricePerHour = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$fPricePerHour();
        if (realmGet$fPricePerHour != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fPricePerHourIndex, rowIndex, realmGet$fPricePerHour, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fPricePerHourIndex, rowIndex, false);
        }
        String realmGet$fMinHour = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$fMinHour();
        if (realmGet$fMinHour != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fMinHourIndex, rowIndex, realmGet$fMinHour, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fMinHourIndex, rowIndex, false);
        }
        String realmGet$iVehicleTypeId = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$iVehicleTypeId();
        if (realmGet$iVehicleTypeId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.iVehicleTypeIdIndex, rowIndex, realmGet$iVehicleTypeId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.iVehicleTypeIdIndex, rowIndex, false);
        }
        Table.nativeSetBoolean(tableNativePtr, columnInfo.isAddIndex, rowIndex, ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$isAdd(), false);
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.adapter.files.CategoryListItem.class);
        long tableNativePtr = table.getNativePtr();
        CategoryListItemColumnInfo columnInfo = (CategoryListItemColumnInfo) realm.getSchema().getColumnInfo(com.adapter.files.CategoryListItem.class);
        com.adapter.files.CategoryListItem object = null;
        while (objects.hasNext()) {
            object = (com.adapter.files.CategoryListItem) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = OsObject.createRow(table);
            cache.put(object, rowIndex);
            Table.nativeSetLong(tableNativePtr, columnInfo.typeIndex, rowIndex, ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$type(), false);
            String realmGet$text = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$text();
            if (realmGet$text != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.textIndex, rowIndex, realmGet$text, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.textIndex, rowIndex, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.sectionPositionIndex, rowIndex, ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$sectionPosition(), false);
            Table.nativeSetLong(tableNativePtr, columnInfo.listPositionIndex, rowIndex, ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$listPosition(), false);
            Table.nativeSetLong(tableNativePtr, columnInfo.CountSubItemsIndex, rowIndex, ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$CountSubItems(), false);
            String realmGet$vTitle = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$vTitle();
            if (realmGet$vTitle != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.vTitleIndex, rowIndex, realmGet$vTitle, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.vTitleIndex, rowIndex, false);
            }
            String realmGet$vDesc = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$vDesc();
            if (realmGet$vDesc != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.vDescIndex, rowIndex, realmGet$vDesc, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.vDescIndex, rowIndex, false);
            }
            String realmGet$iVehicleCategoryId = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$iVehicleCategoryId();
            if (realmGet$iVehicleCategoryId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.iVehicleCategoryIdIndex, rowIndex, realmGet$iVehicleCategoryId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.iVehicleCategoryIdIndex, rowIndex, false);
            }
            String realmGet$vCategory = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$vCategory();
            if (realmGet$vCategory != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.vCategoryIndex, rowIndex, realmGet$vCategory, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.vCategoryIndex, rowIndex, false);
            }
            String realmGet$eFareType = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$eFareType();
            if (realmGet$eFareType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.eFareTypeIndex, rowIndex, realmGet$eFareType, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.eFareTypeIndex, rowIndex, false);
            }
            String realmGet$eFareValue = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$eFareValue();
            if (realmGet$eFareValue != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.eFareValueIndex, rowIndex, realmGet$eFareValue, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.eFareValueIndex, rowIndex, false);
            }
            String realmGet$fFixedFare = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$fFixedFare();
            if (realmGet$fFixedFare != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fFixedFareIndex, rowIndex, realmGet$fFixedFare, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.fFixedFareIndex, rowIndex, false);
            }
            String realmGet$fPricePerHour = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$fPricePerHour();
            if (realmGet$fPricePerHour != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fPricePerHourIndex, rowIndex, realmGet$fPricePerHour, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.fPricePerHourIndex, rowIndex, false);
            }
            String realmGet$fMinHour = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$fMinHour();
            if (realmGet$fMinHour != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fMinHourIndex, rowIndex, realmGet$fMinHour, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.fMinHourIndex, rowIndex, false);
            }
            String realmGet$iVehicleTypeId = ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$iVehicleTypeId();
            if (realmGet$iVehicleTypeId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.iVehicleTypeIdIndex, rowIndex, realmGet$iVehicleTypeId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.iVehicleTypeIdIndex, rowIndex, false);
            }
            Table.nativeSetBoolean(tableNativePtr, columnInfo.isAddIndex, rowIndex, ((com_adapter_files_CategoryListItemRealmProxyInterface) object).realmGet$isAdd(), false);
        }
    }

    public static com.adapter.files.CategoryListItem createDetachedCopy(com.adapter.files.CategoryListItem realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.adapter.files.CategoryListItem unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.adapter.files.CategoryListItem();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.adapter.files.CategoryListItem) cachedObject.object;
            }
            unmanagedObject = (com.adapter.files.CategoryListItem) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_adapter_files_CategoryListItemRealmProxyInterface unmanagedCopy = (com_adapter_files_CategoryListItemRealmProxyInterface) unmanagedObject;
        com_adapter_files_CategoryListItemRealmProxyInterface realmSource = (com_adapter_files_CategoryListItemRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$type(realmSource.realmGet$type());
        unmanagedCopy.realmSet$text(realmSource.realmGet$text());
        unmanagedCopy.realmSet$sectionPosition(realmSource.realmGet$sectionPosition());
        unmanagedCopy.realmSet$listPosition(realmSource.realmGet$listPosition());
        unmanagedCopy.realmSet$CountSubItems(realmSource.realmGet$CountSubItems());
        unmanagedCopy.realmSet$vTitle(realmSource.realmGet$vTitle());
        unmanagedCopy.realmSet$vDesc(realmSource.realmGet$vDesc());
        unmanagedCopy.realmSet$iVehicleCategoryId(realmSource.realmGet$iVehicleCategoryId());
        unmanagedCopy.realmSet$vCategory(realmSource.realmGet$vCategory());
        unmanagedCopy.realmSet$eFareType(realmSource.realmGet$eFareType());
        unmanagedCopy.realmSet$eFareValue(realmSource.realmGet$eFareValue());
        unmanagedCopy.realmSet$fFixedFare(realmSource.realmGet$fFixedFare());
        unmanagedCopy.realmSet$fPricePerHour(realmSource.realmGet$fPricePerHour());
        unmanagedCopy.realmSet$fMinHour(realmSource.realmGet$fMinHour());
        unmanagedCopy.realmSet$iVehicleTypeId(realmSource.realmGet$iVehicleTypeId());
        unmanagedCopy.realmSet$isAdd(realmSource.realmGet$isAdd());

        return unmanagedObject;
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
        com_adapter_files_CategoryListItemRealmProxy aCategoryListItem = (com_adapter_files_CategoryListItemRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aCategoryListItem.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aCategoryListItem.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aCategoryListItem.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
