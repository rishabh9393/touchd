package app.touched.com.touched.Activites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.touched.com.touched.Adapter.MessagingAdapter;
import app.touched.com.touched.Interfaces.DownloadCallback;
import app.touched.com.touched.Interfaces.IMessaging;
import app.touched.com.touched.MainApplicationClass;
import app.touched.com.touched.Models.DownloadDataModel;
import app.touched.com.touched.Models.MessageModel;
import app.touched.com.touched.Models.MessageSentModel;
import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.Models.Users;
import app.touched.com.touched.R;
import app.touched.com.touched.Utilities.CompressFile;
import app.touched.com.touched.Models.MessageStatus;
import app.touched.com.touched.Models.MessagingType;
import app.touched.com.touched.Utilities.PersistanceManager;
import app.touched.com.touched.Utilities.RealPathUtil;
import app.touched.com.touched.Utilities.TimeUtils;
import app.touched.com.touched.Utilities.Utility;
import de.hdodenhof.circleimageview.CircleImageView;

import static app.touched.com.touched.Utilities.Constants.CAPTURE_IMAGE_REQ_CODE;
import static app.touched.com.touched.Utilities.Constants.FRIENDS_TAG;
import static app.touched.com.touched.Utilities.Constants.GALLERY_IMAGE_REQ_CODE;
import static app.touched.com.touched.Utilities.Constants.META_NO;
import static app.touched.com.touched.Utilities.Constants.MSG_COUNT_NODE;
import static app.touched.com.touched.Utilities.Constants.MSG_NODE;
import static app.touched.com.touched.Utilities.Constants.POKE_GIFT_MSG_NODE;
import static app.touched.com.touched.Utilities.Constants.USERS_Details_NODE;
import static app.touched.com.touched.Utilities.Constants.USERS_NODE;
import static app.touched.com.touched.Utilities.TimeUtils.getUniqueTime;

public class MessagingActivity extends BaseActivity implements View.OnClickListener, IMessaging, DownloadCallback {
    ImageButton imvGallery, imvCamera, imvGift1, imvGift2, imvGift3, imvGift4, imvCamera2, imvSendSms;
    TextView userName, userStatus;
    FrameLayout fl_attachment;
    EditText edt_sendSms;
    RecyclerView rcv_chat;
    private LinearLayout lly_bottomChatLayout, lly_pokeLayout, lly_giftRcvLayout;
    private DatabaseReference dbToRootNode, dbToMyNode, dbToFriendNode, dbToFriendStatus, dbToMyPGNode;
    User_Details myDetails = new User_Details();
    User_Details myFriend = new User_Details();
    private FirebaseAuth mAuth;
    StorageReference storageReference;
    MessagingAdapter messagingAdapter;
    ArrayList<MessageModel> messageModelArrayList = new ArrayList<>();
    CircleImageView userImage;
    Uri selectedImageURI;
    UploadTask uploadTask;
    Bitmap map;
    String msg_count;
    private String mCurrentPhotoPath;
    private Uri photoURI;
    private ImageView imvBack;
    private Boolean isActiveUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        intializeControls();
        intitalizeObjects();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        rcv_chat.setLayoutManager(layoutManager);
        messagingAdapter = new MessagingAdapter(MessagingActivity.this, messageModelArrayList, myDetails.getPicture().getData().getUrl(), myFriend.getPicture().getData().getUrl(), this);
        rcv_chat.setAdapter(messagingAdapter);
        upDateTimelineToReadAllMsgs();

    }


    private void intializeControls() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.tool);
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
        imvBack = (ImageView) findViewById(R.id.imv_back);
        lly_bottomChatLayout = (LinearLayout) findViewById(R.id.bottomlayout);
        lly_pokeLayout = (LinearLayout) findViewById(R.id.lly_pokeLayout);
        lly_giftRcvLayout = (LinearLayout) findViewById(R.id.lly_giftRecieveLayout);

        imvSendSms.setOnClickListener(this);
        imvGallery.setOnClickListener(this);
        imvCamera.setOnClickListener(this);
        imvCamera.setOnClickListener(this);
        imvCamera2.setOnClickListener(this);
    }

    private void intitalizeObjects() {
        myDetails = ((MainApplicationClass) getApplication()).getProfileUsersDetail();
        mAuth = ((MainApplicationClass) getApplication()).getmAuth();
        dbToRootNode = FirebaseDatabase.getInstance().getReference();

        storageReference = ((MainApplicationClass) getApplication()).getStorageRef();
        myFriend = getIntent().getExtras().getParcelable(FRIENDS_TAG);
        dbToMyNode = dbToRootNode.child(MSG_NODE).child(myDetails.getKey()).child(myFriend.getKey());
        dbToFriendNode = dbToRootNode.child(MSG_NODE).child(myFriend.getKey()).child(myDetails.getKey());
        // Node to check either you have any poke or Gift
        // if you have poke you need to send gift
        // if you have gift you need to accept and chat with user on conditions
        dbToMyPGNode = dbToRootNode.child(POKE_GIFT_MSG_NODE).child(myDetails.getKey()).child(myFriend.getKey());
        dbToMyPGNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {

                    MessageModel myPGResponse = dataSnapshot.getValue(MessageModel.class);
                    if (myPGResponse.getMetaType().equals(MessagingType.POKE.toString())) {
                        lly_pokeLayout.setVisibility(View.VISIBLE);
                    } else if (myPGResponse.getMetaType().equals(MessagingType.GIFTS.toString())) {
                        lly_giftRcvLayout.setVisibility(View.VISIBLE);
                    }

                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
// node and event to check live status for online or offline
        dbToFriendStatus = dbToRootNode.child(USERS_NODE).child(myFriend.getKey());
        dbToFriendStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users frndStatus = dataSnapshot.getValue(Users.class);
                if (frndStatus.getIs_login().equals("true"))
                    userStatus.setText("online");
                else
                    userStatus.setText(frndStatus.getLast_online_time());
                // check for online or offline in realtime
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        userName.setText(Utility.getFullName(myFriend));
        if (isActiveUser) {
            lly_bottomChatLayout.setVisibility(View.VISIBLE);
            dbToMyNode.addChildEventListener(myMsgCallBack);
            dbToFriendNode.addChildEventListener(friendMsgCallback);
            dbToMyNode.keepSynced(true);
            dbToFriendNode.keepSynced(true);
        } else {
            lly_bottomChatLayout.setVisibility(View.GONE);
        }
        // check for the current users msg count and if it is less then 3 then check for
        // is they have more than 3 msgs then update the count and make that user old user
        if (Integer.parseInt(myDetails.getMsg_count()) >= 3) {
            if (messageModelArrayList.size() >= 3) {
                Map<String, Object> childUpdate = new HashMap<>();
                childUpdate.put(USERS_Details_NODE + "/" + myDetails.getKey() + "/" + MSG_COUNT_NODE, "3");
                dbToRootNode.updateChildren(childUpdate);
            }
        }

    }

    private void upDateTimelineToReadAllMsgs() {
        if (messageModelArrayList.size() > 0) {
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(getString(R.string.MYMESSAGESTAG) + "/" + myDetails.getKey() + "/" + myFriend.getKey() + "/isMessagesUnread", "false");
            dbToRootNode.updateChildren(childUpdates);
        }
    }
//    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            DownloadDataModel downloadDataModel = intent.getExtras().getParcelable("downloadedDetails");
//            if(downloadDataModel.getMsgId().equals())
//
//        }
//    };

    @Override
    protected void onResume() {
        super.onResume();
//        IntentFilter intentFilter = new IntentFilter(
//                "app.touched.com.touched.Activites.UpdateToUI");
//
//
//        //registering our receiver
//        this.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregisterReceiver(mReceiver);
        if (messageModelArrayList.size() > 0) {
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(getString(R.string.MYMESSAGESTAG) + "/" + myDetails.getKey() + "/" + myFriend.getKey() + "/isMessagesUnread", "false");
            dbToRootNode.updateChildren(childUpdates);
        }
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
                        messagingAdapter.notifyDataSetChanged();
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

            if (!messageModelArrayList.contains(getMsg)) {
                if (!getMsg.getSenderEmail().equals(myDetails.getEmail())) {
                    getMsg.setMine(false);
                }
                messageModelArrayList.add(0, getMsg);
                messagingAdapter.notifyItemInserted(0);
                messagingAdapter.notifyDataSetChanged();

            }
            if (!getMsg.isMine()) {
                if (getMsg.getDeliveredTime() == null) {
                    Map<String, Object> childUpdate = new HashMap<>();
                    childUpdate.put("isDelivered", "true");
                    childUpdate.put("deliveredTime", TimeUtils.getCurrentDateTime());
                    dbToMyNode.child(getMsg.getMsg_id()).updateChildren(childUpdate);
                    dbToFriendNode.child(getMsg.getMsg_id()).updateChildren(childUpdate);
                }
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
                    if (data.getIsMetaData().equals("true"))
                        data.setDownloadStatus(getMsg.getDownloadStatus());
                    messagingAdapter.notifyItemChanged(i);
                    messagingAdapter.notifyDataSetChanged();
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
            case R.id.imv_camera_bottom:
            case R.id.imv_camera:
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePicture.resolveActivity(getPackageManager()) != null) {

                    File photoFile = null;
                    try {
                        photoFile = CompressFile.createImageFile(MessagingActivity.this);

                    } catch (IOException ex) {
                        photoFile = null;
                        mCurrentPhotoPath = null;
                        // Error occurred while creating the File

                    }
                    if (photoFile != null) {

                        // Continue only if the File was successfully created
                        photoURI = Uri.fromFile(photoFile);
                        mCurrentPhotoPath = photoFile.getAbsolutePath();

                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);


                        startActivityForResult(takePicture, CAPTURE_IMAGE_REQ_CODE);
                    }
                }
                fl_attachment.setVisibility(View.GONE);
                break;
            case R.id.imv_gallery:
                Intent galleryPicture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryPicture, GALLERY_IMAGE_REQ_CODE);
                fl_attachment.setVisibility(View.GONE);
                break;
            case R.id.imv_back:
                onBackPressed();
                break;
            default:
                fl_attachment.setVisibility(View.GONE);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_REQ_CODE && resultCode == RESULT_OK) {
            try {
                File destination = new File(mCurrentPhotoPath);

//                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                selectedImageURI = Uri.fromFile(destination);// file

                //   map = Utility.decodeUri(this, selectedImageURI);//bitmap
                MessageModel msgModel = new MessageModel(myDetails.getFirst_name(), myDetails.getEmail(), TimeUtils.getCurrentDateTime(), "true", MessagingType.IMAGE.toString(), null, getUniqueTime(), true);
                msgModel.setLocalUri(selectedImageURI.getPath());
                msgModel.setMeta_no(TimeUtils.getUniqueTime() + ".jpg");

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
                // map = Utility.decodeUri(this, selectedImageURI);//bitmap
                MessageModel msgModel = new MessageModel(myDetails.getFirst_name(), myDetails.getEmail(), TimeUtils.getCurrentDateTime(), "true", MessagingType.IMAGE.toString(), null, getUniqueTime(), true);
                msgModel.setLocalUri(realPath);
                msgModel.setMeta_no(TimeUtils.getUniqueTime() + ".jpg");
                uploadImage(msgModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param msg      it is used to pass the Msg
     * @param isOnlyMe it tells weather you want to update my timeline only
     *                 or you want to friends timeline also
     *                 isOnlyMe=true-only update my timeline
     *                 isOnlyMe=false- update my and my friend's timeline
     *                 isOnlyMe=null- update myfriend's timeline
     *                 It update about new msg arrived/send
     */
    private void saveOnMyMessageList(MessageModel msg, Boolean isOnlyMe) {
        // model to show on my Message Fragment to show whom you send the data and who ever sent you the data.
        MessageSentModel dataToShowOnMyTimeline = new MessageSentModel();
        dataToShowOnMyTimeline.setMsg_content(msg.getMsgContent());
        dataToShowOnMyTimeline.setDeliveredTime(msg.getDeliveredTime());
        dataToShowOnMyTimeline.setEmail(myFriend.getEmail());
        dataToShowOnMyTimeline.setFirst_name(myFriend.getFirst_name());
        dataToShowOnMyTimeline.setLast_name(myFriend.getLast_name());
        dataToShowOnMyTimeline.setDeliveredTime(msg.getDeliveredTime());
        dataToShowOnMyTimeline.setId(myFriend.getId());
        dataToShowOnMyTimeline.setIsMetaData(msg.getIsMetaData());
        dataToShowOnMyTimeline.setIsDelivered(msg.getIsDelivered());
        dataToShowOnMyTimeline.setIsRead(msg.getIsRead());
        dataToShowOnMyTimeline.setIsSent(msg.getIsSent());
        dataToShowOnMyTimeline.setKey(myFriend.getKey());
        dataToShowOnMyTimeline.setMetaType(msg.getMetaType());
        dataToShowOnMyTimeline.setMsg_count(msg_count);
        dataToShowOnMyTimeline.setPicture(myFriend.getPicture());
        dataToShowOnMyTimeline.setSenderEmail(msg.getSenderEmail());
        dataToShowOnMyTimeline.setSentTime(msg.getSentTime());
        dataToShowOnMyTimeline.setTimestamp(msg.getTimestamp());
        dataToShowOnMyTimeline.setIsMessagesUnread("false");

        // model to show on my friends Message Fragment timeline to show whom you send the data and who ever sent you the data.

        MessageSentModel dataToShowOnFriendTimeline = new MessageSentModel();
        dataToShowOnFriendTimeline.setMsg_content(msg.getMsgContent());
        dataToShowOnFriendTimeline.setDeliveredTime(msg.getDeliveredTime());
        dataToShowOnFriendTimeline.setEmail(myDetails.getEmail());
        dataToShowOnFriendTimeline.setFirst_name(myDetails.getFirst_name());
        dataToShowOnFriendTimeline.setLast_name(myDetails.getLast_name());
        dataToShowOnFriendTimeline.setDeliveredTime(msg.getDeliveredTime());
        dataToShowOnFriendTimeline.setId(myDetails.getId());
        dataToShowOnFriendTimeline.setIsMetaData(msg.getIsMetaData());
        dataToShowOnFriendTimeline.setIsDelivered(msg.getIsDelivered());
        dataToShowOnFriendTimeline.setIsRead("false");
        dataToShowOnFriendTimeline.setIsSent(msg.getIsSent());
        dataToShowOnFriendTimeline.setKey(myDetails.getKey());
        dataToShowOnFriendTimeline.setMetaType(msg.getMetaType());
        dataToShowOnFriendTimeline.setMsg_count(msg_count);
        dataToShowOnFriendTimeline.setPicture(myDetails.getPicture());
        dataToShowOnFriendTimeline.setSenderEmail(msg.getSenderEmail());
        dataToShowOnFriendTimeline.setSentTime(msg.getSentTime());
        dataToShowOnFriendTimeline.setTimestamp(msg.getTimestamp());
        dataToShowOnFriendTimeline.setIsMessagesUnread("true");
        Map<String, Object> childUpdates = new HashMap<>();
        if (isOnlyMe == null) {
            childUpdates.put(getString(R.string.MYMESSAGESTAG) + "/" + myFriend.getKey() + "/" + myDetails.getKey(), dataToShowOnFriendTimeline);

        } else if (isOnlyMe) {
            childUpdates.put(getString(R.string.MYMESSAGESTAG) + "/" + myDetails.getKey() + "/" + myFriend.getKey(), dataToShowOnMyTimeline);
        } else if (!isOnlyMe) {
            childUpdates.put(getString(R.string.MYMESSAGESTAG) + "/" + myFriend.getKey() + "/" + myDetails.getKey(), dataToShowOnFriendTimeline);
            childUpdates.put(getString(R.string.MYMESSAGESTAG) + "/" + myDetails.getKey() + "/" + myFriend.getKey(), dataToShowOnMyTimeline);
        }
        dbToRootNode.updateChildren(childUpdates);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbToMyNode.removeEventListener(myMsgCallBack);
        dbToFriendNode.removeEventListener(friendMsgCallback);
    }


    @Override
    public void sendTextMsg(MessageModel msg) {
        saveOnMyMessageList(msg, false);
        String id = dbToMyNode.push().getKey();
        msg.setMsg_id(id);
        dbToMyNode.child(id).setValue(msg);
        messageModelArrayList.add(0, msg);
        messagingAdapter.notifyItemInserted(0);
        messagingAdapter.notifyDataSetChanged();
        dbToFriendNode.child(id).setValue(msg);


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
        String id = dbToMyNode.push().getKey();
        msg.setMsg_id(id);
        saveOnMyMessageList(msg, true); // set my timeline that i am sending the image
        dbToMyNode.child(id).setValue(msg);
        messageModelArrayList.add(0, msg);
        messagingAdapter.notifyItemInserted(0);
        messagingAdapter.notifyDataSetChanged();

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .setCustomMetadata(META_NO, msg.getMeta_no())
                .build();

        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(selectedImageURI.getPath());
        Bitmap bitmapThumb = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(selectedImageURI.getPath()), 50, 50);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapThumb.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        final byte[] thumbData = baos.toByteArray();
// compress the file if it is more than 1 mb size
        if (sourceFile.length() / maxBufferSize >= 1)
            sourceFile = CompressFile.getCompressedImageFile(sourceFile, getApplicationContext());

        uploadTask = storageReference.child(myDetails.getKey()).child(myFriend.getKey()).child(msg.getMeta_no()).putFile(Uri.fromFile(sourceFile), metadata);
        final File finalSourceFile = sourceFile;
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MessagingActivity.this, "error upload", Toast.LENGTH_LONG).show();
//                int i = -1;
//                for (MessageModel data : messageModelArrayList) {
//                    i++;
//                    if (data.getMeta_no().equals(msg.getMeta_no())) {
//                        data.setIsSent("false");
//                        messagingAdapter.notifyItemChanged(i);
//                        messagingAdapter.notifyDataSetChanged();
//                        saveOnMyMessageList(msg);
//                        break;
//                        // now update image status of this msg
//                    }
//                }
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                final String content = downloadUrl.toString();
                final String meta_no = taskSnapshot.getMetadata().getCustomMetadata(META_NO);

                UploadTask uploadThumb = storageReference.child(myDetails.getKey()).child(myFriend.getKey()).child(msg.getMeta_no()).child("thumbnail").putBytes(thumbData);
                uploadThumb.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        Uri thumb = taskSnapshot.getDownloadUrl();
                        String thumbUrl = thumb.toString();
                        if (content.length() > 0) {

                            int i = -1;
                            for (MessageModel data : messageModelArrayList) {
                                i++;
                                if (data.getMeta_no().equals(meta_no)) {

                                    data.setMsgContent(content);
                                    data.setThumbImage(thumbUrl);
                                    messagingAdapter.notifyItemChanged(i);
                                    messagingAdapter.notifyDataSetChanged();
                                    Map<String, Object> childUpdates = new HashMap<>();
                                    childUpdates.put("msgContent", data.getMsgContent());
                                    childUpdates.put("thumbImage", data.getThumbImage());
                                    dbToMyNode.child(data.getMsg_id()).updateChildren(childUpdates);
                                    dbToFriendNode.child(data.getMsg_id()).setValue(data);

                                    saveOnMyMessageList(msg, null);


                                    break;
                                    // now update image status of this msg
                                }
                            }
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

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
            if (fl_attachment.getVisibility() == View.VISIBLE)
                fl_attachment.setVisibility(View.GONE);
            else
                fl_attachment.setVisibility(View.VISIBLE);
        }
        return true;
    }

    @Override
    public void downloadStartSetValue(final int position, final long downloadId, final MessageModel msg) {
        if (messageModelArrayList.contains(msg)) {
            DownloadDataModel downloadDataModel = new DownloadDataModel(myFriend.getKey(), myDetails.getKey(), msg.getMsg_id());
            PersistanceManager.getInstance(MessagingActivity.this).setPersistanceObjectValue(String.valueOf(downloadId), downloadDataModel);

            msg.setDownloadStatus(MessageStatus.PROGRESS.toString());
            msg.setDownloadUid(String.valueOf(downloadId));
            Map<String, Object> childUpdate = new HashMap<>();
            childUpdate.put("downloadStatus", msg.getDownloadStatus());
            childUpdate.put("downloadUid", msg.getDownloadUid());
            dbToMyNode.child(msg.getMsg_id()).updateChildren(childUpdate);
            messagingAdapter.notifyItemChanged(position);
            messagingAdapter.notifyDataSetChanged();

        }
    }
}
