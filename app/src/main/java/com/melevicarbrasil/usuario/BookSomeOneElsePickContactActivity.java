package com.melevicarbrasil.usuario;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.adapter.files.BookSomeOneContactListAdapter;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.model.ContactModel;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.MyProgressDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class BookSomeOneElsePickContactActivity extends AppCompatActivity {
    GeneralFunctions generalFunc;
    String userProfileJson;

    List<ContactModel> list = new ArrayList<>();
    BookSomeOneContactListAdapter bookSomeOneContactListAdapter;
    public List<ContactModel> mSectionList = new ArrayList<>();
    public List<ContactModel> finalList = new ArrayList<>();

    RecyclerView contactListRecyclerView;
    EditText searchTxt;
    ImageView backImgView;
    LinearLayout closeArea;
    MTextView titleTxt;
    MTextView payTypeTxt;
    public LinearLayout pickContactArea;

    public String selectedRowId = "-1";
    MButton btn_type2;
    int submitBtnId;
    MyProgressDialog myPDialog;

    boolean mIsLoading = false;
    boolean isNextPageAvailable = false;
    boolean isInProcess = false;

    ContactModel selectPrams;
    String colors[] = {"#2ECC71", "#F1C40F", "#D35400", "#7F8C8D", "#9B59B6", "#3498DB", "#C0392B"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.design_book_someone_pick_contact);
        initView();


        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);

        setData();

        searchTxt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (bookSomeOneContactListAdapter != null) {
                    bookSomeOneContactListAdapter.getFilter().filter(v.getText().toString());
                }
                return true;
            }
            return false;
        });

        searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (bookSomeOneContactListAdapter != null) {
                    bookSomeOneContactListAdapter.getFilter().filter(searchTxt.getText().toString().trim());
                }
            }
        });

        contactListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

    }

    private void setData() {
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CHOOSE_CONTACT_TITLE_TXT"));
        payTypeTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CHOOSE_CONTACT_HINT_TXT"));
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_CHOOSE_CONTACT_CONT_TXT"));
        searchTxt.setHint(generalFunc.retrieveLangLBl("", "LBL_SEARCH_CONTACT_HINT_TXT"));

        new CreateRoundedView(Color.parseColor("#23559A"), Utils.dipToPixels(this, 5), 0,
                Color.parseColor("#23559A"), findViewById(R.id.pickContactArea));

        submitBtnId = Utils.generateViewId();
        btn_type2.setId(submitBtnId);

        btn_type2.setOnClickListener(new setOnClick());

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadList();
    }

    public void loadList() {
        LoadContact loadContact = new LoadContact();
        loadContact.execute();
    }

    public Context getActContext() {
        return BookSomeOneElsePickContactActivity.this;
    }

    private void initView() {
        contactListRecyclerView = findViewById(R.id.contactListRecyclerView);
        searchTxt = findViewById(R.id.searchTxt);
        backImgView = findViewById(R.id.backImgView);
        pickContactArea = findViewById(R.id.pickContactArea);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        payTypeTxt = (MTextView) findViewById(R.id.payTypeTxt);
        closeArea = (LinearLayout) (findViewById(R.id.closeArea));

        backImgView.setOnClickListener(new setOnClick());
        closeArea.setOnClickListener(new setOnClick());

        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();

    }


    class LoadContact extends AsyncTask<Void, Void, Void> implements BookSomeOneContactListAdapter.ItemClickListener {
        @Override
        protected void onPreExecute() {
            list = new ArrayList<>();
            mSectionList = new ArrayList<>();
            finalList = new ArrayList<>();
            super.onPreExecute();
            myPDialog = new MyProgressDialog(getActContext(), false, generalFunc.retrieveLangLBl("Wait..!! Gathering your contacts", "LBL_RETRIVE_CONTACT_TXT"));
            try {
                myPDialog.show();
            } catch (Exception e) {

            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Get Contact list from Phone
            ContentResolver cResolver = getActContext().getContentResolver();
            ContentProviderClient mCProviderClient = cResolver.acquireContentProviderClient(ContactsContract.Contacts.CONTENT_URI);

            try {

                final String[] PROJECTION = new String[] {
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                        ContactsContract.CommonDataKinds.Contactables.PHOTO_URI
                };

                Cursor mCursor = mCProviderClient.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, ContactsContract.Contacts.HAS_PHONE_NUMBER, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");


                final int idIdx = mCursor.getColumnIndex(ContactsContract.Contacts._ID);
                final int nameIdx = mCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int photoIdx = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.PHOTO_URI);
                final int numberIdx = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);


                if (mCursor != null && mCursor.getCount() > 0) {
                    mCursor.moveToFirst();
                    while (mCursor.moveToNext()) {
                        String contactId = mCursor.getString(idIdx);
                        String displayName = mCursor.getString(nameIdx);
                        String photoURI = mCursor.getString(photoIdx);
                        String phoneNumber = mCursor.getString(numberIdx);

                        ContactModel info = new ContactModel();
                        info.id = contactId;
                        info.name = displayName;
                        info.nameLbl = displayName;
                        info.mobileNumber = phoneNumber;
                        info.photo = "";
                        info.photoURI = photoURI;
                        list.add(info);

                    }

                }

                mCursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            getHeaderListLatter(list);

            if (myPDialog != null) {
                myPDialog.close();
            }

            bookSomeOneContactListAdapter = new BookSomeOneContactListAdapter(contactListRecyclerView, getActContext(), mSectionList, generalFunc, false);
            contactListRecyclerView.setAdapter(bookSomeOneContactListAdapter);
            bookSomeOneContactListAdapter.onClickListener(this);
            bookSomeOneContactListAdapter.notifyDataSetChanged();

        }

        @Override
        public void setSelected(int position, String rowId) {
            if (selectedRowId.equalsIgnoreCase("-1")) {
                selectedRowId = rowId;
                selectPrams = mSectionList.get(position);
                searchTxt.setText("");
                findViewById(R.id.submitBtnArea).setVisibility(View.VISIBLE);
                pickContactArea.setVisibility(View.VISIBLE);
                searchTxt.setHint("");
                ((MTextView) findViewById(R.id.pickedConatactTxt)).setText(mSectionList.get(position).name);
            } else if (!selectedRowId.equalsIgnoreCase(rowId) && pickContactArea.getVisibility() == View.VISIBLE) {
                generalFunc.showMessage(findViewById(R.id.submitBtnArea), generalFunc.retrieveLangLBl("", "LBL_MAX_CONTACT_WARNING_TXT"));
            } else if (pickContactArea.getVisibility() == View.VISIBLE && selectedRowId.equalsIgnoreCase(rowId)) {
                resetParams();
            }
        }

    }

    private void resetParams() {
        selectPrams = null;
        selectedRowId = "-1";
        pickContactArea.setVisibility(View.GONE);
//        if(!searchTxt.getText().toString().trim().equals("")){
            searchTxt.setHint(generalFunc.retrieveLangLBl("", "LBL_SEARCH_CONTACT_HINT_TXT"));
            searchTxt.setText("");
//        }
        findViewById(R.id.submitBtnArea).setVisibility(View.GONE);

    }

    public String getDigitsFromText(String number) {
        String no = number;
        StringBuilder builder = new StringBuilder();
        if (Utils.checkText(number)) {
            for (int i = 0; i < number.length(); i++) {
                char c = number.charAt(i);
                char c1 = new Character('+');

                if (Character.isDigit(c) || ((c == c1) && i == 0)) {
                    builder.append(c);
                }
            }
            no = builder.toString();
        }

        return no;
    }

    public void removeNextPageConfig() {
        isNextPageAvailable = false;
        mIsLoading = false;
        bookSomeOneContactListAdapter.removeFooterView();
    }

    private void storedDetails(ContactModel contactModel) {

        getStoredDetails(contactModel);
    }

    private List<ContactModel> getStoredDetails(ContactModel contactModel) {
        List<ContactModel> tempList = new ArrayList<>();
        List<ContactModel> finalList = new ArrayList<>();
        boolean isExist = false;

        if (Utils.checkText(generalFunc.retrieveValue(Utils.LIST_CONTACTS_KEY))) {

            Gson gson = new Gson();
            String data1 = generalFunc.retrieveValue(Utils.LIST_CONTACTS_KEY);
            List<ContactModel> listofViewsData = gson.fromJson(data1, new TypeToken<List<ContactModel>>() {
                    }.getType()
            );

            if (tempList == null) {
                tempList = new ArrayList<>();
            }

            for (int i = 0; i < listofViewsData.size(); i++) {
                if (listofViewsData.get(i).name.equalsIgnoreCase(contactModel.name) && listofViewsData.get(i).mobileNumber.equalsIgnoreCase(contactModel.mobileNumber)) {
                    {
                        isExist = true;
                        listofViewsData.remove(i);
                        break;
                    }
                }
            }

            tempList.addAll(listofViewsData);

            Collections.reverse(tempList);
        }

        tempList.add(contactModel);

        Collections.reverse(tempList);

        int limitShowContacts = generalFunc.parseIntegerValue(2, generalFunc.retrieveValue(Utils.BOOK_FOR_ELSE_SHOW_NO_CONTACT_KEY));

        if (tempList.size() > limitShowContacts) {
            int fromNo = 0;
            int toNo = limitShowContacts;
            finalList.addAll(tempList.subList(fromNo, toNo));
        } else {
            finalList.addAll(tempList);
        }


        Gson gson = new Gson();
        String json = gson.toJson(finalList);

        isInProcess = true;
        generalFunc.removeValue(Utils.LIST_CONTACTS_KEY);
        generalFunc.storeData(Utils.LIST_CONTACTS_KEY, json);
        isInProcess = false;

        backImgView.performClick();

        return tempList;
    }

    private List<ContactModel> getHeaderListLatter(List<ContactModel> usersList) {
        Collections.sort(usersList, new Comparator<ContactModel>() {
            @Override
            public int compare(ContactModel user1, ContactModel user2) {
                return String.valueOf(user1.name.charAt(0)).toUpperCase(Locale.US).compareTo(String.valueOf(user2.name.charAt(0)).toUpperCase(Locale.US));
            }
        });

        String lastHeader = "";

        int size = usersList.size();
        int colorIndex = 0;

        for (int i = 0; i < size; i++) {

            ContactModel user = usersList.get(i);
            String header = String.valueOf(user.nameLbl.charAt(0)).toUpperCase(Locale.US);
            user.mobileNumber = getDigitsFromText(user.mobileNumber);
            user.nameChar = header;
            user.colorVal = colors[colorIndex];
            if (colorIndex == 6) {
                colorIndex = 0;
            }
            colorIndex++;
            if (!TextUtils.equals(lastHeader, header)) {
                lastHeader = header;
                mSectionList.add(new ContactModel(header, true));
            }

            mSectionList.add(user);
        }
        finalList.addAll(mSectionList);
        return mSectionList;
    }

    @Override
    public void onBackPressed() {
        if (isInProcess) {
            generalFunc.showMessage(findViewById(R.id.submitBtnArea), generalFunc.retrieveLangLBl("", "LBL_WAIT"));
            return;
        }
        super.onBackPressed();
    }

    public class setOnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == R.id.backImgView) {
                onBackPressed();
            } else if (i == R.id.closeArea) {
                resetParams();
            } else if (i == submitBtnId) {

                if (selectPrams != null) {
                    Gson gson = new Gson();
                    String json = gson.toJson(selectPrams);
                    generalFunc.storeData(Utils.BFSE_SELECTED_CONTACT_KEY, json);
                    storedDetails(selectPrams);
                }
            }
        }
    }

}
