package premium.manager.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import premium.manager.mapper.CategoryMapper;
import premium.model.vo.product.CategoryExcelVo;

import java.util.List;

public class ExcelListener<T> implements ReadListener<T> {
    /*
     * 注意：监听器不能被Spring管理
     * 解决方案：可以给Listener创建成员变量，然后在构造方法中传入Mapper
     * */

    private final CategoryMapper categoryMapper;

    // Cache -> forbid OOM
    private static final int BATCH_COUNT = 100;
    private List<T> cacheDataList = ListUtils.newArrayListWithCapacity(BATCH_COUNT);

    public ExcelListener(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    /**
     * 从第二含开始读取，把每行读取内容封装到 t 对象里
     */
    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        cacheDataList.add(t);
        if (cacheDataList.size() >= BATCH_COUNT) {
            saveData();
            cacheDataList = ListUtils.newArrayListWithCapacity(BATCH_COUNT);
        }
    }

    /**
     * 保存 cacheDataList 中残余数据（小于BATCH_COUNT）
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();
    }

    private void saveData() {
        categoryMapper.batchInsert((List<CategoryExcelVo>)cacheDataList);
    }
}
