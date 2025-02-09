package logSystem;

import huffmanCoding.HuffmanGUI;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class pwdchange extends JFrame {
    // 数据库连接信息（请根据实际情况修改）
    private static final String DB_URL = "jdbc:mysql://localhost:3306/study?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456";

    public pwdchange() {
        setTitle("用户管理系统");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 窗口居中

        // 创建一个面板，使用 GridLayout 布局，四个按钮均分一行
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        JButton viewUsersBtn = new JButton("查看所有用户");
        JButton addUserBtn = new JButton("增加用户");
        JButton changePwdBtn = new JButton("修改密码");
        JButton openHuffmanBtn = new JButton("打开 Huffman 系统");

        panel.add(viewUsersBtn);
        panel.add(addUserBtn);
        panel.add(changePwdBtn);
        panel.add(openHuffmanBtn);

        add(panel, BorderLayout.CENTER);

        // 绑定按钮事件
        viewUsersBtn.addActionListener(e -> viewAllUsers());
        addUserBtn.addActionListener(e -> addUser());
        changePwdBtn.addActionListener(e -> changePassword());
        openHuffmanBtn.addActionListener(e -> openHuffman());
    }

    // 功能1：查看所有用户
    private void viewAllUsers() {
        List<String> users = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT username FROM user")) {
            while (rs.next()) {
                users.add(rs.getString("username"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "查询用户时出错: " + ex.getMessage(),
                    "数据库错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder("所有用户：\n");
        for (String u : users) {
            sb.append(u).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "用户列表", JOptionPane.INFORMATION_MESSAGE);
    }

    // 功能2：增加新用户
    private void addUser() {
        String username = JOptionPane.showInputDialog(this, "请输入新用户名:");
        if (username == null || username.isEmpty()) {
            return;
        }
        String password = JOptionPane.showInputDialog(this, "请输入新用户密码:");
        if (password == null || password.isEmpty()) {
            return;
        }
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO user (username, pwd) VALUES (?, ?)")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "用户添加成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "用户添加失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "添加用户时出错: " + ex.getMessage(),
                    "数据库错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 功能3：修改密码
    private void changePassword() {
        String username = JOptionPane.showInputDialog(this, "请输入需要修改密码的账号:");
        if (username == null || username.isEmpty()) {
            return;
        }
        String oldPwd = JOptionPane.showInputDialog(this, "请输入原密码:");
        if (oldPwd == null || oldPwd.isEmpty()) {
            return;
        }
        String newPwd = JOptionPane.showInputDialog(this, "请输入新密码:");
        if (newPwd == null || newPwd.isEmpty()) {
            return;
        }
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE user SET password = ? WHERE username = ? AND pwd = ?")) {
            pstmt.setString(1, newPwd);
            pstmt.setString(2, username);
            pstmt.setString(3, oldPwd);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "密码修改成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "修改失败：账号或原密码错误！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "修改密码时出错: " + ex.getMessage(),
                    "数据库错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 功能4：打开 Huffman 系统
    private void openHuffman() {
        new HuffmanGUI().setVisible(true);
    }
}