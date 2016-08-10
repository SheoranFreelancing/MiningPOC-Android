package com.drill.adaptors;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.drill.android.R;
import com.drill.responsemodel.Merchant;

import java.util.ArrayList;

/**
 * Created by groupon on 6/17/15.
 */
public class MerchantsAdapter extends ArrayAdapter<Merchant> implements Filterable {

    Context context;
    private ArrayList<Merchant> mOriginalValues;
    private ArrayList<Merchant> mDisplayedValues;

    public MerchantsAdapter(Context context, int textViewResourceId, ArrayList<Merchant> merchants) {
        super(context, textViewResourceId, merchants);
        this.context = context;
        this.mOriginalValues = merchants;
        this.mDisplayedValues = merchants;
    }

    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public Merchant getItem(int position) {
        return mDisplayedValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.merchant_row, null);
            holder.merchantNameTextView = (TextView) convertView.findViewById(R.id.merchant_name);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Merchant merchant = mDisplayedValues.get(position);
        if (merchant != null) {
            holder.merchantNameTextView.setText(Html.fromHtml(merchant.getName()));
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mDisplayedValues = (ArrayList<Merchant>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<Merchant> filteredArrayList = new ArrayList<Merchant>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<Merchant>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns filteredArrayList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
//                        String data = mOriginalValues.get(i).getSalesRepName();
//                        if (data.toLowerCase().startsWith(constraint.toString())) {
                        if (mOriginalValues.get(i).toString().toLowerCase().contains(constraint)) {
                            Merchant merchant = new Merchant();
                            merchant.set_id(mOriginalValues.get(i).get_id());
                            merchant.setName(mOriginalValues.get(i).getName());
                            filteredArrayList.add(merchant);
                        }
                    }
                    // set the Filtered result to return
                    results.count = filteredArrayList.size();
                    results.values = filteredArrayList;
                }
                return results;
            }
        };
        return filter;
    }

    private class ViewHolder {
        TextView merchantNameTextView;
    }
}
