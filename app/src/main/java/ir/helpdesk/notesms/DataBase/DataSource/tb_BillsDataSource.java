package ir.helpdesk.notesms.DataBase.DataSource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ir.helpdesk.notesms.Classes.CalendarTool;
import ir.helpdesk.notesms.DataBase.DatabaseManagement;
import ir.helpdesk.notesms.DataBase.Structure.tb_BillsStructure;
import ir.helpdesk.notesms.DataBase.Tables.tb_Bills;

public class tb_BillsDataSource {

    private SQLiteDatabase database;
    private DatabaseManagement dbManagement;

    private String[] allColumns = {

            tb_BillsStructure.PK_key,
            tb_BillsStructure.txtSMS,
            tb_BillsStructure.senderSMS,
            tb_BillsStructure.txtNote,
            tb_BillsStructure.dateSMSMiladi,
            tb_BillsStructure.dateSMSJalali,
            tb_BillsStructure.dateNoteMiladi,
            tb_BillsStructure.dateNoteJalali,
            tb_BillsStructure.temp

    };

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public tb_BillsDataSource(Context context) {
        dbManagement = new DatabaseManagement(context);
    }

    public void Open() throws SQLException {
        database = dbManagement.getWritableDatabase();
    }

    public void Close() {
        dbManagement.close();
        database.close();
    }

    public long Add(tb_Bills data) {

        Open();
        ContentValues values = new ContentValues();
        values.put(tb_BillsStructure.PK_key, data.PK_key);
        values.put(tb_BillsStructure.txtSMS, data.txtSMS);
        values.put(tb_BillsStructure.senderSMS, data.senderSMS);
        values.put(tb_BillsStructure.txtNote, data.txtNote);
        values.put(tb_BillsStructure.dateSMSMiladi, data.dateSMSMiladi);
        values.put(tb_BillsStructure.dateSMSJalali, data.dateSMSJalali);
        values.put(tb_BillsStructure.dateNoteMiladi, data.dateNoteMiladi);
        values.put(tb_BillsStructure.dateNoteJalali, data.dateNoteJalali);
        values.put(tb_BillsStructure.temp, data.temp);
        long res = database.insert(tb_BillsStructure.tableName, null, values);
        Close();

        return res;
    }

    public long EditItems(tb_Bills data) {
        Open();
        ContentValues values = new ContentValues();
        values.put(tb_BillsStructure.PK_key, data.PK_key);
        values.put(tb_BillsStructure.txtSMS, data.txtSMS);
        values.put(tb_BillsStructure.senderSMS, data.senderSMS);
        values.put(tb_BillsStructure.txtNote, data.txtNote);
        values.put(tb_BillsStructure.dateSMSMiladi, data.dateSMSMiladi);
        values.put(tb_BillsStructure.dateSMSJalali, data.dateSMSJalali);
        values.put(tb_BillsStructure.dateNoteMiladi, data.dateNoteMiladi);
        values.put(tb_BillsStructure.dateNoteJalali, data.dateNoteJalali);
        values.put(tb_BillsStructure.temp, data.temp);
        long res = database.update(tb_BillsStructure.tableName,
                values,
                tb_BillsStructure.PK_key + "= '" + data.PK_key + "'",
                null);
        Close();

        return res;
    }

    public long QueryNumEntries() {
        return DatabaseUtils.queryNumEntries(database, tb_BillsStructure.tableName);
    }

    public tb_Bills GetRecord() {
        Cursor cursor = database.query(tb_BillsStructure.tableName, allColumns,
                null, null, null, null, null);
        cursor.moveToFirst();

        if (cursor.getCount() == 0) {
            return null;
        }

        tb_Bills data = ConvertToRecord(cursor);
        cursor.close();
        return data;
    }

    public tb_Bills GetARecord(String id) {
        Open();
        Cursor cursor = database.query(tb_BillsStructure.tableName, allColumns,
                tb_BillsStructure.PK_key + "= '" + id + "'",
                null, null, null, null);

        cursor.moveToFirst();

        if (cursor.getCount() == 0) {
            return null;
        }

        tb_Bills data = ConvertToRecord(cursor);
        cursor.close();
        Close();
        return data;
    }

    public boolean isARecordExist(String id) {
        Open();
        Cursor cursor = database.query(tb_BillsStructure.tableName, allColumns,
                tb_BillsStructure.PK_key + "= '" + id + "'",
                null, null, null, null);

        cursor.moveToFirst();

        if (cursor.getCount() == 0) {
            Close();
            return true;
        } else {
            Close();
            return false;
        }
    }

    public void DeleteAll() {
        database.delete(tb_BillsStructure.tableName, null, null);
    }

    public void DeleteById(String id) {
        database.delete(tb_BillsStructure.tableName, tb_BillsStructure.PK_key + " = " + id, null);
    }

    public void DeleteLast() {
        database.delete(tb_BillsStructure.tableName, tb_BillsStructure.PK_key + " = \"\" ", null);
    }

    public List<tb_Bills> GetList() {
        Open();
        List<tb_Bills> lstData = new ArrayList<tb_Bills>();

        Cursor cursor = database.query(tb_BillsStructure.tableName,
                allColumns,
                null, null, null, null,
                tb_BillsStructure.dateSMSMiladi + " DESC");
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            tb_Bills tmpInfo = ConvertToRecord(cursor);
            lstData.add(tmpInfo);
            cursor.moveToNext();
        }

        cursor.close();
        Close();
        return lstData;
    }

    public List<tb_Bills> GetListByDate(String startDate, String endDate) {
        List<tb_Bills> lstData = new ArrayList<tb_Bills>();

        Cursor cursor = database.query(tb_BillsStructure.tableName,
                allColumns,
                tb_BillsStructure.dateSMSMiladi + " BETWEEN '" + startDate + "' AND '" + endDate + "'", null, null, null,
                tb_BillsStructure.dateSMSMiladi + " DESC");
        cursor.moveToFirst();


//        "select * from tb_Bills where dateMiladi between 'date' and 'date' AND Pk = pk AND name =  'سید' AND"
//        "Phone = '999'";

        while (!cursor.isAfterLast()) {
            tb_Bills tmpInfo = ConvertToRecord(cursor);
            lstData.add(tmpInfo);
            cursor.moveToNext();
        }

        cursor.close();
        return lstData;
    }

    public List<tb_Bills> Search(ArrayList array) {
        Open();
        List<tb_Bills> lstData = new ArrayList<tb_Bills>();

        String query = "";
        if (array.size() == 1) {
            query = array.get(0).toString();
        } else {
            for (int i = 0; i < array.size(); i++) {
                query += array.get(i).toString();
                if (i < array.size() - 1) {
                    query += " AND ";
                }
            }
        }

        Cursor cursor = database.query(tb_BillsStructure.tableName,
                allColumns,
                query,
                null, null, null,
                tb_BillsStructure.dateSMSMiladi + " DESC");
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            tb_Bills tmpInfo = ConvertToRecord(cursor);
            lstData.add(tmpInfo);
            cursor.moveToNext();
        }

        cursor.close();
        Close();
        return lstData;
    }


    public ArrayList<tb_Bills> SearchArrayList(ArrayList array) {
        Open();
        ArrayList<tb_Bills> lstData = new ArrayList<tb_Bills>();

        String query = "";
        if (array.size() == 1) {
            query = array.get(0).toString();
        } else {
            for (int i = 0; i < array.size(); i++) {
                query += array.get(i).toString();
                if (i < array.size() - 1) {
                    query += " AND ";
                }
            }
        }

        Cursor cursor = database.query(tb_BillsStructure.tableName,
                allColumns,
                query,
                null, null, null,
                tb_BillsStructure.dateSMSMiladi + " DESC");
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            tb_Bills tmpInfo = ConvertToRecord(cursor);
            lstData.add(tmpInfo);
            cursor.moveToNext();
        }

        cursor.close();
        Close();
        return lstData;
    }



    public void IraniToGery() {

        List<tb_Bills> list = GetList();

        for (int i = 0; i < list.size(); i++) {

            if (!list.get(i).temp.equals("")) {
                String[] date = list.get(i).temp.split("/");

                CalendarTool tool = new CalendarTool();
                tool.setIranianDate(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));

                ContentValues values = new ContentValues();
                values.put(tb_BillsStructure.dateSMSMiladi, tool.getGregorianDate());
                database.update(tb_BillsStructure.tableName,
                        values,
                        tb_BillsStructure.PK_key + "= '" + list.get(i).PK_key + "'",
                        null);
            }
        }

    }

    private tb_Bills ConvertToRecord(Cursor cursor) {
        tb_Bills data = new tb_Bills();

        data.PK_key = cursor.getString(0);
        data.txtSMS = cursor.getString(1);
        data.senderSMS = cursor.getString(2);
        data.txtNote = cursor.getString(3);
        data.dateSMSMiladi = cursor.getString(4);
        data.dateSMSJalali = cursor.getString(5);
        data.dateNoteMiladi = cursor.getString(6);
        data.dateNoteJalali = cursor.getString(7);
        data.temp = cursor.getString(8);

        return data;
    }

}