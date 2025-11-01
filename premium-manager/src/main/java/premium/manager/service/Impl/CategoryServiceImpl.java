package premium.manager.service.Impl;

import com.alibaba.excel.EasyExcel;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import premium.common.exception.MyException;
import premium.manager.listener.ExcelListener;
import premium.manager.mapper.CategoryMapper;
import premium.manager.service.CategoryService;
import premium.model.entity.product.Category;
import premium.model.vo.common.ResultCodeEnum;
import premium.model.vo.product.CategoryExcelVo;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> findcategoryList(int id) {
        // 根据 id 查询分类
        List<Category> categoryList = categoryMapper.selectCategoryByParentId(id);
        // 遍历返回的list集合。有下层 hasChildren = true
        if (categoryList != null) {
            categoryList.forEach(category -> {
                // 判断每个分类是否有下层分类, 并设置 setChildren
                category.setHasChildren(categoryMapper.selectCountByParentId(category.getId()) > 0);
            });
        }

        return categoryList;
    }

    @Override
    public void exportData(HttpServletResponse response) {
        try {
            // 设置相应的头信息
                // 1 设置响应结果类型
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
                // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("分类数据", "UTF-8");
                // 2 设置响应头信息
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            // 调用 Mapper 查询所有分类，返回 list 集合
            List<Category> categoryList = categoryMapper.findAll();
            List<CategoryExcelVo> categoryExcelVoList = new ArrayList<>();
            for (Category category : categoryList) {
                CategoryExcelVo categoryExcelVo = new CategoryExcelVo();
                BeanUtils.copyProperties(category, categoryExcelVo);
                categoryExcelVoList.add(categoryExcelVo);
            }
            // 调用EasyExcel的write方法, 完成写操作
            EasyExcel.write(response.getOutputStream(), CategoryExcelVo.class)
                    .sheet("分类数据").doWrite(categoryExcelVoList);

        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultCodeEnum.DATA_ERROR);
        }
    }

    @Override
    public void importData(MultipartFile file) {
        ExcelListener<CategoryExcelVo> excelListener = new ExcelListener<>(categoryMapper);
        try {
            EasyExcel.read(file.getInputStream(), CategoryExcelVo.class, excelListener)
                    .sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
            throw new MyException(ResultCodeEnum.DATA_ERROR);
        }
    }
}
