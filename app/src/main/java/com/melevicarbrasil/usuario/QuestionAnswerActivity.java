package com.melevicarbrasil.usuario;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.utils.Utils;
import com.view.MTextView;

public class QuestionAnswerActivity extends AppCompatActivity {

    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    MTextView vQuestion,vAnswer;
    ImageView backImgView;
    // List<String> listDataHeader;
    // HashMap<String, List<String>> listDataChild;

    // ExpandableListView expandableList;

    // QuestionAnswerEAdapter adapter;

    // private int lastExpandedPosition = -1;

   // String answer="";
    MTextView contact_us_btn;
    MTextView textstillneedhelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());


        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);


        vQuestion = (MTextView) findViewById(R.id.vQuestion);

        vAnswer = (MTextView) findViewById(R.id.vAnswer);
        contact_us_btn = (MTextView) findViewById(R.id.contact_us_btn);
        textstillneedhelp = (MTextView) findViewById(R.id.textstillneedhelp);


      /*  expandableList = (ExpandableListView) findViewById(R.id.list);

        expandableList.setDividerHeight(2);
        expandableList.setGroupIndicator(null);
        expandableList.setClickable(true);

        listDataHeader = new ArrayList<String>();*/
       // answer = getIntent().getStringExtra("ANSWER_MAP");
       // bn.putString("QUESTION", question);
       // bn.putString("ANSWER", answer);

        if (getIntent().getStringExtra("QUESTION")!=null){
            vQuestion.setText(getIntent().getStringExtra("QUESTION")+"");
            vAnswer.setText(getIntent().getStringExtra("ANSWER")+"");
        }

       // String[] answerArrray = answer.split(",");

        titleTxt.setText(generalFunc.retrieveLangLBl("FAQ","LBL_FAQ_TXT"));

      /*  adapter = new QuestionAnswerEAdapter(getActContext(), listDataHeader, listDataChild);

        expandableList.setAdapter(adapter);


        expandableList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expandableList.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });*/

        backImgView.setOnClickListener(new setOnClickList());
        contact_us_btn.setOnClickListener(new setOnClickList());
        setData();
    }

    public void setData() {

        contact_us_btn.setText("" + generalFunc.retrieveLangLBl("", "LBL_CONTACT_US_HEADER_TXT"));
        textstillneedhelp.setText("" + generalFunc.retrieveLangLBl("", "LBL_STILL_NEED_HELP"));


      //  titleTxt.setText(generalFunc.getJsonValue("vTitle", getIntent().getStringExtra("QUESTION_LIST")));
     /*   JSONArray obj_ques = generalFunc.getJsonArray("Questions", getIntent().getStringExtra("QUESTION_LIST"));
        for (int i = 0; i < obj_ques.length(); i++) {
            JSONObject obj_temp = generalFunc.getJsonObject(obj_ques, i);


           // listDataHeader.add(generalFunc.getJsonValueStr("vTitle", obj_temp));

            List<String> answer = new ArrayList<String>();
            answer.add(generalFunc.getJsonValueStr("tAnswer", obj_temp));
//
           // listDataChild.put(listDataHeader.get(i), answer);
        }

       // adapter.notifyDataSetChanged();*/
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public Context getActContext() {
        return QuestionAnswerActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            switch (view.getId()) {
                case R.id.backImgView:
                    QuestionAnswerActivity.super.onBackPressed();
                    break;
                case R.id.contact_us_btn:
                    new StartActProcess(getActContext()).startAct(ContactUsActivity.class);
                    break;
            }
        }
    }
}
