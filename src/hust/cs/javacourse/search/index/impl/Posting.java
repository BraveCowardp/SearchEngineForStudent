package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractPosting;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Posting extends AbstractPosting {
    public Posting(){ }
    /**
     * 接受三个参数的构造函数
     * @param docId ： 文档ID
     * @param freq ： 出现频率
     * @param positions ： 单词出现位置
     */
    public Posting(int docId, int freq, List<Integer> positions){
        super(docId, freq, positions);
    }

    /**
     * 判断二个Posting内容是否相同
     *
     * @param obj ：要比较的另外一个Posting
     * @return 如果内容相等返回true，否则返回false
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Posting){
            Posting posting = (Posting) obj;
            return this.docId==posting.docId && this.freq==posting.freq && this.positions.containsAll(posting.positions)
                    && posting.positions.containsAll(this.positions);
        }
        return false;
    }

    /**
     * 返回Posting的字符串表示
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return "Posting:{docId:"+this.docId+", freq:"+this.freq+", positions:"+this.positions+"}";
    }

    /**
     * 返回包含单词的文档id
     *
     * @return ：文档id
     */
    @Override
    public int getDocId() {
        return this.docId;
    }

    /**
     * 设置包含单词的文档id
     *
     * @param docId ：包含单词的文档id
     */
    @Override
    public void setDocId(int docId) {
        this.docId=docId;
    }

    /**
     * 返回单词在文档里出现的次数
     *
     * @return ：出现次数
     */
    @Override
    public int getFreq() {
        return this.freq;
    }

    /**
     * 设置单词在文档里出现的次数
     *
     * @param freq :单词在文档里出现的次数
     */
    @Override
    public void setFreq(int freq) {
        this.freq=freq;
    }

    /**
     * 返回单词在文档里出现的位置列表
     *
     * @return ：位置列表
     */
    @Override
    public List<Integer> getPositions() {
        return this.positions;
    }

    /**
     * 设置单词在文档里出现的位置列表
     *
     * @param positions ：单词在文档里出现的位置列表
     */
    @Override
    public void setPositions(List<Integer> positions) {
        this.positions=positions;
    }

    /**
     * 比较二个Posting对象的大小（根据docId）
     *
     * @param o ： 另一个Posting对象
     * @return ：二个Posting对象的docId的差值
     */
    @Override
    public int compareTo(AbstractPosting o) {
        return this.docId-o.getDocId();
    }

    /**
     * 对内部positions从小到大排序
     */
    @Override
    public void sort() {
        this.positions.sort(Comparator.naturalOrder());
    }

    /**
     * 写到二进制文件
     *
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeInt(this.docId);
            out.writeInt(this.freq);
            out.writeObject(this.positions);   // ArrayList本身实现了Serializable接口
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从二进制文件读
     *
     * @param in ：输入流对象
     */
    @Override
    public void readObject(ObjectInputStream in) {
        try {
            this.docId = in.readInt();
            this.freq = in.readInt();
            this.positions = (ArrayList<Integer>)in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}