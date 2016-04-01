package com.wamel.beaconear.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wamel.beaconear.R;
import com.wamel.beaconear.callbacks.LongSelectionCallback;
import com.wamel.beaconear.callbacks.SelectionCallback;
import com.wamel.beaconear.model.Type;

import java.util.List;

/**
 * Created by mreverter on 28/3/16.
 */
public class TypesAdapter extends RecyclerView.Adapter<TypesAdapter.ViewHolder> {

    private final List<Type> mTypes;
    private final SelectionCallback<Type> mCallback;
    private final LongSelectionCallback<Type> mLongSelectionCallback;

    public TypesAdapter(List<Type> types, SelectionCallback<Type> callback, LongSelectionCallback<Type> longSelectionCallback) {
        mTypes = types;
        mCallback = callback;
        mLongSelectionCallback = longSelectionCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_type, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Type type = mTypes.get(position);

        holder.mType = type;
        holder.mTypeName.setText(type.getName());
        if(position == mTypes.size()-1) {
            holder.mSeparator.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mTypes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTypeName;
        private View mSeparator;

        private Type mType;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onSelected(mType);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongSelectionCallback.onSelected(mType, v);
                    return true;
                }
            });
            mTypeName = (TextView) itemView.findViewById(R.id.typeName);
            mSeparator = itemView.findViewById(R.id.separator);
        }
    }
}
