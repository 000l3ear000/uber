package com.melevicarbrasil.usuario;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.GetAddressFromLocation;
import com.general.files.GetLocationUpdates;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.editBox.MaterialEditText;

import java.util.Calendar;
import java.util.HashMap;
import com.google.android.gms.maps.model.MapStyleOptions;
public class AddAddressActivity extends AppCompatActivity implements OnMapReadyCallback, GetLocationUpdates.LocationUpdates, GetAddressFromLocation.AddressFound, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener {

    private static final String TAG = AddAddressActivity.class.getSimpleName();

    GeneralFunctions generalFunc;
    ImageView backImgView;
    MTextView titleTxt;

    MaterialEditText buildingBox;
    MaterialEditText landmarkBox;
    MaterialEditText addrtypeBox;
    MaterialEditText apartmentLocNameBox;
    MaterialEditText companyBox;
    MaterialEditText postCodeBox;
    MaterialEditText addr2Box;
    MaterialEditText deliveryIntructionBox;
    MaterialEditText vContryBox;

    MTextView locationImage;
    String addresslatitude;
    String addresslongitude;
    String address;
    String tempAddress;
    MTextView locAddrTxtView;
    MTextView serviceAddrHederTxtView;
    //    MTextView AddrareaTxtView;
    MButton btn_type2;
    LinearLayout loc_area;
    LinearLayout content_add_address;

    String required_str = "";
    String type = "";
    String iUserAddressId;
    String quantity = "0";
    String SelectedVehicleTypeId = "";
    boolean isclick = false;

    String iCompanyId;
    GetAddressFromLocation getAddressFromLocation;

    public SupportMapFragment map;
    GoogleMap gMap;
    ImageView pinImgView;
    boolean isPlaceSelected = false;
    LatLng placeLocation;
    public boolean isAddressEnable = false;
    private boolean isFirstLocation = true;
    GetLocationUpdates getLastLocation;
    private Location userLocation;
    private AddAddressActivity listener;
    boolean isBlockIdle = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        quantity = getIntent().getStringExtra("Quantity");
        SelectedVehicleTypeId = getIntent().getStringExtra("SelectedVehicleTypeId");

        backImgView = (ImageView) findViewById(R.id.backImgView);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        loc_area = (LinearLayout) findViewById(R.id.loc_area);
        content_add_address = (LinearLayout) findViewById(R.id.content_add_address);

        pinImgView = (ImageView) findViewById(R.id.pinImgView);

        buildingBox = (MaterialEditText) findViewById(R.id.buildingBox);
        landmarkBox = (MaterialEditText) findViewById(R.id.landmarkBox);
        addrtypeBox = (MaterialEditText) findViewById(R.id.addrtypeBox);
        apartmentLocNameBox = (MaterialEditText) findViewById(R.id.apartmentLocNameBox);
        companyBox = (MaterialEditText) findViewById(R.id.companyBox);
        postCodeBox = (MaterialEditText) findViewById(R.id.postCodeBox);
        addr2Box = (MaterialEditText) findViewById(R.id.addr2Box);
        deliveryIntructionBox = (MaterialEditText) findViewById(R.id.deliveryIntructionBox);
        vContryBox = (MaterialEditText) findViewById(R.id.vContryBox);
        locationImage = (MTextView) findViewById(R.id.locationImage);
        locAddrTxtView = (MTextView) findViewById(R.id.locAddrTxtView);
        serviceAddrHederTxtView = (MTextView) findViewById(R.id.serviceAddrHederTxtView);
//        AddrareaTxtView = (MTextView) findViewById(R.id.AddrareaTxtView);
        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();
        btn_type2.setOnClickListener(new setOnClick());
        loc_area.setOnClickListener(new setOnClick());

        locAddrTxtView.setText(address);

        backImgView.setOnClickListener(new setOnClick());
        locationImage.setOnClickListener(new setOnClick());
        setLabel();

        if (getIntent().hasExtra("iCompanyId")) {
            iCompanyId = getIntent().getStringExtra("iCompanyId");
        }

        map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapV2);
        getAddressFromLocation = new GetAddressFromLocation(getActContext(), generalFunc);
        getAddressFromLocation.setAddressList(this);

        map.getMapAsync(this);

        /*if (gMap != null) {
            gMap.setPadding(0, 0, 0, Utils.dipToPixels(getActContext(), content_add_address.getHeight()));
        }*/
    }

    public void releaseResources() {
        setGoogleMapCameraListener(null);
        this.gMap = null;
        getAddressFromLocation.setAddressList(null);
        getAddressFromLocation = null;
    }

    public void setGoogleMapCameraListener(AddAddressActivity act) {
        listener = act;
        this.gMap.setOnCameraMoveStartedListener(act);
        this.gMap.setOnCameraIdleListener(act);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Maps dias e noches inicio by gabriel
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if(hourOfDay >= 06 && hourOfDay < 18){
//googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.ub__map_style_pedro));
        } else {
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.ub__map_style_noite_pedro));
        }
// Maps dias e noches fim by gabriel

        this.gMap = googleMap;
        setGoogleMapCameraListener(this);

        getLastLocation = new GetLocationUpdates(getActContext(), Utils.LOCATION_UPDATE_MIN_DISTANCE_IN_MITERS, false, this);


        getLocationLatLng(true);
        gMap.getUiSettings().setCompassEnabled(false);

    }

    @Override
    protected void onDestroy() {
        if (getLastLocation != null) {
            getLastLocation.stopLocationUpdates();
        }
        releaseResources();
        super.onDestroy();
    }

    @Override
    public void onLocationUpdate(Location location) {
        if (location == null) {
            return;
        }

        if (isFirstLocation) {
            LatLng placeLocation = getLocationLatLng(true);

            if (isAddressEnable && listener == null) {
                setGoogleMapCameraListener(this);
            }
            pinImgView.setVisibility(View.VISIBLE);
            if (placeLocation != null) {
                setCameraPosition(new LatLng(placeLocation.latitude, placeLocation.longitude));
            } else {
                setCameraPosition(new LatLng(location.getLatitude(), location.getLongitude()));
            }

            pinImgView.setVisibility(View.VISIBLE);
            isFirstLocation = false;
        }

        userLocation = location;
    }

    private void setCameraPosition(LatLng location) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(location.latitude,
                        location.longitude))
                .zoom(Utils.defaultZomLevel).build();
        gMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    private LatLng getLocationLatLng(boolean setText) {
        LatLng placeLocation = null;

        String CURRENT_ADDRESS = generalFunc.retrieveValue(Utils.CURRENT_ADDRESSS);

        if (getIntent().hasExtra("iCompanyId") && CURRENT_ADDRESS != null && !CURRENT_ADDRESS.equalsIgnoreCase("")) {
            address = CURRENT_ADDRESS;
            addresslatitude = generalFunc.retrieveValue(Utils.CURRENT_LATITUDE);
            addresslongitude = generalFunc.retrieveValue(Utils.CURRENT_LONGITUDE);
            placeLocation = new LatLng(generalFunc.parseDoubleValue(0.0, addresslatitude),
                    generalFunc.parseDoubleValue(0.0, addresslongitude));

            if (iCompanyId != null && iCompanyId.equalsIgnoreCase("-1")) {
                addresslatitude = getIntent().getStringExtra("latitude");
                addresslongitude = getIntent().getStringExtra("longitude");
                address = getIntent().getStringExtra("address");
            }
            isAddressEnable = true;
            pinImgView.setVisibility(View.VISIBLE);
            locAddrTxtView.setText(address);
        } /*else if (getIntent().hasExtra("latitude") && getIntent().hasExtra("longitude") && setText && getIntent().hasExtra("address")) {

            isAddressEnable = true;
            placeLocation = new LatLng(generalFunc.parseDoubleValue(0.0, getIntent().getStringExtra("latitude")),
                    generalFunc.parseDoubleValue(0.0, getIntent().getStringExtra("longitude")));

            pinImgView.setVisibility(View.VISIBLE);
            address = getIntent().getStringExtra("address");
            locAddrTxtView.setText("" + address);
        }*/ else if (userLocation != null) {
            placeLocation = new LatLng(generalFunc.parseDoubleValue(0.0, "" + userLocation.getLatitude()),
                    generalFunc.parseDoubleValue(0.0, "" + userLocation.getLongitude()));

        } else {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager = (LocationManager) getSystemService
                    (Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return placeLocation;
            }
            Location getLastLocation = locationManager.getLastKnownLocation
                    (LocationManager.PASSIVE_PROVIDER);
            if (getLastLocation != null) {
                LatLng UserLocation = new LatLng(generalFunc.parseDoubleValue(0.0, "" + getLastLocation.getLatitude()),
                        generalFunc.parseDoubleValue(0.0, "" + getLastLocation.getLongitude()));
                if (UserLocation != null) {
                    placeLocation = UserLocation;
                }
            }
        }


        return placeLocation;
    }

    private void setLabel() {
        titleTxt.setText(generalFunc.retrieveLangLBl("Add New Address", "LBL_ADD_NEW_ADDRESS_TXT"));
        buildingBox.setBothText(generalFunc.retrieveLangLBl("Building/House/Flat No.", "LBL_JOB_LOCATION_HINT_INFO"));
        landmarkBox.setBothText(generalFunc.retrieveLangLBl("Landmark(e.g hospital,park etc.)", "LBL_LANDMARK_HINT_INFO"));
        addrtypeBox.setBothText(generalFunc.retrieveLangLBl("Nickname(optional-home,office etc.)", " LBL_ADDRESSTYPE_HINT_INFO"));
        serviceAddrHederTxtView.setText(generalFunc.retrieveLangLBl("Service address", "LBL_SERVICE_ADDRESS_HINT_INFO"));
//        AddrareaTxtView.setText(generalFunc.retrieveLangLBl("Area of service", "LBL_AREA_SERVICE_HINT_INFO"));
        btn_type2.setText(generalFunc.retrieveLangLBl("Save", "LBL_SAVE_ADDRESS_TXT"));
        required_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_REQUIRD");
        locationImage.setText(generalFunc.retrieveLangLBl("", "LBL_CHANGE"));
        locAddrTxtView.setText(generalFunc.retrieveLangLBl("", "LBL_SET_STORE_LOCATION"));
    }

    public void checkValues() {
        boolean buildingDataenterd = Utils.checkText(buildingBox) ? true
                : Utils.setErrorFields(buildingBox, required_str);
        boolean landmarkDataenterd = Utils.checkText(landmarkBox) ? true
                : Utils.setErrorFields(landmarkBox, required_str);

        if (buildingDataenterd == false || landmarkDataenterd == false) {
            return;
        }

        if (!getIntent().hasExtra("iCompanyId")) {
            if (addresslatitude.equalsIgnoreCase("") || addresslatitude.equalsIgnoreCase("0.0") || addresslongitude.equalsIgnoreCase("") || addresslongitude.equalsIgnoreCase("0.0")) {
                generalFunc.showMessage(btn_type2, generalFunc.retrieveLangLBl("", "LBL_SET_LOCATION"));

                return;
            }
        }
//        if (!isclick) {
//            isclick = true;
        addDeliveryAddr();
//        }
    }

    private void addDeliveryAddr() {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "UpdateUserAddressDetails");
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("eUserType", Utils.app_type);
        parameters.put("vServiceAddress", address);
        parameters.put("vBuildingNo", Utils.getText(buildingBox));
        parameters.put("vLandmark", Utils.getText(landmarkBox));
        parameters.put("vAddressType", Utils.getText(addrtypeBox));
        parameters.put("vLatitude", addresslatitude);
        parameters.put("vLongitude", addresslongitude);

        if (getIntent().hasExtra("iCompanyId")) {
            parameters.put("iCompanyId", iCompanyId);
        } else {
            parameters.put("iUserAddressId", "");
            parameters.put("iSelectVehicalId", "");
            parameters.put("iSelectVehicalId", SelectedVehicleTypeId);
        }

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);

        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {
                    if (gMap != null) {
                        gMap.setPadding(0, 0, 0, 0);
                    }
                    btn_type2.setText(generalFunc.retrieveLangLBl("add Location", "LBL_ADD_LOC"));

                    if (getIntent().hasExtra("iCompanyId")) {
                        generalFunc.storeData(Utils.USER_PROFILE_JSON, generalFunc.getJsonValue(Utils.message_str, responseString));
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("ToTalAddress", "1");
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();

                    } else {

                        String userprofileJson = "";


                        generalFunc.storeData(Utils.USER_PROFILE_JSON, generalFunc.getJsonValue(Utils.message_str, responseString));

                        userprofileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);


                        String IsProceed = generalFunc.getJsonValue("IsProceed", responseString);

                        if (IsProceed.equalsIgnoreCase("No")) {

                            final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                            generateAlert.setCancelable(false);
                            generateAlert.setBtnClickList(btn_id -> {
                                generateAlert.closeAlertBox();
                                new StartActProcess(getActContext()).setOkResult();
                                backImgView.performClick();

                            });
                            generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("Job Location not allowed", "LBL_JOB_LOCATION_NOT_ALLOWED"));
                            generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("Ok", "LBL_BTN_OK_TXT"));

                            generateAlert.showAlertBox();
                            return;
                        }

                        iUserAddressId = generalFunc.getJsonValue("AddressId", responseString);

                        if (type.equals(Utils.CabReqType_Later)) {


                            if (generalFunc.getJsonValue("ToTalAddress", userprofileJson).equals("1")) {
                                Bundle bundle = new Bundle();
                                bundle.putString("latitude", addresslatitude);
                                bundle.putString("longitude", addresslongitude);
                                bundle.putString("address", address);
                                bundle.putString("iUserAddressId", iUserAddressId);
                                bundle.putString("SelectvVehicleType", getIntent().getStringExtra("SelectvVehicleType"));
                                bundle.putString("SelectvVehiclePrice", getIntent().getStringExtra("SelectvVehiclePrice"));
                                bundle.putString("iUserAddressId", iUserAddressId);
                                bundle.putString("Quantityprice", getIntent().getStringExtra("Quantityprice"));
                                bundle.putString("Quantity", quantity);
                                bundle.putString("SelectedVehicleTypeId", SelectedVehicleTypeId);
                                bundle.putBoolean("isWalletShow", getIntent().getBooleanExtra("isWalletShow", false));
                                new StartActProcess(getActContext()).startActWithData(ScheduleDateSelectActivity.class, bundle);

                                finish();
                            } else {
                                final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                                generateAlert.setCancelable(false);
                                generateAlert.setBtnClickList(btn_id -> {
                                    generateAlert.closeAlertBox();
                                    new StartActProcess(getActContext()).setOkResult();
                                    backImgView.performClick();

                                });
                                generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str_one, responseString)));
                                generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("Ok", "LBL_BTN_OK_TXT"));

                                generateAlert.showAlertBox();
                            }

                        } else {


                            if (generalFunc.getJsonValue("ToTalAddress", userprofileJson).equals("1")) {
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("isufx", true);
                                bundle.putString("latitude", addresslatitude);
                                bundle.putString("longitude", addresslongitude);
                                bundle.putString("address", address);
                                bundle.putString("SelectvVehicleType", getIntent().getStringExtra("SelectvVehicleType"));
                                bundle.putString("SelectvVehiclePrice", getIntent().getStringExtra("SelectvVehiclePrice"));
                                bundle.putString("type", Utils.CabReqType_Now);
                                bundle.putString("iUserAddressId", iUserAddressId);
                                bundle.putString("Quantity", quantity);
                                bundle.putString("Quantityprice", getIntent().getStringExtra("Quantityprice"));
                                bundle.putString("SelectedVehicleTypeId", SelectedVehicleTypeId);
                                bundle.putString("Sdate", "");
                                bundle.putString("Stime", "");
                                bundle.putBoolean("isWalletShow", getIntent().getBooleanExtra("isWalletShow", false));
                                new StartActProcess(getActContext()).startActWithData(MainActivity.class, bundle);
                                finish();
                            } else {
                                final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                                generateAlert.setCancelable(false);
                                generateAlert.setBtnClickList(btn_id -> {
                                    generateAlert.closeAlertBox();


                                    new StartActProcess(getActContext()).setOkResult();
                                    backImgView.performClick();

                                });
                                generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str_one, responseString)));
                                generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("Ok", "LBL_BTN_OK_TXT"));

                                generateAlert.showAlertBox();
                            }
                        }
                    }

                } else {
                    generalFunc.showGeneralMessage("",
                            generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                }
            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }

    public Context getActContext() {
        return AddAddressActivity.this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utils.SEARCH_PICKUP_LOC_REQ_CODE && resultCode == RESULT_OK && data != null) {
            Place place = PlaceAutocomplete.getPlace(getActContext(), data);
             placeLocation = place.getLatLng();
            locAddrTxtView.setText(place.getAddress().toString());
            addresslatitude = placeLocation.latitude + "";
            addresslongitude = placeLocation.longitude + "";
            address = place.getAddress().toString();
            if (placeLocation != null) {
                isBlockIdle = true;
                setCameraPosition(new LatLng(placeLocation.latitude, placeLocation.longitude));
                pinImgView.setVisibility(View.VISIBLE);
            }
        } else if (requestCode == Utils.UBER_X_SEARCH_PICKUP_LOC_REQ_CODE && resultCode == RESULT_OK && data != null) {
            address = data.getStringExtra("Address");
            locAddrTxtView.setText(address);
            addresslatitude = data.getStringExtra("Latitude") == null ? "0.0" : data.getStringExtra("Latitude");
            addresslongitude = data.getStringExtra("Longitude") == null ? "0.0" : data.getStringExtra("Longitude");
            placeLocation = new LatLng(GeneralFunctions.parseDoubleValue(0.0, addresslatitude),
                                        GeneralFunctions.parseDoubleValue(0.0, addresslongitude));
            if (placeLocation != null) {
                isBlockIdle = true;
                setCameraPosition(new LatLng(placeLocation.latitude, placeLocation.longitude));
                pinImgView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onAddressFound(String address, double latitude, double longitude, String geocodeobject) {
        locAddrTxtView.setText(address);
        this.address = address;
        isPlaceSelected = true;
        this.placeLocation = new LatLng(latitude, longitude);

        addresslatitude = latitude + "";
        addresslongitude = longitude + "";

        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(this.placeLocation, 14.0f);
        if (gMap != null) {
            gMap.clear();
            if (isFirstLocation) {
                gMap.moveCamera(cu);
            }
            isFirstLocation = false;
            setGoogleMapCameraListener(this);
        }

    }

    @Override
    public void onCameraMoveStarted(int i) {
        if (pinImgView.getVisibility() == View.VISIBLE) {
            if (!isAddressEnable) {
                locAddrTxtView.setText(generalFunc.retrieveLangLBl("", "LBL_SELECTING_LOCATION_TXT"));
            }
        }

    }


    @Override
    public void onCameraIdle() {


        if (getAddressFromLocation == null|| isBlockIdle || pinImgView.getVisibility() == View.GONE) {
            isBlockIdle = false;
            return;
        }


        LatLng center = null;
        if (gMap != null && gMap.getCameraPosition() != null) {
            center = gMap.getCameraPosition().target;
        }

        if (center == null) {
            return;
        }


        if (!isAddressEnable) {
            setGoogleMapCameraListener(null);
            getAddressFromLocation.setLocation(center.latitude, center.longitude);
            getAddressFromLocation.setLoaderEnable(true);
            getAddressFromLocation.execute();
        } else {
            isAddressEnable = false;
        }
    }

    public class setOnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == R.id.backImgView) {
                AddAddressActivity.super.onBackPressed();
            } else if (i == R.id.loc_area) {

                Bundle bn = new Bundle();
                bn.putString("locationArea", "source");
                bn.putBoolean("isaddressview", true);
                if (getIntent().hasExtra("iCompanyId")) {
                    bn.putString("eSystem", Utils.eSystem_Type);
                }

                new StartActProcess(getActContext()).startActForResult(SearchLocationActivity.class, bn, Utils.UBER_X_SEARCH_PICKUP_LOC_REQ_CODE);

            } else if (i == locationImage.getId()) {
                loc_area.performClick();
            } else if (i == btn_type2.getId()) {
                if (Utils.checkText(address)) {
                    checkValues();

                } else {
                    generalFunc.showMessage(backImgView, generalFunc.retrieveLangLBl("", "LBL_SELECT_ADDRESS_TITLE_TXT"));

                }

            }
        }
    }
}
