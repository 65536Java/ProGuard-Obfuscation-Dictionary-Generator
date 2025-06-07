package source;

import javax.swing.*;

public class AutoScrollTextArea extends JTextArea {
    int lc = 0;
    boolean ass = true;

    @Override
    public void append(String str) {
        lc++;
        super.append(str);
        if(this.ass && lc >= 240) {
            end();
            lc = 0;
        }
    }
    public void end(){
        this.setCaretPosition(this.getDocument().getLength());
    }
}
