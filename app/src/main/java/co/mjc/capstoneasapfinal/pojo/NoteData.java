package co.mjc.capstoneasapfinal.pojo;

import java.io.Serializable;

public class NoteData implements Serializable {

    String noteName;
    String noteBitmapImage;

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getNoteBitmapImage() {
        return noteBitmapImage;
    }

    public void setNoteBitmapImage(String noteBitmapImage) {
        this.noteBitmapImage = noteBitmapImage;
    }
}
