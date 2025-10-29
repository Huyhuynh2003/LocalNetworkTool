package zSERVER;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class SwingPredictor extends JFrame {
    private JCheckBox[] checkboxes;
    private JButton predictButton;
    private JTextArea resultArea;

    public SwingPredictor() {
        setTitle("Dự đoán lỗi máy tính (Java + Python)");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel chứa checkbox
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new GridLayout(0, 2)); // 2 cột

        
        String[] features = {
        		"Nhiệt độ CPU cao",
        	    "Kết nối mạng chập chờn",
        	    "Tiếng ồn từ quạt",
        	    "Tiếng kêu từ ổ cứng",
        	    "Thời gian khởi động lâu",
        	    "Màn hình xuất hiện lỗi hình ảnh",
        	    "Ứng dụng bị treo",
        	    "Máy tắt đột ngột",
        	    "Chuột hoặc bàn phím không phản hồi",
        	    "Âm thanh bị méo",
        	    "RAM sử dụng vượt 90%",
        	    "CPU sử dụng liên tục >90%",
        	    "Ổ cứng đầy >95%",
        	    "Pin laptop chai nhanh",
        	    "Adapter sạc nóng bất thường",
        	    "Mùi khét từ máy",
        	    "Đèn báo lỗi nhấp nháy",
        	    "Cổng USB không nhận thiết bị",
        	    "Wifi yếu dù gần router",
        	    "Quạt CPU quay quá nhanh"
        };

        checkboxes = new JCheckBox[features.length];
        for (int i = 0; i < features.length; i++) {
            checkboxes[i] = new JCheckBox(features[i]);
            checkboxPanel.add(checkboxes[i]);
        }

        add(new JScrollPane(checkboxPanel), BorderLayout.CENTER);

        // Nút dự đoán
        predictButton = new JButton("Dự đoán");
        add(predictButton, BorderLayout.SOUTH);

        // Kết quả
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        add(new JScrollPane(resultArea), BorderLayout.NORTH);

        // Xử lý nút bấm
        predictButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String inputData = buildInputData();
                String prediction = callPythonScript(inputData);
                resultArea.setText("Kết quả dự đoán: " + prediction);
            }
        });
    }

    private String buildInputData() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < checkboxes.length; i++) {
            sb.append(checkboxes[i].isSelected() ? "1" : "0");
            if (i < checkboxes.length - 1) sb.append(",");
        }
        return sb.toString();
    }

    private String callPythonScript(String inputData) {
        String result = "Không có kết quả";
        try {
            ProcessBuilder pb = new ProcessBuilder(
                "C:/Users/BRAVO 15/AppData/Local/Programs/Python/Python312/python.exe",
                "E:/VKU/Thuchanh/python/thuctaptest/predict_cli.py",
                inputData // chỉnh path Python script
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
            );
            String line;
            if ((line = reader.readLine()) != null) {
                result = line.trim();
            }

            process.waitFor();
        } catch (Exception ex) {
            result = "Lỗi khi chạy Python: " + ex.getMessage();
            ex.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SwingPredictor().setVisible(true);
        });
    }
}
