package huffmanCoding;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
        import java.awt.*;
        import java.awt.event.*;
        import java.nio.file.*;
        import java.io.*;
        import java.util.*;
//这个类为哈夫曼编码译码的实现类
public class HuffmanGUI extends JFrame {
    private JRadioButton manualRadio, fileRadio;
    private JTextField inputField;
    private JTextArea resultArea;
    private JButton loadBtn, encodeBtn, decodeBtn, mapBtn, saveBtn;
    private String text = "";
    private Map<Character, String> huffmanCodes;
    private String encodedText;
    private Map<String, Character> reverseCodes;

    public HuffmanGUI() {
        initUI();
        setTitle("哈夫曼编码工具");//主页面框
        setSize(600, 400);//框的大小
        setDefaultCloseOperation(EXIT_ON_CLOSE);//关闭窗口时关闭程序
        setLocationRelativeTo(null);//居中显示
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));//创建边距

        // 输入面板
        JPanel inputPanel = new JPanel(new GridLayout(3, 1, 5, 5));//网格布局
        ButtonGroup bg = new ButtonGroup();
        //单选框
        manualRadio = new JRadioButton("手动输入", true);
        fileRadio = new JRadioButton("文件输入");
        //加入组件
        bg.add(manualRadio);
        bg.add(fileRadio);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radioPanel.add(manualRadio);
        radioPanel.add(fileRadio);

        //按钮
        inputField = new JTextField();
        JButton fileChooseBtn = new JButton("选择文件");
        loadBtn = new JButton("加载文本");

        JPanel filePanel = new JPanel(new BorderLayout(5, 5));
        filePanel.add(fileChooseBtn, BorderLayout.WEST);//fileChooseBtn放左侧
        filePanel.add(loadBtn, BorderLayout.CENTER);//loadBtn放中间

        //添加组件
        inputPanel.add(radioPanel);
        inputPanel.add(inputField);
        inputPanel.add(filePanel);

        // 功能按钮面板
        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        encodeBtn = new JButton("查看编码");
        decodeBtn = new JButton("查看译码");
        mapBtn = new JButton("对应关系");
        saveBtn = new JButton("保存结果");
        btnPanel.add(encodeBtn);
        btnPanel.add(decodeBtn);
        btnPanel.add(mapBtn);
        btnPanel.add(saveBtn);

        // 结果区域
        resultArea = new JTextArea();
        resultArea.setEditable(false);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        // 事件监听
        fileChooseBtn.addActionListener(e -> chooseFile());
        loadBtn.addActionListener(e -> loadText());
        encodeBtn.addActionListener(e -> showEncoded());
        decodeBtn.addActionListener(e -> showDecoded());
        mapBtn.addActionListener(e -> showMapping());
        saveBtn.addActionListener(e -> saveResults());

        toggleInputMode();
        manualRadio.addItemListener(e -> toggleInputMode());
        fileRadio.addItemListener(e -> toggleInputMode());

        add(mainPanel);
    }

    private void toggleInputMode() {
        inputField.setEnabled(manualRadio.isSelected());
        loadBtn.setEnabled(fileRadio.isSelected());
    }

    private void chooseFile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            inputField.setText(fc.getSelectedFile().getAbsolutePath());
        }
    }

    //读取文件类
    private void loadText() {
        if (fileRadio.isSelected()) {
            try {
                text = new String(Files.readAllBytes(Paths.get(inputField.getText())));
                initializeHuffman();
                JOptionPane.showMessageDialog(this, "文件加载成功！");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "文件读取失败：" + ex.getMessage());
            }
        }
    }

    //构建字符及其编码的哈希表
    private void initializeHuffman() {
        Map<Character, Integer> freq = HuffmanCoding.calculateFrequency(text);//计算字符频率
        HuffmanNode root = HuffmanCoding.buildHuffmanTree(freq);
        huffmanCodes = new HashMap<>();
        HuffmanCoding.generateHuffmanCodes(root, "", huffmanCodes);
        encodedText = HuffmanCoding.encode(text, huffmanCodes);
        reverseCodes = new HashMap<>();
        for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
            reverseCodes.put(entry.getValue(), entry.getKey());
        }
    }

    private void showEncoded() {
        if (manualRadio.isSelected()) {
            text = inputField.getText();
            initializeHuffman();
        }
        resultArea.setText("编码结果：\n" + encodedText);
    }

    private void showDecoded() {
        String decoded = HuffmanCoding.decode(encodedText, reverseCodes);
        resultArea.setText("译码结果：\n" + decoded);
    }

    private void showMapping() {
        StringBuilder sb = new StringBuilder("字符对应关系：\n");
        for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        resultArea.setText(sb.toString());
    }

    //保存文件类
    private void saveResults() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter pw = new PrintWriter(fc.getSelectedFile())) {
                pw.println("编码结果：");
                pw.println(encodedText);
                pw.println("\n译码结果：");
                pw.println(HuffmanCoding.decode(encodedText, reverseCodes));
                pw.println("\n对应关系：");
                for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
                    pw.println(entry.getKey() + ": " + entry.getValue());
                }
                JOptionPane.showMessageDialog(this, "保存成功！");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "保存失败：" + ex.getMessage());
            }
        }
    }
}
