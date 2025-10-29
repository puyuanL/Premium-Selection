package premium.manager.utils;

import premium.model.entity.system.SysMenu;

import java.util.*;

public class MenuHelper {

    /**
     * 封装树形菜单数据
     * @param sysMenuList List<sysMenu>
     * @return tree of list
     */
    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {

        Map<Long, List<SysMenu>> sysMenuMap = new HashMap<>();

        for (SysMenu sysMenu : sysMenuList) {
            sysMenuMap.computeIfAbsent(sysMenu.getParentId(), k -> new ArrayList<>()).add(sysMenu);
        }

        List<SysMenu> treeMenuList = sysMenuMap.getOrDefault(0L, new ArrayList<>());
        for (SysMenu sysMenu : treeMenuList) {
            setChildren(sysMenu, sysMenuMap);
        }

        return treeMenuList;
    }

    /**
     * 查找下层的菜单子节点
     * @param parent SysMenu
     * @param parentChildMap Map<Long, List<SysMenu>>
     */
    private static void setChildren(SysMenu parent, Map<Long, List<SysMenu>> parentChildMap) {
        List<SysMenu> children = parentChildMap.getOrDefault(parent.getId(), new ArrayList<>());
        parent.setChildren(children);

        for (SysMenu child : children) {
            setChildren(child, parentChildMap);
        }
    }

//    递归方法时间复杂度O(n^2)
//    /**
//     * 封装树形菜单数据
//     * @param sysMenuList List<sysMenu>
//     * @return tree of list
//     */
//    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {
//        List<SysMenu> treeMenuList = new ArrayList<>();
//        for (SysMenu sysMenu : sysMenuList) {
//            if (sysMenu.getParentId() == 0) {
//                treeMenuList.add(findChildren(sysMenu, sysMenuList));
//            }
//        }
//
//        return treeMenuList;
//    }
//
//    /**
//     * 递归查找下层的菜单
//     * @param sysMenu SysMenu
//     * @param sysMenuList List<SysMenu>
//     * @return updated sysMenu SysMenu
//     */
//    private static SysMenu findChildren(SysMenu sysMenu, List<SysMenu> sysMenuList) {
//
//        sysMenu.setChildren(new ArrayList<>());
//        for (SysMenu childMenu: sysMenuList) {
//            if (Objects.equals(childMenu.getParentId(), sysMenu.getId())) {
//                sysMenu.getChildren().add(findChildren(childMenu, sysMenuList));
//            }
//        }
//        return sysMenu;
//    }
}
