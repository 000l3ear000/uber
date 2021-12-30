package com.melevicarbrasil.usuario;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.adapter.files.GalleryImagesRecyclerAdapter;
import com.melevicarbrasil.usuario.deliverAll.CheckOutActivity;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.ImageFilePath;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.general.files.UploadProfileImage;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

public class PrescriptionActivity extends AppCompatActivity implements GalleryImagesRecyclerAdapter.OnItemClickListener {
    GeneralFunctions generalFunc;
    ImageView backImgView;
    MTextView titleTxt;
    MTextView noteTxt, noDescTxt;
    ImageView rightImgView;
    RecyclerView imageListRecyclerView;
    MButton btn_type2, btn_type2_confirm;
    GalleryImagesRecyclerAdapter adapter;
    ArrayList<HashMap<String, String>> listData;
    AppCompatImageView noImgView;
    ProgressBar loading_images;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "Temp";
    private static final int SELECT_PICTURE = 2;
    private static final int SELECT_HISTROY_IMAGE = 4;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private Uri fileUri;
    //MTextView attechTxt;
    private String selectedImagePath = "";
    private String pathForCameraImage = "";

    View carouselContainerView;
    CarouselView carouselView;
    MTextView closeCarouselTxtView;
    LinearLayout confirmBtnArea;
    String iImageId = "";
    LinearLayout bottomarea;
    MTextView skipTxt;
    LinearLayout btn2Area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        backImgView = (ImageView) findViewById(R.id.backImgView);
        rightImgView = (ImageView) findViewById(R.id.rightImgView);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        // attechTxt = (MTextView) findViewById(R.id.attechTxt);
        carouselContainerView = findViewById(R.id.carouselContainerView);
        carouselView = (CarouselView) findViewById(R.id.carouselView);
        noteTxt = (MTextView) findViewById(R.id.noteTxt);
        noDescTxt = (MTextView) findViewById(R.id.noDescTxt);
        closeCarouselTxtView = (MTextView) findViewById(R.id.closeCarouselTxtView);
        imageListRecyclerView = (RecyclerView) findViewById(R.id.imageListRecyclerView);
        confirmBtnArea = (LinearLayout) findViewById(R.id.confirmBtnArea);
        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();
        btn_type2_confirm = ((MaterialRippleLayout) findViewById(R.id.btn_type2_confirm)).getChildView();
        btn_type2_confirm.setOnClickListener(new setOnClick());
        bottomarea = (LinearLayout) findViewById(R.id.bottomarea);
        skipTxt = (MTextView) findViewById(R.id.skipTxt);
        // skipTxt.setPaintFlags(skipTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btn2Area = (LinearLayout) findViewById(R.id.btn2Area);
        skipTxt.setOnClickListener(new setOnClick());


        btn_type2.setId(Utils.generateViewId());
        noImgView = (AppCompatImageView) findViewById(R.id.noImgView);
        loading_images = (ProgressBar) findViewById(R.id.loading_images);
        btn_type2.setOnClickListener(new setOnClick());
        //  attechTxt.setOnClickListener(new setOnClick());


        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_DONE"));
        noteTxt.setText(generalFunc.retrieveLangLBl("", "LBL_GALLERY_IMG_NOTE"));
        backImgView.setOnClickListener(new setOnClick());
        rightImgView.setOnClickListener(new setOnClick());
        closeCarouselTxtView.setOnClickListener(new setOnClick());
        closeCarouselTxtView.setText(generalFunc.retrieveLangLBl("", "LBL_CLOSE_TXT"));
        titleTxt.setText(generalFunc.retrieveLangLBl("", "Prescription"));
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_ATTACH_PRESCRIPTION"));
        //  attechTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ATTACH_PRESCRIPTION"));
        noDescTxt.setText(generalFunc.retrieveLangLBl("", "LBL_NOPRESCRIPTION"));
        noteTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PRESCRIPTION_BODY_TEXT"));
        btn_type2_confirm.setText(generalFunc.retrieveLangLBl("", "LBL_CONFIRM_TXT"));
        skipTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SKIP_TXT"));


        rightImgView.setVisibility(View.GONE);

        if (getIntent().getBooleanExtra("isOrder", false)) {
            listData = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("imageList");
            adapter = new GalleryImagesRecyclerAdapter(getActContext(), listData, generalFunc, false, false, false);
            GridLayoutManager gridLay = new GridLayoutManager(getActContext(), adapter.getNumOfColumns());
            imageListRecyclerView.setLayoutManager(gridLay);
            imageListRecyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(this);
            noteTxt.setVisibility(View.GONE);
            bottomarea.setVisibility(View.GONE);
            skipTxt.setVisibility(View.GONE);


        } else {
            skipTxt.setVisibility(View.VISIBLE);
            listData = new ArrayList<>();
            adapter = new GalleryImagesRecyclerAdapter(getActContext(), listData, generalFunc, false, true, false);
            GridLayoutManager gridLay = new GridLayoutManager(getActContext(), adapter.getNumOfColumns());
            imageListRecyclerView.setLayoutManager(gridLay);
            imageListRecyclerView.setAdapter(adapter);
            imageListRecyclerView.setClipToPadding(false);
            adapter.setOnItemClickListener(this);
            getImages();
        }


        if (getIntent().getBooleanExtra("isBack", false)) {
            skipTxt.setVisibility(View.GONE);
        }


    }


    public void handleImgUploadResponse(String responseString) {

        if (responseString != null && !responseString.equals("")) {

            boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

            if (isDataAvail) {
                getImages();
//
//                generalFunc.showGeneralMessage("",
//                        generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
            } else {
                generalFunc.showGeneralMessage("",
                        generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
            }
        } else {
            generalFunc.showError();
        }
    }

    private void getImages() {
        loading_images.setVisibility(View.VISIBLE);
        noImgView.setVisibility(View.GONE);
        skipTxt.setVisibility(View.GONE);
        noDescTxt.setVisibility(View.GONE);
        btn2Area.setVisibility(View.GONE);
        confirmBtnArea.setVisibility(View.VISIBLE);
        //  attechTxt.setVisibility(View.VISIBLE);
        listData.clear();

        adapter.notifyDataSetChanged();

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "getPrescriptionImages");
        parameters.put("UserType", Utils.app_type);
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("iServiceId", generalFunc.getServiceId());


        //parameters.put("SelectedCabType", Utils.CabGeneralType_UberX);

        ExecuteWebServerUrl exeServerUrl = new ExecuteWebServerUrl(getActContext(), parameters);
        exeServerUrl.setLoaderConfig(getActContext(), false, generalFunc);
        exeServerUrl.setDataResponseListener(responseString -> {
            if (responseString != null && !responseString.equalsIgnoreCase("")) {
                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {
                    listData.clear();

                    JSONArray arr_data = generalFunc.getJsonArray(Utils.message_str, responseString);

                    if (arr_data != null) {
                        HashMap<String, String> mapData1 = new HashMap<>();
                        mapData1.put("add", "add");
                        listData.add(mapData1);

                        for (int i = 0; i < arr_data.length(); i++) {
                            JSONObject obj_tmp = generalFunc.getJsonObject(arr_data, i);

                            HashMap<String, String> mapData = new HashMap<>();
                            Iterator<String> keysItr = obj_tmp.keys();
                            while (keysItr.hasNext()) {
                                String key = keysItr.next();
                                String value = generalFunc.getJsonValueStr(key, obj_tmp);

                                mapData.put(key, value);
                            }
                            mapData.put("isDelete", "Yes");
                            listData.add(mapData);
                        }
                    }

                    adapter.notifyDataSetChanged();


                    if (listData.size() == 0) {
                        btn2Area.setVisibility(View.VISIBLE);
                        confirmBtnArea.setVisibility(View.GONE);
                        //  attechTxt.setVisibility(View.GONE);
                        noDescTxt.setVisibility(View.VISIBLE);
                        noImgView.setVisibility(View.VISIBLE);
                        skipTxt.setVisibility(View.VISIBLE);
                    }

                } else {
                    btn2Area.setVisibility(View.VISIBLE);
                    confirmBtnArea.setVisibility(View.GONE);
                    //  attechTxt.setVisibility(View.GONE);
                    noDescTxt.setVisibility(View.VISIBLE);
                    noImgView.setVisibility(View.VISIBLE);
                    skipTxt.setVisibility(View.VISIBLE);
                }

            } else {
                generalFunc.showError(true);
            }

            loading_images.setVisibility(View.GONE);
        });
        exeServerUrl.execute();
    }


    @Override
    public void onItemClickList(View v, int position) {

        if (position == 0 && !getIntent().getBooleanExtra("isOrder", false)) {
            if (generalFunc.isCameraStoragePermissionGranted()) {
                new PrescriptionActivity.ImageSourceDialog().run();
            } else {
                generalFunc.showMessage(getCurrView(), "Allow this app to use camera.");
            }
        } else {
            carouselContainerView.setVisibility(View.VISIBLE);
            carouselView.setViewListener(viewListener);
            carouselView.setPageCount(listData.size() - 1);
            carouselView.setCurrentItem(position);
        }
    }

    @Override
    public void onLongItemClickList(View v, int position) {

    }


    androidx.appcompat.app.AlertDialog alertDialog;

    @Override
    public void onDeleteClick(View v, int position) {
       /* generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_DELETE_IMG_CONFIRM_PRESCEIPTION_NOTE"), generalFunc.retrieveLangLBl("", "LBL_NO"), generalFunc.retrieveLangLBl("", "LBL_YES"), buttonId -> {

            if (buttonId == 1) {
                selectedImagePath = "";
                configPrescriptionImage(listData.get(position).get("iImageId"), "DELETE");
            }

        });

       */

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("");

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.delete_precription, null);
        dialogView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        builder.setView(dialogView);


        //inflateviews
        MTextView messageTxt = dialogView.findViewById(R.id.messageTxt);
        MTextView cancelTxt = dialogView.findViewById(R.id.cancelTxt);
        MTextView submitTxt = dialogView.findViewById(R.id.submitTxt);

        messageTxt.setText(generalFunc.retrieveLangLBl("", "LBL_DELETE_IMG_CONFIRM_PRESCEIPTION_NOTE"));
        cancelTxt.setOnClickListener(view -> {
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
        });
        submitTxt.setOnClickListener(view -> {
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
            selectedImagePath = "";
            configPrescriptionImage(listData.get(position).get("iImageId"), "DELETE");
        });


        alertDialog = builder.create();
        if (generalFunc.isRTLmode()) {
            generalFunc.forceRTLIfSupported(alertDialog);
        }
        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        alertDialog.show();
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

        try {
            Picasso.get()
                    .load(Utilities.getResizeImgURL(getActContext(), item.get("vImage"), ((int) Utils.getScreenPixelWidth(getActContext())) - Utils.dipToPixels(getActContext(), 30), 0, Utils.getScreenPixelHeight(getActContext()) - Utils.dipToPixels(getActContext(), 30)))
                    .placeholder(R.mipmap.ic_no_icon).error(R.mipmap.ic_no_icon)
                    .into(customView, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customView;
    };

    public Context getActContext() {
        return PrescriptionActivity.this;
    }

    public class setOnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == R.id.backImgView) {
                PrescriptionActivity.super.onBackPressed();
            } else if (i == R.id.rightImgView) {


            } else if (i == btn_type2.getId()) {
                if (generalFunc.isCameraStoragePermissionGranted()) {
                    new PrescriptionActivity.ImageSourceDialog().run();
                } else {
                    generalFunc.showMessage(getCurrView(), "Allow this app to use camera.");
                }

            } else if (i == btn_type2_confirm.getId()) {

                if (getIntent().getBooleanExtra("isBack", false)) {
                    onBackPressed();
                    return;
                }
                Bundle bn = new Bundle();
                bn.putBoolean("isFromEditCard", getIntent().getBooleanExtra("isFromEditCard", false));
                bn.putBoolean("isPrescription", true);
                new StartActProcess(getActContext()).startActWithData(CheckOutActivity.class, bn);
                finish();


            } else if (i == closeCarouselTxtView.getId()) {
                if (carouselContainerView.getVisibility() == View.VISIBLE) {
                    carouselContainerView.setVisibility(View.GONE);
                }
            } else if (i == skipTxt.getId()) {
                Bundle bn = new Bundle();
                bn.putBoolean("isFromEditCard", getIntent().getBooleanExtra("isFromEditCard", false));
                bn.putBoolean("isPrescription", true);
                new StartActProcess(getActContext()).startActWithData(CheckOutActivity.class, bn);
                finish();


            }
        }
    }

    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    public void chooseFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

    }

    public void chooseFromCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public View getCurrView() {
        return generalFunc.getCurrentView(PrescriptionActivity.this);
    }


    public Uri getOutputMediaFileUri(int type) {
//        return Uri.fromFile(getOutputMediaFile(type));

        return FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", getOutputMediaFile(type));
    }

    private File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
            pathForCameraImage = mediaFile.getAbsolutePath();
        } else {
            return null;
        }

        return mediaFile;
    }

    class ImageSourceDialog implements Runnable {

        @Override
        public void run() {


            final Dialog dialog_img_update = new Dialog(getActContext(), R.style.ImageSourceDialogStyle);

            dialog_img_update.setContentView(R.layout.design_prescription_image_source_select);

            //  MTextView chooseImgHTxt = (MTextView) dialog_img_update.findViewById(R.id.chooseImgHTxt);
            MTextView cameraTxt = (MTextView) dialog_img_update.findViewById(R.id.cameraTxt);
            MTextView galleryTxt = (MTextView) dialog_img_update.findViewById(R.id.galleryTxt);
            MTextView prescriptionTxt = (MTextView) dialog_img_update.findViewById(R.id.prescriptionTxt);
            LinearLayout cameraView = (LinearLayout) dialog_img_update.findViewById(R.id.cameraView);
            LinearLayout galleryView = (LinearLayout) dialog_img_update.findViewById(R.id.galleryView);
            LinearLayout prescriptionArea = (LinearLayout) dialog_img_update.findViewById(R.id.prescriptionArea);
            MButton btn_type2 = ((MaterialRippleLayout) dialog_img_update.findViewById(R.id.btn_type2)).getChildView();
            btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));
            cameraTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CAMERA"));
            galleryTxt.setText(generalFunc.retrieveLangLBl("", "LBL_GALLERY"));
            prescriptionTxt.setText(generalFunc.retrieveLangLBl("Prescriptions Upload by You", "LBL_PRESCRIPTION_UPLOADED_BY_YOU"));


            // ImageView closeDialogImgView = (ImageView) dialog_img_update.findViewById(R.id.closeDialogImgView);

            btn_type2.setOnClickListener(v -> {
                if (dialog_img_update != null) {
                    dialog_img_update.cancel();
                }
            });


            cameraView.setOnClickListener(v -> {

                if (dialog_img_update != null) {
                    dialog_img_update.cancel();
                }

                if (!isDeviceSupportCamera()) {
                    generalFunc.showMessage(getCurrView(), generalFunc.retrieveLangLBl("", "LBL_NOT_SUPPORT_CAMERA_TXT"));
                } else {
                    chooseFromCamera();
                }

            });

            galleryView.setOnClickListener(v -> {

                if (dialog_img_update != null) {
                    dialog_img_update.cancel();
                }

                chooseFromGallery();
            });

            prescriptionArea.setOnClickListener(v -> {

                if (dialog_img_update != null) {
                    dialog_img_update.cancel();
                }

                Bundle bn = new Bundle();
                // bn.putBoolean("isFromEditCard", true);
                new StartActProcess(getActContext()).startActForResult(PrescriptionHistoryImagesActivity.class, bn, SELECT_HISTROY_IMAGE);
            });

            dialog_img_update.setCanceledOnTouchOutside(true);

            Window window = dialog_img_update.getWindow();
            window.setGravity(Gravity.BOTTOM);

            window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            dialog_img_update.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            if (generalFunc.isRTLmode()) {
                dialog_img_update.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }

            dialog_img_update.show();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {

            if (pathForCameraImage.equalsIgnoreCase("")) {
                selectedImagePath = new ImageFilePath().getPath(getActContext(), fileUri);
            } else {
                selectedImagePath = pathForCameraImage;
            }

            if (selectedImagePath == null || selectedImagePath.equalsIgnoreCase("")) {
                selectedImagePath = "";

                generalFunc.showMessage(generalFunc.getCurrentView((Activity) getActContext()), generalFunc.retrieveLangLBl("Can't read selected image. Please try again.", "LBL_IMAGE_READ_FAILED"));
                return;
            }


            if (selectedImagePath == null || selectedImagePath.equalsIgnoreCase("")) {
                selectedImagePath = "";

                generalFunc.showMessage(generalFunc.getCurrentView((Activity) getActContext()), generalFunc.retrieveLangLBl("Can't read selected image. Please try again.", "LBL_IMAGE_READ_FAILED"));
                return;
            }

            configPrescriptionImage("", "ADD");
        } else if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {

            Uri selectedImageUri = data.getData();

            selectedImagePath = ImageFilePath.getPath(getApplicationContext(), selectedImageUri);

            if (selectedImagePath == null || selectedImagePath.equalsIgnoreCase("")) {
                selectedImagePath = "";

                generalFunc.showMessage(generalFunc.getCurrentView((Activity) getActContext()), generalFunc.retrieveLangLBl("Can't read selected image. Please try again.", "LBL_IMAGE_READ_FAILED"));
                return;
            }

            configPrescriptionImage("", "ADD");
        } else if (requestCode == SELECT_HISTROY_IMAGE && resultCode == RESULT_OK) {
            iImageId = data.getStringExtra("iImageId");

            getPreviouslyUploadedbyYou();


        }

    }


    private void getPreviouslyUploadedbyYou() {


        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "PreviouslyUploadedbyYou");
        parameters.put("UserType", Utils.app_type);
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("iServiceId", generalFunc.getServiceId());


        parameters.put("iImageId", iImageId);

        //parameters.put("SelectedCabType", Utils.CabGeneralType_UberX);

        ExecuteWebServerUrl exeServerUrl = new ExecuteWebServerUrl(getActContext(), parameters);
        exeServerUrl.setLoaderConfig(getActContext(), false, generalFunc);
        exeServerUrl.setDataResponseListener(responseString -> {
            if (responseString != null && !responseString.equalsIgnoreCase("")) {
                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {

                    getImages();

                } else {
                    generalFunc.showError(true);
                }

            }
        });
        exeServerUrl.execute();
    }

    private void configPrescriptionImage(String iImageId, String action_type) {

        ArrayList<String[]> paramsList = new ArrayList<>();
        paramsList.add(generalFunc.generateImageParams("type", "PrescriptionImages"));
        paramsList.add(generalFunc.generateImageParams("iUserId", generalFunc.getMemberId()));
        paramsList.add(generalFunc.generateImageParams("UserType", Utils.app_type));
        paramsList.add(generalFunc.generateImageParams("action_type", action_type));
        paramsList.add(generalFunc.generateImageParams("iImageId", iImageId));

        paramsList.add(generalFunc.generateImageParams("iMemberId", generalFunc.getMemberId()));
        paramsList.add(generalFunc.generateImageParams("tSessionId", generalFunc.getMemberId().equals("") ? "" : generalFunc.retrieveValue(Utils.SESSION_ID_KEY)));
        paramsList.add(generalFunc.generateImageParams("GeneralUserType", Utils.app_type));
        paramsList.add(generalFunc.generateImageParams("GeneralMemberId", generalFunc.getMemberId()));
        // paramsList.add(generalFunc.generateImageParams("iServiceId", generalFunc.getServiceId()));
        paramsList.add(generalFunc.generateImageParams("iServiceId", generalFunc.getServiceId()));

        new UploadProfileImage(PrescriptionActivity.this, selectedImagePath, Utils.TempProfileImageName, paramsList).execute();

    }


}
