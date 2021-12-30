package com.viewholder;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melevicarbrasil.usuario.R;
import com.adapter.files.deliverAll.ExpandableRecyclerAdapter;
import com.model.ParentListItem;
import com.model.RestaurantCataChildModel;
import com.model.RestaurantCataParentModel;

import java.util.List;

public class BiodataExpandable extends ExpandableRecyclerAdapter<RestaurntCataParentViewHolder, RestaurntCataChildViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    ChildItemClickListener itemChildClickListener;

    // public List<RestaurantCataParentModel> cataParentModelList;


    public BiodataExpandable(@NonNull List<? extends ParentListItem> parentItemList, Context mContext) {
        super(parentItemList);
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        // this.cataParentModelList = (List<RestaurantCataParentModel>) parentItemList;
    }

    @Override
    public RestaurntCataParentViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View view = mLayoutInflater.inflate(R.layout.item_list_parent, parentViewGroup, false);
        return new RestaurntCataParentViewHolder(view);
    }

    @Override
    public RestaurntCataChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View view = mLayoutInflater.inflate(R.layout.item_list_child, childViewGroup, false);
        return new RestaurntCataChildViewHolder(view, mContext, itemChildClickListener);
    }

    @Override
    public void onBindParentViewHolder(RestaurntCataParentViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
        RestaurantCataParentModel head = (RestaurantCataParentModel) parentListItem;
        parentViewHolder.bind(head);
    }

    @Override
    public void onBindChildViewHolder(RestaurntCataChildViewHolder childViewHolder, int position, Object childListItem) {
        RestaurantCataChildModel child = (RestaurantCataChildModel) childListItem;
        childViewHolder.bind(child);
    }


    public void onChildClickListener(ChildItemClickListener itemChildClickListener) {
        this.itemChildClickListener = itemChildClickListener;
    }

    public interface ChildItemClickListener {
        void setOnClick(int position, RestaurantCataChildModel restaurantCataChildModel);


    }


}
