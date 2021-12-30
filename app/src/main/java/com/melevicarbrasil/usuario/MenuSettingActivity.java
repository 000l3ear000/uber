package com.melevicarbrasil.usuario;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.fragments.MyProfileFragment;

public class MenuSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_menu_setting);


        MyProfileFragment frag = new MyProfileFragment();
        Bundle bn = new Bundle();
        bn.putBoolean("isback",true);
        frag.setArguments(bn);
        addFragment(android.R.id.content,
               frag, "MyProfileFrag");

    }

    protected void addFragment(@IdRes int containerViewId,
                               @NonNull Fragment fragment,
                               @NonNull String fragmentTag) {



        getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .disallowAddToBackStack()
                .commit();
    }
}
