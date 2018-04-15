package app.touched.com.touched.Activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import app.touched.com.touched.Adapter.MessagingAdapter;
import app.touched.com.touched.Interfaces.IMessaging;
import app.touched.com.touched.MainApplicationClass;
import app.touched.com.touched.Models.MessageModel;
import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.R;
import app.touched.com.touched.Utilities.Constants;
import app.touched.com.touched.Utilities.MessagingType;
import app.touched.com.touched.Utilities.TimeUtils;
import de.hdodenhof.circleimageview.CircleImageView;

import static app.touched.com.touched.Utilities.Constants.FRIENDS_TAG;
import static app.touched.com.touched.Utilities.Constants.MSG_NODE;
import static app.touched.com.touched.Utilities.Constants.USERS_NODE;

public class MessagingActivity extends AppCompatActivity implements View.OnClickListener, IMessaging {
    ImageButton imvGallery, imvCamera, imvGift1, imvGift2, imvGift3, imvGift4, imvCamera2, imvSendSms;
    FrameLayout fl_attachment;
    EditText edt_sendSms;
    RecyclerView rcv_chat;
    private DatabaseReference dbToRootNode, dbToMyNode, dbToFriendNode;
    User_Details myDetails = new User_Details();
    User_Details myFriend = new User_Details();
    private FirebaseAuth mAuth;

    MessagingAdapter messagingAdapter;
    ArrayList<MessageModel> messageModelArrayList = new ArrayList<>();
    CircleImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        intitalizeObjects();
        intializeControls();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        rcv_chat.setLayoutManager(layoutManager);
        messagingAdapter = new MessagingAdapter(MessagingActivity.this, messageModelArrayList);
        rcv_chat.setAdapter(messagingAdapter);
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
        userImage = (CircleImageView) findViewById(R.id.civ_profile);
        imvSendSms.setOnClickListener(this);
    }

    private void intitalizeObjects() {
        myDetails = ((MainApplicationClass) getApplication()).getProfileUsersDetail();
        mAuth = ((MainApplicationClass) getApplication()).getmAuth();
        dbToRootNode = FirebaseDatabase.getInstance().getReference();
        myFriend = getIntent().getExtras().getParcelable(FRIENDS_TAG);
        dbToMyNode = dbToRootNode.child(MSG_NODE).child(myDetails.getId() + "_" + myFriend.getId());
        dbToFriendNode = dbToRootNode.child(MSG_NODE).child(myFriend.getId() + "_" + myDetails.getId());
        dbToMyNode.addChildEventListener(myMsgCallBack);
        dbToFriendNode.addChildEventListener(friendMsgCallback);

    }

    private ChildEventListener friendMsgCallback = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            MessageModel getMsg = dataSnapshot.getValue(MessageModel.class);


            if (getMsg.getSenderEmail().equals(myDetails.getEmail())) {
                int i = -1;
                for (MessageModel data : messageModelArrayList) {
                    i++;
                    if (data.getMsg_id().equals(getMsg.getMsg_id())) {
                        data.setIsSent("true");
                        data.setSentTime(TimeUtils.getCurrentDateTime());
                        messagingAdapter.notifyItemChanged(i);
                        break;
                        // now update image status of this msg
                    }
                }
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private ChildEventListener myMsgCallBack = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            MessageModel getMsg = dataSnapshot.getValue(MessageModel.class);

            if (!getMsg.getSenderEmail().equals(myDetails.getEmail())) {
                getMsg.setMine(false);
                messageModelArrayList.add(0, getMsg);
                messagingAdapter.notifyItemInserted(0);

            }
            // as msg get need to send the status of delivery to firebase
            // change value in frnd data


        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            MessageModel getMsg = dataSnapshot.getValue(MessageModel.class);
            int i = -1;
            for (MessageModel data : messageModelArrayList) {
                i++;
                if (data.getMsg_id().equals(getMsg.getMsg_id())) {
                    data.setIsDelivered("true");
                    data.setDeliveredTime(TimeUtils.getCurrentDateTime());
                    messagingAdapter.notifyItemChanged(i);
                    break;
                    // now update image status of this msg
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_sendMsg:
                if (!edt_sendSms.getText().toString().isEmpty()) {
                    MessageModel msgModel = new MessageModel(myDetails.getFirst_name(), myDetails.getEmail(), TimeUtils.getCurrentDateTime(), "false", MessagingType.TEXT.toString(), edt_sendSms.getText().toString().trim());
                    edt_sendSms.getText().clear();
                    sendTextMsg(msgModel);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbToMyNode.removeEventListener(myMsgCallBack);
    }

    @Override
    public void sendTextMsg(MessageModel msg) {
        String id = dbToMyNode.push().getKey();
        msg.setMine(true);
        msg.setMsg_id(id);
        dbToMyNode.child(id).setValue(msg);
        dbToFriendNode.child(id).setValue(msg);
        messageModelArrayList.add(0, msg);
        messagingAdapter.notifyItemInserted(0);
    }

    @Override
    public void deleteMsg(MessageModel msg) {
        dbToMyNode.child(msg.getMsg_id()).removeValue();
        messagingAdapter.notifyItemRemoved(messageModelArrayList.size());
    }

    @Override
    public void updateMsg(MessageModel msg) {

    }

    @Override
    public void msgDelivered(MessageModel msg) {

    }

    @Override
    public void msgRead(MessageModel msg) {

    }

    @Override
    public void sendImageMsg(MessageModel msg) {

    }

    @Override
    public void sendGiftMsg(MessageModel msg) {

    }
}
