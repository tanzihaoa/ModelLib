package com.tzh.mylibrary.adapter;

import com.contrarywind.adapter.WheelAdapter;
import com.tzh.mylibrary.dto.LanguageDto;

import java.util.List;

public class MyWheelAdapter  implements WheelAdapter {

    // items
    private List<LanguageDto> items;

    /**
     * Constructor
     * @param items the items
     */
    public MyWheelAdapter(List<LanguageDto> items) {
        this.items = items;

    }

    @Override
    public Object getItem(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index).getText();
        }
        return "";
    }

    @Override
    public int getItemsCount() {
        return items.size();
    }

    @Override
    public int indexOf(Object o){
        return items.indexOf(o);
    }
}
