package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class IndexSearcher extends AbstractIndexSearcher {
    /**
     * 构造函数
     */
    public IndexSearcher() {
    }
    /**
     * 从指定索引文件打开索引，加载到index对象里. 一定要先打开索引，才能执行search方法
     *
     * @param indexFile ：指定索引文件
     */
    @Override
    public void open(String indexFile) {
        try {
            this.index.load(new File(indexFile));
            this.index.optimize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据单个检索词进行搜索
     *
     * @param queryTerm ：检索词
     * @param sorter    ：排序器
     * @return ：命中结果数组
     */
    @Override
    public AbstractHit[] search(AbstractTerm queryTerm, Sort sorter) {
        AbstractPostingList postinglist = this.index.search(queryTerm);
        if (postinglist != null) {
            AbstractHit[] hits = new AbstractHit[postinglist.size()];
            for (int i = 0; i < postinglist.size(); i++) {
                Map<AbstractTerm, AbstractPosting> termPostingMapping = new HashMap<>();
                AbstractPosting posting = postinglist.get(i);
                termPostingMapping.put(queryTerm, posting);
                hits[i] = new Hit(posting.getDocId(), this.index.getDocName(posting.getDocId()), termPostingMapping);
                hits[i].setScore(sorter.score(hits[i]));
            }
            sorter.sort(Arrays.asList(hits));
            return hits;
        } else {
            return null;
        }
    }

    /**
     * 根据二个检索词进行搜索
     *
     * @param queryTerm1 ：第1个检索词
     * @param queryTerm2 ：第2个检索词
     * @param sorter     ：    排序器
     * @param combine    ：   多个检索词的逻辑组合方式
     * @return ：命中结果数组
     */
    @Override
    public AbstractHit[] search(AbstractTerm queryTerm1, AbstractTerm queryTerm2, Sort sorter, LogicalCombination combine) {
        AbstractPostingList postinglist1 = this.index.search(queryTerm1);
        AbstractPostingList postinglist2 = this.index.search(queryTerm2);
        Map<AbstractTerm, AbstractPosting> termPostingMapping = new HashMap<>();
        ArrayList<AbstractHit> hits = new ArrayList<>();
        switch (combine){
            case OR:
                if (postinglist1 != null) {
                    for (int i = 0; i < postinglist1.size(); i++) {
                        AbstractPosting posting = postinglist1.get(i);
                        termPostingMapping.put(queryTerm1, posting);
                        hits.add(new Hit(posting.getDocId(), this.index.getDocName(posting.getDocId()), termPostingMapping));
                        hits.get(i).setScore(sorter.score(hits.get(i)));
                        termPostingMapping.clear();
                    }
                } else if(postinglist2 != null){
                    for (int i = 0; i < postinglist2.size(); i++) {
                        boolean hasflag = false;
                        AbstractPosting posting = postinglist2.get(i);
                        for(AbstractHit hit:hits){
                            if(hit.getDocId()==posting.getDocId()){
                                hasflag = true;
                                hit.getTermPostingMapping().put(queryTerm2,posting);
                                hit.setScore(sorter.score(hit));
                            }
                        }
                        if(!hasflag){
                            termPostingMapping.put(queryTerm2,posting);
                            hits.add(new Hit(posting.getDocId(),this.index.getDocName(posting.getDocId()),termPostingMapping));
                            hits.get(-1).setScore(sorter.score(hits.get(-1)));
                            termPostingMapping.clear();
                        }
                    }
                } else {
                    return null;
                }
                break;
            case AND:
                if (postinglist1 != null && postinglist2 != null) {
                    for (int i = 0; i < postinglist1.size(); i++) {
                        AbstractPosting posting = postinglist1.get(i);
                        for (int j = 0; j < postinglist2.size(); j++) {
                            AbstractPosting posting1 = postinglist2.get(j);
                            if (posting.getDocId() == posting1.getDocId()) {
                                termPostingMapping.put(queryTerm1, posting);
                                termPostingMapping.put(queryTerm2, posting1);
                                hits.add(new Hit(posting.getDocId(), this.index.getDocName(posting.getDocId()), termPostingMapping));
                                hits.get(i).setScore(sorter.score(hits.get(i)));
                                termPostingMapping.clear();
                            }
                        }
                    }
                }
                else return null;
                break;
        }
        sorter.sort(hits);
        return (AbstractHit[]) hits.toArray(hits.toArray(new AbstractHit[hits.size()]));
    }
}
