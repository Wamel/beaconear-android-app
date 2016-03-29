package com.wamel.beaconear.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wamel.beaconear.R;
import com.wamel.beaconear.callbacks.ApplicationSelectedCallback;
import com.wamel.beaconear.model.RegisteredApplication;

import java.util.List;

/**
 * Created by mreverter on 27/3/16.
 */
public class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ViewHolder> {

    private List<RegisteredApplication> mApplications;
    private ApplicationSelectedCallback mCallback;

    public ApplicationsAdapter(List<RegisteredApplication> applications, ApplicationSelectedCallback callback) {
        this.mApplications = applications;
        this.mCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_application, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RegisteredApplication application = mApplications.get(position);
        holder.mApplication = application;
        holder.mApplicationName.setText(application.getName());
        if(application.isActive()) {
            holder.mActiveImageView.setImageResource(R.drawable.ic_active);
        } else {
            holder.mActiveImageView.setImageResource(R.drawable.ic_inactive);
        }
        if(position == mApplications.size()-1) {
            holder.mSeparator.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mApplications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mApplicationName;
        private View mSeparator;
        private ImageView mActiveImageView;

        private RegisteredApplication mApplication;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onSelected(mApplication);
                }
            });

            mApplicationName = (TextView) itemView.findViewById(R.id.applicationName);
            mSeparator = itemView.findViewById(R.id.separator);
            mActiveImageView = (ImageView) itemView.findViewById(R.id.activeImage);
        }
    }
}
