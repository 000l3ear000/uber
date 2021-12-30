package com.fragments;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.adapter.files.GalleryImagesRecyclerAdapter;
import com.melevicarbrasil.usuario.MoreInfoActivity;
import com.melevicarbrasil.usuario.R;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.utils.Utils;
import com.view.MTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class GalleryFragment extends Fragment implements View.OnClickListener, GalleryImagesRecyclerAdapter.OnItemClickListener {
    View view;

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "Temp";
    private static final int SELECT_PICTURE = 2;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    RecyclerView galleryRecyclerView;


    ProgressBar loading_images;


    GeneralFunctions generalFunc;


    private String selectedImagePath = "";
    private String pathForCameraImage = "";
    private Uri fileUri;

    GalleryImagesRecyclerAdapter adapter;

    ArrayList<HashMap<String, String>> listData = new ArrayList<>();
    MoreInfoActivity moreInfoActivity;
    MTextView noDataTxt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gallery, container, false);


        moreInfoActivity = (MoreInfoActivity) getActivity();
        generalFunc = MyApp.getInstance().getGeneralFun(moreInfoActivity.getActContext());
        galleryRecyclerView = (RecyclerView) view.findViewById(R.id.galleryRecyclerView);
        loading_images = (ProgressBar) view.findViewById(R.id.loading_images);

        noDataTxt = (MTextView) view.findViewById(R.id.noDataTxt);
        noDataTxt.setText(generalFunc.retrieveLangLBl("", "LBL_NO_DATA_AVAIL"));

        adapter = new GalleryImagesRecyclerAdapter(moreInfoActivity.getActContext(), listData, generalFunc, false, false,false);
        galleryRecyclerView.setAdapter(adapter);
        galleryRecyclerView.setClipToPadding(false);

        setLabels();


        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(getResources().getColor(R.color.appThemeColor_TXT_1), PorterDuff.Mode.SRC_IN);

        Drawable mGalleryDrawable = moreInfoActivity.getActContext().getResources().getDrawable(R.mipmap.ic_gallery_fab);
        mGalleryDrawable.setColorFilter(colorFilter);

        Drawable mCameraDrawable = moreInfoActivity.getActContext().getResources().getDrawable(R.mipmap.ic_camera_fab);
        mCameraDrawable.setColorFilter(colorFilter);


        GridLayoutManager gridLay = new GridLayoutManager(getActivity(), adapter.getNumOfColumns());

        galleryRecyclerView.setLayoutManager(gridLay);

        adapter.setOnItemClickListener(this);
        getImages();

        return view;
    }

    public void setLabels() {


    }

    private void getImages() {
        loading_images.setVisibility(View.VISIBLE);
        noDataTxt.setVisibility(View.GONE);
        listData.clear();

        adapter.notifyDataSetChanged();

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "getProviderImages");
        parameters.put("UserType", Utils.app_type);
        parameters.put("iDriverId", moreInfoActivity.getIntent().getStringExtra("iDriverId"));
        parameters.put("SelectedCabType", Utils.CabGeneralType_UberX);

        ExecuteWebServerUrl exeServerUrl = new ExecuteWebServerUrl(getActivity(), parameters);
        exeServerUrl.setLoaderConfig(moreInfoActivity.getActContext(), false, generalFunc);
        exeServerUrl.setDataResponseListener(responseString -> {
            if (responseString != null && !responseString.equalsIgnoreCase("")) {
                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {
                    listData.clear();

                    JSONArray arr_data = generalFunc.getJsonArray(Utils.message_str, responseString);

                    if (arr_data != null) {
                        for (int i = 0; i < arr_data.length(); i++) {
                            JSONObject obj_tmp = generalFunc.getJsonObject(arr_data, i);

                            HashMap<String, String> mapData = new HashMap<>();
                            Iterator<String> keysItr = obj_tmp.keys();
                            while (keysItr.hasNext()) {
                                String key = keysItr.next();
                                String value = generalFunc.getJsonValueStr(key, obj_tmp);

                                mapData.put(key, value);
                            }
                            listData.add(mapData);
                        }
                    }

                    adapter.notifyDataSetChanged();


                    if (listData.size() == 0) {
                        noDataTxt.setVisibility(View.VISIBLE);
                    }

                } else {

                    noDataTxt.setVisibility(View.VISIBLE);
                }

            } else {
                generalFunc.showError(true);
            }

            loading_images.setVisibility(View.GONE);
        });
        exeServerUrl.execute();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }


    @Override
    public void onItemClickList(View v, int position) {
        moreInfoActivity.openCarouselView(listData, position);
    }

    @Override
    public void onLongItemClickList(View v, int position) {

    }

    @Override
    public void onDeleteClick(View v, int position) {

    }
}