package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;
import hust.cs.javacourse.search.query.impl.IndexSearcher;
import hust.cs.javacourse.search.query.impl.SimpleSorter;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.StopWords;

import javax.swing.plaf.nimbus.AbstractRegionPainter;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 测试搜索
 */
public class TestSearchIndex {
    /**
     *  搜索程序入口
     * @param args ：命令行参数
     */
    public static void main(String[] args) throws IOException {
        String indexFile = Config.INDEX_DIR + "index.dat"; //index文件
        String searchResultFile = Config.DOC_DIR + "searchResult.txt"; //搜索结果文件
        String searchWordFile = Config.DOC_DIR + "searchword.txt"; //搜索词文件
        Sort simpleSorter = new SimpleSorter();
        AbstractIndexSearcher searcher = new IndexSearcher();
        searcher.open(indexFile);//读取index文件
        FileWriter resultWriter = new FileWriter(new File(searchResultFile));
        BufferedReader searchWordReader = new BufferedReader(new InputStreamReader(new FileInputStream(searchWordFile)));
        String line = "";
        while(true){
            line = searchWordReader.readLine();
            if(line==null){
                break;
            }
            AbstractHit[] hits = searcher.search(new Term(line), simpleSorter);
            System.out.println("搜索词："+line);
            resultWriter.write("搜索词："+line);
            if(hits!=null){
                for(AbstractHit hit : hits){
                    System.out.println(hit.toString());
                    resultWriter.write(hit.toString());
                }
            }   else {
                System.out.println("查询不到");
                resultWriter.write("查询不到");
            }

            System.out.println(" ");
            System.out.println(" ");
        }
        searchWordReader.close();
    }
}
