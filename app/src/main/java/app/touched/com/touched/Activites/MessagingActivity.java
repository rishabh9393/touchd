package app.touched.com.touched.Activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import app.touched.com.touched.Adapter.MessagingAdapter;
import app.touched.com.touched.MainApplicationClass;
import app.touched.com.touched.Models.MessageModel;
import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.R;
import app.touched.com.touched.Utilities.Constants;

import static app.touched.com.touched.Utilities.Constants.FRIENDS_TAG;
import static app.touched.com.touched.Utilities.Constants.USERS_NODE;

public class MessagingActivity extends AppCompatActivity {
    ImageButton imvGallery, imvCamera, imvGift1, imvGift2, imvGift3, imvGift4, imvCamera2, imvSendSms;
    FrameLayout fl_attachment;
    EditText edt_sendSms;
    RecyclerView rcv_chat;
    private DatabaseReference dbToRootNode, dbToMyNode, dbToFriendNode;
    User_Details myDetails = new User_Details();
    User_Details myFriend = new User_Details();
    private FirebaseAuth mAuth;
    private String friendId;
    MessagingAdapter messagingAdapter;
    ArrayList<MessageModel> messageModelArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        intitalizeObjects();
        intializeControls();
        friendId = getIntent().getExtras().getString(FRIENDS_TAG);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        rcv_chat.setLayoutManager(layoutManager);
        messagingAdapter = new MessagingAdapter(MessagingActivity.this, messageModelArrayList);

    }

    private void intializeControls() {
        imvGallery = (ImageButton) findViewById(R.id.imv_gallery);
        imvCamera = (ImageButton) findViewById(R.id.imv_camera);
        imvGift1 = (ImageButton) findViewById(R.id.imv_gift1);
        imvGift2 = (ImageButton) findViewById(R.id.imv_gift2);
        imvGift3 = (ImageButton) findViewById(R.id.imv_gift3);
        imvGift4 = (ImageButton) findViewById(R.id.imv_gift4);
        imvCamera2 = (ImageButton) findViewById(R.id.imv_camera_bottom);
        imvSendSms = (ImageButton) findViewById(R.id.imv_sendMsg);
        fl_attachment = (FrameLayout) findViewById(R.id.fl_attachment);
        edt_sendSms = (EditText) findViewById(R.id.edt_writeMsg);
        rcv_chat = (RecyclerView) findViewById(R.id.rev_chat);
    }

    private void intitalizeObjects() {
        myDetails = ((MainApplicationClass) getApplication()).getProfileUsersDetail();
        mAuth = ((MainApplicationClass) getApplication()).getmAuth();
        dbToRootNode = FirebaseDatabase.getInstance().getReference();
        dbToMyNode = dbToRootNode.child(USERS_NODE).child(myDetails.getId());

    }
}
