package com.mlauncher.logic.adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.mlauncher.R;
import com.mlauncher.model.SortableResolveInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by INFOR PL on 2015-01-26.
 */
public class AppsAdapter extends BaseAdapter implements Filterable {

    private static final HashMap<String, Drawable> sIcons = new HashMap<>();

    private class AppsFilter extends Filter {

        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (prefix == null || prefix.length() == 0) {
                ArrayList<SortableResolveInfo> list;
                synchronized (lock) {
                    list = new ArrayList<>(apps);
                }

                results.values = list;
                results.count = list.size();
            }
            else {

                String prefixString = toLower(prefix.toString());
                ArrayList<SortableResolveInfo> values;

                synchronized (lock) {
                    values = new ArrayList<>(apps);
                }

                int count = values.size();
                ArrayList<SortableResolveInfo> newValues = new ArrayList<SortableResolveInfo>();

                for (int i = 0; i < count; i++) {
                    SortableResolveInfo value = values.get(i);
                    if (filterObject(value, prefixString))
                        newValues.add(value);
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filteredApps = (List<SortableResolveInfo>)filterResults.values;
            notifyDataSetChanged();
        }

        private boolean filterObject(SortableResolveInfo app, String constraint) {
            String label = toLower(app.label);
            if (label.startsWith(constraint))
                return true;
            return false;
        }

        private String toLower(String value) {
            return value.toLowerCase(Locale.ENGLISH);
        }
    }

    private AppsFilter filter;
    private LayoutInflater inflater;
    private Context context;
    private List<SortableResolveInfo> apps;
    private List<SortableResolveInfo> filteredApps;

    public AppsAdapter(Context context, List<SortableResolveInfo> apps) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.apps = new ArrayList<>(apps);
        this.filteredApps = apps;
    }

    public void refresh(List<SortableResolveInfo> apps) {
        this.apps.clear();
        filteredApps.clear();
        this.apps.addAll(apps);
        this.filteredApps.addAll(apps);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHendler handler;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row_apps, null);
            handler = new ViewHendler();
            handler.icon = (ImageView)convertView.findViewById(R.id.apps_icon);
            convertView.setTag(handler);
        }
        else {
            handler = (ViewHendler)convertView.getTag();
        }

        SortableResolveInfo info = filteredApps.get(position);
        handler.icon.setImageDrawable(loadIcon(info.packageName));
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new AppsFilter();
        return filter;
    }

    private Drawable getIconFromPackage(String packageName) {
        try {
            return context.getPackageManager().getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    private Drawable loadIcon(String packageName) {
        if (sIcons.containsKey(packageName)) {
            return sIcons.get(packageName);
        } else {
            Drawable icon = getIconFromPackage(packageName);
            sIcons.put(packageName, icon);
            return icon;
        }
    }

    class ViewHendler {
        ImageView icon;
    }

    public final int getCount() {
        return filteredApps.size();
    }

    public final Object getItem(int position) {
        return filteredApps.get(position);
    }

    public final long getItemId(int position) {
        return position;
    }
}