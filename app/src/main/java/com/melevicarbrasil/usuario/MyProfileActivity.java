package com.melevicarbrasil.usuario;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import com.google.android.material.appbar.AppBarLayout;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cropper.CropImage;
import com.cropper.CropImageView;
import com.fragments.EditProfileFragment;
import com.fragments.ProfileFragment;
import com.general.files.AppFunctions;
import com.general.files.GeneralFunctions;
import com.general.files.ImageFilePath;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.general.files.UploadProfileImage;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.SelectableRoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MyProfileActivity extends AppCompatActivity {

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "Temp";
    private static final int SELECT_PICTURE = 2;
    private static final int CROP_IMAGE = 3;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public GeneralFunctions generalFunc;
    public String userProfileJson = "";
    public String isDriverAssigned = "";
    //ImageView profileback;
    public boolean isEdit = false;
    public boolean isMobile = false;
    public boolean isEmail = false;
    MTextView titleTxt;
    ImageView backImgView;
    SelectableRoundedImageView userProfileImgView;
    SelectableRoundedImageView editIconImgView;

    ProfileFragment profileFrag;
    EditProfileFragment editProfileFrag;
    FrameLayout userImgArea;
    String SITE_TYPE = "";
    String SITE_TYPE_DEMO_MSG = "";
    Menu menu;
    androidx.appcompat.app.AlertDialog alertDialog;
    private Uri fileUri;
    AppBarLayout appBarLayoutMain;


    public static Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(c);
        return bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_profile);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(mToolbar);
        //appBarLayoutMain = (AppBarLayout) findViewById(R.id.app_bar_layout);
        // appBarLayoutMain.setOutlineProvider(null);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());

        //userProfileJson = getIntent().getStringExtra("UserProfileJson");
        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        isDriverAssigned = getIntent().getStringExtra("isDriverAssigned");
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        isMobile = getIntent().getBooleanExtra("isMobile", false);
        isEmail = getIntent().getBooleanExtra("isEmail", false);

        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        userProfileImgView = (SelectableRoundedImageView) findViewById(R.id.userProfileImgView);

        editIconImgView = (SelectableRoundedImageView) findViewById(R.id.editIconImgView);
        userImgArea = (FrameLayout) findViewById(R.id.userImgArea);

        backImgView.setOnClickListener(new setOnClickList());
        userImgArea.setOnClickListener(new setOnClickList());


        new CreateRoundedView(getResources().getColor(R.color.white), Utils.dipToPixels(getActContext(), getResources().getDimension(R.dimen._15sdp)), 0,
                Color.parseColor("#00000000"), editIconImgView);

        editIconImgView.setColorFilter(getResources().getColor(R.color.appThemeColor_1));

        userProfileImgView.setImageResource(R.mipmap.ic_no_pic_user);

        (new AppFunctions(getActContext())).checkProfileImage(userProfileImgView, userProfileJson, "vImgName", null);
        String vImgName_str = generalFunc.getJsonValue("vImgName", userProfileJson);

        if (vImgName_str == null || vImgName_str.equals("") || vImgName_str.equals("NONE")) {
            editIconImgView.setImageResource(R.drawable.ic_pic_add);

        }


        SITE_TYPE = generalFunc.getJsonValue("SITE_TYPE", userProfileJson);
        SITE_TYPE_DEMO_MSG = generalFunc.getJsonValue("SITE_TYPE_DEMO_MSG", userProfileJson);


        //  if (isEdit) {
        openEditProfileFragment();
//        } else {
//            openProfileFragment();
//        }
    }

    public void changePageTitle(String title) {
        titleTxt.setText(title);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case GeneralFunctions.MY_PERMISSIONS_REQUEST: {
                if (generalFunc.isPermisionGranted()) {
                    new ImageSourceDialog().run();
                }
                break;

            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {

            // perform your desired action here

            // return 'true' to prevent further propagation of the key event
            return true;
        }

        // let the system handle all other key events
        return super.onKeyDown(keyCode, event);
    }


    public Context getActContext() {
        return MyProfileActivity.this;
    }


    public void openEditProfileFragment() {

        if (editProfileFrag != null) {
            editProfileFrag = null;
            Utils.runGC();
        }
        editProfileFrag = new EditProfileFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragContainer, editProfileFrag).commit();
    }


    public EditProfileFragment getEditProfileFrag() {
        return this.editProfileFrag;
    }

    public ProfileFragment getProfileFrag() {
        return this.profileFrag;
    }

    public void changeUserProfileJson(String userProfileJson) {
        this.userProfileJson = userProfileJson;

        Bundle bn = new Bundle();
        //bn.putString("UserProfileJson", userProfileJson);

        generalFunc.storeData(Utils.WALLET_ENABLE, generalFunc.getJsonValue("WALLET_ENABLE", userProfileJson));
        generalFunc.storeData(Utils.REFERRAL_SCHEME_ENABLE, generalFunc.getJsonValue("REFERRAL_SCHEME_ENABLE", userProfileJson));


        new StartActProcess(getActContext()).setOkResult(bn);


        generalFunc.showMessage(getCurrView(), generalFunc.retrieveLangLBl("", "LBL_INFO_UPDATED_TXT"));
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the rider's current state
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
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
        } else {
            return null;
        }


        return mediaFile;
    }

    @Override
    public void onBackPressed() {


        super.onBackPressed();
    }

    public View getCurrView() {
        return generalFunc.getCurrentView(MyProfileActivity.this);
    }

    public boolean isValidImageResolution(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, options);
        int width = options.outWidth;
        int height = options.outHeight;

        if (width >= Utils.ImageUpload_MINIMUM_WIDTH && height >= Utils.ImageUpload_MINIMUM_HEIGHT) {
            return true;
        }
        return false;
    }

    public String[] generateImageParams(String key, String content) {
        String[] tempArr = new String[2];
        tempArr[0] = key;
        tempArr[1] = content;

        return tempArr;
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE || requestCode == SELECT_PICTURE || requestCode == CROP_IMAGE) {
            if (resultCode == RESULT_OK) {

                if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                    // successfully captured the image
                    // display it in image view
//                    fileUri = Uri.parse(fileUriFilePath);
                    try {
                        cropImage(fileUri, fileUri);

                    } catch (Exception e) {
                        if (fileUri != null) {
//                            generalFunc.showMessage(getCurrView(), generalFunc.retrieveLangLBl("Some problem occurred.can't able to get cropped image.so we are uploading original captured image.", "LBL_CROP_ERROR_TXT"));
                            imageUpload(fileUri);
                        } else if (data != null) {
//                            generalFunc.showMessage(getCurrView(), generalFunc.retrieveLangLBl("Some problem occurred.can't able to get cropped image.so we are uploading original captured image.", "LBL_CROP_ERROR_TXT"));
                            imageUpload(data.getData());
                        } else {
                            generalFunc.showMessage(getCurrView(), generalFunc.retrieveLangLBl("", "LBL_ERROR_OCCURED"));

                        }
                        e.printStackTrace();
                    }

                } else if (requestCode == SELECT_PICTURE) {

                    try {
                        Uri cropPictureUrl = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
                        String realPathFromURI = new ImageFilePath().getPath(getActContext(), data.getData());
                        File file = new File(realPathFromURI == null ? getImageUrlWithAuthority(this, data.getData()) : realPathFromURI);

                        if (file == null || realPathFromURI == null || realPathFromURI.equalsIgnoreCase("")) {
                            generalFunc.showMessage(generalFunc.getCurrentView((Activity) getActContext()), generalFunc.retrieveLangLBl("Can't read selected image. Please try again.", "LBL_IMAGE_READ_FAILED"));
                            return;
                        }

                        if (file.exists()) {
                            if (Build.VERSION.SDK_INT > 23) {
                                cropImage(FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file), cropPictureUrl);
                            } else {
                                cropImage(Uri.fromFile(file), cropPictureUrl);
                            }

                        } else {
                            cropImage(data.getData(), cropPictureUrl);
                        }

                    } catch (Exception e) {
                        if (data != null) {
//                            generalFunc.showMessage(getCurrView(), generalFunc.retrieveLangLBl("Some problem occurred.can't able to get cropped image.so we are uploading original captured image.", "LBL_CROP_ERROR_TXT"));
                            imageUpload(data.getData());
                        } else {
                            generalFunc.showMessage(getCurrView(), generalFunc.retrieveLangLBl("", "LBL_ERROR_OCCURED"));

                        }
                        e.printStackTrace();
                    }
                } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri resultUri = result.getUri();
                    imageUpload(resultUri);
                } else if (requestCode == CROP_IMAGE) {

                    imageUpload(fileUri);
                }
            } else {
                if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                    generalFunc.showMessage(getCurrView(), generalFunc.retrieveLangLBl("", "LBL_FAILED_CAPTURE_IMAGE_TXT"));
                } else {
                    generalFunc.showMessage(getCurrView(), generalFunc.retrieveLangLBl("", "LBL_ERROR_OCCURED"));
                }
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri resultUri = result.getUri();
            imageUpload(resultUri);
        }
    }

    public static String getImageUrlWithAuthority(Context context, Uri uri) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                return writeToTempImageAndGetPathUri(context, bmp).toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Uri writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, Utils.TempProfileImageName, null);
        return Uri.parse(path);
    }

    private void cropImage(final Uri sourceImage, Uri destinationImage) {

        try {
            CropImage.activity(sourceImage)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setMultiTouchEnabled(false)
                    .setDoneButtonText(generalFunc.retrieveLangLBl("Done", "LBL_DONE"))
                    .setCancelButtonText(generalFunc.retrieveLangLBl("Cancel", "LBL_CANCEL_TXT"))
                    .setAspectRatio(1024, 1024)
                    .setNoOutputImage(false)
                    .start(this);
        } catch (Exception e) {
            imageUpload(sourceImage);
        }
    }


    private void imageUpload(Uri fileUri) {
        if (SITE_TYPE.equalsIgnoreCase("Demo") && generalFunc.getJsonValue("vEmail", generalFunc.retrieveValue(Utils.USER_PROFILE_JSON)).equalsIgnoreCase("Driver@gmail.com")) {
            generalFunc.showGeneralMessage("", SITE_TYPE_DEMO_MSG);
            return;
        }

        if (fileUri == null) {
            generalFunc.showMessage(getCurrView(), generalFunc.retrieveLangLBl("", "LBL_ERROR_OCCURED"));
            return;
        }

        ArrayList<String[]> paramsList = new ArrayList<>();
        paramsList.add(generateImageParams("iMemberId", generalFunc.getMemberId()));
        paramsList.add(generateImageParams("tSessionId", generalFunc.getMemberId().equals("") ? "" : generalFunc.retrieveValue(Utils.SESSION_ID_KEY)));
        paramsList.add(generateImageParams("GeneralUserType", Utils.app_type));
        paramsList.add(generateImageParams("GeneralMemberId", generalFunc.getMemberId()));
        paramsList.add(generateImageParams("type", "uploadImage"));

        String selectedImagePath = new ImageFilePath().getPath(getActContext(), fileUri);

        boolean isStoragePermissionAvail = generalFunc.isStoragePermissionGranted();

        if (isValidImageResolution(selectedImagePath) && isStoragePermissionAvail) {

            new UploadProfileImage(MyProfileActivity.this, selectedImagePath, Utils.TempProfileImageName, paramsList).execute();
        } else {
            generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("Please select image which has minimum is 256 * 256 resolution.", "LBL_MIN_RES_IMAGE"));
        }

    }

    public void handleImgUploadResponse(String responseString) {

        if (responseString != null && !responseString.equals("")) {

            boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

            if (isDataAvail) {
                generalFunc.storeData(Utils.USER_PROFILE_JSON, generalFunc.getJsonValue(Utils.message_str, responseString));
                changeUserProfileJson(generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));
                String vImgName_str = generalFunc.getJsonValue("vImage", userProfileJson);

                if (vImgName_str == null || vImgName_str.equals("") || vImgName_str.equals("NONE")) {
                    editIconImgView.setImageResource(R.drawable.ic_pic_add);
                } else {
                    editIconImgView.setImageResource(R.mipmap.ic_edit);

                }

                (new AppFunctions(getActContext())).checkProfileImage(userProfileImgView, userProfileJson, "vImgName", null);

            } else {
                generalFunc.showGeneralMessage("",
                        generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
            }
        } else {
            generalFunc.showError();
        }
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {


        super.onOptionsMenuClosed(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        return super.onPrepareOptionsMenu(menu);
    }


    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            switch (view.getId()) {
                case R.id.backImgView:

                    MyProfileActivity.super.onBackPressed();
                    break;

                case R.id.userImgArea:
                    if (generalFunc.isCameraStoragePermissionGranted()) {
                        new ImageSourceDialog().run();
                    } else {
                        generalFunc.showMessage(getCurrView(), "Allow this app to use camera.");
                    }

                    break;

            }
        }
    }


    class ImageSourceDialog implements Runnable {

        @Override
        public void run() {
            
            final Dialog dialog_img_update = new Dialog(getActContext(), R.style.ImageSourceDialogStyle);
            dialog_img_update.setContentView(R.layout.design_image_source_select);
            MTextView cameraTxt = (MTextView) dialog_img_update.findViewById(R.id.cameraTxt);
            MTextView galleryTxt = (MTextView) dialog_img_update.findViewById(R.id.galleryTxt);
            LinearLayout cameraView = (LinearLayout) dialog_img_update.findViewById(R.id.cameraView);
            LinearLayout galleryView = (LinearLayout) dialog_img_update.findViewById(R.id.galleryView);

            MButton btn_type2 = ((MaterialRippleLayout) dialog_img_update.findViewById(R.id.btn_type2)).getChildView();
            btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));

            cameraTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CAMERA"));
            galleryTxt.setText(generalFunc.retrieveLangLBl("", "LBL_GALLERY"));

            SelectableRoundedImageView cameraIconImgView = (SelectableRoundedImageView) dialog_img_update.findViewById(R.id.cameraIconImgView);
            SelectableRoundedImageView galleryIconImgView = (SelectableRoundedImageView) dialog_img_update.findViewById(R.id.galleryIconImgView);

            ImageView closeDialogImgView = (ImageView) dialog_img_update.findViewById(R.id.closeDialogImgView);

            closeDialogImgView.setOnClickListener(v -> {
                if (dialog_img_update != null) {
                    dialog_img_update.cancel();
                }
            });

            btn_type2.setOnClickListener(view -> dialog_img_update.dismiss());




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
}
