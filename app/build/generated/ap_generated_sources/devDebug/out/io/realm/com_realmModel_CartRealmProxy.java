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
public class com_realmModel_CartRealmProxy extends com.realmModel.Cart
    implements RealmObjectProxy, com_realmModel_CartRealmProxyInterface {

    static final class CartColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long vItemTypeIndex;
        long QtyIndex;
        long vImageIndex;
        long fDiscountPriceIndex;
        long iMenuItemIdIndex;
        long eFoodTypeIndex;
        long iFoodMenuIdIndex;
        long iCompanyIdIndex;
        long vCompanyIndex;
        long iToppingIdIndex;
        long isToopingIndex;
        long isOptionIndex;
        long iOptionIdIndex;
        long currencySymbolIndex;
        long iServiceIdIndex;
        long ispriceshowIndex;
        long millisecondsIndex;

        CartColumnInfo(OsSchemaInfo schemaInfo) {
            super(17);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("Cart");
            this.vItemTypeIndex = addColumnDetails("vItemType", "vItemType", objectSchemaInfo);
            this.QtyIndex = addColumnDetails("Qty", "Qty", objectSchemaInfo);
            this.vImageIndex = addColumnDetails("vImage", "vImage", objectSchemaInfo);
            this.fDiscountPriceIndex = addColumnDetails("fDiscountPrice", "fDiscountPrice", objectSchemaInfo);
            this.iMenuItemIdIndex = addColumnDetails("iMenuItemId", "iMenuItemId", objectSchemaInfo);
            this.eFoodTypeIndex = addColumnDetails("eFoodType", "eFoodType", objectSchemaInfo);
            this.iFoodMenuIdIndex = addColumnDetails("iFoodMenuId", "iFoodMenuId", objectSchemaInfo);
            this.iCompanyIdIndex = addColumnDetails("iCompanyId", "iCompanyId", objectSchemaInfo);
            this.vCompanyIndex = addColumnDetails("vCompany", "vCompany", objectSchemaInfo);
            this.iToppingIdIndex = addColumnDetails("iToppingId", "iToppingId", objectSchemaInfo);
            this.isToopingIndex = addColumnDetails("isTooping", "isTooping", objectSchemaInfo);
            this.isOptionIndex = addColumnDetails("isOption", "isOption", objectSchemaInfo);
            this.iOptionIdIndex = addColumnDetails("iOptionId", "iOptionId", objectSchemaInfo);
            this.currencySymbolIndex = addColumnDetails("currencySymbol", "currencySymbol", objectSchemaInfo);
            this.iServiceIdIndex = addColumnDetails("iServiceId", "iServiceId", objectSchemaInfo);
            this.ispriceshowIndex = addColumnDetails("ispriceshow", "ispriceshow", objectSchemaInfo);
            this.millisecondsIndex = addColumnDetails("milliseconds", "milliseconds", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        CartColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new CartColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final CartColumnInfo src = (CartColumnInfo) rawSrc;
            final CartColumnInfo dst = (CartColumnInfo) rawDst;
            dst.vItemTypeIndex = src.vItemTypeIndex;
            dst.QtyIndex = src.QtyIndex;
            dst.vImageIndex = src.vImageIndex;
            dst.fDiscountPriceIndex = src.fDiscountPriceIndex;
            dst.iMenuItemIdIndex = src.iMenuItemIdIndex;
            dst.eFoodTypeIndex = src.eFoodTypeIndex;
            dst.iFoodMenuIdIndex = src.iFoodMenuIdIndex;
            dst.iCompanyIdIndex = src.iCompanyIdIndex;
            dst.vCompanyIndex = src.vCompanyIndex;
            dst.iToppingIdIndex = src.iToppingIdIndex;
            dst.isToopingIndex = src.isToopingIndex;
            dst.isOptionIndex = src.isOptionIndex;
            dst.iOptionIdIndex = src.iOptionIdIndex;
            dst.currencySymbolIndex = src.currencySymbolIndex;
            dst.iServiceIdIndex = src.iServiceIdIndex;
            dst.ispriceshowIndex = src.ispriceshowIndex;
            dst.millisecondsIndex = src.millisecondsIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private CartColumnInfo columnInfo;
    private ProxyState<com.realmModel.Cart> proxyState;

    com_realmModel_CartRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (CartColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.realmModel.Cart>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$vItemType() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.vItemTypeIndex);
    }

    @Override
    public void realmSet$vItemType(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.vItemTypeIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.vItemTypeIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.vItemTypeIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.vItemTypeIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$Qty() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.QtyIndex);
    }

    @Override
    public void realmSet$Qty(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.QtyIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.QtyIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.QtyIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.QtyIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$vImage() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.vImageIndex);
    }

    @Override
    public void realmSet$vImage(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.vImageIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.vImageIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.vImageIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.vImageIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$fDiscountPrice() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.fDiscountPriceIndex);
    }

    @Override
    public void realmSet$fDiscountPrice(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.fDiscountPriceIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.fDiscountPriceIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.fDiscountPriceIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.fDiscountPriceIndex, value);
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
    public String realmGet$eFoodType() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.eFoodTypeIndex);
    }

    @Override
    public void realmSet$eFoodType(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.eFoodTypeIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.eFoodTypeIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.eFoodTypeIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.eFoodTypeIndex, value);
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

    @Override
    @SuppressWarnings("cast")
    public String realmGet$iCompanyId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.iCompanyIdIndex);
    }

    @Override
    public void realmSet$iCompanyId(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.iCompanyIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.iCompanyIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.iCompanyIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.iCompanyIdIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$vCompany() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.vCompanyIndex);
    }

    @Override
    public void realmSet$vCompany(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.vCompanyIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.vCompanyIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.vCompanyIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.vCompanyIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$iToppingId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.iToppingIdIndex);
    }

    @Override
    public void realmSet$iToppingId(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.iToppingIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.iToppingIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.iToppingIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.iToppingIdIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$isTooping() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.isToopingIndex);
    }

    @Override
    public void realmSet$isTooping(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.isToopingIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.isToopingIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.isToopingIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.isToopingIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$isOption() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.isOptionIndex);
    }

    @Override
    public void realmSet$isOption(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.isOptionIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.isOptionIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.isOptionIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.isOptionIndex, value);
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
    public String realmGet$currencySymbol() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.currencySymbolIndex);
    }

    @Override
    public void realmSet$currencySymbol(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.currencySymbolIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.currencySymbolIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.currencySymbolIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.currencySymbolIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$iServiceId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.iServiceIdIndex);
    }

    @Override
    public void realmSet$iServiceId(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.iServiceIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.iServiceIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.iServiceIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.iServiceIdIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$ispriceshow() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.ispriceshowIndex);
    }

    @Override
    public void realmSet$ispriceshow(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.ispriceshowIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.ispriceshowIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.ispriceshowIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.ispriceshowIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public long realmGet$milliseconds() {
        proxyState.getRealm$realm().checkIfValid();
        return (long) proxyState.getRow$realm().getLong(columnInfo.millisecondsIndex);
    }

    @Override
    public void realmSet$milliseconds(long value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.millisecondsIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.millisecondsIndex, value);
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("Cart", 17, 0);
        builder.addPersistedProperty("vItemType", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("Qty", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("vImage", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("fDiscountPrice", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("iMenuItemId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("eFoodType", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("iFoodMenuId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("iCompanyId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("vCompany", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("iToppingId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("isTooping", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("isOption", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("iOptionId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("currencySymbol", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("iServiceId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("ispriceshow", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("milliseconds", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static CartColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new CartColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "Cart";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "Cart";
    }

    @SuppressWarnings("cast")
    public static com.realmModel.Cart createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        com.realmModel.Cart obj = realm.createObjectInternal(com.realmModel.Cart.class, true, excludeFields);

        final com_realmModel_CartRealmProxyInterface objProxy = (com_realmModel_CartRealmProxyInterface) obj;
        if (json.has("vItemType")) {
            if (json.isNull("vItemType")) {
                objProxy.realmSet$vItemType(null);
            } else {
                objProxy.realmSet$vItemType((String) json.getString("vItemType"));
            }
        }
        if (json.has("Qty")) {
            if (json.isNull("Qty")) {
                objProxy.realmSet$Qty(null);
            } else {
                objProxy.realmSet$Qty((String) json.getString("Qty"));
            }
        }
        if (json.has("vImage")) {
            if (json.isNull("vImage")) {
                objProxy.realmSet$vImage(null);
            } else {
                objProxy.realmSet$vImage((String) json.getString("vImage"));
            }
        }
        if (json.has("fDiscountPrice")) {
            if (json.isNull("fDiscountPrice")) {
                objProxy.realmSet$fDiscountPrice(null);
            } else {
                objProxy.realmSet$fDiscountPrice((String) json.getString("fDiscountPrice"));
            }
        }
        if (json.has("iMenuItemId")) {
            if (json.isNull("iMenuItemId")) {
                objProxy.realmSet$iMenuItemId(null);
            } else {
                objProxy.realmSet$iMenuItemId((String) json.getString("iMenuItemId"));
            }
        }
        if (json.has("eFoodType")) {
            if (json.isNull("eFoodType")) {
                objProxy.realmSet$eFoodType(null);
            } else {
                objProxy.realmSet$eFoodType((String) json.getString("eFoodType"));
            }
        }
        if (json.has("iFoodMenuId")) {
            if (json.isNull("iFoodMenuId")) {
                objProxy.realmSet$iFoodMenuId(null);
            } else {
                objProxy.realmSet$iFoodMenuId((String) json.getString("iFoodMenuId"));
            }
        }
        if (json.has("iCompanyId")) {
            if (json.isNull("iCompanyId")) {
                objProxy.realmSet$iCompanyId(null);
            } else {
                objProxy.realmSet$iCompanyId((String) json.getString("iCompanyId"));
            }
        }
        if (json.has("vCompany")) {
            if (json.isNull("vCompany")) {
                objProxy.realmSet$vCompany(null);
            } else {
                objProxy.realmSet$vCompany((String) json.getString("vCompany"));
            }
        }
        if (json.has("iToppingId")) {
            if (json.isNull("iToppingId")) {
                objProxy.realmSet$iToppingId(null);
            } else {
                objProxy.realmSet$iToppingId((String) json.getString("iToppingId"));
            }
        }
        if (json.has("isTooping")) {
            if (json.isNull("isTooping")) {
                objProxy.realmSet$isTooping(null);
            } else {
                objProxy.realmSet$isTooping((String) json.getString("isTooping"));
            }
        }
        if (json.has("isOption")) {
            if (json.isNull("isOption")) {
                objProxy.realmSet$isOption(null);
            } else {
                objProxy.realmSet$isOption((String) json.getString("isOption"));
            }
        }
        if (json.has("iOptionId")) {
            if (json.isNull("iOptionId")) {
                objProxy.realmSet$iOptionId(null);
            } else {
                objProxy.realmSet$iOptionId((String) json.getString("iOptionId"));
            }
        }
        if (json.has("currencySymbol")) {
            if (json.isNull("currencySymbol")) {
                objProxy.realmSet$currencySymbol(null);
            } else {
                objProxy.realmSet$currencySymbol((String) json.getString("currencySymbol"));
            }
        }
        if (json.has("iServiceId")) {
            if (json.isNull("iServiceId")) {
                objProxy.realmSet$iServiceId(null);
            } else {
                objProxy.realmSet$iServiceId((String) json.getString("iServiceId"));
            }
        }
        if (json.has("ispriceshow")) {
            if (json.isNull("ispriceshow")) {
                objProxy.realmSet$ispriceshow(null);
            } else {
                objProxy.realmSet$ispriceshow((String) json.getString("ispriceshow"));
            }
        }
        if (json.has("milliseconds")) {
            if (json.isNull("milliseconds")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'milliseconds' to null.");
            } else {
                objProxy.realmSet$milliseconds((long) json.getLong("milliseconds"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.realmModel.Cart createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        final com.realmModel.Cart obj = new com.realmModel.Cart();
        final com_realmModel_CartRealmProxyInterface objProxy = (com_realmModel_CartRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("vItemType")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$vItemType((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$vItemType(null);
                }
            } else if (name.equals("Qty")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$Qty((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$Qty(null);
                }
            } else if (name.equals("vImage")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$vImage((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$vImage(null);
                }
            } else if (name.equals("fDiscountPrice")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$fDiscountPrice((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$fDiscountPrice(null);
                }
            } else if (name.equals("iMenuItemId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$iMenuItemId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$iMenuItemId(null);
                }
            } else if (name.equals("eFoodType")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$eFoodType((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$eFoodType(null);
                }
            } else if (name.equals("iFoodMenuId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$iFoodMenuId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$iFoodMenuId(null);
                }
            } else if (name.equals("iCompanyId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$iCompanyId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$iCompanyId(null);
                }
            } else if (name.equals("vCompany")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$vCompany((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$vCompany(null);
                }
            } else if (name.equals("iToppingId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$iToppingId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$iToppingId(null);
                }
            } else if (name.equals("isTooping")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$isTooping((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$isTooping(null);
                }
            } else if (name.equals("isOption")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$isOption((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$isOption(null);
                }
            } else if (name.equals("iOptionId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$iOptionId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$iOptionId(null);
                }
            } else if (name.equals("currencySymbol")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$currencySymbol((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$currencySymbol(null);
                }
            } else if (name.equals("iServiceId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$iServiceId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$iServiceId(null);
                }
            } else if (name.equals("ispriceshow")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$ispriceshow((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$ispriceshow(null);
                }
            } else if (name.equals("milliseconds")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$milliseconds((long) reader.nextLong());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'milliseconds' to null.");
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return realm.copyToRealm(obj);
    }

    private static com_realmModel_CartRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.realmModel.Cart.class), false, Collections.<String>emptyList());
        io.realm.com_realmModel_CartRealmProxy obj = new io.realm.com_realmModel_CartRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.realmModel.Cart copyOrUpdate(Realm realm, CartColumnInfo columnInfo, com.realmModel.Cart object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (com.realmModel.Cart) cachedRealmObject;
        }

        return copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.realmModel.Cart copy(Realm realm, CartColumnInfo columnInfo, com.realmModel.Cart newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.realmModel.Cart) cachedRealmObject;
        }

        com_realmModel_CartRealmProxyInterface realmObjectSource = (com_realmModel_CartRealmProxyInterface) newObject;

        Table table = realm.getTable(com.realmModel.Cart.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.vItemTypeIndex, realmObjectSource.realmGet$vItemType());
        builder.addString(columnInfo.QtyIndex, realmObjectSource.realmGet$Qty());
        builder.addString(columnInfo.vImageIndex, realmObjectSource.realmGet$vImage());
        builder.addString(columnInfo.fDiscountPriceIndex, realmObjectSource.realmGet$fDiscountPrice());
        builder.addString(columnInfo.iMenuItemIdIndex, realmObjectSource.realmGet$iMenuItemId());
        builder.addString(columnInfo.eFoodTypeIndex, realmObjectSource.realmGet$eFoodType());
        builder.addString(columnInfo.iFoodMenuIdIndex, realmObjectSource.realmGet$iFoodMenuId());
        builder.addString(columnInfo.iCompanyIdIndex, realmObjectSource.realmGet$iCompanyId());
        builder.addString(columnInfo.vCompanyIndex, realmObjectSource.realmGet$vCompany());
        builder.addString(columnInfo.iToppingIdIndex, realmObjectSource.realmGet$iToppingId());
        builder.addString(columnInfo.isToopingIndex, realmObjectSource.realmGet$isTooping());
        builder.addString(columnInfo.isOptionIndex, realmObjectSource.realmGet$isOption());
        builder.addString(columnInfo.iOptionIdIndex, realmObjectSource.realmGet$iOptionId());
        builder.addString(columnInfo.currencySymbolIndex, realmObjectSource.realmGet$currencySymbol());
        builder.addString(columnInfo.iServiceIdIndex, realmObjectSource.realmGet$iServiceId());
        builder.addString(columnInfo.ispriceshowIndex, realmObjectSource.realmGet$ispriceshow());
        builder.addInteger(columnInfo.millisecondsIndex, realmObjectSource.realmGet$milliseconds());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_realmModel_CartRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        return realmObjectCopy;
    }

    public static long insert(Realm realm, com.realmModel.Cart object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.realmModel.Cart.class);
        long tableNativePtr = table.getNativePtr();
        CartColumnInfo columnInfo = (CartColumnInfo) realm.getSchema().getColumnInfo(com.realmModel.Cart.class);
        long rowIndex = OsObject.createRow(table);
        cache.put(object, rowIndex);
        String realmGet$vItemType = ((com_realmModel_CartRealmProxyInterface) object).realmGet$vItemType();
        if (realmGet$vItemType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.vItemTypeIndex, rowIndex, realmGet$vItemType, false);
        }
        String realmGet$Qty = ((com_realmModel_CartRealmProxyInterface) object).realmGet$Qty();
        if (realmGet$Qty != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.QtyIndex, rowIndex, realmGet$Qty, false);
        }
        String realmGet$vImage = ((com_realmModel_CartRealmProxyInterface) object).realmGet$vImage();
        if (realmGet$vImage != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.vImageIndex, rowIndex, realmGet$vImage, false);
        }
        String realmGet$fDiscountPrice = ((com_realmModel_CartRealmProxyInterface) object).realmGet$fDiscountPrice();
        if (realmGet$fDiscountPrice != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fDiscountPriceIndex, rowIndex, realmGet$fDiscountPrice, false);
        }
        String realmGet$iMenuItemId = ((com_realmModel_CartRealmProxyInterface) object).realmGet$iMenuItemId();
        if (realmGet$iMenuItemId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.iMenuItemIdIndex, rowIndex, realmGet$iMenuItemId, false);
        }
        String realmGet$eFoodType = ((com_realmModel_CartRealmProxyInterface) object).realmGet$eFoodType();
        if (realmGet$eFoodType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.eFoodTypeIndex, rowIndex, realmGet$eFoodType, false);
        }
        String realmGet$iFoodMenuId = ((com_realmModel_CartRealmProxyInterface) object).realmGet$iFoodMenuId();
        if (realmGet$iFoodMenuId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.iFoodMenuIdIndex, rowIndex, realmGet$iFoodMenuId, false);
        }
        String realmGet$iCompanyId = ((com_realmModel_CartRealmProxyInterface) object).realmGet$iCompanyId();
        if (realmGet$iCompanyId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.iCompanyIdIndex, rowIndex, realmGet$iCompanyId, false);
        }
        String realmGet$vCompany = ((com_realmModel_CartRealmProxyInterface) object).realmGet$vCompany();
        if (realmGet$vCompany != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.vCompanyIndex, rowIndex, realmGet$vCompany, false);
        }
        String realmGet$iToppingId = ((com_realmModel_CartRealmProxyInterface) object).realmGet$iToppingId();
        if (realmGet$iToppingId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.iToppingIdIndex, rowIndex, realmGet$iToppingId, false);
        }
        String realmGet$isTooping = ((com_realmModel_CartRealmProxyInterface) object).realmGet$isTooping();
        if (realmGet$isTooping != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.isToopingIndex, rowIndex, realmGet$isTooping, false);
        }
        String realmGet$isOption = ((com_realmModel_CartRealmProxyInterface) object).realmGet$isOption();
        if (realmGet$isOption != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.isOptionIndex, rowIndex, realmGet$isOption, false);
        }
        String realmGet$iOptionId = ((com_realmModel_CartRealmProxyInterface) object).realmGet$iOptionId();
        if (realmGet$iOptionId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.iOptionIdIndex, rowIndex, realmGet$iOptionId, false);
        }
        String realmGet$currencySymbol = ((com_realmModel_CartRealmProxyInterface) object).realmGet$currencySymbol();
        if (realmGet$currencySymbol != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.currencySymbolIndex, rowIndex, realmGet$currencySymbol, false);
        }
        String realmGet$iServiceId = ((com_realmModel_CartRealmProxyInterface) object).realmGet$iServiceId();
        if (realmGet$iServiceId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.iServiceIdIndex, rowIndex, realmGet$iServiceId, false);
        }
        String realmGet$ispriceshow = ((com_realmModel_CartRealmProxyInterface) object).realmGet$ispriceshow();
        if (realmGet$ispriceshow != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.ispriceshowIndex, rowIndex, realmGet$ispriceshow, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.millisecondsIndex, rowIndex, ((com_realmModel_CartRealmProxyInterface) object).realmGet$milliseconds(), false);
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.realmModel.Cart.class);
        long tableNativePtr = table.getNativePtr();
        CartColumnInfo columnInfo = (CartColumnInfo) realm.getSchema().getColumnInfo(com.realmModel.Cart.class);
        com.realmModel.Cart object = null;
        while (objects.hasNext()) {
            object = (com.realmModel.Cart) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = OsObject.createRow(table);
            cache.put(object, rowIndex);
            String realmGet$vItemType = ((com_realmModel_CartRealmProxyInterface) object).realmGet$vItemType();
            if (realmGet$vItemType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.vItemTypeIndex, rowIndex, realmGet$vItemType, false);
            }
            String realmGet$Qty = ((com_realmModel_CartRealmProxyInterface) object).realmGet$Qty();
            if (realmGet$Qty != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.QtyIndex, rowIndex, realmGet$Qty, false);
            }
            String realmGet$vImage = ((com_realmModel_CartRealmProxyInterface) object).realmGet$vImage();
            if (realmGet$vImage != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.vImageIndex, rowIndex, realmGet$vImage, false);
            }
            String realmGet$fDiscountPrice = ((com_realmModel_CartRealmProxyInterface) object).realmGet$fDiscountPrice();
            if (realmGet$fDiscountPrice != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fDiscountPriceIndex, rowIndex, realmGet$fDiscountPrice, false);
            }
            String realmGet$iMenuItemId = ((com_realmModel_CartRealmProxyInterface) object).realmGet$iMenuItemId();
            if (realmGet$iMenuItemId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.iMenuItemIdIndex, rowIndex, realmGet$iMenuItemId, false);
            }
            String realmGet$eFoodType = ((com_realmModel_CartRealmProxyInterface) object).realmGet$eFoodType();
            if (realmGet$eFoodType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.eFoodTypeIndex, rowIndex, realmGet$eFoodType, false);
            }
            String realmGet$iFoodMenuId = ((com_realmModel_CartRealmProxyInterface) object).realmGet$iFoodMenuId();
            if (realmGet$iFoodMenuId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.iFoodMenuIdIndex, rowIndex, realmGet$iFoodMenuId, false);
            }
            String realmGet$iCompanyId = ((com_realmModel_CartRealmProxyInterface) object).realmGet$iCompanyId();
            if (realmGet$iCompanyId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.iCompanyIdIndex, rowIndex, realmGet$iCompanyId, false);
            }
            String realmGet$vCompany = ((com_realmModel_CartRealmProxyInterface) object).realmGet$vCompany();
            if (realmGet$vCompany != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.vCompanyIndex, rowIndex, realmGet$vCompany, false);
            }
            String realmGet$iToppingId = ((com_realmModel_CartRealmProxyInterface) object).realmGet$iToppingId();
            if (realmGet$iToppingId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.iToppingIdIndex, rowIndex, realmGet$iToppingId, false);
            }
            String realmGet$isTooping = ((com_realmModel_CartRealmProxyInterface) object).realmGet$isTooping();
            if (realmGet$isTooping != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.isToopingIndex, rowIndex, realmGet$isTooping, false);
            }
            String realmGet$isOption = ((com_realmModel_CartRealmProxyInterface) object).realmGet$isOption();
            if (realmGet$isOption != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.isOptionIndex, rowIndex, realmGet$isOption, false);
            }
            String realmGet$iOptionId = ((com_realmModel_CartRealmProxyInterface) object).realmGet$iOptionId();
            if (realmGet$iOptionId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.iOptionIdIndex, rowIndex, realmGet$iOptionId, false);
            }
            String realmGet$currencySymbol = ((com_realmModel_CartRealmProxyInterface) object).realmGet$currencySymbol();
            if (realmGet$currencySymbol != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.currencySymbolIndex, rowIndex, realmGet$currencySymbol, false);
            }
            String realmGet$iServiceId = ((com_realmModel_CartRealmProxyInterface) object).realmGet$iServiceId();
            if (realmGet$iServiceId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.iServiceIdIndex, rowIndex, realmGet$iServiceId, false);
            }
            String realmGet$ispriceshow = ((com_realmModel_CartRealmProxyInterface) object).realmGet$ispriceshow();
            if (realmGet$ispriceshow != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.ispriceshowIndex, rowIndex, realmGet$ispriceshow, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.millisecondsIndex, rowIndex, ((com_realmModel_CartRealmProxyInterface) object).realmGet$milliseconds(), false);
        }
    }

    public static long insertOrUpdate(Realm realm, com.realmModel.Cart object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.realmModel.Cart.class);
        long tableNativePtr = table.getNativePtr();
        CartColumnInfo columnInfo = (CartColumnInfo) realm.getSchema().getColumnInfo(com.realmModel.Cart.class);
        long rowIndex = OsObject.createRow(table);
        cache.put(object, rowIndex);
        String realmGet$vItemType = ((com_realmModel_CartRealmProxyInterface) object).realmGet$vItemType();
        if (realmGet$vItemType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.vItemTypeIndex, rowIndex, realmGet$vItemType, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.vItemTypeIndex, rowIndex, false);
        }
        String realmGet$Qty = ((com_realmModel_CartRealmProxyInterface) object).realmGet$Qty();
        if (realmGet$Qty != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.QtyIndex, rowIndex, realmGet$Qty, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.QtyIndex, rowIndex, false);
        }
        String realmGet$vImage = ((com_realmModel_CartRealmProxyInterface) object).realmGet$vImage();
        if (realmGet$vImage != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.vImageIndex, rowIndex, realmGet$vImage, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.vImageIndex, rowIndex, false);
        }
        String realmGet$fDiscountPrice = ((com_realmModel_CartRealmProxyInterface) object).realmGet$fDiscountPrice();
        if (realmGet$fDiscountPrice != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fDiscountPriceIndex, rowIndex, realmGet$fDiscountPrice, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fDiscountPriceIndex, rowIndex, false);
        }
        String realmGet$iMenuItemId = ((com_realmModel_CartRealmProxyInterface) object).realmGet$iMenuItemId();
        if (realmGet$iMenuItemId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.iMenuItemIdIndex, rowIndex, realmGet$iMenuItemId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.iMenuItemIdIndex, rowIndex, false);
        }
        String realmGet$eFoodType = ((com_realmModel_CartRealmProxyInterface) object).realmGet$eFoodType();
        if (realmGet$eFoodType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.eFoodTypeIndex, rowIndex, realmGet$eFoodType, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.eFoodTypeIndex, rowIndex, false);
        }
        String realmGet$iFoodMenuId = ((com_realmModel_CartRealmProxyInterface) object).realmGet$iFoodMenuId();
        if (realmGet$iFoodMenuId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.iFoodMenuIdIndex, rowIndex, realmGet$iFoodMenuId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.iFoodMenuIdIndex, rowIndex, false);
        }
        String realmGet$iCompanyId = ((com_realmModel_CartRealmProxyInterface) object).realmGet$iCompanyId();
        if (realmGet$iCompanyId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.iCompanyIdIndex, rowIndex, realmGet$iCompanyId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.iCompanyIdIndex, rowIndex, false);
        }
        String realmGet$vCompany = ((com_realmModel_CartRealmProxyInterface) object).realmGet$vCompany();
        if (realmGet$vCompany != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.vCompanyIndex, rowIndex, realmGet$vCompany, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.vCompanyIndex, rowIndex, false);
        }
        String realmGet$iToppingId = ((com_realmModel_CartRealmProxyInterface) object).realmGet$iToppingId();
        if (realmGet$iToppingId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.iToppingIdIndex, rowIndex, realmGet$iToppingId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.iToppingIdIndex, rowIndex, false);
        }
        String realmGet$isTooping = ((com_realmModel_CartRealmProxyInterface) object).realmGet$isTooping();
        if (realmGet$isTooping != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.isToopingIndex, rowIndex, realmGet$isTooping, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.isToopingIndex, rowIndex, false);
        }
        String realmGet$isOption = ((com_realmModel_CartRealmProxyInterface) object).realmGet$isOption();
        if (realmGet$isOption != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.isOptionIndex, rowIndex, realmGet$isOption, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.isOptionIndex, rowIndex, false);
        }
        String realmGet$iOptionId = ((com_realmModel_CartRealmProxyInterface) object).realmGet$iOptionId();
        if (realmGet$iOptionId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.iOptionIdIndex, rowIndex, realmGet$iOptionId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.iOptionIdIndex, rowIndex, false);
        }
        String realmGet$currencySymbol = ((com_realmModel_CartRealmProxyInterface) object).realmGet$currencySymbol();
        if (realmGet$currencySymbol != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.currencySymbolIndex, rowIndex, realmGet$currencySymbol, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.currencySymbolIndex, rowIndex, false);
        }
        String realmGet$iServiceId = ((com_realmModel_CartRealmProxyInterface) object).realmGet$iServiceId();
        if (realmGet$iServiceId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.iServiceIdIndex, rowIndex, realmGet$iServiceId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.iServiceIdIndex, rowIndex, false);
        }
        String realmGet$ispriceshow = ((com_realmModel_CartRealmProxyInterface) object).realmGet$ispriceshow();
        if (realmGet$ispriceshow != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.ispriceshowIndex, rowIndex, realmGet$ispriceshow, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.ispriceshowIndex, rowIndex, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.millisecondsIndex, rowIndex, ((com_realmModel_CartRealmProxyInterface) object).realmGet$milliseconds(), false);
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.realmModel.Cart.class);
        long tableNativePtr = table.getNativePtr();
        CartColumnInfo columnInfo = (CartColumnInfo) realm.getSchema().getColumnInfo(com.realmModel.Cart.class);
        com.realmModel.Cart object = null;
        while (objects.hasNext()) {
            object = (com.realmModel.Cart) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = OsObject.createRow(table);
            cache.put(object, rowIndex);
            String realmGet$vItemType = ((com_realmModel_CartRealmProxyInterface) object).realmGet$vItemType();
            if (realmGet$vItemType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.vItemTypeIndex, rowIndex, realmGet$vItemType, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.vItemTypeIndex, rowIndex, false);
            }
            String realmGet$Qty = ((com_realmModel_CartRealmProxyInterface) object).realmGet$Qty();
            if (realmGet$Qty != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.QtyIndex, rowIndex, realmGet$Qty, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.QtyIndex, rowIndex, false);
            }
            String realmGet$vImage = ((com_realmModel_CartRealmProxyInterface) object).realmGet$vImage();
            if (realmGet$vImage != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.vImageIndex, rowIndex, realmGet$vImage, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.vImageIndex, rowIndex, false);
            }
            String realmGet$fDiscountPrice = ((com_realmModel_CartRealmProxyInterface) object).realmGet$fDiscountPrice();
            if (realmGet$fDiscountPrice != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fDiscountPriceIndex, rowIndex, realmGet$fDiscountPrice, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.fDiscountPriceIndex, rowIndex, false);
            }
            String realmGet$iMenuItemId = ((com_realmModel_CartRealmProxyInterface) object).realmGet$iMenuItemId();
            if (realmGet$iMenuItemId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.iMenuItemIdIndex, rowIndex, realmGet$iMenuItemId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.iMenuItemIdIndex, rowIndex, false);
            }
            String realmGet$eFoodType = ((com_realmModel_CartRealmProxyInterface) object).realmGet$eFoodType();
            if (realmGet$eFoodType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.eFoodTypeIndex, rowIndex, realmGet$eFoodType, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.eFoodTypeIndex, rowIndex, false);
            }
            String realmGet$iFoodMenuId = ((com_realmModel_CartRealmProxyInterface) object).realmGet$iFoodMenuId();
            if (realmGet$iFoodMenuId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.iFoodMenuIdIndex, rowIndex, realmGet$iFoodMenuId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.iFoodMenuIdIndex, rowIndex, false);
            }
            String realmGet$iCompanyId = ((com_realmModel_CartRealmProxyInterface) object).realmGet$iCompanyId();
            if (realmGet$iCompanyId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.iCompanyIdIndex, rowIndex, realmGet$iCompanyId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.iCompanyIdIndex, rowIndex, false);
            }
            String realmGet$vCompany = ((com_realmModel_CartRealmProxyInterface) object).realmGet$vCompany();
            if (realmGet$vCompany != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.vCompanyIndex, rowIndex, realmGet$vCompany, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.vCompanyIndex, rowIndex, false);
            }
            String realmGet$iToppingId = ((com_realmModel_CartRealmProxyInterface) object).realmGet$iToppingId();
            if (realmGet$iToppingId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.iToppingIdIndex, rowIndex, realmGet$iToppingId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.iToppingIdIndex, rowIndex, false);
            }
            String realmGet$isTooping = ((com_realmModel_CartRealmProxyInterface) object).realmGet$isTooping();
            if (realmGet$isTooping != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.isToopingIndex, rowIndex, realmGet$isTooping, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.isToopingIndex, rowIndex, false);
            }
            String realmGet$isOption = ((com_realmModel_CartRealmProxyInterface) object).realmGet$isOption();
            if (realmGet$isOption != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.isOptionIndex, rowIndex, realmGet$isOption, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.isOptionIndex, rowIndex, false);
            }
            String realmGet$iOptionId = ((com_realmModel_CartRealmProxyInterface) object).realmGet$iOptionId();
            if (realmGet$iOptionId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.iOptionIdIndex, rowIndex, realmGet$iOptionId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.iOptionIdIndex, rowIndex, false);
            }
            String realmGet$currencySymbol = ((com_realmModel_CartRealmProxyInterface) object).realmGet$currencySymbol();
            if (realmGet$currencySymbol != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.currencySymbolIndex, rowIndex, realmGet$currencySymbol, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.currencySymbolIndex, rowIndex, false);
            }
            String realmGet$iServiceId = ((com_realmModel_CartRealmProxyInterface) object).realmGet$iServiceId();
            if (realmGet$iServiceId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.iServiceIdIndex, rowIndex, realmGet$iServiceId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.iServiceIdIndex, rowIndex, false);
            }
            String realmGet$ispriceshow = ((com_realmModel_CartRealmProxyInterface) object).realmGet$ispriceshow();
            if (realmGet$ispriceshow != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.ispriceshowIndex, rowIndex, realmGet$ispriceshow, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.ispriceshowIndex, rowIndex, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.millisecondsIndex, rowIndex, ((com_realmModel_CartRealmProxyInterface) object).realmGet$milliseconds(), false);
        }
    }

    public static com.realmModel.Cart createDetachedCopy(com.realmModel.Cart realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.realmModel.Cart unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.realmModel.Cart();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.realmModel.Cart) cachedObject.object;
            }
            unmanagedObject = (com.realmModel.Cart) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_realmModel_CartRealmProxyInterface unmanagedCopy = (com_realmModel_CartRealmProxyInterface) unmanagedObject;
        com_realmModel_CartRealmProxyInterface realmSource = (com_realmModel_CartRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$vItemType(realmSource.realmGet$vItemType());
        unmanagedCopy.realmSet$Qty(realmSource.realmGet$Qty());
        unmanagedCopy.realmSet$vImage(realmSource.realmGet$vImage());
        unmanagedCopy.realmSet$fDiscountPrice(realmSource.realmGet$fDiscountPrice());
        unmanagedCopy.realmSet$iMenuItemId(realmSource.realmGet$iMenuItemId());
        unmanagedCopy.realmSet$eFoodType(realmSource.realmGet$eFoodType());
        unmanagedCopy.realmSet$iFoodMenuId(realmSource.realmGet$iFoodMenuId());
        unmanagedCopy.realmSet$iCompanyId(realmSource.realmGet$iCompanyId());
        unmanagedCopy.realmSet$vCompany(realmSource.realmGet$vCompany());
        unmanagedCopy.realmSet$iToppingId(realmSource.realmGet$iToppingId());
        unmanagedCopy.realmSet$isTooping(realmSource.realmGet$isTooping());
        unmanagedCopy.realmSet$isOption(realmSource.realmGet$isOption());
        unmanagedCopy.realmSet$iOptionId(realmSource.realmGet$iOptionId());
        unmanagedCopy.realmSet$currencySymbol(realmSource.realmGet$currencySymbol());
        unmanagedCopy.realmSet$iServiceId(realmSource.realmGet$iServiceId());
        unmanagedCopy.realmSet$ispriceshow(realmSource.realmGet$ispriceshow());
        unmanagedCopy.realmSet$milliseconds(realmSource.realmGet$milliseconds());

        return unmanagedObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("Cart = proxy[");
        stringBuilder.append("{vItemType:");
        stringBuilder.append(realmGet$vItemType() != null ? realmGet$vItemType() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{Qty:");
        stringBuilder.append(realmGet$Qty() != null ? realmGet$Qty() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{vImage:");
        stringBuilder.append(realmGet$vImage() != null ? realmGet$vImage() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{fDiscountPrice:");
        stringBuilder.append(realmGet$fDiscountPrice() != null ? realmGet$fDiscountPrice() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{iMenuItemId:");
        stringBuilder.append(realmGet$iMenuItemId() != null ? realmGet$iMenuItemId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{eFoodType:");
        stringBuilder.append(realmGet$eFoodType() != null ? realmGet$eFoodType() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{iFoodMenuId:");
        stringBuilder.append(realmGet$iFoodMenuId() != null ? realmGet$iFoodMenuId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{iCompanyId:");
        stringBuilder.append(realmGet$iCompanyId() != null ? realmGet$iCompanyId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{vCompany:");
        stringBuilder.append(realmGet$vCompany() != null ? realmGet$vCompany() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{iToppingId:");
        stringBuilder.append(realmGet$iToppingId() != null ? realmGet$iToppingId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{isTooping:");
        stringBuilder.append(realmGet$isTooping() != null ? realmGet$isTooping() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{isOption:");
        stringBuilder.append(realmGet$isOption() != null ? realmGet$isOption() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{iOptionId:");
        stringBuilder.append(realmGet$iOptionId() != null ? realmGet$iOptionId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{currencySymbol:");
        stringBuilder.append(realmGet$currencySymbol() != null ? realmGet$currencySymbol() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{iServiceId:");
        stringBuilder.append(realmGet$iServiceId() != null ? realmGet$iServiceId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{ispriceshow:");
        stringBuilder.append(realmGet$ispriceshow() != null ? realmGet$ispriceshow() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{milliseconds:");
        stringBuilder.append(realmGet$milliseconds());
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
        com_realmModel_CartRealmProxy aCart = (com_realmModel_CartRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aCart.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aCart.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aCart.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
