package huffmanCoding;
//哈夫曼树节点类
class HuffmanNode {
    char c;                // 字符
    int freq;              // 字符的频率（权重）
    HuffmanNode left, right; // 左右子树

    // 构造函数
    HuffmanNode(char c, int freq) {
        this.c = c;
        this.freq = freq;
        this.left = this.right = null;
    }
}