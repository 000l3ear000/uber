package com.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.melevicarbrasil.usuario.BusinessProfileActivity;
import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.squareup.picasso.Picasso;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;

public class BusinessProfileListFragment extends Fragment {


    View v;
    LinearLayout contentArea;
    BusinessProfileActivity businessProfileActivity;
    GeneralFunctions generalFunc;
    ArrayList<HashMap<String, String>> map;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_business_profile_list, container, false);

        businessProfileActivity = (BusinessProfileActivity) getActivity();
        generalFunc = businessProfileActivity.generalFunc;
        map = (ArrayList<HashMap<String, String>>) getArguments().get("Data");
        contentArea = (LinearLayout) v.findViewById(R.id.contentArea);


        if (contentArea.getChildCount() > 0) {
            contentArea.removeAllViewsInLayout();
        }

        if (map != null) {
            for (int i = 0; i < map.size(); i++) {

                if (map.size() == i + 1) {
                    addFareDetailRow(map.get(i), true);
                } else {
                    addFareDetailRow(map.get(i), false);
                }
            }
        }


        return v;
    }


    private void addFareDetailRow(HashMap<String, String> data, boolean isLast) {
        View convertView = null;

        LayoutInflater infalInflater = (LayoutInflater) businessProfileActivity.getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.item_business_profile_list, null);

        convertView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // convertView.setMinimumHeight(Utils.dipToPixels(businessProfileActivity.getActContext(), 40));

        MTextView profileNameTxt = (MTextView) convertView.findViewById(R.id.profileNameTxt);
        MTextView profileDescTxt = (MTextView) convertView.findViewById(R.id.profileDescTxt);
        ImageView profileImage = (ImageView) convertView.findViewById(R.id.profileImage);
        ImageView arrowImage = (ImageView) convertView.findViewById(R.id.arrowImage);
        if (generalFunc.isRTLmode()) {
            arrowImage.setRotation(180);
        }
        View divideview = (View) convertView.findViewById(R.id.divideview);
        LinearLayout mainview = (LinearLayout) convertView.findViewById(R.id.mainview);
        mainview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                businessProfileActivity.OpenProfileIntroScreen(data, true);

            }
        });


        String vImage=data.get("vImage");
        if (!vImage.equalsIgnoreCase("")) {
            Picasso.get().load(vImage).placeholder(R.mipmap.ic_no_icon).into(profileImage);
        }

        profileNameTxt.setText(data.get("vTitle"));

        String vSubTitle=data.get("vSubTitle");
        if (!vSubTitle.equalsIgnoreCase("")) {
            profileDescTxt.setText(vSubTitle);
            profileDescTxt.setVisibility(View.VISIBLE);
        } else {
            profileDescTxt.setVisibility(View.GONE);
        }

        String eProfileAdded=data.get("eProfileAdded");
        if (eProfileAdded != null && eProfileAdded.equalsIgnoreCase("Yes")) {
            profileDescTxt.setVisibility(View.GONE);
            profileNameTxt.setText(data.get("vProfileName"));
        }


        if (isLast) {
            divideview.setVisibility(View.GONE);
        } else {
            divideview.setVisibility(View.VISIBLE);
        }


        if (convertView != null)
            contentArea.addView(convertView);
    }

}
