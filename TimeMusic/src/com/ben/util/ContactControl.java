package com.ben.util;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

public class ContactControl {
	private static final String TAG = "ContactTest";
	private Context context = null;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public ContactControl(Context context) {
		super();
		this.context = context;
	}

	/**
	 * ��ȡͨѶ¼����ϵ��
	 */
	public void GetContact() {
		ContentResolver contentResolver = this.getContext()
				.getContentResolver();
		Uri uri = Uri.parse("content://com.android.contacts/contacts");
		Cursor cursor = contentResolver.query(uri, null, null, null, null);
		while (cursor.moveToNext()) {
			// ��ȡ��ϵ������
			StringBuilder sb = new StringBuilder();
			String contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			String name = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			sb.append("contactId=").append(contactId).append(",name=")
					.append(name);

			// ��ȡ��ϵ���ֻ�����
			Cursor phones = contentResolver.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
							+ contactId, null, null);
			while (phones.moveToNext()) {
				String phone = phones.getString(phones.getColumnIndex("data1"));
				sb.append(",phone=").append(phone);
			}

			// ��ȡ��ϵ��email
			Cursor emails = contentResolver.query(
					ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = "
							+ contactId, null, null);
			while (emails.moveToNext()) {
				String email = emails.getString(emails.getColumnIndex("data1"));
				sb.append(",email=").append(email);
			}
			Log.i(TAG, sb.toString());
		}
	}

	/**
	 * ������RawContacts.CONTENT_URIִ��һ����ֵ���룬Ŀ���ǻ�ȡϵͳ���ص�rawContactId
	 * 
	 * ���Ǻ������data������ݣ�ֻ��ִ�п�ֵ���룬����ʹ�������ϵ����ͨѶ¼��ɼ�
	 */
	public void testInsert() {
		ContentValues values = new ContentValues();
		// ������RawContacts.CONTENT_URIִ��һ����ֵ���룬Ŀ���ǻ�ȡϵͳ���ص�rawContactId
		Uri rawContactUri = this.getContext().getContentResolver()
				.insert(RawContacts.CONTENT_URI, values);
		long rawContactId = ContentUris.parseId(rawContactUri);

		// ��data������������
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		values.put(StructuredName.GIVEN_NAME, "zhangsan");
		this.getContext()
				.getContentResolver()
				.insert(android.provider.ContactsContract.Data.CONTENT_URI,
						values);

		// ��data����绰����
		values.clear();
		values.put(
				android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID,
				rawContactId);
		values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		values.put(Phone.NUMBER, "5554");
		values.put(Phone.TYPE, Phone.TYPE_MOBILE);
		this.getContext()
				.getContentResolver()
				.insert(android.provider.ContactsContract.Data.CONTENT_URI,
						values);

		// ��data����Email����
		values.clear();
		values.put(
				android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID,
				rawContactId);
		values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
		values.put(Email.DATA, "ljq218@126.com");
		values.put(Email.TYPE, Email.TYPE_WORK);
		this.getContext()
				.getContentResolver()
				.insert(android.provider.ContactsContract.Data.CONTENT_URI,
						values);
	}

	/**
	 * ���������ϵ�ˣ�����ͬһ��������
	 */
	public void testSave() throws Throwable {
		// �ĵ�λ�ã�reference\android\provider\ContactsContract.RawContacts.html
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		int rawContactInsertIndex = 0;
		ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
				.withValue(RawContacts.ACCOUNT_TYPE, null)
				.withValue(RawContacts.ACCOUNT_NAME, null).build());

		// �ĵ�λ�ã�reference\android\provider\ContactsContract.Data.html
		ops.add(ContentProviderOperation
				.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID,
						rawContactInsertIndex)
				.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
				.withValue(StructuredName.GIVEN_NAME, "lisi").build());
		ops.add(ContentProviderOperation
				.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID,
						rawContactInsertIndex)
				.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
				.withValue(Phone.NUMBER, "5556")
				.withValue(Phone.TYPE, Phone.TYPE_MOBILE)
				.withValue(Phone.LABEL, "").build());
		ops.add(ContentProviderOperation
				.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID,
						rawContactInsertIndex)
				.withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
				.withValue(Email.DATA, "lisi@126.cn")
				.withValue(Email.TYPE, Email.TYPE_WORK).build());

		ContentProviderResult[] results = this.getContext()
				.getContentResolver()
				.applyBatch(ContactsContract.AUTHORITY, ops);
		for (ContentProviderResult result : results) {
			Log.i(TAG, result.uri.toString());
		}
	}

}