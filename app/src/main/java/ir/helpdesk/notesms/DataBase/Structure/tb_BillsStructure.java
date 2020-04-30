package ir.helpdesk.notesms.DataBase.Structure;

public class tb_BillsStructure {

    public static final String tableName = "tb_note_sms";

    public static final String PK_key = "PK_key";
    public static final String txtSMS = "txtSMS";
    public static final String senderSMS = "senderSMS";
    public static final String txtNote = "txtNote";
    public static final String dateSMSMiladi = "dateSMSMiladi";
    public static final String dateSMSJalali = "dateSMSJalali";
    public static final String dateNoteMiladi = "dateNoteMiladi";
    public static final String dateNoteJalali = "dateNoteJalali";
    public static final String temp = "temp";

    public static String createTableQuery = "create table " + tableName + "(" +
            PK_key + " text primary key , " +
            txtSMS + " text, " +
            senderSMS + " text, " +
            txtNote + " text, " +
            dateSMSMiladi + " current_date, " +
            dateSMSJalali + " text, " +
            dateNoteMiladi + " current_date, " +
            dateNoteJalali + " text, " +
            temp + " text" +
            ")";
}