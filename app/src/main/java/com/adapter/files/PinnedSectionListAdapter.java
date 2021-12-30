package com.adapter.files;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.squareup.picasso.Picasso;
import com.view.pinnedListView.CountryListItem;
import com.view.pinnedListView.PinnedSectionListView;

import java.util.ArrayList;

public class PinnedSectionListAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter, SectionIndexer {

    Context mContext;
    ArrayList<CountryListItem> countryListItems;

    CountryClick countryClickList;
    boolean isStateList = false;
    private CountryListItem[] sections;
    private LayoutInflater inflater;
    GeneralFunctions generalFunctions;

    public PinnedSectionListAdapter(Context mContext, ArrayList<CountryListItem> countryListItems, CountryListItem[] sections) {
        // TODO Auto-generated constructor stub
        this.mContext = mContext;
        this.countryListItems = countryListItems;
        this.sections = sections;
        generalFunctions = new GeneralFunctions(mContext);
    }

    public void isStateList(boolean value) {
        this.isStateList = value;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final CountryListItem countryListItem = countryListItems.get(position);

        if (countryListItem.type == CountryListItem.SECTION) {
            convertView = inflater.inflate(R.layout.country_header_list_item, null);
        } else {
            //  if (convertView == null)
            convertView = inflater.inflate(R.layout.country_list_item, null);
        }


        TextView txt_view = (TextView) convertView.findViewById(R.id.txt);
        TextView txt_count = (TextView) convertView.findViewById(R.id.txt_count);
        ImageView img = (ImageView) convertView.findViewById(R.id.img);


        txt_view.setTextColor(Color.BLACK);
        txt_view.setTag("" + position);


        txt_view.setText(countryListItem.text);
        if (countryListItem.type == CountryListItem.SECTION) {

//			convertView.setBackgroundResource(R.drawable.bg_header_country_list);
            // convertView.setBackgroundColor(mContext.getResources().getColor(R.color.appThemeColor_1));
//			convertView.setAlpha((float) 0.96);
            txt_view.setClickable(false);
            txt_view.setEnabled(false);

            //int color = Color.parseColor("#FFFFFF");
            // txt_view.setTextColor(color);
            // txt_count.setTextColor(color);
            txt_count.setText("" + countryListItem.CountSubItems);
            txt_count.setVisibility(View.GONE);


        } else {
            LinearLayout mainView = (LinearLayout) convertView.findViewById(R.id.mainView);
            Picasso.get().load(countryListItem.getvRImage()).into(img);
//			txt_count.setVisibility(View.GONE);
            if (isStateList == false) {
                txt_count.setVisibility(View.VISIBLE);
                txt_count.setText("(" + generalFunctions.convertNumberWithRTL(countryListItem.getvPhoneCode()) + ")");
            } else {
                txt_count.setVisibility(View.GONE);
            }

            mainView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

//				Toast.makeText(mContext, "hi--" + countryListItem.text, Toast.LENGTH_LONG).show();
                    if (countryClickList != null) {
                        countryClickList.countryClickList(countryListItem);
                    }
                }
            });

        }


        return convertView;
    }

    public void setCountryClickListener(CountryClick countryClickList) {
        this.countryClickList = countryClickList;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public CountryListItem[] getSections() {
        return sections;
    }

    @Override
    public int getPositionForSection(int section) {
        if (section >= sections.length) {
            section = sections.length - 1;
        }
        return sections[section].listPosition;
    }

    @Override
    public int getSectionForPosition(int position) {
        if (position >= getCount()) {
            position = getCount() - 1;
        }
        return countryListItems.get(position).sectionPosition;
    }

    @Override
    public int getItemViewType(int position) {
        return countryListItems.get(position).type;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == CountryListItem.SECTION;
    }

    @Override
    public int getCount() {

        return countryListItems.size();
    }

    @Override
    public Object getItem(int position) {

        return countryListItems.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }


    public interface CountryClick {
        void countryClickList(CountryListItem countryListItem);
    }

}
