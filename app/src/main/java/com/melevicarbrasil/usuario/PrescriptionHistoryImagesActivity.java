package com.melevicarbrasil.usuario;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.adapter.files.GalleryImagesRecyclerAdapter;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.squareup.picasso.Picasso;
import com.utils.Utilities;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.carouselview.CarouselView;
import com.view.carouselview.ViewListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PrescriptionHistoryImagesActivity extends AppCompatActivity implements GalleryImagesRecyclerAdapter.OnItemClickListener {
    GeneralFunctions generalFunc;
    ImageView backImgView;
    MTextView titleTxt;
    MTextView noteTxt, noDescTxt;
    ArrayList<HashMap<String, String>> listData = new ArrayList<>();
    AppCompatImageView noImgView;
    ProgressBar loading_images;

    RecyclerView imageListRecyclerView;
    GalleryImagesRecyclerAdapter adapter;
    LinearLayout confirmBtnArea;
    MButton btn_type2_confirm;

    View carouselContainerView;
    CarouselView carouselView;
    MTextView closeCarouselTxtView;
    ArrayList<String> imageIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_history_images);
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        backImgView = (ImageView) findViewById(R.id.backImgView);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        confirmBtnArea = (LinearLayout) findViewById(R.id.confirmBtnArea);
        btn_type2_confirm = ((MaterialRippleLayout) findViewById(R.id.btn_type2_confirm)).getChildView();
        btn_type2_confirm.setOnClickListener(new setOnClick());

        noteTxt = (MTextView) findViewById(R.id.noteTxt);
        noDescTxt = (MTextView) findViewById(R.id.noDescTxt);
        noImgView = (AppCompatImageView) findViewById(R.id.noImgView);
        imageListRecyclerView = (RecyclerView) findViewById(R.id.imageListRecyclerView);
        loading_images = (ProgressBar) findViewById(R.id.loading_images);
        carouselContainerView = findViewById(R.id.carouselContainerView);
        carouselView = (CarouselView) findViewById(R.id.carouselView);
        closeCarouselTxtView = (MTextView) findViewById(R.id.closeCarouselTxtView);
        closeCarouselTxtView.setOnClickListener(new setOnClick());

        adapter = new GalleryImagesRecyclerAdapter(getActContext(), listData, generalFunc, false, false, true);

        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PRESCRIPTION_HISTORY"));
        noDescTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PRESCRIPTION_HISTORY_NOREPORT"));
        noteTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PRESCRIPTION_HISTORY_NOTE"));
        btn_type2_confirm.setText(generalFunc.retrieveLangLBl("", "LBL_CONFIRM_TXT"));
        closeCarouselTxtView.setText(generalFunc.retrieveLangLBl("", "LBL_CLOSE_TXT"));


        GridLayoutManager gridLay = new GridLayoutManager(getActContext(), adapter.getNumOfColumns());

        imageListRecyclerView.setLayoutManager(gridLay);
        adapter.setOnItemClickListener(this);
        imageListRecyclerView.setAdapter(adapter);
        backImgView.setOnClickListener(new setOnClick());
        getImages();

    }

    public Context getActContext() {
        return PrescriptionHistoryImagesActivity.this;
    }


    @Override
    public void onItemClickList(View v, int position) {
        carouselContainerView.setVisibility(View.VISIBLE);
        carouselView.setViewListener(viewListener);
        carouselView.setPageCount(listData.size());
        carouselView.setCurrentItem(position);


    }

    ViewListener viewListener = position -> {
        ImageView customView = new ImageView(getActContext());

        CarouselView.LayoutParams layParams = new CarouselView.LayoutParams(CarouselView.LayoutParams.MATCH_PARENT, CarouselView.LayoutParams.MATCH_PARENT);
//        layParams.leftMargin = Utils.dipToPixels(getActContext(), 15);
//        layParams.rightMargin = Utils.dipToPixels(getActContext(), 15);
        customView.setLayoutParams(layParams);

        customView.setPadding(Utils.dipToPixels(getActContext(), 15), 0, Utils.dipToPixels(getActContext(), 15), 0);
        customView.setImageResource(R.mipmap.ic_no_icon);

        final HashMap<String, String> item = listData.get(position);

        Picasso.get()
                .load(Utilities.getResizeImgURL(getActContext(), item.get("vImage"), ((int) Utils.getScreenPixelWidth(getActContext())) - Utils.dipToPixels(getActContext(), 30), 0, Utils.getScreenPixelHeight(getActContext()) - Utils.dipToPixels(getActContext(), 30)))
                .placeholder(R.mipmap.ic_no_icon).error(R.mipmap.ic_no_icon)
                .into(customView, null);

        return customView;
    };

    @Override
    public void onLongItemClickList(View v, int position) {
        HashMap<String, String> map = listData.get(position);
        map.put("isSel", "Yes");
        listData.set(position, map);
        imageIdList.add(listData.get(position).get("iImageId"));
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDeleteClick(View v, int position) {
        HashMap<String, String> map = listData.get(position);
        map.put("isSel", "No");
        listData.set(position, map);
        adapter.notifyDataSetChanged();
        while (imageIdList.remove(listData.get(position).get("iImageId"))) {
        }


    }

    public class setOnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == R.id.backImgView) {
                PrescriptionHistoryImagesActivity.super.onBackPressed();
            } else if (i == btn_type2_confirm.getId()) {

                if (imageIdList.size() == 0) {
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_SELECT_IMAGE_ERROR"));
                    return;
                }
                Bundle bn = new Bundle();
                bn.putString("iImageId", android.text.TextUtils.join(",", imageIdList));
                (new StartActProcess(getActContext())).setOkResult(bn);
                finish();

            } else if (i == closeCarouselTxtView.getId()) {
                if (carouselContainerView.getVisibility() == View.VISIBLE) {
                    carouselContainerView.setVisibility(View.GONE);
                }
            }
        }
    }

    private void getImages() {
        loading_images.setVisibility(View.VISIBLE);
        noImgView.setVisibility(View.GONE);
        noDescTxt.setVisibility(View.GONE);
        noteTxt.setVisibility(View.VISIBLE);
        confirmBtnArea.setVisibility(View.GONE);


        listData.clear();

        adapter.notifyDataSetChanged();

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "getPrescriptionImages");
        parameters.put("UserType", Utils.app_type);
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("iServiceId", generalFunc.getServiceId());
        parameters.put("PreviouslyUploaded", "1");


        ExecuteWebServerUrl exeServerUrl = new ExecuteWebServerUrl(getActContext(), parameters);
        exeServerUrl.setLoaderConfig(getActContext(), false, generalFunc);
        exeServerUrl.setDataResponseListener(responseString -> {
            if (responseString != null && !responseString.equalsIgnoreCase("")) {
                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);
                confirmBtnArea.setVisibility(View.VISIBLE);
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
                            mapData.put("isSel", "No");
                            listData.add(mapData);
                        }
                    }

                    adapter.notifyDataSetChanged();


                    if (listData.size() == 0) {

                        noDescTxt.setVisibility(View.VISIBLE);
                        noImgView.setVisibility(View.VISIBLE);
                        noteTxt.setVisibility(View.VISIBLE);
                        confirmBtnArea.setVisibility(View.GONE);

                    }

                } else {

                    noDescTxt.setVisibility(View.VISIBLE);
                    noImgView.setVisibility(View.VISIBLE);
                    noteTxt.setVisibility(View.VISIBLE);
                    confirmBtnArea.setVisibility(View.GONE);

                }

            } else {
                generalFunc.showError(true);
            }

            loading_images.setVisibility(View.GONE);
        });
        exeServerUrl.execute();
    }
}
