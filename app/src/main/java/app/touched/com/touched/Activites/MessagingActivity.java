package app.touched.com.touched.Activites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import app.touched.com.touched.Adapter.MessagingAdapter;
import app.touched.com.touched.Interfaces.IMessaging;
import app.touched.com.touched.MainApplicationClass;
import app.touched.com.touched.Models.MessageModel;
import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.R;
import app.touched.com.touched.Utilities.Constants;
import app.touched.com.touched.Utilities.MessagingType;
import app.touched.com.touched.Utilities.RealPathUtil;
import app.touched.com.touched.Utilities.TimeUtils;
import app.touched.com.touched.Utilities.Utility;
import de.hdodenhof.circleimageview.CircleImageView;

import static app.touched.com.touched.Utilities.Constants.CAPTURE_IMAGE_REQ_CODE;
import static app.touched.com.touched.Utilities.Constants.FRIENDS_TAG;
import static app.touched.com.touched.Utilities.Constants.GALLERY_IMAGE_REQ_CODE;
import static app.touched.com.touched.Utilities.Constants.IS_DELIVERED_TAG;
import static app.touched.com.touched.Utilities.Constants.IS_LOGIN_NODE;
import static app.touched.com.touched.Utilities.Constants.META_NO;
import static app.touched.com.touched.Utilities.Constants.MSG_NODE;
import static app.touched.com.touched.Utilities.Constants.USERS_NODE;
import static app.touched.com.touched.Utilities.TimeUtils.getUniqueTime;

public class MessagingActivity extends BaseActivity implements View.OnClickListener, IMessaging {
    ImageButton imvGallery, imvCamera, imvGift1, imvGift2, imvGift3, imvGift4, imvCamera2, imvSendSms;
    TextView userName, userStatus;
    FrameLayout fl_attachment;
    EditText edt_sendSms;
    RecyclerView rcv_chat;
    private DatabaseReference dbToRootNode, dbToMyNode, dbToFriendNode, dbToFriendStatus;
    User_Details myDetails = new User_Details();
    User_Details myFriend = new User_Details();
    private FirebaseAuth mAuth;
    StorageReference storageReference;
    MessagingAdapter messagingAdapter;
    ArrayList<MessageModel> messageModelArrayList = new ArrayList<>();
    CircleImageView userImage;
    Uri selectedImageURI;
    UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        intializeControls();
        intitalizeObjects();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        rcv_chat.setLayoutManager(layoutManager);
        messagingAdapter = new MessagingAdapter(MessagingActivity.this, messageModelArrayList);
        rcv_chat.setAdapter(messagingAdapter);
    }

    private void intializeControls() {
        android.support.v7.widget.Toolbar toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.tool);
        setSupportActionBar(toolbar);
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
        userName = (TextView) findViewById(R.id.txvUserName);
        userStatus = (TextView) findViewById(R.id.txvUserStatus);
        imvSendSms.setOnClickListener(this);
        imvGallery.setOnClickListener(this);
        imvCamera.setOnClickListener(this);
    }

    private void intitalizeObjects() {
        myDetails = ((MainApplicationClass) getApplication()).getProfileUsersDetail();
        mAuth = ((MainApplicationClass) getApplication()).getmAuth();
        dbToRootNode = FirebaseDatabase.getInstance().getReference();
        storageReference = ((MainApplicationClass) getApplication()).getStorageRef();
        myFriend = getIntent().getExtras().getParcelable(FRIENDS_TAG);
        dbToMyNode = dbToRootNode.child(MSG_NODE).child(myDetails.getId()).child(myFriend.getId());
        dbToFriendNode = dbToRootNode.child(MSG_NODE).child(myFriend.getId()).child(myDetails.getId());
        dbToMyNode.addChildEventListener(myMsgCallBack);
        dbToFriendNode.addChildEventListener(friendMsgCallback);
        dbToFriendStatus = dbToRootNode.child(USERS_NODE).child(myFriend.getId());
        dbToFriendStatus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // Map values = dataSnapshot.getValue(Map.class);
                //userStatus.setText(values.get(IS_LOGIN_NODE).toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        userName.setText(Utility.getFullName(myFriend));

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
                MessageModel deliverReport = new MessageModel();
                deliverReport.setIsDelivered("true");
                deliverReport.setDeliveredTime(TimeUtils.getCurrentDateTime());
                dbToMyNode.child(getMsg.getMsg_id()).setValue(deliverReport);
                dbToFriendNode.child(getMsg.getMsg_id()).setValue(deliverReport);
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
                    data.setIsDelivered(getMsg.getIsDelivered());
                    data.setDeliveredTime(getMsg.getDeliveredTime());
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
                    MessageModel msgModel = new MessageModel(myDetails.getFirst_name(), myDetails.getEmail(), TimeUtils.getCurrentDateTime(), "false", MessagingType.TEXT.toString(), edt_sendSms.getText().toString().trim(), getUniqueTime(), true);
//                    messageModelArrayList.add(0,msgModel);
//                    messagingAdapter.notifyItemInserted(0);
                    edt_sendSms.getText().clear();
                    sendTextMsg(msgModel);
                }
                break;
            case R.id.imv_camera:
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePicture.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePicture, CAPTURE_IMAGE_REQ_CODE);
                }
                break;
            case R.id.imv_gallery:
                Intent galleryPicture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryPicture, GALLERY_IMAGE_REQ_CODE);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_REQ_CODE && resultCode == RESULT_OK && null != data) {
            try {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                FileOutputStream fo;
                try {
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                selectedImageURI = Uri.fromFile(destination);// file
//                Bitmap bitmap = Utility.decodeUri(this, uri);//bitmap
                MessageModel msgModel = new MessageModel(myDetails.getFirst_name(), myDetails.getEmail(), TimeUtils.getCurrentDateTime(), "true", MessagingType.IMAGE.toString(), null, getUniqueTime(), true);
                messageModelArrayList.add(0, msgModel);
                messagingAdapter.notifyItemInserted(0);
                uploadImage(msgModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == GALLERY_IMAGE_REQ_CODE && resultCode == RESULT_OK && null != data) {
            String realPath;
            Uri uri = data.getData();
            try {


                if (Build.VERSION.SDK_INT < 11) {
                    realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, uri);
                } else if (Build.VERSION.SDK_INT < 19) {
                    realPath = RealPathUtil.getRealPathFromURI_API11to18(this, uri);
                } else {
                    realPath = RealPathUtil.getRealPathFromURI_API19(this, uri);
                }
                File file = new File(realPath);
                selectedImageURI = Uri.fromFile(file);
                MessageModel msgModel = new MessageModel(myDetails.getFirst_name(), myDetails.getEmail(), TimeUtils.getCurrentDateTime(), "true", MessagingType.IMAGE.toString(), null, getUniqueTime(), true);
                messageModelArrayList.add(0, msgModel);
                messagingAdapter.notifyItemInserted(0);
                uploadImage(msgModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
    public void sendGiftMsg(MessageModel msg) {

    }

    //https://code.tutsplus.com/courses/use-firebase-as-the-back-end
    @Override
    public void uploadImage(final MessageModel msg) {
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .setCustomMetadata(META_NO, msg.getMeta_no())
                .build();
        uploadTask = storageReference.child(selectedImageURI.getLastPathSegment()).putFile(selectedImageURI, metadata);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int i = -1;
                for (MessageModel data : messageModelArrayList) {
                    i++;
                    if (data.getMeta_no().equals(msg.getMeta_no())) {
                        data.setIsSent("false");
                        messagingAdapter.notifyItemChanged(i);
                        break;
                        // now update image status of this msg
                    }
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                String content = downloadUrl.toString();
                String meta_no = taskSnapshot.getMetadata().getCustomMetadata(META_NO);
                if (content.length() > 0) {

                    int i = -1;
                    for (MessageModel data : messageModelArrayList) {
                        i++;
                        if (data.getMeta_no().equals(meta_no)) {
                            String id = dbToMyNode.push().getKey();
                            data.setMsg_id(id);
                            data.setMsgContent(content);


                            dbToMyNode.child(id).setValue(data);
                            dbToFriendNode.child(id).setValue(data);
                            messagingAdapter.notifyItemChanged(i);
                            break;
                            // now update image status of this msg
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.chatting_menu, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        if (id == R.id.action_attachment) {
            fl_attachment.setVisibility(View.VISIBLE);
        }
        return true;
    }
}
