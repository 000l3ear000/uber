package com.adapter.files;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.melevicarbrasil.usuario.MainActivity;
import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.squareup.picasso.Picasso;
import com.utils.CommonUtilities;
import com.view.CreateRoundedView;
import com.view.MTextView;
import com.view.SelectableRoundedImageView;
import com.view.anim.loader.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 04-07-2016.
 */
public class CabTypeAdapter extends RecyclerView.Adapter<CabTypeAdapter.ViewHolder> {

    public GeneralFunctions generalFunc;
    ArrayList<HashMap<String, String>> list_item;
    ArrayList<HashMap<String, String>> rental_item;
    public ArrayList<HashMap<String, String>> cabTypeList;
    Context mContext;
    String vehicleIconPath = CommonUtilities.SERVER_URL + "webimages/icons/VehicleType/";
    String vehicleDefaultIconPath = CommonUtilities.SERVER_URL + "webimages/icons/DefaultImg/";

    OnItemClickList onItemClickList;
    ViewHolder viewHolder;

    String selectedVehicleTypeId = "";
    boolean isMultiDelivery = false;
    int whiteColor;

    public CabTypeAdapter(Context mContext, ArrayList<HashMap<String, String>> list_item, GeneralFunctions generalFunc) {
        this.mContext = mContext;
        this.list_item = list_item;
        this.generalFunc = generalFunc;
        this.rental_item = rental_item;
        whiteColor=mContext.getResources().getColor(R.color.white);

    }

    public void setRentalItem(ArrayList<HashMap<String, String>> list_item) {
        this.list_item = list_item;

    }

    @Override
    public CabTypeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design_cab_type, parent, false);

        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void setSelectedVehicleTypeId(String selectedVehicleTypeId) {
        this.selectedVehicleTypeId = selectedVehicleTypeId;
    }

    public void isMultiDelivery(boolean isMultiDelivery) {
        this.isMultiDelivery = isMultiDelivery;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        setData(viewHolder, position, false);
    }

    public void setData(CabTypeAdapter.ViewHolder viewHolder, final int position, boolean isHoverOLD) {
        HashMap<String, String> item = list_item.get(position);

        String vVehicleType = item.get("vVehicleType");
        String iVehicleTypeId = item.get("iVehicleTypeId");


        String eRental=item.get("eRental");
        if (eRental != null && !eRental.equals("") && eRental.equalsIgnoreCase("Yes")) {
            viewHolder.carTypeTitle.setText(item.get("vRentalVehicleTypeName"));
            viewHolder.capacityVTxt.setText(item.get("iPersonSize"));
            viewHolder.capacityVTxt2.setText(generalFunc.retrieveLangLBl("", "LBL_PEOPLE_TXT"));

        } else {
            viewHolder.carTypeTitle.setText(vVehicleType);
            viewHolder.capacityVTxt.setText(item.get("iPersonSize"));
            viewHolder.capacityVTxt2.setText(generalFunc.retrieveLangLBl("", "LBL_PEOPLE_TXT"));
        }

        boolean isHover = selectedVehicleTypeId.equals(iVehicleTypeId) ? true : false;

        String total_fare=item.get("total_fare");
        if (total_fare != null && !total_fare.equals("")) {
            viewHolder.totalfare.setText(generalFunc.convertNumberWithRTL(total_fare));
        } else {
            viewHolder.infoimage.setVisibility(View.GONE);
            viewHolder.totalfare.setText("");
        }


        String imgUrl = "";
        String imgName = "";
        if (isHover) {
            imgName = getImageName(item.get("vLogo1"));
        } else {
            imgName = getImageName(item.get("vLogo"));
        }
        if (imgName.equals("")) {
            if (isHover) {
                imgUrl = vehicleDefaultIconPath + "hover_ic_car.png";
            } else {
                imgUrl = vehicleDefaultIconPath + "ic_car.png";
            }
        } else {
            imgUrl = vehicleIconPath + item.get("iVehicleTypeId") + "/android/" + imgName;
        }
        loadImage(viewHolder, imgUrl);

//
//        if (position == 0) {
//            viewHolder.leftSeperationLine.setVisibility(View.INVISIBLE);
//            viewHolder.leftSeperationLine2.setVisibility(View.INVISIBLE);
//        } else {
//            viewHolder.leftSeperationLine.setVisibility(View.VISIBLE);
//            viewHolder.leftSeperationLine2.setVisibility(View.VISIBLE);
//        }
//
//        if (position == list_item.size() - 1) {
//            viewHolder.rightSeperationLine.setVisibility(View.INVISIBLE);
//            viewHolder.rightSeperationLine2.setVisibility(View.INVISIBLE);
//        } else {
//            viewHolder.rightSeperationLine.setVisibility(View.VISIBLE);
//            viewHolder.rightSeperationLine2.setVisibility(View.VISIBLE);
//        }


        viewHolder.contentArea.setOnClickListener(view -> {
            if (onItemClickList != null) {
                onItemClickList.onItemClick(position);
            }
        });

        if (isMultiDelivery)
        {
            viewHolder.llArea.setBackgroundColor(whiteColor);
        }

        if (isHover) {
            if (!isMultiDelivery) {
                if (viewHolder.totalfare.getText().toString().length() > 0) {
                    viewHolder.infoimage.setVisibility(View.VISIBLE);
                }
                viewHolder.totalfare.setTextColor(Color.parseColor("#232324")); // color cuando esta selecionado
            }

            viewHolder.imagareaselcted.setVisibility(View.VISIBLE);
            viewHolder.imagarea.setVisibility(View.GONE);

            // para ocultar el circulo de fondo en el carro
            int color=mContext.getResources().getColor(R.color.appThemeColor_2);
            viewHolder.carTypeTitle.setTextColor(color);

            new CreateRoundedView(mContext.getResources().getColor(R.color.white),(int) mContext.getResources().getDimension(R.dimen._50sdp), 3,
                    color, viewHolder.imagareaselcted);
            // viewHolder.carTypeImgView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            //  viewHolder.carTypeImgViewselcted.setColorFilter(mContext.getResources().getColor(R.color.white));
            viewHolder.carTypeImgViewselcted.setBorderColor(color);
            MainActivity.valorcorrida = viewHolder.totalfare.getText().toString();

        } else {
            if (!isMultiDelivery) {
                viewHolder.totalfare.setTextColor(Color.parseColor("#BABABA")); // color cuando no esta selecionado
                viewHolder.infoimage.setVisibility(View.GONE);
            }
            // para ocultar el circulo de fondo en el carro
            viewHolder.imagareaselcted.setVisibility(View.GONE);
            viewHolder.imagarea.setVisibility(View.VISIBLE);
            viewHolder.carTypeTitle.setTextColor(mContext.getResources().getColor(R.color.black));

            int color=Color.parseColor("#cbcbcb");

            new CreateRoundedView(Color.parseColor("#ffffff"), (int) mContext.getResources().getDimension(R.dimen._30sdp), 2,
                    color, viewHolder.imagarea);
            //   viewHolder.carTypeImgView.setColorFilter(Color.parseColor("#999fa2"));
            viewHolder.carTypeImgView.setBorderColor(color);

        }

        if (isMultiDelivery) {
            viewHolder.totalFareArea.setVisibility(View.GONE);
        } else {
            viewHolder.totalFareArea.setVisibility(View.VISIBLE);
        }

    }

    private String getImageName(String vLogo) {
        String imageName = "";

        if (vLogo.equals("")) {
            return vLogo;
        }

        DisplayMetrics metrics = (mContext.getResources().getDisplayMetrics());
        int densityDpi = (int) (metrics.density * 160f);
        switch (densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                imageName = "mdpi_" + vLogo;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                imageName = "mdpi_" + vLogo;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                imageName = "hdpi_" + vLogo;
                break;

            case DisplayMetrics.DENSITY_TV:
                imageName = "hdpi_" + vLogo;
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                imageName = "xhdpi_" + vLogo;
                break;

            case DisplayMetrics.DENSITY_280:
                imageName = "xhdpi_" + vLogo;
                break;

            case DisplayMetrics.DENSITY_400:
                imageName = "xxhdpi_" + vLogo;
                break;

            case DisplayMetrics.DENSITY_360:
                imageName = "xxhdpi_" + vLogo;
                break;
            case DisplayMetrics.DENSITY_420:
                imageName = "xxhdpi_" + vLogo;
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                imageName = "xxhdpi_" + vLogo;
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                imageName = "xxxhdpi_" + vLogo;
                break;

            case DisplayMetrics.DENSITY_560:
                imageName = "xxxhdpi_" + vLogo;
                break;

            default:
                imageName = "xxhdpi_" + vLogo;
                break;
        }

        return imageName;
    }

    private void loadImage(final CabTypeAdapter.ViewHolder holder, String imageUrl) {

        Picasso.get()
                .load(imageUrl)
                .into(holder.carTypeImgView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        holder.loaderView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e){
                        holder.loaderView.setVisibility(View.VISIBLE);
                    }
                });

        Picasso.get()
                .load(imageUrl)
                .into(holder.carTypeImgViewselcted, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        holder.loaderView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e){
                        holder.loaderView.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public int getItemCount() {
        if (list_item == null) {
            return 0;
        }
        return list_item.size();
    }

    public void setOnItemClickList(OnItemClickList onItemClickList) {
        this.onItemClickList = onItemClickList;
    }

    public void clickOnItem(int position) {
        if (onItemClickList != null) {
            onItemClickList.onItemClick(position);
        }
    }

    public interface OnItemClickList {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SelectableRoundedImageView carTypeImgView, carTypeImgViewselcted;
        MTextView carTypeTitle;
        View leftSeperationLine;
        View rightSeperationLine;
        View leftSeperationLine2;
        View rightSeperationLine2;
        RelativeLayout contentArea;
        AVLoadingIndicatorView loaderView, loaderViewselected;
        MTextView totalfare;
        MTextView capacityVTxt;
        MTextView capacityVTxt2;

        ImageView infoimage;
        LinearLayout totalFareArea;
        RelativeLayout llArea;

        public FrameLayout imagarea, imagareaselcted;

        public ViewHolder(View view) {
            super(view);

            carTypeImgView = (SelectableRoundedImageView) view.findViewById(R.id.carTypeImgView);
            carTypeImgViewselcted = (SelectableRoundedImageView) view.findViewById(R.id.carTypeImgViewselcted);
            carTypeTitle = (MTextView) view.findViewById(R.id.carTypeTitle);
            totalFareArea = (LinearLayout) view.findViewById(R.id.totalFareArea);
            leftSeperationLine = view.findViewById(R.id.leftSeperationLine);
            rightSeperationLine = view.findViewById(R.id.rightSeperationLine);
            leftSeperationLine2 = view.findViewById(R.id.leftSeperationLine2);
            rightSeperationLine2 = view.findViewById(R.id.rightSeperationLine2);
            contentArea = (RelativeLayout) view.findViewById(R.id.contentArea);
            llArea = (RelativeLayout) view.findViewById(R.id.llArea);
            loaderView = (AVLoadingIndicatorView) view.findViewById(R.id.loaderView);
            loaderViewselected = (AVLoadingIndicatorView) view.findViewById(R.id.loaderViewselected);
            totalfare = (MTextView) view.findViewById(R.id.totalfare);
            capacityVTxt = (MTextView) view.findViewById(R.id.capacityVTxt);
            capacityVTxt2 = (MTextView) view.findViewById(R.id.capacityVTxt2);
            imagarea = (FrameLayout) view.findViewById(R.id.imagarea);
            imagareaselcted = (FrameLayout) view.findViewById(R.id.imagareaselcted);
            infoimage = (ImageView) view.findViewById(R.id.infoimage);

        }
    }
}

