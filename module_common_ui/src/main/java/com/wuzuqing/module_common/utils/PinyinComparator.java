package com.wuzuqing.module_common.utils;


import com.wuzuqing.module_common.bean.HoverItemBean;

import java.util.Comparator;

/**
 * 排序
 */
public class PinyinComparator implements Comparator<HoverItemBean> {

	@Override
	public int compare(HoverItemBean o1, HoverItemBean o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}
}
