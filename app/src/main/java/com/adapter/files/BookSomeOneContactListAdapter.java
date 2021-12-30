package com.adapter.files;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.melevicarbrasil.usuario.BookSomeOneElsePickContactActivity;
import com.melevicarbrasil.usuario.R;
import com.melevicarbrasil.usuario.SearchLocationActivity;
import com.general.files.GeneralFunctions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.model.ContactModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.utils.CommonUtilities;
import com.utils.Logger;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MTextView;
import com.view.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookSomeOneContactListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {


    public GeneralFunctions generalFunc;
    public String userProfileJson;
    public String selectedNum;
    public ItemClickListener clickListener;
    Context mContext;

    List<ContactModel> list = new ArrayList<>();
    private List<ContactModel> contactListFiltered = new ArrayList<>();

    public static final int SECTION_VIEW = 0;
    public static final int CONTENT_VIEW = 1;
    private static final int TYPE_FOOTER = 2;

    // paging
    boolean isFooterEnabled = false;
    View footerView;

    FooterViewHolder footerHolder;
    RecyclerView recyclerView;

    private final int btnRadius;

    public BookSomeOneContactListAdapter(RecyclerView recyclerView, Context mContext, List<ContactModel> list, GeneralFunctions generalFunctions, boolean isFooterEnabled) {
        this.list = (mContext instanceof BookSomeOneElsePickContactActivity) ? ((BookSomeOneElsePickContactActivity) mContext).finalList : list;
        this.contactListFiltered = list;
        this.mContext = mContext;
        this.generalFunc = generalFunctions;
        this.isFooterEnabled = isFooterEnabled;
        this.recyclerView = recyclerView;
        userProfileJson = generalFunctions.retrieveValue(Utils.USER_PROFILE_JSON);

        Gson gson = new Gson();
        String data1 = generalFunc.retrieveValue(Utils.BFSE_SELECTED_CONTACT_KEY);
        ContactModel contactdetails = gson.fromJson(data1, new TypeToken<ContactModel>() {
                }.getType()
        );

        selectedNum = (contactdetails != null && Utils.checkText(contactdetails.mobileNumber)) ? contactdetails.mobileNumber : "";
        btnRadius=Utils.dipToPixels(mContext, R.dimen._17sdp);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == SECTION_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_someone_contacts_header, parent, false);
            return new SectionHeaderViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_list, parent, false);
            this.footerView = v;
            return new FooterViewHolder(v);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_book_someone_contact_design, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        setData(holder, position);
    }


    private void setData(RecyclerView.ViewHolder holder, final int position) {
        if (SECTION_VIEW == getItemViewType(position)) {
            final ContactModel item = contactListFiltered.get(position);
            final SectionHeaderViewHolder viewHolder = (SectionHeaderViewHolder) holder;
            viewHolder.headerTitleTxt.setText(item.hederName);
            return;
        } else if (CONTENT_VIEW == getItemViewType(position)) {
            final ContactModel item = contactListFiltered.get(position);

            final ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.contactNameTxt.setText(item.nameLbl);
            viewHolder.nameTxt.setText(item.nameChar);
            viewHolder.contactNoTxt.setText(item.mobileNumber);
            if (Utils.checkText(item.mobileNumber)) {
                viewHolder.contactNoTxt.setVisibility(View.VISIBLE);
            }

            int _color=mContext.getResources().getColor(R.color.white);

            new CreateRoundedView(Color.parseColor(item.colorVal), btnRadius, 1, _color, viewHolder.contactProfileImgView);
            new CreateRoundedView(Color.parseColor(item.colorVal), btnRadius, 1, _color, viewHolder.nameTxt);

            String vContactID = item.id;

            int color=mContext.getResources().getColor(R.color.bt_text_blue);

            if (mContext instanceof SearchLocationActivity && item.mobileNumber.equalsIgnoreCase(selectedNum)) {
                viewHolder.imgSelected.setVisibility(View.VISIBLE);
            } else if (mContext instanceof BookSomeOneElsePickContactActivity && ((BookSomeOneElsePickContactActivity) mContext).selectedRowId.equalsIgnoreCase(item.id)) {
                viewHolder.imgSelected.setVisibility(View.VISIBLE);
                viewHolder.contactNameTxt.setTextColor(color);
                viewHolder.imgSelected.setColorFilter(color);
            } else {
                viewHolder.imgSelected.setVisibility(View.INVISIBLE);
                int color_=mContext.getResources().getColor(R.color.black);
                viewHolder.contactNameTxt.setTextColor(color_);
                viewHolder.imgSelected.setColorFilter(color_);
            }

            if (vContactID.equalsIgnoreCase("0")) {
                viewHolder.contactProfileImgView.setVisibility(View.GONE);
                viewHolder.nameTxt.setVisibility(View.GONE);
                viewHolder.imgSelected.setVisibility(View.INVISIBLE);
                viewHolder.addArea.setVisibility(View.VISIBLE);
                viewHolder.contactNameTxt.setTextColor(color);
            } else if (Utils.checkText(item.photo)) {
                Bitmap bitmap = Utils.fromBase64(item.photo);
                if (bitmap != null && !bitmap.equals("")) {
                    viewHolder.nameTxt.setVisibility(View.GONE);
                    viewHolder.contactProfileImgView.setImageBitmap(bitmap);
                } else {
                    viewHolder.nameTxt.setVisibility(View.VISIBLE);
                }
            } else {

                viewHolder.nameTxt.setVisibility(View.VISIBLE);
                viewHolder.contactProfileImgView.setVisibility(View.GONE);
                viewHolder.contactProfileImgView.setImageURI(null);

                Picasso.get().load(item.photoURI != null && item.photoURI.startsWith("http") ? item.photoURI : CommonUtilities.USER_PHOTO_PATH).into(viewHolder.contactProfileImgView, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (item.photoURI != null && !item.photoURI.equals("") && item.photoURI.startsWith("http")) {
                            viewHolder.contactProfileImgView.setVisibility(View.VISIBLE);
                            viewHolder.nameTxt.setVisibility(View.GONE);
                        } else {
                            viewHolder.nameTxt.setVisibility(View.VISIBLE);
                            viewHolder.contactProfileImgView.setVisibility(View.GONE);
                            viewHolder.contactProfileImgView.setImageURI(null);
                        }
                    }

                    @Override
                    public void onError(Exception e){
                        if (item.photoURI != null && !item.photoURI.equals("") && Uri.parse(item.photoURI) != null && !item.photoURI.startsWith("http")) {
                            viewHolder.contactProfileImgView.setVisibility(View.VISIBLE);
                            viewHolder.nameTxt.setVisibility(View.GONE);
                            viewHolder.contactProfileImgView.setImageURI(Uri.parse(item.photoURI));
                        } else {
                            viewHolder.nameTxt.setVisibility(View.VISIBLE);
                            viewHolder.contactProfileImgView.setVisibility(View.GONE);
                            viewHolder.contactProfileImgView.setImageURI(null);
                        }
                    }
                });

            }

            viewHolder.showContactArea.setOnClickListener(view -> {
                if (clickListener != null) {
                    clickListener.setSelected(position, item.id);
                }

                notifyItemChanged(position);

            });

        } else if (TYPE_FOOTER == getItemViewType(position)) {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            this.footerHolder = footerHolder;
        }

    }

    private boolean isPositionFooter(int position) {
        return position == contactListFiltered.size();
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (isFooterEnabled == true) {
            return contactListFiltered.size() + 1;
        } else {
            return contactListFiltered.size();
        }

    }

    public void onClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<ContactModel> filteredList = new ArrayList<>();

                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredList = list;
                } else {
                    String lastHeader = "";
                    for (ContactModel row : list) {
                        String header = "";

                        if (Utils.checkText(row.nameLbl)) {
                            header = String.valueOf(row.nameLbl.charAt(0)).toUpperCase(Locale.US);
                        }

                        String listName = row.nameLbl.toLowerCase(Locale.US);
                        String listNum = row.mobileNumber;
                        String typedName = charString.toLowerCase(Locale.US);

                        if (listName.startsWith(typedName) || listNum.startsWith(String.valueOf(charSequence))) {
                            if (!TextUtils.equals(lastHeader, header)) {
                                lastHeader = header;
                                filteredList.add(new ContactModel(header, true));
                            }
                            filteredList.add(row);
                        }
                    }

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                Logger.d("SearchContact", "filteredList" + filterResults.values);
                if (filterResults.values != null)
                    contactListFiltered = (ArrayList<ContactModel>) filterResults.values;
                ((BookSomeOneElsePickContactActivity) mContext).mSectionList = contactListFiltered;
                notifyDataSetChanged();
            }
        };
    }

    public interface ItemClickListener {
        void setSelected(int position, String rowId);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SelectableRoundedImageView contactProfileImgView;
        MTextView contactNameTxt, nameTxt;
        MTextView contactNoTxt;
        LinearLayout showContactArea, addArea;
        ImageView addImgView, imgSelected;

        public ViewHolder(View itemView) {
            super(itemView);

            imgSelected = (ImageView) itemView.findViewById(R.id.imgSelected);
            addImgView = (ImageView) itemView.findViewById(R.id.addImgView);
            addArea = (LinearLayout) itemView.findViewById(R.id.addArea);
            contactNameTxt = (MTextView) itemView.findViewById(R.id.contactNameTxt);
            contactNoTxt = (MTextView) itemView.findViewById(R.id.contactNoTxt);
            nameTxt = (MTextView) itemView.findViewById(R.id.nameTxt);
            showContactArea = (LinearLayout) itemView.findViewById(R.id.showContactArea);
            contactProfileImgView = (SelectableRoundedImageView) itemView.findViewById(R.id.contactProfileImgView);

        }

    }

    public class SectionHeaderViewHolder extends RecyclerView.ViewHolder {
        MTextView headerTitleTxt;

        public SectionHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            headerTitleTxt = (MTextView) itemView.findViewById(R.id.headerTitleTxt);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position) && isFooterEnabled == true) {
            return TYPE_FOOTER;
        } else if (contactListFiltered.get(position).isSection) {
            return SECTION_VIEW;
        } else {
            return CONTENT_VIEW;
        }
    }


    public void addFooterView() {
//        Logger.d("Footer", "added");
        this.isFooterEnabled = true;
        notifyDataSetChanged();
        if (footerHolder != null)
            footerHolder.progressArea.setVisibility(View.VISIBLE);
    }

    public void removeFooterView() {
//        Logger.d("Footer", "removed");
        if (footerHolder != null)
            footerHolder.progressArea.setVisibility(View.GONE);
//        footerHolder.progressArea.setPadding(0, -1 * footerView.getHeight(), 0, 0);
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        LinearLayout progressArea;

        public FooterViewHolder(View itemView) {
            super(itemView);

            progressArea = (LinearLayout) itemView;

        }
    }

}
