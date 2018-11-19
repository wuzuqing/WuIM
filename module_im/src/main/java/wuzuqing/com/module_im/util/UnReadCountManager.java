package wuzuqing.com.module_im.util;

import com.yinglan.alphatabs.AlphaTabsIndicator;

import org.jetbrains.annotations.Nullable;

public class UnReadCountManager {
    private static UnReadCountManager instance;
    private AlphaTabsIndicator mAlphaIndicator;

    private UnReadCountManager() {
    }

    public static UnReadCountManager getInstance() {
        if (instance == null) {
            instance = new UnReadCountManager();
        }
        return instance;
    }

    private int totalCount;

    public void addCount(int count, boolean refresh) {
        totalCount += count;
        if (refresh) refresh();
    }

    public void removeCount(int count) {
        if (count > 0) {
            totalCount -= count;
            refresh();
        }
    }

    public void refresh() {
        if (mAlphaIndicator != null) {
            mAlphaIndicator.getTabView(0).showNumber(totalCount);
        }
    }

    public void reset() {
        totalCount = 0;
    }

    public void registerUnReadCount(@Nullable AlphaTabsIndicator alphaIndicator) {
        mAlphaIndicator = alphaIndicator;
    }

}