package huffmanCoding;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

//
public class HuffmanCoding {

    // 使用一个优先队列来构建哈夫曼树
    public static HuffmanNode buildHuffmanTree(Map<Character, Integer> frequencyMap) {//哈希表名为frequencyMap
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>(Comparator.comparingInt(node -> node.freq));//将哈夫曼节点对象放入优先队列中，并从小到大排序

        // 将每个字符和它的频率封装成 HuffmanNode，并加入优先队列，升序排序，小的在前
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            pq.offer(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        // 构建哈夫曼树
        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();//取出并移除元素
            HuffmanNode right = pq.poll();
            HuffmanNode parent = new HuffmanNode('\0', left.freq + right.freq); // 创建一个新的父节点
            parent.left = left;
            parent.right = right;
            pq.offer(parent); // 将新节点加入队列
        }

        return pq.poll(); // 返回根节点
    }

    // 从哈夫曼树中生成编码
    public static void generateHuffmanCodes(HuffmanNode root, String code, Map<Character, String> huffmanCodes) {
        if (root == null) {
            return;
        }

        // 如果是叶子节点，则记录字符的编码
        if (root.left == null && root.right == null) {
            huffmanCodes.put(root.c, code);
        }

        // 向左子树添加 '0'
        generateHuffmanCodes(root.left, code + '0', huffmanCodes);
        // 向右子树添加 '1'
        generateHuffmanCodes(root.right, code + '1', huffmanCodes);
    }

    // 计算每个字符的频率
    public static Map<Character, Integer> calculateFrequency(String text) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        return frequencyMap;
    }

    // 编码字符串
    public static String encode(String text, Map<Character, String> huffmanCodes) {
        StringBuilder encodedString = new StringBuilder();
        for (char c : text.toCharArray()) {
            encodedString.append(huffmanCodes.get(c)); // 将哈希表中的哈夫曼编码添加到 StringBuilder中
        }
        return encodedString.toString();
    }

    //译码结果
    public static String decode(String encodedText, Map<String, Character> reverseHuffmanCodes) {
        StringBuilder decodedString = new StringBuilder();
        StringBuilder currentCode = new StringBuilder();

        for (int i = 0; i < encodedText.length(); i++) {
            currentCode.append(encodedText.charAt(i));//将encodedText（要求译码的字符串）添加到currentCode中

            if (reverseHuffmanCodes.containsKey(currentCode.toString())) { // 如果当前编码在反向哈夫曼映射表中找到字符
                decodedString.append(reverseHuffmanCodes.get(currentCode.toString()));//就将这个编码的哈希表对应的字符加到decodedString中
                currentCode.setLength(0); // 清空当前编码
            }
        }

        return decodedString.toString();
    }
}
