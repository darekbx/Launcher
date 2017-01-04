package com.mlauncher.logic;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.mlauncher.R;
import com.mlauncher.view.FilterButton;

import java.util.HashMap;
import java.util.List;

/**
 * Created by daba on 2016-09-29.
 */
public class FilterController {

    private TableLayout filterTable;
    private Context context;
    private View.OnClickListener listener;

    public FilterController(Context context, TableLayout filterTable, View.OnClickListener listener) {
        this.context = context;
        this.filterTable = filterTable;
        this.listener = listener;
    }

    public void setFilterTable(List<ResolveInfo> apps) {
        HashMap<Character, Integer> characters = new HashMap<Character, Integer>(32);
        for (ResolveInfo info : apps) {
            char firstLetter = info.loadLabel(context.getPackageManager()).toString().toLowerCase().charAt(0);
            if (!characters.containsKey(firstLetter)) {
                characters.put(firstLetter, 1);
            } else {
                characters.put(firstLetter, characters.get(firstLetter) + 1);
            }
        }

        int activeText = context.getResources().getColor(R.color.font_default);
        int inactiveBackground = Color.argb(238, 58, 58, 58);
        int inactiveText = Color.argb(255, 118, 130, 138);

        int rowsCount = filterTable.getChildCount();
        for (int r = 0; r < rowsCount; r++) {
            TableRow row = (TableRow) filterTable.getChildAt(r);
            int buttonsCount = row.getChildCount();
            for (int c = 0; c < buttonsCount; c++) {
                FilterButton button = (FilterButton) row.getChildAt(c);
                char firstLetter = button.getText().toString().toLowerCase().charAt(0);

                if (characters.containsKey(firstLetter)) {
                    button.setCount(characters.get(firstLetter));
                    button.setOnClickListener(listener);
                    button.setBackgroundResource(R.drawable.btn_filter);
                    button.setTextColor(activeText);
                } else {
                    button.setBackgroundColor(inactiveBackground);
                    button.setTextColor(inactiveText);
                }
            }
        }
    }

    public void destroyButtons() {
        if (filterTable != null) {
            int rowsCount = filterTable.getChildCount();
            for (int r = 0; r < rowsCount; r++) {
                TableRow row = (TableRow) filterTable.getChildAt(r);
                if (row != null) {
                    int buttonsCount = row.getChildCount();
                    for (int c = 0; c < buttonsCount; c++) {
                        Button button = (Button) row.getChildAt(c);
                        if (button != null) {
                            button.setOnClickListener(null);
                        }
                    }
                }
            }
        }
    }
}
