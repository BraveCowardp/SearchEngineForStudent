package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;

public class TermTuple extends AbstractTermTuple {
    public TermTuple(){ }
    /**
     * 构造函数
     * @param term ：单词
     * @param curPos ： 单词的位置
     */
    public TermTuple(Term term, int curPos) {
        this.term = term;
        this.curPos = curPos;
    }
    /**
     * 判断二个三元组内容是否相同
     *
     * @param obj ：要比较的另外一个三元组
     * @return 如果内容相等（三个属性内容都相等）返回true，否则返回false
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TermTuple){
            TermTuple termtup = (TermTuple)obj;
            return this.curPos==termtup.curPos && this.term.equals(termtup.term);
        }
        return false;
    }

    /**
     * 获得三元组的字符串表示
     *
     * @return ： 三元组的字符串表示
     */
    @Override
    public String toString() {
        return "TermTuple:{term:"+this.term.toString()+", freq:"+this.freq+", curPos:"+this.curPos+"}";
    }
}
