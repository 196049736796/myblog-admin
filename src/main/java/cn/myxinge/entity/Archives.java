package cn.myxinge.entity;

        import java.util.List;
        import java.util.Map;

/**
 * Created by chenxinghua on 2017/12/13.
 */
public class Archives {
    private String year;
    private List<Map<String,List<Blog>>> datas;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<Map<String, List<Blog>>> getDatas() {
        return datas;
    }

    public void setDatas(List<Map<String, List<Blog>>> datas) {
        this.datas = datas;
    }
}
