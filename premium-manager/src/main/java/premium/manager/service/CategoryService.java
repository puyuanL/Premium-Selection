package premium.manager.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import premium.model.entity.product.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findcategoryList(int id);

    void exportData(HttpServletResponse response);

    void importData(MultipartFile file);
}
