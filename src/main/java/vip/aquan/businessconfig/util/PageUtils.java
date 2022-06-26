package vip.aquan.businessconfig.util;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: page utils
 * @author: bob
 * @create: 2021/02/08 15:19
 */
@Data
public class PageUtils {

    /**
     * @Description get collection page
     * @Param [list, pageIndex, pageSize]
     * @Return void
     */
    public static <T> Map<String, Object> getCollectionPage(List<T> list, Pageable pageable) {
        if (list == null || list.isEmpty()) {
            return PageUtils.getResultPage(0, null);
        }

        int offset = (int) pageable.getOffset();
        int pageSize = pageable.getPageSize();

        List<T> listView = new ArrayList<>();

        if (list != null && !list.isEmpty()) {
            if (list.size() < offset) {
                return PageUtils.getResultPage(0, null);
            }
            for (; list.size() > offset && pageSize != 0; offset++, pageSize--) {
                listView.add(list.get(offset));
            }
            return PageUtils.getResultPage(list.size(), listView);
        }
        return PageUtils.getResultPage(0, null);
    }


    /**
     * @Description get pageable by page index , page size and sort
     * @Param [pageIndex, pageSize]
     * @Return org.springframework.data.domain.Pageable
     */
    public static Pageable getPageable(Integer pageIndex, Integer pageSize, Sort sort) {
        if (pageIndex == null || pageIndex == 0) {
            pageIndex = 0;
        } else {
            pageIndex--;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, sort);
        return pageRequest;
    }

    /**
     * @Description get page object
     * @Param [totalCount, list]
     * @Return java.util.Map<java.lang.String, java.lang.Object>
     */
    public static Map<String, Object> getResultPage(Page<?> page, List<?> list) {
        Map<String, Object> resultPageMap = new HashMap<>();
        resultPageMap.put("totalCount", page.getTotalElements());
        resultPageMap.put("list", list);
        return resultPageMap;
    }

    public static Map<String, Object> getResultPage(Integer count, List<?> list) {
        Map<String, Object> resultPageMap = new HashMap<>();
        resultPageMap.put("totalCount", count);
        resultPageMap.put("list", list);
        return resultPageMap;
    }

    public static int getTotalPages(int totalCount, int pageSize) {
        return (totalCount + pageSize - 1) / pageSize;
    }

    /**
     * @Description get pageable by page index (start:0), page size and sort
     * @Param [pageIndex, pageSize]
     * @Return org.springframework.data.domain.Pageable
     */
    public static Pageable getPageableByIndex(Integer pageIndex, Integer pageSize, Sort sort) {
        if(pageSize>400){
            throw new RuntimeException("页面容量不能大于400!");
        }
        if (pageIndex == null) {
            pageIndex = 0;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, sort);
        return pageRequest;
    }
}