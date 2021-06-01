package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.index.impl.TermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleScanner;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.StringSplitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TermTupleScanner extends AbstractTermTupleScanner {
    private List<TermTuple> termTupleList;     //单词列表
    public TermTupleScanner() { }
    /**
     * 构造函数
     * @param input : 输入流对象
     */
    public TermTupleScanner(BufferedReader input) {
        super(input);
        int pos = 0;
        termTupleList = new ArrayList<TermTuple>();
        try {
            String str = input.readLine();
            while (str != null) {
                StringSplitter splitter = new StringSplitter();
                splitter.setSplitRegex(Config.STRING_SPLITTER_REGEX);
                List<String> terms = splitter.splitByRegex(str);
                int i;
                for (i = 0; i < terms.size(); i++) {
                    if (!terms.get(i).equals("")){
                        this.termTupleList.add(new TermTuple(new Term(terms.get(i).toLowerCase()), pos++));
                    }
                }

                str = input.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * 获得下一个三元组
     *
     * @return: 下一个三元组；如果到了流的末尾，返回null
     */
    @Override
    public AbstractTermTuple next() {
        if(termTupleList.size()==0) return null;
        TermTuple temp = termTupleList.get(0);
        termTupleList.remove(0);
        return temp;
    }
}
