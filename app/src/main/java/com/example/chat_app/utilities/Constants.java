package com.example.chat_app.utilities;

import java.util.HashMap;

public final class Constants {
    //COLLECTIONS
    public static final String KEY_COLLECTION_USERS = "users";
    public static final String KEY_COLLECTION_GROUPS = "group";
    public static final String KEY_COLLECTION_INVITATIONS = "invitations";
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_COLLECTION_CONVERSATIONS = "conversations";

    //FIELDS OF USER
    public static final String KEY_AVAILABILITY = "availability";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_LIST_FRIEND="listIdFriend";
    public static final String KEY_NAME = "name";
    public static final String KEY_NUMBER_PHONE="numberPhone";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USER_ID = "userId";

    //FIELDS OF GROUP
    public static final String KEY_ENCODE_IMAGE="enCodeImage";
    public static final String KEY_GROUP_ID="groupId";
    public static final String KEY_ID_MEMBERS="idMember";
    public static final String KEY_NAME_GROUP="nameGroup";
    public static final String KEY_WATCHEDS="watcheds";
    public static final String KEY_USERS_ONLINE="usersOnline";

    //FIELDS OF INVITATION
    public static final String KEY_INVITATION_ID="invitationId";

    //FIELDS OF MESSAGE
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY__RECEIVER_ID = "receiverId";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "timestamp";

    //FIELDS OF CONVERSATION
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_RECEIVER_NAME = "receiverName";
    public static final String KEY_SENDER_IMAGE = "senderImage";
    public static final String KEY_RECEIVER_IMAGE = "receiverImage";
    public static final String KEY_LAST_MESSAGE = "lastMessage";
    public static final String KEY_NEW_MESSAGE_OF="newMessageOf";

    //PREFERENCE
    public static final String KEY_PREFERENCE_NAME = "chatAppPreference";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";
    public static final String KEY_CONVERSATION_ID="conversationId";

    //NAME OBJECT
    public static final String KEY_USER = "user";

    //
    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";
    public static HashMap<String, String> remoteMsgHeaders = null;
    public static HashMap<String, String> getRemoteMsgHeaders() {
        if (remoteMsgHeaders == null) {
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(REMOTE_MSG_AUTHORIZATION,
                    "key=AAAAKc81BGA:APA91bFlET-mJk-kDjAvdpW-sZIT2ERh2eDeCoOjqj8-ad13gsFWx8_8X6V2M7OVnvRQlIzzK2NCcbN81YB3vDddQMQ8GlcsxMS5NePPoWeTolp_42C6GeMYo_xpLmzQaPb7b5fstk9T");
            remoteMsgHeaders.put(REMOTE_MSG_CONTENT_TYPE, "application/json");
        }
        return remoteMsgHeaders;
    }


}
