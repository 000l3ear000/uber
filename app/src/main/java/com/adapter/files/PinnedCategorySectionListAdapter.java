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
import com.general.files.AppFunctions;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MTextView;
import com.view.pinnedListView.CountryListItem;
import com.view.pinnedListView.PinnedSectionListView;

import java.util.ArrayList;

public class PinnedCategorySectionListAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter, SectionIndexer {

    private CategoryListItem[] sections;

    private LayoutInflater inflater;

    Context mContext;
    ArrayList<CategoryListItem> categoryListItems;

    ServiceClick serviceClickList;

    boolean isStateList = false;
    GeneralFunctions generalFunctions;
    int padding, padding1;
    String LBL_HOUR = "", LBL_MINIMUM_TXT = "", LBL_HOURS_TXT = "", LBL_REMOVE_SERVICE = "", LBL_BOOK_SERVICE = "";

    public PinnedCategorySectionListAdapter(Context mContext, ArrayList<CategoryListItem> categoryListItems, CategoryListItem[] sections) {
        // TODO Auto-generated constructor stub
        this.mContext = mContext;
        this.categoryListItems = categoryListItems;
        this.sections = sections;
        generalFunctions = MyApp.getInstance().getGeneralFun(mContext);

        padding = Utils.dipToPixels(mContext, 5);
        padding1 = Utils.dipToPixels(mContext, 10);
        LBL_HOUR = generalFunctions.retrieveLangLBl("hour", "LBL_HOUR");
        LBL_HOURS_TXT = generalFunctions.retrieveLangLBl("", "LBL_HOURS_TXT");
        LBL_MINIMUM_TXT = generalFunctions.retrieveLangLBl("", "LBL_MINIMUM_TXT");
        LBL_REMOVE_SERVICE = generalFunctions.retrieveLangLBl("", "LBL_REMOVE_SERVICE");
        LBL_BOOK_SERVICE = generalFunctions.retrieveLangLBl("", "LBL_BOOK_SERVICE");
    }

    public void changeSection(CategoryListItem[] sections) {
        this.sections = sections;
    }

    public void isStateList(boolean value) {
        this.isStateList = value;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.service_list_item, null);

        TextView headingTxt = (TextView) convertView.findViewById(R.id.headingTxt);
        TextView headingTitleTxt = (TextView) convertView.findViewById(R.id.headingTitleTxt);
        TextView titleTxt = (TextView) convertView.findViewById(R.id.titleTxt);
        TextView descTxt = (TextView) convertView.findViewById(R.id.descTxt);
        TextView amountTxt = (TextView) convertView.findViewById(R.id.amountTxt);
        TextView bookRemoveTxt = (TextView) convertView.findViewById(R.id.bookRemoveTxt);
        MTextView btnTxt = (MTextView) convertView.findViewById(R.id.btnTxt);
        ImageView btnImg = (ImageView) convertView.findViewById(R.id.btnImg);
        LinearLayout btnArea = (LinearLayout) convertView.findViewById(R.id.btnArea);
        LinearLayout detalisArea = (LinearLayout) convertView.findViewById(R.id.detalisArea);

        new CreateRoundedView(mContext.getResources().getColor(R.color.appThemeColor_1), 5, 0, 0, bookRemoveTxt);

        headingTxt.setTextColor(Color.BLACK);
        headingTxt.setTag("" + position);
        final CategoryListItem categoryListItem = categoryListItems.get(position);


        if (categoryListItem.type == CountryListItem.SECTION) {

//			convertView.setBackgroundResource(R.drawable.bg_header_country_list);
           // convertView.setBackgroundColor(mContext.getResources().getColor(R.color.bt_very_light_gray));
//			convertView.setAlpha((float) 0.96);
            detalisArea.setClickable(false);
            detalisArea.setEnabled(false);
            headingTitleTxt.setText(categoryListItem.getvTitle());
            headingTitleTxt.setVisibility(View.VISIBLE);
            headingTitleTxt.setText(categoryListItem.text);
          //  headingTitleTxt.setPaddingRelative(padding, padding, padding1, padding1);
            detalisArea.setVisibility(View.GONE);

        } else {


            if (categoryListItem.getvDesc().equalsIgnoreCase("")) {
                descTxt.setVisibility(View.GONE);
            } else {
                descTxt.setVisibility(View.VISIBLE);
            }

            descTxt.setText(AppFunctions.fromHtml(categoryListItem.getvDesc()));
            headingTxt.setText(categoryListItem.getvTitle());
            headingTitleTxt.setVisibility(View.GONE);

            if (categoryListItem.isAdd) {
                btnTxt.setText(LBL_REMOVE_SERVICE);
            } else {
                btnTxt.setText(LBL_BOOK_SERVICE);
            }

            if (generalFunctions.isRTLmode()) {
                btnImg.setRotation(180);
                btnArea.setBackground(mContext.getResources().getDrawable(R.drawable.login_border_rtl));
            }

            String FareType = categoryListItem.geteFareType();

            if (FareType.equalsIgnoreCase("Fixed")) {
                amountTxt.setText(generalFunctions.convertNumberWithRTL(categoryListItem.getfFixedFare()));
            } else if (FareType.equalsIgnoreCase("Hourly")) {
                String hourlyPrice = generalFunctions.convertNumberWithRTL(categoryListItem.getfPricePerHour());
                amountTxt.setText(
                        hourlyPrice + "/" + LBL_HOUR);

                if (GeneralFunctions.parseIntegerValue(0, categoryListItem.getfMinHour()) > 1) {
                    amountTxt.setText(
                            hourlyPrice + "/" + LBL_HOUR + "\n" + "(" + LBL_MINIMUM_TXT + " " + categoryListItem.getfMinHour() + " " + LBL_HOURS_TXT + ")");

                }
            } else {
                amountTxt.setText("");
            }
            detalisArea.setClickable(true);
            detalisArea.setEnabled(true);
//			txt_count.setVisibility(View.GONE);

        }

        detalisArea.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

//				Toast.makeText(mContext, "hi--" + countryListItem.text, Toast.LENGTH_LONG).show();
                if (serviceClickList != null) {
                    serviceClickList.serviceClickList(categoryListItem);
                }
            }
        });

        btnArea.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (categoryListItem.isAdd) {
                    if (serviceClickList != null) {
                        serviceClickList.serviceRemoveClickList(categoryListItem);
                    }
                } else {
                    if (serviceClickList != null) {
                        serviceClickList.serviceClickList(categoryListItem);
                    }
                }
            }
        });

        return convertView;
    }

    public interface ServiceClick {
        void serviceClickList(CategoryListItem serviceListItem);

        void serviceRemoveClickList(CategoryListItem serviceListItem);
    }

    public void setserviceClickListener(ServiceClick serviceClickList) {
        this.serviceClickList = serviceClickList;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public CategoryListItem[] getSections() {
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
        return categoryListItems.get(position).sectionPosition;
    }

    @Override
    public int getItemViewType(int position) {
        try {
            return categoryListItems.get(position).type;
        } catch (Exception e) {

        }
        return 0;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == CountryListItem.SECTION;
    }

    @Override
    public int getCount() {

        return categoryListItems.size();
    }

    @Override
    public Object getItem(int position) {

        return categoryListItems.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

}
