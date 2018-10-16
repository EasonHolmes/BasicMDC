package com.cui.mdc.uiExample.adapter;


import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author markzhai on 16/6/26
 */
public class BindingViewHolder<T extends ViewDataBinding>
        extends RecyclerView.ViewHolder {

    private T mBinding;

    public BindingViewHolder(T binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public T getBinding() {
        return mBinding;
    }
}
