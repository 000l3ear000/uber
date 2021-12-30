package com.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.melevicarbrasil.usuario.BusinessProfileActivity;
import com.melevicarbrasil.usuario.BusinessSetupActivity;
import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.general.files.StartActProcess;
import com.squareup.picasso.Picasso;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;

import java.util.HashMap;

public class BusinessProfileIntroFragment extends Fragment {

    View v;
    MButton btn_type2;
    MTextView noteHeading;
    MTextView noteTxt;
    ImageView introImage;

    HashMap<String, String> map;
    BusinessProfileActivity businessProfileActivity;
    GeneralFunctions generalFunc;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_business_profile_intro, container, false);

        businessProfileActivity = (BusinessProfileActivity) getActivity();

        generalFunc = businessProfileActivity.generalFunc;
        btn_type2 = ((MaterialRippleLayout) v.findViewById(R.id.btn_type2)).getChildView();
        introImage = ((ImageView) v.findViewById(R.id.introImage));
        noteHeading = (MTextView) v.findViewById(R.id.noteHeading);
        noteTxt = (MTextView) v.findViewById(R.id.noteTxt);
        noteTxt.setOnClickListener(new setOnClickList());
        btn_type2.setId(Utils.generateViewId());
        btn_type2.setOnClickListener(new setOnClickList());
        map = (HashMap<String, String>) getArguments().get("Data");

        noteHeading.setText(map.get("vScreenTitle"));
        noteTxt.setText(map.get("tDescription"));
        btn_type2.setText(map.get("vScreenButtonText"));

        String vWelcomeImage=map.get("vWelcomeImage");
        if (!vWelcomeImage.equalsIgnoreCase("")) {
            Picasso.get().load(vWelcomeImage).placeholder(R.mipmap.ic_no_icon).into(introImage);
        }
        return v;
    }


    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == btn_type2.getId()) {
                Bundle bn = new Bundle();
                bn.putString("iUserProfileMasterId", map.get("iUserProfileMasterId"));
                new StartActProcess(getContext()).startActWithData(BusinessSetupActivity.class, bn);
            }
        }
    }
}
