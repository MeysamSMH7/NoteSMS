package ir.helpdesk.notesms.DataBase.Tables;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public class tb_Bills implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public tb_Bills createFromParcel(Parcel in) {
            return new tb_Bills();
        }

        public tb_Bills[] newArray(int size) {
            return new tb_Bills[size];
        }
    };

    public String PK_key;
    public String txtSMS;
    public String senderSMS;
    public String txtNote;
    public String dateSMSMiladi;
    public String dateSMSJalali;
    public String dateNoteMiladi;
    public String dateNoteJalali;
    public String temp;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.PK_key);
        dest.writeString(this.txtSMS);
        dest.writeString(this.senderSMS);
        dest.writeString(this.txtNote);
        dest.writeString(this.dateSMSMiladi);
        dest.writeString(this.dateSMSJalali);
        dest.writeString(this.dateNoteMiladi);
        dest.writeString(this.dateNoteJalali);
        dest.writeString(this.temp);
    }

    @Override
    public String toString() {

        /*
        "Student{" +
                ", titleNum='" + titleNum + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                '}';

         */


        return null;
    }


}