package com.adapter.files;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.squareup.picasso.Picasso;
import com.utils.CommonUtilities;
import com.utils.Utils;
import com.view.MTextView;
import com.view.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 09-07-2016.
 */
public class ChatMessagesRecycleAdapter extends RecyclerView.Adapter<ChatMessagesRecycleAdapter.ViewHolder> {

    public GeneralFunctions generalFunc;
    ArrayList<HashMap<String, Object>> list_item;
    Context mContext;
    OnItemClickList onItemClickList;
    private HashMap<String, String> data_trip;

    public ChatMessagesRecycleAdapter(Context mContext, ArrayList<HashMap<String, Object>> list_item, GeneralFunctions generalFunc, HashMap<String, String> data_trip) {
        this.mContext = mContext;
        this.list_item = list_item;
        this.data_trip = data_trip;
        this.generalFunc = generalFunc;
    }

    @Override
    public ChatMessagesRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        HashMap<String, Object> item = list_item.get(position);

        String eUserType = item.get("eUserType").toString();

        if (eUserType.equals(Utils.app_type)) {
            viewHolder.activity_main.setVisibility(View.VISIBLE);
            viewHolder.activity_main.setBackground(mContext.getResources().getDrawable(R.drawable.shape_outgoing_message));
            viewHolder.messageUser.setText("You");
            viewHolder.rightuserImageview.setVisibility(View.VISIBLE);
            viewHolder.lefttuserImageview.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
            viewHolder.activity_main.setLayoutParams(params);
            viewHolder.mainlayout.setVisibility(View.VISIBLE);
            viewHolder.rightshape.setVisibility(View.GONE);
            viewHolder.leftshap.setVisibility(View.GONE);
            viewHolder.messageText.setTextColor(Color.parseColor("#002a2a"));

            if (item.get("vDate") != null && !item.get("vDate").equals("")) {
                viewHolder.right_message_time.setVisibility(View.VISIBLE);
                viewHolder.right_message_time.setText(generalFunc.convertNumberWithRTL(generalFunc.getDateFormatedType(item.get("vDate") + "", Utils.OriginalDateFormate, "hh:mm aa")));

            }
            viewHolder.left_message_time.setVisibility(View.GONE);


            String image_url = CommonUtilities.USER_PHOTO_PATH + item.get("passengerId") + "/" + item.get("passengerImageName");
//            Logger.d("image_url:", "Passenger:" + image_url);

            Picasso.get()
                    .load(image_url)
                    .placeholder(R.mipmap.ic_no_pic_user)
                    .error(R.mipmap.ic_no_pic_user)
                    .into(viewHolder.rightuserImageview);
        } else {
            viewHolder.activity_main.setVisibility(View.VISIBLE);
            viewHolder.activity_main.setBackground(mContext.getResources().getDrawable(R.drawable.shape_incoming_message));

            viewHolder.messageUser.setText(data_trip.get("FromMemberName"));
            viewHolder.lefttuserImageview.setVisibility(View.VISIBLE);
            viewHolder.rightuserImageview.setVisibility(View.GONE);
            viewHolder.messageText.setTextColor(Color.parseColor("#FFFFFF"));

            if (item.get("vDate") != null && !item.get("vDate").equals("")) {
                viewHolder.left_message_time.setVisibility(View.VISIBLE);
                viewHolder.left_message_time.setText(generalFunc.convertNumberWithRTL(generalFunc.getDateFormatedType(item.get("vDate")+"", Utils.OriginalDateFormate, "hh:mm aa")));
            }
            viewHolder.right_message_time.setVisibility(View.GONE);

            RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
            viewHolder.activity_main.setLayoutParams(params);
            viewHolder.rightshape.setVisibility(View.GONE);
            viewHolder.leftshap.setVisibility(View.GONE);

            String image_url = CommonUtilities.PROVIDER_PHOTO_PATH + item.get("driverId") + "/" + item.get("driverImageName");

            Picasso.get()
                    .load(image_url)
                    .placeholder(R.mipmap.ic_no_pic_user)
                    .error(R.mipmap.ic_no_pic_user)
                    .into(viewHolder.lefttuserImageview);
        }

        String Text = item.get("Text").toString();
        if (Text.length() == 1) {
            viewHolder.messageText.setText(" " + Text + " ");
        } else {
            viewHolder.messageText.setText(Text);
        }

        viewHolder.messageUser.setText(eUserType);
    }

    @Override
    public int getItemCount() {
        return list_item.size();
    }

    public void setOnItemClickList(OnItemClickList onItemClickList) {
        this.onItemClickList = onItemClickList;
    }

    public interface OnItemClickList {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView messageUser, left_message_time, right_message_time;
        public LinearLayout activity_main, mainlayout;
        ImageView rightshape, leftshap;
        SelectableRoundedImageView lefttuserImageview, rightuserImageview;
        MTextView messageText;

        public ViewHolder(View view) {
            super(view);


            mainlayout = (LinearLayout) view.findViewById(R.id.mainlayout);
            rightshape = (ImageView) view.findViewById(R.id.rightshape);
            leftshap = (ImageView) view.findViewById(R.id.leftshap);

            lefttuserImageview = (SelectableRoundedImageView) view.findViewById(R.id.lefttuserImageview);
            rightuserImageview = (SelectableRoundedImageView) view.findViewById(R.id.rightuserImageview);


            messageText = (MTextView) view.findViewById(R.id.message_text);
            messageUser = (TextView) view.findViewById(R.id.message_user);
            left_message_time = (TextView) view.findViewById(R.id.left_message_time);
            right_message_time = (TextView) view.findViewById(R.id.right_message_time);
            activity_main = (LinearLayout) view.findViewById(R.id.activity_main);
        }
    }

}
