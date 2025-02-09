package logSystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

//这个是登录页面
public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginForm() {
        setTitle("登录");
        setSize(450, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());//将框内内容展示出来

        // 创建界面组件
        JLabel usernameLabel = new JLabel("用户名:");//输出文本框
        JLabel passwordLabel = new JLabel("密码:");

        usernameField = new JTextField(15);//输入文本框
        passwordField = new JPasswordField(15);
        loginButton = new JButton("登录");//交互按钮

        // 添加组件
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);

        // 登录按钮的点击事件
        loginButton.addActionListener(e -> login());//监听器，用户摁下按钮时执行login方法

        // 窗口居中
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // 登录方法
    private void login() {
        //获取用户名和密码
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        // 校验用户名和密码
        if (validateUser(username, password)) {
            System.out.println("正在打开HuffmanGUI...");
            // 打开主界面
            System.out.println("登录成功！");
            // 登录成功
            JOptionPane.showMessageDialog(this, "登录成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();  // 关闭当前登录窗口
            new pwdchange().setVisible(true); //打开密码管理页面

        } else {
            // 登录失败
            JOptionPane.showMessageDialog(this, "用户名或密码错误！", "错误", JOptionPane.ERROR_MESSAGE);
            // 失败时不做其他操作，保持在当前登录界面
        }

    }

    // 校验用户信息
    private boolean validateUser(String username, String password) {
        boolean isValid = false;
        // 数据库连接
        String url = "jdbc:mysql://localhost:3306/study";
        String dbUsername = "root";  // 数据库用户名
        String dbPassword = "123456";  // 数据库密码

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String query = "SELECT * FROM user WHERE username = ? AND pwd = ?";//在数据库中查找对应的数据
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, password);

                // 执行查询
                ResultSet resultSet = statement.executeQuery();

                // 如果查到了匹配的用户
                if (resultSet.next()) {
                    isValid = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isValid;
    }
}
