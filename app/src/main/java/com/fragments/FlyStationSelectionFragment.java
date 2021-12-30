package com.fragments;


import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.adapter.files.SkyPortsRecyclerAdapter;
import com.melevicarbrasil.usuario.MainActivity;
import com.melevicarbrasil.usuario.R;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.anim.loader.AVLoadingIndicatorView;
import com.view.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlyStationSelectionFragment extends Fragment implements SkyPortsRecyclerAdapter.OnSelectListener, ViewTreeObserver.OnGlobalLayoutListener {


    static MainActivity mainAct;
    static GeneralFunctions generalFunc;
    String userProfileJson = "";
    private View view;

    private MTextView tvTitle;
    private MTextView tvNoDetails;
    private MTextView tvSelectedAddress;
    private LinearLayout changeArea;
    private MButton btn_type2;
    public RecyclerView skyPortsListRecyclerView;
    private AVLoadingIndicatorView loaderView;
    public Location finalAddressLocation = null;
    public String finalAddress = "";
    public String finaliLocationId = "";
    public int viewHeight = 0;

    private ArrayList<HashMap<String, String>> dateList = new ArrayList<>();
    int pos = -1;

    int submitBtnId;

    public int fragmentWidth = 0;
    public int fragmentHeight = 0;
    public boolean isPickup;
    Location destLocation = null;
    String destAddress = "";
    int popupviewheight=0;
    private LinearLayout mPopupView;
    CustomLinearLayoutManager customLayoutManager;
    boolean isFirst = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null) {
            return view;
        }

        view = inflater.inflate(R.layout.design_skyports_bottom_view, container, false);
        mainAct = (MainActivity) getActivity();
        generalFunc = mainAct.generalFunc;
        isPickup = getArguments().getBoolean("isPickup", true);

        skyPortsListRecyclerView = (RecyclerView) view.findViewById(R.id.skyPortsListRecyclerView);
        skyPortsListRecyclerView.addOnItemTouchListener(mOnItemTouchListener);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;

        tvTitle = (MTextView) view.findViewById(R.id.tvTitle);
        mPopupView = (LinearLayout) view.findViewById(R.id.popupView);
        tvNoDetails = (MTextView) view.findViewById(R.id.tvNoDetails);
        tvSelectedAddress = (MTextView) view.findViewById(R.id.tvSelectedAddress);
        changeArea = (LinearLayout) view.findViewById(R.id.changeArea);
        loaderView = (AVLoadingIndicatorView) view.findViewById(R.id.loaderView);

        MTextView tvMoreStations = (MTextView) view.findViewById(R.id.tvMoreStations);
//        MTextView tvChangeAddress = (MTextView) view.findViewById(R.id.tvChangeAddress);

        btn_type2 = ((MaterialRippleLayout) view.findViewById(R.id.btn_type2)).getChildView();
        submitBtnId = Utils.generateViewId();
        btn_type2.setId(submitBtnId);

//        tvChangeAddress.setText(generalFunc.retrieveLangLBl("", "LBL_CHANGE"));
        tvMoreStations.setText(generalFunc.retrieveLangLBl("", "LBL_FLY_VIEW_MORE_STATIONS"));

        btn_type2.setOnClickListener(new setOnClickList());
        changeArea.setOnClickListener(new setOnClickList());
        viewHeight = view.getHeight();


        // Implement it's on touch listener.
        view.findViewById(R.id.swipeArea).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mainAct.enableDisableScroll(true);
                // Return false, then android os will still process click event,
                // if return true, the on click listener will never be triggered.
                return false;
            }
        });


        // Implement it's on touch listener.

        view.findViewById(R.id.dataArea).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mainAct.enableDisableScroll(false);

                // Return false, then android os will still process click event,
                // if return true, the on click listener will never be triggered.
                return false;
            }
        });

        view.findViewById(R.id.popupView).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mainAct.enableDisableScroll(false);
                // Return false, then android os will still process click event,
                // if return true, the on click listener will never be triggered.
                return false;
            }
        });



        mainAct.sliding_layout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                int cal = mainAct.staticPanelHeight - popupviewheight + ((int) ((mainAct.staticPanelHeight - (popupviewheight / 2)) * slideOffset));
                if (slideOffset > 0) {
                    mPopupView.setY(cal);
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    mainAct.enableDisableScroll(false);
                    isFirst = false;
                    if (mPopupView.getY() != (mainAct.staticPanelHeight - popupviewheight)) {
                        mPopupView.setY(mainAct.staticPanelHeight - popupviewheight);
                    }

                } else if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    mainAct.enableDisableScroll(false);
                    isFirst = true;
                    if (dateList.size() > 0) {
                        if (pos != -1 && dateList.size() - 1 < pos) {
                            customLayoutManager.smoothScrollToPosition(skyPortsListRecyclerView, null, pos);
                        } else {
                            customLayoutManager.smoothScrollToPosition(skyPortsListRecyclerView, null, 0);
                        }
                    }

                    if (mPopupView.getY() != (fragmentHeight - popupviewheight)) {
                        mPopupView.setY(fragmentHeight - popupviewheight);
                    }

                } else if (newState == SlidingUpPanelLayout.PanelState.DRAGGING) {
                    isFirst = false;
                }

                setViewPadding();
            }
        });

        reSetDetails();
        addGlobalLayoutListner();


        return view;


    }

    RecyclerView.OnItemTouchListener mOnItemTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            if (e.getAction() == MotionEvent.ACTION_DOWN && rv.getScrollState() == RecyclerView.SCROLL_STATE_SETTLING) {
              //  Log.d("SET_PICKUP", "onInterceptTouchEvent: click performed");
                rv.findChildViewUnder(e.getX(), e.getY()).performClick();
                return true;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            mainAct.enableDisableScroll(false);
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    };

    private void reSetDetails() {
        if (!isPickup && mainAct.data != null) {
            destAddress = mainAct.data.getStringExtra("Address");
            if (destLocation == null) {
                destLocation = new Location("dest");
            }
            destLocation.setLatitude(GeneralFunctions.parseDoubleValue(0.0, mainAct.data.getStringExtra("Latitude")));
            destLocation.setLongitude(GeneralFunctions.parseDoubleValue(0.0, mainAct.data.getStringExtra("Longitude")));

        }


        tvSelectedAddress.setText(!isPickup ? destAddress : mainAct.pickUpLocationAddress);

        tvTitle.setText(generalFunc.retrieveLangLBl("", !isPickup ? "LBL_FLY_DROP_STATION_TXT" : "LBL_FLY_PICKUP_STATION_TXT"));
        btn_type2.setText(generalFunc.retrieveLangLBl("", !isPickup ? "LBL_FLY_CONFIRM_LOCATION_TXT" : "LBL_FLY_CONFIRM_PICKUP_TXT"));

        getSkyPortsPoints();
    }

    public void setPickup(boolean isPickup) {
     //   Logger.d("SET_PICKUP", "-->isPickup");
        this.isPickup = isPickup;
        reSetDetails();
    }

    @Override
    public void onResume() {
        super.onResume();
        addGlobalLayoutListner();

        mPopupView.post(new Runnable() {
            @Override
            public void run() {
                popupviewheight = mPopupView.getHeight();
                setViewPadding();
            }
        });

    }

    private void setViewPadding() {

        if (mainAct.sliding_layout.getPanelState()== SlidingUpPanelLayout.PanelState.COLLAPSED)
        {
            view.findViewById(R.id.mainLayout).setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT));
            view.findViewById(R.id.dataArea).setPadding(0, 0, 0, mainAct.staticPanelHeight - popupviewheight);
            skyPortsListRecyclerView.setPadding(0, 0, 0, popupviewheight);
        }else  if (mainAct.sliding_layout.getPanelState()== SlidingUpPanelLayout.PanelState.EXPANDED)
        {
            view.findViewById(R.id.mainLayout).setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));

            view.findViewById(R.id.dataArea).setPadding(0, 0, 0, popupviewheight);
            skyPortsListRecyclerView.setPadding(0, 0, 0, 0);
        }


    }

    private void addGlobalLayoutListner() {

        if (getView() != null) {
            getView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
        if (view != null) {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }

        if (getView() != null) {

            getView().getViewTreeObserver().addOnGlobalLayoutListener(this);
        } else if (view != null) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            Utils.hideKeyboard(getActivity());
            int i = view.getId();
            if (i == btn_type2.getId()) {
                if (pos == -1) {
//                    Logger.d("Fly_CLICK", "no selected");
                    generalFunc.showMessage(skyPortsListRecyclerView, generalFunc.retrieveLangLBl("Please select any 1 skyPort.", "LBL_FLY_WARNING_MSG"));
                } else {
//                    Logger.d("Fly_CLICK", " selected");
                    String address = dateList.get(pos).get("skyPortAddress");
                    finalAddress = dateList.get(pos).get("skyPortTitle") + (Utils.checkText(address) ? (" | " + dateList.get(pos).get("skyPortAddress")) : "");
                    finaliLocationId = dateList.get(pos).get("iLocationId");

                    if (finalAddressLocation == null) {
                        finalAddressLocation = new Location(!isPickup ? "dest" : "");
                    }
                    finalAddressLocation.setLatitude(GeneralFunctions.parseDoubleValue(0.0, dateList.get(pos).get("skyPortLatitude")));
                    finalAddressLocation.setLongitude(GeneralFunctions.parseDoubleValue(0.0, dateList.get(pos).get("skyPortLongitude")));

                    if (mainAct.sliding_layout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                        mainAct.sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    }
                    mainAct.setSelectedSkyPortPoint(isPickup, finalAddressLocation, finalAddress, finaliLocationId, isPickup);
                }

            } else if (i == changeArea.getId()) {
                if (mainAct.sliding_layout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    mainAct.sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
                mainAct.changeAddress(isPickup);
            }
        }
    }

    @Override
    public void onDataSelect(int position) {
        if (mainAct.sliding_layout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            mainAct.sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
        skyPortsListRecyclerView.smoothScrollToPosition(position);
        pos = position;
        mainAct.highlightSelectedSkyPortPointMarkers(isPickup, finalAddressLocation, finalAddress, position);

    }

    public void getSkyPortsPoints() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getNearestFlyStations");
        parameters.put("lattitude", !isPickup ? "" + destLocation.getLatitude() : "" + mainAct.pickUpLocation.getLatitude());
        parameters.put("longitude", !isPickup ? "" + destLocation.getLongitude() : "" + mainAct.pickUpLocation.getLongitude());
        parameters.put("address", Utils.getText(tvSelectedAddress));

        String iLocationId = "";

        iLocationId = mainAct.iFromStationId;


        if (Utils.checkText(iLocationId)) {
            parameters.put("iLocationId", iLocationId);
        }


        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(mainAct.getActContext(), parameters);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {
                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);
                dateList.clear();
                pos = -1;
                SkyPortsRecyclerAdapter dateAdapter = new SkyPortsRecyclerAdapter(dateList, mainAct.getActContext());


                if (isDataAvail == true) {

                    JSONArray arr_sky_ports = generalFunc.getJsonArray(Utils.message_str, responseString);

                    if (arr_sky_ports != null && arr_sky_ports.length() > 0) {
                        for (int i = 0; i < arr_sky_ports.length(); i++) {
                            JSONObject obj_temp = generalFunc.getJsonObject(arr_sky_ports, i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("iLocationId", generalFunc.getJsonValueStr("iLocationId", obj_temp));
                            map.put("skyPortTitle", generalFunc.getJsonValueStr("vLocationName", obj_temp));
                            map.put("skyPortKm", generalFunc.getJsonValueStr("distance", obj_temp));
                            map.put("skyPortLatitude", generalFunc.getJsonValueStr("tCentroidLattitude", obj_temp));
                            map.put("skyPortLongitude", generalFunc.getJsonValueStr("tCentroidLongitude", obj_temp));
                            map.put("skyPortAddress", generalFunc.getJsonValueStr("vLocationAddress", obj_temp));
                            map.put("LBL_AWAY_TXT", generalFunc.retrieveLangLBl("", "LBL_AWAY_TXT"));
                            dateList.add(map);
                        }

                        mainAct.addListOfSkyPortPointMarkers(isPickup, dateList);
                        tvNoDetails.setVisibility(View.GONE);
                    }
                    dateAdapter = new SkyPortsRecyclerAdapter(dateList, mainAct.getActContext());
                    dateAdapter.setSelectedListener(this);

                    customLayoutManager = new CustomLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    skyPortsListRecyclerView.setLayoutManager(customLayoutManager);
                    skyPortsListRecyclerView.setClipToPadding(false);
                    skyPortsListRecyclerView.setAdapter(dateAdapter);
                    dateAdapter.notifyDataSetChanged();

                    loaderView.setVisibility(View.GONE);


                } else {
                    tvNoDetails.setVisibility(View.VISIBLE);

                    tvNoDetails.setText(generalFunc.retrieveLangLBl("No station found nearby to this location.", generalFunc.getJsonValue(Utils.message_str, responseString)));
                    dateAdapter.notifyDataSetChanged();
                    loaderView.setVisibility(View.GONE);
                }

            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();


    }

    @Override
    public void onGlobalLayout() {
        boolean heightChanged = false;
        if (getView() != null || view != null) {
            if (getView() != null) {

                if (getView().getHeight() != 0 && getView().getHeight() != fragmentHeight) {
                    heightChanged = true;
                }
                fragmentWidth = getView().getWidth();
                fragmentHeight = getView().getHeight();
            } else if (view != null) {

                if (view.getHeight() != 0 && view.getHeight() != fragmentHeight) {
                    heightChanged = true;
                }

                fragmentWidth = view.getWidth();
                fragmentHeight = view.getHeight();
            }

            if (!isFirst) {

                mPopupView.setY(mainAct.staticPanelHeight - popupviewheight);
                mPopupView.setVisibility(View.VISIBLE);

                if (heightChanged && fragmentWidth != 0 && fragmentHeight != 0) {

                    mainAct.setFlyPanelHeight(fragmentHeight);

                }
            }

        }
    }

    public class CustomLinearLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);

        }

        // it will always pass false to RecyclerView when calling "canScrollVertically()" method.
        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}


