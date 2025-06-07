package source;

import source.exceptions.InvalidNumberException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;

public class Frame extends JFrame {
    Color bgColor = Color.GREEN;
    JButton generate = new JButton("Generate!");
    JTextField characters = new JTextField();
    JTextField lines = new JTextField();
    JTextField maxStringLength = new JTextField("10000");
    AutoScrollTextArea log = new AutoScrollTextArea();
    JPanel panel = new JPanel(new BorderLayout());
    JPanel text1 = new JPanel(new BorderLayout());
    JPanel text2 = new JPanel(new BorderLayout());
    JPanel text3 = new JPanel(new BorderLayout());
    JPanel consolePanel = new JPanel(new BorderLayout());
    JScrollPane logScrollPane = new JScrollPane(log);
    public Frame() {
        this.setAlwaysOnTop(false);
        this.setTitle("ProGuard obfuscation dictionary generator v0.0.1_01");
        this.setSize(700, 400);
        this.setLocation(700, 500);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        generate.addActionListener((e)->{
            JFileChooser out = new JFileChooser();
            out.showDialog(null,"Select");
            File selected = out.getSelectedFile();
            if(selected == null){
                return;
            }
            Thread pro = new Thread(()->{
                try{
                    try {
                        process(selected,characters.getText().toCharArray(), Long.parseLong(lines.getText()),Long.parseLong(maxStringLength.getText()),log,generate);
                    }catch (NumberFormatException NFE){
                        throw new InvalidNumberException();
                    }
                }catch (InvalidNumberException INE){
                    INE.printStackTrace();
                    JOptionPane.showMessageDialog(null,"Error:\n"+INE.getMessage());
                }
            });
            pro.start();
        });
        log.setEditable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        text1.add(new JLabel("Characters"),BorderLayout.WEST);
        text1.add(characters,BorderLayout.CENTER);
        text1.setBackground(bgColor);
        text2.add(new JLabel("Line count"),BorderLayout.WEST);
        text2.add(lines,BorderLayout.CENTER);
        text2.setBackground(bgColor);
        text3.add(new JLabel("Max string length"),BorderLayout.WEST);
        text3.add(maxStringLength,BorderLayout.CENTER);
        text3.setBackground(bgColor);
        panel.add(text1,BorderLayout.NORTH);
        panel.add(generate,BorderLayout.EAST);
        panel.add(text2,BorderLayout.CENTER);
        panel.add(text3,BorderLayout.SOUTH);
        consolePanel.add(new JLabel("Output log"),BorderLayout.NORTH);
        consolePanel.add(logScrollPane,BorderLayout.CENTER);
        consolePanel.setBackground(bgColor);
        log.setFont(new Font(Font.MONOSPACED,Font.BOLD,15));
        this.add(panel,BorderLayout.NORTH);
        this.add(consolePanel,BorderLayout.CENTER);
        this.setVisible(true);
    }
    public void process(File out, char[] characters, long line,long max,AutoScrollTextArea output,JButton action){
        this.setAlwaysOnTop(true);
        output.setText("");
        output.append("Process start!\n");
        action.setEnabled(false);
        try(FileWriter fw = new FileWriter(out)){
            if(line < 1L){
                throw new InvalidNumberException("Invalid line count.");
            }
            if(max < (characters.length * 10L)){
                throw new InvalidNumberException("Invalid max string length("+max+")");
            }
            if(characters.length < 2){
                throw new InvalidNumberException("Characters length shouldn't be empty or 1.");
            }
            for(long processedLines = 0;processedLines < line;processedLines++){
                for(long i = 0;i < new java.util.Random().nextLong(max);i++){
                    fw.write(characters[(int)Math.floor((Math.random()*characters.length))]);
                }
                fw.write("\n");
                output.append(String.format("Processed %d %% (%d / %d lines)\n",(int)((((double) processedLines)/((double) line))*100),processedLines,line));
            }
            output.append("Process completed successfully!\n");
        }catch (Throwable cs){
            cs.printStackTrace();
            output.append("Process failure. Error:\n");
            output.append(cs.getMessage());
        }finally {
            this.setAlwaysOnTop(false);
            generate.setEnabled(true);
            output.end();
        }
    }
}
