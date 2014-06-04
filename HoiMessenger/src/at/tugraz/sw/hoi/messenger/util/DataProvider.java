package at.tugraz.sw.hoi.messenger.util;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.preference.PreferenceManager;
import at.tugraz.sw.hoi.messenger.remote.Configuration;

public class DataProvider extends ContentProvider {

  public static final Uri CONTENT_URI_MESSAGES = Uri.parse("content://at.tugraz.sw.hoi.messenger.provider/messages");
  public static final Uri CONTENT_URI_PROFILE = Uri.parse("content://at.tugraz.sw.hoi.messenger.provider/profile");
  public static final Uri CONTENT_URI_CONVERSATIONS = Uri
      .parse("content://at.tugraz.sw.hoi.messenger.provider/messages/conversations");
  // public static final Uri CONTENT_URI_OTR =
  // Uri.parse("content://at.tugraz.sw.hoi.messenger.provider/otr");

  public static final String COL_ID = "_id";

  public enum MessageType {

    INCOMING, OUTGOING
  }

  // parameters recognized by demo server
  public static final String SENDER_EMAIL = "senderEmail";
  public static final String RECEIVER_EMAIL = "receiverEmail";
  public static final String REG_ID = "regId";
  public static final String MESSAGE = "message";

  // TABLE MESSAGE
  public static final String TABLE_MESSAGES = "messages";
  public static final String COL_TYPE = "type";
  public static final String COL_SENDER_EMAIL = "senderEmail";
  public static final String COL_RECEIVER_EMAIL = "receiverEmail";
  public static final String COL_MESSAGE = "message";
  public static final String COL_TIME = "time";

  // TABLE PROFILE
  public static final String TABLE_PROFILE = "profile";
  public static final String COL_NAME = "name";
  public static final String COL_EMAIL = "email";
  public static final String COL_COUNT = "count";
  public static final String COL_ACCOUNT = "accountID";
  public static final String COL_SECURED = "secured";
  public static final String COL_LAST_KEY = "lastPubKey";

  // TABLE OTR
  // public static final String TABLE_OTR = "otr";
  // public static final String COL_STATE = "state";
  // public static final String COL_RECEIVER = "account";
  // public static final String COL_COUNT = "user";

  private DbHelper dbHelper;

  private static final int MESSAGES_ALLROWS = 1;
  private static final int MESSAGES_SINGLE_ROW = 2;
  private static final int PROFILE_ALLROWS = 3;
  private static final int PROFILE_SINGLE_ROW = 4;
  private static final int MESSAGES_CONVERSATIONS = 5;

  private static final UriMatcher uriMatcher;
  static {
    uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    uriMatcher.addURI("at.tugraz.sw.hoi.messenger.provider", "messages", MESSAGES_ALLROWS);
    uriMatcher.addURI("at.tugraz.sw.hoi.messenger.provider", "messages/#", MESSAGES_SINGLE_ROW);
    uriMatcher.addURI("at.tugraz.sw.hoi.messenger.provider", "profile", PROFILE_ALLROWS);
    uriMatcher.addURI("at.tugraz.sw.hoi.messenger.provider", "profile/#", PROFILE_SINGLE_ROW);
    uriMatcher.addURI("at.tugraz.sw.hoi.messenger.provider", "messages/conversations", MESSAGES_CONVERSATIONS);
  }

  @Override
  public boolean onCreate() {
    dbHelper = new DbHelper(getContext());
    return true;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

    SQLiteDatabase db = dbHelper.getReadableDatabase();
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

    Cursor c = null;
    switch (uriMatcher.match(uri)) {
    case MESSAGES_ALLROWS:
      qb.setTables(TABLE_MESSAGES);
      break;

    case MESSAGES_SINGLE_ROW:
      qb.setTables(TABLE_MESSAGES);
      qb.appendWhere("_id = " + uri.getLastPathSegment());
      break;

    case MESSAGES_CONVERSATIONS:
      String registered_mail = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(
          Configuration.CHAT_EMAIL_ID, "");
      c = db.rawQuery("SELECT main._id, main.message, main.time, CASE WHEN main." + COL_RECEIVER_EMAIL
          + "= ? THEN main." + COL_SENDER_EMAIL + " ELSE main." + COL_RECEIVER_EMAIL + " END AS " + COL_EMAIL
          + " FROM " + TABLE_MESSAGES + " main INNER JOIN (SELECT _id, " + COL_EMAIL + "," + "max(" + COL_TIME
          + ") FROM (select _id, CASE WHEN " + COL_RECEIVER_EMAIL + "= ? THEN " + COL_SENDER_EMAIL + " ELSE "
          + COL_RECEIVER_EMAIL + " END AS " + COL_EMAIL + "," + COL_MESSAGE + "," + COL_TIME + " FROM "
          + TABLE_MESSAGES + ") GROUP BY " + COL_EMAIL + ") temp ON main._id = temp._id ORDER BY main.time DESC",
          new String[] { registered_mail, registered_mail });
      break;
    case PROFILE_ALLROWS:
      qb.setTables(TABLE_PROFILE);
      break;

    case PROFILE_SINGLE_ROW:
      qb.setTables(TABLE_PROFILE);
      qb.appendWhere("_id = " + uri.getLastPathSegment());
      break;

    default:
      throw new IllegalArgumentException("Unsupported URI: " + uri);
    }

    if (c == null) {
      c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    c.setNotificationUri(getContext().getContentResolver(), uri);
    return c;
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    SQLiteDatabase db = dbHelper.getWritableDatabase();

    long id;
    switch (uriMatcher.match(uri)) {
    case MESSAGES_ALLROWS:
      id = db.insertOrThrow(TABLE_MESSAGES, null, values);
      if (values.get(COL_RECEIVER_EMAIL) == null) {
        db.execSQL("update profile set count = count+1 where email = ?", new Object[] { values.get(COL_SENDER_EMAIL) });
        getContext().getContentResolver().notifyChange(CONTENT_URI_PROFILE, null);
      }
      break;

    case PROFILE_ALLROWS:
      id = db.insertOrThrow(TABLE_PROFILE, null, values);
      break;

    default:
      throw new IllegalArgumentException("Unsupported URI: " + uri);
    }

    Uri insertUri = ContentUris.withAppendedId(uri, id);
    getContext().getContentResolver().notifyChange(insertUri, null);
    return insertUri;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    SQLiteDatabase db = dbHelper.getWritableDatabase();

    int count;
    switch (uriMatcher.match(uri)) {
    case MESSAGES_ALLROWS:
      count = db.update(TABLE_MESSAGES, values, selection, selectionArgs);
      break;

    case MESSAGES_SINGLE_ROW:
      count = db.update(TABLE_MESSAGES, values, "_id = ?", new String[] { uri.getLastPathSegment() });
      break;

    case PROFILE_ALLROWS:
      count = db.update(TABLE_PROFILE, values, selection, selectionArgs);
      break;

    case PROFILE_SINGLE_ROW:
      count = db.update(TABLE_PROFILE, values, "_id = ?", new String[] { uri.getLastPathSegment() });
      break;

    default:
      throw new IllegalArgumentException("Unsupported URI: " + uri);
    }

    getContext().getContentResolver().notifyChange(uri, null);
    return count;
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    SQLiteDatabase db = dbHelper.getWritableDatabase();

    int count;
    switch (uriMatcher.match(uri)) {
    case MESSAGES_ALLROWS:
      count = db.delete(TABLE_MESSAGES, selection, selectionArgs);
      break;

    case MESSAGES_SINGLE_ROW:
      count = db.delete(TABLE_MESSAGES, "_id = ?", new String[] { uri.getLastPathSegment() });
      break;

    case PROFILE_ALLROWS:
      count = db.delete(TABLE_PROFILE, selection, selectionArgs);

      break;

    case PROFILE_SINGLE_ROW:
      count = db.delete(TABLE_PROFILE, "_id = ?", new String[] { uri.getLastPathSegment() });
      break;

    default:
      throw new IllegalArgumentException("Unsupported URI: " + uri);
    }

    getContext().getContentResolver().notifyChange(uri, null);
    return count;
  }

  @Override
  public String getType(Uri uri) {
    return null;
  }

  private static class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "instachat.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL("create table messages (" + "_id integer primary key autoincrement, " + COL_TYPE + " integer, "
          + COL_MESSAGE + " text, " + COL_SENDER_EMAIL + " text, " + COL_RECEIVER_EMAIL + " text, " + COL_TIME
          + " datetime default current_timestamp);");

      db.execSQL("create table profile(" + "_id integer primary key autoincrement, " + COL_NAME + " text, " + COL_EMAIL
          + " text unique, " + COL_ACCOUNT + " text,, " + COL_SECURED + " integer default 0, " + COL_LAST_KEY
          + " text, " + COL_COUNT + " integer default 0);");

      // db.execSQL("CREATE VIEW conversations SELECT p."+);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
  }
}
