package dto;

public class CreateBoardRequest {
    private String name;
    private String prefsBackground;
    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefsBackground() {
        return prefsBackground;
    }

    public void setPrefsBackground(String prefsBackground) {
        this.prefsBackground = prefsBackground;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
