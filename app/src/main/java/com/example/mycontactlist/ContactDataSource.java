package com.example.mycontactlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class ContactDataSource {
    private SQLiteDatabase database;
    private ContactDBHelper dbHelper;

    public ContactDataSource (Context context) {
        dbHelper = new ContactDBHelper(context);
    }
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    public void close(){
        dbHelper.close();
    }

    public boolean insertContact(Contact c) {
        boolean didSucced = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("contactname", c.getContactName());
            initialValues.put("streetaddress", c.getStreetAddress());
            initialValues.put("city", c.getCity());
            initialValues.put("state", c.getState());
            initialValues.put("phonenumber", c.getZipCode());
            initialValues.put("cellnumber", c.getCellNumber());
            initialValues.put("email", c.getEMail());
            initialValues.put("birthday", String.valueOf(c.getBirthday().getTimeInMillis()));

            didSucced = database.insert("contact", null, initialValues) > 0;
        }
        catch (Exception e) {
            //do nothing -will return false if there is an exception
        }
    return didSucced;
    }

    public boolean updateContact(Contact c) {
        boolean didSucced = false;
        try {
            long rowId = (long) c.getContactID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("contactname", c.getContactName());
            updateValues.put("streetaddress", c.getStreetAddress());
            updateValues.put("city", c.getCity());
            updateValues.put("state", c.getState());
            updateValues.put("phonenumber", c.getZipCode());
            updateValues.put("cellnumber", c.getCellNumber());
            updateValues.put("email", c.getEMail());
            updateValues.put("birthday", String.valueOf(c.getBirthday().getTimeInMillis()));

            didSucced = database.update("contact",  updateValues, "_id=" + rowId , null) > 0;
        }
        catch (Exception e) {
            //do nothing -will return false if there is an exception
        }
        return didSucced;
    }

    public int getLastContactID() {
        int lastID;
        try{
            String query = "Select MAX(_id) from contact";
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            lastID = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e) {
            lastID = -1;
        }
        return lastID;

    }
    public ArrayList<String> getContactName() {
        ArrayList<String> contactNames = new ArrayList<>();
        try {
            String query = "Select contactname from contact";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                contactNames.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            contactNames = new ArrayList<String>();
        }
        return contactNames;
    }
    public ArrayList<Contact> getContacts() {
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        try {
            String query = "SELECT * FROM contact";
            Cursor cursor = database.rawQuery(query, null);

            Contact newContact;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newContact = new Contact();
                newContact.setContactID(cursor.getInt(0));
                newContact.setContactName(cursor.getString(1));
                newContact.setStreetAddress(cursor.getString(2));
                newContact.setCity(cursor.getString(3));
                newContact.setState(cursor.getString(4));
                newContact.setZipCode(cursor.getString(5));
                newContact.setPhoneNumber(cursor.getString(6));
                newContact.setCellNumber(cursor.getString(7));
                newContact.setEMail(cursor.getString(8));
                Calendar calendar =  Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(9)));
                newContact.setBirthday(calendar);
                contacts.add(newContact);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            contacts = new ArrayList<Contact>();
        }
        return contacts;
    }

    public Contact getSpecificContact (int contactId) {
        Contact contact = new Contact();
        String query = "SELECT * FROM contact WHERE _id =" + contactId;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            contact.setContactID(cursor.getInt(0));
            contact.setContactName(cursor.getString(1));
            contact.setStreetAddress(cursor.getString(2));
            contact.setCity(cursor.getString(3));
            contact.setState(cursor.getString(4));
            contact.setZipCode(cursor.getString(5));
            contact.setPhoneNumber(cursor.getString(6));
            contact.setCellNumber(cursor.getString(7));
            contact.setEMail(cursor.getString(8));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(cursor.getString(9)));
            contact.setBirthday(calendar);

            cursor.close();
        }
        return contact;
    }
    public boolean deleteContact (int contanctId) {
        boolean didDelete = false;
        try{
            didDelete = database.delete("contanct", "_id=" + contanctId,null) >0;
        }
        catch (Exception e) {
            //Do nothing -return value is already set to false
        }
        return didDelete;
    }
}