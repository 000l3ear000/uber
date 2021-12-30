package io.realm;


import android.util.JsonReader;
import io.realm.ImportFlag;
import io.realm.internal.ColumnInfo;
import io.realm.internal.OsObjectSchemaInfo;
import io.realm.internal.OsSchemaInfo;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.RealmProxyMediator;
import io.realm.internal.Row;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

@io.realm.annotations.RealmModule
class DefaultRealmModuleMediator extends RealmProxyMediator {

    private static final Set<Class<? extends RealmModel>> MODEL_CLASSES;
    static {
        Set<Class<? extends RealmModel>> modelClasses = new HashSet<Class<? extends RealmModel>>(5);
        modelClasses.add(com.adapter.files.CategoryListItem.class);
        modelClasses.add(com.realmModel.Cart.class);
        modelClasses.add(com.realmModel.CarWashCartData.class);
        modelClasses.add(com.realmModel.Options.class);
        modelClasses.add(com.realmModel.Topping.class);
        MODEL_CLASSES = Collections.unmodifiableSet(modelClasses);
    }

    @Override
    public Map<Class<? extends RealmModel>, OsObjectSchemaInfo> getExpectedObjectSchemaInfoMap() {
        Map<Class<? extends RealmModel>, OsObjectSchemaInfo> infoMap = new HashMap<Class<? extends RealmModel>, OsObjectSchemaInfo>(5);
        infoMap.put(com.adapter.files.CategoryListItem.class, io.realm.com_adapter_files_CategoryListItemRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.realmModel.Cart.class, io.realm.com_realmModel_CartRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.realmModel.CarWashCartData.class, io.realm.com_realmModel_CarWashCartDataRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.realmModel.Options.class, io.realm.com_realmModel_OptionsRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.realmModel.Topping.class, io.realm.com_realmModel_ToppingRealmProxy.getExpectedObjectSchemaInfo());
        return infoMap;
    }

    @Override
    public ColumnInfo createColumnInfo(Class<? extends RealmModel> clazz, OsSchemaInfo schemaInfo) {
        checkClass(clazz);

        if (clazz.equals(com.adapter.files.CategoryListItem.class)) {
            return io.realm.com_adapter_files_CategoryListItemRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.realmModel.Cart.class)) {
            return io.realm.com_realmModel_CartRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.realmModel.CarWashCartData.class)) {
            return io.realm.com_realmModel_CarWashCartDataRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.realmModel.Options.class)) {
            return io.realm.com_realmModel_OptionsRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.realmModel.Topping.class)) {
            return io.realm.com_realmModel_ToppingRealmProxy.createColumnInfo(schemaInfo);
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public String getSimpleClassNameImpl(Class<? extends RealmModel> clazz) {
        checkClass(clazz);

        if (clazz.equals(com.adapter.files.CategoryListItem.class)) {
            return "CategoryListItem";
        }
        if (clazz.equals(com.realmModel.Cart.class)) {
            return "Cart";
        }
        if (clazz.equals(com.realmModel.CarWashCartData.class)) {
            return "CarWashCartData";
        }
        if (clazz.equals(com.realmModel.Options.class)) {
            return "Options";
        }
        if (clazz.equals(com.realmModel.Topping.class)) {
            return "Topping";
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public <E extends RealmModel> E newInstance(Class<E> clazz, Object baseRealm, Row row, ColumnInfo columnInfo, boolean acceptDefaultValue, List<String> excludeFields) {
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        try {
            objectContext.set((BaseRealm) baseRealm, row, columnInfo, acceptDefaultValue, excludeFields);
            checkClass(clazz);

            if (clazz.equals(com.adapter.files.CategoryListItem.class)) {
                return clazz.cast(new io.realm.com_adapter_files_CategoryListItemRealmProxy());
            }
            if (clazz.equals(com.realmModel.Cart.class)) {
                return clazz.cast(new io.realm.com_realmModel_CartRealmProxy());
            }
            if (clazz.equals(com.realmModel.CarWashCartData.class)) {
                return clazz.cast(new io.realm.com_realmModel_CarWashCartDataRealmProxy());
            }
            if (clazz.equals(com.realmModel.Options.class)) {
                return clazz.cast(new io.realm.com_realmModel_OptionsRealmProxy());
            }
            if (clazz.equals(com.realmModel.Topping.class)) {
                return clazz.cast(new io.realm.com_realmModel_ToppingRealmProxy());
            }
            throw getMissingProxyClassException(clazz);
        } finally {
            objectContext.clear();
        }
    }

    @Override
    public Set<Class<? extends RealmModel>> getModelClasses() {
        return MODEL_CLASSES;
    }

    @Override
    public <E extends RealmModel> E copyOrUpdate(Realm realm, E obj, boolean update, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<E> clazz = (Class<E>) ((obj instanceof RealmObjectProxy) ? obj.getClass().getSuperclass() : obj.getClass());

        if (clazz.equals(com.adapter.files.CategoryListItem.class)) {
            com_adapter_files_CategoryListItemRealmProxy.CategoryListItemColumnInfo columnInfo = (com_adapter_files_CategoryListItemRealmProxy.CategoryListItemColumnInfo) realm.getSchema().getColumnInfo(com.adapter.files.CategoryListItem.class);
            return clazz.cast(io.realm.com_adapter_files_CategoryListItemRealmProxy.copyOrUpdate(realm, columnInfo, (com.adapter.files.CategoryListItem) obj, update, cache, flags));
        }
        if (clazz.equals(com.realmModel.Cart.class)) {
            com_realmModel_CartRealmProxy.CartColumnInfo columnInfo = (com_realmModel_CartRealmProxy.CartColumnInfo) realm.getSchema().getColumnInfo(com.realmModel.Cart.class);
            return clazz.cast(io.realm.com_realmModel_CartRealmProxy.copyOrUpdate(realm, columnInfo, (com.realmModel.Cart) obj, update, cache, flags));
        }
        if (clazz.equals(com.realmModel.CarWashCartData.class)) {
            com_realmModel_CarWashCartDataRealmProxy.CarWashCartDataColumnInfo columnInfo = (com_realmModel_CarWashCartDataRealmProxy.CarWashCartDataColumnInfo) realm.getSchema().getColumnInfo(com.realmModel.CarWashCartData.class);
            return clazz.cast(io.realm.com_realmModel_CarWashCartDataRealmProxy.copyOrUpdate(realm, columnInfo, (com.realmModel.CarWashCartData) obj, update, cache, flags));
        }
        if (clazz.equals(com.realmModel.Options.class)) {
            com_realmModel_OptionsRealmProxy.OptionsColumnInfo columnInfo = (com_realmModel_OptionsRealmProxy.OptionsColumnInfo) realm.getSchema().getColumnInfo(com.realmModel.Options.class);
            return clazz.cast(io.realm.com_realmModel_OptionsRealmProxy.copyOrUpdate(realm, columnInfo, (com.realmModel.Options) obj, update, cache, flags));
        }
        if (clazz.equals(com.realmModel.Topping.class)) {
            com_realmModel_ToppingRealmProxy.ToppingColumnInfo columnInfo = (com_realmModel_ToppingRealmProxy.ToppingColumnInfo) realm.getSchema().getColumnInfo(com.realmModel.Topping.class);
            return clazz.cast(io.realm.com_realmModel_ToppingRealmProxy.copyOrUpdate(realm, columnInfo, (com.realmModel.Topping) obj, update, cache, flags));
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public void insert(Realm realm, RealmModel object, Map<RealmModel, Long> cache) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<RealmModel> clazz = (Class<RealmModel>) ((object instanceof RealmObjectProxy) ? object.getClass().getSuperclass() : object.getClass());

        if (clazz.equals(com.adapter.files.CategoryListItem.class)) {
            io.realm.com_adapter_files_CategoryListItemRealmProxy.insert(realm, (com.adapter.files.CategoryListItem) object, cache);
        } else if (clazz.equals(com.realmModel.Cart.class)) {
            io.realm.com_realmModel_CartRealmProxy.insert(realm, (com.realmModel.Cart) object, cache);
        } else if (clazz.equals(com.realmModel.CarWashCartData.class)) {
            io.realm.com_realmModel_CarWashCartDataRealmProxy.insert(realm, (com.realmModel.CarWashCartData) object, cache);
        } else if (clazz.equals(com.realmModel.Options.class)) {
            io.realm.com_realmModel_OptionsRealmProxy.insert(realm, (com.realmModel.Options) object, cache);
        } else if (clazz.equals(com.realmModel.Topping.class)) {
            io.realm.com_realmModel_ToppingRealmProxy.insert(realm, (com.realmModel.Topping) object, cache);
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public void insert(Realm realm, Collection<? extends RealmModel> objects) {
        Iterator<? extends RealmModel> iterator = objects.iterator();
        RealmModel object = null;
        Map<RealmModel, Long> cache = new HashMap<RealmModel, Long>(objects.size());
        if (iterator.hasNext()) {
            //  access the first element to figure out the clazz for the routing below
            object = iterator.next();
            // This cast is correct because obj is either
            // generated by RealmProxy or the original type extending directly from RealmObject
            @SuppressWarnings("unchecked") Class<RealmModel> clazz = (Class<RealmModel>) ((object instanceof RealmObjectProxy) ? object.getClass().getSuperclass() : object.getClass());

            if (clazz.equals(com.adapter.files.CategoryListItem.class)) {
                io.realm.com_adapter_files_CategoryListItemRealmProxy.insert(realm, (com.adapter.files.CategoryListItem) object, cache);
            } else if (clazz.equals(com.realmModel.Cart.class)) {
                io.realm.com_realmModel_CartRealmProxy.insert(realm, (com.realmModel.Cart) object, cache);
            } else if (clazz.equals(com.realmModel.CarWashCartData.class)) {
                io.realm.com_realmModel_CarWashCartDataRealmProxy.insert(realm, (com.realmModel.CarWashCartData) object, cache);
            } else if (clazz.equals(com.realmModel.Options.class)) {
                io.realm.com_realmModel_OptionsRealmProxy.insert(realm, (com.realmModel.Options) object, cache);
            } else if (clazz.equals(com.realmModel.Topping.class)) {
                io.realm.com_realmModel_ToppingRealmProxy.insert(realm, (com.realmModel.Topping) object, cache);
            } else {
                throw getMissingProxyClassException(clazz);
            }
            if (iterator.hasNext()) {
                if (clazz.equals(com.adapter.files.CategoryListItem.class)) {
                    io.realm.com_adapter_files_CategoryListItemRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.realmModel.Cart.class)) {
                    io.realm.com_realmModel_CartRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.realmModel.CarWashCartData.class)) {
                    io.realm.com_realmModel_CarWashCartDataRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.realmModel.Options.class)) {
                    io.realm.com_realmModel_OptionsRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.realmModel.Topping.class)) {
                    io.realm.com_realmModel_ToppingRealmProxy.insert(realm, iterator, cache);
                } else {
                    throw getMissingProxyClassException(clazz);
                }
            }
        }
    }

    @Override
    public void insertOrUpdate(Realm realm, RealmModel obj, Map<RealmModel, Long> cache) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<RealmModel> clazz = (Class<RealmModel>) ((obj instanceof RealmObjectProxy) ? obj.getClass().getSuperclass() : obj.getClass());

        if (clazz.equals(com.adapter.files.CategoryListItem.class)) {
            io.realm.com_adapter_files_CategoryListItemRealmProxy.insertOrUpdate(realm, (com.adapter.files.CategoryListItem) obj, cache);
        } else if (clazz.equals(com.realmModel.Cart.class)) {
            io.realm.com_realmModel_CartRealmProxy.insertOrUpdate(realm, (com.realmModel.Cart) obj, cache);
        } else if (clazz.equals(com.realmModel.CarWashCartData.class)) {
            io.realm.com_realmModel_CarWashCartDataRealmProxy.insertOrUpdate(realm, (com.realmModel.CarWashCartData) obj, cache);
        } else if (clazz.equals(com.realmModel.Options.class)) {
            io.realm.com_realmModel_OptionsRealmProxy.insertOrUpdate(realm, (com.realmModel.Options) obj, cache);
        } else if (clazz.equals(com.realmModel.Topping.class)) {
            io.realm.com_realmModel_ToppingRealmProxy.insertOrUpdate(realm, (com.realmModel.Topping) obj, cache);
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public void insertOrUpdate(Realm realm, Collection<? extends RealmModel> objects) {
        Iterator<? extends RealmModel> iterator = objects.iterator();
        RealmModel object = null;
        Map<RealmModel, Long> cache = new HashMap<RealmModel, Long>(objects.size());
        if (iterator.hasNext()) {
            //  access the first element to figure out the clazz for the routing below
            object = iterator.next();
            // This cast is correct because obj is either
            // generated by RealmProxy or the original type extending directly from RealmObject
            @SuppressWarnings("unchecked") Class<RealmModel> clazz = (Class<RealmModel>) ((object instanceof RealmObjectProxy) ? object.getClass().getSuperclass() : object.getClass());

            if (clazz.equals(com.adapter.files.CategoryListItem.class)) {
                io.realm.com_adapter_files_CategoryListItemRealmProxy.insertOrUpdate(realm, (com.adapter.files.CategoryListItem) object, cache);
            } else if (clazz.equals(com.realmModel.Cart.class)) {
                io.realm.com_realmModel_CartRealmProxy.insertOrUpdate(realm, (com.realmModel.Cart) object, cache);
            } else if (clazz.equals(com.realmModel.CarWashCartData.class)) {
                io.realm.com_realmModel_CarWashCartDataRealmProxy.insertOrUpdate(realm, (com.realmModel.CarWashCartData) object, cache);
            } else if (clazz.equals(com.realmModel.Options.class)) {
                io.realm.com_realmModel_OptionsRealmProxy.insertOrUpdate(realm, (com.realmModel.Options) object, cache);
            } else if (clazz.equals(com.realmModel.Topping.class)) {
                io.realm.com_realmModel_ToppingRealmProxy.insertOrUpdate(realm, (com.realmModel.Topping) object, cache);
            } else {
                throw getMissingProxyClassException(clazz);
            }
            if (iterator.hasNext()) {
                if (clazz.equals(com.adapter.files.CategoryListItem.class)) {
                    io.realm.com_adapter_files_CategoryListItemRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.realmModel.Cart.class)) {
                    io.realm.com_realmModel_CartRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.realmModel.CarWashCartData.class)) {
                    io.realm.com_realmModel_CarWashCartDataRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.realmModel.Options.class)) {
                    io.realm.com_realmModel_OptionsRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.realmModel.Topping.class)) {
                    io.realm.com_realmModel_ToppingRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else {
                    throw getMissingProxyClassException(clazz);
                }
            }
        }
    }

    @Override
    public <E extends RealmModel> E createOrUpdateUsingJsonObject(Class<E> clazz, Realm realm, JSONObject json, boolean update)
        throws JSONException {
        checkClass(clazz);

        if (clazz.equals(com.adapter.files.CategoryListItem.class)) {
            return clazz.cast(io.realm.com_adapter_files_CategoryListItemRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.realmModel.Cart.class)) {
            return clazz.cast(io.realm.com_realmModel_CartRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.realmModel.CarWashCartData.class)) {
            return clazz.cast(io.realm.com_realmModel_CarWashCartDataRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.realmModel.Options.class)) {
            return clazz.cast(io.realm.com_realmModel_OptionsRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.realmModel.Topping.class)) {
            return clazz.cast(io.realm.com_realmModel_ToppingRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public <E extends RealmModel> E createUsingJsonStream(Class<E> clazz, Realm realm, JsonReader reader)
        throws IOException {
        checkClass(clazz);

        if (clazz.equals(com.adapter.files.CategoryListItem.class)) {
            return clazz.cast(io.realm.com_adapter_files_CategoryListItemRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.realmModel.Cart.class)) {
            return clazz.cast(io.realm.com_realmModel_CartRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.realmModel.CarWashCartData.class)) {
            return clazz.cast(io.realm.com_realmModel_CarWashCartDataRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.realmModel.Options.class)) {
            return clazz.cast(io.realm.com_realmModel_OptionsRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.realmModel.Topping.class)) {
            return clazz.cast(io.realm.com_realmModel_ToppingRealmProxy.createUsingJsonStream(realm, reader));
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public <E extends RealmModel> E createDetachedCopy(E realmObject, int maxDepth, Map<RealmModel, RealmObjectProxy.CacheData<RealmModel>> cache) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<E> clazz = (Class<E>) realmObject.getClass().getSuperclass();

        if (clazz.equals(com.adapter.files.CategoryListItem.class)) {
            return clazz.cast(io.realm.com_adapter_files_CategoryListItemRealmProxy.createDetachedCopy((com.adapter.files.CategoryListItem) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.realmModel.Cart.class)) {
            return clazz.cast(io.realm.com_realmModel_CartRealmProxy.createDetachedCopy((com.realmModel.Cart) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.realmModel.CarWashCartData.class)) {
            return clazz.cast(io.realm.com_realmModel_CarWashCartDataRealmProxy.createDetachedCopy((com.realmModel.CarWashCartData) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.realmModel.Options.class)) {
            return clazz.cast(io.realm.com_realmModel_OptionsRealmProxy.createDetachedCopy((com.realmModel.Options) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.realmModel.Topping.class)) {
            return clazz.cast(io.realm.com_realmModel_ToppingRealmProxy.createDetachedCopy((com.realmModel.Topping) realmObject, 0, maxDepth, cache));
        }
        throw getMissingProxyClassException(clazz);
    }

}
