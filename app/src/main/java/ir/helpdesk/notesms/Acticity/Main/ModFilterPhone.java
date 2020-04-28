package ir.helpdesk.notesms.Acticity.Main;

public class ModFilterPhone {
    private String titleName;
    private String id;


    public ModFilterPhone(String titleName,String id) {
        this.titleName = titleName;
        this.id = id;
    }

    public String getTitle() {
        return this.titleName;
    }
    public String getId() {
        return this.id;
    }

}
